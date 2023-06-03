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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.FeatureGeneratorSelectorRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGeneratedContext;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGeneratedContext.GeneratedType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * TODO Terrestraial and AquaticChestFeature are extactly the same. Merge into one parent class and
 * make the constructor take in the FeatureType.
 * @author Mark Gottschling on Jan 27, 2021
 *
 */
public class AquaticChestFeature extends Feature<NoneFeatureConfiguration> implements IChestFeature {

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
		GeneratedCache<ChestGeneratedContext> chestCache = DimensionalGeneratedRegistry.getChestGeneratedCache(dimension, FEATURE_TYPE);
		if (chestCache == null) {
			Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & AQUATIC. This shouldn't be. Should be initialized.");
			return false;
		}
		
		// get the generator config
		ChestConfiguration config = Config.chestConfigMap.get(dimension);
		if (config == null) {
			Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
			return false;
		}
		
		Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
			return false;
		}
		
		ICoords spawnCoords = WorldInfo.getOceanFloorSurfaceCoords(genLevel, context.chunkGenerator(),
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
		
		// TODO might have feature generator specific biome and proximity criteria checks. ie Wither
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
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords);
		}
		
		// select the feature generator
		Optional<IFeatureGeneratorSelector> generatorSelector = FeatureGeneratorSelectorRegistry.getSelector(FEATURE_TYPE, rarity);
		if (!generatorSelector.isPresent()) {
			Treasure.LOGGER.warn("unable to obtain a generator selector for rarity - >{}", rarity);
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords);
		}
		
		// select the generator
		IFeatureGenerator featureGenerator = generatorSelector.get().select();
		Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
		// call generate
		Optional<GeneratorResult<ChestGeneratorData>> result = featureGenerator.generate(new WorldGenContext(context), spawnCoords, rarity, rarityConfig.get());

		if (result.isPresent()) {
			Treasure.LOGGER.debug("feature gen result -> {}", result.get());
			// create a chest context
			ChestGeneratedContext chestGeneratedContext = new ChestGeneratedContext(
					result.get().getData().getRarity(), result.get().getData().getCoords())
					.withSurfaceCoords(result.get().getData().getSpawnCoords())
					.withFeatureType(FEATURE_TYPE)
					.withName(result.get().getData().getRegistryName());
			
			Treasure.LOGGER.debug("chestGenContext -> {}", chestGeneratedContext);
			// cache the chest at its exact location
			chestCache.cache(rarity, chestGeneratedContext.getCoords(), chestGeneratedContext);

			// update the adjusted weight collection			
			RarityLevelWeightedChestGeneratorRegistry.adjustAllWeightsExcept(dimension, FEATURE_TYPE, 1, rarity);
			Map<IFeatureType, RarityLevelWeightedCollection> map = RarityLevelWeightedChestGeneratorRegistry.RARITY_SELECTOR.get(dimension);
			RarityLevelWeightedCollection dumpCol = map.get(FEATURE_TYPE);
			List<String> dump = dumpCol.dump();
			Treasure.LOGGER.debug("weighted collection dump -> {}", dump);
		} else {
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords);
		}
		
		// save world data
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}
	
	/**
	 * TODO move to  interface IChestFeature
	 * @param random
	 * @return
	 */
	private boolean meetsProbabilityCriteria(Random random, Generator generatorConfig) {
		if (generatorConfig.getProbability() == null) {
			Treasure.LOGGER.warn("chest generator config -> '{}' is missing 'probability' value", generatorConfig.getKey());
			return false;
		}
		if (!RandomHelper.checkProbability(random, generatorConfig.getProbability())) {
			Treasure.LOGGER.debug("chest gen does not meet generate probability.");
			return false;
		}
		return true;
	}
	
	/**
	 * TODO move to  interface IChestFeature
	 * @param genLevel
	 * @param registry
	 * @param rarity
	 * @param coords
	 * @return
	 */
	private boolean failAndPlaceholdChest(WorldGenLevel genLevel, GeneratedCache<ChestGeneratedContext> registry, IRarity rarity, ICoords coords) {
		// add placeholder
		ChestGeneratedContext chestGeneratedContext = new ChestGeneratedContext(rarity, coords, GeneratedType.PLACEHOLDER).withFeatureType(FEATURE_TYPE);
		registry.cache(rarity, coords, chestGeneratedContext);
		// need to save on fail
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return false;
	}
}
