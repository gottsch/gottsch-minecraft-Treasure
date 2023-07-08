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
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasureConfiguredFeatures {
	//	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Treasure.MODID);
	//	private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, Treasure.MODID);
	//	private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, Treasure.MODID);

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TOPAZ_ORE_KEY = registerKey("topaz_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ONYX_ORE_KEY = registerKey("onyx_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RUBY_ORE_KEY = registerKey("ruby_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SAPPHIRE_ORE_KEY = registerKey("sapphire_ore");

	/**
	 * 
	 * @param context
	 */
	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		RuleTest ruleTest1 = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest ruleTest2 = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

		List<OreConfiguration.TargetBlockState> topazOres = List.of(OreConfiguration.target(ruleTest1,
				TreasureBlocks.TOPAZ_ORE.get().defaultBlockState()),
				OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get().defaultBlockState()));
		
		List<OreConfiguration.TargetBlockState> onyxOres = List.of(OreConfiguration.target(ruleTest1,
				TreasureBlocks.ONYX_ORE.get().defaultBlockState()),
				OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_ONYX_ORE.get().defaultBlockState()));
		
		List<OreConfiguration.TargetBlockState> rubyOres = List.of(OreConfiguration.target(ruleTest1,
				TreasureBlocks.RUBY_ORE.get().defaultBlockState()),
				OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState()));
		
		List<OreConfiguration.TargetBlockState> sapphireOres = List.of(OreConfiguration.target(ruleTest1,
				TreasureBlocks.SAPPHIRE_ORE.get().defaultBlockState()),
				OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get().defaultBlockState()));


		register(context, OVERWORLD_TOPAZ_ORE_KEY, Feature.ORE, new OreConfiguration(topazOres, 3));
		register(context, OVERWORLD_ONYX_ORE_KEY, Feature.ORE, new OreConfiguration(onyxOres, 3));
		register(context, OVERWORLD_RUBY_ORE_KEY, Feature.ORE, new OreConfiguration(rubyOres, 3));
		register(context, OVERWORLD_SAPPHIRE_ORE_KEY, Feature.ORE, new OreConfiguration(sapphireOres, 3));

	}
	//	public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_TOPAZ_ORES = Suppliers.memoize(() -> List.of(
	//			OreConfiguration.target(ruleTest1, TreasureBlocks.TOPAZ_ORE.get().defaultBlockState()),
	//			OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get().defaultBlockState()))
	//			);
	//
	//	public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_ONYX_ORES = Suppliers.memoize(() -> List.of(
	//			OreConfiguration.target(ruleTest1, TreasureBlocks.ONYX_ORE.get().defaultBlockState()),
	//			OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_ONYX_ORE.get().defaultBlockState()))
	//			);
	//
	//	public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_RUBY_ORES = Suppliers.memoize(() -> List.of(
	//			OreConfiguration.target(ruleTest1, TreasureBlocks.RUBY_ORE.get().defaultBlockState()),
	//			OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState()))
	//			);
	//
	//	public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SAPPHIRE_ORES = Suppliers.memoize(() -> List.of(
	//			OreConfiguration.target(ruleTest1, TreasureBlocks.SAPPHIRE_ORE.get().defaultBlockState()),
	//			OreConfiguration.target(ruleTest2, TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get().defaultBlockState()))
	//			);


	// Feature
//	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TERRANEAN_CHEST = FEATURES.register("terranean_chest",
//			() -> new TerraneanChestFeature(NoneFeatureConfiguration.CODEC));
//
//	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SUBAQUATIC_CHEST = FEATURES.register("subaquatic_chest",
//			() -> new AquaticChestFeature(NoneFeatureConfiguration.CODEC));
//
//	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WELL = FEATURES.register("well",
//			() -> new WellFeature(NoneFeatureConfiguration.CODEC));

	// Configured features
