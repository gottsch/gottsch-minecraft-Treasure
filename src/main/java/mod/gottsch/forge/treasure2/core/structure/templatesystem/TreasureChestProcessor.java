/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.structure.templatesystem;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.Rarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeightsManager;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.ChestSubprocessorDataRegistry;
import mod.gottsch.forge.treasure2.core.cache.TreasureChestCache;
import mod.gottsch.forge.treasure2.core.cache.data.TreasureChestCacheData;
import mod.gottsch.forge.treasure2.core.structure.BlockRotationUtil;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.IChestSubprocessor;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.TreasureChestSubprocessors;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author by Mark Gottschling on 8/15/2025
 */
public class TreasureChestProcessor extends VanillaChestProcessor {

    // NOTE cannot pass data via a custom PlaceSettings in build 47.3.0. Has to come from the json definition.
    protected FeatureType featureType;
    @Deprecated
    protected ResourceLocation dimension;

    // property to capture the size of the current piece
    private Vec3i size;

    // cached info
    // used to cache the structure info of the chest so if multiple passes occur.
    // the cached info can be used instead of replaced with the original/current info.
    private StructureTemplate.StructureBlockInfo cachedInfo;

    public static final Codec<TreasureChestProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FeatureType.CODEC.fieldOf("feature_type").forGetter(TreasureChestProcessor::getFeatureType),
            ResourceLocation.CODEC.optionalFieldOf("dimension", null).forGetter(TreasureChestProcessor::getDimension),
            ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(VanillaChestProcessor::getLootTable)
    ).apply(instance, TreasureChestProcessor::new));



    public TreasureChestProcessor(FeatureType featureType) {
        this(featureType, ModUtil.asLocation("minecraft:overworld"),Optional.empty()
        );
    }

    public TreasureChestProcessor(FeatureType featureType, ResourceLocation dimension, Optional<ResourceLocation> lootTable) {
        super(lootTable);
        this.featureType = featureType;
        this.dimension = dimension;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.CHEST_PROCESSOR.get();
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos piecePos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {
        // Origin=current.pos()−relativePos

        if (!(current.state().getBlock() instanceof ITreasureChestBlock)) {
            return current;
        }

//        Treasure.LOGGER.debug("Chest processBlock called on piece pos -> {}", piecePos);
//        Treasure.LOGGER.debug("piece rotation -> {}", placementSettings.getRotation());
//        Treasure.LOGGER.debug("Chest processBlock called on relative pos -> {}", relativePos);
//        Treasure.LOGGER.debug("Chest processBlock called on current.pos -> {}", current.pos());

        // get real world piece pos
        BlockPos newPiecePos = BlockRotationUtil.transformStartCoords(piecePos, this.size, placementSettings.getRotation());
        Treasure.LOGGER.debug("attempting process called on piece pos -> {}", newPiecePos);

        // NOTE this assumes that the structure has only 1 treasure chest present
        // TODO origin is the unrotated piece pos... rotate it first and this is incorrect pos.
//        BlockPos origin = piecePos.subtract(relativePos);
        if (addProcessGuard(TREASURE_CHEST, newPiecePos)) {
            Treasure.LOGGER.debug("single pass processBlock called on pos -> {}", newPiecePos);
            StructureTemplate.StructureBlockInfo info = buildTreasureChest(levelReader, current.state(), current.pos(), placementSettings);
            Treasure.LOGGER.debug("returned info -> {}", info);
            cachedInfo = info;
            return info;
        }

        // return the cached info if it exists
        return cachedInfo != null ? cachedInfo : current;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos piecePos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
        this.size = template.getSize();
        return super.process(levelReader, piecePos, relativePos, original, current, placeSettings, template);
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor levelAccessor, BlockPos piecePos, BlockPos originalPos, List<StructureTemplate.StructureBlockInfo> blocks, List<StructureTemplate.StructureBlockInfo> processedBlocks, StructurePlaceSettings placeSettings) {
 // NOTE this is the piece, not the entire structure

//        Treasure.LOGGER.debug("what is piece pos -> {}", piecePos);
//        Treasure.LOGGER.debug("what is original pos? -> {}", originalPos);
//        Treasure.LOGGER.debug("rotation -> {}", placeSettings.getRotation());

        BlockPos newPiecePos = BlockRotationUtil.transformStartCoords(piecePos, this.size, placeSettings.getRotation());
//        Treasure.LOGGER.debug("finalize processing called on piece pos -> {}", newPiecePos);

        if (hasProcessGuard(TREASURE_CHEST, newPiecePos)) {
            if (addFinalizeGuard(TREASURE_CHEST, newPiecePos)) {
                Treasure.LOGGER.debug("finalize processing called on pos -> {}", piecePos);
                processedBlocks.forEach(info -> {
                    if (info.state().getBlock() instanceof ITreasureChestBlock) {

                        // update the matching chest with dimension value
                        TreasureChestCache.getCache().stream()
                                .filter(chest -> chest.getCoords().equals(Coords.of(info.pos())))
                                .filter(chest -> chest.getBiomeName().equals(ModUtil.getName(levelAccessor.getBiome(info.pos()))))
                                .filter(chest -> chest.getDimensionName() == null)
                                .findFirst().ifPresent(chest -> {
                                    chest.setDimensionName(levelAccessor.dimensionType().effectsLocation());
                                    // mark the persistence data as dirty
                                    TreasureSavedData.get(levelAccessor.getLevel()).setDirty();
                                });
                    }
                });
            }
        }
        return super.finalizeProcessing(levelAccessor, piecePos, originalPos, blocks, processedBlocks, placeSettings);
    }


    public StructureTemplate.StructureBlockInfo buildTreasureChest(LevelReader levelReader, BlockState state, BlockPos pos, StructurePlaceSettings placeSettings) {
        RandomSource random = placeSettings.getRandom(pos);

        // 1. map feature type to either TERRANEAN or AQUATIC.
        FeatureType processedFeatureType = this.featureType == FeatureType.AQUATIC ? this.featureType : FeatureType.TERRANEAN;

        // 2. get rarity and ensure it's a valid entry.
        Optional<Rarity> rarityOptional = Optional.ofNullable(RarityWeightsManager.getNextRarity(processedFeatureType))
                .filter(rarity -> rarity != Rarity.NONE)
                .filter(rarity -> rarity != TreasureRarities.UNKNOWN.get());

        IRarity rarityEntry = rarityOptional.orElseGet(() -> {
            Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}, reverting to default rarity.", processedFeatureType);
            return (Rarity) TreasureRarities.COMMON.get();
        });
        Treasure.LOGGER.debug("rarity -> {}", rarityEntry);

        // 3. get chest subprocessor data.
        Optional<ChestSubprocessorData> dataOptional = ChestSubprocessorDataRegistry.getAssociation(processedFeatureType, rarityEntry);
        ChestSubprocessorData data = dataOptional.orElseGet(() -> {
            Treasure.LOGGER.warn("unable to locate chest subprocessor data for feature type -> {} and rarity -> {}, reverting to default subprocessor data.", processedFeatureType, rarityEntry.getName());
            // no data pack entry for this (feature type, rarity) pair - build a safe standard default
            // so nothing downstream NPEs on a null ChestSubprocessorData.
            ResourceLocation rarityId = TreasureRarities.getKey(rarityEntry).orElseGet(() -> new ResourceLocation(Treasure.MODID, rarityEntry.getName()));
            return new ChestSubprocessorData(TreasureChestSubprocessors.STANDARD.getId(), rarityId, 0.0,
                    List.of(), List.of(), List.of());
        });

        // 4. get a chest subprocessor.
        Optional<IChestSubprocessor> subprocessorOptional = TreasureChestSubprocessors.getChestSubprocessor(data.getType());
        IChestSubprocessor subprocessor = subprocessorOptional.orElseGet(() -> {
            Treasure.LOGGER.warn("unable to locate chest subprocessor for processor type -> {}, reverting to default subprocessor", data.getType());
            return TreasureChestSubprocessors.STANDARD.get();
        });

        // 5. set properties and process the subprocessor to get the StructureBlockInfo.
        subprocessor.setFeatureType(processedFeatureType);
        subprocessor.setData(data);
        Treasure.LOGGER.debug("original chest is facing -> {}", state.getValue(StandardChestBlock.FACING));
        Optional<StructureTemplate.StructureBlockInfo> infoOptional = subprocessor
                .process(levelReader, placeSettings.getRandom(pos), state, pos,  placeSettings.getRotation(), rarityEntry, data);

        // 6. if the block info is present, cache the data and return it. Otherwise, return null.
        return infoOptional.map(info -> {
            Treasure.LOGGER.debug("info -> {}", info);
            TreasureChestCacheData chestSpawn = new TreasureChestCacheData();
            chestSpawn.setChestName(ModUtil.getName(info.state().getBlock()));
            chestSpawn.setCoords(Coords.of(info.pos()));
//            chestSpawn.setDimensionName(dimension);
            chestSpawn.setBiomeName(ModUtil.getName(levelReader.getBiome(info.pos())));
            chestSpawn.setFeatureType(getFeatureType()); // NOTE the original feature type to describe what feature spawned the chest
            chestSpawn.setRarity(rarityEntry);
            chestSpawn.setDiscovered(false);
            TreasureChestCache.cache(chestSpawn);
            Treasure.LOGGER.debug("caching chest at pos -> {}", info.pos());

            // increment rarity weighted collection - move to placement
            RarityWeightsManager.adjustAllWeightsExcept(featureType, 1, rarityEntry);

            return info;
        }).orElseGet(() -> {
            Treasure.LOGGER.warn("unable to generate StructureBlockInfo for processor type -> {}, reverting to default chest", data.getType());
            return subprocessor.defaultChest(levelReader, state, pos, placeSettings);
            // TODO needs to cache the chest here as well.
        });
     }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

}
