/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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
package com.someguyssoftware.treasure2.registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestRegistry {
	private static final int MAX_SIZE = TreasureConfig.CHESTS.chestRegistrySize.get();
	
//	private static ChestRegistry instance = new ChestRegistry();
	// TODO this does not need to be a ListMultimap, can be just a LinkedList to preserve order
	private ListMultimap<String, ChestInfo> registry;
	
	/**
	 * 
	 */
	public ChestRegistry() {
		registry = LinkedListMultimap.create();
	}
	
	/**
	 * 
	 * @return
	 */
//	public static ChestRegistry getInstance() {
//		return instance;
//	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRegistered(final String key) {
		return registry.containsKey(key);
	}
	
	/**
	 * Registers a ChestInfo with a key.
	 * @param key
	 * @param info
	 */
	public synchronized void register(final String key, final ChestInfo info) {	
		Treasure.LOGGER.debug("Registering chest using key: " + key);
		// test the size
		if (registry.size() >= MAX_SIZE) {
			// remove the first element
			String headKey = registry.keySet().iterator().next();
			unregister(headKey);
		}
		// register by the unique key
		registry.put(key, info);
	}
	
	/**
	 * 
	 * @param key
	 */
	public synchronized void unregister(final String key) {
		if (registry.containsKey(key)) {
			registry.removeAll(key);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<ChestInfo> get(String key) {
		List<ChestInfo> info = null;
		if (registry.containsKey(key)) {
			info = registry.get(key);
		}
		return info;
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	public List<ChestInfo> getValues() {
		HashSet<ChestInfo> set = Sets.newHashSet(registry.values());
		return new ArrayList<>(set);
	}
	
	public void clear() {
		registry.clear();
	}
}