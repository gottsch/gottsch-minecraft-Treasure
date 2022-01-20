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
package com.someguyssoftware.treasure2.runestone;

import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2022
 *
 */
public class ManaRunestone extends Runestone {

	protected ManaRunestone(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		return itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null);
	}

	// TODO may need an apply() method that performs on a single charm
	// so that when new charms are added or recharged, the rune can be applied
	// without reapplying to all the other charms that may already have been applied.
	public void apply(ICharmEntity entity) {
		// double the current mana
		entity.setMana(entity.getMana() * 1.25);
		entity.setMaxMana(entity.getMaxMana());
	}

	@Override
	public void apply(ItemStack itemStack, IRunestoneEntity entity) {
		if (!isValid(itemStack)) {
			return;
		}

		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		// for each charm, increase the mana
		cap.getCharmEntities().forEach((key, value) -> {
			if (!entity.isAppliedTo(value.getCharm().getType())) {
				Treasure.logger.debug("before apply: entity -> {}, mana -> {}, max mana -> {}", value.getCharm().getName().toString(), value.getMana(), value.getMaxMana());
				//add 25% to the current mana
				value.setMana(Math.floor(value.getMana() * 1.25D));
				value.setMaxMana(Math.floor(value.getMaxMana() * 1.25D));
				Treasure.logger.debug("after apply: entity -> {}, mana -> {}, max mana -> {}", value.getCharm().getName().toString(), value.getMana(), value.getMaxMana());
				entity.getAppliedTo().add(value.getCharm().getType());
				entity.setApplied(true);
			}
		});		
	}

	@Override
	public void undo(ItemStack itemStack, IRunestoneEntity entity) {
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		// for each charm, decrease the mana
		cap.getCharmEntities().forEach((key, value) -> {
			if (entity.isAppliedTo(value.getCharm().getType())) {
				value.setMana(value.getMana() / 1.25);
				value.setMaxMana(value.getMaxMana() / 1.25);
				entity.getAppliedTo().remove(value.getCharm().getType());
			}
		});
		entity.setApplied(false);
	}

	/*
	 * 
	 */
	public static class Builder extends Runestone.Builder {
		public Builder(ResourceLocation name) {
			super(name);
		}
		@Override
		public IRunestone build() {
			return new ManaRunestone(this);
		}
	}
}
