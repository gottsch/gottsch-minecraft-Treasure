package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


/**
 * Generates lava blocks outside the main pit to prevent players from digging down on the edges
 * @author Mark Gottschling on Dec 9, 2018
 *
 */
public class BigBottomMobTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public BigBottomMobTrapPitGenerator() {
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
	@Override
	public boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		IBlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.logger.debug("Spawn coords is in cavern.");
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
		Treasure.logger.debug("Distance to ySurface =" + yDist);
	
		ICoords nextCoords = null;
		if (yDist > 6) {			
			Treasure.logger.debug("Generating shaft @ " + spawnCoords.toShortString());
			// at chest level
			nextCoords = build6WideLayer(world, random, spawnCoords, Blocks.AIR);
			
			// above the chest
			nextCoords = build6WideLayer(world, random, nextCoords, Blocks.AIR);
			nextCoords = build6WideLayer(world, random, nextCoords, Blocks.AIR);
			nextCoords = buildLogLayer(world, random, nextCoords, Blocks.LOG);
			nextCoords = buildLayer(world, nextCoords, Blocks.SAND);
			
			// shaft enterance
			buildLogLayer(world, random, surfaceCoords.add(0, -3, 0), Blocks.LOG);
			buildLayer(world, surfaceCoords.add(0, -4, 0), Blocks.SAND);
			buildLogLayer(world, random, surfaceCoords.add(0, -5, 0), Blocks.LOG);

			// build the trap
			buildTrapLayer(world, random, spawnCoords, null);
			
			// build the pit
			// NOTE must add nextCoords by Y_OFFSET, because the AbstractPitGen.buildPit() starts with the Y_OFFSET, which is above the standard chest area.
			buildPit(world, random, nextCoords.down(Y_OFFSET), surfaceCoords, getBlockLayers());
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}		
		Treasure.logger.debug("Generated Big Bottom Mob Trap Pit at " + spawnCoords.toShortString());
		return true;
	}	

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build6WideLayer(World world, Random random, ICoords coords, Block block) {
		ICoords startCoords = coords.add(-2, 0, -2);
		for (int x = startCoords.getX(); x < startCoords.getX() + 6; x++) {
			for (int z = startCoords.getZ(); z < startCoords.getZ() + 6; z++) {
				GenUtil.replaceWithBlockState(world, new Coords(x, coords.getY(), z), block.getDefaultState());
			}
		}
		return coords.add(0, 1, 0);
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
		// spawn the mobs
    	spawnMob(world, coords.add(-2, 0, 0), "skeleton");
    	spawnMob(world, coords.add(0, 0, -2), "zombie");
    	spawnMob(world, coords.add(2, 0, 0), "zombie");
    	spawnMob(world, coords.add(0, 0, 2), "skeleton");    	
		return coords;
	}
	
}
