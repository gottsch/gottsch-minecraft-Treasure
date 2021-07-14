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
package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IServerWorld;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class CollapsingTrapPitGenerator extends AbstractPitGenerator {

	/**
	 * 
	 */
	public CollapsingTrapPitGenerator() {
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = super.generate(world, random, surfaceCoords, spawnCoords);
		if (result.isSuccess()) {
			Treasure.LOGGER.debug("Generated Collapsing Trap Pit at " + spawnCoords.toShortString());
		}
		return result;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	@Override
	public ICoords buildPit(IServerWorld world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {

		// replace surface and build air shaft
		int minCoordsX = coords.getX() - 2;
		int maxCoordsX = coords.getX() + 2;
		int minCoordsZ = coords.getZ() - 2;
		int maxCoordsZ = coords.getZ() + 2;

		for (int x = minCoordsX; x <= maxCoordsX; x++) {
			for (int z = minCoordsZ; z <= maxCoordsZ; z++ ) {
				ICoords spawnCoords = WorldInfo.getSurfaceCoords(world, new Coords(x, 255, z));
				spawnCoords = spawnCoords.down(1);

				// skip the corners

				BlockState state = world.getBlockState(spawnCoords.toPos());
				if (state.getBlock() == Blocks.GRASS_BLOCK) {	
					if ((x == minCoordsX || x == maxCoordsX) && (z == minCoordsZ || z == maxCoordsZ)) {
					}
					else {
						GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_GRASS.defaultBlockState());	
					}
				}
				else if (state.getBlock() == Blocks.SAND)  {
					GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_SAND.defaultBlockState());	
				}
				else if (state.getBlock() == Blocks.RED_SAND) {
					GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_RED_SAND.defaultBlockState());
				}
				spawnCoords = spawnCoords.down(1);
				while (spawnCoords.getY() >= coords.getY()) {
					GenUtil.replaceWithBlockState(world, spawnCoords,  Blocks.AIR.defaultBlockState());
					spawnCoords = spawnCoords.down(1);
				}
			}
		}
		return coords;
	}
}