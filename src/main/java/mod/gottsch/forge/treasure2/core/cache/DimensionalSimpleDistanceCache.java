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
package mod.gottsch.forge.treasure2.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

/**
 * Meant as a wrapper class for all caches that use a Map<dimension, simpleDistanceCache>
 * @author Mark Gottschling on May 19, 2023
 *
 */
public class DimensionalSimpleDistanceCache {
	private static final String DIMENSION_NAME = "dimension";
	private static final String WELL_CACHES_NAME = "wellCaches";
	
	public static final Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> WELL_CACHE = new HashMap<>();

	private DimensionalSimpleDistanceCache() {}
	
	public static void initialize() {
		// for each allowable dimension for the mod
		for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
			ResourceLocation dimension = ModUtil.asLocation(dimensionName);
			
			WELL_CACHE.put(dimension, new SimpleDistanceCache<>(Config.SERVER.wells.registrySize.get()));
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Tag save() {
		CompoundTag tag = new CompoundTag();
		
		// wells
		Tag wellTag = saveWellRegistry(WELL_CACHE);
		tag.put(WELL_CACHES_NAME, wellTag);
		
		return tag;
	}
	
	/**
	 * 
	 * @param tag
	 */
	public static void load(CompoundTag tag) {
		if (tag.contains(WELL_CACHES_NAME)) {
			loadWellRegistry(tag.getList(WELL_CACHES_NAME, Tag.TAG_COMPOUND), WELL_CACHE, GeneratedContext::new);
		}
	}
	
	/**
	 * 
	 * @param cacheMap
	 * @return
	 */
	public static Tag saveWellRegistry(Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> cacheMap) {
		ListTag dimensionalCachesTag = new ListTag();
		cacheMap.forEach((dimension, cache) -> {
			CompoundTag dimensionCacheTag = new CompoundTag();
			dimensionCacheTag.putString(DIMENSION_NAME, dimension.toString());
			ListTag dataTag = new ListTag();
			cache.getValues().forEach(datum -> {
				CompoundTag datumTag = datum.save();
				// add entry to list
				dataTag.add(datumTag);	
			});			
			dimensionCacheTag.put("data", dataTag);
			dimensionalCachesTag.add(dimensionCacheTag);
		});
		return dimensionalCachesTag;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param dimensinoalCachesTag
	 * @param cacheMap
	 * @param supplier
	 */
	public static <T> void loadWellRegistry(ListTag dimensinoalCachesTag, 
			Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> cacheMap, Supplier<GeneratedContext> supplier) {

		if (dimensinoalCachesTag != null) {
			Treasure.LOGGER.debug("loading well caches...");  	
			dimensinoalCachesTag.forEach(dimensionalCacheTag -> {
				CompoundTag dimensionalCacheCompound = (CompoundTag)dimensionalCacheTag;
				if (dimensionalCacheCompound.contains(DIMENSION_NAME)) {
					String dimensionName = dimensionalCacheCompound.getString(DIMENSION_NAME);
					Treasure.LOGGER.debug("loading dimension -> {}", dimensionName);
					// load the data
					if (dimensionalCacheCompound.contains("data")) {
						ResourceLocation dimension = ModUtil.asLocation(dimensionName);					
						// clear the registry first
						if (!WELL_CACHE.containsKey(dimension)) {
							WELL_CACHE.put(dimension, new SimpleDistanceCache<GeneratedContext>(Config.SERVER.wells.registrySize.get()));
						}
						WELL_CACHE.get(dimension).clear();
						
						ListTag dataTag = dimensionalCacheCompound.getList("data", Tag.TAG_COMPOUND);
						dataTag.forEach(datum -> {
							GeneratedContext context = supplier.get();
							// extract the data
							context.load((CompoundTag)datum);
							Treasure.LOGGER.debug("context -> {}", context);
							if (context.getRarity() != null && context.getCoords() != null) {									
								WELL_CACHE.get(dimension).cache(context.getCoords(), context);
							}
						});
					}
				}
			});
		}            		
	}
}
