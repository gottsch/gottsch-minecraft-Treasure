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
package mod.gottsch.forge.treasure2.core.registry.support;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;

/*
 * 
 */
public class GeneratedContext {
	private ICoords coords;
	private IRarity rarity;
	
	public GeneratedContext() {}
	public GeneratedContext(IRarity rarity, ICoords coords) {
		this.rarity = rarity;
		this.coords = coords;
	}
	
	public ICoords getCoords() {
		return coords;
	}
	public void setCoords(ICoords coords) {
		this.coords = coords;
	}
	public IRarity getRarity() {
		return rarity;
	}
	public void setRarity(IRarity rarity) {
		this.rarity = rarity;
	}
}
