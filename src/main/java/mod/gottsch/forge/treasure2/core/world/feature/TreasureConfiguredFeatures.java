/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.world.feature;

import java.util.List;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasureConfiguredFeatures {
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, Treasure.MODID);
	private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Treasure.MODID);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Treasure.MODID);
    
	public static final List<OreConfiguration.TargetBlockState> OVERWORLD_TOPAZ_ORES = List.of(
			OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, TreasureBlocks.TOPAZ_ORE.get().defaultBlockState()),
			OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get().defaultBlockState())
			);

	public static final List<OreConfiguration.TargetBlockState> OVERWORLD_ONYX_ORES = List.of(
			OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, TreasureBlocks.ONYX_ORE.get().defaultBlockState()),
			OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, TreasureBlocks.DEEPSLATE_ONYX_ORE.get().defaultBlockState())
			);
	
	public static final List<OreConfiguration.TargetBlockState> OVERWORLD_RUBY_ORES = List.of(
			OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, TreasureBlocks.RUBY_ORE.get().defaultBlockState()),
			OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, TreasureBlocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState())
			);
	
	public static final List<OreConfiguration.TargetBlockState> OVERWORLD_SAPPHIRE_ORES = List.of(
			OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, TreasureBlocks.SAPPHIRE_ORE.get().defaultBlockState()),
			OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get().defaultBlockState())
			);
	
	// last param = vein size (shouldn't be less than 3)
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> TOPAZ_ORE = 
			FeatureUtils.register("topaz_ore", Feature.ORE, new OreConfiguration(OVERWORLD_TOPAZ_ORES, 3));
	
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ONYX_ORE = 
			FeatureUtils.register("onyx_ore", Feature.ORE, new OreConfiguration(OVERWORLD_ONYX_ORES, 3));
	
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> RUBY_ORE = 
			FeatureUtils.register("ruby_ore", Feature.ORE, new OreConfiguration(OVERWORLD_RUBY_ORES, 3));
	
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> SAPPHIRE_ORE = 
			FeatureUtils.register("sapphire_ore", Feature.ORE, new OreConfiguration(OVERWORLD_SAPPHIRE_ORES, 3));
	
	// TEMP
//	public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> TERRESTRIAL_CHEST = 
//			FeatureUtils.register("terrestrial_chest", new TerrestrialChestFeature(NoneFeatureConfiguration.CODEC));
	
    // Feature
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TERRESTRIAL_CHEST = FEATURES.register("terrestrial_chest",
            () -> new TerrestrialChestFeature(NoneFeatureConfiguration.CODEC));
    
    public static final RegistryObject<ConfiguredFeature<?, ?>> TERRESTRIAL_CHEST_CONFIGURED = CONFIGURED_FEATURES.register("terrestrial_chest",
            () -> new ConfiguredFeature<>(TERRESTRIAL_CHEST.get(), NoneFeatureConfiguration.INSTANCE));

    // Placement
    public static final RegistryObject<PlacedFeature> TERRESTRIAL_CHEST_PLACED = PLACED_FEATURES.register("my_kelp",
            () -> new PlacedFeature(TERRESTRIAL_CHEST_CONFIGURED.getHolder().get(), List.of(BiomeFilter.biome())))));

//        	public static final Holder<PlacedFeature> TERRESTRIAL_CHEST_PLACED = PlacementUtils.register("terrestrial_chest_placed", 
//			TreasureConfiguredFeatures.TERRESTRIAL_CHEST);
    
    	    public static void register() {
    	    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	        CONFIGURED_FEATURES.register(bus);
    	        PLACED_FEATURES.register(bus);
    	        FEATURES.register(bus);
    	        
    	    }
}
