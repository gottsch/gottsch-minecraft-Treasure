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
import java.util.Map;
import java.util.Random;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext.GeneratedType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 
 * @author Mark Gottschling on Jun 3, 2023
 *
 */
public abstract class ChestFeature extends Feature<NoneFeatureConfiguration> implements IChestFeature {

	public ChestFeature(Codec<NoneFeatureConfiguration> featureConfig) {
		super(featureConfig);
	}
	
	/**
	 * 
	 * @param random
	 * @return
	 */
	protected boolean meetsProbabilityCriteria(Random random, Generator generatorConfig) {
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
	 * 
	 * @param world
	 * @param dimension
	 * @param spawnCoords
	 * @return
	 */
	public boolean meetsProximityCriteria(ServerLevelAccessor world, ResourceLocation dimension, IFeatureType key, ICoords spawnCoords, int minDistance) {
		if (isRegisteredChestWithinDistance(world, dimension, key, spawnCoords, minDistance)) {
			Treasure.LOGGER.trace("The distance to the nearest treasure chest is less than the minimun required.");
			return false;
		}	
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param dimension
	 * @param key
	 * @param coords
	 * @param minDistance
	 * @return
	 */
	public static boolean isRegisteredChestWithinDistance(ServerLevelAccessor world, ResourceLocation dimension, IFeatureType key, ICoords coords, int minDistance) {
		GeneratedCache<? extends GeneratedContext> registry = DimensionalGeneratedCache.getChestGeneratedCache(dimension, key);
		if (registry == null || registry.getValues().isEmpty()) {
			Treasure.LOGGER.debug("unable to locate the GeneratedRegistry or the registry doesn't contain any values");
			return false;
		}

		// generate a box with coords as center and minDistance as radius
		ICoords startBox = new Coords(coords.getX() - minDistance, 0, coords.getZ() - minDistance);
		ICoords endBox = new Coords(coords.getX() + minDistance, 0, coords.getZ() + minDistance);

		// find if box overlaps anything in the registry
		if (registry.withinArea(startBox, endBox)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param genLevel
	 * @param registry
	 * @param rarity
	 * @param coords
	 * @param featureType
	 * @return
	 */
	protected boolean failAndPlaceholdChest(WorldGenLevel genLevel, GeneratedCache<GeneratedChestContext> registry, IRarity rarity, ICoords coords, IFeatureType featureType) {
		// add placeholder
		GeneratedChestContext generatedChestContext = new GeneratedChestContext(rarity, coords, GeneratedType.PLACEHOLDER).withFeatureType(featureType);
		registry.cache(rarity, coords, generatedChestContext);
		// need to save on fail
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return false;
	}
	
	/**
	 * 
	 * @param world
	 * @param rarity
	 * @param featureType
	 * @param cache
	 * @param data
	 */
	public void cacheGeneratedChest(ServerLevelAccessor world, IRarity rarity, IFeatureType featureType, GeneratedCache<GeneratedChestContext> cache, GeneratorResult<ChestGeneratorData> data) {
		Treasure.LOGGER.debug("feature gen result -> {}", data);
		// GeneratedChestContext is used to cache data about the chest in the Dimension Generated Chest cache.
		GeneratedChestContext context = new GeneratedChestContext(
				data.getData().getRarity(), data.getData().getCoords())		
				.withMarkerCoords(data.getData().getSpawnCoords())
				.withFeatureType(featureType)
				.withName(data.getData().getRegistryName());

		Treasure.LOGGER.debug("chestGenContext -> {}", context);
		// cache the chest at its exact location
		cache.cache(rarity, context.getCoords(), context);
	}
	
	/**
	 * 
	 * @param dimension
	 * @param rarity
	 * @param featureType
	 */
	protected void updateChestGeneratorRegistry(ResourceLocation dimension, IRarity rarity, IFeatureType featureType) {
		// update the adjusted weight collection			
		RarityLevelWeightedChestGeneratorRegistry.adjustAllWeightsExcept(dimension, featureType, 1, rarity);
		Map<IFeatureType, RarityLevelWeightedCollection> map = RarityLevelWeightedChestGeneratorRegistry.RARITY_SELECTOR.get(dimension);
		RarityLevelWeightedCollection dumpCol = map.get(featureType);
		List<String> dump = dumpCol.dump();
		Treasure.LOGGER.debug("weighted collection dump -> {}", dump);		
	}
}
