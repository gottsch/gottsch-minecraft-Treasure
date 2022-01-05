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
package com.someguyssoftware.treasure2.capability;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Shared inventory limits for magical items (ie charms and artifacts/runes).
 * @author Mark Gottschling on Jan 2, 2022
 *
 */
public class MagicsInventoryCapability implements IMagicsInventoryCapability {
	
	/*
	 * Propeties that refer to the Inventory of the Item that has this capability
	 */
	
	// max inventory sizes
	private int maxSocketSize;
	private int maxImbueSize;
	private int maxInnateSize;
	
	// current inventory sizes
	private int innateSize;
	private int imbueSize;
	private int socketSize;
	
	/**
	 * 
	 */
	public MagicsInventoryCapability() {	}
	
	/**
	 * 
	 * @param maxInnateSize
	 * @param maxImbueSize
	 * @param maxSocketSize
	 */
	public MagicsInventoryCapability(int maxInnateSize, int maxImbueSize, int maxSocketSize) {
		this.setMaxInnateSize(maxInnateSize);
		this.setMaxImbueSize(maxImbueSize);
		this.setMaxSocketSize(maxSocketSize);
	}
	
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

	@Override
	public int getMaxSocketSize() {
		return maxSocketSize;
	}

	@Override
	public void setMaxSocketSize(int maxSocketsSize) {
		this.maxSocketSize = maxSocketsSize;
	}

	@Override
	public int getMaxImbueSize() {
		return maxImbueSize;
	}

	@Override
	public void setMaxImbueSize(int maxImbueSize) {
		this.maxImbueSize = maxImbueSize;
	}

	@Override
	public int getMaxInnateSize() {
		return maxInnateSize;
	}

	@Override
	public void setMaxInnateSize(int maxInnateSize) {
		this.maxInnateSize = maxInnateSize;
	}

	@Override
	public int getInnateSize() {
		return innateSize;
	}

	@Override
	public void setInnateSize(int innateSize) {
		this.innateSize = innateSize;
	}

	@Override
	public int getImbueSize() {
		return imbueSize;
	}

	@Override
	public void setImbueSize(int imbueSize) {
		this.imbueSize = imbueSize;
	}

	@Override
	public int getSocketSize() {
		return socketSize;
	}

	@Override
	public void setSocketSize(int socketSize) {
		this.socketSize = socketSize;
	}
}
