/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

/**
 * TODO should be moved to datagen package as it is only used there.
 * @author Mark Gottschling Jul 7, 2023
 *
 */
public class TreasurePlacedFeatures {
	public static final ResourceKey<PlacedFeature> TOPAZ_PLACED_KEY = createKey("topaz_placed");
	public static final ResourceKey<PlacedFeature> ONYX_PLACED_KEY = createKey("onyx_placed");
	public static final ResourceKey<PlacedFeature> RUBY_PLACED_KEY = createKey("ruby_placed");
	public static final ResourceKey<PlacedFeature> SAPPHIRE_PLACED_KEY = createKey("sapphire_placed");
	
	/**
	 * 
	 * @param context
	 */
	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

		/*
		 * placement
		 * NOTE: the aboveBottom values are OFFSETS, ex -64 + x = placement depth
		 * @54 -> -64 + 54 = -10, @ 84 -> -64 + 84 = 20, therefor the range is from y = -10 to 20
		 */
		register(context, TOPAZ_PLACED_KEY,
				configuredFeatures.getOrThrow(TreasureConfiguredFeatures.OVERWORLD_TOPAZ_ORE_KEY),
				TreasureOrePlacement.commonOrePlacement(1, // veins per chunk
						HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), VerticalAnchor.aboveBottom(84))));

		register(context, ONYX_PLACED_KEY,
				configuredFeatures.getOrThrow(TreasureConfiguredFeatures.OVERWORLD_ONYX_ORE_KEY),
				TreasureOrePlacement.commonOrePlacement(1, // veins per chunk
						HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), VerticalAnchor.aboveBottom(84))));
		
		register(context, RUBY_PLACED_KEY,
				configuredFeatures.getOrThrow(TreasureConfiguredFeatures.OVERWORLD_RUBY_ORE_KEY),
				TreasureOrePlacement.commonOrePlacement(1, // veins per chunk
						HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), VerticalAnchor.aboveBottom(70))));
		
		register(context, SAPPHIRE_PLACED_KEY,
				configuredFeatures.getOrThrow(TreasureConfiguredFeatures.OVERWORLD_SAPPHIRE_ORE_KEY),
				TreasureOrePlacement.commonOrePlacement(1, // veins per chunk
						HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), VerticalAnchor.aboveBottom(70))));
	}

	private static ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Treasure.MODID, name));
	}

	private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
			Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
		context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
	}

	private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
			Holder<ConfiguredFeature<?, ?>> configuration, PlacementModifier... modifiers) {
		register(context, key, configuration, List.of(modifiers));
	}
}
