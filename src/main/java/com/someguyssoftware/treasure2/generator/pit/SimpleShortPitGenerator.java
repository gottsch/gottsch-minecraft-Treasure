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

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.IServerWorld;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SimpleShortPitGenerator extends AbstractPitGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param coords
	 * @return
	 */
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		Treasure.LOGGER.debug("Generating SimpleShortShaft.");
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setSpawnCoords(spawnCoords);
		result.getData().getChestContext().setCoords(spawnCoords);
		
		ICoords checkCoords = null;
		// check each position if already not air and generate

		checkCoords = spawnCoords.add(0, 1, 0);
		BlockState blockState = world.getBlockState(checkCoords.toPos());
		if (blockState.getMaterial() != Material.AIR) {
			buildLogLayer(world, random, checkCoords, DEFAULT_LOG);
		}
		
		checkCoords = spawnCoords.add(0, 2, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLayer(world, checkCoords, Blocks.SAND);
		}
		
		checkCoords = surfaceCoords.add(0, -2, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLayer(world, checkCoords, Blocks.SAND);
		}
		
		checkCoords = surfaceCoords.add(0, -3, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLogLayer(world, random, checkCoords, DEFAULT_LOG);
		}
		
		Treasure.LOGGER.debug("Generated Simple Short Shaft Chamber at " + spawnCoords.toShortString());
		return result.success();
	}
}