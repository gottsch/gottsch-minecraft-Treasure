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

/**
 * 
 * @author Mark Gottschling on Jan 17, 2022
 *
 */
@Deprecated
public interface IMagicsInventorySupportCapability {
	
	IMagicsInventoryCapability getMagicsCap();
	
	void clear();
	
	int getCurrentSize(InventoryType type);

	void setCurrentSize(InventoryType type, int size);

	void addCurrentSize(InventoryType type, int amount);

	/**
	 * @param type
	 * @return
	 */
	int getMaxSize(InventoryType type);

}
