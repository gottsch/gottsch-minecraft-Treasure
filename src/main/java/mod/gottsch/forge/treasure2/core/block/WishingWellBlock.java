/*
 * This file is part of  Treasure2.
 * Copyright (c) 2014 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


/**
 * @author Mark Gottschling on Sep 19, 2014
 *
 */
public class WishingWellBlock extends Block implements IWishingWellBlock {

    VoxelShape shape = Block.box(0, 0, 0, 16, 16, 16);

    /**
     * 
     * @param properties
     */
	public WishingWellBlock(Block.Properties properties) {
		super(properties);
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
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return shape;
    }
}