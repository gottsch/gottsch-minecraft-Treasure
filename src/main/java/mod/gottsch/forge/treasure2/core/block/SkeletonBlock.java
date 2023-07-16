/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.block;

import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonBlock extends GravestoneBlock {

	public static final EnumProperty<SkeletonBlock.EnumPartType> PART = EnumProperty.<SkeletonBlock.EnumPartType>create("part", SkeletonBlock.EnumPartType.class);

	/**
	 * 
	 */
	public SkeletonBlock(Block.Properties properties) {
		super(properties);
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
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 * ie. after the bottom/feet has been placed
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (WorldInfo.isServerSide(level)) {
			BlockPos blockPos = pos.relative(state.getValue(FACING).getOpposite());
			level.setBlock(blockPos, state.setValue(PART, SkeletonBlock.EnumPartType.TOP), 3);
			level.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(level, pos, 3);
		}
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		Direction facing = (Direction) state.getValue(FACING);
		if (state.getValue(PART) == SkeletonBlock.EnumPartType.BOTTOM) {
			ICoords coords = new Coords(pos);
			BlockPos blockPos = coords.add(facing.getOpposite(), 1).toPos();
//			BlockPos blockPos = pos.relative(facing.getOpposite());

			if (level.getBlockState(blockPos).getBlock() == this) {
				Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), level, blockPos, 3);
			}
		}
		else {
			BlockPos blockPos = pos.relative(facing);
			if (level.getBlockState(blockPos).getBlock() == this) {
				Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), level, blockPos, 3);

			}
		}
	}

   @Deprecated
   public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
   }
	   
	/**
	 * 
	 * @author Mark Gottschling on Feb 2, 2019
	 *
	 */
	public static enum EnumPartType implements StringRepresentable {
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