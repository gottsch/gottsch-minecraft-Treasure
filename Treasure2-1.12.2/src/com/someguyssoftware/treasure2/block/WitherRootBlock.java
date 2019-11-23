/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.config.ModConfig;
import com.someguyssoftware.treasure2.item.TreasureItems;

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
 * 
 * @author Mark Gottschling on Apr 7, 2018
 *
 */
public class WitherRootBlock extends CardinalDirectionFacadeBlock implements ITreasureBlock {
	
	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public WitherRootBlock(String modID, String name) {
		super(modID, name, Material.WOOD);
		setSoundType(SoundType.WOOD);
//		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);
		setBounds(
				new AxisAlignedBB[] {
				new AxisAlignedBB(0.25F, 0F, 0F, 0.75F, 0.5F, 1F), 	// N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F),  	// E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F),  	// S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F) 	// W)
				}
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
	 * Drops WitherRootItem or a stick.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {

		if (RandomHelper.checkProbability(random, ModConfig.WITHER_TREE.witherRootItemGenProbability)) {
			return TreasureItems.WITHER_ROOT_ITEM;
		}
		return Items.STICK;
    }

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
	}
}
