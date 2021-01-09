/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

/**
 * @author Mark Gottschling on Sep 19, 2014
 *
 */
public class WishingWellBlock extends ModBlock implements IWishingWellBlock {
	// logger
	public static Logger logger = LogManager.getLogger(WishingWellBlock.class);
    
    VoxelShape shape = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);

	/**
	 * 
	 * @param material
	 */
	public WishingWellBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		// setSoundType(SoundType.STONE);
		// setCreativeTab(Treasure.TREASURE_TAB);
		// this.setHardness(2.0F);	
	}

    /**
     * 
     * @param state
     * @param worldIn
     * @param pos
     * @param context
     * @return
     */
    @Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
    }
    
	/**
	 * Drops vanilla mossy cobblestone instead of wishing well block i.e. the well loses it's magic on break.
	 */
	// @Override
	// public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    //     return Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE);
    // }
}