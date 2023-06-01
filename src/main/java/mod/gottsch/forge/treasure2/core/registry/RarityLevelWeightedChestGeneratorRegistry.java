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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGeneratorType;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

/**
 * This registry is manually populated ie. not via config file.
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class RarityLevelWeightedChestGeneratorRegistry {

	private static final String GENERATORS_TAG = "generators";
	
	// collection of chest generators by rarity and type
	public static final Table<IRarity, IFeatureType, IChestGenerator> REGISTRY = HashBasedTable.create();
	public static final Map<ResourceLocation, Map<IFeatureType, RarityLevelWeightedCollection>> RARITY_SELECTOR = new HashMap<>();

	
	/**
	 * 
	 */
	private RarityLevelWeightedChestGeneratorRegistry() {	}

	
	public static void registerGenerator(IRarity rarity, IFeatureType type, IChestGeneratorType chestGenType) {
		// first check if chestGenType is registered
		Optional<IChestGenerator> generator = ChestGeneratorRegistry.get(chestGenType);
		if (!generator.isPresent()) {
			return;
		}

		REGISTRY.put(rarity, type, generator.get());
	}
	
	public static void clear() {
		RARITY_SELECTOR.clear();
	}
	
	/**
	 * load/register chest generators from config
	 */
	public static void initialize() {
		// clear just the rarity selector. the registry is initialized during setup.
		RARITY_SELECTOR.clear();
		Map<IFeatureType, RarityLevelWeightedCollection> map = null;
		RarityLevelWeightedCollection collection = null;
		
		// for each allowable dimension for the mod
		for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
			ResourceLocation dimension = ModUtil.asLocation(dimensionName);
			
			// find the ChestConfiguration that contains the same dimension
			ChestConfiguration chestConfig = Config.chestConfigMap.get(ModUtil.asLocation(dimensionName));
			if (chestConfig != null && chestConfig.getDimensions().contains(dimension.toString())) {
				// get the collection map by dimension
				if (RARITY_SELECTOR.containsKey(dimension)) {
					map = RARITY_SELECTOR.get(dimension);
				} else {
					map = Maps.newHashMap();
					RARITY_SELECTOR.put(dimension, map);
				}
				
				// for each generator
				for (Generator generator : chestConfig.getGenerators()) {
					Treasure.LOGGER.debug("processing generator -> {}", generator.getClass().getSimpleName());
					// match the generator to the enum
					Optional<IFeatureType> type = TreasureApi.getFeatureType(generator.getKey().toUpperCase());
					if (type.isPresent()) {
						Treasure.LOGGER.debug("processing feature type -> {}", type.get());
						// get the weighted collection from the map
						if (map.containsKey(type.get())) {
							collection = map.get(type.get());
						} else {
							collection = new RarityLevelWeightedCollection();
							map.put(type.get(), collection);
						}
						
						// setup chest collection generator maps
						for (ChestRarity chestRarity : generator.getRarities()) {
							Treasure.LOGGER.debug("processing generator rarity -> {}", chestRarity.getRarity());
							// determine the rarity
							Optional<IRarity> rarity = TreasureApi.getRarity(chestRarity.getRarity());
							if (rarity.isPresent()) {
								Treasure.LOGGER.debug("rarity is present -> {}", rarity.get());
								// add the weight to the collection
								collection.add(chestRarity.getWeight(), rarity.get());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Saves the RARITY_SELECTOR as it has state stored in the LevelWeightedCollection.
	 * @return
	 */
	public static ListTag save() {
	
		ListTag dimensionsTag = new ListTag();
		RARITY_SELECTOR.forEach((key, value) -> {
			CompoundTag dimensionTag = new CompoundTag();
			dimensionTag.putString("dimension", key.toString());
			
			ListTag generators = new ListTag();
			value.forEach((generatorType, levelWeightedCollection) -> {
				CompoundTag generatorTag = new CompoundTag();
				generatorTag.putString("type", generatorType.getValue());
				generatorTag.put("collection", levelWeightedCollection.save());
				generators.add(generatorTag);
			});
			dimensionTag.put(GENERATORS_TAG, generators);
			dimensionsTag.add(dimensionTag);
		});
		return dimensionsTag;
	}
	
	/**
	 * 
	 * @param dimensionsTag
	 */
	public static void load(ListTag dimensionsTag) {
		dimensionsTag.forEach(dimensionTag -> {
			CompoundTag dimensionCompound = (CompoundTag)dimensionTag;
			if (dimensionCompound.contains("dimension")) {
				ResourceLocation dimension = ModUtil.asLocation(dimensionCompound.getString("dimension"));
				if (RARITY_SELECTOR.containsKey(dimension) && dimensionCompound.contains(GENERATORS_TAG)) {
					ListTag generatorsTag = dimensionCompound.getList(GENERATORS_TAG, Tag.TAG_COMPOUND);
					generatorsTag.forEach(generatorTag -> {
						CompoundTag generatorCompound = (CompoundTag)generatorTag;
						if (generatorCompound.contains("type")) {
							Optional<IFeatureType> type = TreasureApi.getFeatureType(generatorCompound.getString("type"));
							if (type.isPresent()) {
								if (generatorCompound.contains("collection")) {
									// TODO ensure dimension exists in rarity selector
									RarityLevelWeightedCollection baseCollection = RARITY_SELECTOR.get(dimension).get(type.get());
									baseCollection.load((CompoundTag)generatorCompound.get("collection"));
									// add or replace
									RARITY_SELECTOR.get(dimension).put(type.get(), baseCollection);						
								}
							}
						}
					});
				}
			}
		});
	}
	
	/**
	 * 
	 * @param location
	 * @param type
	 * @return
	 */
	public static IRarity getNextRarity(ResourceLocation location, IFeatureType type) {
		if (RARITY_SELECTOR.containsKey(location)) {
			Map<IFeatureType, RarityLevelWeightedCollection> map = RARITY_SELECTOR.get(location);
			if (map.containsKey(type)) {
				return map.get(type).next();
			}
		}
		return Rarity.NONE;
	}
	
	/**
	 * 
	 * @param rarity
	 * @param type
	 * @return
	 */
	public static IChestGenerator getNextGenerator(IRarity rarity, IFeatureType type) {
		if (REGISTRY.contains(rarity, type)) {
			IChestGenerator generator = REGISTRY.get(rarity, type);
			return generator;
		}
		return null;
	}
	
	/**
	 * 
	 * @param location
	 * @param type
	 * @param weight
	 * @param rarity
	 */
	public static void adjustAllWeightsExcept(ResourceLocation location, IFeatureType type, int weight, IRarity rarity) {
		if (RARITY_SELECTOR.containsKey(location)) {
			Map<IFeatureType, RarityLevelWeightedCollection> map = RARITY_SELECTOR.get(location);
			if (map.containsKey(type)) {
				map.put(type, new RarityLevelWeightedCollection(map.get(type).adjustExcept(weight, rarity)));
			}
		}
	}	
}
