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
package mod.gottsch.forge.treasure2.core.block;


import mod.gottsch.forge.treasure2.core.block.entity.StructureNeighborDependentStateMarkerBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author by Mark Gottschling on 8/15/2025
 */
public class StructureNeighborDependentStateMarkerBlock extends BaseEntityBlock implements ITreasureBlock {

    public StructureNeighborDependentStateMarkerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StructureNeighborDependentStateMarkerBlockEntity(
                TreasureBlockEntities.STRUCTURE_NEIGHBOR_DEPENDENT_STATE_MARKER.get(), pos, state);
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof StructureNeighborDependentStateMarkerBlockEntity entity) {
                entity.tickServer();
            }
        };
    }
}
