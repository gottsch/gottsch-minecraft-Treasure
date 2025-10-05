/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.BoneChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 
 * @author Mark Gottschling on Sep 25, 2022
 *
 */
public class BoneLock extends LockItem {

	/**
	 * NOTE lock is not added to any item tab.
	 * @param properties
	 */
	public BoneLock(Properties properties) {
		super(properties);
	}
	
	public BoneLock(Properties properties, KeyItem[] keys) {
		super(properties, keys);
	}

	@Override
	public boolean handleHeldLock(AbstractTreasureChestBlockEntity blockEntity, Player player, ItemStack heldItem) {
		return false;
	}

	// TODO what about re-locking?
	public void doUnlock(Level level, Player player, BlockPos chestPos, LockState lockState) {

		Treasure.LOGGER.debug("in bone lock doUnlock()");
		doUnlockedEffects(level, player, chestPos, lockState);
		 
		// NOTE does NOT remove the lock from the lock state.
		// the bone lock is a "permanent" lock of the bone chest.
		
		// update TE's locked property OR this can be executed from the BoneKey
		BlockEntity blockEntity = level.getBlockEntity(chestPos);
		if (blockEntity instanceof BoneChestBlockEntity chestBlockEntity) {
			chestBlockEntity.setLocked(false);
		}
	}
	
	@Override
	public void dropLock(Level level, BlockPos pos) {
		// do nothing ie. don't drop a lock
	}
}
