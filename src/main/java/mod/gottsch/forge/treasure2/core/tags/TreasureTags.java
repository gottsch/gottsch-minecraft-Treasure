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
package mod.gottsch.forge.treasure2.core.tags;

import java.util.Iterator;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.TagRegistry;
import mod.gottsch.forge.treasure2.core.registry.WishableRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

/**
 * 
 * @author Mark Gottschling on Nov 11, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TreasureTags {

	public static class Items {
		// keys
		public static final TagKey<Item> COMMON_KEY = mod(Treasure.MODID, "key/common");
		public static final TagKey<Item> UNCOMMON_KEY = mod(Treasure.MODID, "key/uncommon");
		public static final TagKey<Item> SCARCE_KEY = mod(Treasure.MODID, "key/scarce");
		public static final TagKey<Item> RARE_KEY = mod(Treasure.MODID, "key/rare");
		public static final TagKey<Item> EPIC_KEY = mod(Treasure.MODID, "key/epic");
		public static final TagKey<Item> LEGENDARY_KEYS = mod(Treasure.MODID, "key/legendary");
		public static final TagKey<Item> MYTHICAL_KEY = mod(Treasure.MODID, "key/mythical");
		public static final TagKey<Item> KEYS = mod(Treasure.MODID, "key/keys");

		// locks
		public static final TagKey<Item> COMMON_LOCKS = mod(Treasure.MODID, "lock/common");
		public static final TagKey<Item> UNCOMMON_LOCKS = mod(Treasure.MODID, "lock/uncommon");
		public static final TagKey<Item> SCARCE_LOCKS = mod(Treasure.MODID, "lock/scarce");
		public static final TagKey<Item> RARE_LOCKS = mod(Treasure.MODID, "lock/rare");
		public static final TagKey<Item> EPIC_LOCKS = mod(Treasure.MODID, "lock/epic");
		public static final TagKey<Item> LEGENDARY_LOCKS = mod(Treasure.MODID, "lock/legendary");
		public static final TagKey<Item> MYTHICAL_LOCKS = mod(Treasure.MODID, "lock/mythical");
		public static final TagKey<Item> LOCKS = mod(Treasure.MODID, "lock/locks");

		// wishables
		public static final TagKey<Item> COMMON_WISHABLE = mod(Treasure.MODID, "wishable/common");
		public static final TagKey<Item> UNCOMMON_WISHABLE = mod(Treasure.MODID, "wishable/uncommon");
		public static final TagKey<Item> SCARCE_WISHABLE = mod(Treasure.MODID, "wishable/scarce");
		public static final TagKey<Item> RARE_WISHABLE = mod(Treasure.MODID, "wishable/rare");
		public static final TagKey<Item> EPIC_WISHABLE = mod(Treasure.MODID, "wishable/epic");
		public static final TagKey<Item> LEGENDARY_WISHABLE = mod(Treasure.MODID, "wishable/legendary");
		public static final TagKey<Item> MYTHICAL_WISHABLE = mod(Treasure.MODID, "wishable/mythical");
		public static final TagKey<Item> WISHABLES = mod(Treasure.MODID, "wishable/wishables");

		// other
		public static final TagKey<Item> POUCH = mod(Treasure.MODID, "pouch");

		public static TagKey<Item> mod(String domain, String path) {
			return ItemTags.create(new ResourceLocation(domain, path));
		}
	}

	public static class Blocks {
		// chests
		public static final TagKey<Block> COMMON_CHESTS = mod(Treasure.MODID, "chests/rarity/core/common");
		public static final TagKey<Block> UNCOMMON_CHESTS = mod(Treasure.MODID, "chests/rarity/core/uncommon");
		public static final TagKey<Block> SCARCE_CHESTS = mod(Treasure.MODID, "chests/rarity/core/scarce");
		public static final TagKey<Block> RARE_CHESTS = mod(Treasure.MODID, "chests/rarity/core/rare");
		public static final TagKey<Block> EPIC_CHESTS = mod(Treasure.MODID, "chests/rarity/core/epic");
		public static final TagKey<Block> LEGENDARY_CHESTS = mod(Treasure.MODID, "chests/rarity/core/legendary");
		public static final TagKey<Block> MYTHICAL_CHESTS = mod(Treasure.MODID, "chests/rarity/core/mythical");

		// speciality chests
		public static final TagKey<Block> SKULL_CHESTS = mod(Treasure.MODID, "chests/rarity/special/skull");
		public static final TagKey<Block> GOLD_SKULL_CHESTS = mod(Treasure.MODID, "chests/rarity/special/gold_skull");
		public static final TagKey<Block> CRYSTAL_SKULL_CHESTS = mod(Treasure.MODID, "chests/rarity/special/crystal_skull");
		public static final TagKey<Block> WITHER_CHESTS = mod(Treasure.MODID, "chests/rarity/special/wither");

		public static TagKey<Block> mod(String domain, String path) {
			return BlockTags.create(new ResourceLocation(domain, path));
		}
	}

	@SubscribeEvent
	public static void registerTags(TagsUpdatedEvent event) {
		Treasure.LOGGER.info("in tags updated event");
		// clear key/locks registries
		KeyLockRegistry.clearKeysByRarity();
		KeyLockRegistry.clearLocksByRarity();

		// add all key/locks to registries
		KeyLockRegistry.getKeys().forEach(key -> {
			// NOTE ItemStack.is() is just a wrapper for Item.Holder.Reference.is()
			// so, it is being accessed directly here instead of creating a new ItemStack first.
			Holder.Reference<Item> holder = key.get().builtInRegistryHolder();

			for (IRarity rarity : TreasureApi.getRarities()) {	
				TagKey<Item> tag = TreasureApi.getKeyTag(rarity);// RarityRegistry.getKeyTag(rarity);
				if (tag != null && holder.is(tag)) {
					// register the key in the key-lock registry by rarity
					KeyLockRegistry.registerKeyByRarity(rarity, key);
					Treasure.LOGGER.info("registering key -> {} by rarity -> {}", ModUtil.getName(key.get()), rarity);
					break;
				}
			}			
		});

		KeyLockRegistry.getLocks().forEach(lock -> {
			Holder.Reference<Item> holder = lock.get().builtInRegistryHolder();

			for (IRarity rarity : TreasureApi.getRarities()) {			
				//			for (IRarity rarity : RarityRegistry.getValues()) {
				TagKey<Item> tag = TagRegistry.getLockTag(rarity);
				if (tag != null && holder.is(tag)) {
					// register the lock in the key-lock registry by rarity
					KeyLockRegistry.registerLockByRarity(rarity, lock);
					Treasure.LOGGER.info("registering lock -> {} by rarity -> {}", ModUtil.getName(lock.get()), rarity);
					break;
				}
			}			
		});

		/*
		 * process tags to and register chest according to rarity
		 */
		ChestRegistry.getAll().forEach(chest -> {
			Holder.Reference<Block> holder = chest.get().builtInRegistryHolder();

			for (IRarity rarity : TreasureApi.getRarities()) {	
				//			for (IRarity rarity : RarityRegistry.getValues()) {
				TagKey<Block> tag = TagRegistry.getChestTag(rarity);
				if (tag != null && holder.is(tag)) {
					ChestRegistry.registerByRarity(rarity, chest);
					Treasure.LOGGER.info("registering chest -> {} by rarity -> {}", ModUtil.getName(chest.get()), rarity);
					break;
				}
			}			
		});

		/*
		 * process tags to and register wishables according to rarity
		 */
		for (IRarity rarity : TreasureApi.getRarities()) {	
			TagKey<Item> tagKey = TagRegistry.getWishableTag(rarity);
			if (tagKey != null) {
				ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(tagKey);
				for (Iterator<Item> iterator = tag.iterator(); iterator.hasNext();) {
					Item wishable = iterator.next();
					// register the wishable in the wishable registry by rarity
					WishableRegistry.registerByRarity(rarity, wishable);
					Treasure.LOGGER.info("registering wishable -> {} by rarity -> {}", ModUtil.getName(wishable), rarity);
				}
			}
		}
	}
}
