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

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * @author Mark Gottschling on Jun 19, 2018
 *
 */
public class WitherChestBlock extends StandardChestBlock {

	/**
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 * @param rarity
	 * @param properties 
	 */
	public WitherChestBlock(Class<? extends AbstractTreasureChestBlockEntity> te, LockLayout type, Properties properties) {
		super(te, type, properties);
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);

		// add the placeholder block above
		worldIn.setBlock(pos.above(), TreasureBlocks.WITHER_CHEST_TOP.get().defaultBlockState(), 3);
	}

	/**
	 * 
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);

		// TODO Check if the wither chest (double high) can be placed at location.
		// need to go above and check if == air
		// however if fails will not return boolean... therefor the check must take place in the Item

		// add the placeholder block above
		level.setBlock(pos.above(), TreasureBlocks.WITHER_CHEST_TOP.get().defaultBlockState(), 3);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {

		// TODO hopefully this prevents block from being placed if it can't
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().above());
		// TODO wrap these check in BlockContext
		if (blockState.isAir() && blockState.canBeReplaced()) {
			return super.getStateForPlacement(context);
		}
		return null;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {

		// destory placeholder above
		BlockPos upPos = pos.above();
		Block topBlock = level.getBlockState(upPos).getBlock();
		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP.get()) {
			Block.updateOrDestroy(level.getBlockState(upPos), Blocks.AIR.defaultBlockState(), level, upPos, 3);
		}		
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	/**
	 * 
	 */
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		// destory placeholder above
		BlockPos upPos = pos.above();
		Block topBlock = level.getBlockState(upPos).getBlock();
		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP.get()) {
			Block.updateOrDestroy(level.getBlockState(upPos), Blocks.AIR.defaultBlockState(), level, upPos, 3);
		}
		
		super.onBlockExploded(state, level, pos, explosion);
	}

//	@Override
//	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
//		// destory placeholder above
//		BlockPos upPos = pos.above();
//		Block topBlock = level.getBlockState(upPos).getBlock();
//		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP.get()) {
//			Block.updateOrDestroy(level.getBlockState(upPos), Blocks.AIR.defaultBlockState(), level, upPos, 3);
//		}
//		// break as normal
//		super.destroy(level, pos, state);
//	}

}
