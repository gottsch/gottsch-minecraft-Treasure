/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.core.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonBlock extends GravestoneBlock {

	public static final EnumProperty<SkeletonBlock.EnumPartType> PART = EnumProperty.<SkeletonBlock.EnumPartType>create("part", SkeletonBlock.EnumPartType.class);

	// TODO move to  TreasureBlock or something that requires a Shape
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];

	/**
	 * 
	 */
	public SkeletonBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, SkeletonBlock.EnumPartType.BOTTOM));

		VoxelShape shape = Block.box(1, 0, 0, 15, 6, 16);
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
protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(PART, FACING);
	}

	/**
	 * 
	 */
//	@Override
//	public boolean isNormalCube(BlockState state, LevelAccessor world, BlockPos pos) {
//		return false;
//	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 * ie. after the bottom/feet has been placed
	 */
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		if (WorldInfo.isServerSide(worldIn)) {
			BlockPos blockPos = pos.relative(state.getValue(FACING).getOpposite());
			worldIn.setBlock(blockPos, state.setValue(PART, SkeletonBlock.EnumPartType.TOP), 3);
			worldIn.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(worldIn, pos, 3);
		}
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void destroy(ILevel world, BlockPos pos, BlockState state) {
		Direction facing = (Direction) state.getValue(FACING);
		if (state.getValue(PART) == SkeletonBlock.EnumPartType.BOTTOM) {
			ICoords coords = new Coords(pos);
			BlockPos blockPos = coords.add(facing.getOpposite(), 1).toPos();
//			BlockPos blockPos = pos.relative(facing.getOpposite());

			if (world.getBlockState(blockPos).getBlock() == this) {
				Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), world, blockPos, 3);
			}
		}
		else {
			BlockPos blockPos = pos.relative(facing);
			if (world.getBlockState(blockPos).getBlock() == this) {
				Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), world, blockPos, 3);

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

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}

}