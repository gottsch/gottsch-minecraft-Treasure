/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author Mark Gottschling on Aug 4, 2021
 *
 */
public class TreasureShapeBuilder {
	// NOTE bounds order is NESW
	
	/**
	 * 
	 * @return
	 */
	public static VoxelShape[] buildGravestone1() {
		VoxelShape northSouth = Shapes.or(
				Block.box(2D, 0D, 6D, 14D, 10D, 10D),
				Block.box(4D, 10D, 6D, 12D, 12D, 10D)
				);

		VoxelShape eastWest = Shapes.or(
				Block.box(6D, 0D, 2D, 10D, 10D, 14D),
				Block.box(6D, 10D, 4D, 10D, 12D, 12D)
				);

		return new VoxelShape[] {
				northSouth,	
				eastWest,
				northSouth,
				eastWest,
		};
	}

	public static VoxelShape[] buildGravestone2() {
		VoxelShape northSouth = Shapes.or(
				Block.box(2D, 0D, 5D, 14D, 2D, 11D),	// base
				Block.box(5D, 2D, 7D, 11D, 14D, 9D),	// main
				Block.box(5D, 14D, 7D, 11D, 15D, 9D),	// star1
				Block.box(4D, 15D, 7D, 12D, 18D, 9D)	// star2
				);

		VoxelShape eastWest = Shapes.or(
				Block.box(5D, 0D, 2D, 11D, 2D, 14D),	// base
				Block.box(7D, 2D, 5D, 9D, 14D, 11D),	// main
				Block.box(7D, 14D, 5D, 9D, 15D, 11D),	// star1
				Block.box(7D, 15D, 4D, 9D, 16D, 12D)	// star1
				);

		return new VoxelShape[] {
				northSouth,
				eastWest,
				northSouth,
				eastWest,
		};
	}

	public static VoxelShape[] buildGravestone3() {
		VoxelShape northSouth = Shapes.or(
				Block.box(2D, 0D, 5D, 14D, 2D, 11D),	// base
				Block.box(5D, 2D, 7D, 11D, 9D, 9D),	// main1
				Block.box(7D, 9D, 7D, 9D, 15D, 9D),	// main2
				Block.box(5D, 15D, 7D, 11D, 16D, 9D)	// star
				);

		VoxelShape eastWest = Shapes.or(
				Block.box(5D, 0D, 2D, 11D, 2D, 14D),	// base
				Block.box(7D, 2D, 5D, 9D, 9D, 11D),	// main1
				Block.box(7D, 9D, 7D, 9D, 15D, 9D),	// main2
				Block.box(7D, 15D, 5D, 9D, 16D, 11D)	// star1
				);

		return new VoxelShape[] {
				northSouth,
				eastWest,
				northSouth,
				eastWest,
		};
	}

