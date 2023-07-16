/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.pit;

import java.util.Optional;

import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;

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
		super();
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		Optional<GeneratorResult<ChestGeneratorData>> result = super.generate(context, surfaceCoords, spawnCoords);
		if (result.isPresent()) {
			Treasure.LOGGER.debug("generated CollapsingTrapPit at -> {}", spawnCoords.toShortString());
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
	public ICoords buildPit(IWorldGenContext context, ICoords coords, ICoords surfaceCoords, WeightedCollection<Integer, Block> col) {
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		
		// replace surface and build air shaft
		int minCoordsX = coords.getX() - 2;
		int maxCoordsX = coords.getX() + 2;
		int minCoordsZ = coords.getZ() - 2;
		int maxCoordsZ = coords.getZ() + 2;

		for (int x = minCoordsX; x <= maxCoordsX; x++) {
			for (int z = minCoordsZ; z <= maxCoordsZ; z++ ) {
				ICoords spawnCoords = WorldInfo.getSurfaceCoords(context.level(), chunkGenerator, new Coords(x, 0, z));
				spawnCoords = spawnCoords.down(1);

				// skip the corners

				BlockState state = context.level().getBlockState(spawnCoords.toPos());
				if (state.getBlock() == Blocks.GRASS_BLOCK) {	
					if ((x == minCoordsX || x == maxCoordsX) && (z == minCoordsZ || z == maxCoordsZ)) {
					}
					else {
						GeneratorUtil.replaceWithBlockState(context.level(), spawnCoords, TreasureBlocks.FALLING_GRASS.get().defaultBlockState());	
					}
				}
				else if (state.getBlock() == Blocks.SAND)  {
					GeneratorUtil.replaceWithBlockState(context.level(), spawnCoords, TreasureBlocks.FALLING_SAND.get().defaultBlockState());	
				}
				else if (state.getBlock() == Blocks.RED_SAND) {
					GeneratorUtil.replaceWithBlockState(context.level(), spawnCoords, TreasureBlocks.FALLING_RED_SAND.get().defaultBlockState());
				}
				spawnCoords = spawnCoords.down(1);
				while (spawnCoords.getY() >= coords.getY()) {
					GeneratorUtil.replaceWithBlockState(context.level(), spawnCoords,  Blocks.AIR.defaultBlockState());
					spawnCoords = spawnCoords.down(1);
				}
			}
		}
		return coords;
	}
}