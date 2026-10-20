/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Shelf fungus growing out of the side of a log: one to three brackets, stacked by placing more on it,
 * like sea pickles. FACING is the way the brackets point, away from the log they grow on; they break
 * if that log goes. No collision.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class BracketFungusBlock extends HorizontalDirectionalBlock implements ITreasureBlock {
	public static final int MAX_COUNT = 3;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX_COUNT);

	/** selection boxes for a fungus on the south face of its log, pointing north (see the models) */
	private static final VoxelShape[] NORTH_SHAPES = {
			Block.box(3, 5, 9, 13, 8, 16),
			Block.box(3, 5, 9, 13, 12, 16),
			Block.box(2, 1, 9, 13, 12, 16)
	};
	private static final Map<Direction, VoxelShape[]> SHAPES = new EnumMap<>(Direction.class);

	static {
		for (Direction d : Direction.Plane.HORIZONTAL) {
			VoxelShape[] shapes = new VoxelShape[MAX_COUNT];
			for (int i = 0; i < MAX_COUNT; i++) {
				shapes[i] = rotate(NORTH_SHAPES[i], d);
			}
			SHAPES.put(d, shapes);
		}
	}

	public BracketFungusBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COUNT, 1));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, COUNT);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING))[state.getValue(COUNT) - 1];
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction facing = state.getValue(FACING);
		BlockPos log = pos.relative(facing.getOpposite());
		return level.getBlockState(log).isFaceSturdy(level, log, facing);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
		return direction == state.getValue(FACING).getOpposite() && !canSurvive(state, level, pos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, direction, neighbour, level, pos, neighbourPos);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return !context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && state.getValue(COUNT) < MAX_COUNT
				|| super.canBeReplaced(state, context);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
		if (existing.is(this)) {
			return existing.setValue(COUNT, Math.min(MAX_COUNT, existing.getValue(COUNT) + 1));
		}
		// on the clicked side of a log, or else on whichever side of the block faces the player
		for (Direction d : context.getNearestLookingDirections()) {
			if (d.getAxis().isHorizontal()) {
				BlockState state = defaultBlockState().setValue(FACING, d.getOpposite());
				if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
					return state;
				}
			}
		}
		return null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	/** a north-facing box turned to face d, about the block's centre */
	private static VoxelShape rotate(VoxelShape north, Direction d) {
		double x1 = north.min(Direction.Axis.X) * 16, x2 = north.max(Direction.Axis.X) * 16;
		double y1 = north.min(Direction.Axis.Y) * 16, y2 = north.max(Direction.Axis.Y) * 16;
		double z1 = north.min(Direction.Axis.Z) * 16, z2 = north.max(Direction.Axis.Z) * 16;
		return switch (d) {
			case SOUTH -> Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
			case EAST -> Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
			case WEST -> Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
			default -> north;
		};
	}
}
