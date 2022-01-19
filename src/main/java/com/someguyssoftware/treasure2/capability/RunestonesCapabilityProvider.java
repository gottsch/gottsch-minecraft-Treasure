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
 * @author Mark Gottschling on Jan 15, 2022
 *
 */
public class RunestonesCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {
	private final IMagicsInventoryCapability magicsCap;
	private final IRunestonesCapability runestonesCap;

	/**
	 * 
	 */
	public RunestonesCapabilityProvider() {
		this.magicsCap = new MagicsInventoryCapability(1, 0, 0);
//		this.runstonesCap = new RunestonesCapability(magicsCap);
		this.runestonesCap = new RunestonesCapability(0, 0, 0);
	}

	/**
	 * 
	 * @param capability
	 */
	public RunestonesCapabilityProvider(IRunestonesCapability capability) {
//		this.magicsCap = capability.getMagicsCap();
		this.magicsCap = new MagicsInventoryCapability(1, 0, 0);
		runestonesCap = capability;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.RUNESTONES) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.RUNESTONES) {
			return TreasureCapabilities.RUNESTONES.cast(this.runestonesCap);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = (NBTTagCompound)TreasureCapabilities.RUNESTONES.getStorage().writeNBT(TreasureCapabilities.RUNESTONES, runestonesCap, null);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		TreasureCapabilities.RUNESTONES.getStorage().readNBT(TreasureCapabilities.RUNESTONES, runestonesCap, null, nbt);
	}

}
