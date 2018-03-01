package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class TntTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	// TODO this can move to abstract or default method in interface
	public boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		IBlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.logger.debug("Spawn coords is in cavener.");
			inCavern = true;
		}
		
		if (inCavern) {
			Treasure.logger.debug("Shaft is in cavern... finding ceiling.");
			spawnCoords = GenUtil.findUndergroundCeiling(world, spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.logger.warn("Exiting: Unable to locate cavern ceiling.");
				return false;
			}
		}
	
		// generate shaft
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.logger.trace("Distance to ySurface =" + yDist);
	
		if (yDist > 6) {			
			Treasure.logger.debug("Generating shaft @ " + spawnCoords.toShortString());
			// at chest level
			buildLayer(world, spawnCoords, Blocks.AIR);
			
			// above the chest	
			buildLayer(world, spawnCoords.add(0, 1, 0), Blocks.AIR);
			buildLogLayer(world, random, spawnCoords.add(0, 2, 0), Blocks.LOG);
			buildLayer(world, spawnCoords.add(0, 3, 0), Blocks.SAND);
			
			// shaft enterance
			buildLogLayer(world, random, surfaceCoords.add(0, -2, 0), Blocks.LOG);
			buildLayer(world, surfaceCoords.add(0, -3, 0), Blocks.SAND);
			buildLogLayer(world, random, surfaceCoords.add(0, -4, 0), Blocks.LOG);
			
			RandomWeightedCollection<Block> col = new RandomWeightedCollection<>();
			if (random.nextInt(4) == 0) {
				// empty shaft
				Treasure.logger.debug("Filling with air");
				col.add(50, Blocks.AIR);
				buildPit(world, random, spawnCoords, surfaceCoords, col);			
			}
			else {
				col.add(50, Blocks.AIR);
				col.add(25,  Blocks.SAND);
				col.add(15, Blocks.GRAVEL);
				col.add(10, Blocks.LOG);
				buildPit(world, random, spawnCoords, surfaceCoords, col);
			}
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}		
		Treasure.logger.debug("Generated TNT Trap Pit at " + spawnCoords.toShortString());
		return true;
	}
	
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
		
		// select mid-point of pit length - coords for trap
		ICoords midCoords = surfaceCoords.delta(coords);
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + Y_OFFSET; yIndex <= surfaceCoords.getY() - Y_SURFACE_OFFSET; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			Cube cube = new Cube(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}
			
			// TODO ensure that the mid-point has enough room to create the trap
			// check for midpoint
			if (yIndex == midCoords.getY()) {
				// build trap layer
				nextCoords = buildTrapLayer(world, random, cube.getCoords(), Blocks.LOG); // could have difference classes and implement buildLayer differently
			} // TODO remember to add else here to skip the rest of the stuff
			
			// get the next type of block layer to build
			Block block = col.next();
			if (block == Blocks.LOG) {
				// special log build layer
				nextCoords = buildLogLayer(world, random, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}
			
			// get the expected coords
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autocorrectIndex(yIndex, nextCoords, expectedCoords);
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
			nextCoords = buildLayer(world, nextCoords, block);
		}
		
		// ensure that the difference is only 1 between nextCoords and coords
		if (nextCoords.delta(coords).getY() > 1) return nextCoords;

		// core 4-square pressure plate (above log)
		IBlockState plate = Blocks.WOODEN_PRESSURE_PLATE.getDefaultState();
//		world.setBlockState(nextCoords.toPos(), plate, 3);
//		world.setBlockState(nextCoords.add(1, 0, 0).toPos(), plate, 3);		
//		world.setBlockState(nextCoords.add(0, 0, 1).toPos(), plate, 3);
//		world.setBlockState(nextCoords.add(1, 0, 1).toPos(), plate, 3);
		GenUtil.replaceWithBlock(world, coords, block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), block);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), block);
		
		// TODO add TNT and redstone(below log)
		IBlockState redstone = Blocks.REDSTONE_WIRE.getDefaultState();
//		world.setBlockState(nextCoords.toPos(), Blocks.TNT.getDefaultState(), 3);
//		world.setBlockState(nextCoords.add(1, 0, 0).toPos(), redstone, 3);		
//		world.setBlockState(nextCoords.add(0, 0, 1).toPos(), redstone, 3);
//		world.setBlockState(nextCoords.add(1, 0, 1).toPos(), redstone, 3);
		GenUtil.replaceWithBlock(world, coords, block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), block);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), block);
				
		// get the next coords
		nextCoords = nextCoords.add(0, 1, 0);

		// return the next coords
		return nextCoords;
	}
}
