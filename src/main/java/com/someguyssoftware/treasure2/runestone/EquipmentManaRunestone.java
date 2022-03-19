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

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.cost.EquipmentCostEvaluator;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 21, 2022
 *
 */

// TODO this class simply replaces the cost evaluator on charms.
// could be a more generic runestone class that takes a ICostEvaluator in the constructor.
// that would mean Rune would need to be more like Charm with a createEntity(IRunestoneEntity) method.
public class EquipmentManaRunestone extends Runestone {

	protected EquipmentManaRunestone(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		// has charmable
		if (itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			return true;
		}
		return false;
	}

	@Override
	public void apply(ItemStack itemStack, IRunestoneEntity runestoneEntity) {
		if (!isValid(itemStack)) {
			return;
		}
		ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		charmableCap.getCharmEntities().forEach((type, charmEntity) -> {
			charmEntity.setCostEvaluator(new EquipmentCostEvaluator(charmEntity.getCostEvaluator()));
			Treasure.logger.debug("setting entity -> {} to use cost eval -> {} with child eval -> {}", charmEntity.getCharm().getName().toString(), charmEntity.getCostEvaluator().getClass().getSimpleName(),
					((EquipmentCostEvaluator)charmEntity.getCostEvaluator()).getEvaluator().getClass().getSimpleName());
		});
		runestoneEntity.setApplied(true);
	}

	@Override
	public void undo(ItemStack itemStack, IRunestoneEntity runestoneEntity) {

		ICharmableCapability charmableCap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		charmableCap.getCharmEntities().forEach((type, charmEntity) -> {
			if (charmEntity.getCostEvaluator() instanceof EquipmentCostEvaluator) {
				charmEntity.setCostEvaluator(((EquipmentCostEvaluator)charmEntity.getCostEvaluator()).getEvaluator());
			}
		});
		runestoneEntity.setApplied(false);
	}

	// TODO extend to take a ICostEvaluator as a required param
	public static class Builder extends Runestone.Builder {
		public Builder(ResourceLocation name) {
			super(name);
		}
		@Override
		public IRunestone build() {
			return new EquipmentManaRunestone(this);
		}
	}
}
