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
package com.someguyssoftware.treasure2.rune;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 26, 2022
 *
 */
public class SocketsRune extends Rune {

	protected SocketsRune(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		return itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null) && itemStack.getCapability(TreasureCapabilities.CHARMABLE, null).isSocketable();
	}

	@Override
	public void apply(ItemStack itemStack, IRuneEntity entity) {
		if (!isValid(itemStack) || entity.isApplied()) {
			return;
		}

		if (itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			Treasure.LOGGER.debug("adding socket to item -> {}, size -> {}", itemStack.getDisplayName(), cap.getMaxSocketSize());
			cap.addMaxSocketSize(1);
			entity.setApplied(true);
		}
	}

	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		if (cap.getMaxSocketSize() > 0) {
			cap.addMaxSocketSize(-1);
			// remove any charms in sockets inventory at indexes > maxSocketSize
			for (int i = cap.getCharmEntities().get(InventoryType.SOCKET).size()-1; i > cap.getMaxSocketSize()-1; i--) {
				cap.remove(InventoryType.SOCKET, i);
			}
		}
		entity.setApplied(false);
	}

	/*
	 * 
	 */
	public static class Builder extends Rune.Builder {
		public Builder(ResourceLocation name) {
			super(name);
		}
		@Override
		public IRune build() {
			return new SocketsRune(this);
		}
	}
}
