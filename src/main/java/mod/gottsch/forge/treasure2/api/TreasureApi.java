/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.api;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.IKeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityRegistry;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class TreasureApi {
	
	/**
	 * 
	 * @param rarity
	 */
	public static void registerRarity(IRarity rarity) {
		// create tags
		TagKey<Item> keyTag = TreasureTags.Items.mod(Treasure.MODID, "keys/" + rarity.getValue());
		TagKey<Item> lockTag = TreasureTags.Items.mod(Treasure.MODID, "locks/" + rarity.getValue());
		TagKey<Block> chestTag = TreasureTags.Blocks.mod(Treasure.MODID, "chests/" + rarity.getValue());

		RarityRegistry.registerKeyLockChests(rarity, keyTag, lockTag, chestTag);
	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 */
	public static void registerRarity(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag) {
		RarityRegistry.registerKeyLocks(rarity, keyTag, lockTag);
	}
	
//	public static void registerRarity(IRarity rarity, TagKey<Block> tag) {
//		RarityRegistry.register(rarity, tag);
//	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 * @param chestTag
	 */
	public static void registerRarity(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag, TagKey<Block> chestTag) {
		RarityRegistry.registerKeyLockChests(rarity, keyTag, lockTag, chestTag);
	}
	
	public static void registerKey(RegistryObject<KeyItem> key) {
		KeyLockRegistry.registerKey(key);
	}

	public static void registerLock(RegistryObject<LockItem> lock) {
		KeyLockRegistry.registerLock(lock);		
	}
	
	public static void registerKeyLockCategory(IKeyLockCategory category) {
		KeyLockRegistry.registerCategory(category);
	}
	// TODO add method for Locks accepting keys

	public static void registerChest(RegistryObject<Block> chest) {
		ChestRegistry.register(chest);
	}
}
