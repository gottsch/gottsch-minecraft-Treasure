/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * TODO Gravestone and SkullAndBones are identical in the sense of CardinalDirectionFacadeBlock and bound.
 *  create a new abstract class for them.
 * @author Mark Gottschling on Oct 21, 2014
 *
 */
public class SkullAndBonesBlock extends CardinalDirectionFacadeBlock implements ITreasureBlock, IFogSupport {
	
	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 * @param material
	 * @param name
	 */
	public SkullAndBonesBlock(String modID, String name, Material material) {
		super(modID, name, material);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);
		setBoundingBox(
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)	// W
				);
	}

	  /**
     * Determines if this block can prevent leaves connected to it from decaying.
     * @param state The current state
     * @param world The current world
     * @param pos Block position in world
     * @return true if the presence this block can prevent leaves from decaying.
     */
	@Override
    public boolean canSustainFog(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }
	
	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		}
		else {		
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}
	
	/**
	 * 
	 */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	int value = rand.nextInt(100);
    	
    	// 5% of the time, drop skull
     	if (value < 5 ) {
     		return Items.SKULL;
    	}
     	// 20%, drop block item
    	else if (value >= 5 && value < 25) {
    		return Item.getItemFromBlock(this);
   		}
     	// 75%, drop bone
    	else {
    		return Items.BONE;
    	}       
    }
    
	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 * @return 
	 */
	public SkullAndBonesBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
