package com.someguyssoftware.treasure2.worldgen;

import com.someguyssoftware.treasure2.world.gen.feature.SurfaceChestFeature;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class TestOreGen {
	// TODO where does this get called from ?
	public static void generateOre() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome == Biomes.PLAINS) {
				ConfiguredPlacement customConfig = Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 5, 5, 25));
				biome.addFeature(GenerationStage.Decoration.RAW_GENERATION,
						new SurfaceChestFeature(NoFeatureConfig::deserialize).withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
			}
		}
	}
}
