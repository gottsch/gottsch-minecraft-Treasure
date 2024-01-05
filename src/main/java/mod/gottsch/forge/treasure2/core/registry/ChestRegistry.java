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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.someguyssoftware.gottschcore.bst.CoordsInterval;
import com.someguyssoftware.gottschcore.bst.CoordsIntervalTree;
import com.someguyssoftware.gottschcore.bst.IInterval;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.chest.ChestInfo;
import mod.gottsch.forge.treasure2.core.enums.Rarity;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestRegistry {

	/*
	 * a Interval BST registry to determine the proximity of chests.
	 */
	private final CoordsIntervalTree<ChestInfo> distanceRegistry;
	/*
	 * a Linked List registry to maintain descending age of insertion of chests
	 */
	private final LinkedList<ChestInfo> ageRegistry;
	/*
	 * a Table registry for rarity/key lookups
	 */
	private final Table<Rarity, String, ChestInfo> tableRegistry;	
	
	private int registrySize;
	
	/**
	 * 
	 */
	public ChestRegistry() {
		distanceRegistry = new CoordsIntervalTree<>();
		ageRegistry = new LinkedList<>();
		tableRegistry = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);
	}
	
	/**
	 * 
	 * @param size
	 */
	public ChestRegistry(int size) {
		this();
		this.registrySize = size;
	}
	
	public boolean isRegistered(final Rarity rarity, final String key) {
		return tableRegistry.contains(rarity, key);
	}
	
	public boolean hasRarity(final Rarity rarity) {
		return tableRegistry.containsColumn(rarity);
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @param info
	 */
	public synchronized void register(final Rarity rarity, final ICoords key, final ChestInfo info) {
		// if bigger than max size of registry, remove the first (oldest) element
		if (ageRegistry.size() > getRegistrySize()) {
			unregisterFirst();
		}
		distanceRegistry.insert(new CoordsInterval<>(key.withY(0), key.add(1, -key.getY(), 1), info));
		ageRegistry.add(info);
		tableRegistry.put(rarity, key.toShortString(), info);
	}
	
	/**
	 * 
	 */
	public synchronized void unregisterFirst() {
		ChestInfo removeChestInfo = ageRegistry.pollFirst();
		if (removeChestInfo != null) {
			if (tableRegistry.contains(removeChestInfo.getRarity(), removeChestInfo.getCoords().toShortString())) {
				tableRegistry.remove(removeChestInfo.getRarity(), removeChestInfo.getCoords().toShortString());
			}
			distanceRegistry.delete(new CoordsInterval<>(removeChestInfo.getCoords(), removeChestInfo.getCoords(), null));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 */
	public synchronized void unregister(final Rarity rarity, final ICoords key) {		
		if (tableRegistry.contains(rarity, key.toShortString())) {
			ChestInfo chestInfo = tableRegistry.remove(rarity, key.toShortString());
			if (chestInfo != null) {
				ageRegistry.remove(chestInfo);
				distanceRegistry.delete(new CoordsInterval<>(key, key, null));
			}
		}
	}
	
	/**
	 * 
	 * @param chestInfo
	 */
	public synchronized void unregister(ChestInfo chestInfo) {
		ageRegistry.remove(chestInfo);
		tableRegistry.remove(chestInfo.getRarity(), chestInfo.getCoords().toShortString());
		distanceRegistry.delete(new CoordsInterval<>(chestInfo.getCoords(), chestInfo.getCoords(), null));
	}
	
	/**
	 * 
	 * @param rarity
	 * @param key
	 * @return
	 */
	public Optional<ChestInfo> get(Rarity rarity, String key) {
		if (tableRegistry.contains(rarity, key)) {
			return Optional.of(tableRegistry.get(rarity, key));
		}
		return Optional.empty();
	}
	
	// Optional
	public Optional<List<ChestInfo>> getByRarity(Rarity rarity) {
		if (tableRegistry.containsRow(rarity)) {
			Treasure.LOGGER.debug("table registry contains rarity -> {}", rarity);
			Map<String, ChestInfo> infoMap = tableRegistry.row(rarity);			
			Treasure.LOGGER.debug("chest infos size -> {}", infoMap.size());
			return Optional.of(infoMap.values().stream().collect(Collectors.toList()));
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean withinArea(ICoords start, ICoords end) {
		List<IInterval<ChestInfo>> overlaps = distanceRegistry.getOverlapping(distanceRegistry.getRoot(), new CoordsInterval<>(start, end));
		if (overlaps.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	public List<ChestInfo> getValues() {
		return ageRegistry;
	}
	
	public void clear() {
		ageRegistry.clear();
		tableRegistry.clear();
		distanceRegistry.clear();
	}

	public int getRegistrySize() {
		return registrySize;
	}
	
//	// TEMP
//	public void dump() {
//		for (ChestInfo c : backingRegistry) {
//			Treasure.LOGGER.debug("Rarity -> {}, Key -> {}, Chest -> {}", c.getRarity(), c.getCoords(), c);
//		}
//	}
}