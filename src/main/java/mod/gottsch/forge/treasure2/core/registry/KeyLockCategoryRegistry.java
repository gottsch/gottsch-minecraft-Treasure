/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import java.util.Map;

import com.google.common.collect.Maps;

import mod.gottsch.forge.treasure2.core.item.IKeyLockCategory;

/**
 * 
 * @author Mark Gottschling on Nov 12, 2022
 *
 */
// why is this deprecated? you might want a new category is a addon mod
@Deprecated
public class KeyLockCategoryRegistry {
	private static final Map<String, IKeyLockCategory> REGISTRY = Maps.newHashMap();
	
	public static void register(IKeyLockCategory category) {
		if (!REGISTRY.containsKey(category.toString())) {
			REGISTRY.put(category.toString(), category);
		}
	}
	
	public static void unregister(IKeyLockCategory category) {
		if (REGISTRY.containsKey(category.toString())) {
			REGISTRY.remove(category.toString());
		}
	}
	
	public int size() {
		return REGISTRY.size();
	}
}
