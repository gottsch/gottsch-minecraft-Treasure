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

/**
 * @author Mark Gottschling on May 11, 2020
 *
 */
public class KeyRingCapability implements IKeyRingCapability  {
	private boolean isOpen;
	
	/**
	 * 
	 */
	public KeyRingCapability() {
		
	}
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
