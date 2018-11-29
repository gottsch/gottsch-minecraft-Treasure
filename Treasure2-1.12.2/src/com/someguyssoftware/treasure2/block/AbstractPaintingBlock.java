/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Nov 8, 2018
 *
 */
public abstract class AbstractPaintingBlock extends CardinalDirectionFacadeBlock implements ITreasureBlock {


	
	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * @param modID
	 * @param name
	 * @param material
	 */
	public AbstractPaintingBlock(String modID, String name, Material material) {
		super(modID, name, material);
		setSoundType(SoundType.WOOD); // TODO find vanilla painting code
		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(0.1F);
		setBoundingBox(
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)		// W
				);
	}
	
	/**
	 * 
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		// face the block towards the player (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);
	}
	
	/**
	 * don't return an item - that is handled in breakBlock();
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	/**
	 * 
	 */
	@Override
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
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.NORTH.getHorizontalIndex()]);
		}
		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.SOUTH.getHorizontalIndex()]);
		}
		else if (state.getValue(FACING) == EnumFacing.EAST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.EAST.getHorizontalIndex()]);
		}
		else if (state.getValue(FACING) == EnumFacing.WEST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.WEST.getHorizontalIndex()]);
		}
		else {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.NORTH.getHorizontalIndex()]);
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
	public AbstractPaintingBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}
}

