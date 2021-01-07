/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen.feature;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
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

	/**
	 * This method is called in Treasure.setup(final FMLCommonSetupEvent event) by a DeferredWorkQueue.
	 * This method assigns the Features to all applicable biomes
	 */
	public static void addFeatureToBiomes() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			biome.addFeature(Decoration.RAW_GENERATION, SURFACE_CHEST_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
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
