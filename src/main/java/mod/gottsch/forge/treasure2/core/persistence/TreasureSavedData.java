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

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.random.LevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.WeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext.GenType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.registries.tags.ITag;


/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class TreasureSavedData extends SavedData {	
//	private static final String TREASURE_GEN_TAG_NAME = "treasureGenerator";
	
	private static final String CHEST_GEN_REGISTRY_NAME = "weightedChestGeneratorRegistry";
	private static final String DIM_GEN_REGISTRY_NAME = "dimensionalGeneratedRegistry";
	
	public static final String GEN_DATA_KEY = Treasure.MODID + ":generationData";

	private static final String KEY_TAG_NAME = "key";
	private static final String CHEST_REGISTRY_TAG_NAME = "chestRegistry";
	private static final String REGISTRY_TAG_NAME = "registry";
	private static final String WELL_REGISTRIES_TAG_NAME = "wellRegistries";
	private static final String WITHER_TREE_REGISTRIES_TAG_NAME = "witherTreeRegistries";
	private static final String COORDS_TAG_NAME = "coords";

	private static final String RARITY_TAG_NAME = "rarity";
	private static final String RARITIES_TAG_NAME = "rarities";
	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";
	private static final String DIMENSIONS_TAG_NAME = "dimensions";
	private static final String BIOME_ID_TAG_NAME = "biomeID";
	private static final String BIOMES_TAG_NAME = "biomes";

	// legacy
	private static final String CHEST_REGISTRIES_TAG_NAME = "chestRegistries";

	
	
	public static TreasureSavedData create() {
		return new TreasureSavedData();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromTag(net.minecraft.nbt.CompoundTag)
	 */
	public static TreasureSavedData load(CompoundTag tag) {
		Treasure.LOGGER.debug("loading treasure2 saved gen data...");

		// treasure generation tag
//		CompoundTag genTag = tag.getCompound(TREASURE_GEN_TAG_NAME);
		
		/*
		 * rarities map
		 */
		ListTag rarityMapNbt = genTag.getList("rarityMap", 10);
		if (rarityMapNbt != null) {
			rarityMapNbt.forEach(entryNbt -> {
				String key = ((CompoundTag)entryNbt).getString("key");
				ListTag valueNbt = ((CompoundTag)entryNbt).getList("value", 10);
				if (valueNbt != null) {
					LevelWeightedCollection<IRarity> collection = new LevelWeightedCollection<Rarity>();
					valueNbt.forEach(rarityNbt -> {
						String rarity = ((CompoundTag)rarityNbt).getString("rarity");
						int left = ((CompoundTag)rarityNbt).getInt("left");
						int right = ((CompoundTag)rarityNbt).getInt("right");
						collection.add(Pair.of(left, right), Rarity.valueOf(rarity)); // TODO get from registry
					});	
					// replace the default rarities map
					TreasureData.RARITIES_MAP.put(WorldGenerators.valueOf(key), collection);
				}
			});
		}
		
        /*
         * chest registry
         */
		if (tag.contains(DIM_GEN_REGISTRY_NAME)) {
			DimensionalGeneratedRegistry.load(tag);
		}

//        ListTag registriesNbt = genTag.getList("chestRegistries", 10);
//        if (registriesNbt != null) {
//        	Treasure.LOGGER.debug("loading chest registries...");  	
//            registriesNbt.forEach(registryNbt -> {
//                String dimensionID = ((CompoundTag) registryNbt).getString(DIMENSION_ID_TAG_NAME);
//                Treasure.LOGGER.debug("loading dimension -> {}", dimensionID);
//
//                // check for legacy tags and load those
//                if (((CompoundTag)registryNbt).contains(CHEST_REGISTRY_TAG_NAME)) {
//                	loadChests(registryNbt, dimensionID, null);
//                	// delete tag after load
//                	((CompoundTag)registryNbt).remove(CHEST_REGISTRY_TAG_NAME);
//                }
//                else {
//                    ListTag keysNbt = ((CompoundTag) registryNbt).getList("keys", 10);
//                    keysNbt.forEach(keyNbt -> {
//                    	String key = ((CompoundTag)keyNbt).getString("name");
//                    	Treasure.LOGGER.debug("loading chest registry for -> {}", key);
//                    	if (((CompoundTag)keyNbt).contains(CHEST_REGISTRY_TAG_NAME)) {
//                    		loadChests(keyNbt, dimensionID, RegistryType.valueOf(key));
//                    	}
//                    });
//                }                 
//            });
//        }
        
        /*
         * well registries
         */
//        Treasure.LOGGER.debug("loading well registries...");
//        ListTag wellRegistryList = genTag.getList(WELL_REGISTRIES_TAG_NAME, 10);
//        if (wellRegistryList != null) {
//        	wellRegistryList.forEach(dimensionCompound -> {
//        		loadRegistry(dimensionCompound, TreasureData.WELL_REGISTRIES);
//        	});
//        }
        
        /*
         * wither tree registries
         */
//        Treasure.LOGGER.debug("loading wither tree registries...");
//        ListTag witherTreeRegistryList = genTag.getList(WITHER_TREE_REGISTRIES_TAG_NAME, 10);
//        if (witherTreeRegistryList != null) {
//        	witherTreeRegistryList.forEach(dimensionCompound -> {
//        		loadRegistry(dimensionCompound, TreasureData.WITHER_TREE_REGISTRIES);
//        	});
//        }
		
        return create();
	}

	/**
	 * 
	 * @param dimensionTag
	 * @param registryMap
	 */
//	private void loadRegistry(ITag dimensionTag, Map<String, SimpleListRegistry<ICoords>> registryMap) {
//        String dimensionID = ((CompoundTag) dimensionTag).getString(DIMENSION_ID_TAG_NAME);
//        Treasure.LOGGER.debug("\t...loading dimension -> {}", dimensionID);
//        // get the registry
//        ListTag registryList = ((CompoundTag) dimensionTag).getList(REGISTRY_TAG_NAME, 10);
//        registryList.forEach(registryTag -> {
//            int x = ((CompoundTag)registryTag).getInt("x");
//            int y = ((CompoundTag)registryTag).getInt("y");
//            int z = ((CompoundTag)registryTag).getInt("z");
//            Treasure.LOGGER.debug("\t...loading registry coords -> x:{} y:{} z:{}", x, y, z);
//            registryMap.get(dimensionID).register(new Coords(x, y, z));
//        });
//	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToTag(net.minecraft.nbt.CompoundTag)
	 */
	@Override
	public CompoundTag save(CompoundTag tag) {
		try {
			updateCompound(tag, CHEST_GEN_REGISTRY_NAME, WeightedChestGeneratorRegistry.save());

			// update tag
			updateCompound(tag, DIM_GEN_REGISTRY_NAME, DimensionalGeneratedRegistry.save());
		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.LOGGER.error("an exception occurred:", e);
		}
	
		return tag;
	}

	/**
	 * 
	 * @param coords
	 * @return
	 */
//	private CompoundTag saveCoords(ICoords coords) {
//		CompoundTag nbt = new CompoundTag();					
//		nbt.putInt("x", coords.getX());
//		nbt.putInt("y", coords.getY());
//		nbt.putInt("z", coords.getZ());
//		return nbt;
//	}
	
	/**
	 * 
	 * @param nbt
	 * @param name
	 * @return
	 */
//	private ICoords loadCoords(CompoundTag nbt, String name) {
//        CompoundTag coords = nbt.getCompound(name);
//        int x = coords.getInt("x");
//        int y = coords.getInt("y");
//        int z = coords.getInt("z");
//        return new Coords(x, y, z);
//	}

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
				TreasureSavedData::create, GEN_DATA_KEY);
		
		if (data == null) {
			data = new TreasureSavedData();
			storage.set(data);
		}
		return data;
	}
}