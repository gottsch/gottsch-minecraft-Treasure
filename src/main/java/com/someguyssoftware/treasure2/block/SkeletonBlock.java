/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonBlock extends GravestoneBlock {

	public static final EnumProperty<SkeletonBlock.EnumPartType> PART = EnumProperty.<SkeletonBlock.EnumPartType>create("part", SkeletonBlock.EnumPartType.class);

	// TODO move to  TreasureBlock or something that requires a Shape
	/*
	 * An array of VoxelShape bounds for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];

	/**
	 * 
	 */
	public SkeletonBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(PART, SkeletonBlock.EnumPartType.BOTTOM));

		VoxelShape shape = Block.makeCuboidShape(1, 0, 0, 15, 6, 16);
		setBounds(
				new VoxelShape[] {
						shape, 	// N
						shape,  	// E
						shape,  	// S
						shape	// W
				});
	}

	/**
	 * 
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PART, FACING);
	}

	/**
	 * 
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 * ie. after the bottom/feet has been placed
	 */
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote) {
			BlockPos blockPos = pos.offset(state.get(FACING).getOpposite());
			worldIn.setBlockState(blockPos, state.with(PART, SkeletonBlock.EnumPartType.TOP), 3);
			worldIn.notifyNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(worldIn, pos, 3);
		}
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		Direction facing = (Direction) state.get(FACING);
		if (state.get(PART) == SkeletonBlock.EnumPartType.BOTTOM) {
			BlockPos blockPos = pos.offset(facing.getOpposite());

			if (worldIn.getBlockState(blockPos).getBlock() == this) {
				worldIn.destroyBlock(blockPos, false);
			}
		}
		else {
			BlockPos blockPos = pos.offset(facing);
			if (worldIn.getBlockState(blockPos).getBlock() == this) {
				worldIn.destroyBlock(blockPos, false);
			}
		}
	}

	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
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