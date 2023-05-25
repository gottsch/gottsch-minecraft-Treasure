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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Extends AxeItem so it behaves like a vanilla axe elsewhere in the codebase. ex. breaks shields etc.
 * @author Mark Gottschling May 24, 2023
 *
 */
public class Axe extends AxeItem implements IWeapon {
	
	/**
	 * 
	 * @param tier
	 * @param damage
	 * @param speed
	 * @param properties
	 */
	public Axe(Tier tier, float damage, float speed, Properties properties) {
		super(tier, damage, speed, properties);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		if (isUnique()) {
			return new TranslatableComponent(this.getDescriptionId(itemStack)).withStyle(ChatFormatting.YELLOW);
		} else {
			return new TranslatableComponent(this.getDescriptionId(itemStack));
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		appendHoverExtras(stack, worldIn, tooltip, flag);
	}
}
