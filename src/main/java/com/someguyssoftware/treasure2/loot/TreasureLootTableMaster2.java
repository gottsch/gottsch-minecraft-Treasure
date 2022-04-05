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
package com.someguyssoftware.treasure2.loot;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.loot.LootTableMaster2;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

/**
 * @author Mark Gottschling on Dec 2, 2020
 *
 */
public class TreasureLootTableMaster2 extends LootTableMaster2 {
	public static enum ManagedTableType {
		CHEST,
		INJECT
	}

	public static Logger LOGGER = LogManager.getLogger(Treasure.logger.getName());

	public static final String CUSTOM_LOOT_TABLES_RESOURCE_PATH = "/loot_tables/";
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";
	
	/*
	 * Guava Table of loot table ResourceLocations for Chests based on LootTableManager-key and Rarity 
	 */
	private final Table<String, Rarity, List<ResourceLocation>> CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();

	/*
	 * Guava Table of loot table ResourceLocations for Injects based on a category-key and Rarity
	 */
	@Deprecated
	private final Table<String, Rarity, List<ResourceLocation>> INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();

	
	/*
	 * Guava Table of LootTableShell for Chests based on LootTableManager-key and Rarity
	 */
	private final Table<String, Rarity, List<LootTableShell>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	/*
	 * Map of LootTableShell for Chests base on ResourceLocation
	 */
	private final Map<ResourceLocation, LootTableShell> CHEST_LOOT_TABLES_MAP = new HashMap<>();

	/*
	 * 
	 */
	private final Map<SpecialLootTables, LootTableShell> SPECIAL_LOOT_TABLES_MAP = new HashMap<>();
	
