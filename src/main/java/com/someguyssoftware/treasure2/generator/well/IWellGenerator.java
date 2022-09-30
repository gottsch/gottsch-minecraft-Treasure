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
package com.someguyssoftware.treasure2.generator.well;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IWellsConfig;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;

/**
 * @author Mark Gottschling on Feb 18, 2018
 *
 */
public interface IWellGenerator<RESULT extends IGeneratorResult<?>> {
	public static final List<Block> FLOWERS = Arrays.asList(new Block[] {
			Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
			Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY});
	public static final List<Block> MUSHROOMS = Arrays.asList(new Block[] {Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM});
	public static final List<Block> TALL_PLANTS = Arrays.asList(new Block[] {Blocks.TALL_GRASS, Blocks.LARGE_FERN});
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public abstract RESULT generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords spawnCoords, IWellsConfig config);
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param originalSpawnCoords
	 * @param templateHolder
	 * @param config
	 * @return
	 */
	public abstract RESULT generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords originalSpawnCoords,
			TemplateHolder templateHolder, IWellsConfig config);
	
	/**
	 * Default implementation based on a 3x3 well structure.
	 * @param world
	 * @param random
	 * @param coords
	 */
	public default void addDecoration(IServerWorld world, ChunkGenerator chunkGenerator, Random random, ICoords coords) {
		// coords matrix centered around the input coords
		ICoords[] circle = new Coords[16];
		circle[0] = coords.add(-2, 0, -2);
		circle[1] = coords.add(-1, 0, -2);
		circle[2] = coords.add(0, 0, -2);
		circle[3] = coords.add(1, 0, -2);
		circle[4] = coords.add(2, 0, -2);
		
		circle[5] = coords.add(-2, 0, -1);
		circle[6] = coords.add(2, 0, -1);
		circle[7] = coords.add(-2, 0, 0);
		circle[8] = coords.add(2, 0, 0);
		circle[9] = coords.add(-2, 0, 1);
		circle[10] = coords.add(2, 0, 1);
		
		circle[11] = coords.add(-2, 0, 2);
		circle[12] = coords.add(-1, 0, 2);
		circle[13] = coords.add(0, 0, 2);
		circle[14] = coords.add(1, 0, 2);
		circle[15] = coords.add(2, 0, 2);

		BlockState blockState = null;
		for (int i = 0; i < 16; i++) {
			if (random.nextInt(2) == 0) {
				// check if the block is dry land
				ICoords markerCoords = WorldInfo.getDryLandSurfaceCoords(world, chunkGenerator, circle[i]);
				if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
					Treasure.LOGGER.debug("Returning due to marker coords == null or EMPTY_COORDS");
					continue; 
				}
				BlockContext blockContext = new BlockContext(world, markerCoords.add(0, -1, 0));
				
				if (blockContext.equalsBlock(Blocks.GRASS) || blockContext.equalsBlock(Blocks.DIRT)) {
					blockState = FLOWERS.get(random.nextInt(FLOWERS.size())).defaultBlockState();
				}
				else if (blockContext.equalsBlock(Blocks.COARSE_DIRT)) {
					blockState = Blocks.TALL_GRASS.defaultBlockState();
				}
				else if (blockContext.equalsBlock(Blocks.MYCELIUM) || blockContext.equalsBlock(Blocks.PODZOL)) {
					blockState = MUSHROOMS.get(random.nextInt(MUSHROOMS.size())).defaultBlockState();			
				}
				else {
					blockState = TALL_PLANTS.get(random.nextInt(TALL_PLANTS.size())).defaultBlockState();
				}				
				// set the block state
				world.setBlock(circle[i].toPos(), blockState, 3);
			}
		}
	}
}