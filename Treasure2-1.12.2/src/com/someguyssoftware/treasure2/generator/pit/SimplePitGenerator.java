package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class SimplePitGenerator {

	public static boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		IBlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
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
			world.setBlockState(spawnCoords.add(0, 1, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			world.setBlockState(spawnCoords.add(0, 2, 0).toPos(), Blocks.SAND.getDefaultState(), 3);
			world.setBlockState(spawnCoords.add(0, 3, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			
			// shaft enterance
			world.setBlockState(surfaceCoords.add(0,-2, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			world.setBlockState(surfaceCoords.add(0, -3, 0).toPos(), Blocks.SAND.getDefaultState(), 3);
			world.setBlockState(surfaceCoords.add(0, -4, 0).toPos(), Blocks.LOG.getDefaultState(), 3);
			
			// empty shaft
			if (random.nextInt(4) == 0) {
				Treasure.logger.debug("Filling with air");
				GenUtil.fillSimpleShaftWithAir(world, spawnCoords, surfaceCoords);
			}
			else {
				GenUtil.fillSimpleShaftRandomly(world, random, spawnCoords, surfaceCoords);
			}
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			SimpleShortPitGenerator.generate(world, random, surfaceCoords, spawnCoords);
		}		
		Treasure.logger.debug("Generated Simple Pit at " + spawnCoords.toShortString());
		return true;
	}

}
