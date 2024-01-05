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
package mod.gottsch.forge.treasure2.core.rune;

import mod.gottsch.forge.treasure2.core.capability.ICharmableCapability;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.charm.ICharmEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jun 13, 2022
 *
 */
public class DoubleChargeRune extends Rune {

	protected DoubleChargeRune(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		return itemStack.getCapability(TreasureCapabilities.CHARMABLE).isPresent();
	}

	public void initCapabilityApply(ICharmableCapability capability, IRuneEntity entity) {	
		process(capability, entity);
	}
	
	@Override
	public void apply(ItemStack itemStack, IRuneEntity entity) {		
		if (!isValid(itemStack) || entity.isApplied()) {
			return;
		}
		
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(c -> c).orElse(null);
		
		process(cap, entity);
	}
	
	/**
	 * 
	 * @param cap
	 * @param entity
	 */
	protected void process(ICharmableCapability cap, IRuneEntity entity) {
		if (cap != null) {
			cap.getCharmEntities().forEach((key, value) -> {
				if (!entity.isAppliedTo(value.getCharm().getType()) && !getInvalids().contains(value.getCharm().getType())) {
					apply(value);
					entity.getAppliedTo().add(value.getCharm().getType());
					entity.setApplied(true);
				}
			});
		}
	}
	
	protected void apply(ICharmEntity entity) {
		// doubles the current and max recharges
		entity.setRecharges(entity.getRecharges() * 2);
		entity.setMaxRecharges(entity.getMaxRecharges() * 2);
	}
	
	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(c -> c).orElse(null);
		// for each charm, decrease the mana
		if (cap != null) {
			cap.getCharmEntities().forEach((key, value) -> {
				if (entity.isAppliedTo(value.getCharm().getType())) {
					value.setRecharges(value.getRecharges() / 2);
					value.setMaxRecharges(value.getMaxRecharges() / 2);
					entity.getAppliedTo().remove(value.getCharm().getType());
				}
			});
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
			return new DoubleChargeRune(this);
		}
	}
}
