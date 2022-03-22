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

import com.someguyssoftware.treasure2.inventory.PouchInventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	private static final String INVENTORY_TAG = "inventory";

	private final ItemStackHandler inventory_instance = new ItemStackHandler(PouchInventory.INVENTORY_SIZE);
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound parentTag = new NBTTagCompound();
		NBTTagCompound inventoryTag = this.inventory_instance.serializeNBT();
		parentTag.setTag(INVENTORY_TAG, inventoryTag);
		return parentTag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
		this.inventory_instance.deserializeNBT(nbt.getCompoundTag(INVENTORY_TAG));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.POUCH) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.POUCH) {
			return TreasureCapabilities.POUCH.cast(this.inventory_instance);
		}
		return null;
	}
}
