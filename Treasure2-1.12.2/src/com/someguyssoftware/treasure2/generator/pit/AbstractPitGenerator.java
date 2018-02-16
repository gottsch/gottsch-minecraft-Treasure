package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class AbstractPitGenerator {

	private static final int Y_OFFSET = 4;
	private static final int Y_SURFACE_OFFSET = 5;

	/**
	 * 
	 */
	public AbstractPitGenerator() {
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
	public abstract boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	public ICoords buildPit(World world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + Y_OFFSET; yIndex <= surfaceCoords.getY() - Y_SURFACE_OFFSET; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			Cube cube = new Cube(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}
			
			// get the next type of block layer to build
			Block block = col.next();
			if (block == Blocks.LOG) {
				// special log build layer
				nextCoords = buildLogLayer(world, random, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
				// ie. LayerBuilder.build(world, coords, block)
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			if (!nextCoords.equals(expectedCoords)) {
				// find the difference in y int and add to yIndex;
				Treasure.logger.debug("Next coords does not equal expected coords. next: {}; expected: {}", nextCoords.toShortString(), expectedCoords.toShortString());
				// NOTE the difference should = 1, there remove 1 from the diff to find unexpected difference
				int diff = nextCoords.getY() - expectedCoords.getY() - 1;
				if (diff > 0) {
					yIndex += diff;
					Treasure.logger.debug("Difference of: {}. Updating yIndex to {}", diff, yIndex);
				}
			}
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLayer(World world, ICoords coords, Block block) {
		Treasure.logger.debug("Building layer from {} @ {} ", block.getLocalizedName(), coords.toShortString());
		
		world.setBlockState(coords.toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 0).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(0, 0, 1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 1).toPos(), block.getDefaultState(), 3);
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLogLayer(final World world, final Random random, final ICoords coords, final Block block) {
		// ensure that block is of type LOG/LOG2
		if (block != Blocks.LOG && block != Blocks.LOG2) return coords;
		
		 // determine the direction the logs are facing - north/south (8) or east/west (4)
		int meta = random.nextInt() % 2 == 0 ? 8 : 4;
		IBlockState blockState = block.getStateFromMeta(meta);
				
		// core 4-square
		world.setBlockState(coords.toPos(), blockState, 3);
		world.setBlockState(coords.add(1, 0, 0).toPos(), blockState, 3);		
		world.setBlockState(coords.add(0, 0, 1).toPos(), blockState, 3);
		world.setBlockState(coords.add(1, 0, 1).toPos(), blockState, 3);
		
		if (meta == 8) {			
			// north of
			world.setBlockState(coords.add(0, 0, -1).toPos(), blockState, 3);
			world.setBlockState(coords.add(1, 0, -1).toPos(), blockState, 3);
			
			// south of
			world.setBlockState(coords.add(0, 0, 2).toPos(), blockState, 3);
			world.setBlockState(coords.add(1, 0, 2).toPos(), blockState, 3);
		}
		else {
			// west of
			world.setBlockState(coords.add(-1, 0, 0).toPos(), blockState, 3);
			world.setBlockState(coords.add(-1, 0, 1).toPos(), blockState, 3);
			// east of 
			world.setBlockState(coords.add(2, 0, 0).toPos(), blockState, 3);
			world.setBlockState(coords.add(2, 0, 1).toPos(), blockState, 3);
		}
		return coords.add(0, 1, 0);
	}
}