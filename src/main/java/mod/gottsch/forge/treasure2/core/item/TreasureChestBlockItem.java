/*
 * This file is part of  Treasure2.
 * Copyright (c) 2017 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import java.util.*;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.IWishingWellBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.wishable.IWishable;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.C;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureChestBlockItem extends BlockItem {

	/**
	 * 
	 * @param block
	 */
	public TreasureChestBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// get the block
		AbstractTreasureChestBlock tb = (AbstractTreasureChestBlock) getBlock();

		boolean isLocked = Optional.ofNullable(stack.getTag()).map(tag -> {
			if (tag.contains("lockStates")) {
				ListTag list = tag.getList("lockStates", Tag.TAG_COMPOUND);
				for(Tag state : list) {
					if (LockState.load((CompoundTag) state).getLock() != null) return true;
				}
			}
			return false;
		}).orElse(false);

		if (isLocked) {
			tooltip.add(Component.translatable(LangUtil.tooltip("chest.locked")).withStyle(ChatFormatting.RED));
			// usage
			tooltip.add(Component.translatable(LangUtil.tooltip("chest.usage")).withStyle(ChatFormatting.GOLD));
			tooltip.add(Component.literal(LangUtil.NEWLINE));
		}

		// chest info		
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.rarity"), ChatFormatting.BLUE + tb.getRarity().toString()));
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.max_locks"), ChatFormatting.BLUE + String.valueOf(tb.getLockLayout().getMaxLocks())));
		int size = tb.getBlockEntityInstance() != null ? tb.getBlockEntityInstance().getInventorySize() : ChestInventorySize.getSizeOf(tb);
		tooltip.add(Component.translatable(LangUtil.tooltip("chest.container_size"), ChatFormatting.DARK_GREEN + String.valueOf(size)));

		// TODO if locked at tooltip to throw into well
	}

	/**
	 *
	 */
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		Level level = entity.level();
		if (WorldInfo.isClientSide(level)) {
			return super.onEntityItemUpdate(stack, entity);
		}

		// check if check is locked
		CompoundTag tag = stack.getTag();
		ListTag lockStateTags;
		if (tag != null && tag.contains("lockStates")) {
			lockStateTags = tag.getList("lockStates", Tag.TAG_COMPOUND);
			if (lockStateTags.isEmpty()) {
				return super.onEntityItemUpdate(stack, entity);
			}
		} else {
			return super.onEntityItemUpdate(stack, entity);
		}

		List<BlockPos> wishingWellPosList = isValidLocation(entity);
		if (!wishingWellPosList.isEmpty()) {

			// transform wishing well blocks back to normal blocks
			BlockPos pos = wishingWellPosList.get(0);
			BlockState state = level.getBlockState(pos);

			// determine if block at pos is a wishing well block candidate
//			if (state.is(TreasureTags.Blocks.WISHING_WELLS)) {
			if (state.getBlock() instanceof IWishingWellBlock) {
				// start search for all attached well blocks up to radius X
				List<BlockPos> visited = new ArrayList<>();
				Queue<BlockPos> active = new LinkedList<>();

				active.add(pos);
				while (!active.isEmpty()) {
					BlockPos activePos = active.poll();
					// update activePos block to wishing well block
					// NOTE if adding any more types of bricks, use a Map
					Block activeBlock = level.getBlockState(activePos).getBlock();
					if (activeBlock.equals(TreasureBlocks.WISHING_WELL.get())) {
						level.setBlockAndUpdate(activePos, Blocks.MOSSY_COBBLESTONE.defaultBlockState());
					} else if (activeBlock.equals(TreasureBlocks.WISHING_WELL_COBBLESTONE.get())) {
						level.setBlock(activePos, Blocks.COBBLESTONE.defaultBlockState(), 3);
					} else if (activeBlock.equals(TreasureBlocks.WISHING_WELL_MOSSY_COBBLESTONE.get())) {
						level.setBlock(activePos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
					} else if (activeBlock.equals(TreasureBlocks.WISHING_WELL_MOSSY_STONE_BRICKS.get())) {
						level.setBlock(activePos, Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 3);
					} else if (activeBlock.equals(TreasureBlocks.WISHING_WELL_STONE_BRICKS.get())) {
						level.setBlockAndUpdate(activePos, Blocks.STONE_BRICKS.defaultBlockState());
					} else {
						level.setBlockAndUpdate(activePos, Blocks.MOSSY_COBBLESTONE.defaultBlockState());
					}

					// check all adjacent neighbors
					checkAndAdd(level, pos, activePos.north(), active, visited);
					checkAndAdd(level, pos, activePos.south(), active, visited);
					checkAndAdd(level, pos, activePos.east(), active, visited);
					checkAndAdd(level, pos, activePos.west(), active, visited);
					checkAndAdd(level, pos, activePos.above(), active, visited);
					checkAndAdd(level, pos, activePos.below(), active, visited);

					// remove activePos from active list
					active.remove(activePos);

					// add active pos to the visited list
					visited.add(activePos);
				}
			}

			// call lightning
			ModUtil.SpawnEntityHelper.spawn((ServerLevel) level, level.getRandom(), EntityType.LIGHTNING_BOLT, EntityType.LIGHTNING_BOLT.create(level), new Coords(entity.blockPosition()));

			explode(level, entity, wishingWellPosList.get(0));

			// remove chest lock from each lock state
			ListTag newLockStates = new ListTag();
			lockStateTags.forEach(lockStateTag -> {
				LockState lockState = LockState.load((CompoundTag) lockStateTag);
				lockState.removeLock();
				newLockStates.add(lockState.save(new CompoundTag()));
			});
			tag.put("lockStates", newLockStates);

			return true;
		}
		return super.onEntityItemUpdate(stack, entity);
	}

	/**
	 * this method is very similar to the one in IWishableHandler except that this returns
	 * a list of BlockPos where IWishingWellBlocks are located.
	 * @param itemEntity
	 * @return
	 */
	public List<BlockPos> isValidLocation(ItemEntity itemEntity) {
		List<BlockPos> posList = new ArrayList<>();
		int count = 0;
		// check if in water
		if (itemEntity.level().getBlockState(itemEntity.blockPosition()).is(Blocks.WATER)) {
			// NOTE use vanilla classes as this scan will be performed frequently and don't need the overhead.
			int scanRadius = Config.SERVER.wells.scanForWellRadius.get();
			BlockPos pos = itemEntity.blockPosition().offset(-scanRadius, 0, -scanRadius);
			for (int z = 0; z < (scanRadius * 2) + 1; z++) {
				for (int x = 0; x < (scanRadius * 2) + 1; x++) {
					Block block = itemEntity.level().getBlockState(pos.offset(x, 0, z)).getBlock();
					if (block instanceof IWishingWellBlock) {
						count++;
						posList.add(pos.offset(x, 0, z));
					}
					if (itemEntity.blockPosition().below().getY() > itemEntity.level().getMinBuildHeight()) {
						block = itemEntity.level().getBlockState(pos.offset(x, -1, z)).getBlock();
						if (block instanceof IWishingWellBlock) {
							count++;
							posList.add(pos.offset(x, -1, z));
						}
					}
					if (count >= Config.SERVER.wells.scanMinBlockCount.get()) {
						return posList;
					}
				}
			}
		}
		return posList;
	}

	protected void checkAndAdd(Level level, BlockPos activePos, BlockPos targetPos, Queue<BlockPos> active, List<BlockPos> visited) {
		boolean isCandidate = level.getBlockState(targetPos).getBlock() instanceof IWishingWellBlock;
		double dist = activePos.distSqr(targetPos);
		boolean isVisited = visited.contains(targetPos);
		Block targetBlock = level.getBlockState(targetPos).getBlock();

		if (isCandidate && dist <= 25 && !isVisited) {
			active.add(targetPos);
		}
	}

	/**
	 *
	 */
	protected void explode(Level level, ItemEntity entity, BlockPos pos) {
		level.explode(entity, pos.getX() +0.5, pos.getY(), entity.getZ() + 0.5, 4.0F, Level.ExplosionInteraction.TNT);
	}
}
