/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.block.MimicChestBlock;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2018
 *
 */
public class MimicChestItemBlock extends ItemBlock {

	/**
	 * 
	 * @param block
	 */
	public MimicChestItemBlock(Block block) {
		super(block);
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// get the block
		MimicChestBlock tb = (MimicChestBlock)getBlock();
		ITreasureChestTileEntity te = tb.getTileEntity();
	
		// chest info		
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + tb.getRarity().toString()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_locks", TextFormatting.DARK_BLUE + String.valueOf(tb.getChestType().getMaxLocks())));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.container_size", TextFormatting.DARK_GREEN + String.valueOf(te.getNumberOfSlots())));
	}	
}
