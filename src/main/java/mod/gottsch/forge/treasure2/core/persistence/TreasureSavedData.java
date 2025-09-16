/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.persistence;

import java.util.List;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.cache.FeatureCaches;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestCache;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeightsManager;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;


/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class TreasureSavedData extends SavedData {	
	private static final String CHEST_GEN_REGISTRY_NAME = "weightedChestGeneratorRegistry";

	private static final String TREASURE = Treasure.MODID;
	
	public static TreasureSavedData create() {
		TreasureSavedData data = new TreasureSavedData();
		return data;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromTag(net.minecraft.nbt.CompoundTag)
	 */
	public static TreasureSavedData load(CompoundTag tag) {
		Treasure.LOGGER.debug("loading treasure2 persisted data...");

        /*
         * chest cache
         */
		if (tag.contains(TreasureChestCache.TAG_NAME)) {
			TreasureChestCache.load(tag);
		}

		if (tag.contains(RarityWeightsManager.RARITY_SELECTOR_TAG)) {
			RarityWeightsManager.load(tag);
		}

		// NOTE could return null here. but this line is moot because the data is loaded
		//	into singleton manager/caches.
        return create();
	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToTag(net.minecraft.nbt.CompoundTag)
	 */
	@Override
	public CompoundTag save(CompoundTag tag) {
		try {
			// save chest cache
			TreasureChestCache.save(tag);
			Treasure.LOGGER.debug("should have saved chest list to tag");

			// TODO save weighted rarities
			RarityWeightsManager.save(tag);
			Treasure.LOGGER.debug("should have saved rarity weight manager to tag");
		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.LOGGER.error("an exception occurred:", e);
		}
	
		return tag;
	}


	/**
	 * @param world
	 * @return
	 */
	public static TreasureSavedData get(Level world) {
		DimensionDataStorage storage = ((ServerLevel)world).getDataStorage();
		TreasureSavedData data = (TreasureSavedData) storage.computeIfAbsent(TreasureSavedData::load, 
				TreasureSavedData::create, TREASURE);
		return data;
	}
}