//	public static final RegistryObject<ConfiguredFeature<?, ?>> TOPAZ_ORE_CONFIGURED = CONFIGURED_FEATURES.register("topaz_ore",
//			() -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_TOPAZ_ORES.get(), 3))); // 3 = vien_size
//
//	public static final RegistryObject<ConfiguredFeature<?, ?>> ONYX_ORE_CONFIGURED = CONFIGURED_FEATURES.register("onyx_ore",
//			() -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_ONYX_ORES.get(), 3)));
//
//	public static final RegistryObject<ConfiguredFeature<?, ?>> RUBY_ORE_CONFIGURED = CONFIGURED_FEATURES.register("ruby_ore",
//			() -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_RUBY_ORES.get(), 3)));
//
//	public static final RegistryObject<ConfiguredFeature<?, ?>> SAPPHIRE_ORE_CONFIGURED = CONFIGURED_FEATURES.register("sapphire_ore",
//			() -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SAPPHIRE_ORES.get(), 3)));

	
	
//	public static final RegistryObject<ConfiguredFeature<?, ?>> TERRANEAN_CHEST_CONFIGURED = CONFIGURED_FEATURES.register("terranean_chest",
//			() -> new ConfiguredFeature<>(TERRANEAN_CHEST.get(), NoneFeatureConfiguration.INSTANCE));
//
//	public static final RegistryObject<ConfiguredFeature<?, ?>> SUBAQUATIC_CHEST_CONFIGURED = CONFIGURED_FEATURES.register("subaquatic_chest",
//			() -> new ConfiguredFeature<>(SUBAQUATIC_CHEST.get(), NoneFeatureConfiguration.INSTANCE));
//
//	public static final RegistryObject<ConfiguredFeature<?, ?>> WELL_CONFIGURED = CONFIGURED_FEATURES.register("well",
//			() -> new ConfiguredFeature<>(WELL.get(), NoneFeatureConfiguration.INSTANCE));

	/*
	 * placement
	 * NOTE: the aboveBottom values are OFFSETS, ex -64 + x = placement depth
	 * @54 -> -64 + 54 = -10, @ 84 -> -64 + 84 = 20, therefor the range is from y = -10 to 20
	 */
//	public static final RegistryObject<PlacedFeature> TOPAZ_ORE_PLACED = PLACED_FEATURES.register("topaz_ore_placed",
//			() -> new PlacedFeature(TOPAZ_ORE_CONFIGURED.getHolder().get(), TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), 
//					VerticalAnchor.aboveBottom(84)))));
//
//	public static final RegistryObject<PlacedFeature> ONYX_ORE_PLACED = PLACED_FEATURES.register("onyx_ore_placed",
//			() -> new PlacedFeature(ONYX_ORE_CONFIGURED.getHolder().get(), TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), 
//					VerticalAnchor.aboveBottom(84)))));
//
//	public static final RegistryObject<PlacedFeature> RUBY_ORE_PLACED = PLACED_FEATURES.register("ruby_ore_placed",
//			() -> new PlacedFeature(RUBY_ORE_CONFIGURED.getHolder().get(), TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), 
//					VerticalAnchor.aboveBottom(70)))));
//
//	public static final RegistryObject<PlacedFeature> SAPPHIRE_ORE_PLACED = PLACED_FEATURES.register("sapphire_ore_placed",
//			() -> new PlacedFeature(SAPPHIRE_ORE_CONFIGURED.getHolder().get(), TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), 
//					VerticalAnchor.aboveBottom(70)))));
//
//	public static final RegistryObject<PlacedFeature> TERRANEAN_CHEST_PLACED = PLACED_FEATURES.register("terranean_chest_placed",
//			() -> new PlacedFeature(TERRANEAN_CHEST_CONFIGURED.getHolder().get(), List.of(BiomeFilter.biome())));
//
//	public static final RegistryObject<PlacedFeature> SUBAQUATIC_CHEST_PLACED = PLACED_FEATURES.register("subaquatic_chest_placed",
//			() -> new PlacedFeature(SUBAQUATIC_CHEST_CONFIGURED.getHolder().get(), List.of(BiomeFilter.biome())));
//
//	public static final RegistryObject<PlacedFeature> WELL_PLACED = PLACED_FEATURES.register("well_placed",
//			() -> new PlacedFeature(WELL_CONFIGURED.getHolder().get(), List.of(BiomeFilter.biome())));

	/**
	 * 
	 */
//	public static void register(IEventBus bus) {
//		CONFIGURED_FEATURES.register(bus);
//		PLACED_FEATURES.register(bus);
//		FEATURES.register(bus);
//	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Treasure.MODID, name));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
			ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
