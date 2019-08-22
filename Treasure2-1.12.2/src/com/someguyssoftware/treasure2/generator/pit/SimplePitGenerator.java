package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GeneratorChestData;
import com.someguyssoftware.treasure2.generator.TreasureGeneratorResult;

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
	 */
	public SimplePitGenerator() {
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
	public TreasureGeneratorResult<GeneratorChestData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		TreasureGeneratorResult<GeneratorChestData> result = super.generate(world, random, surfaceCoords, spawnCoords);
		if (result.isSuccess()) {
			Treasure.logger.debug("Generated Simple Pit at " + spawnCoords.toShortString());
		}
		return result;
	}	
}
