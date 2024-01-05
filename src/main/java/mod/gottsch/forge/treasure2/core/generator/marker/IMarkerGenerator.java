/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.marker;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;

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
	public abstract RESULT generate(IServerWorld world, Random random, ICoords spawnCoords);
	
	/**
	 * 
	 * @param world
	 * @param generator
	 * @param random
	 * @param spawnCoords
	 * @return
	 */
	public abstract RESULT generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords spawnCoords);
}