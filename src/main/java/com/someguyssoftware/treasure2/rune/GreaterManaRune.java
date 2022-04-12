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

import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Mar 29, 2022
 *
 */
public class GreaterManaRune extends ManaRune {

	protected GreaterManaRune(Builder builder) {
		super((Rune.Builder)builder);
	}

	@Override
	public void apply(ICharmEntity entity) {
		// add 50% to the current mana
		entity.setMana(entity.getMana() * 1.5D);
		entity.setMaxMana(entity.getMaxMana() * 1.5D);
	}

	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		// for each charm, decrease the mana
		cap.getCharmEntities().forEach((key, value) -> {
			if (entity.isAppliedTo(value.getCharm().getType())) {
				value.setMana(value.getMana() / 1.5);
				value.setMaxMana(value.getMaxMana() / 1.5);
				entity.getAppliedTo().remove(value.getCharm().getType());
			}
		});
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
			return new GreaterManaRune(this);
		}
	}
}
