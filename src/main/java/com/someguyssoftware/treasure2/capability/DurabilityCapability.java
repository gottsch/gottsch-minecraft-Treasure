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
package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.material.CharmableMaterial;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class DurabilityCapability implements IDurabilityCapability  {
	// the max value the durability can be set to
	private int maxDurability = MAX_DURABILITY; // this is kinda useless and confusing
	
	// the durability of the capability
	private int durability;
	
	// is this durability infinite (as opposed to the finite value of the durability property)
	private boolean infinite;
	
	// the max number of repairs
	private int maxRepairs;
	
	// the remaining repairs available
	private int repairs;
	
	/**
	 * 
	 */
	public DurabilityCapability() {
		setDurability(MAX_DURABILITY);
	}
	
	public DurabilityCapability(int durability) {
		setDurability(durability);
	}
	
	public DurabilityCapability(int durability, int max) {
		// NOTE order is important here
		setMaxDurability(max);
		setDurability(durability);
	}
	
	// if given a material, setup default maxRepairs
	// note: material is not saved as part of the durability
	public DurabilityCapability(int durability, int max, CharmableMaterial material) {
		this(durability, max);
		this.setRepairs(material.getMaxRepairs());
		this.setMaxRepairs(material.getMaxRepairs());
	}
	
	@Override
	public void copyTo(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.DURABILITY, null)) {
			IDurabilityCapability cap = stack.getCapability(TreasureCapabilities.DURABILITY, null);
			cap.setDurability(getDurability());
			cap.setInfinite(isInfinite());
			cap.setMaxDurability(getMaxDurability());
			cap.setRepairs(getRepairs());
			cap.setMaxRepairs(getMaxRepairs());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (!isInfinite()) {
			String text = TextFormatting.WHITE.toString() 
					+ I18n.translateToLocalFormatted("tooltip.durability.amount", getDurability() - stack.getItemDamage(), getDurability());
			if (getMaxRepairs() > 0) {
				text += " " + I18n.translateToLocalFormatted("tooltip.durability.repairs", getRepairs(), getMaxRepairs());
			}
			tooltip.add(text);
		}
		else {
			tooltip.add(TextFormatting.WHITE.toString() + I18n.translateToLocal("tooltip.durability.amount.infinite"));
		}
	}
	
	@Override
	public int getDurability() {
		return durability;
	}
	
	@Override
	public void setDurability(int maxDamage) {
		if (maxDamage > getMaxDurability()) {
            this.durability = getMaxDurability();
        }
        else {
            this.durability = maxDamage;
        }
	}

	@Override
	public int getMaxDurability() {
		return maxDurability;
	}

	@Override
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}

	@Override
	public boolean isInfinite() {
		return infinite;
	}

	@Override
	public void setInfinite(boolean infinite) {
		this.infinite = infinite;
	}

	@Override
	public int getMaxRepairs() {
		return maxRepairs;
	}

	@Override
	public void setMaxRepairs(int maxRepairs) {
		this.maxRepairs = maxRepairs;
	}

	@Override
	public int getRepairs() {
		return repairs;
	}

	@Override
	public void setRepairs(int repairs) {
		this.repairs = repairs;
	}

	@Override
	public String toString() {
		return "DurabilityCapability [maxDurability=" + maxDurability + ", durability=" + durability + ", infinite="
				+ infinite + ", maxRepairs=" + maxRepairs + ", repairs=" + repairs + "]";
	}
}
