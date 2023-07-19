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
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasureConfiguredFeatures {
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Treasure.MODID);

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TOPAZ_ORE_KEY = registerKey("topaz_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ONYX_ORE_KEY = registerKey("onyx_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RUBY_ORE_KEY = registerKey("ruby_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SAPPHIRE_ORE_KEY = registerKey("sapphire_ore");

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TERRANEAN_CHEST = registerKey("terranean_chest");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SUBAQUATIC_CHEST = registerKey("subaquatic_chest");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_WELL_CHEST = registerKey("well");

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

	// Feature
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TERRANEAN_CHEST = FEATURES.register("terranean_chest",
			() -> new TerraneanChestFeature(NoneFeatureConfiguration.CODEC));

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SUBAQUATIC_CHEST = FEATURES.register("subaquatic_chest",
			() -> new AquaticChestFeature(NoneFeatureConfiguration.CODEC));

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WELL = FEATURES.register("well",
			() -> new WellFeature(NoneFeatureConfiguration.CODEC));


	/**
	 * 
	 */
	public static void register(IEventBus bus) {
		FEATURES.register(bus);
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Treasure.MODID, name));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
			ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
