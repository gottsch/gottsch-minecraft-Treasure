package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.generator.IGeneratorResult;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jul 27, 2018
 *
 */
public class MobTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public MobTrapPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25,  Blocks.SAND);
		getBlockLayers().add(15, Blocks.COBBLESTONE);
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
	public IGeneratorResult generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		IGeneratorResult result = super.generate(world, random, surfaceCoords, spawnCoords);
		if (result.isSuccess()) {
			Treasure.logger.debug("Generated Mob Trap Pit at " + spawnCoords.toShortString());
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
		
		// select mid-point of pit length - coords for trap
		int midY = (surfaceCoords.getY() + coords.getY())/2;
		ICoords midCoords = new Coords(coords.getX(), midY, coords.getZ());
		int deltaY = surfaceCoords.delta(midCoords).getY();
		
//		Treasure.logger.debug("Mob Trap pit from {} to {}", coords.getY() + OFFSET_Y, surfaceCoords.getY() - SURFACE_OFFSET_Y);
		// randomly fill shaft
		for (int yIndex = coords.getY() + getOffsetY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			Cube cube = new Cube(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}

			// check for midpoint and that there is enough room to build the trap
			if (yIndex == midCoords.getY() && deltaY > 4) {
				// build trap layer
				nextCoords = buildTrapLayer(world, random, cube.getCoords(), Blocks.LOG); // could have difference classes and implement buildLayer differently
			}
			else {
				// get the next type of block layer to build
				Block block = col.next();
				if (block == Blocks.LOG) {
					// special log build layer
					nextCoords = buildLogLayer(world, random, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
				}
				else {
					nextCoords = buildLayer(world, cube.getCoords(), block);
				}
			}
			// get the expected coords
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
//			Treasure.logger.debug("yIndex: {}", yIndex);
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
		ICoords nextCoords = null;
		if (block == Blocks.LOG) {
			nextCoords = buildLogLayer(world, random, coords, block);
		}
		else {
			nextCoords = buildLayer(world, coords, block);
		}
//		Treasure.logger.debug("Coords for trap base layer: {}", coords.toShortString());
//		Treasure.logger.debug("Next Coords after base log: {}", nextCoords.toShortString());
		
		// ensure that the difference is only 1 between nextCoords and coords
//		if (nextCoords.delta(coords).getY() > 1) return nextCoords;
		ICoords spawnCoords = nextCoords;
		GenUtil.replaceWithBlock(world, nextCoords.add(0, 0, 0), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(1, 0, 0), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(0, 0, 1), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(1, 0, 1), Blocks.AIR);
		
		nextCoords = nextCoords.up(1);

		// add another air layer
		GenUtil.replaceWithBlock(world, nextCoords.add(0, 0, 0), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(1, 0, 0), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(0, 0, 1), Blocks.AIR);
		GenUtil.replaceWithBlock(world, nextCoords.add(1, 0, 1), Blocks.AIR);
		
		// add aother  log layer
		nextCoords = buildLogLayer(world, random, nextCoords, block);

		// spawn the mobs
//    	spawnMob(world, spawnCoords, "skeleton");
//    	spawnMob(world, spawnCoords.add(1, 0, 0), "zombie");
//    	spawnMob(world, spawnCoords.add(0, 0, 1), "zombie");
//    	spawnMob(world, spawnCoords.add(1, 0, 1), "skeleton");
		
    	spawnRandomMob(world, random, spawnCoords);
    	spawnRandomMob(world, random, spawnCoords.add(1, 0, 0));
    	spawnRandomMob(world, random, spawnCoords.add(0, 0, 1));
    	spawnRandomMob(world, random, spawnCoords.add(1, 0, 1));
    	
		// get the next coords
		nextCoords = nextCoords.up(1);
		// return the next coords
		return nextCoords;
	}
}