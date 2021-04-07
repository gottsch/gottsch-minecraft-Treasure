/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
	// features
	public static SurfaceChestFeature SURFACE_CHEST_FEATURE;
	public static SubmergedChestFeature SUBMERGED_CHEST_FEATURE;
	public static WellFeature WELL_FEATURE;
	public static GemOreFeature GEM_ORE_FEATURE;
	public static WitherTreeFeature WITHER_TREE_FEATURE;
	
	// feature configs
	public static ConfiguredFeature<?, ?> SURFACE_CHEST_FEATURE_CONFIG;
	public static ConfiguredFeature<?, ?> SUBMERGED_CHEST_FEATURE_CONFIG;
	public static ConfiguredFeature<?, ?> WELL_FEATURE_CONFIG;
	
	public static ConfiguredFeature<?, ?> RUBY_ORE_FEATURE_CONFIG;
	public static ConfiguredFeature<?, ?> SAPPHIRE_ORE_FEATURE_CONFIG;
	
	// list of features used for persisting to world save
	public static final List<ITreasureFeature> PERSISTED_FEATURES = new ArrayList<>();
	

	
	/**
	 * This method is called in Treasure.setup(final FMLCommonSetupEvent event) by a DeferredWorkQueue.
	 * This method assigns the Features to all applicable biomes
	 */
	public static void init() {
        
		// initialize features
		SURFACE_CHEST_FEATURE = new SurfaceChestFeature(NoFeatureConfig.CODEC);
		SUBMERGED_CHEST_FEATURE = new SubmergedChestFeature(NoFeatureConfig.CODEC);
		WELL_FEATURE = new WellFeature(NoFeatureConfig.CODEC);
		GEM_ORE_FEATURE = new GemOreFeature(OreFeatureConfig.CODEC);
		WITHER_TREE_FEATURE = new WitherTreeFeature(NoFeatureConfig.CODEC);
		
		// add features to persisted list to be accessed during world load/save
		PERSISTED_FEATURES.add(SURFACE_CHEST_FEATURE);
		PERSISTED_FEATURES.add(SUBMERGED_CHEST_FEATURE);
		PERSISTED_FEATURES.add(WELL_FEATURE);
		PERSISTED_FEATURES.add(WITHER_TREE_FEATURE);

		// NEW WAY
		// init the feature configs
		SURFACE_CHEST_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "surface_chest", SURFACE_CHEST_FEATURE.configured(IFeatureConfig.NONE));
		SUBMERGED_CHEST_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "submerged_chest", SUBMERGED_CHEST_FEATURE.configured(IFeatureConfig.NONE));
		WELL_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "well", WELL_FEATURE.configured(IFeatureConfig.NONE));
	
		RUBY_ORE_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "ruby_ore",
                GEM_ORE_FEATURE.configured(
                    new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        TreasureBlocks.RUBY_ORE.defaultBlockState(), TreasureConfig.GEMS_AND_ORES.rubyOreVeinSize.get())
                ).decorated(Placement.DEPTH_AVERAGE.configured(new DepthAverageConfig(
                		(TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get() + TreasureConfig.GEMS_AND_ORES.rubyOreMaxY.get()) / 2, 
                		(TreasureConfig.GEMS_AND_ORES.rubyOreMaxY.get() - TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get()) / 2)
                		)).squared().count(TreasureConfig.GEMS_AND_ORES.rubyOreVeinsPerChunk.get())
            );
		
		SAPPHIRE_ORE_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "sapphire_ore",
                GEM_ORE_FEATURE.configured(
                    new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        TreasureBlocks.RUBY_ORE.defaultBlockState(), TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get())
                ).decorated(Placement.DEPTH_AVERAGE.configured(new DepthAverageConfig(
                		(TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get() + TreasureConfig.GEMS_AND_ORES.sapphireOreMaxY.get()) / 2, 
                		(TreasureConfig.GEMS_AND_ORES.sapphireOreMaxY.get() - TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get()) / 2)
                		)).squared().count(TreasureConfig.GEMS_AND_ORES.rubyOreVeinsPerChunk.get())
            );
		
		
