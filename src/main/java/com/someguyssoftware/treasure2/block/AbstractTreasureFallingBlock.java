/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.ModFallingBlock;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


/**
 * @author Mark Gottschling on Feb 9, 2021
 *
 */
public abstract class AbstractTreasureFallingBlock extends ModFallingBlock implements ITreasureBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	protected static final VoxelShape BOUNDING_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	protected static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	
	public AbstractTreasureFallingBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}
	
	/**
	 * 
	 */
	@Override
protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return BOUNDING_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE;
	}

	/**
	 * Called When an Entity Collided with the Block.
	 */
	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entityIn) {
//		Treasure.LOGGER.debug("stepped on block...");
		// only on server
		if (!WorldInfo.isClientSide(world)) {
			if (!(entityIn instanceof Player)) {
				return;
			}
			// set to activated
			world.setBlock(pos, defaultBlockState().setValue(ACTIVATED, Boolean.valueOf(true)), 3);
			// initiate fall
			tick(world.getBlockState(pos), (ServerLevel)world, pos, null);
		}
	}

	/**
	 * 
	 * @param state
	 * @param worldIn
	 * @param pos
	 * @param rand
	 */
	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		// ensure the block is activated
		if (!worldIn.getBlockState(pos).getValue(ACTIVATED)) {
			return;
		}
		super.tick(state, worldIn, pos, null);
	}
	
	/**
	 * 
	 */
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		// do nothing
	}
}
