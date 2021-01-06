/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * NOTE: Feature is the equivalent to 1.12 WorldGenerator
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class ChestFeature extends Feature<NoFeatureConfig> {

	/**
	 * 
	 * @param configFactory
	 */
	public ChestFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "treasure_chest");
	}

	/**
	 * NOTE equivalent to 1.12 generate()
	 */
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random,
			BlockPos pos, NoFeatureConfig config) {
		
		// TODO do what a world gen normally does here.
//		Treasure.LOGGER.info("chest feature works @ {}", pos);

		return true;
	}

}
