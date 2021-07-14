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

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class LavaTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public LavaTrapPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25, Blocks.SAND);
		getBlockLayers().add(15, Blocks.COBBLESTONE);
		getBlockLayers().add(15, Blocks.GRAVEL);
		getBlockLayers().add(10, DEFAULT_LOG);
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
			Treasure.LOGGER.debug("Generated Lava Trap Pit at " + spawnCoords.toShortString());
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
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// select mid-point of pit length - topmost coords of trap shaft
		int midY = (surfaceCoords.getY() + coords.getY())/2;
		
		// build lava around base
		buildLavaBaseLayer(world, coords.down(1));
		
		// build @ chest layer (overwrites what abstract pit gen does
		build3WideLayer(world, random, coords, Blocks.AIR);
		
		// fill shaft will air to mid-point
		for (int yIndex = coords.getY() + OFFSET_Y; yIndex <= midY; yIndex++) {
			nextCoords = build3WideLayer(world, random, new Coords(coords.getX(), yIndex, coords.getZ()), Blocks.AIR);
		}		
		nextCoords = build3WideLayer(world, random, new Coords(coords.getX(), midY+1, coords.getZ()), DEFAULT_LOG);
		
		for (int yIndex = nextCoords.getY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			BlockContext context = new BlockContext(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (context.isAir()) {
				continue;
			}

			// get the next type of block layer to build
			Block block = col.next();
			if (block == DEFAULT_LOG) {
				// special log build layer
				nextCoords = buildLogLayer(world, random, context.getCoords(), block); // could have difference classes and implement buildLayer differently
			}
			else {
				nextCoords = buildLayer(world, context.getCoords(), block);
			}

			// get the expected coords
			expectedCoords = context.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
			Treasure.LOGGER.debug("yIndex -> {}", yIndex);
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 */
	@Override
	public void buildAboveChestLayers(IServerWorld world, Random random, ICoords spawnCoords) {
		build3WideLayer(world, random, spawnCoords.add(0, 1, 0), Blocks.AIR);
		build3WideLayer(world, random, spawnCoords.add(0, 2, 0), Blocks.AIR);	
		build3WideLayer(world, random, spawnCoords.add(0, 3, 0), Blocks.AIR);	
		build3WideLayer(world, random, spawnCoords.add(0, 4, 0), Blocks.AIR);	
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build3WideLayer(IWorld world, Random random, ICoords coords, Block block) {
        BlockState blockState = block.defaultBlockState();
        // randomly select the axis the logs are facing (0 = Z, 1 = X);
        if (block == DEFAULT_LOG) {
            int axis = random.nextInt(2);
            if (axis == 0) {
                blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
            }
            else {
                blockState.setValue(RotatedPillarBlock.AXIS,  Direction.Axis.X);
            }
        }

		GenUtil.replaceWithBlockState(world, coords, blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, -1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, -1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, -1), blockState);	
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildLavaBaseLayer(IWorld world, ICoords coords) {
		Treasure.LOGGER.debug("Building lava baselayer from @ {} ", coords.toShortString());
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, 0), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, -1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, -1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, -1), Blocks.LAVA);		
	}
}