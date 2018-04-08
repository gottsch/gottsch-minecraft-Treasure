/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import net.minecraft.item.Item;

/**
 * @author Mark Gottschling on Nov 14, 2014
 *
 */
public class WitherStickItem extends Item {
	
	/**
	 * 
	 */
	public WitherStickItem() {
//		setCreativeTab(Treasure.treasureTab);
//		setUnlocalizedName(IdsConfig.witherStickItemId);
		//setTextureName(TexturesConfig.witherStickItemTexture);
	}
//	
//	/**
//	 * 
//	 */
//	@Override
//    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
//    		float hitX, float hitY, float hitZ) {
//        if (world.isRemote) {
//            return true;
//        }
//        else if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
//        	return false;
//        }
//        else {
//        	Block block = TreasureBlocks.witherBranch;
//        	int playerDirection = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//        	int xOffset = 0;
//        	int zOffset = 0;
//        	 
//     		switch (playerDirection) {
//	    		case 0: // south
//	    		zOffset = -1;
//	    		break;
//	    		case 1: // west
//	    		xOffset = 1;
//	    		break;
//	    		case 2: // north
//	    		zOffset = 1;
//	    		break;
//	    		case 3: // east
//	    		xOffset = -1;
//	    		break;
//    		}
//
//     		this.placeBlock(world, pos.add(xOffset, 0, zOffset), block, player, itemStack);	        	
//        	// decrement the stack
//            --itemStack.stackSize;
//            return true;
//
//        }
//    }
//	
//	/**
//	 * 
//	 * @param world
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @param player
//	 * @param itemStack
//	 */
//	public void placeBlock(World world, BlockPos pos, Block block, EntityPlayer player, ItemStack itemStack) {
//    	// set the block
// 		Treasure.logger.debug("Placing wither branch into world.");
//    	world.setBlockState(pos, block.getDefaultState());    		      	
//    	
//    	Treasure.logger.debug("Calling onBlockPlacedby.");
//    	block.onBlockPlacedBy(world, pos, block.getDefaultState(), player, itemStack);
//	}
}
