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
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.structure.BlockRotationUtil;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.BlockDataCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a processor to capture the original block data of a structure, by individual piece.
 * this processor must be placed at the end of the processor list in the JSON file.
 *
 * @author by Mark Gottschling on 9/9/2025
 */
public class DecayProcessor extends ModProcessor {

    // codec
    public static final Codec<DecayProcessor> CODEC = Codec.unit(DecayProcessor::new);
    // property to capture the size of the current piece
    private Vec3i size;

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.DECAY_PROCESSOR.get();
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {
        // capture the original block info before any other processor has a chance to change it.
//        this.originalBlocks.add(original);
//
//        // return the result from the previous processor to allow the chain to continue.
        return current;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(LevelReader p_74140_, BlockPos p_74141_, BlockPos p_74142_, StructureTemplate.StructureBlockInfo p_74143_, StructureTemplate.StructureBlockInfo p_74144_, StructurePlaceSettings p_74145_, @Nullable StructureTemplate template) {
        this.size = template.getSize();
        return super.process(p_74140_, p_74141_, p_74142_, p_74143_, p_74144_, p_74145_, template);
    }

    /*
     * this method is called after all other processors have finished for this specific piece.
     */
    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor levelAccessor, BlockPos piecePos, BlockPos originalPos, List<StructureTemplate.StructureBlockInfo> blocks, List<StructureTemplate.StructureBlockInfo> processedBlocks, StructurePlaceSettings placeSettings) {

//        Treasure.LOGGER.debug("original should be the same between calls. -> {}", originalPos);
        Treasure.LOGGER.debug("what is piece pos -> {}", piecePos);
//        Treasure.LOGGER.debug("what is original pos? -> {}", originalPos);
        Treasure.LOGGER.debug("decay rotation -> {}", placeSettings.getRotation());
//        BlockPos newPiecePos = BlockRotationUtil.transformStartCoords(piecePos, this.size, placeSettings.getRotation());
//      Treasure.LOGGER.debug("what is rotated piece pos -> {}", newPiecePos);
//        Treasure.LOGGER.debug("rotation pivot -> {}", placeSettings.getRotationPivot());
//        Treasure.LOGGER.debug("rotated piece pos around pivot -> {}", BlockRotationUtil.rotateAroundPivot(piecePos, placeSettings.getRotationPivot(), placeSettings.getRotation()));
//        Treasure.LOGGER.debug("rotated piece pos around origin-> {}", BlockRotationUtil.rotateAroundPivot(piecePos, originalPos, placeSettings.getRotation()));

        //        Treasure.LOGGER.debug("rotated origin pos -> {}", BlockRotationUtil.rotateAroundPivot(originalPos, placeSettings.getRotationPivot(), placeSettings.getRotation()));
        BlockPos newPiecePos = BlockRotationUtil.transformStartCoords(piecePos, this.size, placeSettings.getRotation());

        // allow to execute once on the piece
        if (!hasFinalizeGuard(DECAY, newPiecePos)) {
            addFinalizeGuard(DECAY, newPiecePos);

            Treasure.LOGGER.debug("finalize processing called on piece pos -> {}", newPiecePos);

            // NOTE this class is PRE placement, so the blocks aren't rotated yet, nor is there a piece size info available from this call
            //  the template size was captured in process(), so we can calculate the rotated pos by using the piece's pos, the size,
            //  and the rotation value provided from placeSettings.

            // NOTE further testing seems to indicate that the blocks ARE rotated, but the piecePos is the ORIGINAL UNROTATED pos.
            //  so only the piecePos needs to be rotated to calculate the correct key.

            // get the existing sets from the cache, or create new ones if this is the first piece.
            Set<StructureTemplate.StructureBlockInfo> currentOriginals = BlockDataCache.getOriginalBlocks(newPiecePos);
            // stream all blocks and rotate
            currentOriginals.addAll(blocks.stream()
                    .map(block -> {
                        BlockPos absPos = block.pos().offset(piecePos);
//                        BlockPos rotatedPos = BlockRotationUtil.transformStartCoords(absPos, this.size, placeSettings.getRotation());
                        BlockPos rotatedPos = BlockRotationUtil.rotateAroundPivot(absPos, piecePos, placeSettings.getRotation());
                        Treasure.LOGGER.debug("rotate decay original from -> {} to -> {}", absPos, rotatedPos);
                        return new StructureTemplate.StructureBlockInfo(rotatedPos, block.state(), block.nbt());
                    })
                    .collect(Collectors.toSet()));
            BlockDataCache.putOriginalBlocks(newPiecePos, currentOriginals);

            Set<StructureTemplate.StructureBlockInfo> currentProcessed = BlockDataCache.getProcessedBlocks(newPiecePos);
            // stream all blocks and rotate
            currentProcessed.addAll(
                    processedBlocks.stream()
                            .map(processedBlock -> {
//                                BlockPos rotatedPos = BlockRotationUtil.transformStartCoords(block.pos(), this.size, placeSettings.getRotation());
//                                BlockPos rotatedPos = BlockRotationUtil.rotateAroundPivot(block.pos(), piecePos, placeSettings.getRotation());
                                BlockPos rotatedPos = processedBlock.pos();
                                Treasure.LOGGER.debug("decay processed -> {}", rotatedPos);
//                                Treasure.LOGGER.debug("rotate decay processed from -> {} to -> {}", block.pos(), rotatedPos);
                                return new StructureTemplate.StructureBlockInfo(rotatedPos, processedBlock.state(), processedBlock.nbt());
                            })
                            .collect(Collectors.toSet()));
            BlockDataCache.putProcessedBlocks(newPiecePos, currentProcessed);

            // return the final block list for placement.
            return processedBlocks;

            /// //////////////////////////////

//            NOTE this section is moved to the RuinsStructure.afterPlace()
//            TODO 9/11/25 bring back processing here. can add each piece's processed list
//              and sort and re-calculate. then don't have to touch afterPlace but once to clean up
//
//            // a set for quick O(1) lookups of block positions to be removed.
//            Set<BlockPos> removedPositions = new HashSet<>();
//
//            // create a map for efficient lookups of blocks by their position.
//            // this is necessary to check the block at y-1.
//            Map<BlockPos, StructureTemplate.StructureBlockInfo> processedBlockMap = processedBlocks.stream()
//                    .collect(Collectors.toMap(StructureTemplate.StructureBlockInfo::pos, block -> block));
//
//            // map of original blocks for checking the "air" condition.
//            Map<BlockPos, StructureTemplate.StructureBlockInfo> originalBlockMap = blocks.stream()
//                    .collect(Collectors.toMap(StructureTemplate.StructureBlockInfo::pos, block -> block));
//
//            List<StructureTemplate.StructureBlockInfo> sortedProcessed = StructureBlockSorter.sortByNaturalOrder(processedBlocks);
//
//            // iterate through the sorted list and apply the removal logic.
//            for (StructureTemplate.StructureBlockInfo currentBlockInfo : sortedProcessed) {
//                BlockPos currentPos = currentBlockInfo.pos();
//                BlockPos belowPos = currentPos.below();
//
//                // check the condition for removal.
//                // condition 1: the block below was solid in the original structure but is now missing.
//                boolean wasBelowSolidAndIsNowAir = originalBlockMap.containsKey(belowPos) && !processedBlockMap.containsKey(belowPos);
//                // condition 2: the block below is already marked for removal.
//                boolean isBelowRemoved = removedPositions.contains(belowPos);
//
//                if (wasBelowSolidAndIsNowAir || isBelowRemoved) {
//                    removedPositions.add(currentPos);
//                }
//            }
//            // finally iterate the processed and remove all from the removePositions list
//            List<StructureTemplate.StructureBlockInfo> finalProcessedList = processedBlocks.stream()
//                    .filter(block -> !removedPositions.contains(block.pos()))
//                    .toList();
//            return finalProcessedList;
        }
        return super.finalizeProcessing(levelAccessor, piecePos, originalPos, blocks, processedBlocks, placeSettings);
//        return processedBlocks;
    }
}
