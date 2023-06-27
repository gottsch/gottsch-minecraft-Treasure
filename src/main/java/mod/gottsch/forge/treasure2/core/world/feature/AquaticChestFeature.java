/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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

import java.util.Optional;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.FeatureGeneratorSelectorRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * TODO Terrestraial and AquaticChestFeature are extactly the same. Merge into one parent class and
 * make the constructor take in the FeatureType.
 * @author Mark Gottschling on Jan 27, 2021
 *
 */
public class AquaticChestFeature extends ChestFeature {

	// feature type
	private static IFeatureType FEATURE_TYPE= FeatureType.AQUATIC;
	
	// NOTE aquatic chest feature does not use waitChunkCount, since even if you spawn on an island in an ocean
	// the closest chest will still probably not be immediately visible/accessible.

	/**
	 * 
	 * @param configuration
	 */
	public AquaticChestFeature(Codec<NoneFeatureConfiguration> configuration) {
		super(configuration);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel genLevel = context.level();
		ResourceLocation dimension = WorldInfo.getDimension(genLevel.getLevel());

		// test the dimension
		if (!meetsDimensionCriteria(dimension)) { 
			return false;
		}
		
		// get the chest registry
		GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
		if (chestCache == null) {
			Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & AQUATIC. This shouldn't be. Should be initialized.");
			return false;
		}
		
		// get the generator config
		ChestFeaturesConfiguration config = Config.chestConfig; //Config.chestConfigMap.get(dimension);
		if (config == null) {
			Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
			return false;
		}
		
		Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
			return false;
		}
		
		ICoords spawnCoords = WorldInfo.getOceanFloorSurfaceCoords(genLevel.getLevel(), context.chunkGenerator(),
				new Coords(context.origin().offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == Coords.EMPTY) {
			return false;
		}
		
		// determine what type to generate
		IRarity rarity = (IRarity) RarityLevelWeightedChestGeneratorRegistry.getNextRarity(dimension, FEATURE_TYPE);
//		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity == Rarity.NONE) {
			Treasure.LOGGER.warn("unable to obtain the next rarity for generator -> {}", FEATURE_TYPE);
			return false;
		}
		Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
		if (!rarityConfig.isPresent()) {
			Treasure.LOGGER.warn("unable to locate rarity config for rarity -> {}", rarity);
			return false;
		}
		
		if (!meetsBiomeCriteria(genLevel.getLevel(), spawnCoords, rarityConfig.get().getBiomeWhitelist(), rarityConfig.get().getBiomeBlacklist())) {
			return false;
		}

		// check against all registered chests
		if (!meetsProximityCriteria(genLevel, dimension, FEATURE_TYPE, spawnCoords, generatorConfig.getMinBlockDistance())) {
			return false;
		}
		
		// check if meets the probability criteria. this is used as a randomizer so that chests aren't predictably placed.
		if (!meetsProbabilityCriteria(context.random(), generatorConfig)) {
			// place a placeholder chest in the registry
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}
		
		// select the feature generator
		Optional<IFeatureGeneratorSelector> generatorSelector = FeatureGeneratorSelectorRegistry.getSelector(FEATURE_TYPE, rarity);
		if (!generatorSelector.isPresent()) {
			Treasure.LOGGER.warn("unable to obtain a generator selector for rarity - >{}", rarity);
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}
		
		// select the generator
		IFeatureGenerator featureGenerator = generatorSelector.get().select();
		Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
		// call generate
		Optional<GeneratorResult<ChestGeneratorData>> result = featureGenerator.generate(new FeatureGenContext(context, FEATURE_TYPE), spawnCoords, rarity, rarityConfig.get());

		if (result.isPresent()) {
			cacheGeneratedChest(context.level(), rarity, FEATURE_TYPE, chestCache, result.get());
			updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
		} else {
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}
		
		// save world data
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}
}
