/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Feb 27, 2018
 *
 */
public class FogBlock extends ModBlock {
	// public static final PropertyBool DECAYABLE =
	// PropertyBool.create("decayable");
	// public static final PropertyBool CHECK_DECAY =
	// PropertyBool.create("check_decay");
	// public static final PropertyInteger DECAY = PropertyInteger.create("decay",
	// 0, 3);

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public FogBlock(String modID, String name, Material material) {
		super(modID, name, material);
		// this.setDefaultState(blockState.getBaseState()
		// .withProperty(DECAYABLE, true)
		// .withProperty(CHECK_DECAY, false)
		// .withProperty(DECAY, 0)
		// );
		setNormalCube(false);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	/**
	 * 
	 * @return
	 */
	// @Override
	// protected BlockStateContainer createBlockState() {
	// return new BlockStateContainer(this, new IProperty[] {DECAYABLE, CHECK_DECAY,
	// DECAY});
	// }

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

//	@Override
//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//		return NULL_AABB;
//	}

	/**
	 * 
	 */
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * 
	 */
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return false;
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	/**
	 * Whether this Block can be replaced directly by other blocks (true for e.g.
	 * tall grass)
	 */
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
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
	 * @param blockState
	 * @param blockAccess
	 * @param pos
	 * @param side
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}
		
		return false;
	}

	protected boolean canSilkHarvest() {
		return true;
	}

	// /**
	// * Convert the given metadata into a BlockState for this Block
	// */
	// @Override
	// public IBlockState getStateFromMeta(int meta) {
	//// EnumFacing enumFacing = EnumFacing.getFront(meta);
	//// if (enumFacing.getAxis() == EnumFacing.Axis.Y)
	//// {
	//// enumFacing = EnumFacing.NORTH;
	//// }
	//// return this.getDefaultState().withProperty(FACING, enumFacing);
	// return this.getDefaultState();
	// }
	//
	// /**
	// * Convert the BlockState into the correct metadata value
	// */
	// @Override
	// public int getMetaFromState(IBlockState state) {
	//// return state.getValue(FACING).getIndex();
	// return 0;
	// }
}
