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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * 
 * @author Mark Gottschling on Oct 26, 2021
 *
 */
@Deprecated
public class CharmInventoryCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

	private final ICharmInventoryCapability instance;

	/**
	 * 
	 */
	public CharmInventoryCapabilityProvider() {
		instance = new CharmInventoryCapability();
	}

	/**
	 * 
	 * @param capability
	 */
	public CharmInventoryCapabilityProvider(ICharmInventoryCapability capability) {
		instance = capability;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.CHARM_INVENTORY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.CHARM_INVENTORY) {
			return TreasureCapabilities.CHARM_INVENTORY.cast(this.instance);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = (NBTTagCompound)TreasureCapabilities.CHARM_INVENTORY.getStorage().writeNBT(TreasureCapabilities.CHARM_INVENTORY, instance, null);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		TreasureCapabilities.CHARM_INVENTORY.getStorage().readNBT(TreasureCapabilities.CHARM_INVENTORY, instance, null, nbt);
	}

}
