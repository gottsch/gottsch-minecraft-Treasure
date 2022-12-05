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
import com.google.common.collect.Table;


import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class WeightedChestGeneratorRegistry {
	// this replaces the old ChestGeneratorType enum -> ChestGenerator nonsense that mojang likes to use.

	// chest weighted collection of generators by rarity and type
	// NOTE: most generators will only have 1 entry but some specials have multiple ex SCARCE
	public static final Table<IRarity, IGeneratorType, WeightedCollection<Number, IChestGenerator>> REGISTRY = HashBasedTable.create();
	
	/**
	 * 
	 */
	private WeightedChestGeneratorRegistry() {	}
	
	public static void registerGenerator(IRarity rarity, IGeneratorType type, IChestGenerator generator, Number weight) {
		if (!REGISTRY.contains(rarity, type)) {
			REGISTRY.put(rarity, type, new WeightedCollection<>());
		}
		REGISTRY.get(rarity, type).add(weight, generator);
	}
	
	public static void intialize() {
		
		// for each allowable dimension for the mod
		for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
			ResourceLocation dimension = ModUtil.asLocation(dimensionName);
			
			// find the ChestConfiguration that contains the same dimension
			ChestConfiguration chestConfig = Config.chestConfigMap.get(ModUtil.asLocation(dimensionName));
			if (chestConfig != null && chestConfig.getDimensions().contains(dimension.toString())) {
				// for each generator
				chestConfig.getGenerators().forEach(generator -> {
					// match the generator to the enum
					Optional<IGeneratorType> type = TreasureApi.getGeneratorType(generator.getKey().toUpperCase());
					if (type.isPresent()) {						
						// setup chest collection generator maps
						generator.getRarities().forEach(chestRarity -> {	
							// determine the rarity
							Optional<IRarity> rarity = TreasureApi.getRarity(chestRarity.getRarity());
							if (rarity.isPresent()) {
								WeightedCollection<Number, IChestGenerator> collection = null;
								// check if exists in table already
								
								// TODO this is ALL WRONG - this would be for the BY_RARITY map
//								if (REGISTRY.containsKey(dimension)) {
//									if (REGISTRY.get(dimension).contains(rarity.get(), type.get())) {
//										collection = REGISTRY.get(dimension).get(rarity.get(), type.get());
//									} else {
//										REGISTRY.get(dimension).put(rarity.get(), type.get(), new WeightedCollection<>());
//									}
//								} else {
//									collection = new WeightedCollection<>();
//									Table<IRarity, IGeneratorType, WeightedCollection<Number, IChestGenerator>> t = HashBasedTable.create();
//									t.put(rarity.get(), type.get(), collection);
//									REGISTRY.put(dimension, t);
//								}
//								
//								// TODO finally add to the weighted collection
//								collection.add(1, null); // ??? the chest generators objects need to be registered already
								// this portion of init is setting up weight collections of generators
							}
						});
//							addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.COMMON, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.COMMON));
//							CHEST_GENS.put(Rarity.COMMON, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
//							CHEST_GENS.get(Rarity.COMMON, WorldGenerators.SURFACE_CHEST).add(1, ChestGeneratorType.COMMON.getChestGenerator());
					}
				});
			}
		}

	}


}
