/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.ModBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;

/**
 * @author Mark Gottschling
 *
 */
public class DesertWishingWellBlock extends ModBlock implements IWishingWellBlock {
    
    VoxelShape shape = Block.box(0, 0, 0, 16, 16, 16);

	/**
	 * 
	 * @param material
	 */
	public DesertWishingWellBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
	}

    /**
     * 
     * @param state
     * @param worldIn
     * @param pos
     * @param context
     * @return
     */
    @Override
	public VoxelShape getShape(BlockState state, LevelAccessor worldIn, BlockPos pos, CollisionContext context) {
		return shape;
    }
}