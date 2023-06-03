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
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.AquaticChestFeatureGeneratorSelector;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.WeightedChestFeatureGeneratorSelector;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.WitherFeatureGeneratorSelector;

/**
 * 
 * @author Mark Gottschling on May 12, 2023
 *
 */
public class TreasureFeatureGenerators {

	// feature generators
	public static final IFeatureGenerator SIMPLE_SURFACE_FEATURE_GENERATOR = new SimpleSurfaceChestFeatureGenerator();
	public static final IFeatureGenerator PIT_FEATURE_GENERATOR = new PitChestFeatureGenerator();
	public static final IFeatureGenerator SURFACE_STRUCTURE_FEATURE_GENERATOR = new SurfaceStructureFeatureGenerator();
	public static final IFeatureGenerator WITHER_FEATURE_GENERATOR = new WitherFeatureGenerator();
	public static final IFeatureGenerator SUBAQUATIC_FEATURE_GENERATOR = new SubaquaticStructureFeatureGenerator();
	
	// feature generator selectors
	public static final IFeatureGeneratorSelector STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR = new WeightedChestFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector WITHER_FEATURE_GENERATOR_SELECTOR = new WitherFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR = new AquaticChestFeatureGeneratorSelector();
	
	static {
		/*
		 * NOTE currently only 1 setup for a feature. The ChestConfiguration file/class is setup by dimension. This would require a map here of feature generators.
		 */
		// setup pre-made feature generators
		WeightedChestFeatureGeneratorSelector selector = (WeightedChestFeatureGeneratorSelector)STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR;
		selector.add(10, TreasureFeatureGenerators.SIMPLE_SURFACE_FEATURE_GENERATOR);
		selector.add(65, TreasureFeatureGenerators.PIT_FEATURE_GENERATOR);
		selector.add(25, SURFACE_STRUCTURE_FEATURE_GENERATOR);
	}
}
