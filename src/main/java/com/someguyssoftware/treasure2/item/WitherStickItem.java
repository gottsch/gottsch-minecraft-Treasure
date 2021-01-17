/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModBlockItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

/**
 * @author Mark Gottschling on Nov 14, 2014
 *
 */
public class WitherStickItem extends ModBlockItem {
	
	/**
	 * 
	 */
	public WitherStickItem(String modID, String name, Block block, Item.Properties properties) {
		super(modID, name, block, properties.group(TreasureItemGroups.MOD_ITEM_GROUP));
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (WorldInfo.isClientSide(context.getWorld())) {
            return ActionResultType.PASS;
        }
		BlockState state = TreasureBlocks.WITHER_BRANCH.getDefaultState().with(WitherBranchBlock.FACING, context.getPlacementHorizontalFacing().getOpposite());
		
 		ItemStack heldItem = context.getPlayer().getHeldItem(context.getHand());	     		
 		this.placeBlock(new BlockItemUseContext(context), state);     		
 		heldItem.shrink(1);            
        return ActionResultType.SUCCESS;
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @param itemStack
	 */
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		// set the block
		context.getWorld().setBlockState(context.getPos(), state);
		return true;
	}
}