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
package com.someguyssoftware.treasure2.item;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Aug 14, 2021
 *
 */
public interface IWishable {
	public static final String DROPPED_BY_KEY = "droppedBy";
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param entityItem
	 * @param coords
	 */
	public Optional<ItemStack> generateLoot(World world, Random random, ItemStack itemStack, ICoords coords);
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param itemStacks
	 * @param category
	 * @param rarity
	 * @param lootContext
	 */
	default public void injectLoot(World world, Random random, List<ItemStack> itemStacks, String category, Rarity rarity, LootContext lootContext) {
		Optional<List<LootTableShell>> injectLootTableShells = buildInjectedLootTableList(category, rarity);		
		if (injectLootTableShells.isPresent()) {
			itemStacks.addAll(TreasureLootTableRegistry.getLootTableMaster().getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
	default public Optional<List<LootTableShell>> buildInjectedLootTableList(String key, Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getLootTableByKeyRarity(TreasureLootTableMaster2.ManagedTableType.INJECT, key, rarity));
	}
}
