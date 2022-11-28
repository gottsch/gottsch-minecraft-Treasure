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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Maps;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class RarityRegistry {
	private static final Map<String, IRarity> REGISTRY = Maps.newHashMap();
	// L = key, M = lock, R = chest
	private static final Map<IRarity, Triple<TagKey<Item>, TagKey<Item>, TagKey<Block>>> TAGS_REGISTRY = Maps.newHashMap();

	/*
	 * other registries
	 */
	// wishable registry
	private static final Map<IRarity, TagKey<Item>> WISHABLE_TAGS_REGISTRY = Maps.newHashMap();
	
	private RarityRegistry() {}
	
//	public static void register(IRarity rarity) {
//		registerRarity(rarity);
		
		// TODO need to create new TagKeys base on rarity name (convention over config)
//	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 */
	public static void registerKeyLocks(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag) {
		registerRarity(rarity);
		registerTags(rarity, Triple.of(keyTag, lockTag, null));
	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 * @param chestTag
	 */
	public static void registerKeyLockChests(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag, TagKey<Block> chestTag) {
		registerRarity(rarity);
		registerTags(rarity, Triple.of(keyTag, lockTag, chestTag));
	}
	
	public static void registerKeys(IRarity rarity, TagKey<Item> keyTag) {
		// TODO individual registering. get the triple if exists and update with key or create
	}
	
	public static void registerLocks(IRarity rarity, TagKey<Item> lockTag) {
		
	}
	
	public static void registerChests(IRarity rarity, TagKey<Block> chestTag) {
		
	}
	
	public static void registerWishable(IRarity rarity, TagKey<Item> wishableTag) {
		registerRarity(rarity);
		registerWishableTag(rarity, wishableTag);
	}
	
	private static void registerRarity(IRarity rarity) {
		if (!REGISTRY.containsKey(rarity.getName())) {
			REGISTRY.put(rarity.getName(), rarity);
		}
	}
		
	private static void registerTags(IRarity rarity, Triple<TagKey<Item>, TagKey<Item>, TagKey<Block>> tagTuple) {
		if (!TAGS_REGISTRY.containsKey(rarity)) {
			TAGS_REGISTRY.put(rarity, tagTuple);
		}
	}
	
	private static void registerWishableTag(IRarity rarity, TagKey<Item> wishableTag) {
		if (!WISHABLE_TAGS_REGISTRY.containsKey(rarity)) {
			WISHABLE_TAGS_REGISTRY.put(rarity, wishableTag);
		}
	}

	public static void unregister(IRarity rarity) {
		if (REGISTRY.containsKey(rarity.getName())) {
			REGISTRY.remove(rarity.getName());
		}
	}
	
	public static Optional<IRarity> getRarity(String name) {
		IRarity rarity = REGISTRY.get(name);
		if (name == null || rarity == null) {
			return Optional.empty();
		}
		return Optional.of(rarity);
	}
	
	public static List<IRarity> getValues() {
		return new ArrayList<>(REGISTRY.values());
	}
	
	public int size() {
		return REGISTRY.size();
	}

	public static TagKey<Item> getKeyTag(IRarity rarity) {
		if (TAGS_REGISTRY.containsKey(rarity)) {
			return TAGS_REGISTRY.get(rarity).getLeft();
		}
		else {
			return null;
		}
	}
	
	public static TagKey<Item> getLockTag(IRarity rarity) {
		if (TAGS_REGISTRY.containsKey(rarity)) {
			return TAGS_REGISTRY.get(rarity).getMiddle();
		}
		else {
			return null;
		}
	}
	
	public static TagKey<Block> getChestTag(IRarity rarity) {
		if (TAGS_REGISTRY.containsKey(rarity)) {
			return TAGS_REGISTRY.get(rarity).getRight();
		}
		return null;
	}

	public static TagKey<Item> getWishableTag(IRarity rarity) {
		if (WISHABLE_TAGS_REGISTRY.containsKey(rarity)) {
			return WISHABLE_TAGS_REGISTRY.get(rarity);
		}
		return null;
	}
}
