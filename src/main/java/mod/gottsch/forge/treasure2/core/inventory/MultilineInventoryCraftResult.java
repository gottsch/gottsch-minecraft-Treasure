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
package mod.gottsch.forge.treasure2.core.inventory;

import net.minecraft.inventory.CraftResultInventory;

/**
 * A simple wrapper to add the "line" property.
 * @author Mark Gottschling on Mar 23, 2022
 *
 */
public class MultilineInventoryCraftResult extends CraftResultInventory {
	// the line index in a multiline output grid ie array of InventoryCraftResults, where index corresponds to the row
	private int line;
	
	public MultilineInventoryCraftResult(int line) {
		super();
		setLine(line);
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
}
