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

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.KEY_RING_CAPABILITY;
import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.KEY_RING_INVENTORY_CAPABILITY;

import mod.gottsch.forge.treasure2.core.inventory.KeyRingInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on May 11, 2020
 *
 */
public class KeyRingCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
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
	public CompoundNBT serializeNBT() {
		CompoundNBT parentTag = new CompoundNBT();
		CompoundNBT inventoryTag = this.inventoryInstance.serializeNBT();
		parentTag.put(INVENTORY_TAG, inventoryTag);
		
		CompoundNBT stateTag = (CompoundNBT)KEY_RING_CAPABILITY.getStorage().writeNBT(KEY_RING_CAPABILITY, instance, null);
		parentTag.put(STATE_TAG, stateTag);
		return parentTag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(CompoundNBT nbt) {		
		this.inventoryInstance.deserializeNBT((CompoundNBT) nbt.get(INVENTORY_TAG));
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
