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

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasurePlacedFeatures {

	// 1= veinsPerChunk, -80 = bottom depth, 80 = top depth
	// the aboveBottom values are OFFSETS, ex -64 + x = placement depth
	// @54 -> -64 + 54 = -10, @ 84 -> -64 + 84 = 20, there the range is from y = -10 to 20
	public static final Holder<PlacedFeature> TOPAZ_ORE_PLACED = PlacementUtils.register("topaz_ore_placed", 
			TreasureConfiguredFeatures.TOPAZ_ORE, TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), 
					VerticalAnchor.aboveBottom(84))));
	
	public static final Holder<PlacedFeature> ONYX_ORE_PLACED = PlacementUtils.register("onyx_ore_placed", 
			TreasureConfiguredFeatures.ONYX_ORE, TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(44), 
					VerticalAnchor.aboveBottom(84))));
	
	public static final Holder<PlacedFeature> RUBY_ORE_PLACED = PlacementUtils.register("ruby_ore_placed", 
			TreasureConfiguredFeatures.RUBY_ORE, TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), 
					VerticalAnchor.aboveBottom(70))));
	
	public static final Holder<PlacedFeature> SAPPHIRE_ORE_PLACED = PlacementUtils.register("sapphire_ore_placed", 
			TreasureConfiguredFeatures.SAPPHIRE_ORE, TreasureOrePlacement.commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(14), 
					VerticalAnchor.aboveBottom(70))));
	
//	public static final Holder<PlacedFeature> TERRESTRIAL_CHEST_PLACED = PlacementUtils.register("terrestrial_chest_placed", 
//			TreasureConfiguredFeatures.TERRESTRIAL_CHEST);
}
