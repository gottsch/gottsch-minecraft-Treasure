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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY_CAPABILITY;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Mark Gottschling on Aug 2, 2021
 *
 */
public class DurabilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    // capabilities for item
	private final IDurabilityCapability instance = new DurabilityCapability();

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == DURABILITY_CAPABILITY) {
			return  (LazyOptional<T>) LazyOptional.of(() -> instance);
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = (CompoundTag)DURABILITY_CAPABILITY.getStorage().writeNBT(DURABILITY_CAPABILITY, instance, null);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		DURABILITY_CAPABILITY.getStorage().readNBT(DURABILITY_CAPABILITY, instance, null, nbt);
	}
}