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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.cache.DimensionalSimpleDistanceCache;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.WeightedChestGeneratorRegistry;
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
//	private static final String TREASURE_GEN_TAG_NAME = "treasureGenerator";
	
	private static final String CHEST_GEN_REGISTRY_NAME = "weightedChestGeneratorRegistry";
	private static final String DIM_GEN_REGISTRY_NAME = "dimensionalGeneratedRegistry";
	private static final String DIM_WELL_CACHE_NAME = "dimensionalWellCache";
	
//	public static final String GEN_DATA_KEY = Treasure.MODID + ":generationData";
	private static final String TREASURE = Treasure.MODID;

//	private static final String KEY_TAG_NAME = "key";
//	private static final String CHEST_REGISTRY_TAG_NAME = "chestRegistry";
//	private static final String REGISTRY_TAG_NAME = "registry";
//	private static final String WELL_REGISTRIES_TAG_NAME = "wellRegistries";
//	private static final String WITHER_TREE_REGISTRIES_TAG_NAME = "witherTreeRegistries";
//	private static final String COORDS_TAG_NAME = "coords";

//	private static final String RARITY_TAG_NAME = "rarity";
//	private static final String RARITIES_TAG_NAME = "rarities";
//	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";
//	private static final String DIMENSIONS_TAG_NAME = "dimensions";
//	private static final String BIOME_ID_TAG_NAME = "biomeID";
//	private static final String BIOMES_TAG_NAME = "biomes";

	// legacy
//	private static final String CHEST_REGISTRIES_TAG_NAME = "chestRegistries";

	
	
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
			RarityLevelWeightedChestGeneratorRegistry.load(tag.getList(CHEST_GEN_REGISTRY_NAME, Tag.TAG_COMPOUND));
		}
		
        /*
         * chest registry
         */
		if (tag.contains(DIM_GEN_REGISTRY_NAME)) {
			DimensionalGeneratedRegistry.load((CompoundTag)tag.get(DIM_GEN_REGISTRY_NAME));
		}
		
		/*
		 * well cache
		 */
		if (tag.contains(DIM_WELL_CACHE_NAME)) {
			DimensionalSimpleDistanceCache.load((CompoundTag)tag.get(DIM_WELL_CACHE_NAME));
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
			updateCompound(tag, DIM_GEN_REGISTRY_NAME, DimensionalGeneratedRegistry.save());

			// update well
			updateCompound(tag, DIM_WELL_CACHE_NAME, DimensionalSimpleDistanceCache.save());
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
	 * 
	 * @param registryList
	 * @param dimension
	 * @param registry
	 */
//	private void saveRegistry(ListTag registryList, String dimension, SimpleListRegistry<ICoords> registry) {
//		CompoundTag dimensionCompound = new CompoundTag();
//		dimensionCompound.putString(DIMENSION_ID_TAG_NAME, dimension);
//		ListTag coordsList = new ListTag();
//		registry.getValues().forEach(coords -> {
//			CompoundTag coordsCompound = new CompoundTag();
//			coordsCompound.putInt("x", coords.getX());
//			coordsCompound.putInt("y", coords.getY());
//			coordsCompound.putInt("z", coords.getZ());
//			// add entry to list
//			coordsList.add(coordsCompound);
//		});
//		dimensionCompound.put(REGISTRY_TAG_NAME, coordsList);
//		registryList.add(dimensionCompound);
//	}

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