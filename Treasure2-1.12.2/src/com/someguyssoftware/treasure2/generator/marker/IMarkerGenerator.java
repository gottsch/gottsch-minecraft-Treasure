/**
 * 
 */
package com.someguyssoftware.treasure2.generator.marker;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.config.IWellConfig;
import com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 27, 2019
 *
 */
public interface IMarkerGenerator<RESULT extends ITreasureGeneratorResult<?>> {

	public boolean generate(World world, Random random, ICoords spawnCoords);
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public abstract RESULT generate2(World world, Random random, ICoords spawnCoords);
	
}