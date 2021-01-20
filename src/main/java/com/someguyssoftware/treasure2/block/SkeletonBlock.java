/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.world.WorldInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonBlock extends GravestoneBlock {

	public static final PropertyEnum<SkeletonBlock.EnumPartType> PART = PropertyEnum.<SkeletonBlock.EnumPartType>create("part", SkeletonBlock.EnumPartType.class);

    // TODO move to  TreasureBlock or something that requires a Shape
    /*
	 * An array of VoxelShape bounds for the bounding box
	 */
    private VoxelShape[] bounds = new VoxelShape[4];
    
	/**
	 * 
	 */
	public SkeletonBlock(String modID, String name, Material material) {
		super(modID, name, material);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, SkeletonBlock.EnumPartType.BOTTOM));
		this.bounds[0] =new AxisAlignedBB(0F, 0F, 0F, 1F, 0.375F, 1F);	// S
		this.bounds[1] = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.375F, 1F);	// W
		this.bounds[2] = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.375F, 1F); // N
		this.bounds[3] = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.375F, 1F);	// E
	}

	/**
	 * 
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PART, FACING);
	}
	
    // TODO ensure to add the CUTOUT in TreasureGui
	
	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		if (state.getValue(PART) == SkeletonBlock.EnumPartType.BOTTOM) {
			// if the head is something other than SkeletonBlock, indicating that is has been destroyed
			if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		}
		// this is the the head
		else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
			if (WorldInfo.isServerSide(worldIn)) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}
			worldIn.setBlockToAir(pos);
		}
	}

	// @Override
	// public Item getItemDropped(IBlockState state, Random rand, int fortune) {
	// 	if (state.getValue(PART) == SkeletonBlock.EnumPartType.BOTTOM)
	// 		return Items.AIR;

	// 	int value = rand.nextInt(100);

	// 	// 5% of the time, drop skull
	// 	if (value < 5) {
	// 		return Items.SKULL;
	// 	}
	// 	// 20%, drop block item
	// 	else if (value >= 5 && value < 25) {
	// 		return Item.getItemFromBlock(this);
	// 	}
	// 	// 75%, drop bone
	// 	else {
	// 		return Items.BONE;
	// 	}
	// }

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == SkeletonBlock.EnumPartType.BOTTOM) {
			BlockPos blockpos = pos.offset((EnumFacing) state.getValue(FACING));

			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		IBlockState blockState = super.getStateFromMeta(meta);
		return (meta & 8) > 0 ? blockState.withProperty(PART, SkeletonBlock.EnumPartType.TOP)
				: blockState.withProperty(PART, SkeletonBlock.EnumPartType.BOTTOM);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		int i = 0;
//		i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		i = i | super.getMetaFromState(state);

		if (state.getValue(PART) == SkeletonBlock.EnumPartType.TOP) {
			i |= 8;
		}

		return i;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	/**
	 * 
	 * @author Mark Gottschling on Feb 2, 2019
	 *
	 */
	public static enum EnumPartType implements IStringSerializable {
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

}