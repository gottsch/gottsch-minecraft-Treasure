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

import static com.someguyssoftware.treasure2.capability.DurabilityCapability.DURABILITY_CAPABILITY;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * TODO see https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.18.x/src/main/java/choonster/testmod3/capability/SimpleCapabilityProvider.java
 * for generic provider
 * @author Mark Gottschling on Aug 2, 2021
 *
 */
public class DurabilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<Tag> {

    // capabilities for item
	private final IDurabilityCapability instance = new DurabilityCapability();

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, Direction facing) {
		return getCapability().orEmpty(capability, LazyOptional.of(() -> instance));
	}

	/**
	 * Get the {@link Capability} instance to provide the handler for.
	 *
	 * @return The Capability instance
	 */
	public final Capability<IDurabilityCapability> getCapability() {
		return DURABILITY_CAPABILITY;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Tag serializeNBT() {
		return  ((INBTSerializable<Tag>)instance).serializeNBT();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeNBT(Tag tag) {
		((INBTSerializable<Tag>)instance).deserializeNBT(tag);
	}
}