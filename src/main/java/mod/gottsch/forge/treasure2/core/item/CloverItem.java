/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.IWishingWellBlock;
import mod.gottsch.forge.treasure2.core.block.SkeletonBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;

/**
 * @author Mark Gottschling on Aug 19, 2024
 *
 */
public class CloverItem extends Item {
	/**
	 *
	 * @param block
	 * @param properties
	 */
	public CloverItem(Block block, Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, worldIn, tooltip, flag);
		Component lore = Component.translatable(LangUtil.tooltip("clover"));
		for (String s : lore.getString().split("~")) {
			tooltip.add(Component.translatable(s).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		// exit if on the client
		if (WorldInfo.isClientSide(context.getLevel())) {
			return InteractionResult.FAIL;
		}

		BlockPos pos = context.getClickedPos();
		BlockState state = context.getLevel().getBlockState(pos);
		Block block = state.getBlock();

		// determine if block at pos is a wishing well block candidate
		if (state.is(TreasureTags.Blocks.WISHING_WELL_CANDIDATES)) {
			// start search for all attached well blocks up to radius X
			List<BlockPos> visited = new ArrayList<>();
			Queue<BlockPos> active = new LinkedList<>();

			active.add(context.getClickedPos());
			while (!active.isEmpty()) {
				BlockPos activePos = active.poll();
				// update activePos block to wishing well block
				// NOTE if adding any more types of bricks, use a Map
				Block activeBlock = context.getLevel().getBlockState(activePos).getBlock();
				if (activeBlock.equals(Blocks.MOSSY_COBBLESTONE)) {
					context.getLevel().setBlockAndUpdate(activePos, TreasureBlocks.WISHING_WELL_MOSSY_COBBLESTONE.get().defaultBlockState());
				} else if (activeBlock.equals(Blocks.COBBLESTONE)) {
					context.getLevel().setBlock(activePos, TreasureBlocks.WISHING_WELL_COBBLESTONE.get().defaultBlockState(), 3);
				} else if (activeBlock.equals(Blocks.MOSSY_STONE_BRICKS)) {
					context.getLevel().setBlock(activePos, TreasureBlocks.WISHING_WELL_MOSSY_STONE_BRICKS.get().defaultBlockState(), 3);
				} else if (activeBlock.equals(Blocks.STONE_BRICKS)) {
					context.getLevel().setBlockAndUpdate(activePos, TreasureBlocks.WISHING_WELL_STONE_BRICKS.get().defaultBlockState());
				} else {
					context.getLevel().setBlockAndUpdate(activePos, TreasureBlocks.WISHING_WELL_MOSSY_COBBLESTONE.get().defaultBlockState());
				}

				RandomSource random = context.getLevel().getRandom();
				int coinIndex = random.nextInt(3);
				SimpleParticleType coinParticle = switch(coinIndex) {
					case 0 -> TreasureParticles.COPPER_COIN_PARTICLE.get();
					case 1 -> TreasureParticles.SILVER_COIN_PARTICLE.get();
					default -> TreasureParticles.GOLD_COIN_PARTICLE.get();
				};
				((ServerLevel)context.getLevel()).sendParticles(coinParticle,
						(double)activePos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1),
						(double)activePos.getY() + random.nextDouble() + random.nextDouble(),
						(double)activePos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1),
						0,
						0.0D, 0.07D, 0.0D, 0.3);


				// check all adjacent neighbors
				checkAndAdd(context.getLevel(), pos, activePos.north(), active, visited);
				checkAndAdd(context.getLevel(), pos, activePos.south(), active, visited);
				checkAndAdd(context.getLevel(), pos, activePos.east(), active, visited);
				checkAndAdd(context.getLevel(), pos, activePos.west(), active, visited);
				checkAndAdd(context.getLevel(), pos, activePos.above(), active, visited);
				checkAndAdd(context.getLevel(), pos, activePos.below(), active, visited);

				// remove activePos from active list
				active.remove(activePos);

				// add active pos to the visited list
				visited.add(activePos);
			}

			// shrink the item
			context.getItemInHand().shrink(1);
		}
		return InteractionResult.SUCCESS;
	}

	private void checkAndAdd(Level level, BlockPos activePos, BlockPos targetPos, Queue<BlockPos> active, List<BlockPos> visited) {
		boolean isCandidate = level.getBlockState(targetPos).is(TreasureTags.Blocks.WISHING_WELL_CANDIDATES);
		double dist = activePos.distSqr(targetPos);
		boolean isVisited = visited.contains(targetPos);
		Block targetBlock = level.getBlockState(targetPos).getBlock();

		if (isCandidate && dist < 25 && !isVisited) {
			active.add(targetPos);
		}
	}
}