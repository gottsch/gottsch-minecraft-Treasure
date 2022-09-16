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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.POUCH;

import com.someguyssoftware.treasure2.inventory.PouchInventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    // capabilities for item
	private final ItemStackHandler instance = new ItemStackHandler(PouchInventory.INVENTORY_SIZE);

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability ==POUCH) {
			return  (LazyOptional<T>) LazyOptional.of(() -> instance);
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = this.instance.serializeNBT();
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
//		POUCH_CAPABILITY.getStorage().readNBT(POUCH_CAPABILITY, instance, null, nbt);
		this.instance.deserializeNBT(nbt);
	}
}