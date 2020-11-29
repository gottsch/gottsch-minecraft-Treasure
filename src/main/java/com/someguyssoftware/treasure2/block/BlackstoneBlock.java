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
 * 
 * @author Mark Gottschling on Sep 8, 2020
 *
 */
public class BlackstoneBlock extends ModBlock implements IWishingWellBlock {
	public static Logger logger = LogManager.getLogger(BlackstoneBlock.class);
	
	/**
	 * 
	 * @param material
	 */
	public BlackstoneBlock(String modID, String name) {
		super(modID, name, Material.ROCK);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		this.setHardness(3.0F);	
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(TreasureBlocks.BLACKSTONE);
    }
}
