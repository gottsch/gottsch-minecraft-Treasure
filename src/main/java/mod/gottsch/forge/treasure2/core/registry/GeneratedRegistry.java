/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.gottschcore.bst.CoordsInterval;
import mod.gottsch.forge.gottschcore.bst.CoordsIntervalTree;
import mod.gottsch.forge.gottschcore.bst.IInterval;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;

/**
 * Renamed from ChestRegistry in 1.12.2/1.16.5
 * This Registry is a non-Singleton.
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
// TODO rename to GeneratedCache
public class GeneratedRegistry<T extends GeneratedContext> {
	
	/*
	 * a Interval BST registry to determine the proximity of chests.
	 */
	private final CoordsIntervalTree<T> distanceRegistry;
	/*
	 * a Linked List registry to maintain descending age of insertion of chests
	 */
	private final LinkedList<T> ageRegistry;
	/*
	 * a Table registry for rarity/key lookups
	 */
	private final Table<IRarity, String, T> tableRegistry;	
	
	private int registrySize;
	
	/**
	 * 
	 */
	public GeneratedRegistry() {
		distanceRegistry = new CoordsIntervalTree<>();
		ageRegistry = new LinkedList<>();
		tableRegistry = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);
	}
	
	/**
	 * 
	 * @param size
	 */
	public GeneratedRegistry(int size) {
		this();
		this.registrySize = size;
	}
	
	public boolean isRegistered(final IRarity rarity, final String key) {
		return tableRegistry.contains(rarity, key);
	}
	
	public boolean hasIRarity(final IRarity rarity) {
		return tableRegistry.containsColumn(rarity);
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @param info
	 */
	public synchronized void register(final IRarity rarity, final ICoords key, final T info) {
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
		T removeGenContext = ageRegistry.pollFirst();
		if (removeGenContext != null) {
			if (tableRegistry.contains(removeGenContext.getRarity(), removeGenContext.getCoords().toShortString())) {
				tableRegistry.remove(removeGenContext.getRarity(), removeGenContext.getCoords().toShortString());
			}
			distanceRegistry.delete(new CoordsInterval<>(removeGenContext.getCoords(), removeGenContext.getCoords(), null));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 */
	public synchronized void unregister(final IRarity rarity, final ICoords key) {		
		if (tableRegistry.contains(rarity, key.toShortString())) {
			T genContext = tableRegistry.remove(rarity, key.toShortString());
			if (genContext != null) {
				ageRegistry.remove(genContext);
				distanceRegistry.delete(new CoordsInterval<>(key, key, null));
			}
		}
	}
	
	/**
	 * 
	 * @param genContext
	 */
	public synchronized void unregister(T genContext) {
		ageRegistry.remove(genContext);
		tableRegistry.remove(genContext.getRarity(), genContext.getCoords().toShortString());
		distanceRegistry.delete(new CoordsInterval<>(genContext.getCoords(), genContext.getCoords(), null));
	}
	
	/**
	 * 
	 * @param rarity
	 * @param key
	 * @return
	 */
	public Optional<T> get(IRarity rarity, String key) {
		if (tableRegistry.contains(rarity, key)) {
			return Optional.of(tableRegistry.get(rarity, key));
		}
		return Optional.empty();
	}
	
	// Optional
	public Optional<List<T>> getByIRarity(IRarity rarity) {
		if (tableRegistry.containsRow(rarity)) {
			Treasure.LOGGER.debug("table registry contains rarity -> {}", rarity);
			Map<String, T> infoMap = tableRegistry.row(rarity);			
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
		List<IInterval<T>> overlaps = distanceRegistry.getOverlapping(distanceRegistry.getRoot(), new CoordsInterval<>(start, end));
		if (overlaps.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	public List<T> getValues() {
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
}
