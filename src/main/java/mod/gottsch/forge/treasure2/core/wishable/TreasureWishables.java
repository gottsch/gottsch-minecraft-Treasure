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
package mod.gottsch.forge.treasure2.core.wishable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityTagAssociation;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

/**
 * @author Mark Gottschling on Nov 25, 2022
 *
 */
public class TreasureWishables {

	private static final String WISHABLE = "wishable";

	private static final Map<Item, IWishableHandler> HANDLER_MAP;

	static {
		HANDLER_MAP = Maps.newHashMap();
	}

	/**
	 * determines the Rarity for a given Item by checking its tags against a data-driven list.
	 * @param item the Item to check.
	 * @return the RegistryObject<RarityEntry> associated with the item, or a default.
	 */
	public static Optional<IRarityEntry> getRarity(Item item) {
		return getRarityAssociations().stream()
				.filter(association -> item.builtInRegistryHolder().is(association.getItemTag()))
				.findFirst()
				.flatMap(association -> TreasureRarities.getRarityByName(association.rarityId()));
	}

	public static List<RarityTagAssociation> getRarityAssociations() {
		return RarityTagAssociationRegistry.Items.getAssociation(WISHABLE);
	}

	public static void registerHandler(Item item, IWishableHandler handler) {
		HANDLER_MAP.put(item, handler);
	}
	
	public static Optional<IWishableHandler> getHandler(Item item) {
		if (HANDLER_MAP.containsKey(item)) {
			return Optional.of(HANDLER_MAP.get(item));
		}
		return Optional.empty();
	}
	
//	public static void register(Item item) {
//		ResourceLocation name = ModUtil.getName(item);
//		if (!BY_NAME.containsKey(name)) {
//			BY_NAME.put(name, item);
//		}
//	}
	
//	public static void registerByRarity(IRarity rarity, Item item) {
//		ResourceLocation name = ModUtil.getName(item);
//		BY_RARITY.put(rarity, item);
//		RARITY_BY_NAME.put(name, rarity);
//	}
	
//	public static List<Item> getAll() {
//		return new ArrayList<>(BY_NAME.values());
//	}
	
//	public static void clearByRarity() {
//		BY_RARITY.clear();
//		RARITY_BY_NAME.clear();
//	}
	
//	public static Optional<IRarity> getRarity(ResourceLocation name) {
//		if (RARITY_BY_NAME.containsKey(name)) {
//			return Optional.of(RARITY_BY_NAME.get(name));
//		}
//		return Optional.empty();
//	}
	
//	public static Optional<IRarity> getRarity(Item wishable) {
//		ResourceLocation name = ModUtil.getName(wishable);
//		return getRarity(name);
//	}

//	public static boolean isRegistered(Item item) {
//		ResourceLocation name = ModUtil.getName(item);
//		return isRegistered(name);
//	}
	
//	public static boolean isRegistered(ResourceLocation name) {
//		return BY_NAME.containsKey(name);
//	}
}
