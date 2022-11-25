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
package mod.gottsch.forge.treasure2.core.lock;

/**
 * NOTE should this contain all sort of data of a chest or is this class simply about locks? If about just locks it should change it's name.
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class LockLayout {
	private int maxLocks;
	private LockSlot[] slots;
	
	/**
	 * 
	 */
	public LockLayout(int maxLocks) {
		setMaxLocks(maxLocks);
		setSlots(new LockSlot[maxLocks]);
	}

	/**
	 * @return the maxLocks
	 */
	public int getMaxLocks() {
		return maxLocks;
	}

	/**
	 * @param maxLocks the maxLocks to set
	 */
	private LockLayout setMaxLocks(int maxLocks) {
		this.maxLocks = maxLocks;
		return this;
	}

	/**
	 * @return the slots
	 */
	public LockSlot[] getSlots() {
		return slots;
	}

	/**
	 * 
	 * @param lockSlots
	 */
	public LockLayout addSlots(LockSlot...lockSlots ) {
		for (int i = 0; i < lockSlots.length; i++) {
			if (i < this.getMaxLocks()) {
				this.getSlots()[i] = lockSlots[i];
			}
		}
		return this;
	}
	
	/**
	 * @param slots the slots to set
	 */
	private LockLayout setSlots(LockSlot[] slots) {
		this.slots = slots;
		return this;
	}
}
