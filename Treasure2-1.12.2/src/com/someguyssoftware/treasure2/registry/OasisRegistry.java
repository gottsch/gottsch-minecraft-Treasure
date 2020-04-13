/**
 * 
 */
package com.someguyssoftware.treasure2.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.generator.oasis.OasisInfo;

/**
 * @author Mark Gottschling on Apr 8, 2020
 *
 */
public class OasisRegistry {
	
	// TODO wrong!! each dimension gets its own registry!
	private static OasisRegistry instance = new OasisRegistry();
	// TODO this does not need to be a ListMultimap, can be just a LinkedList to preserve order
	private Map<Integer, ListMultimap<String, OasisInfo>> registry;
	
	/**
	 * 
	 */
	private OasisRegistry() {
		registry = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public static OasisRegistry getInstance() {
		return instance;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRegistered(final Integer dimensionID, final String key) {
		if (registry.containsKey(dimensionID)) {
			ListMultimap<String, OasisInfo> map = registry.get(dimensionID);
			return registry.containsKey(key);
		}
		return false;		
	}

	/**
	 * Registers a OasisInfo with a key.
	 * @param key
	 * @param info
	 */
	public synchronized void register(final Integer dimensionID, final String key, final OasisInfo info) {
		Treasure.logger.debug("Registering oasis in dimension -> {} using key -> {} ", dimensionID, key);
		// get the registry for the dimension
		if (!registry.containsKey(dimensionID)) {
			registry.put(dimensionID, LinkedListMultimap.create());
		}
		
		ListMultimap<String, OasisInfo> map = registry.get(dimensionID);
		// test the size		
		if (map.size() >= TreasureConfig.OASES.oasisRegistrySize) {
			// remove the first element
			String headKey = map.keySet().iterator().next();
			unregister(dimensionID, headKey);
		}
		
		// register by the unique key
		map.put(key, info);
	}
	
	/**
	 * 
	 * @param key
	 */
	public synchronized void unregister(final Integer dimensionID, final String key) {
		if (registry.containsKey(dimensionID)) {
			ListMultimap<String, OasisInfo> map = registry.get(dimensionID);
			if (map.containsKey(key)) {
				map.removeAll(key);
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<OasisInfo> get(final Integer dimensionID, final String key) {
		List<OasisInfo> info = null;
		if (registry.containsKey(dimensionID)) {
			ListMultimap<String, OasisInfo> map = registry.get(dimensionID);
			if (map.containsKey(key)) {
				info = map.get(key);
			}
		}
		return info;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Integer> getDimensionKeys() {
		return registry.keySet();
	}
	
	/**
	 * This will not update parent collection.
	 * @param dimensionID
	 * @return
	 */
	public ListMultimap<String, OasisInfo> getDimensionEntry(final Integer dimensionID) {
		return LinkedListMultimap.create(registry.get(dimensionID));
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	public List<OasisInfo> getValues(final Integer dimensionID) {
		if (registry.containsKey(dimensionID)) {
			ListMultimap<String, OasisInfo> map = registry.get(dimensionID);
			HashSet<OasisInfo> set = Sets.newHashSet(map.values());
			return new ArrayList<>(set);
		}
		return new ArrayList<>();
	}
	
	/**
	 * 
	 */
	public void clear() {
		registry.clear();
	}
}
