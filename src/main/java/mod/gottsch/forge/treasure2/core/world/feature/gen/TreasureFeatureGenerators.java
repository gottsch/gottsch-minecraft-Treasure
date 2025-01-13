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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.DeferredGeneratorBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.registry.FeatureGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.*;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on May 12, 2023
 *
 */
public class TreasureFeatureGenerators {

	// feature generators
	public static final IFeatureGenerator SIMPLE_SURFACE_FEATURE_GENERATOR = new SimpleSurfaceChestFeatureGenerator();

	// the actual feature generators that are called by a Tickable Block Entity
	public static final IFeatureGenerator WITHER_FEATURE_GENERATOR = new WitherFeatureGenerator();
	public static final IFeatureGenerator SURFACE_STRUCTURE_FEATURE_GENERATOR = new SurfaceStructureFeatureGenerator();
	public static final IFeatureGenerator SUBAQUATIC_FEATURE_GENERATOR = new SubaquaticStructureFeatureGenerator();
	public static final IFeatureGenerator PIT_FEATURE_GENERATOR = new PitChestFeatureGenerator();

	/*
	 * a deferred feature generators (ie generator proxy) that places a Tickable Block Entity, which calls the actual feature generator to generate at a specific player proximity.
	 */
	public static final IFeatureGenerator DEFERRED_WITHER_FEATURE_GENERATOR =
			new DeferredFeatureGenerator(new ResourceLocation(Treasure.MODID, "deferred_wither"), (DeferredGeneratorBlock) TreasureBlocks.DEFERRED_WITHER_TREE_GENERATOR.get());
	public static final IFeatureGenerator DEFERRED_SURFACE_STRUCTURE_FEATURE_GENERATOR =
			new DeferredFeatureGenerator(new ResourceLocation(Treasure.MODID, "deferred_subaquatic_structure"), (DeferredGeneratorBlock) TreasureBlocks.DEFERRED_SUBAQUATIC_GENERATOR.get());
	public static final IFeatureGenerator DEFERRED_SUBAQUATIC_FEATURE_GENERATOR =
			new DeferredFeatureGenerator(new ResourceLocation(Treasure.MODID, "deferred_surface_structure"), (DeferredGeneratorBlock) TreasureBlocks.DEFERRED_SURFACE_GENERATOR.get());

	//	public static final IFeatureGenerator DEFERRED_PIT_FEATURE_GENERATOR = new DeferredPitFeatureGenerator();
	public static final IFeatureGenerator DEFERRED_PIT_FEATURE_GENERATOR =
		new DeferredFeatureGenerator(new ResourceLocation(Treasure.MODID, "deferred_pit"), (DeferredGeneratorBlock) TreasureBlocks.DEFERRED_PIT_GENERATOR.get());

	// feature generator selectors
	public static final IFeatureGeneratorSelector STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR = new WeightedChestFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector WITHER_FEATURE_GENERATOR_SELECTOR = new DeferredWitherFeatureGeneratorSelector();
	public static final IFeatureGeneratorSelector DEFERRED_AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR = new DeferredAquaticFeatureGeneratorSelector();
	@Deprecated
	public static final IFeatureGeneratorSelector AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR = new AquaticChestFeatureGeneratorSelector();
	
	static {
		/*
		 * Default feature generators setup statically. If loading of the treasure2-chests toml works correctly,
		 * it will clear these values and re-initialize.
		 * NOTE currently only 1 setup for a feature, ie non-dimensional.
		 */
		// setup pre-made feature generators
		WeightedChestFeatureGeneratorSelector selector = (WeightedChestFeatureGeneratorSelector)STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR;
		selector.add(10, TreasureFeatureGenerators.SIMPLE_SURFACE_FEATURE_GENERATOR);
		selector.add(65, TreasureFeatureGenerators.DEFERRED_PIT_FEATURE_GENERATOR);
		selector.add(25, DEFERRED_SURFACE_STRUCTURE_FEATURE_GENERATOR);
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
