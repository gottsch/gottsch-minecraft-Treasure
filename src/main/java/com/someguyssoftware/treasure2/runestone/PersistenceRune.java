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

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 23, 2022
 *
 */
public class PersistenceRune extends Runestone {

	protected PersistenceRune(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isValid(ItemStack itemStack) {		
		return 	(EnchantmentHelper.hasVanishingCurse(itemStack));
	}

	@Override
	public void apply(ItemStack itemStack, IRunestoneEntity entity) {
		Treasure.logger.debug("applying persistence rune, isvalid -> {}, isapplied -> {}", isValid(itemStack), entity.isApplied());
		if (!isValid(itemStack) || entity.isApplied()) {
			return;
		}
		Treasure.logger.debug("do we get here?");
        NBTTagList enchantmentTagList = itemStack.getEnchantmentTagList();
        int indexOfVanishing = -1;
        for (int i = 0; i < enchantmentTagList.tagCount(); ++i) {
            NBTTagCompound nbt = enchantmentTagList.getCompoundTagAt(i);
            Enchantment enchantment = Enchantment.getEnchantmentByID(nbt.getShort("id"));
            if (enchantment == Enchantments.VANISHING_CURSE) {
                indexOfVanishing = i;
                break;
            }
        }
        if (indexOfVanishing > -1) {
        	enchantmentTagList.removeTag(indexOfVanishing);
        }
        entity.setApplied(true);
	}

	@Override
	public void undo(ItemStack itemStack, IRunestoneEntity entity) {
		itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
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
			return new PersistenceRune(this);
		}
	}
}
