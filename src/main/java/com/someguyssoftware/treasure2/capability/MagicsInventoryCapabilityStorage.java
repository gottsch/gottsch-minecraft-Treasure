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

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Jan 3, 2022
 *
 */
@Deprecated
public class MagicsInventoryCapabilityStorage implements Capability.IStorage<IMagicsInventoryCapability> {
	private static final String MAX_INNATE_SIZE = "maxInnateSize";
	private static final String MAX_IMBUE_SIZE = "maxImbueSize";
	private static final String MAX_SOCKET_SIZE = "maxSocketSize";
	private static final String INNATE_SIZE = "innateSize";
	private static final String IMBUE_SIZE = "imbueSize";
	private static final String SOCKET_SIZE = "socketSize";
	
	@Override
	public NBTBase writeNBT(Capability<IMagicsInventoryCapability> capability, IMagicsInventoryCapability instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();

		try {
			nbt.setInteger(MAX_INNATE_SIZE, instance.getMaxInnateSize());
			nbt.setInteger(MAX_IMBUE_SIZE, instance.getMaxImbueSize());
			nbt.setInteger(MAX_SOCKET_SIZE, instance.getMaxSocketSize());
			
			nbt.setInteger(INNATE_SIZE, instance.getInnateSize());
			nbt.setInteger(IMBUE_SIZE, instance.getImbueSize());
			nbt.setInteger(SOCKET_SIZE, instance.getSocketSize());
		} catch (Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<IMagicsInventoryCapability> capability, IMagicsInventoryCapability instance, EnumFacing side, NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey(INNATE_SIZE)) {
				instance.setInnateSize(tag.getInteger(INNATE_SIZE));
			}
			if (tag.hasKey(IMBUE_SIZE)) {
				instance.setImbueSize(tag.getInteger(IMBUE_SIZE));
			}
			if (tag.hasKey(SOCKET_SIZE)) {
				instance.setSocketSize(tag.getInteger(SOCKET_SIZE));
			}
			
			if (tag.hasKey(MAX_INNATE_SIZE)) {
				instance.setMaxInnateSize(tag.getInteger(MAX_INNATE_SIZE));
			}
			if (tag.hasKey(MAX_IMBUE_SIZE)) {
				instance.setMaxImbueSize(tag.getInteger(MAX_IMBUE_SIZE));
			}
			if (tag.hasKey(MAX_SOCKET_SIZE)) {
				instance.setMaxSocketSize(tag.getInteger(MAX_SOCKET_SIZE));
			}
		}
	}
}
