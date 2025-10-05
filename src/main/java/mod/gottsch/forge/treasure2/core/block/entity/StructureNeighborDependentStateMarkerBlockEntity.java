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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.SyntheticBlockPlaceContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

/**
 * This class is meant to be used in Structure NBTs only as a marker
 * for where blocks that contain neighbor-dependent state will be placed.
 * ex. for Walls, etc.
 * @author by Mark Gottschling on 8/15/2025
 */
public class StructureNeighborDependentStateMarkerBlockEntity extends BlockEntity {
    public static final String TARGET_BLOCK = "targetBlock";

    private ResourceLocation targetBlock;
    private boolean processed = false;

    public StructureNeighborDependentStateMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.STRUCTURE_NEIGHBOR_DEPENDENT_STATE_MARKER.get(), pos, state);
    }

    public StructureNeighborDependentStateMarkerBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
         super(entityType, pos, state);
    }

    public void tickServer() {
        if (level.isClientSide() || this.processed) {
            return;
        }

        this.processed = true;

        Optional.ofNullable(this.targetBlock)
                .map(id -> ForgeRegistries.BLOCKS.getValue(id))
                .map(Block::defaultBlockState)
                .filter(state -> state.is(BlockTags.WALLS))
                .map(state -> {
                    SyntheticBlockPlaceContext context = new SyntheticBlockPlaceContext(level, getBlockPos());
                    return state.getBlock().getStateForPlacement(context);
                })
                .ifPresent(updatedState -> level.setBlock(getBlockPos(), updatedState, 3));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        try {
            // read the custom name
            if (tag.contains(TARGET_BLOCK)) {
                this.targetBlock = ModUtil.asLocation(tag.getString(TARGET_BLOCK));
            }
        } catch (Exception e) {
            Treasure.LOGGER.error("error reading StructureNeighborDependentStateMarkerBlockEntity properties from tag:", e);
        }
    }

    /**
     *
     */
    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        Optional.ofNullable(getTargetBlock())
                .ifPresent(block -> nbt.putString(TARGET_BLOCK, block.toString()));
    }

    public ResourceLocation getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(ResourceLocation targetBlock) {
        this.targetBlock = targetBlock;
    }
}
