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

import com.google.common.collect.*;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGeneratorType;
import mod.gottsch.forge.treasure2.core.random.LevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * This registry is manually populated ie. not via config file.
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class WeightedChestGeneratorRegistry {
	// this replaces the old ChestGeneratorType enum -> ChestGenerator nonsense that mojang likes to use.

	// chest weighted collection of generators by rarity and type
	// NOTE: most generators will only have 1 entry but some specials have multiple ex SCARCE
	public static final Table<IRarity, IGeneratorType, WeightedCollection<Number, IChestGenerator>> REGISTRY = HashBasedTable.create();
	public static final Map<ResourceLocation, Map<IGeneratorType, LevelWeightedCollection<IRarity>>> RARITY_SELECTOR = new HashMap<>();

	// NEW way
	public static final Multimap<Block, IChestGenerator> NON_STANDARD_REVERSE = ArrayListMultimap.create();
	
	/**
	 * 
	 */
	private WeightedChestGeneratorRegistry() {	}
	
	@Deprecated
	public static void registerGenerator(IRarity rarity, IGeneratorType type, IChestGenerator generator, Number weight) {
		if (!REGISTRY.contains(rarity, type)) {
			REGISTRY.put(rarity, type, new WeightedCollection<>());
		}
		REGISTRY.get(rarity, type).add(weight, generator);
	}
	
	public static void registerGenerator(IRarity rarity, IGeneratorType type, IChestGeneratorType chestGenType, Number weight) {
		// first check if chestGenType is registered
		Optional<IChestGenerator> generator = ChestGeneratorRegistry.get(chestGenType);
		if (!generator.isPresent()) {
			return;
		}
		
		if (!REGISTRY.contains(rarity, type)) {
			REGISTRY.put(rarity, type, new WeightedCollection<>());
		}
		REGISTRY.get(rarity, type).add(weight, generator.get());
	}
	
	/**
	 * load/register chest generators from config
	 */
	public static void intialize() {
		Map<IGeneratorType, LevelWeightedCollection<IRarity>> map = null;
		LevelWeightedCollection<IRarity> collection = null;
		
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
					// match the generator to the enum
					Optional<IGeneratorType> type = TreasureApi.getGeneratorType(generator.getKey().toUpperCase());
					if (type.isPresent()) {
						// get the weighted collection from the map
						if (map.containsKey(type.get())) {
							collection = map.get(type.get());
						} else {
							collection = new LevelWeightedCollection<>();
							map.put(type.get(), collection);
						}
						
						// setup chest collection generator maps
						for (ChestRarity chestRarity : generator.getRarities()) {
							// determine the rarity
							Optional<IRarity> rarity = TreasureApi.getRarity(chestRarity.getRarity());
							if (rarity.isPresent()) {
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
	 * Saves the RARITY_SELECTOR as it has state stored in the LevelWeightedCollection
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
			dimensionTag.put("generators", generators);
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
				if (RARITY_SELECTOR.containsKey(dimension) && dimensionCompound.contains("generators")) {
					ListTag generatorsTag = dimensionCompound.getList("generators", Tag.TAG_COMPOUND);
					generatorsTag.forEach(generatorTag -> {
						CompoundTag generatorCompound = (CompoundTag)generatorTag;
						if (generatorCompound.contains("type")) {
							Optional<IGeneratorType> type = TreasureApi.getGeneratorType(generatorCompound.getString("type"));
							if (type.isPresent()) {
								if (generatorCompound.contains("collection")) {
									LevelWeightedCollection<IRarity> collection = new LevelWeightedCollection<>();
									collection.load((CompoundTag)generatorCompound.get("collection"));
									// add or replace
									RARITY_SELECTOR.get(dimension).put(type.get(), collection);						
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
	public static IRarity getNextRarity(ResourceLocation location, IGeneratorType type) {
		if (RARITY_SELECTOR.containsKey(location)) {
			Map<IGeneratorType, LevelWeightedCollection<IRarity>> map = RARITY_SELECTOR.get(location);
			if (map.containsKey(type)) {
				return map.get(type).next();
			}
		}
		return Rarity.NONE;
	}
	
	public static IChestGenerator getNextGenerator(IRarity rarity, IGeneratorType type) {
		if (REGISTRY.contains(rarity, type)) {
			WeightedCollection<Number, IChestGenerator> collection = REGISTRY.get(rarity, type);
			return collection.next();
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
	public static void adjustAllWeightsExcept(ResourceLocation location, IGeneratorType type, int weight, IRarity rarity) {
		if (RARITY_SELECTOR.containsKey(location)) {
			Map<IGeneratorType, LevelWeightedCollection<IRarity>> map = RARITY_SELECTOR.get(location);
			if (map.containsKey(type)) {
				map.put(type, map.get(type).add(weight, rarity));
			}
		}
	}
	
	
}
