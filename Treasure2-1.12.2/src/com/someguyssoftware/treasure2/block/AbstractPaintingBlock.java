/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.PaintingItem;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
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

	private PaintingItem item;
	
	/*
	 * the rarity of the painting
	 */
	private Rarity rarity;

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
		setBoundingBox(new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F) // W
		);
		setRarity(Rarity.SCARCE);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param rarity
	 */
	public AbstractPaintingBlock(String modID, String name, Material material, Rarity rarity) {
		this(modID, name, material);
		setRarity(rarity);
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

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	/**
	 * Prevent torches and buttons from being placed on block.
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	/**
	 * 
	 */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		} else {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}

	/**
	 * 
	 */
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.NORTH.getHorizontalIndex()]);
		} else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.SOUTH.getHorizontalIndex()]);
		} else if (state.getValue(FACING) == EnumFacing.EAST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.EAST.getHorizontalIndex()]);
		} else if (state.getValue(FACING) == EnumFacing.WEST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.WEST.getHorizontalIndex()]);
		} else {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bounds[EnumFacing.NORTH.getHorizontalIndex()]);
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Cube cube = new Cube(worldIn, new Coords(pos));
		if ((!cube.isAir() && !cube.isReplaceable()) || cube.getState().getBlock() instanceof AbstractPaintingBlock) return false;
		
		for (EnumFacing enumfacing : EnumFacing.values()) {
			if (canPlaceBlock(worldIn, pos, enumfacing)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param facing
	 * @return
	 */
	private boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos blockpos = pos.offset(facing.getOpposite());
		IBlockState state = worldIn.getBlockState(blockpos);
		Block block = state.getBlock();
		BlockFaceShape blockFaceShape = state.getBlockFaceShape(worldIn, blockpos, facing);
		
		if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
			return !isExceptBlockForAttachWithPiston(block) && blockFaceShape == BlockFaceShape.SOLID;
		} else {
			return false;
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.checkForDrop(worldIn, pos, state);
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		this.onNeighborChangeInternal(worldIn, pos, state);
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @return
	 */
	protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.checkForDrop(worldIn, pos, state)) {
			return true;
		} else {
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			BlockPos blockpos = pos.offset(enumfacing1);
			boolean flag = false;

			if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID) {
				flag = true;
			}

			if (flag) {
				this.dropBlockAsItem(worldIn, pos, state, 1);
				worldIn.setBlockToAir(pos);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @return
	 */
	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this && this.canPlaceBlock(worldIn, pos, (EnumFacing) state.getValue(FACING))) {
			return true;
		} else {
			if (worldIn.getBlockState(pos).getBlock() == this) {
				this.dropBlockAsItem(worldIn, pos, state, 1);
				worldIn.setBlockToAir(pos);
			}
			return false;
		}
	}

	/**
	 * 
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return getItem();
	}

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 * @return
	 */
	public AbstractPaintingBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity
	 *            the rarity to set
	 */
	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	/**
	 * @return the item
	 */
	public PaintingItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(PaintingItem item) {
		this.item = item;
	}
}
