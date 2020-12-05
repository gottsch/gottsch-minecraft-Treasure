/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2018
 *
 */
public class OreBlock extends ModBlock {
	// logger
	public static Logger logger = LogManager.getLogger(OreBlock.class);
	
	private Item item;
	
	/**
	 * 
	 * @param material
	 */
	public OreBlock(String modID, String name, Material material) {
		super(modID, name, material	);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		this.setHarvestLevel("pickaxe", 3);
		this.setHardness(3.0F);	
		this.setResistance(5.0F);
	}

	/**
	 * This is required to process alpha channels in block textures
	 */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
   		return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return false;
    }
    
	/**
	 * 
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.item;
    }

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}
}
