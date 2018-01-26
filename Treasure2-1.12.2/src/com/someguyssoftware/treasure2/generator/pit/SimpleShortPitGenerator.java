package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SimpleShortPitGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param coords
	 * @return
	 */
	public static boolean generate(World world, Random random, ICoords surfaceCoords, ICoords coords) {
		Treasure.logger.debug("Generating SimpleShortShaft.");
		BlockPos checkPos = null;
		// check each position if already not air and generate

		checkPos = coords.add(0, 1, 0).toPos();
		IBlockState blockState = world.getBlockState(checkPos);
		if (blockState.getMaterial() != Material.AIR)
			// TODO build log in the right orientation
			world.setBlockState(checkPos, Blocks.LOG.getDefaultState(), 1);
		
		checkPos = coords.add(0, 2, 0).toPos();
		if (blockState.getMaterial() != Material.AIR)
			world.setBlockState(checkPos, Blocks.SAND.getDefaultState(), 1);
		
		checkPos = surfaceCoords.add(0, -2, 0).toPos();
		if (blockState.getMaterial() != Material.AIR)
			world.setBlockState(checkPos, Blocks.SAND.getDefaultState(), 1);
		
		checkPos = surfaceCoords.add(0, -3, 0).toPos();
		if (blockState.getMaterial() != Material.AIR)
			world.setBlockState(checkPos, Blocks.LOG.getDefaultState(), 1);
		
		Treasure.logger.debug("Generated Simple Short Shaft Chamber at " + coords);
		return true;
	}
}
