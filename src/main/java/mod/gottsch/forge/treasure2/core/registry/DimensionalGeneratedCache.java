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

import java.util.*;
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

/**
 * This is a singleton Registry that contains the instance registries
 * for the generated chests in all the dimension/region combinations.
 * It also contains the mappings/registries for the special Wither Tree
 * Chests and Wells.
 * @author Mark Gottschling on Nov 30, 2022
 *
 */
public class DimensionalGeneratedCache {	
	private static final String DIMENSION_NAME = "dimension";
	private static final String CHEST_CACHE_NAME = "chestCache";

	public static final Map<ResourceLocation, Map<IFeatureType, GeneratedCache<? extends GeneratedContext>>> CHEST_CACHE = new HashMap<>();

	/**
	 * 
	 */
	private DimensionalGeneratedCache() {}

	/**
	 * 
	 */
	public static void clear() {
		CHEST_CACHE.clear();
	}

	/**
	 * Initialize from Config file.
	 */
	public static void initialize() {
		// find the ChestConfiguration that contains the same dimension
		ChestFeaturesConfiguration chestConfig = Config.chestConfig;//Config.chestConfigMap.get(ModUtil.asLocation(dimensionName));
		//			for (ChestConfiguration chestConfig : Config.chestConfigs) {
		if (chestConfig != null) {

			// for each allowable dimension for the mod
			for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
				Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
				ResourceLocation dimension = ModUtil.asLocation(dimensionName);

				/*
				 * setup chest registry
				 */

				//				if (Config.SERVER.integration.dimensionsWhiteList.get().contains(dimension.toString())) {
				// create a new map for generatorType->generatedChestRegistry
				Map<IFeatureType, GeneratedCache<? extends GeneratedContext>> chestRegistryMap = new HashMap<>();
				// add generator map to byDimension map
				CHEST_CACHE.put(dimension, chestRegistryMap);

				// for each generator
				chestConfig.getGenerators().forEach(generator -> {
					// match the generator to the enum
					Optional<IFeatureType> type = TreasureApi.getFeatureType(generator.getKey().toUpperCase());
					if (type.isPresent()) {
						// create a new generated chest registry with size set from config
						chestRegistryMap.put(type.get(), new GeneratedCache<>(generator.getRegistrySize()));
					}
				});
				//				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public static Tag save() {
		CompoundTag tag = new CompoundTag();

		Tag chestTag = saveRegistry(CHEST_CACHE);
		tag.put(CHEST_CACHE_NAME, chestTag);		

		return tag;
	}

	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static Tag saveRegistry(Map<ResourceLocation, Map<IFeatureType, GeneratedCache<? extends GeneratedContext>>> registry) {
		ListTag dimensionalRegistriesTag = new ListTag();
		registry.forEach((dimension, map) -> {
			CompoundTag dimensionRegistryTag = new CompoundTag();
			dimensionRegistryTag.putString(DIMENSION_NAME, dimension.toString());
			ListTag generatedRegistriesTag = new ListTag();
			map.forEach((generatorType, genRegistry) -> {
				CompoundTag generatedRegistryTag = new CompoundTag();
				generatedRegistryTag.putString("name", generatorType.getName());
				ListTag dataTag = new ListTag();
				genRegistry.getValues().forEach(datum -> {
					//					Treasure.LOGGER.debug("saving generated context datum -> {}", datum);
					CompoundTag datumTag = datum.save();
					// add entry to list
					dataTag.add(datumTag);	
				});
				generatedRegistryTag.put("data", dataTag);
				generatedRegistriesTag.add(generatedRegistryTag);
			});
			dimensionRegistryTag.put("registries", generatedRegistriesTag);
			dimensionalRegistriesTag.add(dimensionRegistryTag);
		});
		return dimensionalRegistriesTag;
	}

	/**
	 * 
	 * @param tag
	 */
	public static void load(CompoundTag tag) {
		if (tag.contains(CHEST_CACHE_NAME)) {
			loadRegistry(tag.getList(CHEST_CACHE_NAME, Tag.TAG_COMPOUND), CHEST_CACHE, GeneratedChestContext::new);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param dimensinoalRegistriesTag the tag to load from
	 * @param registry the registry to load into
	 * @param supplier a factor to create data objects
	 */
	@SuppressWarnings("unchecked")
	public static <T> void loadRegistry(ListTag dimensinoalRegistriesTag, 
			Map<ResourceLocation, Map<IFeatureType, GeneratedCache<?>>> registry, Supplier<? extends GeneratedContext> supplier) {

		if (dimensinoalRegistriesTag != null) {
			Treasure.LOGGER.debug("loading chest registries...");  	
			dimensinoalRegistriesTag.forEach(dimensionalRegistryTag -> {
				CompoundTag dimensionalRegistryCompound = (CompoundTag)dimensionalRegistryTag;
				if (dimensionalRegistryCompound.contains(DIMENSION_NAME)) {
					String dimensionName = dimensionalRegistryCompound.getString(DIMENSION_NAME);
					Treasure.LOGGER.debug("loading dimension -> {}", dimensionName);
					if (dimensionalRegistryCompound.contains("registries")) {
						ListTag registriesTag = dimensionalRegistryCompound.getList("registries", Tag.TAG_COMPOUND);
						registriesTag.forEach(registryTag -> {
							CompoundTag registryCompound = (CompoundTag)registryTag;
							if (registryCompound.contains("name")) {
								// extract the name
								String name = registryCompound.getString("name");
								Optional<IFeatureType> generatorType = TreasureApi.getFeatureType(name);
								if (!generatorType.isPresent()) {
									return;
								}
								if (registryCompound.contains("data")) {
									ResourceLocation dimension = ModUtil.asLocation(dimensionName);										
									// clear the registry
									GeneratedCache<GeneratedContext> generatedRegistry = (GeneratedCache<GeneratedContext>) getGeneratedCache(registry, dimension, generatorType.get());
									generatedRegistry.clear();

									// get the data list
									ListTag dataTag = registryCompound.getList("data", Tag.TAG_COMPOUND);
									dataTag.forEach(datum -> {
										GeneratedContext context = supplier.get();
										// extract the data
										context.load((CompoundTag)datum);
										//										Treasure.LOGGER.debug("loading datum context -> {}", context);
										if (context.getRarity() != null && context.getCoords() != null) {										
											if (generatedRegistry != null) {
												generatedRegistry.cache(context.getRarity(), context.getCoords(), context);
											}
										}										
									});
								}
							}
						});
					}            		
				}   
			});
		}
	}

	/**
	 * 
	 * @param dimension
	 * @param genType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GeneratedCache<GeneratedChestContext> getChestGeneratedCache(ResourceLocation dimension, IFeatureType genType) {
		return (GeneratedCache<GeneratedChestContext>) getGeneratedCache(CHEST_CACHE, dimension, genType);
	}

	@SuppressWarnings("unchecked")
	public static List<GeneratedCache<GeneratedChestContext>> getChestGeneratedCaches(ResourceLocation dimension) {
		Map<IFeatureType, GeneratedCache<? extends GeneratedContext>> registryMap = CHEST_CACHE.get(dimension);
		List<GeneratedCache<GeneratedChestContext>> caches = new ArrayList<GeneratedCache<GeneratedChestContext>>();
		if (registryMap != null) {
			registryMap.values().forEach(c -> {
				caches.add((GeneratedCache<GeneratedChestContext>) c);
			});
		}
		return caches;
	}

	/**
	 * 
	 * @param registry
	 * @param dimension
	 * @param genType
	 * @return
	 */
	public static GeneratedCache<? extends GeneratedContext> getGeneratedCache(Map<ResourceLocation, Map<IFeatureType, GeneratedCache<?>>> registry, ResourceLocation dimension, IFeatureType genType) {
		Map<IFeatureType, GeneratedCache<? extends GeneratedContext>> registryMap = registry.get(dimension);
		GeneratedCache<? extends GeneratedContext> genRegistry = null;
		if (registryMap != null) {
			genRegistry =  (GeneratedCache<? extends GeneratedContext>) registryMap.get(genType);
		}
		return genRegistry;
	}


}
