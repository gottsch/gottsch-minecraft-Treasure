/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.world.feature;

import java.util.List;

import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasureOrePlacement {
	
	   public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
		      return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
		   }

		   public static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier modifier) {
		      return orePlacement(CountPlacement.of(veinsPerChunk), modifier);
		   }

		   public static List<PlacementModifier> rareOrePlacement(int veinsPerChunk, PlacementModifier modifier) {
		      return orePlacement(RarityFilter.onAverageOnceEvery(veinsPerChunk), modifier);
		   }
}
