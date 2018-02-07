/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
		
		// TODO chest info
	}
}
