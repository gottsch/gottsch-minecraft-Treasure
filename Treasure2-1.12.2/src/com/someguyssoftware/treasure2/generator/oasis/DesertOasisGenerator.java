/**
 * 
 */
package com.someguyssoftware.treasure2.generator.oasis;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IOasisConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.world.World;

/**
 * @author Mark
 *
 */
public class DesertOasisGenerator implements IOasisGenerator<GeneratorResult<ChestGeneratorData>> {

	/**
	 * 
	 */
	public DesertOasisGenerator() {
		
	}
	
	@Override
	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords) {

		ICoords chestCoords = null;
		ICoords wellCoords = null;
		ICoords markerCoords = null;
		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// result from chest generation
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);		

		// 1. collect location data points
		ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.logger.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}
		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
		markerCoords = surfaceCoords;
		
		// TODO determine size of oasis
		
		// TODO use distance from coords to determine if pos is within radius. cycle through all blocks within diameterXdiameter area.
		// TODO think of how to use a map or 2d array to make these checks more efficient instead of calc-ing distance for each block.
		int radius = RandomHelper.randomInt(random, 15, 30);
		
		return null;
	}

	@Override
	public IOasisConfig getConfig() {
		return TreasureConfig.OASES.desertOasisProperties;
	}

}
