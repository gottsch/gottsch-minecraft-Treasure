/*
 * This file is part of  Treasure2.
 * Copyright (c) 2017 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureChestBlockItem extends BlockItem {

	/**
	 * 
	 * @param block
	 */
	public TreasureChestBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// get the block
		AbstractTreasureChestBlock tb = (AbstractTreasureChestBlock) getBlock();

		// chest info		
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.rarity"), ChatFormatting.BLUE + tb.getRarity().toString()));
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.max_locks"), ChatFormatting.BLUE + String.valueOf(tb.getLockLayout().getMaxLocks())));
		int size = tb.getBlockEntityInstance() != null ? tb.getBlockEntityInstance().getInventorySize() : 0;
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.container_size"), ChatFormatting.DARK_GREEN + String.valueOf(size)));
	}	
}
