/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.CHARMABLE;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CharmableCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
	private final ICharmableCapability charmableCap;

	
	/**
	 * 
	 */
	public CharmableCapabilityProvider() {
		this.charmableCap = new CharmableCapability(0, 0, 0);
	}
	
	/**
	 * 
	 * @param capability
	 */
	public CharmableCapabilityProvider(ICharmableCapability capability) {
		charmableCap = capability;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == CHARMABLE) {
			return  (LazyOptional<T>) LazyOptional.of(() -> charmableCap);
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = (CompoundNBT)CHARMABLE.getStorage().writeNBT(CHARMABLE, charmableCap, null);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		CHARMABLE.getStorage().readNBT(CHARMABLE, charmableCap, null, nbt);
	}
}
