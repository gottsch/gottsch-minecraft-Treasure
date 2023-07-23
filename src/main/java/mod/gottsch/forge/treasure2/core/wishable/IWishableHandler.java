/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.wishable;

import java.util.Optional;
import java.util.Random;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.core.block.IWishingWellBlock;
import mod.gottsch.forge.treasure2.core.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 
 * @author Mark Gottschling May 26, 2023
 *
 */
public interface IWishableHandler {

	/**
	 * 
	 * @param itemEntity
	 * @return
	 */
	default public boolean isValidLocation(ItemEntity itemEntity) {
		int count = 0;
		// check if in water
		if (itemEntity.level().getBlockState(itemEntity.blockPosition()).is(Blocks.WATER)) {
			// NOTE use vanilla classes as this scan will be performed frequentlly and don't need the overhead.
			int scanRadius = Config.SERVER.wells.scanForWellRadius.get();
			BlockPos pos = itemEntity.blockPosition().offset(-scanRadius, 0, -scanRadius);
			for (int z = 0; z < (scanRadius * 2) + 1; z++) {
				for (int x = 0; x < (scanRadius * 2) + 1; x++) {
					Block block = itemEntity.level().getBlockState(pos).getBlock();
					if (block instanceof IWishingWellBlock) {
						count++;
					}					
					if (count >= Config.SERVER.wells.scanMinBlockCount.get()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param itemEntity
	 */
	default public void doWishable(ItemEntity itemEntity) {
			Random random = new Random();
			for (int itemIndex = 0; itemIndex < itemEntity.getItem().getCount(); itemIndex++) {
				// generate an item for each item in the stack
				ICoords coords = new Coords(itemEntity.blockPosition());
				Optional<ItemStack> lootStack = generateLoot(itemEntity.level(), random, itemEntity, coords);
				if (lootStack.isPresent()) {
					// spawn the item 
					Containers.dropItemStack(itemEntity.level(), (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), lootStack.get());
				}
			}
			// remove the item entity
			itemEntity.remove(RemovalReason.DISCARDED);
	}

	/**
	 * 
	 * @param level
	 * @param random
	 * @param itemEntity
	 * @param coords
	 * @return
	 */
	public Optional<ItemStack> generateLoot(Level level, Random random, ItemEntity itemEntity, ICoords coords);
}
