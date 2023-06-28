/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author Mark Gottschling on Jul 29, 2018
 *
 */
public class TreasureToolItem extends Item {

	public TreasureToolItem(Item.Properties properties) {
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);	
		tooltip.add(Component.translatable(LangUtil.tooltip("treasure_tool")));
	}	

	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        if (!hasCraftingRemainingItem()) {
            return false;
        }
        return true;
	}
}