//		for (Entry<RegistryKey<Biome>, Biome> biomeEntry : ForgeRegistries.BIOMES.getEntries()) {
////ForgeRegistries.BIOMES.getKey(biome);
//			RegistryKey<Biome> biomeType = biomeEntry.getKey();
//			
//			if (biomeEntry.getValue().getBiomeCategory().equals(Biome.Category.OCEAN)) {
//				// TODO
//			}
//			// TODO make a list in BiomeHelper for all the oceans
//			if (biomeType == Biomes.OCEAN || biomeEntry == Biomes.COLD_OCEAN || biomeEntry == Biomes.DEEP_COLD_OCEAN || biomeEntry == Biomes.DEEP_FROZEN_OCEAN ||
//					biomeEntry == Biomes.DEEP_LUKEWARM_OCEAN || biomeEntry == Biomes.DEEP_OCEAN || biomeEntry == Biomes.DEEP_WARM_OCEAN || biomeEntry == Biomes.FROZEN_OCEAN
//					|| biomeEntry == Biomes.LUKEWARM_OCEAN || biomeEntry == Biomes.WARM_OCEAN) {
//				biomeEntry.getValue(). (Decoration.RAW_GENERATION, SUBMERGED_CHEST_FEATURE.configured(IFeatureConfig.NONE));
//			}
//			else {
//				biomeEntry.addFeature(Decoration.RAW_GENERATION, SURFACE_CHEST_FEATURE.configured(IFeatureConfig.NONE));
//				// TODO change to feature.isEnabled()
//				if (TreasureConfig.WELLS.isEnabled()) {
//					Treasure.LOGGER.debug("registering well feature with biome -> {}", biomeEntry.getValue().getRegistryName().toString());
//					biomeEntry.addFeature(Decoration.RAW_GENERATION, WELL_FEATURE.configured(IFeatureConfig.NONE));
//				}
//				
//				if (WITHER_TREE_FEATURE.isEnabled()) {
//					biomeEntry.addFeature(Decoration.RAW_GENERATION, WITHER_TREE_FEATURE.configured(IFeatureConfig.NONE));
//				}
//			}
//			
//			// gem ore
//			if (TreasureConfig.GEMS_AND_ORES.enableGemOreSpawn.get()) {
//				// add ruby
//				biomeEntry.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GEM_ORE_FEATURE
//						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
//								TreasureBlocks.RUBY_ORE.defaultBlockState(), TreasureConfig.GEMS_AND_ORES.rubyOreVeinsPerChunk.get())) // veins per chunk
//						.withPlacement(Placement.COUNT_RANGE.configure(
//								new CountRangeConfig(
//										TreasureConfig.GEMS_AND_ORES.rubyOreVeinSize.get(), 
//										TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get(),
//										0, 
//										TreasureConfig.GEMS_AND_ORES.rubyOreMaxY.get()))));
//				
//				// add sapphire
//				biomeEntry.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GEM_ORE_FEATURE
//						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
//								TreasureBlocks.SAPPHIRE_ORE.defaultBlockState(), TreasureConfig.GEMS_AND_ORES.sapphireOreVeinsPerChunk.get())) // veins per chunk
//						.withPlacement(Placement.COUNT_RANGE.configure(
//								new CountRangeConfig(
//										TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get(), 
//										TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get(),
//										0, 
//										TreasureConfig.GEMS_AND_ORES.sapphireOreMaxY.get()))));
//			}
//		}
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
			registry.register(SUBMERGED_CHEST_FEATURE);
			registry.register(GEM_ORE_FEATURE);
			registry.register(WELL_FEATURE);
			registry.register(WITHER_TREE_FEATURE);
		}
		
		/*
		 * Register the Feature Configs
		 */
		@SubscribeEvent
	    public void onBiomeLoading(final BiomeLoadingEvent biome) {
			// TODO could change this to WorldInfo.isSurfaceWorld();
	        if(biome.getCategory() == Biome.Category.NETHER || biome.getCategory() == Biome.Category.THEEND) {
	        	return;
	        }

	        biome.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
	            .add(() -> RUBY_ORE_FEATURE_CONFIG);
	        biome.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
	        	.add(() -> SAPPHIRE_ORE_FEATURE_CONFIG);
	        
	        if (biome.getCategory() == Biome.Category.OCEAN) {
		        biome.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION)
	            .add(() -> SUBMERGED_CHEST_FEATURE_CONFIG);
	        }
	        else {
		        biome.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION)
	            .add(() -> SURFACE_CHEST_FEATURE_CONFIG);	       
	        	if (TreasureConfig.WELLS.isEnabled()) {
			        biome.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION)
		            .add(() -> WELL_FEATURE_CONFIG);	       
	        	}
	        }
	    }
	}
}
