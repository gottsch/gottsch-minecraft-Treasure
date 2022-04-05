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
 * @author Mark Gottschling on Jul 26, 2018
 *
 */
public class SpanishMossItem extends ModItem {

	/**
	 * 
	 */
	public SpanishMossItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (WorldInfo.isClientSide(worldIn)) {
            return EnumActionResult.SUCCESS;
        }

		// determine the correct position of the block
    	BlockPos  p = null;
    	if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
    		p = pos;
    	}
    	else {
	 		switch (facing) {
	    		case SOUTH:
	    			p = pos.south();
	    			break;
	    		case WEST:
	    			p = pos.west();
	    			break;
	    		case NORTH:
		    		p = pos.north();
		    		break;
	    		case EAST:
	    			p = pos.east();
	    			break;
	    		case UP:
	    			p = pos.up();
	    			break;
	    		case DOWN:
	    			p = pos.down();
	    			break;
				default:
					p = pos.north();
					break;
			}
    	}
       	Block block = TreasureBlocks.SPANISH_MOSS;
       	
       	// determine if the block can be placed
       if (!worldIn.getBlockState(p.up()).isSideSolid(worldIn, p.up(), EnumFacing.DOWN)) {
    	   return EnumActionResult.FAIL;
       }
       	
       	// get the item held in hand
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
 		Treasure.LOGGER.debug("Placing spanish moss into world.");
    	world.setBlockState(pos, block.getDefaultState());    		      	
    	
    	Treasure.LOGGER.debug("Calling onBlockPlacedby.");
    	block.onBlockPlacedBy(world, pos, block.getDefaultState(), player, itemStack);
	}
}
