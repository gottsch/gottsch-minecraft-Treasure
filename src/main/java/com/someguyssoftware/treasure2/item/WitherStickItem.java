/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Nov 14, 2014
 *
 */
public class WitherStickItem extends ModItem {
	
	/**
	 * 
	 */
	public WitherStickItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (WorldInfo.isClientSide(worldIn)) {
            return EnumActionResult.PASS;
        }

		BlockPos p = pos.offset(facing);
		IBlockState state = TreasureBlocks.WITHER_BRANCH.getDefaultState().withProperty(WitherBranchBlock.FACING,
				player.getHorizontalFacing().getOpposite());
		
 		ItemStack heldItem = player.getHeldItem(hand);	     		
 		this.placeBlock(worldIn, p, state, player, heldItem);     		
 		heldItem.shrink(1);            
        return EnumActionResult.SUCCESS;
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
	public void placeBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack itemStack) {
		// set the block
		world.setBlockState(pos, state);
		state.getBlock().onBlockPlacedBy(world, pos, state, player, itemStack);
	}
}
