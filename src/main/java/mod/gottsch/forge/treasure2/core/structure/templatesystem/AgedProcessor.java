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


import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.StructureNeighborDependentStateMarkerBlockEntity;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * A custom processor for Well structures to handle blocks
 * that need to retain their state, such as Stair and Wall blocks.
 * @author by Mark Gottschling on 8/15/2025
 */
public class AgedProcessor extends StructureProcessor {

    public static final Codec<AgedProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("mossiness").forGetter(processor -> processor.mossiness)
    ).apply(instance, AgedProcessor::new));

    // TODO change to agedness
    private final float mossiness;

    private static final Map<Block, Block> STAIR_REPLACEMENTS = ImmutableMap.of(
            Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS,
            Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS,
            Blocks.STONE_STAIRS, Blocks.COBBLESTONE_STAIRS,
            Blocks.POLISHED_ANDESITE_STAIRS, Blocks.ANDESITE_STAIRS
            // TODO do more
    );

    private static final Map<Block, Block> SLAB_REPLACEMENTS = ImmutableMap.of(
            Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB,
            Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB,
            Blocks.STONE_SLAB, Blocks.COBBLESTONE_SLAB,
            Blocks.POLISHED_ANDESITE_SLAB, Blocks.ANDESITE_SLAB
            // TODO do more
    );

    /**
     *
     * @param mossiness
     */
    public AgedProcessor(float mossiness) {
        this.mossiness = mossiness;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings settings) {
        try {
            BlockState currentState = current.state();
            BlockState newState = null;

            if (currentState.is(BlockTags.STAIRS)) {
                if (currentState.is(Blocks.STONE_BRICK_STAIRS)) {
                    newState = maybeReplaceStairs(settings.getRandom(current.pos()), currentState, Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState());
                } else if (currentState.is(Blocks.COBBLESTONE_STAIRS)) {
                    newState = maybeReplaceStairs(settings.getRandom(current.pos()), currentState, Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState());
                }

                return newState != null ? new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt()) : current;
            }
            else if (currentState.is(BlockTags.SLABS)) {
                Block replacementBlock = SLAB_REPLACEMENTS.get(currentState.getBlock());
                if (replacementBlock != null) {
                    newState = maybeReplaceSlab(settings.getRandom(current.pos()), currentState, replacementBlock.defaultBlockState());
                    if (newState != null) {
                        return new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt());
                    }
                }
                return current;
            }
            // check if the block is a cobblestone wall and if a random chance passes
            else if (currentState.is(BlockTags.WALLS)) {

                if (currentState.is(Blocks.COBBLESTONE_WALL)) {
                    newState = maybeReplaceWall(settings.getRandom(current.pos()), currentState, Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState());
                } else if (currentState.is(Blocks.STONE_BRICK_WALL)) {
                    newState = maybeReplaceWall(settings.getRandom(current.pos()), currentState, Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState());
                }

                // return the new info with the NBT tag
                return Optional.ofNullable(newState)
                        .map(state -> {
                            CompoundTag nbt = new CompoundTag();
                            nbt.putString(StructureNeighborDependentStateMarkerBlockEntity.TARGET_BLOCK,
                                    ModUtil.getName(state.getBlock()).toString());
                            return new StructureTemplate.StructureBlockInfo(
                                    current.pos(),
                                    TreasureBlocks.STRUCTURE_NEIGHBOR_DEPENDENT_STATE_MARKER.get().defaultBlockState(),
                                    nbt
                            );
                        })
                        .orElse(current);
            }
            return current;

        } catch(Exception e) {
            Treasure.LOGGER.error(e);
            throw e;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.AGED_PROCESSOR.get();
    }

    @Nullable
    private BlockState maybeReplaceStairs(RandomSource randomSource, BlockState state, BlockState newState) {
        if (randomSource.nextFloat() >= mossiness) {
            return null;
        } else {
            Direction direction = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            return newState.setValue(StairBlock.FACING, direction).setValue(StairBlock.HALF, half);
        }
    }

    private BlockState maybeReplaceSlab(RandomSource randomSource, BlockState state, BlockState newState) {
        SlabType type = state.getValue(SlabBlock.TYPE);
        return randomSource.nextFloat() < mossiness ? newState.setValue(SlabBlock.TYPE, type) : null;
    }

    private BlockState maybeReplaceWall(RandomSource randomSource, BlockState state, BlockState newState) {
        return randomSource.nextFloat() < mossiness ? newState : null;
    }
}
