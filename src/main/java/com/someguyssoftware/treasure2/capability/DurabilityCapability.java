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

import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class DurabilityCapability implements IDurabilityCapability  {
	// the max value the durability can be set to
	private int maxDurability = MAX_DURABILITY;
	
	// the durability of the capability
	private int durability;
	
	// is this durability infinite (as opposed to the finite value of the durability property)
	private boolean infinite;
	/**
	 * 
	 */
	public DurabilityCapability() {
		setDurability(MAX_DURABILITY);
	}
	
	public DurabilityCapability(int durability) {
		setDurability(durability);
	}
	
	public DurabilityCapability(int durability, int max) {
		// NOTE order is important here
		setMaxDurability(max);
		setDurability(durability);
	}
	
	// TODO rename copyTo()
	@Override
	public void transferTo(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.DURABILITY, null)) {
			IDurabilityCapability cap = stack.getCapability(TreasureCapabilities.DURABILITY, null);
			cap.setDurability(getDurability());
			cap.setInfinite(isInfinite());
			cap.setMaxDurability(getMaxDurability());
		}
	}
	
	@Override
	public int getDurability() {
		return durability;
	}
	
	@Override
	public void setDurability(int maxDamage) {
		if (maxDamage > getMaxDurability()) {
            this.durability = getMaxDurability();
        }
        else {
            this.durability = maxDamage;
        }
	}

	@Override
	public int getMaxDurability() {
		return maxDurability;
	}

	@Override
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}

	@Override
	public boolean isInfinite() {
		return infinite;
	}

	@Override
	public void setInfinite(boolean infinite) {
		this.infinite = infinite;
	}
}
