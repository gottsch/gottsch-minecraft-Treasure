/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
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
	public static ConfiguredFeature<?, ?> WITHER_TREE_FEATURE_CONFIG;
	
	public static ConfiguredFeature<?, ?> RUBY_ORE_FEATURE_CONFIG;
	public static ConfiguredFeature<?, ?> SAPPHIRE_ORE_FEATURE_CONFIG;

	// list of features used for persisting to world save
	public static final List<ITreasureFeature> PERSISTED_FEATURES = new ArrayList<>();

	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		/*
		 * Register all Features
		 */
		@SubscribeEvent
		public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
			Treasure.LOGGER.info("registering features and configs...");
			// initialize features
			SURFACE_CHEST_FEATURE = new SurfaceChestFeature(NoFeatureConfig.CODEC);
			SUBMERGED_CHEST_FEATURE = new SubmergedChestFeature(NoFeatureConfig.CODEC);
			WELL_FEATURE = new WellFeature(NoFeatureConfig.CODEC);
			GEM_ORE_FEATURE = new GemOreFeature(OreFeatureConfig.CODEC);
			WITHER_TREE_FEATURE = new WitherTreeFeature(NoFeatureConfig.CODEC);

			// add features to persisted list to be accessed during world load/save
			PERSISTED_FEATURES.add(SURFACE_CHEST_FEATURE);
//			PERSISTED_FEATURES.add(SUBMERGED_CHEST_FEATURE);
			PERSISTED_FEATURES.add(WELL_FEATURE);
			PERSISTED_FEATURES.add(WITHER_TREE_FEATURE);

			final IForgeRegistry<Feature<?>> registry = event.getRegistry();
			registry.register(SURFACE_CHEST_FEATURE);
//			registry.register(SUBMERGED_CHEST_FEATURE);
			registry.register(GEM_ORE_FEATURE);
			registry.register(WELL_FEATURE);
			registry.register(WITHER_TREE_FEATURE);
			
			// initialize configs
			// NEW WAY
			// init the feature configs
			SURFACE_CHEST_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "surface_chest",
					SURFACE_CHEST_FEATURE.configured(IFeatureConfig.NONE));
//			SUBMERGED_CHEST_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "submerged_chest",
//					SUBMERGED_CHEST_FEATURE.configured(IFeatureConfig.NONE));
			WELL_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "well",
					WELL_FEATURE.configured(IFeatureConfig.NONE));
			WITHER_TREE_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "wither_tree",
					WITHER_TREE_FEATURE.configured(IFeatureConfig.NONE));
					
			RUBY_ORE_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "ruby_ore",
					GEM_ORE_FEATURE
							.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
									TreasureBlocks.RUBY_ORE.defaultBlockState(),
									TreasureConfig.GEMS_AND_ORES.rubyOreVeinSize.get()))
							.decorated(
									Placement.RANGE.configured(
											new TopSolidRangeConfig(TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get(), 
													0, 
													TreasureConfig.GEMS_AND_ORES.rubyOreMaxY.get() - TreasureConfig.GEMS_AND_ORES.rubyOreMinY.get())))
							.squared()
							.count(TreasureConfig.GEMS_AND_ORES.rubyOreVeinsPerChunk.get()));

			SAPPHIRE_ORE_FEATURE_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "sapphire_ore",
					GEM_ORE_FEATURE
							.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
									TreasureBlocks.SAPPHIRE_ORE.defaultBlockState(),
									TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get()))
							.decorated(
									Placement.RANGE.configured(
											new TopSolidRangeConfig(TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get(), 
													0, 
													TreasureConfig.GEMS_AND_ORES.sapphireOreMaxY.get() - TreasureConfig.GEMS_AND_ORES.sapphireOreMinY.get())))
							.squared()
							.count(TreasureConfig.GEMS_AND_ORES.sapphireOreVeinsPerChunk.get()));
		}
	}
	
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class BiomeRegistrationHandler {
		/*
		 * Register the Features with Biomes
		 */
		@SubscribeEvent
		public static void onBiomeLoading(final BiomeLoadingEvent biomeEvent) {

			// TODO could change this to WorldInfo.isSurfaceWorld();
			if ( biomeEvent.getCategory() == Biome.Category.NETHER || biomeEvent.getCategory() == Biome.Category.THEEND) {
				return;
			}

			ResourceLocation biome = biomeEvent.getName();
			
			if (TreasureConfig.GEMS_AND_ORES.enableGemOreSpawn.get()) {
				biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
						.add(() -> RUBY_ORE_FEATURE_CONFIG);
				biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
						.add(() -> SAPPHIRE_ORE_FEATURE_CONFIG);
			}
			
			if (biomeEvent.getCategory() == Biome.Category.OCEAN) {
//				biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION)
//						.add(() -> SUBMERGED_CHEST_FEATURE_CONFIG);
			} else {
				biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION)
						.add(() -> SURFACE_CHEST_FEATURE_CONFIG);
				if (TreasureConfig.WELLS.isEnabled()) {
					// test if the biome is allowed
					TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, TreasureConfig.WELLS.getBiomeWhiteList(), TreasureConfig.WELLS.getBiomeBlackList());
					if(biomeCheck != Result.BLACK_LISTED ) {
						biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION)
								.add(() -> WELL_FEATURE_CONFIG);
					}
				}
				
				if (TreasureConfig.WITHER_TREE.enableWitherTree.get()) {
					TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, TreasureConfig.WITHER_TREE.getBiomeWhiteList(), TreasureConfig.WITHER_TREE.getBiomeBlackList());
					if(biomeCheck != Result.BLACK_LISTED ) {
						biomeEvent.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION)
								.add(() -> WITHER_TREE_FEATURE_CONFIG);
					}
				}
			}			
		}
	}
}
