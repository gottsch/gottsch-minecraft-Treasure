/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
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
		super(properties.stacksTo(1));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);	
		tooltip.add(Component.translatable(LangUtil.tooltip("treasure_tool")));
	}	

	/**
	 * Required to prevent item consumption in recipe
	 */
	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	/**
	 * Required to prevent item consumption in recipe
	 */
	@Override
	public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        return hasCraftingRemainingItem();
    }
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
		return itemStack.copy();
	}

}
