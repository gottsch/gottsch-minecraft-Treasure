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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

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
			itemStacks.addAll(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
	default public Optional<List<LootTableShell>> buildInjectedLootTableList(String key, Rarity rarity) {
		return Optional.ofNullable(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByKeyRarity(TreasureLootTableMaster2.ManagedTableType.INJECT, key, rarity));
	}

	/**
	 * 
	 * @return
	 */
	default public List<LootTableShell> getLootTables() {
		return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.COMMON);
	}
	
	/**
	 * 
	 * @param random
	 * @return
	 */
	default public ItemStack getDefaultLootKey (Random random) {
		List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.COMMON));
		return new ItemStack(keys.get(random.nextInt(keys.size())));
	}
	
	/**
	 * 
	 * @param random
	 * @return
	 */
	default public Rarity getDefaultEffectiveRarity(Random random) {
		return Rarity.UNCOMMON;
	}
	
	/**
	 * 
	 * @return
	 */
	@Deprecated
	default public LootContext getLootContext() {
		return TreasureLootTableRegistry.getLootTableMaster().getContext();
	}
	
	/**
	 * 
	 * @param world
	 * @param player
	 * @return
	 */
	default public LootContext getLootContext(World world, EntityPlayer player) {
		if (player == null) return getLootContext();
		LootContext lootContext = new LootContext.Builder((WorldServer) world)
				.withLuck(player != null ? player.getLuck() : 0)
				.withPlayer(player)
				.build();
		return lootContext;
	}
}