/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
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
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
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
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author Mark Gottschling on Oct 12, 2023
 *
 */
public class DeferredWitherTreeGeneratorBlockEntity extends BlockEntity implements IChestFeature {

    private IRarity rarity;

    public DeferredWitherTreeGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.DEFERRED_WITHER_TREE_GENERATOR_ENTITY_TYPE.get(), pos, state);
    }

    public void tickServer() {
        if (Config.SERVER.markers.enableSpawner.get()) {
            if (getLevel().isClientSide()) {
                return;
            }

            try {
                // TODO this currenlty is hardcoded for the the wither tree generator. this should use the wither tree feature select first

                IFeatureType FEATURE_TYPE = FeatureType.TERRANEAN;
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

                IFeatureGenerator featureGenerator = TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR;
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
                    cacheGeneratedChest((ServerLevel)getLevel(), rarity, FEATURE_TYPE, chestCache, result.get());
                    updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
                } else {
                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
                    return;
                }

                // save world data
                TreasureSavedData savedData = TreasureSavedData.get(getLevel());
                if (savedData != null) {
                    savedData.setDirty();
                }

            } catch(Exception e) {
                Treasure.LOGGER.error("unable to generate wither tree", e);
            } finally {
                if (getLevel().getBlockState(getBlockPos()).getBlock() == TreasureBlocks.DEFERRED_WITHER_TREE_GENERATOR.get()) {
                    getLevel().setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("rarity", getRarity().getName());
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

            setRarity(rarity.orElse(Rarity.NONE));
        } catch (Exception e) {
            Treasure.LOGGER.error("error reading to tag:", e);
        }
    }

    public IRarity getRarity() {
        return rarity;
    }

    public void setRarity(IRarity rarity) {
        this.rarity = rarity;
    }

	@Override
	public boolean meetsProximityCriteria(ServerLevelAccessor world, ResourceLocation dimension, IFeatureType key,
			ICoords spawnCoords, int minDistance) {
		// TODO Auto-generated method stub
		return false;
	}
}
