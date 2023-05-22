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

import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on May 19, 2023
 *
 */
public class DelayedFeatureSimpleDistanceCache {
	
	private Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> dimensionDistanceCache = new HashMap<>();
	
	private int delayCount = 0;
	
	/**
	 * 
	 */
	public DelayedFeatureSimpleDistanceCache() {}

	public int getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(int delayCount) {
		this.delayCount = delayCount;
	}

	public Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> getDimensionDistanceCache() {
		return dimensionDistanceCache;
	}

	public void setDimensionDistanceCache(
			Map<ResourceLocation, SimpleDistanceCache<GeneratedContext>> dimensionDistanceCache) {
		this.dimensionDistanceCache = dimensionDistanceCache;
	}

	public void clear() {
		dimensionDistanceCache.clear();
	}
}
