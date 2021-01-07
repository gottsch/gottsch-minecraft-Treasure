/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

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
public class SurfaceChestFeature extends Feature<NoFeatureConfig> {
protected static int UNDERGROUND_OFFSET = 5;
	
	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	
	/**
	 * 
	 * @param configFactory
	 */
	public SurfaceChestFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "treasure_chest");
		
		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate SurfaceChestFeature:", e);
		}
	}

	/**
	 * 
	 */
	public void init() {
		// TODO all these values need to be indexed by dimension
		
		// initialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		
		// setup temporary rarity-generators map
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}
	}
	
	/**
	 * NOTE equivalent to 1.12 generate()
	 */
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random,
			BlockPos pos, NoFeatureConfig config) {
		
		// test the dimension white list
		// TODO test what the dimension names are.  ex minecraft:overworld
		// TODO setup config with dimension whitelist names
		// TODO chests should have a properties for the depth range from "surface"
		if (TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(world.getDimension().getType().getRegistryName().toString())) {
			generate(world, random, chunkX, chunkZ);
		}
		
		// TODO do what a world gen normally does here.
//		Treasure.LOGGER.info("chest feature works @ {}", pos);

		
		return true;
	}

}
