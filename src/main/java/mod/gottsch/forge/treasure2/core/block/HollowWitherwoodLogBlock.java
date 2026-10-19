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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * A rotted-out witherwood log: bark 2 px thick around an open core, along its axis. Lying down it is a
 * fallen log that small mobs can pass through (a player can't fit the 12 px opening); standing it is a
 * hollow stump. Shares the witherwood log's mossy property.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class HollowWitherwoodLogBlock extends RotatedPillarBlock implements ITreasureBlock {
	private static final Map<Direction.Axis, VoxelShape> SHAPES = new EnumMap<>(Direction.Axis.class);

	static {
		SHAPES.put(Direction.Axis.Y, Shapes.or(
				Block.box(0, 0, 0, 16, 16, 2), Block.box(0, 0, 14, 16, 16, 16),
				Block.box(0, 0, 2, 2, 16, 14), Block.box(14, 0, 2, 16, 16, 14)));
		SHAPES.put(Direction.Axis.X, Shapes.or(
				Block.box(0, 0, 0, 16, 2, 16), Block.box(0, 14, 0, 16, 16, 16),
				Block.box(0, 2, 0, 16, 14, 2), Block.box(0, 2, 14, 16, 14, 16)));
		SHAPES.put(Direction.Axis.Z, Shapes.or(
				Block.box(0, 0, 0, 16, 2, 16), Block.box(0, 14, 0, 16, 16, 16),
				Block.box(0, 2, 0, 2, 14, 16), Block.box(14, 2, 0, 16, 14, 16)));
	}

	public HollowWitherwoodLogBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(WitherwoodRotatedPillarBlock.MOSSY, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WitherwoodRotatedPillarBlock.MOSSY);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(AXIS));
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
}
