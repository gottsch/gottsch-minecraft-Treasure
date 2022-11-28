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
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import java.util.List;

import mod.gottsch.forge.treasure2.core.world.feature.TreasurePlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
public class TreasureOreGeneration {
	
	public static void generateOres(final BiomeLoadingEvent event) {
		List<Holder<PlacedFeature>> base = 
				event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);
		
		base.add(TreasurePlacedFeatures.TOPAZ_ORE_PLACED);
		base.add(TreasurePlacedFeatures.ONYX_ORE_PLACED);
		base.add(TreasurePlacedFeatures.RUBY_ORE_PLACED);
		base.add(TreasurePlacedFeatures.SAPPHIRE_ORE_PLACED);
	}
}
