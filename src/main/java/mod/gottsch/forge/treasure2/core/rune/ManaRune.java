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
 * @author Mark Gottschling on Jan 19, 2022
 *
 */
public class ManaRune extends Rune {

	protected ManaRune(Builder builder) {
		super(builder);
	}

	/**
	 * Required so sub-classes can call super with a compatible Builder
	 * @param builder
	 */
	protected ManaRune(Rune.Builder builder) {
		super(builder);
	}
	
	@Override
	public boolean isValid(ItemStack itemStack) {		
		return itemStack.getCapability(TreasureCapabilities.CHARMABLE).isPresent();
		// TODO check against invalids
	}

	/**
	 * Applies the Rune's ability/modification to a Capability.
	 * Used only during Item.initCapability().
	 */
	public void initCapabilityApply(ICharmableCapability capability, IRuneEntity entity) {
		process(capability, entity);
	}
	
	@Override
	public void apply(ItemStack itemStack, IRuneEntity entity) {
		if (!isValid(itemStack)) {
			return;
		}
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(c -> c).orElse(null);
		process(cap, entity);
	}
	
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
		// add 25% to the current mana
		entity.setMana(entity.getMana() * 1.25D);
		entity.setMaxMana(entity.getMaxMana() * 1.25D);
	}

	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		itemStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			// for each charm, decrease the mana
			cap.getCharmEntities().forEach((key, value) -> {
				if (entity.isAppliedTo(value.getCharm().getType())) {
					value.setMana(value.getMana() / 1.25);
					value.setMaxMana(value.getMaxMana() / 1.25);
					entity.getAppliedTo().remove(value.getCharm().getType());
				}
			});
			entity.setApplied(false);
		});
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
			return new ManaRune(this);
		}
	}
}
