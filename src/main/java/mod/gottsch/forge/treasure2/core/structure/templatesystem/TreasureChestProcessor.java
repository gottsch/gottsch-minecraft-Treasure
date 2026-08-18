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
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.ChestGeneration;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.IChestSubprocessor;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.TreasureChestSubprocessors;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
                // update the matching chest with dimension value. Shared with the API path, so a
                // chest placed by another mod completes its cache entry the same way this one does.
                processedBlocks.forEach(info -> {
                    if (info.state().getBlock() instanceof ITreasureChestBlock) {
                        ChestGeneration.finalizeChest(levelAccessor, info.pos());
                    }
                });
            }
        }
        return super.finalizeProcessing(levelAccessor, piecePos, originalPos, blocks, processedBlocks, placeSettings);
    }


    /**
     * Delegates to {@link ChestGeneration}, which is also what {@code TreasureApi.generateChest}
     * calls. The six steps that used to live here moved there when the API was added, so a chest
     * placed by another mod's structure is the same chest this processor places -- there is nothing
     * for the two paths to drift on.
     *
     * <p>The facing is resolved here rather than there: this path has an authored chest state and a
     * rotation, and {@code ChestGeneration} takes the direction the finished chest ends up pointing.
     * Rotating first and passing the result is what makes the two callers equivalent.</p>
     */
    public StructureTemplate.StructureBlockInfo buildTreasureChest(LevelReader levelReader, BlockState state, BlockPos pos, StructurePlaceSettings placeSettings) {
        Direction facing = placeSettings.getRotation().rotate(state.getValue(StandardChestBlock.FACING));
        return ChestGeneration.generate(levelReader, pos, placeSettings.getRandom(pos), facing,
                this.featureType, Optional.empty(), getLootTable());
     }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

}
