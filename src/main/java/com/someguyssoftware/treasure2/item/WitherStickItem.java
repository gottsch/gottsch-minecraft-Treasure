/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModBlockItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.InteractionResult;

/**
 * @author Mark Gottschling on Nov 14, 2014
 *
 */
public class WitherStickItem extends ModBlockItem {
	
	/**
	 * 
	 */
	public WitherStickItem(String modID, String name, Block block, Item.Properties properties) {
		super(modID, name, block, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP));
	}
	
	@Override
	public InteractionResult useOn(ItemUseContext context) {
		if (WorldInfo.isClientSide(context.getLevel())) {
            return InteractionResult.PASS;
        }
		BlockState state = TreasureBlocks.WITHER_BRANCH.defaultBlockState().setValue(WitherBranchBlock.FACING, context.getHorizontalDirection().getOpposite());
		
 		ItemStack heldItem = context.getPlayer().getItemInHand(context.getHand());	     		
 		this.placeBlock(new BlockItemUseContext(context), state);     		
 		heldItem.shrink(1);            
        return InteractionResult.SUCCESS;
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
		context.getLevel().setBlock(context.getClickedPos(), state, 3);
		return true;
	}
}