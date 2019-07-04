/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;

import net.minecraft.block.Block;
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
//        else if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
//        	return EnumActionResult.PASS;
//        }

        	Block block = TreasureBlocks.WITHER_BRANCH;
        	 
        	BlockPos  p = null;
     		switch (facing) {
	    		case SOUTH:
	    			p = pos.north();
	    			break;
	    		case WEST:
	    			p = pos.east();
	    			break;
	    		case NORTH:
		    		p = pos.south();
		    		break;
	    		case EAST:
	    			p = pos.west();
	    			break;
			default:
				p = pos.north();
				break;
    		}
     		
     		ItemStack heldItem = player.getHeldItem(hand);	     		
     		this.placeBlock(worldIn, p, block, player, heldItem);     		
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
	public void placeBlock(World world, BlockPos pos, Block block, EntityPlayer player, ItemStack itemStack) {
    	// set the block
 		Treasure.logger.debug("Placing wither branch into world.");
    	world.setBlockState(pos, block.getDefaultState());    		      	
    	
    	Treasure.logger.debug("Calling onBlockPlacedby.");
    	block.onBlockPlacedBy(world, pos, block.getDefaultState(), player, itemStack);
	}
}
