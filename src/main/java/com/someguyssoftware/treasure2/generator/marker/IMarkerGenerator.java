/**
 * 
 */
package com.someguyssoftware.treasure2.generator.marker;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 27, 2019
 *
 */
public interface IMarkerGenerator<RESULT extends IGeneratorResult<?>> {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public abstract RESULT generate(World world, Random random, ICoords spawnCoords);
	
}