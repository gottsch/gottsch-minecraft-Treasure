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
import com.someguyssoftware.treasure2.charm.cost.QualityRuneCostReducerEvaluator;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 20, 2022
 *
 */
public class QualityRune extends Rune {
	public static final double MULTIPLIER = 1.25D;
	
	protected QualityRune(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		return itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null); // && check against Illumination and other specials
	}

	@Override
	public void apply(ItemStack itemStack, IRuneEntity entity) {
		if (!isValid(itemStack)) {
			return;
		}

		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		// for each charm, increase the amount
		cap.getCharmEntities().forEach((key, value) -> {
			if (!entity.isAppliedTo(value.getCharm().getType()) && !getInvalids().contains(value.getCharm().getType())) {
//				Treasure.logger.debug("before apply: entity -> {}, mana -> {}, max mana -> {}", value.getCharm().getName().toString(), value.getMana(), value.getMaxMana());
				//add 25% to the current amount
				value.setAmount(Math.floor(value.getAmount() * MULTIPLIER));
				// create a new cost evaluator that reduces the cost to the original input
				value.setCostEvaluator(new QualityRuneCostReducerEvaluator());
//				Treasure.logger.debug("after apply: entity -> {}, mana -> {}, max mana -> {}", value.getCharm().getName().toString(), value.getMana(), value.getMaxMana());
				entity.getAppliedTo().add(value.getCharm().getType());
				entity.setApplied(true);
			}
		});		
	}

	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		// for each charm, decrease the mana
		cap.getCharmEntities().forEach((key, value) -> {
			if (entity.isAppliedTo(value.getCharm().getType())) {
				value.setAmount(Math.ceil(value.getAmount() / MULTIPLIER));
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
			return new QualityRune(this);
		}
	}
}
