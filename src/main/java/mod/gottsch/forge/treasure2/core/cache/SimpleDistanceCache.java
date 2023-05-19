/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mod.gottsch.forge.gottschcore.bst.CoordsInterval;
import mod.gottsch.forge.gottschcore.bst.CoordsIntervalTree;
import mod.gottsch.forge.gottschcore.bst.IInterval;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;

/**
 * 
 * @author Mark Gottschling on May 19, 2023
 *
 * @param <T>
 */
public class SimpleDistanceCache<T> implements ISimpleDistanceCache<T> {

	private LinkedList<T> cache;
	private int maxSize;
	
	/*
	 * a Interval BST registry to determine the proximity of chests.
	 */
	private final CoordsIntervalTree<T> distanceCache;
	
	/**
	 * 
	 */
	public SimpleDistanceCache(int size) {
		cache = new LinkedList<>();
		maxSize = size;
		distanceCache = new CoordsIntervalTree<>();
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public boolean isCached(final T object) {
		return cache.contains(object);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void cache(final ICoords key, final T object) {	
		Treasure.LOGGER.debug("caching @ ->" + object.toString());
		if (isCached(object)) {
			return;
		}
		
		// test the size
		if (cache.size() >= maxSize) {
			// remove the first element in list (oldest).
			cache.pollFirst();
		}
		cache.add(object);		
		distanceCache.insert(new CoordsInterval<>(key.withY(0), key.add(1, -key.getY(), 1), object));
	}
	
	/**
	 * 
	 * @param object
	 */
	@Override
	public synchronized void uncache(ICoords key) {
		IInterval<T> interval = distanceCache.delete(new CoordsInterval<>(key, key, null));
		T data = interval.getData();
		
		if (isCached(data)) {
			cache.removeLast();
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public boolean withinArea(ICoords start, ICoords end) {
		List<IInterval<T>> overlaps = distanceCache.getOverlapping(distanceCache.getRoot(), new CoordsInterval<>(start, end));
		if (overlaps.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	@Override
	public List<T> getValues() {
		return new ArrayList<>(cache);
	}
	
	/**
	 * 
	 */
	@Override
	public void clear() {
		cache.clear();
		distanceCache.clear();
	}
	
	// TEMP
//	public void dump() {
//		for (T c : registry) {
//			Treasure.LOGGER.debug("Wells Registry entry -> {}", c.toString());
//		}
//	}
}