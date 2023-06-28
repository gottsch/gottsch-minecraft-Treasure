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

import java.text.DecimalFormat;
import java.util.List;

import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling May 24, 2023
 *
 */
public interface IWeapon {
	static final DecimalFormat df = new DecimalFormat("###.##");
	
	/**
	 * Determines whether the weapon is "unique" or named. ex. The Black Sword, The
	 * Sword of Omens.
	 * 
	 * @return
	 */
	default public boolean isUnique() {
		return false;
	}
	

	/**
	 * 
	 * @param stack
	 * @param worldIn
	 * @param tooltip
	 * @param flag
	 */
	default public void appendStats(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		if (getCriticalChance() > 0f) {
			tooltip.add(Component.literal(LangUtil.NEWLINE));
			tooltip.add(Component.translatable(LangUtil.tooltip("weapons.power_attack_chance"), df.format(getCriticalChance()))
					.withStyle(ChatFormatting.AQUA));
			tooltip.add(Component.translatable(LangUtil.tooltip("weapons.power_attack_damage"), df.format(getCriticalDamage()))
					.withStyle(ChatFormatting.AQUA));
			tooltip.add(Component.literal(LangUtil.NEWLINE));
		}
	}
	
	/**
	 * 
	 * @param stack
	 * @param level
	 * @param tooltip
	 * @param flag
	 */
	default public  void appendHoverExtras(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		// TODO add tooltip info for critical percentage and damage
	}

	float getCriticalChance();

	void setCriticalChance(float criticalChance);

	float getCriticalDamage();

	void setCriticalDamage(float criticalDamage);
}
