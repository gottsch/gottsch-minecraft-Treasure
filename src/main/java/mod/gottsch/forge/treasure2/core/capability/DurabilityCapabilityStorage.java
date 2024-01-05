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

import mod.gottsch.forge.treasure2.core.Treasure;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Mark Gottschling on Jul 31, 2021
 *
 */
public class DurabilityCapabilityStorage implements Capability.IStorage<IDurabilityCapability> {
	private static final String MAX_DURABILITY_TAG = "maxDurability";
	private static final String DURABILITY_TAG = "durability";
	private static final String INFINITE_TAG = "infinite";
	
	@Override
	public INBT writeNBT(Capability<IDurabilityCapability> capability, IDurabilityCapability instance, Direction side) {
		CompoundNBT mainTag = new CompoundNBT();
		try {
			mainTag.putInt(DURABILITY_TAG, instance.getDurability());
			mainTag.putInt(MAX_DURABILITY_TAG, instance.getMaxDurability());
			mainTag.putBoolean(INFINITE_TAG, instance.isInfinite());
			mainTag.putInt("repairs", instance.getRepairs());
			mainTag.putInt("maxRepairs", instance.getMaxRepairs());
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IDurabilityCapability> capability, IDurabilityCapability instance, Direction side,
			INBT nbt) {

		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			
			if (tag.contains(MAX_DURABILITY_TAG)) {
				instance.setMaxDurability(tag.getInt(MAX_DURABILITY_TAG));
			}
			
			if (tag.contains(DURABILITY_TAG)) {
				instance.setDurability(tag.getInt(DURABILITY_TAG));
			}
			
			if (tag.contains(INFINITE_TAG)) {
				instance.setInfinite(tag.getBoolean(INFINITE_TAG));
			}
			
			if (tag.contains("repairs")) {
				instance.setRepairs(tag.getInt("repairs"));
			}
			if (tag.contains("maxRepairs")) {
				instance.setMaxRepairs(tag.getInt("maxRepairs"));
			}
		}		
	}
}
