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
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.AegisCharm;
import com.someguyssoftware.treasure2.charm.GreaterHealingCharm;
import com.someguyssoftware.treasure2.charm.HealingCharm;
import com.someguyssoftware.treasure2.charm.ShieldingCharm;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2022
 *
 */
public class AngelsRune extends Rune {

	protected AngelsRune(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		// check the charms
		if (itemStack.hasCapability(TreasureCapabilities.DURABILITY, null) && itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			return true;
		}
		return false;
	}

	/**
	 * Applies the Rune's ability/modification to a Capability.
	 * Used only during Item.initCapability().
	 */
	public void initCapabilityApply(ICharmableCapability charmCap, IDurabilityCapability durabilityCap, IRuneEntity entity) {
		process(charmCap, durabilityCap, entity);
	}
	
	@Override
	public void apply(ItemStack itemStack, IRuneEntity runestoneEntity) {
		if (!isValid(itemStack)) {
			return;
		}
		
		IDurabilityCapability durabilityCap = itemStack.getCapability(TreasureCapabilities.DURABILITY, null);
		durabilityCap.setInfinite(true);
		
		ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
//		charmableCap.getCharmEntities().forEach((type, charmEntity) -> {
//			if (charmEntity.getCharm().getType().equals(HealingCharm.TYPE)
//					|| charmEntity.getCharm().getType().equals(GreaterHealingCharm.HEALING_TYPE)
//					|| charmEntity.getCharm().getType().equals(ShieldingCharm.SHIELDING_TYPE)
//					|| charmEntity.getCharm().getType().equals(AegisCharm.AEGIS_TYPE)) {
//				if (!runestoneEntity.isAppliedTo(charmEntity.getCharm().getType())) {
//					charmEntity.setMana(Math.floor(charmEntity.getMana() * 2.0D));
//					charmEntity.setMaxMana(Math.floor(charmEntity.getMaxMana() * 2.0D));
//					runestoneEntity.getAppliedTo().add(charmEntity.getCharm().getType());
//				}
//			}
//		});
//		runestoneEntity.setApplied(true);
		process(charmableCap, durabilityCap, runestoneEntity);
	}

	/**
	 * 
	 * @param charmableCap
	 * @param durabilityCap
	 * @param runestoneEntity
	 */
	protected void process(ICharmableCapability charmableCap, IDurabilityCapability durabilityCap, IRuneEntity runestoneEntity) {
		charmableCap.getCharmEntities().forEach((type, charmEntity) -> {
			if (charmEntity.getCharm().getType().equals(HealingCharm.TYPE)
					|| charmEntity.getCharm().getType().equals(GreaterHealingCharm.HEALING_TYPE)
					|| charmEntity.getCharm().getType().equals(ShieldingCharm.SHIELDING_TYPE)
					|| charmEntity.getCharm().getType().equals(AegisCharm.AEGIS_TYPE)) {
				if (!runestoneEntity.isAppliedTo(charmEntity.getCharm().getType())) {
					charmEntity.setMana(Math.floor(charmEntity.getMana() * 2.0D));
					charmEntity.setMaxMana(Math.floor(charmEntity.getMaxMana() * 2.0D));
					runestoneEntity.getAppliedTo().add(charmEntity.getCharm().getType());
				}
			}
		});
		runestoneEntity.setApplied(true);
	}
	
	@Override
	public void undo(ItemStack itemStack, IRuneEntity runestoneEntity) {
		if (itemStack.hasCapability(TreasureCapabilities.DURABILITY, null)) {
			IDurabilityCapability cap = itemStack.getCapability(TreasureCapabilities.DURABILITY, null);
			cap.setInfinite(false);
		}

		ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		charmableCap.getCharmEntities().forEach((type, charmEntity) -> {
			if (charmEntity.getCharm().getType().equals(HealingCharm.TYPE)
					|| charmEntity.getCharm().getType().equals(GreaterHealingCharm.HEALING_TYPE)
					|| charmEntity.getCharm().getType().equals(ShieldingCharm.SHIELDING_TYPE)
					|| charmEntity.getCharm().getType().equals(AegisCharm.AEGIS_TYPE)) {
				if (runestoneEntity.isAppliedTo(charmEntity.getCharm().getType())) {
					charmEntity.setMana(Math.floor(charmEntity.getMana() / 2.0D));
					charmEntity.setMaxMana(Math.floor(charmEntity.getMaxMana() / 2.0D));
					runestoneEntity.getAppliedTo().remove(charmEntity.getCharm().getType());
				}
			}
		});
		runestoneEntity.setApplied(false);
	}

	public static class Builder extends Rune.Builder {
		public Builder(ResourceLocation name	) {
			super(name);
		}
		@Override
		public IRune build() {
			return new AngelsRune(this);
		}
	}
}
