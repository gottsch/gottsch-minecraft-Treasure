/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item.weapon;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

/**
 * Extends AxeItem so it behaves like a vanilla axe elsewhere in the codebase. ex. breaks shields etc.
 * @author Mark Gottschling May 24, 2023
 *
 */
public class Axe extends AxeItem implements IWeapon {
	private float criticalChance;
	private float criticalDamage;
	
	/**
	 * 
	 * @param tier
	 * @param damage
	 * @param speed
	 * @param properties
	 */
	public Axe(Tier tier, float damageModifier, float speedModifier, Item.Properties properties) {
		this(tier, damageModifier, speedModifier, 0f, 0f, properties);
	}

	/**
	 * 
	 * @param tier
	 * @param damageModifier
	 * @param speedModifier
	 * @param criticalChance
	 * @param criticalDamage
	 * @param properties
	 */
	public Axe(Tier tier, float damageModifier, float speedModifier, float criticalChance, float criticalDamage, Item.Properties properties) {
		super(tier, damageModifier, speedModifier, properties);
		this.criticalChance = criticalChance;
		this.criticalDamage = criticalDamage;
	}
	
	@Override
	public Component getName(ItemStack itemStack) {
		if (isUnique()) {
			return Component.translatable(this.getDescriptionId(itemStack)).withStyle(ChatFormatting.YELLOW);
		} else {
			return Component.translatable(this.getDescriptionId(itemStack));
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		appendStats(stack, worldIn, tooltip, flag);
		appendHoverExtras(stack, worldIn, tooltip, flag);
	}

	public float getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(float criticalChance) {
		this.criticalChance = criticalChance;
	}

	public float getCriticalDamage() {
		return criticalDamage;
	}

	public void setCriticalDamage(float criticalDamage) {
		this.criticalDamage = criticalDamage;
	}
}
