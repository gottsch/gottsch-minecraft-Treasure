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
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.nbt.CompoundTag;
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
	private static final String DIM_GEN_REGISTRY_NAME = "dimensionalGeneratedRegistry";
	private static final String FEATURE_CACHES_NAME = "featureCaches";

	private static final String TREASURE = Treasure.MODID;
	
	public static TreasureSavedData create() {
		return new TreasureSavedData();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromTag(net.minecraft.nbt.CompoundTag)
	 */
	public static TreasureSavedData load(CompoundTag tag) {
		Treasure.LOGGER.debug("loading treasure2 saved gen data...");
		
		/*
		 * chest generator registry
		 */
		if (tag.contains(CHEST_GEN_REGISTRY_NAME)) {
			RarityLevelWeightedChestGeneratorRegistry.initialize();
			RarityLevelWeightedChestGeneratorRegistry.load(tag.getList(CHEST_GEN_REGISTRY_NAME, Tag.TAG_COMPOUND));
			
//			RarityLevelWeightedChestGeneratorRegistry.RARITY_SELECTOR.forEach((dim, map) -> {
//				RarityLevelWeightedCollection dumpCol = map.get(FeatureType.TERRANEAN);
//				List<String> dump = dumpCol.dump();
////				Treasure.LOGGER.debug("load terranean weighted collection dump -> {}", dump);
//				dumpCol = map.get(FeatureType.AQUATIC);
//				dump = dumpCol.dump();
////				Treasure.LOGGER.debug("load aquatic weighted collection dump -> {}", dump);
//			});

		}
		
        /*
         * chest registry
         */
		if (tag.contains(DIM_GEN_REGISTRY_NAME)) {
			DimensionalGeneratedCache.clear();
			DimensionalGeneratedCache.initialize();
			DimensionalGeneratedCache.load((CompoundTag)tag.get(DIM_GEN_REGISTRY_NAME));
		}
		
		/*
		 * well cache
		 */
		if (tag.contains(FEATURE_CACHES_NAME)) {
			FeatureCaches.load((CompoundTag)tag.get(FEATURE_CACHES_NAME));
		}
		
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
			updateCompound(tag, CHEST_GEN_REGISTRY_NAME, RarityLevelWeightedChestGeneratorRegistry.save());

			// update tag
			updateCompound(tag, DIM_GEN_REGISTRY_NAME, DimensionalGeneratedCache.save());

			// update feature dimensional simple caches
			updateCompound(tag, FEATURE_CACHES_NAME, FeatureCaches.save());
		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.LOGGER.error("an exception occurred:", e);
		}
	
		return tag;
	}

	/**
	 * 
	 * @param compound
	 * @param name
	 * @param nbt
	 */
	private void updateCompound(CompoundTag compound, String name, Tag nbt) {
		// delete current tag
		compound.remove(name);
		// add new values
		compound.put(name, nbt);		
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