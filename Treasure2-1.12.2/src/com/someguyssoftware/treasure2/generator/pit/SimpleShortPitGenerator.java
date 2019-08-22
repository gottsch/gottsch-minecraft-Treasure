package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GeneratorChestData;
import com.someguyssoftware.treasure2.generator.TreasureGeneratorResult;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SimpleShortPitGenerator extends AbstractPitGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param coords
	 * @return
	 */
	public TreasureGeneratorResult<GeneratorChestData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		Treasure.logger.debug("Generating SimpleShortShaft.");
		TreasureGeneratorResult<GeneratorChestData> result = new TreasureGeneratorResult<>(true, new GeneratorChestData());
		result.getData().setSpawnCoords(spawnCoords);
		result.getData().setChestCoords(spawnCoords);
		
		ICoords checkCoords = null;
		// check each position if already not air and generate

		checkCoords = spawnCoords.add(0, 1, 0);
		IBlockState blockState = world.getBlockState(checkCoords.toPos());
		if (blockState.getMaterial() != Material.AIR) {
			buildLogLayer(world, random, checkCoords, Blocks.LOG);
		}
		
		checkCoords = spawnCoords.add(0, 2, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLayer(world, checkCoords, Blocks.SAND);
		}
		
		checkCoords = surfaceCoords.add(0, -2, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLayer(world, checkCoords, Blocks.SAND);
		}
		
		checkCoords = surfaceCoords.add(0, -3, 0);
		if (blockState.getMaterial() != Material.AIR) {
			buildLogLayer(world, random, checkCoords, Blocks.LOG);
		}
		
		Treasure.logger.debug("Generated Simple Short Shaft Chamber at " + spawnCoords.toShortString());
		return result;
	}
}
