/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.chest.ChestEnvironment;
import mod.gottsch.forge.treasure2.core.chest.ChestInfo;
import mod.gottsch.forge.treasure2.core.chest.ChestInfo.GenType;
import mod.gottsch.forge.treasure2.core.data.TreasureData;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.WorldGenerators;
import mod.gottsch.forge.treasure2.core.random.LevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.RegistryType;
import mod.gottsch.forge.treasure2.core.registry.SimpleListRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class TreasureGenerationSavedData extends WorldSavedData {	
	private static final String TREASURE_GEN_TAG_NAME = "treasureGenerator";
	
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

	
	/**
	 * Empty constructor
	 */
	public TreasureGenerationSavedData() {
		super(GEN_DATA_KEY);
	}
	
	/**
	 * 
	 * @param key
	 */
	public TreasureGenerationSavedData(String key) {
		super(key);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromNBT(net.minecraft.nbt.CompoundNBT)
	 */
	@Override
	public void load(CompoundNBT tag) {
		Treasure.LOGGER.debug("loading treasure2 saved gen data...");

		// treasure generation tag
		CompoundNBT genTag = tag.getCompound(TREASURE_GEN_TAG_NAME);
		
		/*
		 * rarities map
		 */
		ListNBT rarityMapNbt = genTag.getList("rarityMap", 10);
		if (rarityMapNbt != null) {
			rarityMapNbt.forEach(entryNbt -> {
				String key = ((CompoundNBT)entryNbt).getString("key");
				ListNBT valueNbt = ((CompoundNBT)entryNbt).getList("value", 10);
				if (valueNbt != null) {
					LevelWeightedCollection<Rarity> collection = new LevelWeightedCollection<Rarity>();
					valueNbt.forEach(rarityNbt -> {
						String rarity = ((CompoundNBT)rarityNbt).getString("rarity");
						int left = ((CompoundNBT)rarityNbt).getInt("left");
						int right = ((CompoundNBT)rarityNbt).getInt("right");
						collection.add(Pair.of(left, right), Rarity.valueOf(rarity));
					});	
					// replace the default rarities map
					TreasureData.RARITIES_MAP.put(WorldGenerators.valueOf(key), collection);
				}
			});
		}
		
        /*
         * chest registry
         */
        ListNBT registriesNbt = genTag.getList("chestRegistries", 10);
        if (registriesNbt != null) {
        	Treasure.LOGGER.debug("loading chest registries...");  	
            registriesNbt.forEach(registryNbt -> {
                String dimensionID = ((CompoundNBT) registryNbt).getString(DIMENSION_ID_TAG_NAME);
                Treasure.LOGGER.debug("loading dimension -> {}", dimensionID);

                // check for legacy tags and load those
                if (((CompoundNBT)registryNbt).contains(CHEST_REGISTRY_TAG_NAME)) {
                	loadChests(registryNbt, dimensionID, null);
                	// delete tag after load
                	((CompoundNBT)registryNbt).remove(CHEST_REGISTRY_TAG_NAME);
                }
                else {
                    ListNBT keysNbt = ((CompoundNBT) registryNbt).getList("keys", 10);
                    keysNbt.forEach(keyNbt -> {
                    	String key = ((CompoundNBT)keyNbt).getString("name");
                    	Treasure.LOGGER.debug("loading chest registry for -> {}", key);
                    	if (((CompoundNBT)keyNbt).contains(CHEST_REGISTRY_TAG_NAME)) {
                    		loadChests(keyNbt, dimensionID, RegistryType.valueOf(key));
                    	}
                    });
                }                 
            });
        }
        
        /*
         * well registries
         */
        Treasure.LOGGER.debug("loading well registries...");
        ListNBT wellRegistryList = genTag.getList(WELL_REGISTRIES_TAG_NAME, 10);
        if (wellRegistryList != null) {
        	wellRegistryList.forEach(dimensionCompound -> {
        		loadRegistry(dimensionCompound, TreasureData.WELL_REGISTRIES);
        	});
        }
        
        /*
         * wither tree registries
         */
        Treasure.LOGGER.debug("loading wither tree registries...");
        ListNBT witherTreeRegistryList = genTag.getList(WITHER_TREE_REGISTRIES_TAG_NAME, 10);
        if (witherTreeRegistryList != null) {
        	witherTreeRegistryList.forEach(dimensionCompound -> {
        		loadRegistry(dimensionCompound, TreasureData.WITHER_TREE_REGISTRIES);
        	});
        }
	}

	private void loadChests(INBT registryNbt, String dimensionID, RegistryType registryType/*, List<ChestInfo> chestInfos*/) {
		ListNBT chestInfosNbt = ((CompoundNBT) registryNbt).getList(CHEST_REGISTRY_TAG_NAME, 10);
		for (INBT registryTag : chestInfosNbt) {
        	CompoundNBT nbt = (CompoundNBT)registryTag;
            String key = ((CompoundNBT)registryTag).getString(KEY_TAG_NAME);
            String rarity = ((CompoundNBT)registryTag).getString(RARITY_TAG_NAME);
            CompoundNBT coords = ((CompoundNBT)registryTag).getCompound(COORDS_TAG_NAME);
            int x = coords.getInt("x");
            int y = coords.getInt("y");
            int z = coords.getInt("z");
            Treasure.LOGGER.debug("loading chest registry entry -> k:{} r:{} x:{} y:{} z:{}", key, rarity, x, y, z);
            
            ChestInfo info = new ChestInfo(Rarity.getByValue(rarity), new Coords(x, y, z));
            
            if (nbt.contains("markers")) {
            	info.setMarkers(nbt.getBoolean("markers"));
            }
            if (nbt.contains("structure")) {
            	info.setStructure(nbt.getBoolean("structure"));
            }
            if (nbt.contains("pit")) {
            	info.setPit(nbt.getBoolean("pit"));
            }
            if (nbt.contains("discovered")) {
            	info.setDiscovered(nbt.getBoolean("discovered"));
            }
            if (nbt.contains("environment")) {
            	info.setEnvironment(ChestEnvironment.valueOf(nbt.getString("environment")));
            }
            if (nbt.contains("registryName")) {
            	info.setRegistryName(new ResourceLocation(nbt.getString("registryName")));
            }
            if (nbt.contains("mappedFrom")) {
            	info.setTreasureMapFrom(loadCoords(nbt, "mappedFrom"));
            }
            if (nbt.contains("genType")) {
            	info.setGenType(GenType.valueOf(nbt.getString("genType")));
            }            
            
            if (registryType == null) {
            	registryType = info.getEnvironment() == ChestEnvironment.SUBMERGED ? RegistryType.SUBMERGED : RegistryType.SURFACE;
            }
            Treasure.LOGGER.debug("registryType -> {}", registryType);
            Treasure.LOGGER.debug("chest registries -> {}", TreasureData.CHEST_REGISTRIES2);
            Treasure.LOGGER.debug("chest registries[{}] -> {}", dimensionID, TreasureData.CHEST_REGISTRIES2.get(dimensionID));
            Treasure.LOGGER.debug("chest registries[{}][{}] -> {}", dimensionID, registryType, TreasureData.CHEST_REGISTRIES2.get(dimensionID).get(registryType));
            TreasureData.CHEST_REGISTRIES2.get(dimensionID).get(registryType).register(info.getRarity(), info.getCoords(), info);
        }
	}

	/**
	 * 
	 * @param dimensionNBT
	 * @param registryMap
	 */
	private void loadRegistry(INBT dimensionNBT, Map<String, SimpleListRegistry<ICoords>> registryMap) {
        String dimensionID = ((CompoundNBT) dimensionNBT).getString(DIMENSION_ID_TAG_NAME);
        Treasure.LOGGER.debug("\t...loading dimension -> {}", dimensionID);
        // get the registry
        ListNBT registryList = ((CompoundNBT) dimensionNBT).getList(REGISTRY_TAG_NAME, 10);
        registryList.forEach(registryNBT -> {
            int x = ((CompoundNBT)registryNBT).getInt("x");
            int y = ((CompoundNBT)registryNBT).getInt("y");
            int z = ((CompoundNBT)registryNBT).getInt("z");
            Treasure.LOGGER.debug("\t...loading registry coords -> x:{} y:{} z:{}", x, y, z);
            registryMap.get(dimensionID).register(new Coords(x, y, z));
        });
	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToNBT(net.minecraft.nbt.CompoundNBT)
	 */
	@Override
	public CompoundNBT save(CompoundNBT tag) {
		try {
			// create a treasure compound			
			CompoundNBT genTag = new CompoundNBT();
			
			// add main treasure tag
			tag.put(TREASURE_GEN_TAG_NAME, genTag);	
			
			/*
			 * rarities map
			 */
			ListNBT rarityMapNbt = new ListNBT();
			TreasureData.RARITIES_MAP.forEach((worldGen, collection) -> {
				CompoundNBT entryNbt = new CompoundNBT();
				entryNbt.putString("key", worldGen.name());
				ListNBT raritiesNbt = new ListNBT();
				collection.getOriginal().forEach((rarity, pair) -> {
					CompoundNBT rarityNbt = new CompoundNBT();
					rarityNbt.putString("rarity", rarity.name());
					rarityNbt.putInt("left", pair.getLeft());
					rarityNbt.putInt("right", pair.getRight());
					raritiesNbt.add(rarityNbt);
				});
				entryNbt.put("value", raritiesNbt);
				rarityMapNbt.add(entryNbt);
			});
			// delete current tag
			genTag.remove("rarityMap");
			// add new values
			genTag.put("rarityMap", rarityMapNbt);
			
			/*
			 * chest registries
			 */
			ListNBT registriesNbt = new ListNBT();
			TreasureData.CHEST_REGISTRIES2.forEach((dimension, map) -> {
				CompoundNBT registryNbt = new CompoundNBT();
				registryNbt.putString(DIMENSION_ID_TAG_NAME, dimension);
				ListNBT keysNbt = new ListNBT();
				map.forEach((key, registry) -> {
					CompoundNBT keyNbt = new CompoundNBT();
					keyNbt.putString("name", key.name());
					ListNBT chestInfosNbt = new ListNBT();
					registry.getValues().forEach(chestInfo -> {
						CompoundNBT chestInfoEntry = new CompoundNBT();
						////////
						if (chestInfo.getCoords() != null) {
							CompoundNBT coords = new CompoundNBT();					
							coords.putInt("x", chestInfo.getCoords().getX());
							coords.putInt("y", chestInfo.getCoords().getY());
							coords.putInt("z", chestInfo.getCoords().getZ());
							chestInfoEntry.put(COORDS_TAG_NAME, coords);
							chestInfoEntry.putString(KEY_TAG_NAME, chestInfo.getCoords().toShortString());
						}
						chestInfoEntry.putString(RARITY_TAG_NAME, chestInfo.getRarity().getValue());
						chestInfoEntry.putBoolean("markers", chestInfo.hasMarkers());
						chestInfoEntry.putBoolean("structure", chestInfo.isStructure());
						chestInfoEntry.putBoolean("pit", chestInfo.isPit());
						chestInfoEntry.putBoolean("discovered", chestInfo.isDiscovered());
						
						if (chestInfo.getEnvironment() != null) {
							chestInfoEntry.putString("environment", chestInfo.getEnvironment().name());
						}
						if (chestInfo.getRegistryName() != null) {
							chestInfoEntry.putString("registryName", chestInfo.getRegistryName().toString());
						}
						if (chestInfo.getTreasureMapFromCoords().isPresent()) {
							CompoundNBT mappedFrom = saveCoords(chestInfo.getTreasureMapFromCoords().get());
							chestInfoEntry.put("mappedFrom", mappedFrom);
						}
						if (chestInfo.getGenType() != null) {
							chestInfoEntry.putString("genType", chestInfo.getGenType().name());
						}
						else {
							chestInfoEntry.putString("genType", GenType.CHEST.name());
						}
						
						// add entry to list
						chestInfosNbt.add(chestInfoEntry);						
						////////
					});
					keyNbt.put(CHEST_REGISTRY_TAG_NAME, chestInfosNbt);
					keysNbt.add(keyNbt);
				});
				registryNbt.put("keys", keysNbt);
				registriesNbt.add(registryNbt);
			});			
			// delete current tag
			genTag.remove("chestRegistries");
			// add new values
			genTag.put("chestRegistries", registriesNbt);

			/*
			 * well registries
			 */
			ListNBT wellRegistries = new ListNBT();
			TreasureData.WELL_REGISTRIES.entrySet().forEach(entry -> {
				saveRegistry(wellRegistries, entry.getKey(), entry.getValue());
			});
			updateCompound(genTag, WELL_REGISTRIES_TAG_NAME, wellRegistries);
			
			/*
			 * wither registries
			 */
			ListNBT witherTreeRegistries = new ListNBT();
			TreasureData.WITHER_TREE_REGISTRIES.entrySet().forEach(entry -> {
				saveRegistry(witherTreeRegistries, entry.getKey(), entry.getValue());
			});
			updateCompound(genTag, WITHER_TREE_REGISTRIES_TAG_NAME, witherTreeRegistries);			
		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.LOGGER.error("An exception occurred:", e);
		}
	
		return tag;
	}

	/**
	 * 
	 * @param coords
	 * @return
	 */
	private CompoundNBT saveCoords(ICoords coords) {
		CompoundNBT nbt = new CompoundNBT();					
		nbt.putInt("x", coords.getX());
		nbt.putInt("y", coords.getY());
		nbt.putInt("z", coords.getZ());
		return nbt;
	}
	
	/**
	 * 
	 * @param nbt
	 * @param name
	 * @return
	 */
	private ICoords loadCoords(CompoundNBT nbt, String name) {
        CompoundNBT coords = nbt.getCompound(name);
        int x = coords.getInt("x");
        int y = coords.getInt("y");
        int z = coords.getInt("z");
        return new Coords(x, y, z);
	}

	/**
	 * 
	 * @param compound
	 * @param name
	 * @param nbt
	 */
	private void updateCompound(CompoundNBT compound, String name, INBT nbt) {
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
	private void saveRegistry(ListNBT registryList, String dimension, SimpleListRegistry<ICoords> registry) {
		CompoundNBT dimensionCompound = new CompoundNBT();
		dimensionCompound.putString(DIMENSION_ID_TAG_NAME, dimension);
		ListNBT coordsList = new ListNBT();
		registry.getValues().forEach(coords -> {
			CompoundNBT coordsCompound = new CompoundNBT();
			coordsCompound.putInt("x", coords.getX());
			coordsCompound.putInt("y", coords.getY());
			coordsCompound.putInt("z", coords.getZ());
			// add entry to list
			coordsList.add(coordsCompound);
		});
		dimensionCompound.put(REGISTRY_TAG_NAME, coordsList);
		registryList.add(dimensionCompound);
	}

	/**
	 * @param world
	 * @return
	 */
	public static TreasureGenerationSavedData get(IWorld world) {
		DimensionSavedDataManager storage = ((ServerWorld)world).getDataStorage();
		TreasureGenerationSavedData data = (TreasureGenerationSavedData) storage.computeIfAbsent(TreasureGenerationSavedData::new, GEN_DATA_KEY);
		
		if (data == null) {
			data = new TreasureGenerationSavedData();
			storage.set(data);
		}
		return data;
	}
}