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
        
		// TEST
		if (stack.hasTagCompound()) {
				NBTTagList nbttaglist = stack.getTagCompound().getTagList("Items", 10);	
	        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
	        	
	        }
			tooltip.add("# of items: " + String.valueOf(nbttaglist.tagCount()));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		ItemStack stack = player.getHeldItem(hand);
		
		// see all the nbts in the item
		NBTTagCompound nbt;
        if(stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
            Treasure.logger.debug("Item has NBT");
            
			NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(nbt, items);
			for (ItemStack s : items) {
				Treasure.logger.debug("[HoldingItem] item in chest item -> {}", s.getDisplayName());
			}
			
        } else {
            nbt = new NBTTagCompound();
            Treasure.logger.debug("Item does not have NBT");
        }
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// TODO Auto-generated method stub

		
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		// see all the nbts in the item
		NBTTagCompound nbt;
        if(stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
            Treasure.logger.debug("Item has NBT");
            
			NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(nbt, items);
			for (ItemStack s : items) {
				Treasure.logger.debug("[HoldingItem] item in chest item -> {}", s.getDisplayName());
			}
			
        } else {
            nbt = new NBTTagCompound();
            Treasure.logger.debug("Item does not have NBT");
        }
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	

}
