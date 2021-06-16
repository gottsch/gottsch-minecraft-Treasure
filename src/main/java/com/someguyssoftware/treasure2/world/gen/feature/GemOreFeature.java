package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2021
 *
 */
// TODO extend OreFeature and call place(), perform prob test, then call super.place();
public class GemOreFeature extends OreFeature {
	private Map<String, Integer> chunksSinceLastDimensionOre	= new HashMap<>();
	
//	public GemOreFeature(Function<Dynamic<?>, ? extends OreFeatureConfig> configFactory) {
	public GemOreFeature(Codec<OreFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "gem_ore");

		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate GemOreFeature:", e);
		}
	}
	
	/**
	 * NOTE not needed
	 */
	public void init() {
		// setup dimensional properties
		for (String dimension : TreasureConfig.GENERAL.dimensionsWhiteList.get()) {
			chunksSinceLastDimensionOre.put(dimension, 0);
		}
	}


	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random rand, BlockPos pos, OreFeatureConfig config) {
//		String dimensionName = seedReader.getLevel().getDimension().getType().getRegistryName().toString();
//		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
//		Treasure.LOGGER.debug("placing @ {} in biome category -> {}", pos.toShortString(), seedReader.getBiome(pos).getBiomeCategory().getName());
		Treasure.LOGGER.debug("dimension -> {}, location -> {}", seedReader.getLevel().dimension().getRegistryName(), seedReader.getLevel().dimension().location());
		// ore only generates in overworld

		if (!WorldInfo.isSurfaceWorld(seedReader.getLevel(), pos)) {
			Treasure.LOGGER.debug("not a surface world...");
			return false;
		}

		// inspect block to determine generation probability
		double prob = 0;
		if (config.state.getBlock() == TreasureBlocks.RUBY_ORE) {
			Treasure.LOGGER.debug("ruby gem");
			prob = TreasureConfig.GEMS_AND_ORES.rubyGenProbability.get();
		}
		else {
			Treasure.LOGGER.debug("sapphire gem: view size -> {}", TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get());
			prob = TreasureConfig.GEMS_AND_ORES.sapphireGenProbability.get();
		}
		Treasure.LOGGER.debug("config probability -> {}", prob);
		
		// test the probability
		if (!RandomHelper.checkProbability(rand, prob)) {
			Treasure.LOGGER.debug("probability NOT met");
			return false;
		}
		
		return super.place(seedReader, generator, rand, pos, config);
	}
}
