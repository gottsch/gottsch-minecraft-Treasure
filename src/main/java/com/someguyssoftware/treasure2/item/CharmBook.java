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
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.capability.ICharmableCapability;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Aug 27, 2021
 *
 */
public class CharmBook extends CharmItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public CharmBook(String modID, String name, Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP).stacksTo(1));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.label.charm_book").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
		// charmable info
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			cap.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}
}
