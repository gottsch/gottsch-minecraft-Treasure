/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class TreasureFeatures {
	public static final SurfaceChestFeature SURFACE_CHEST_FEATURE = new SurfaceChestFeature(NoFeatureConfig::deserialize);
	public static final SubmergedChestFeature SUBMERGED_CHEST_FEATURE = new SubmergedChestFeature(NoFeatureConfig::deserialize);
	public static final WellFeature WELL_FEATURE = new WellFeature(NoFeatureConfig::deserialize);
	public static final GemOreFeature GEM_ORE_FEATURE = new GemOreFeature(OreFeatureConfig::deserialize);
	public static final WitherTreeFeature WITHER_TREE_FEATURE = new WitherTreeFeature(NoFeatureConfig::deserialize);
	
	// list of features used for persisting to world save
	public static final List<ITreasureFeature> PERSISTED_FEATURES = new ArrayList<>();

	static {
		PERSISTED_FEATURES.add(SURFACE_CHEST_FEATURE);
		PERSISTED_FEATURES.add(SUBMERGED_CHEST_FEATURE);
		PERSISTED_FEATURES.add(WELL_FEATURE);
		PERSISTED_FEATURES.add(WITHER_TREE_FEATURE);
	}
	
	/**
	 * This method is called in Treasure.setup(final FMLCommonSetupEvent event) by a DeferredWorkQueue.
	 * This method assigns the Features to all applicable biomes
	 */
	public static void init() {
		
		for (Biome biome : ForgeRegistries.BIOMES) {
			// TODO how to check if feature is enabled? ie wither trees, or ore?
			

			// TODO make a list in BiomeHelper for all the oceans
			if (biome == Biomes.OCEAN || biome == Biomes.COLD_OCEAN || biome == Biomes.DEEP_COLD_OCEAN || biome == Biomes.DEEP_FROZEN_OCEAN ||
					biome == Biomes.DEEP_LUKEWARM_OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.DEEP_WARM_OCEAN || biome == Biomes.FROZEN_OCEAN
					|| biome == Biomes.LUKEWARM_OCEAN || biome == Biomes.WARM_OCEAN) {

				biome.addFeature(Decoration.RAW_GENERATION, SUBMERGED_CHEST_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
			}
			else {
				biome.addFeature(Decoration.RAW_GENERATION, SURFACE_CHEST_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
				// TODO change to feature.isEnabled()
				if (TreasureConfig.WELLS.isEnabled()) {
					Treasure.LOGGER.debug("registering well feature with biome -> {}", biome.getRegistryName());
					biome.addFeature(Decoration.RAW_GENERATION, WELL_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
				}
				
				if (WITHER_TREE_FEATURE.isEnabled()) {
					biome.addFeature(Decoration.RAW_GENERATION, WITHER_TREE_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
				}
			}
			
			// gem ore
			if (TreasureConfig.GEMS_AND_ORES.enableGemOreSpawn.get()) {
				// add ruby
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GEM_ORE_FEATURE
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
								TreasureBlocks.RUBY_ORE.getDefaultState(), TreasureConfig.GEMS_AND_ORES.rubyOreVeinsPerChunk.get())) // veins per chunk
						.withPlacement(Placement.COUNT_RANGE.configure(
								new CountRangeConfig(
										TreasureConfig.GEMS_AND_ORES.rubyOreVeinSize.get(), 
										TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get(),
										0, 
										TreasureConfig.GEMS_AND_ORES.rubyOreMaxY.get()))));
				
				// add sapphire
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GEM_ORE_FEATURE
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
								TreasureBlocks.SAPPHIRE_ORE.getDefaultState(), TreasureConfig.GEMS_AND_ORES.sapphireOreVeinsPerChunk.get())) // veins per chunk
						.withPlacement(Placement.COUNT_RANGE.configure(
								new CountRangeConfig(
										TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get(), 
										TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get(),
										0, 
										TreasureConfig.GEMS_AND_ORES.sapphireOreMaxY.get()))));
			}
		}
	}

	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		/*
		 * Register all Features
		 */
		@SubscribeEvent
		public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
			final IForgeRegistry<Feature<?>> registry = event.getRegistry();
			registry.register(SURFACE_CHEST_FEATURE);
		}
	}
}
