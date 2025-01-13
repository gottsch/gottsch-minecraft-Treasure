/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
 *
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureGenContext;
import mod.gottsch.forge.treasure2.core.world.feature.IChestFeature;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 *
 * @author Mark Gottschling on July 28, 2024
 *
 */
public abstract class DeferredGeneratorBlockEntity extends BlockEntity implements IChestFeature {
    private long generatedTime;
    private IRarity rarity;

    public DeferredGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract boolean isEnabled();

    public abstract IFeatureGenerator getFeatureGenerator();

    /**
     *
     */
    public void tickServer() {
        if (isEnabled() && generatedTime == 0) {
            if (getLevel().isClientSide()) {
                return;
            }

            if (getLevel().getChunkAt(this.getBlockPos()).getStatus().getChunkType() != ChunkStatus.ChunkType.LEVELCHUNK) {
                Treasure.LOGGER.debug("Not a CHUNKLEVEL - don't tick yet.");
                return;
            }

            try {
                Treasure.LOGGER.debug("executing tick on server for deferred generator BE");
                IFeatureType FEATURE_TYPE = getFeatureType();
                ResourceLocation dimension = WorldInfo.getDimension(getLevel());
                ICoords spawnCoords = new Coords(getBlockPos());

                // get the chest registry
                GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
                if (chestCache == null) {
                    Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRANEAN. This shouldn't be. Should be initialized.");
                    return;
                }

                // get the generator config
                ChestFeaturesConfiguration config = Config.chestConfig;
                if (config == null) {
                    Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
                    return;
                }

                ChestFeaturesConfiguration.Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
                if (generatorConfig == null) {
                    Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
                    return;
                }

                // TODO this could be selected from a feature selector
                IFeatureGenerator featureGenerator = getFeatureGenerator();
                Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());

                Optional<ChestFeaturesConfiguration.ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
                if (!rarityConfig.isPresent()) {
                    Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
                    return;
                }
                // call generate
                Optional<GeneratorResult<ChestGeneratorData>> result =
                        featureGenerator.generate(
                                new FeatureGenContext(
                                        (ServerLevel)getLevel(),
                                        ((ServerLevel)getLevel()).getChunkSource().getGenerator(),
                                        getLevel().getRandom(),
                                        FEATURE_TYPE),
                                spawnCoords, rarity, rarityConfig.get());

                if (result.isPresent()) {
                    Treasure.LOGGER.debug("generated and caching chest");
                    cacheGeneratedChest((ServerLevel)getLevel(), rarity, FEATURE_TYPE, chestCache, result.get());
                    updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
                } else {
                    Treasure.LOGGER.debug("failed to generate and placing placeholder");
                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
                    return;
                }

                // save world data
                TreasureSavedData savedData = TreasureSavedData.get(getLevel());
                if (savedData != null) {
                    savedData.setDirty();
                }
                // TODO set a status that gen is complete instead of waiting for x time.

            } catch(Exception e) {
                Treasure.LOGGER.error("unable to generate feature", e);
            } finally {
                /*
                 * can't self-destruct on server without getting a "Can't load DUMMY" spam message.
                  */
                generatedTime = getLevel().getGameTime();
            }
        }

        if (generatedTime > 0 && getLevel().getGameTime() > generatedTime + 40) {
            if (getLevel().getBlockState(getBlockPos()).getBlock() == getBlockState().getBlock()) {
                Treasure.LOGGER.debug("destroying self ie set to air -> {}", this.getBlockPos().toShortString());
                getLevel().setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
            }
            this.setRemoved();
        }
    }

    public abstract IFeatureType getFeatureType();

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("rarity", getRarity().getName());
        tag.putLong("generatedTime", generatedTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        try {
            Optional<IRarity> rarity = Optional.empty();
            if (tag.contains("rarity")) {
                rarity = TreasureApi.getRarity(tag.getString("rarity"));
            }
            if (tag.contains("generatedTime")) {
                generatedTime = tag.getLong("generatedTime");
            }
            setRarity(rarity.orElse(Rarity.NONE));
        } catch (Exception e) {
            Treasure.LOGGER.error("error reading to tag:", e);
        }
    }

    @Override
    public boolean meetsProximityCriteria(ServerLevelAccessor world, ResourceLocation dimension, IFeatureType key,
                                          ICoords spawnCoords, int minDistance) {
        // TODO Auto-generated method stub
        return false;
    }

    public IRarity getRarity() {
        return rarity;
    }

    public void setRarity(IRarity rarity) {
        this.rarity = rarity;
    }

    public long getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(long generatedTime) {
        this.generatedTime = generatedTime;
    }
}
