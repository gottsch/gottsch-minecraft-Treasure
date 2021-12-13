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

import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * @author Mark Gottschling on Aug 4, 2021
 *
 */
public class TreasureShapeBuilder {

	/**
	 * 
	 * @return
	 */
	public static VoxelShape[] buildGravestone1() {
		VoxelShape northSouth = VoxelShapes.or(
				Block.box(2D, 0D, 6D, 14D, 10D, 10D),
				Block.box(4D, 10D, 6D, 12D, 12D, 10D)
				);
		
		VoxelShape eastWest = VoxelShapes.or(
				Block.box(6D, 0D, 2D, 10D, 10D, 14D),
				Block.box(6D, 10D, 4D, 10D, 12D, 12D)
				);
		
		return new VoxelShape[] {
				northSouth,	// S
				eastWest,	// W
				northSouth,	// N
				eastWest,	// E
	    };
	}
	
	public static VoxelShape[] buildGravestone2() {
		VoxelShape northSouth = VoxelShapes.or(
				Block.box(2D, 0D, 5D, 14D, 2D, 11D),	// base
				Block.box(5D, 2D, 7D, 11D, 14D, 9D),	// main
				Block.box(5D, 14D, 7D, 11D, 15D, 9D),	// star1
				Block.box(4D, 15D, 7D, 12D, 16D, 9D)	// star2
				);

		VoxelShape eastWest = VoxelShapes.or(
				Block.box(5D, 0D, 2D, 11D, 2D, 14D),	// base
				Block.box(7D, 2D, 5D, 9D, 14D, 11D),	// main
				Block.box(7D, 14D, 5D, 9D, 15D, 11D),	// star1
				Block.box(7D, 15D, 4D, 9D, 16D, 12D)	// star1
				);
		
		return new VoxelShape[] {
				northSouth,	// S
				eastWest,	// W
				northSouth,	// N
				eastWest,	// E
	    };
	}
	
	public static VoxelShape[] buildGravestone3() {
		VoxelShape northSouth = VoxelShapes.or(
				Block.box(2D, 0D, 5D, 14D, 2D, 11D),	// base
				Block.box(5D, 2D, 7D, 11D, 9D, 9D),	// main1
				Block.box(7D, 9D, 7D, 9D, 15D, 9D),	// main2
				Block.box(5D, 15D, 7D, 11D, 16D, 9D)	// star
				);

		VoxelShape eastWest = VoxelShapes.or(
				Block.box(5D, 0D, 2D, 11D, 2D, 14D),	// base
				Block.box(7D, 2D, 5D, 9D, 9D, 11D),	// main1
				Block.box(7D, 9D, 7D, 9D, 15D, 9D),	// main2
				Block.box(7D, 15D, 5D, 9D, 16D, 11D)	// star1
				);
		
		return new VoxelShape[] {
				northSouth,	// S
				eastWest,	// W
				northSouth,	// N
				eastWest,	// E
	    };
	}
	
	public static VoxelShape[] buildSkullCrossbones() {
		VoxelShape north = VoxelShapes.or(
				Block.box(5D, 0D, 1D, 11D, 5D, 6D), // head
				Block.box(1D, 0D, 7D, 15D, 1D, 14D) // bones
				);
		
		VoxelShape south = VoxelShapes.or(				
				Block.box(5D, 0D, 10D, 11D, 5D, 15D), // head
				Block.box(1D, 0D, 2D, 15D, 1D, 9D) // bones
				);
		
		VoxelShape east = VoxelShapes.or(
				Block.box(10D, 0D, 5D, 15D, 5D, 11D), // head
				Block.box(2D, 0D, 1D, 9D, 1D, 15D) // bones
				);
		
		VoxelShape west = VoxelShapes.or(
				Block.box(1D, 0D, 5D, 6D, 5D, 11D), // head
				Block.box(7D, 0D, 1D, 15D, 1D, 14D) // bones
				);

		return new VoxelShape[] {
				south,	// S
				west,	// W
				north,	// N
				east,	// E
	    };
	}
	
	/*
	 * rotate VoxelShape a full 90 around an axis
	 */
//	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
//		VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };
//
//		int times = (to.getHorizontalIndex() - from.getHorizontalIndex() + 4) % 4;
//		for (int i = 0; i < times; i++) {
//			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
//			buffer[0] = buffer[1];
//			buffer[1] = VoxelShapes.empty();
//		}
//
//		return buffer[0];
//	}
}