	/*
	 * 
	 */
	private final Table<String, Rarity, List<LootTableShell>> INJECT_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	/**
	 * 
	 * @param mod
	 */
	public TreasureLootTableMaster2(IMod mod) {
		super(mod);
//		buildAndExpose(Treasure.MODID);

		// initialize the maps
		for (Rarity r : Rarity.values()) {
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTableShell>());
		}
	}

	/**
	 * 
	 */
	@Override
	public void clear() {
		super.clear();
		Treasure.logger.debug("clearing all resource tables");
		CHEST_LOOT_TABLES_TABLE.clear();
		CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.clear();
		CHEST_LOOT_TABLES_MAP.clear();
		SPECIAL_LOOT_TABLES_MAP.clear();
		INJECT_LOOT_TABLES_TABLE.clear();
		INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE.clear();
	}
	
	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */
	public List<ResourceLocation> getLootTablesResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}

	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	public void registerChests(String modID, List<String> resourcePaths) {
		List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, resourcePaths);

		// load each ResourceLocation as LootTable and map it.
		resourceLocations.forEach(loc -> {
			LOGGER.debug("register chests -> loading loot table shell resource loc -> {}", loc.getResourcePath().toString());
			tableChest(loc, loadLootTable(loc));
		});
	}

	public void registerChestsFromWorldSave(String modID, List<String> resourceFolders) {
		for (String folder : resourceFolders) {
			// get loot table files as ResourceLocations from the file system location (using GottschCore's version)
			List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, folder);

			// load each ResourceLocation as LootTable and map it.
			resourceLocations.forEach(loc -> {
				LOGGER.debug("world save -> loading loot table shell resource loc -> {}, {}", getWorldDataBaseFolder().getPath(), loc.getResourcePath().toString());
				tableChest(loc, loadLootTable(getWorldDataBaseFolder(), loc));
			});
		}

	}

	/**
	 * 
	 * @param resourceLocation
	 * @param lootTable
	 */
	private void tableChest(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		Treasure.logger.debug("is loot table present -> {}", lootTable.isPresent());
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation); // TODO update GottschCore.loadLootTable to set this value
			Path path = Paths.get(resourceLocation.getResourcePath());
			// map the loot table resource location
			Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
			// add loot table to map
			List<LootTableShell> shells = CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key);
			Optional<LootTableShell> shell = shells.stream().filter(s -> s.getResourceLocation().equals(resourceLocation)).findAny();
			if (shell.isPresent()) {
				LOGGER.debug("removing loot table shell from table -> {} {}", CUSTOM_LOOT_TABLE_KEY, key);
				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).remove(shell.get());
				CHEST_LOOT_TABLES_MAP.remove(resourceLocation);
			}
			CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable.get());
			LOGGER.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, resourceLocation);
			CHEST_LOOT_TABLES_MAP.put(resourceLocation, lootTable.get());
			Treasure.logger.debug("tables table size -> {}", CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).size());
			Treasure.logger.debug("tables map @ {} -> {}", resourceLocation, CHEST_LOOT_TABLES_MAP.get(resourceLocation));
		}
		else {
			LOGGER.debug("unable to load loot table from -> {}", resourceLocation);
		}
	}

	/**
	 * 
	 * @param modID
	 * @param locations
	 */
	public void registerSpecials(String modID, List<String> locations) {
		List<ResourceLocation> specialLocations = getLootTablesResourceLocations(modID, locations);
		LOGGER.debug("size of special chest loot table locations -> {}", specialLocations.size());

		// load each ResourceLocation as LootTable and map it.
		specialLocations.forEach(loc -> {
			LOGGER.debug("register chests -> loading special loot table shell resource loc -> {}", loc.getResourcePath().toString());
			tableSpecialChest(loc, loadLootTable(loc));
		});
	}

	/**
	 * 
	 * @param modID
	 * @param resourceFolders
	 */
	public void registerSpecialsFromWorldSave(String modID, List<String> resourceFolders) {
		for (String folder : resourceFolders) {
			List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, folder);

			resourceLocations.forEach(loc -> {
				LOGGER.debug("world save -> loading special loot table shell resource loc -> {}, {}", getWorldDataBaseFolder().getPath(), loc.getResourcePath().toString());
				tableSpecialChest(loc, loadLootTable(getWorldDataBaseFolder(), loc));
			});
		}
	}

	/**
	 * 
	 * @param resourceLocation
	 * @param lootTable
	 */
	private void tableSpecialChest(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation); // TODO update GottschCore.loadLootTable to set this value
			Path path = Paths.get(resourceLocation.getResourcePath());

			// get the key
			SpecialLootTables specialLootTables = SpecialLootTables.valueOf(com.google.common.io.Files.getNameWithoutExtension(path.getName(path.getNameCount()-1).toString().toUpperCase()));
			LOGGER.debug("special loot tables enum -> {}", specialLootTables);

			// remove if already exists
			LootTableShell shell = CHEST_LOOT_TABLES_MAP.get(resourceLocation);
			if (shell != null) {
				SPECIAL_LOOT_TABLES_MAP.remove(specialLootTables);
				CHEST_LOOT_TABLES_MAP.remove(resourceLocation);
			}

			// add to special map
			SPECIAL_LOOT_TABLES_MAP.put(specialLootTables, lootTable.get());
			LOGGER.debug("tabling special loot table: {} -> {}", specialLootTables, resourceLocation);
			// add to the resource location -> lootTableShell map
			CHEST_LOOT_TABLES_MAP.put(resourceLocation, lootTable.get());
		}
		else {
			LOGGER.debug("unable to load special loot table from -> {}", resourceLocation);
		}
	}

	/**
	 * 
	 * @param modID
	 * @param locations
	 */
	public void registerInjects(String modID, List<String> locations) {
		List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, locations);

		// load each ResourceLocation as LootTable and map it.
		resourceLocations.forEach(loc -> {
			LOGGER.debug("register injects -> loading loot table shell resource loc -> {}", loc.getResourcePath().toString());
			tableInject(loc, loadLootTable(loc));
		});
	}
	
	public void registerInjectsFromWorldSave(String modID, List<String> resourceFolders) {
		for (String folder : resourceFolders) {
			List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, folder);
			resourceLocations.forEach(loc -> {
				LOGGER.debug("world save -> loading inject loot table shell resource loc -> {}, {}", getWorldDataBaseFolder().getPath(), loc.getResourcePath().toString());
				tableInject(loc, loadLootTable(getWorldDataBaseFolder(), loc));
			});
		}
	}

	/**
	 * 
	 * @param resourceLocation
	 * @param lootTable
	 */
	private void tableInject(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation); // TODO update GottschCore.loadLootTable to set this value
			Path path = Paths.get(resourceLocation.getResourcePath());		
			Rarity rarity = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());

			// get the key(s)
			List<String> keys = lootTable.get().getCategories();

			keys.forEach(key -> {
				LOGGER.debug("using inject key to table -> {}", key);
				key = key.isEmpty() ? "general" : key;
				// test if the resource location is already tabled
				List<LootTableShell> shells = INJECT_LOOT_TABLES_TABLE.get(key, rarity);
				if (shells != null) {
					Optional<LootTableShell> shell = shells.stream().filter(s -> s.getResourceLocation().equals(resourceLocation)).findAny();
					if (shell.isPresent()) {
						INJECT_LOOT_TABLES_TABLE.get(key, rarity).remove(shell.get());
					}
				}
				else {
					// initialize
					INJECT_LOOT_TABLES_TABLE.put(key, rarity, new ArrayList<LootTableShell>());
				}

				// add
				INJECT_LOOT_TABLES_TABLE.get(key, rarity).add(lootTable.get());
				LOGGER.debug("tabling inject loot table: {} {} -> {}", key, rarity, resourceLocation);
			});

		}
		else {
			LOGGER.debug("unable to load inject loot table from -> {}", resourceLocation);
		}
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Optional<LootTableShell> loadLootTable(ResourceLocation resource) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();
		// NOTE for some reason, using Paths.toString isn't working with 1.12.2? Doesn't make sense 
		// Path resourceFilePath = Paths.get("data", resource.getResourceDomain(), "loot_tables", resource.getResourcePath() + ".json");
		String relativePath = "assets/" + resource.getResourceDomain() + "/loot_tables/" + resource.getResourcePath() + ".json";
		Treasure.logger.debug("loot table relative path -> {}", relativePath);
		try (InputStream resourceStream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(relativePath);
				Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
			resourceLootTable =  Optional.of(loadLootTable(reader));
		}
		catch(Exception e) {
			Treasure.logger.error(String.format("Couldn't load resource loot table %s ", relativePath), e);
		}		
		return resourceLootTable;
	}

	/**
	 * 
	 * @param reader
	 * @return
	 */
	public LootTableShell loadLootTable(Reader reader) {
		return GSON_INSTANCE.fromJson(reader, LootTableShell.class);
	}

	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> getLootTableByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		Map<String, List<LootTableShell>> mapOfLootTables = CHEST_LOOT_TABLES_TABLE.column(rarity);
		Treasure.logger.debug("searching for {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, rarity, CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, rarity));
		// convert to a single list
		for(Entry<String, List<LootTableShell>> n : mapOfLootTables.entrySet()) {
			Treasure.logger.debug("Adding table shell entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	public Optional<LootTableShell> getLootTableByResourceLocation(ResourceLocation location) {
		LootTableShell lootTableShell = CHEST_LOOT_TABLES_MAP.get(location);
		return Optional.ofNullable(lootTableShell);
	}
	
	/**
	 * 
	 * @param tableType
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> getLootTableByRarity(ManagedTableType tableType, Rarity rarity) {
		Treasure.logger.debug("managed table type -> {}", tableType);
		Table<String, Rarity, List<LootTableShell>> table = (tableType == ManagedTableType.CHEST) ? CHEST_LOOT_TABLES_TABLE : INJECT_LOOT_TABLES_TABLE;
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		Map<String, List<LootTableShell>> mapOfLootTables = table.column(rarity);
		// convert to a single list
		for(Entry<String, List<LootTableShell>> n : mapOfLootTables.entrySet()) {
			Treasure.logger.debug("Adding table shell entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * 
	 * @param tableType
	 * @param key
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> getLootTableByKeyRarity(ManagedTableType tableType, String key, Rarity rarity) {
		Table<String, Rarity, List<LootTableShell>> table = (tableType == ManagedTableType.CHEST) ? CHEST_LOOT_TABLES_TABLE : INJECT_LOOT_TABLES_TABLE;
		// get all loot tables by column key
		List<LootTableShell> tables = table.get(key, rarity);
		return tables;
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<ResourceLocation> getLootTableResourceByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<ResourceLocation> tables = new ArrayList<>();
		Map<String, List<ResourceLocation>> mapOfLootTableResourceLocations = CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.column(rarity);
		// convert to a single list
		for(Entry<String, List<ResourceLocation>> n : mapOfLootTableResourceLocations.entrySet()) {
			tables.addAll(n.getValue());
		}
		return tables;		
	}
	
	/**
	 * 
	 * @param tableEnum
	 * @return
	 */
	public LootTableShell getSpecialLootTable(SpecialLootTables table) {
		Treasure.logger.debug("searching for special loot table --> {}", table);
		
		LootTableShell lootTable = SPECIAL_LOOT_TABLES_MAP.get(table);
		return lootTable;
	}
	
	/**
	 * 
	 * @param lootTableShell
	 * @param defaultRarity
	 * @return
	 */
	public Rarity getEffectiveRarity(LootTableShell lootTableShell, Rarity defaultRarity) {
		return !StringUtils.isNullOrEmpty(lootTableShell.getRarity()) ? Rarity.getByValue(lootTableShell.getRarity().toLowerCase()) : defaultRarity;
	}
	
	/*
	 * Enum of special loot tables (not necessarily chests)
	 */
	public enum SpecialLootTables {
		WITHER_CHEST,
		SKULL_CHEST,
		GOLD_SKULL_CHEST,
		CRYSTAL_SKULL_CHEST,
		CAULDRON_CHEST,
		CLAM_CHEST,
		OYSTER_CHEST,
		SILVER_WELL,
		GOLD_WELL,
		WHITE_PEARL_WELL,
		BLACK_PEARL_WELL;
		
		/**
		 * 
		 * @return
		 */
		public static List<String> getNames() {
			List<String> names = EnumSet.allOf(SpecialLootTables.class).stream().map(x -> x.name()).collect(Collectors.toList());
			return names;
		}		
	}
}
