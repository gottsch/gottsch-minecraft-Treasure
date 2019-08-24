/**
 * 
 */
package com.someguyssoftware.treasure2.generator.well;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.IWellConfig;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public class SimpleWellGenerator implements IWellGenerator<GeneratorResult<GeneratorData>> {

	/**
	 * 
	 */
	public SimpleWellGenerator() {
		super();
	}

	/**
	 * 
	 */
	@Override
	public boolean generate(World world, Random random, ICoords spawnCoords, IWellConfig config) {
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// 1. determine y-coord of land surface
		spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
//		Treasure.logger.debug("Land Coords @ {}", spawnCoords.toShortString());
		if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return false; 
		}
		
		// 2. check if it has 50% land
		if (!WorldInfo.isSolidBase(world, spawnCoords, 3, 3, 50)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", spawnCoords.toShortString(), 3, 3);
			return false;
		}
		
		//  build top (where wishing well block go)
		buildTop(world, spawnCoords);
		
		// build shaft
		nextCoords = spawnCoords.add(0, -1, 0);
		for (int index = 0; index < 5; index++) {
			expectedCoords = nextCoords.add(0, -1, 0);
			nextCoords = buildShaft(world, nextCoords, Blocks.MOSSY_COBBLESTONE);
			
			// check if the return coords is different than the anticipated coords and resolve
			if (!nextCoords.equals(expectedCoords)) {
				// find the difference in y int and add to yIndex;
				Treasure.logger.debug("Next coords does not equal expected coords. next: {}; expected: {}", nextCoords.toShortString(), expectedCoords.toShortString());
				// NOTE the difference should = 1, there remove 1 from the diff to find unexpected difference
				int diff = nextCoords.getY() - expectedCoords.getY() - 1;
				if (diff > 0) {
					index += diff;
					Treasure.logger.debug("Difference of: {}. Updating yIndex to {}", diff, index);
				}
			}
		}
		// build bottom
		buildBottom(world, nextCoords, Blocks.MOSSY_COBBLESTONE);		
		
		Treasure.logger.info("CHEATER! Wishing Well at coords: {}", spawnCoords.toShortString());
		return true;
	}
	
	@Override
	public GeneratorResult generate2(World world, Random random, ICoords spawnCoords, IWellConfig config) {
		return null;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public ICoords buildTop(World world, ICoords coords) {
		ICoords newCoords = new Coords(coords).add(0, -1, 0);
		Block block = TreasureBlocks.WISHING_WELL_BLOCK;
		Random random = new Random();
		
		// north of coords
		world.setBlockState(coords.add(-1, 0, -1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(0, 0, -1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, -1).toPos(), block.getDefaultState(), 3);
	
		// east / west of coords
		world.setBlockState(coords.add(-1, 0, 0).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.toPos(), Blocks.WATER.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 0).toPos(), block.getDefaultState(), 3);
		
		// south of coords
		world.setBlockState(coords.add(-1, 0, 1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(0, 0, 1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 1).toPos(), block.getDefaultState(), 3);

		// add plants / decorations
		addDecoration(world, random, coords);		
		return newCoords;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public ICoords buildShaft(World world, ICoords coords, Block block) {
		// north of coords
		world.setBlockState(coords.add(-1, 0, -1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(0, 0, -1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, -1).toPos(), block.getDefaultState(), 3);
	
		// east / west of coords
		world.setBlockState(coords.add(-1, 0, 0).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.toPos(), Blocks.WATER.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 0).toPos(), block.getDefaultState(), 3);
		
		// south of coords
		world.setBlockState(coords.add(-1, 0, 1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(0, 0, 1).toPos(), block.getDefaultState(), 3);
		world.setBlockState(coords.add(1, 0, 1).toPos(), block.getDefaultState(), 3);
		
		return coords.add(0, -1, 0);
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildBottom(World world, ICoords coords, Block block) {
		ICoords indexCoords = coords.add(-1, 0, -1);
		for (int z = 0; z < 3; z++) {
			for (int x = 0; x < 3; x++) {
				world.setBlockState(indexCoords.add(x, 0, z).toPos(), block.getDefaultState(), 3);
			}
		}
		return null;
	}
}
