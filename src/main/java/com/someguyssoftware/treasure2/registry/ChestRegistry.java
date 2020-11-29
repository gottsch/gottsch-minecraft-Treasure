/**
 * 
 */
package com.someguyssoftware.treasure2.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

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
	private static final int MAX_SIZE = TreasureConfig.CHESTS.chestRegistrySize;
	
	private static ChestRegistry instance = new ChestRegistry();
	// TODO this does not need to be a ListMultimap, can be just a LinkedList to preserve order
	private ListMultimap<String, ChestInfo> registry;
	
	/**
	 * 
	 */
	private ChestRegistry() {
		registry = LinkedListMultimap.create();
	}
	
	/**
	 * 
	 * @return
	 */
	public static ChestRegistry getInstance() {
		return instance;
	}
	
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
		Treasure.logger.debug("Registering chest using key: " + key);
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
