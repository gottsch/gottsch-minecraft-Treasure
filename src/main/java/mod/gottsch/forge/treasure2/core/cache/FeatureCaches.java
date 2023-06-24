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

import java.util.List;
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
public class FeatureCaches {
	private static final String DIMENSION_NAME = "dimension";
	private static final String WELL_CACHE_NAME = "wellCache";


	public static final DelayedFeatureSimpleDistanceCache WELL_CACHE = new DelayedFeatureSimpleDistanceCache();
	
	/**
	 * 
	 */
	private FeatureCaches() {}
	
	/**
	 * 
	 */
	public static void clear() {
		WELL_CACHE.clear();
	}
	
	/**
	 * 
	 */
	public static void initialize() {
		// for each allowable dimension for the mod
		for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
			ResourceLocation dimension = ModUtil.asLocation(dimensionName);

			WELL_CACHE.getDimensionDistanceCache().put(dimension, new SimpleDistanceCache<>(Config.SERVER.wells.cacheSize.get()));
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Tag save() {
		CompoundTag tag = new CompoundTag();
		
		// wells
		Tag wellTag = saveFeatureCache(WELL_CACHE);
		tag.put(WELL_CACHE_NAME, wellTag);
		Treasure.LOGGER.debug("save well cache dump -> {}", WELL_CACHE.dump());
		return tag;
	}
	
	/**
	 * 
	 * @param tag
	 */
	public static void load(CompoundTag tag) {
		if (tag.contains(WELL_CACHE_NAME)) {
			WELL_CACHE.clear();
			initialize();
			loadCache((CompoundTag)tag.get(WELL_CACHE_NAME), WELL_CACHE);
			Treasure.LOGGER.debug("load well cache dump -> {}", WELL_CACHE.dump());
		}
	}
	
	/**
	 * 
	 * @param delayedFeatureCache
	 * @return
	 */
	private static Tag saveFeatureCache(DelayedFeatureSimpleDistanceCache delayedFeatureCache) {
		
		CompoundTag featureTag = new CompoundTag();
		featureTag.putInt("delay", delayedFeatureCache.getDelayCount());
		
		featureTag.put("caches", saveCache(delayedFeatureCache.getDimensionDistanceCache()));
		
		return featureTag;
	}
	
	/**
	 * 
	 * @param featureTag
	 * @param delayedFeatureCache
	 * @return
	 */
	private static Tag loadCache(CompoundTag featureTag, DelayedFeatureSimpleDistanceCache delayedFeatureCache) {		
		int delay = 0;
		if (featureTag.contains("delay")) {
			delay = featureTag.getInt("delay");
		}
		delayedFeatureCache.setDelayCount(delay);
		
		if (featureTag.contains("caches")) {
			loadCache(featureTag.getList("caches", Tag.TAG_COMPOUND), delayedFeatureCache.getDimensionDistanceCache(), GeneratedContext::new);
		}		
		return featureTag;
	}
	
	/**
	 * 
	 * @param cacheMap
	 * @return
	 */
	public static ListTag saveCache(Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> cacheMap) {
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
	 * @param dimensionalCachesTag
	 * @param cacheMap
	 * @param supplier
	 */
	public static <T> void loadCache(ListTag dimensionalCachesTag, 
			Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> cacheMap, Supplier<GeneratedContext> supplier) {

		if (dimensionalCachesTag != null) {
			Treasure.LOGGER.debug("loading well caches...");  	
			dimensionalCachesTag.forEach(dimensionalCacheTag -> {
				CompoundTag dimensionalCacheCompound = (CompoundTag)dimensionalCacheTag;
				if (dimensionalCacheCompound.contains(DIMENSION_NAME)) {
					String dimensionName = dimensionalCacheCompound.getString(DIMENSION_NAME);
					Treasure.LOGGER.debug("loading dimension -> {}", dimensionName);
					// load the data
					if (dimensionalCacheCompound.contains("data")) {
						ResourceLocation dimension = ModUtil.asLocation(dimensionName);					
						// add the dimension if it doesn't exist
						if (!cacheMap.containsKey(dimension)) {
							cacheMap.put(dimension, new SimpleDistanceCache<GeneratedContext>(Config.SERVER.wells.cacheSize.get()));
						}
						
						ListTag dataTag = dimensionalCacheCompound.getList("data", Tag.TAG_COMPOUND);
						dataTag.forEach(datum -> {
							GeneratedContext context = supplier.get();
							// extract the data
							context.load((CompoundTag)datum);
							Treasure.LOGGER.debug("context -> {}", context);
							if (context.getRarity() != null && context.getCoords() != null) {									
								cacheMap.get(dimension).cache(context.getCoords(), context);
							}
						});
					}
				}
			});
		}            		
	}
}
