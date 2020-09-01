package com.someguyssoftware.treasure2.generator.oasis;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.config.IOasisConfig;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.world.World;

/**
 * 
 * @author Mark
 *
 * @param <RESULT>
 */
public interface IOasisGenerator<RESULT extends IGeneratorResult<?>> {

	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords surfaceCoords);
	
	public IOasisConfig getConfig();
}
