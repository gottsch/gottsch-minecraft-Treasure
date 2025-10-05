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
package mod.gottsch.forge.treasure2.core.persistence;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.cache.TreasureChestCache;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeightsManager;
import net.minecraft.nbt.CompoundTag;
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