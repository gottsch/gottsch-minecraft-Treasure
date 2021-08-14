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

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Aug 14, 2021
 *
 */
public class CharmableCapabilityStorage implements Capability.IStorage<ICharmableCapability> {
	private static final String CHARMS = "charms";
	private static final String CHARM = "charm";
	private static final String DATA = "data";
	
	@Override
	public INBT writeNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		try {
		nbt.putInt(CHARM, 0);
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, Direction side,
			INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			if (tag.contains(CHARM)) {
//				instance.setCharmable(tag.getInt(CHARM));
			}
		}
	}

}
