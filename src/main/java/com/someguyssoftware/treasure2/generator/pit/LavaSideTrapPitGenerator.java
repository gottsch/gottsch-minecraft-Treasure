package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


/**
 * Generates lava blocks outside the main pit to prevent players from digging down on the edges
 * @author Mark Gottschling on Dec 9, 2018
 *
 */
public class LavaSideTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public LavaSideTrapPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25,  Blocks.SAND);
		getBlockLayers().add(15, Blocks.GRAVEL);
		getBlockLayers().add(10, Blocks.LOG);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = super.generate(world, random, surfaceCoords, spawnCoords); 
		if (result.isSuccess()) {
			Treasure.LOGGER.debug("Generated Lave Side Trap Pit at " + spawnCoords.toShortString());
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
	public ICoords buildPit(World world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + OFFSET_Y; yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
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
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}
	
			// select random - 30% chance of lava layer
			boolean isLava = RandomHelper.checkProbability(random, 30);
			
			// check for midpoint and that there is enough room to build the trap
			if (isLava) {
				// build trap layer
				buildTrapLayer(world, random, cube.getCoords(), Blocks.LAVA); // could have difference classes and implement buildLayer differently
			}
			
			// get the expected coords
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildTrapLayer(final World world, final Random random, final ICoords coords, final Block block) {
		final int MAX_REPLACES = 5;
		
		ICoords[] matrix = {
				coords.add(-1, 0, -1),
				coords.add(0, 0, -1),
				coords.add(1, 0, -1),
				coords.add(2, 0, -1),
				coords.add(-1, 0, 1),
				coords.add(0, 0, 1),
				coords.add(1, 0, 1),
				coords.add(2, 0, 1),
				coords.add(-1, 0, 0),
				coords.add(-1, 0, 1),
				coords.add(-1, 0, 2),
				coords.add(2, 0, 0),
				coords.add(2, 0, 1),
				coords.add(2, 0, 2),				
		};
		
		// for a number of blocks to replace
		for (int i =0; i < MAX_REPLACES; i++) {
			// randomly select a coord from the array
			int x = RandomHelper.randomInt(0, matrix.length-1);
			GenUtil.replaceWithBlockState(world, matrix[x], block.getDefaultState());
		}		
		return coords;
	}
}
