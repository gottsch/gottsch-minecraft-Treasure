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
package mod.gottsch.forge.treasure2.core.capability;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/*
 * 
 */
public enum InventoryType {
	INNATE(0),
	IMBUE(1),
	SOCKET(2);

	private static final Map<Integer, InventoryType> values = new HashMap<Integer, InventoryType>();
	Integer value;

	// setup reverse lookup
	static {
		for (InventoryType x : EnumSet.allOf(InventoryType.class)) {
			values.put(x.getValue(), x);
		}
	}

	InventoryType(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static InventoryType getByValue(Integer value) {
		return (InventoryType) values.get(value);
	}
}
