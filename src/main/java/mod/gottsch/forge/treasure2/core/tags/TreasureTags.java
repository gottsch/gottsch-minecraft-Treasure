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
package mod.gottsch.forge.treasure2.core.tags;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityLootTableAssociationRegistry;
import mod.gottsch.forge.treasure2.core.registry.TagRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		public static final TagKey<Block> CAULDRON_CHESTS = mod(Treasure.MODID, "chests/rarity/special/cauldron");

		// wishing well candidates
		public static final TagKey<Block> WISHING_WELL_CANDIDATES = mod(Treasure.MODID, "wells/candidates");


		// gravestone base
		public static final TagKey<Block> GRAVESTONE_BASE = mod(Treasure.MODID, "structures/gravestone_base");

		public static TagKey<Block> mod(String domain, String path) {
			return BlockTags.create(new ResourceLocation(domain, path));
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> ALL_OVERWORLD = mod(Treasure.MODID, "all_overworld");
		public static final TagKey<Biome> BOP_OVERWORLD = mod(Treasure.MODID, "bop_overworld");
		public static final TagKey<Biome> BWG_FOREST = mod(Treasure.MODID, "bwg_forest");
		public static final TagKey<Biome> BWG_JUNGLE = mod(Treasure.MODID, "bwg_jungle");
		public static final TagKey<Biome> BWG_DESERT = mod(Treasure.MODID, "bwg_desert");
		public static final TagKey<Biome> BWG_IS_LAND = mod(Treasure.MODID, "bwg_is_land");
		public static final TagKey<Biome> BWG_IS_DRY = mod(Treasure.MODID, "bwg_is_dry");
		public static final TagKey<Biome> BWG_IS_WET = mod(Treasure.MODID, "bwg_is_wet");
		public static final TagKey<Biome> BWG_IS_OCEAN = mod(Treasure.MODID, "bwg_is_ocean");

		// well biomes
		public static final TagKey<Biome> WELLS_GENERAL = mod(Treasure.MODID, "wells_general");
		public static final TagKey<Biome> WELLS_FOREST = mod(Treasure.MODID, "wells_forest");
		public static final TagKey<Biome> WELLS_JUNGLE = mod(Treasure.MODID, "wells_jungle");
		public static final TagKey<Biome> WELLS_DESERT = mod(Treasure.MODID, "wells_desert");

		// simple site biomes
		public static final TagKey<Biome> TEMPERATE = mod(Treasure.MODID, "temperate");

		// POC convert config file generator rarities white/blacklists into Tags
		public static final TagKey<Biome> WITHER_BIOME_WHITELIST = mod(Treasure.MODID, "config/generators/rarities/wither/whitelist");
		public static final TagKey<Biome> WITHER_BIOME_BLACKLIST = mod(Treasure.MODID, "config/generators/rarities/wither/blacklist");

		// chest rarity filters (ie blacklist)
		public static final TagKey<Biome> TERRANEAN_RARE_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/rare");
		public static final TagKey<Biome> TERRANEAN_EPIC_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/epic");
		public static final TagKey<Biome> TERRANEAN_LEGANDARY_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/legendary");
		public static final TagKey<Biome> TERRANEAN_MYTHICAL_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/mythical");
		public static final TagKey<Biome> TERRANEAN_SKULL_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/skull");
		public static final TagKey<Biome> TERRANEAN_GOLD_SKULL_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/gold_skull");
		public static final TagKey<Biome> TERRANEAN_CRYSTAL_SKULL_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/crystal_skull");
		public static final TagKey<Biome> TERRANEAN_CAULDRON_BIOME_FILTER = mod(Treasure.MODID, "biome/filter/chests/rarity/terranean/cauldron");


		public static TagKey<Biome> mod(String domain, String path) {
			return TagKey.create(Registries.BIOME, new ResourceLocation(domain, path));
		}
	}

	public static class Rarities {
		// a TagKey for this new custom registry.
		public static final TagKey<IRarityEntry> ALL_RARITIES = mod("all_rarities");
		public static final TagKey<IRarityEntry> SURFACE_CHEST_RARITIES = mod("structure/surface_chest/allowable_rarities");

		public static TagKey<IRarityEntry> mod(String path) {
			return mod(Treasure.MODID, path);
		}
//		public static TagKey<IRarity> mod(String path) {
//			return mod(Treasure.MODID, path);
//		}
		public static TagKey<IRarityEntry> mod(String domain, String path) {
			return TagKey.create(TreasureRarities.RARITIES_REGISTRY_KEY, new ResourceLocation(domain, path));
		}
//		public static TagKey<IRarity> mod(String domain, String path) {
//			return TagKey.create(TreasureRarities.RARITIES_REGISTRY_KEY, new ResourceLocation(domain, path));
//		}

		// NOTE new way - this probably needs to move to TreasureRarities or something. at least a Rarity specific method name.
		/**
		 * A helper method to check if a Rarity object is in a given tag.
		 * This is similar to how you would check if an Item is in an ItemTag.
		 * @param object The Rarity RegistryObject to check.
		 * @param tagKey The TagKey to check against.
		 * @return True if the Rarity is in the tag, false otherwise.
		 */
		public static boolean isInTag(RegistryObject<IRarityEntry> object, TagKey<IRarityEntry> tagKey) {
			// We get the registry from the supplier, then check the object's Holder.
			return TreasureRarities.RARITIES_REGISTRY_SUPPLIER.get().getHolder(object.getKey()).filter(holder -> holder.is(tagKey)).isPresent();
		}
	}

	// NOTE still need to register LootTables using this event
	@SubscribeEvent
	public static void registerTags(TagsUpdatedEvent event) {		
		Treasure.LOGGER.info("in tags updated event");

//		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
//			Set<ResourceLocation> ALL_LOOT_TABLES;
//
//			// Get the RegistryAccess provider from the event.
//			HolderLookup.Provider registries = event.getRegistryAccess();
//
//			// Use the provider to get the loot table registry.
//			HolderLookup.RegistryLookup<Object> lootTableRegistryLookup = registries.lookupOrThrow(Registries.LOOT_TABLE);
//
//			// Get all ResourceLocation keys from the loot table registry.
//			ALL_LOOT_TABLES = lootTableRegistryLookup.listElementIds().collect(Collectors.toSet());
//
//			System.out.println("Found " + ALL_LOOT_TABLES.size() + " total loot tables after tags updated.");
//
//			// Example: Filter for chest loot tables
//			List<ResourceLocation> chestLootTables = ALL_LOOT_TABLES.stream()
//					.filter(location -> location.getPath().startsWith("chests/"))
//					.collect(Collectors.toList());
//
//			// process all the loot table associations
//			RarityLootTableAssociationRegistry.getAssociations().forEach(association -> {
//				// get all the loot tables for rarity at loot table location
//				RegistryAccess access = event.getRegistryAccess();
//			});
//		}

		// DEPRECATED
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
//					Treasure.LOGGER.info("registering key -> {} by rarity -> {}", ModUtil.getName(key.get()), rarity);
					break;
				}
			}			
		});

		// DEPRECATED
		KeyLockRegistry.getLocks().forEach(lock -> {
			Holder.Reference<Item> holder = lock.get().builtInRegistryHolder();

			for (IRarity rarity : TreasureApi.getRarities()) {			
				//			for (IRarity rarity : RarityRegistry.getValues()) {
				TagKey<Item> tag = TagRegistry.getLockTag(rarity);
				if (tag != null && holder.is(tag)) {
					// register the lock in the key-lock registry by rarity
					KeyLockRegistry.registerLockByRarity(rarity, lock);
//					Treasure.LOGGER.info("registering lock -> {} by rarity -> {}", ModUtil.getName(lock.get()), rarity);
					break;
				}
			}			
		});

		// DEPRECATED
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
//					Treasure.LOGGER.info("registering chest -> {} by rarity -> {}", ModUtil.getName(chest.get()), rarity);
					break;
				}
			}			
		});

		/*
		 * process tags to register wishables according to rarity
		 */
		// DEPRECATED
//		for (IRarity rarity : TreasureApi.getRarities()) {
//			TagKey<Item> tagKey = TagRegistry.getWishableTag(rarity);
//			if (tagKey != null) {
//				ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(tagKey);
//				for (Iterator<Item> iterator = tag.iterator(); iterator.hasNext();) {
//					Item wishable = iterator.next();
//					// register the wishable in the wishable registry by rarity
//					WishableRegistry.registerByRarity(rarity, wishable);
//					Treasure.LOGGER.info("registering wishable -> {} by rarity -> {}", ModUtil.getName(wishable), rarity);
//				}
//			}
//		}

		/*
		 * process tags to register biome white/blacklists by rarity
		 */
//		for (IRarity rarity : TreasureApi.getRarities()) {
//			Optional<TagKey<Biome>> tagKey = TagRegistry.getBiomeWhitelistTag(rarity);
//			tagKey.ifPresent(key -> {
//				ITag<Biome> tag = ForgeRegistries.BIOMES.tags().getTag(key);
//				for (Iterator<Biome> iterator = tag.iterator(); iterator.hasNext();) {
//					Biome biome = iterator.next();
//					....
//				}
//			});
//		}
	}
}
