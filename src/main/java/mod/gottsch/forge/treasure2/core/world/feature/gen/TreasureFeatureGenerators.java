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

import java.util.Optional;

import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.registry.FeatureGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.AquaticChestFeatureGeneratorSelector;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.DeferredWitherFeatureGeneratorSelector;
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
	public static final IFeatureGenerator DEFERRED_WITHER_FEATURE_GENERATOR = new DeferredWitherFeatureGenerator();
	public static final IFeatureGenerator SUBAQUATIC_FEATURE_GENERATOR = new SubaquaticStructureFeatureGenerator();
	
	// feature generator selectors
	public static final IFeatureGeneratorSelector STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR = new WeightedChestFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector WITHER_FEATURE_GENERATOR_SELECTOR = new DeferredWitherFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR = new AquaticChestFeatureGeneratorSelector();
	
	static {
		/*
		 * Default feature generators setup statically. 
		 * NOTE currently only 1 setup for a feature, ie non-dimensional.
		 */
		// setup pre-made feature generators
		WeightedChestFeatureGeneratorSelector selector = (WeightedChestFeatureGeneratorSelector)STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR;
		selector.add(10, TreasureFeatureGenerators.SIMPLE_SURFACE_FEATURE_GENERATOR);
		selector.add(65, TreasureFeatureGenerators.PIT_FEATURE_GENERATOR);
		selector.add(25, SURFACE_STRUCTURE_FEATURE_GENERATOR);
	}
	
	/**
	 * 
	 */
	public static void initialize() {
		if (Config.chestConfig != null) {
			if (!Config.chestConfig.getGenerators().isEmpty()) {
				// clear the default values
				((WeightedChestFeatureGeneratorSelector)STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR).clear();
				// add generators from config
				Generator generator = Config.chestConfig.getGenerator(FeatureType.TERRANEAN.getValue());
				if (generator != null) {
					if (generator.getFeatureGenerators() != null && ! generator.getFeatureGenerators().isEmpty()) {
						generator.getFeatureGenerators().forEach(fg -> {
							// get the feature generator from the register
							Optional<IFeatureGenerator> featureGenerator = FeatureGeneratorRegistry.get(ModUtil.asLocation(fg.getName()));
							if (featureGenerator.isPresent()) {
								((WeightedChestFeatureGeneratorSelector)STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR).add(fg.getWeight(), featureGenerator.get());
							}
						});
					}
				}
			}
		}
	}
}
