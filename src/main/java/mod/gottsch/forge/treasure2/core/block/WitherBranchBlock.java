/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.gottschcore.block.FacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 
 * @author Mark Gottschling on Mar 26, 2018
 *
 */
public class WitherBranchBlock extends FacingBlock implements ITreasureBlock {

	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] shapes = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherBranchBlock(Block.Properties properties) {
		super(properties);
		setShapes(new VoxelShape[] {
				Block.box(4, 0, 0, 12, 8, 16),	// S
				Block.box(0, 0, 4, 16, 8, 12),	// W
				Block.box(4, 0, 0, 12, 8, 16),	// N
				Block.box(0, 0, 4, 16, 8, 12)	// E
		});
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH:
			return shapes[0];
		case EAST:
			return shapes[1];
		case SOUTH:
			return shapes[2];
		case WEST:
			return shapes[3];
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getShapes() {
		return shapes;
	}

	public WitherBranchBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}

}