	public static VoxelShape[] buildSkullCrossbones() {
		VoxelShape north = Shapes.or(
				Block.box(5D, 0D, 1D, 11D, 5D, 6D), // head
				Block.box(1D, 0D, 7D, 15D, 1D, 14D) // bones
				);

		VoxelShape south = Shapes.or(				
				Block.box(5D, 0D, 10D, 11D, 5D, 15D), // head
				Block.box(1D, 0D, 2D, 15D, 1D, 9D) // bones
				);

		VoxelShape east = Shapes.or(
				Block.box(10D, 0D, 5D, 15D, 5D, 11D), // head
				Block.box(2D, 0D, 1D, 9D, 1D, 15D) // bones
				);

		VoxelShape west = Shapes.or(
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

	public static VoxelShape[] buildVikingChest() {
		VoxelShape northSouth = Shapes.or(
				Block.box(1, 3, 3, 15, 15, 13),
				Block.box(1, 0, 3, 3, 4, 13),
				Block.box(13, 0, 3, 15, 4, 13));

		VoxelShape eastWest = Shapes.or(
				Block.box(3, 3, 1, 13, 15, 15),
				Block.box(3, 0, 1, 13, 4, 3),
				Block.box(3, 0, 13, 13, 4, 15));
		
		return new VoxelShape[] {
				northSouth,
				eastWest,
				northSouth,
				eastWest
		};
	}

	public static VoxelShape[] buildFullBlock() {
		VoxelShape block = Block.box(0, 0, 0, 16, 16, 16);
		return new VoxelShape[] {
				 block,  block,  block,  block
		};
	}
	
	public static VoxelShape[] buildCauldronChest() {
		VoxelShape shape = Shapes.or(
				// top
				Block.box(0, 3, 0, 16, 16, 16),
				// front row
				Block.box(0, 0, 0, 4, 3, 2),
				Block.box(12, 0, 0, 16, 3, 2),
				Block.box(0, 0, 2, 2, 3, 4),
				Block.box(14, 0, 2, 16, 3, 4),
				// back row
				Block.box(0, 0, 14, 4, 3, 16),
				Block.box(12, 0, 14, 16, 3, 16),
				Block.box(0, 0, 12, 2, 3, 14),
				Block.box(14, 0, 12, 16, 3, 14)
				);		
		return new VoxelShape[] { shape, shape, shape, shape };
	}
	
	public static VoxelShape[] buildStrongbox() {
		VoxelShape shape = Block.box(3, 0.5, 4, 13, 7.5, 12);
		VoxelShape shape2 = Block.box(4, 0.5, 3, 12, 7.5, 13);
		return new VoxelShape[] { shape, shape2, shape, shape2 };
	}
	
	public static VoxelShape[] buildSafe() {
		VoxelShape shape = Block.box(2, 1, 2, 14, 13, 14);		
		return new VoxelShape[] { shape, shape, shape, shape };
	}
	
	public static VoxelShape[] buildSkull() {
		VoxelShape n = Shapes.or(
				Block.box(5, 0, 4, 11, 2, 9),
				Block.box(5, 2, 4, 11, 3, 10),
				Block.box(4, 3, 4, 12, 8, 12),
				Block.box(5, 8, 5, 11, 9, 11)				
				);
		VoxelShape e = Shapes.or(
				Block.box(7, 0, 5, 12, 2, 11),
				Block.box(6, 2, 5, 12, 3, 11),
				Block.box(4, 3, 4, 12, 8, 12),
				Block.box(5, 8, 5, 11, 9, 11)	
				);		
		VoxelShape s = Shapes.or(
				Block.box(5, 0, 7, 11, 2, 12),
				Block.box(5, 2, 6, 11, 3, 12),
				Block.box(4, 3, 4, 12, 8, 12),
				Block.box(5, 8, 5, 11, 9, 11)	
				);
		VoxelShape w = Shapes.or(
				Block.box(4, 0, 5, 9, 2, 11),
				Block.box(4, 2, 5, 10, 3, 11),
				Block.box(4, 3, 4, 12, 8, 12),
				Block.box(5, 8, 5, 11, 9, 11)				
				);
		return new VoxelShape[] { n, e, s , w };
	}
	
	public static VoxelShape[] buildMilkCrate() {
		VoxelShape shape = Block.box(2.75, 0, 2.75, 13.25, 9.75, 13.25);
		return new VoxelShape[] { shape, shape, shape, shape };
	}
	
	public static VoxelShape[] buildSpiderChest() {
		VoxelShape n = Shapes.or(
				Block.box(2, 7, 6, 14, 16, 16), // body
				Block.box(4, 7, 0, 12, 15, 6) // head
				);
		
		VoxelShape e = Shapes.or(
				Block.box(0, 7, 2, 10, 16, 14), // body
				Block.box(10, 7, 4, 16, 15, 12) // head
				);
		
		VoxelShape s = Shapes.or(
				Block.box(2, 7, 0, 14, 16, 10), // body
				Block.box(4, 7, 10, 12, 15, 16) // head
				);
		
		VoxelShape w = Shapes.or(
				Block.box(6, 7, 2, 16, 16, 14), // body
				Block.box(0, 7, 4, 6, 15, 12) // head
				);
		
		return new VoxelShape[] { n, e, s, w };
	}
	
	public static VoxelShape[] buildCompressorChest() {
		VoxelShape block = Block.box(4.5, 0, 4.5, 11.5, 7, 11.5);
		return new VoxelShape[] {
				 block,  block,  block,  block
		};
	}
}
