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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestRegistry {
	private static final int MAX_SIZE = TreasureConfig.CHESTS.chestRegistrySize.get();
	
	private final LinkedList<ChestInfo> backingRegistry;
	private final Table<Rarity, String, ChestInfo> tableRegistry;
	
	/**
	 * 
	 */
	public ChestRegistry() {
		backingRegistry = new LinkedList<>();
		tableRegistry = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);// HashBasedTable.create();
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
	public synchronized void register(final Rarity rarity, final String key, final ChestInfo info) {
		// if bigger than max size of registry, remove the first (oldest) element
		if (backingRegistry.size() > MAX_SIZE) {
			unregisterFirst();
		}
		backingRegistry.add(info);
		tableRegistry.put(rarity, key, info);
	}
	
	/**
	 * 
	 */
	public synchronized void unregisterFirst() {
		ChestInfo removeChestInfo = backingRegistry.pollFirst();
		if (removeChestInfo != null) {
			if (tableRegistry.contains(removeChestInfo.getRarity(), removeChestInfo.getCoords().toShortString())) {
				tableRegistry.remove(removeChestInfo.getRarity(), removeChestInfo.getCoords().toShortString());
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 */
	public synchronized void unregister(final Rarity rarity, final String key) {
		if (tableRegistry.contains(rarity, key)) {
			ChestInfo chestInfo = tableRegistry.remove(rarity, key);
			if (chestInfo != null) {
				backingRegistry.remove(chestInfo);
			}
		}
	}
	
	/**
	 * 
	 * @param chestInfo
	 */
	public synchronized void unregister(ChestInfo chestInfo) {
		backingRegistry.remove(chestInfo);
		tableRegistry.remove(chestInfo.getRarity(), chestInfo.getCoords().toShortString());
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
	 * This will not update parent collection.
	 * @return
	 */
	public List<ChestInfo> getValues() {
		return backingRegistry;
	}
	
	public void clear() {
		tableRegistry.clear();
	}
	
	// TEMP
	public void dump() {
		for (ChestInfo c : backingRegistry) {
			Treasure.LOGGER.debug("Rarity -> {}, Key -> {}, Chest -> {}", c.getRarity(), c.getCoords(), c);
		}
	}
}