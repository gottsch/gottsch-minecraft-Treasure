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

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Do nothing storage
 * @author Mark Gottschling
 *
 */
public class PouchableCapabilityStorage implements Capability.IStorage<IPouchableCapability> {
    
	@Override
	public NBTBase writeNBT(Capability<IPouchableCapability> capability, IPouchableCapability instance, EnumFacing side) {
		return new NBTTagCompound();
	}

	@Override
	public void readNBT(Capability<IPouchableCapability> capability, IPouchableCapability instance, EnumFacing side, NBTBase nbt) {
		}
}
