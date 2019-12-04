/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.loot.LootTableMaster;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

/**
 * @author Mark Gottschling on Jul 4, 2019
 *
 */
public class TreasureLootTableMaster extends LootTableMaster {
	private static final String CUSTOM_LOOT_TABLES_RESOURCE_PATH = "/loot_tables/";
	private static final String CUSTOM_LOOT_TABLES_PATH = "loot_tables";
	
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";
	
	/*
	 * 
	 */
	private final Map<SpecialLootTables, LootTable> SPECIAL_LOOT_TABLES_MAP = new HashMap<>();
	
	/*
	 * Guava Table of LootTable ResourceLocations based on LootTableManager-key and Rarity 
	 */
	private final Table<String, Rarity, List<ResourceLocation>> CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();

	/*
	 * Guava Table of LootTables based on LootTableManager-key and Rarity
	 */
	private final Table<String, Rarity, List<LootTable>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	/*
	 *  list of special loot table locations
	 */
	private static final List<String> SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
			"chests/special"
			);
	
	/*
	 * relative location of chest loot tables - in resource path or file system
	 */
	private static final List<String> CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
			"chests/common",
			"chests/uncommon",
			"chests/scarce",
			"chests/rare",
			"chests/epic"
			);
	
	/*
	 * relative location of other loot tables - in resource path or file system
	 */
	private static final List<String> NON_CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
			"treasure",
			"armor",
			"food",
			"items",
			"potions",
			"tools"
			);
	
	/**
	 * @param mod
	 * @param resourcePath
	 * @param folderName
	 */
	public TreasureLootTableMaster(IMod mod, String resourcePath, String folderName) {
		super(mod, resourcePath, folderName);
		
		if (TreasureConfig.MOD.enableDefaultLootTablesCheck) {
			buildAndExpose(Treasure.MODID);
			for (String foreignModID : TreasureConfig.FOREIGN_MODS.enableForeignModIDs) {
				if (Loader.isModLoaded(foreignModID)) {				
					buildAndExpose(foreignModID);
				}
			}
		}
		
		// initialize the maps
		for (Rarity r : Rarity.values()) {
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTable>());
		}
	}

	/**
	 * 
	 */
	public void clear() {
		super.getLootTablesMap().clear();
		super.getLootTablesResourceLocationMap().clear();
		CHEST_LOOT_TABLES_TABLE.clear();
		CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.clear();
		SPECIAL_LOOT_TABLES_MAP.clear();
	}
	
	/**
	 * 
	 * @param modID
	 */
	private void buildAndExpose(String modID) {
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, NON_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
	}
	
	/**
	 * Call in WorldEvent.Load event handler.
	 * Overide this method if you have a different cache mechanism.
	 * @param modIDIn
	 * @param location
	 */	
	public void register(String modID) {
		/*
		 *  register special loot tables
		 */
		for (String location : SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			List<ResourceLocation> specialLocations = getLootTablesResourceLocations(modID, location);

			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : specialLocations) {
				Path path = Paths.get(loc.getResourcePath());
				// create loot table
				LootTable lootTable = getLootTableManager().getLootTableFromLocation(loc);
				// add to map
				SpecialLootTables specialLootTables = SpecialLootTables.valueOf(Files.getNameWithoutExtension(path.getName(path.getNameCount()-1).toString().toUpperCase()));
				// add to map
				SPECIAL_LOOT_TABLES_MAP.put(specialLootTables, lootTable);
			}
		}
		for (String location : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getLootTablesResourceLocations(modID, location);

			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getResourcePath());
				Treasure.logger.debug("path to resource loc -> {}", path.toString());
				// map the loot table resource location
				Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
				// add to resourcemap
				CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(loc);				
				// create loot table
				LootTable lootTable = getLootTableManager().getLootTableFromLocation(loc);
				// add loot table to map
				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable);
				Treasure.logger.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, loc);
			}			
		}
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<LootTable> getLootTableByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		Map<String, List<LootTable>> mapOfLootTables = CHEST_LOOT_TABLES_TABLE.column(rarity);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			Treasure.logger.debug("Adding table entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
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
//			Treasure.logger.debug("Adding table resource location entry to loot table resource location list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
		return tables;		
	}
	
	/**
	 * 
	 * @param tableEnum
	 * @return
	 */
	public LootTable getSpecialLootTable(SpecialLootTables table) {
		LootTable lootTable = SPECIAL_LOOT_TABLES_MAP.get(table);
		return lootTable;
	}
	
	/**
	 * 
	 * @return
	 */
	public Table<String, Rarity, List<LootTable>> getChestLootTablesTable() {
		return CHEST_LOOT_TABLES_TABLE;
	}
	
	/*
	 * Enum of special loot tables (not necessarily chests)
	 */
	public enum SpecialLootTables {
		WITHER_CHEST,
		SKULL_CHEST,
		GOLD_SKULL_CHEST,
		CAULDRON_CHEST,
		CLAM_CHEST,
		OYSTER_CHEST,
		SILVER_WELL,
		GOLD_WELL,
		WHITE_PEARL_WELL,
		BLACK_PEARL_WELL
	}
}
