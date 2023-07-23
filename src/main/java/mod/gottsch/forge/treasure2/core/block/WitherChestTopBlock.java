/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


/**
 * Does NOT appear in any creative tab.
 * @author Mark Gottschling on Jun 26, 2018
 *
 */
public class WitherChestTopBlock extends Block implements ITreasureChestBlockProxy, ITreasureBlock {
	protected static final VoxelShape AABB =  Block.box(1, 0, 1, 15, 10, 15);
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties 
	 */
	public WitherChestTopBlock(Properties properties) {
		super(properties);
	}

	/**
	 * Return the position of the chest.
	 */
	@Override
	public BlockPos getChestPos(BlockPos pos) {
		return pos.below();
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	/**
	 * 
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		Treasure.LOGGER.info("wither chest TOP activated");
		// get the block at pos.down()
		BlockState bottomState = world.getBlockState(pos.below());
		return bottomState.getBlock().use(bottomState, world, pos.below(), player, hand, result);
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {
		
		BlockPos downPos = pos.below();
		Block downBlock = level.getBlockState(downPos).getBlock();
		if (downBlock == TreasureBlocks.WITHER_CHEST.get()) {
			downBlock.onDestroyedByPlayer(state, level, downPos, player, willHarvest, fluid);
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		BlockPos downPos = pos.below();
		Block downBlock = level.getBlockState(downPos).getBlock();
		if (downBlock == TreasureBlocks.WITHER_CHEST.get()) {
			downBlock.onBlockExploded(state, level, downPos, explosion);
		}
	}
	
	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		return false;
	}

	/**
	 * Render using a TESR.
	 */
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
