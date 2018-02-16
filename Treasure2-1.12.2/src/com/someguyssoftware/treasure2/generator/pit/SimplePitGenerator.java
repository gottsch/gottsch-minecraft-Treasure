package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

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
public class SimplePitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
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
			// above the chest	
			buildLogLayer(world, random, spawnCoords.add(0, 1, 0), Blocks.LOG);
			buildLayer(world, spawnCoords.add(0, 2, 0), Blocks.SAND);
			buildLogLayer(world, random, spawnCoords.add(0, 3, 0), Blocks.LOG);
			
//			world.setBlockState(spawnCoords.add(0, 1, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
//			world.setBlockState(spawnCoords.add(0, 2, 0).toPos(), Blocks.SAND.getDefaultState(), 3);
//			world.setBlockState(spawnCoords.add(0, 3, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			
			// shaft enterance
			buildLogLayer(world, random, surfaceCoords.add(0, -2, 0), Blocks.LOG);
			buildLayer(world, surfaceCoords.add(0, -3, 0), Blocks.SAND);
			buildLogLayer(world, random, surfaceCoords.add(0, -4, 0), Blocks.LOG);
			
//			world.setBlockState(surfaceCoords.add(0,-2, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
//			world.setBlockState(surfaceCoords.add(0, -3, 0).toPos(), Blocks.SAND.getDefaultState(), 3);
//			world.setBlockState(surfaceCoords.add(0, -4, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			
			RandomWeightedCollection<Block> col = new RandomWeightedCollection<>();
			if (random.nextInt(4) == 0) {
				// empty shaft
				Treasure.logger.debug("Filling with air");
				col.add(50, Blocks.AIR);
				buildPit(world, random, spawnCoords, surfaceCoords, col);
//				GenUtil.fillSimpleShaftWithAir(world, spawnCoords, surfaceCoords);				
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
		Treasure.logger.debug("Generated Simple Pit at " + spawnCoords.toShortString());
		return true;
	}
	
}
