package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class AirPitGenerator extends AbstractPitGenerator {

	/**
	 * 
	 */
	public AirPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
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
		GeneratorResult<ChestGeneratorData> result =super.generate(world, random, surfaceCoords, spawnCoords); 
		if (result.isSuccess()) {
			Treasure.logger.debug("Generated Air Pit at " + spawnCoords.toShortString());
		}
		return result;
	}
}
