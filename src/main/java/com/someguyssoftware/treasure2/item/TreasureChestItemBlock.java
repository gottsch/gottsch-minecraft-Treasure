/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
		AbstractTreasureChestTileEntity te = tb.getTileEntity();
	
		// chest info		
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + tb.getRarity().toString()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_locks", TextFormatting.DARK_BLUE + String.valueOf(tb.getChestType().getMaxLocks())));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.container_size", TextFormatting.DARK_GREEN + String.valueOf(te.getNumberOfSlots())));
	}	
}
