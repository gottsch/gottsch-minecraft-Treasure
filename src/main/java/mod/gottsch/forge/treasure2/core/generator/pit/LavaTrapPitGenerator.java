/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

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
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		Optional<GeneratorResult<ChestGeneratorData>> result = super.generate(context, surfaceCoords, spawnCoords);
		if (result.isPresent()) {
			Treasure.LOGGER.debug("generated LavaTrapPit at -> {}", spawnCoords.toShortString());
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
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// select mid-point of pit length - topmost coords of trap shaft
		int midY = (surfaceCoords.getY() + coords.getY())/2;
		
		// build lava around base
		buildLavaBaseLayer(context, coords.down(1));
		
		// build @ chest layer (overwrites what abstract pit gen does
		build3WideLayer(context, coords, Blocks.AIR);
		
		// fill shaft will air to mid-point
		for (int yIndex = coords.getY() + OFFSET_Y; yIndex <= midY; yIndex++) {
			nextCoords = build3WideLayer(context, new Coords(coords.getX(), yIndex, coords.getZ()), Blocks.AIR);
		}		
		nextCoords = build3WideLayer(context, new Coords(coords.getX(), midY+1, coords.getZ()), DEFAULT_LOG);
		
		for (int yIndex = nextCoords.getY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			BlockContext blockContext = new BlockContext(context.level(), new Coords(coords.getX(), yIndex, coords.getZ()));
			if (blockContext.isAir()) {
				continue;
			}

			// get the next type of block layer to build
			Block block = col.next();
			if (block == DEFAULT_LOG) {
				// special log build layer
				nextCoords = buildLogLayer(context, blockContext.getCoords(), block); // could have difference classes and implement buildLayer differently
			}
			else {
				nextCoords = buildLayer(context, blockContext.getCoords(), block);
			}

			// get the expected coords
			expectedCoords = blockContext.getCoords().add(0, 1, 0);
			
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
	public void buildAboveChestLayers(IWorldGenContext context, ICoords spawnCoords) {
		build3WideLayer(context, spawnCoords.add(0, 1, 0), Blocks.AIR);
		build3WideLayer(context, spawnCoords.add(0, 2, 0), Blocks.AIR);	
		build3WideLayer(context, spawnCoords.add(0, 3, 0), Blocks.AIR);	
		build3WideLayer(context, spawnCoords.add(0, 4, 0), Blocks.AIR);	
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build3WideLayer(IWorldGenContext context, ICoords coords, Block block) {
        BlockState blockState = block.defaultBlockState();
        // randomly select the axis the logs are facing (0 = Z, 1 = X);
        if (block == DEFAULT_LOG) {
            int axis = context.random().nextInt(2);
            if (axis == 0) {
                blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
            }
            else {
                blockState.setValue(RotatedPillarBlock.AXIS,  Direction.Axis.X);
            }
        }

		GeneratorUtil.replaceWithBlockState(context.level(), coords, blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(1, 0, 0), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(-1, 0, 0), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(0, 0, 1), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(0, 0, -1), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(-1, 0, 1), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(1, 0, 1), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(-1, 0, -1), blockState);
		GeneratorUtil.replaceWithBlockState(context.level(), coords.add(1, 0, -1), blockState);	
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildLavaBaseLayer(IWorldGenContext context, ICoords coords) {
		Treasure.LOGGER.debug("Building lava baselayer from @ {} ", coords.toShortString());
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(1, 0, 0), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(-1, 0, 0), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(0, 0, 1), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(0, 0, -1), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(-1, 0, 1), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(1, 0, 1), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(-1, 0, -1), Blocks.LAVA);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(1, 0, -1), Blocks.LAVA);		
	}
}