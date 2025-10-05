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
package mod.gottsch.forge.treasure2.api;

import mod.gottsch.forge.gottschcore.enums.IEnum;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.EnumRegistry;
import mod.gottsch.forge.treasure2.core.registry.MimicRegistry;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishables;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class TreasureApi {
	public static final String FEATURE_TYPE = "featureType";

	public static List<IRarity> getRarities() {
		return TreasureRarities.getRarities();
	}

	/*
	 * core functionality. may need so modders can add their own feature types.
	 */
	public static void registerFeatureType(IFeatureType type) {
		EnumRegistry.register(FEATURE_TYPE, type);
	}
	
	public static Optional<IFeatureType> getFeatureType(String key) {
		IEnum ienum = EnumRegistry.get(FEATURE_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IFeatureType) ienum);
		}
	}

	// TODO candidate to move to data setup
	public static void registerWishableHandler(Item item, IWishableHandler handler) {
		TreasureWishables.registerHandler(item, handler);
	}

	/*
	 * core functionality. don't move to data setup. ie don't want players to be able to change
	 * a mimic from its respective chest.
	 */
	public static void registerMimic(ResourceLocation chestName, ResourceLocation mimicName) {
		MimicRegistry.register(chestName, mimicName);
	}

	/**
	 * this method no longer does anything. it is here for legacy purposes
	 * so existing mods (loot packs) don't break.
	 * @param modID
	 */
	@Deprecated(forRemoval = true)
	public static void registerLootTables(String modID) {
		// do nothing
	}
}
