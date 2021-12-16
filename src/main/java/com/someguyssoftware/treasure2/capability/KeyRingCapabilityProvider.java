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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.KEY_RING_CAPABILITY;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.KEY_RING_INVENTORY_CAPABILITY;

import com.someguyssoftware.treasure2.inventory.KeyRingInventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on May 11, 2020
 *
 */
public class KeyRingCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
	private static final String INVENTORY_TAG = "inventory";
	private static final String STATE_TAG = "state";
	
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	private final ItemStackHandler inventoryInstance = new ItemStackHandler(KeyRingInventory.INVENTORY_SIZE);
	private final KeyRingCapability instance = new KeyRingCapability();
	
	/**
	 * 
	 */
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag parentTag = new CompoundTag();
		CompoundTag inventoryTag = this.inventoryInstance.serializeNBT();
		parentTag.put(INVENTORY_TAG, inventoryTag);
		
		CompoundTag stateTag = (CompoundTag)KEY_RING_CAPABILITY.getStorage().writeNBT(KEY_RING_CAPABILITY, instance, null);
		parentTag.put(STATE_TAG, stateTag);
		return parentTag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(CompoundTag nbt) {		
		this.inventoryInstance.deserializeNBT((CompoundTag) nbt.get(INVENTORY_TAG));
		KEY_RING_CAPABILITY.getStorage().readNBT(KEY_RING_CAPABILITY, this.instance, null, nbt.getCompound(STATE_TAG));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability == KEY_RING_INVENTORY_CAPABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> this.inventoryInstance);
		}
		else if (capability == KEY_RING_CAPABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> this.instance);
		}
		return LazyOptional.empty();
	}
}
