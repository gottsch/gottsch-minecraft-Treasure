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

import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureChestItemBlock extends ItemBlock {

	/**
	 * 
	 * @param block
	 */
	public TreasureChestItemBlock(Block block) {
		super(block);
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// get the block
		TreasureChestBlock tb = (TreasureChestBlock)getBlock();
		ITreasureChestTileEntity te = tb.getTileEntity();
	
		// chest info		
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + tb.getRarity().toString()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_locks", TextFormatting.DARK_BLUE + String.valueOf(tb.getChestType().getMaxLocks())));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.container_size", TextFormatting.DARK_GREEN + String.valueOf(te.getNumberOfSlots())));
	}	
}
