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
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Sets;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;

/**
 * 
 * @author Mark Gottschling on Jul 19, 2021
 * @param <T>
 *
 */
public class SimpleListRegistry<T> implements ISimpleListReigstry<T> {

	private LinkedList<T> registry;
	private int maxSize;
	
	/**
	 * 
	 */
	public SimpleListRegistry(int size) {
		registry = new LinkedList<>();
		maxSize = size;
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public boolean isRegistered(final T object) {
		return registry.contains(object);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void register(T object) {	
		Treasure.LOGGER.debug("Registering @ ->" + object.toString());
		if (isRegistered(object)) {
			return;
		}
		
		// test the size
		if (registry.size() >= maxSize) {
			// remove the first element in list (oldest). this is a stack not a queue so first or top element is the newest.
			registry.pollFirst();
		}
		registry.add(object);
	}
	
	/**
	 * 
	 * @param object
	 */
	@Override
	public synchronized void unregister(T object) {
		if (isRegistered(object)) {
			registry.removeLast();
		}
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	@Override
	public List<T> getValues() {
		return new ArrayList<>(registry);
	}
	
	/**
	 * 
	 */
	@Override
	public void clear() {
		registry.clear();
	}
}