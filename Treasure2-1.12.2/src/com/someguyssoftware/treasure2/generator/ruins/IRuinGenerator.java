/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult;
import com.someguyssoftware.treasure2.generator.TreasureGeneratorData;
import com.someguyssoftware.treasure2.generator.TreasureGeneratorResult;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Aug 23, 2019
 *
 */
public interface IRuinGenerator<RESULT extends ITreasureGeneratorResult<?>> {

	TreasureGeneratorResult<TreasureGeneratorData> generate2(World world, Random random, ICoords spawnCoords);

	void buildOneTimeSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> proximityCoords,
			Quantity quantity, double d);

	void buildVanillaSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> spawnerCoords);

}
