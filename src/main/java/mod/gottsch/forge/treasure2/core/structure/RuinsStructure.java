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
package mod.gottsch.forge.treasure2.core.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.ModProcessor;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

/**
 * this is a copy of vanilla JigsawStructure since it is final
 * @author by Mark Gottschling on 8/21/2025
 */
public class RuinsStructure extends Structure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final Codec<RuinsStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(settingsCodec(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((structure) -> {
            return structure.startPool;
        }), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> {
            return structure.startJigsawName;
        }), Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> {
            return structure.maxDepth;
        }), HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> {
            return structure.startHeight;
        }), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((structure) -> {
            return structure.useExpansionHack;
        }), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((structure) -> {
            return structure.projectStartToHeightmap;
        }), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> {
            return structure.maxDistanceFromCenter;
        }), FeatureType.CODEC.fieldOf("feature_type").forGetter(structure -> {
            return (FeatureType) structure.featureType;
        }), Codec.DOUBLE.optionalFieldOf("probability", 100.0).forGetter((structure) -> {
            return structure.probability;
        }))
                .apply(instance, RuinsStructure::new);
    }), RuinsStructure::verifyRange).codec();

    protected final Holder<StructureTemplatePool> startPool;
    protected final Optional<ResourceLocation> startJigsawName;
    protected final int maxDepth;
    protected final HeightProvider startHeight;
    protected final boolean useExpansionHack;
    protected final Optional<Heightmap.Types> projectStartToHeightmap;
    protected final int maxDistanceFromCenter;
    protected final IFeatureType featureType;
    protected final Double probability;

    protected static DataResult<RuinsStructure> verifyRange(RuinsStructure structure) {
        byte adaptationDistanceModifier;
        switch (structure.terrainAdaptation()) {
            case NONE:
                adaptationDistanceModifier = 0;
                break;
            case BURY:
            case BEARD_THIN:
            case BEARD_BOX:
                adaptationDistanceModifier = 12;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        int i = adaptationDistanceModifier;
        return structure.maxDistanceFromCenter + i > 128 ? DataResult.error(() -> {
            return "Structure size including terrain adaptation must not exceed 128";
        }) : DataResult.success(structure);
    }

    public RuinsStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> templatePoolHolder, Optional<ResourceLocation> location, int maxDepth, HeightProvider heightProvider, boolean useExpansionHack,
                          Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter,
                          IFeatureType featureType, Double probability) {
        super(structureSettings);
        this.startPool = templatePoolHolder;
        this.startJigsawName = location;
        this.maxDepth = maxDepth;
        this.startHeight = heightProvider;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.featureType = featureType;
        this.probability = probability;
    }

    public RuinsStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> templatePoolHolder, int maxDepth, HeightProvider heightProvider, boolean useExpansionHack, Heightmap.Types types) {
        this(structureSettings, templatePoolHolder, Optional.empty(), maxDepth, heightProvider, useExpansionHack, Optional.of(types), 80, FeatureType.UNKNOWN, 100.0);
    }

    public RuinsStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> templatePoolHolder, int maxDepth, HeightProvider heightProvider, boolean useExpansionHack) {
        this(structureSettings, templatePoolHolder, Optional.empty(), maxDepth, heightProvider, useExpansionHack, Optional.empty(), 80, FeatureType.UNKNOWN, 100.0);
    }

    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        // probability check
		if (context.random().nextDouble() > probability) {
			Treasure.LOGGER.debug("structure does not meet generate probability.");
			return Optional.empty();
		}

        // vanilla
        ChunkPos chunkpos = context.chunkPos();
        int i = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        return JigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.maxDepth,
                blockpos,
                this.useExpansionHack,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter);
    }

    @Override
    public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox box, ChunkPos chunkPos, PiecesContainer piecesContainer) {
//        Treasure.LOGGER.debug("bounding box -> {}", box);
        // get the origin from the BoundingBox.
//        BlockPos origin = new BlockPos(box.minX(), box.minY(), box.minZ());
//        Treasure.LOGGER.debug("the origin pos is -> {}", origin);
        Treasure.LOGGER.debug("in afterPlace() for RuinsStructure.");

        // NOTE is this POST placement, so Rotations have been applied.
        //  in order to determine the key to the BlockDataCache, you would have to perform
        //  an "undo" or opposite rotation on the piece pos first.
        //  then you would have to assemble all the piece block data into a singular set/list
        //  and since that data is still PRE placement, you have to perform the Rotation on every
        //  block to get them to the right pos.
        // NOTE this method is also called 4 or 5 times for some reason with different Bounding Boxes
        //  event though the piece pos does not change. so this has to be single pass protected.

//        // TODO
//        Set<StructureTemplate.StructureBlockInfo> originalBlocks = Sets.newHashSet();
//        Set<StructureTemplate.StructureBlockInfo> processedBlocks = Sets.newHashSet();
//
        for (StructurePiece piece : piecesContainer.pieces()) {
            BoundingBox bb = piece.getBoundingBox();
            BlockPos piecePos = new BlockPos(bb.minX(), bb.minY(), bb.minZ());
            Treasure.LOGGER.debug("piece rotation -> {}", piece.getRotation());
            Treasure.LOGGER.debug("piece bb -> {}", bb);
//            for (int y= bb.minY(); y <= bb.maxY(); y++) {
//                for (int z = bb.minZ(); z <= bb.maxZ(); z++) {
//                    for (int x = bb.minX(); x <= bb.maxX(); x++) {
//                        BlockPos p = new BlockPos(x, y, z);
//                        Treasure.LOGGER.debug("piece @ {} -> {}", p, level.getLevel().getBlockState(p).getBlock());
//                    }
//                }
//            }
//            if (ModProcessor.hasFinalizeGuard(ModProcessor.DECAY, piecePos)) {
//                // add to set
//                originalBlocks.addAll(BlockDataCache.getOriginalBlocks(piecePos));
//                processedBlocks.addAll(BlockDataCache.getProcessedBlocks(piecePos));
//
//                // it is crucial to clear the cache after you are done to prevent memory leaks.
//                BlockDataCache.clear(piecePos);
//
//                Treasure.LOGGER.debug("size of original blocks -> {}", originalBlocks.size());
//            }
//
            // remove guards
            ModProcessor.removeFromGuards(piecePos);
        }
//
//        if (originalBlocks.isEmpty() || processedBlocks.isEmpty()) return;
//
//        Treasure.LOGGER.debug("processing the structure once for decay.");
////            piecesContainer.pieces().forEach(piece -> {
////                BoundingBox bb = piece.getBoundingBox();
////                BlockPos piecePos = new BlockPos(bb.minX(), bb.minY(), bb.minZ());
//////                Treasure.LOGGER.debug("rotated piece pos -> {}", BlockRotationUtil.rotateAroundPivot(piecePos, piece.ro, piece.getRotation()));
////
////                // translate back to origin to determine the original key
////                Treasure.LOGGER.debug("piece origin -> {}", piecePos);
////                var o = BlockDataCache.getOriginalBlocks(piecePos);
////                Treasure.LOGGER.debug("size of original blocks -> {}", o.size());
////                var p = BlockDataCache.getProcessedBlocks(piecePos);
////                Treasure.LOGGER.debug("size of processed blocks -> {}", p.size());
////
////            });
//
//
//        // TODO if this ever works, have to single pass protect this as well
////        // retrieve the data from the thread-safe cache using the structure's origin.
////        Set<StructureTemplate.StructureBlockInfo> originalBlocks = BlockDataCache.getOriginalBlocks(origin);
////        Set<StructureTemplate.StructureBlockInfo> processedBlocks = BlockDataCache.getProcessedBlocks(origin);
////
////        if (originalBlocks != null && processedBlocks != null) {
//        try {
//            // a set for quick O(1) lookups of block positions to be removed.
//            Set<BlockPos> removedPositions = new HashSet<>();
//
//            // create a map for efficient lookups of blocks by their position.
//            // this is necessary to check the block at y-1.
//            Map<BlockPos, StructureTemplate.StructureBlockInfo> processedBlockMap = processedBlocks.stream()
//                    .collect(Collectors.toMap(StructureTemplate.StructureBlockInfo::pos, block -> block));
//
//            // TODO originalBlocks is RELATIVE - need to put into real world pos before caching.
//            // map of original blocks for checking the "air" condition.
//            Map<BlockPos, StructureTemplate.StructureBlockInfo> originalBlockMap = originalBlocks.stream()
//                    .collect(Collectors.toMap(StructureTemplate.StructureBlockInfo::pos, block -> block));
//
//            List<StructureTemplate.StructureBlockInfo> sortedProcessed = StructureBlockSorter.sortByNaturalOrder(processedBlocks);
//
//            // iterate through the sorted list and apply the removal logic.
//            for (StructureTemplate.StructureBlockInfo currentBlockInfo : sortedProcessed) {
//                BlockPos currentPos = currentBlockInfo.pos();
//                BlockPos belowPos = currentPos.below();
//
//                // TODO special case for Treaasure Chest. It should be moved down to the first solid block
//                // check the condition for removal.
//                // condition 1: the block below was solid in the original structure but is now missing.
//                Treasure.LOGGER.debug("processing block {} -> {}", currentPos.toShortString(), processedBlockMap.get(currentPos).state().getBlock());
//
//                // does the processed block match the real world?
//                BlockState rwState = level.getLevel().getBlockState(currentPos);
//                Treasure.LOGGER.debug("real world block -> {}", rwState.getBlock());
//
//                Treasure.LOGGER.debug("below processed @ {} -> {}", belowPos.toShortString(), processedBlockMap.get(belowPos));
//                Treasure.LOGGER.debug("below original @ {} -> {}", belowPos.toShortString(), originalBlockMap.get(belowPos));
//
//                BlockState belowProcessed = processedBlockMap.get(belowPos).state();
//                if (processedBlockMap.get(currentPos) != null && !processedBlockMap.get(currentPos).state().isAir()) {
//                    boolean wasBelowSolidAndIsNowAir = originalBlockMap.containsKey(belowPos) && belowProcessed.isAir();
//                    Treasure.LOGGER.debug("wasBelowSolidAndIsNowAir -> {}", wasBelowSolidAndIsNowAir);
//                    // condition 2: the block below is already marked for removal.
//                    boolean isBelowRemoved = removedPositions.contains(belowPos);
//                    Treasure.LOGGER.debug("isBelowRemoved -> {}", removedPositions.contains(belowPos));
//
//                    // TODO currentPos is not the right rotation!! how the heck?
//                    if (wasBelowSolidAndIsNowAir || isBelowRemoved) {
//                        Treasure.LOGGER.debug("adding {} {} to removed", currentPos, processedBlockMap.get(currentPos).state().getBlock());
//                        removedPositions.add(currentPos);
//                    }
//                }
//            }
//
//            Treasure.LOGGER.debug("removed positions size -> {}", removedPositions.size());
//
//            // finally iterate the removedPositions and replace them with air.
//////            List<StructureTemplate.StructureBlockInfo> finalProcessedList = processedBlocks.stream()
//////                    .filter(block -> !removedPositions.contains(block.pos()))
//////                    .toList();
//            removedPositions.forEach(removed -> {
//                Treasure.LOGGER.debug("removing {} pcd-{} rw-{}", removed, processedBlockMap.get(removed).state().getBlock(), level.getLevel().getBlockState(removed).getBlock());
//
//                level.getLevel().setBlockAndUpdate(removed, Blocks.AIR.defaultBlockState());
//            });
//        } catch (Exception e) {
//            Treasure.LOGGER.error("error ->", e);
//            throw new RuntimeException(e);
//
//        }
//
//
//        } else {
//            Treasure.LOGGER.debug("no block data found for final comparison for structure at -> {}", origin);
//        }
//
//
//        // remove any references from the single pass guards
//        ModProcessor.removeFromGuards(origin);

        super.afterPlace(level, structureManager, chunkGenerator, random, box, chunkPos, piecesContainer);
    }

    public StructureType<?> type() {
        return StructureType.JIGSAW;
    }
}
