/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.block.TreasureChestBlock;

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
		TreasureChestBlock tb = (TreasureChestBlock)getBlock();
		
		// chest info
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_locks", TextFormatting.DARK_BLUE + String.valueOf(tb.getChestType().getMaxLocks())));
		// TODO add more info
	}
}
