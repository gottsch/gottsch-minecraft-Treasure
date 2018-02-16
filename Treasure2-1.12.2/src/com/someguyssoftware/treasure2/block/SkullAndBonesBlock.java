/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
public class SkullAndBonesBlock extends CardinalDirectionFacadeBlock {
	
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
