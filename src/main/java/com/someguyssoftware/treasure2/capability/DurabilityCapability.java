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

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class DurabilityCapability implements IDurabilityCapability, INBTSerializable<CompoundTag>  {
	private static final String DURABILITY_TAG = "durability";
	
	public static Capability<IDurabilityCapability> DURABILITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {	});	
	
	private int durability;
	
	/**
	 * 
	 */
	public DurabilityCapability() {
		
	}
	
	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(IDurabilityCapability.class);

//		CapabilityContainerListenerManager.registerListenerFactory(HiddenBlockRevealerContainerListener::new);
	}
	
	@Override
	public int getDurability() {
		return durability;
	}
	
	@Override
	public void setDurability(int durabilityIn) {
		if (durabilityIn > IDurabilityCapability.MAX_DURABILITY) {
            this.durability = IDurabilityCapability.MAX_DURABILITY;
        }
        else {
            this.durability = durabilityIn;
        }
	}

	/**
	 * write data
	 */
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		try {
		tag.putInt(DURABILITY_TAG, getDurability());
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		if (tag instanceof CompoundTag) {
			if (tag.contains(DURABILITY_TAG)) {
				setDurability(tag.getInt(DURABILITY_TAG));
			}
		}		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + durability;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DurabilityCapability other = (DurabilityCapability) obj;
		 return durability == other.durability;
	}

	@Override
	public String toString() {
		return "DurabilityCapability [durability=" + durability + "]";
	}
}