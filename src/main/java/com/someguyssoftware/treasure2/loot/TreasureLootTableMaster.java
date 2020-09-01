/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import java.util.Objects;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.someguyssoftware.gottschcore.json.JSMin;
import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.loot.LootTableManager;
import com.someguyssoftware.gottschcore.loot.LootTableMaster;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.meta.StructureMeta;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;


/**
 * @author Mark Gottschling on Jul 4, 2019
 *
 */
public class TreasureLootTableMaster extends LootTableMaster {
	private static final String DEFAULT_RESOURCES_LIST_PATH = "loot_tables/default_loot_tables_list.json";
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
	
	/**
	 * TODO move to the LootResources class
	 * constants used for registering (used for finding files on the file system
	 */
	
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
	
	private static LootResources lootResources;
	
	/**
	 * @param mod
	 * @param resourcePath
	 * @param folderName
	 */
	public TreasureLootTableMaster(IMod mod, String resourcePath, String folderName) {
		// NOTE resourcePath, folderName and ...FOLDER_LOCATION properties don't really matter unless they match the LootResources input paths
		// it is currently copying the file extactly as specified ie no path renames.
		// will have to expand upon buildAndExpose(String modID, List<String> resourcePaths) in order to customize the destination path
		// this would also mean updating GottschCore to use the new method of LootResources class
		super(mod, resourcePath, folderName);

		if (TreasureConfig.GENERAL.enableDefaultLootTablesCheck.get()) {
			// load master loot resources lists
			try {
				lootResources = readLootResourcesFromFromStream(
						Objects.requireNonNull(getMod().getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCES_LIST_PATH))
						);
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to expose loot tables");
			}
			
			try {
				buildAndExpose(Treasure.MODID, lootResources);
			} catch (Exception e) {
				Treasure.LOGGER.error("Unable to build and expose loot tables",e);
			}			

			// TODO 1.15.2 figure out how to check if mod is loaded
			for (String foreignModID : TreasureConfig.FOREIGN_MODS.enableForeignModIDs.get()) {
//				if (Loader.isModLoaded(foreignModID)) {	
//					buildAndExpose(foreignModID);
//				}

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
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public LootResources readLootResourcesFromFromStream(InputStream inputStream) throws IOException, Exception {
		Treasure.LOGGER.info("reading loot resource file from stream.");
		LootResources resources = null;

		// read json sheet in and minify it
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JSMin minifier = new JSMin(inputStream, out);
		// TODO add custom exceptions or handle
		minifier.jsmin();

		// out minified json into a json reader
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Reader reader = new InputStreamReader(in);
		JsonReader jsonReader = new JsonReader(reader);

		// create a gson builder
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		// read minified json into gson and generate objects
		try {
			resources= gson.fromJson(jsonReader, LootResources.class);
			Treasure.LOGGER.info("master loot resources lists -> {}", resources);
		} catch (JsonIOException | JsonSyntaxException e) {
			// TODO change to custom exception
			throw new Exception("Unable to read master loot resources file:", e);
		} finally {
			// close objects
			try {
				jsonReader.close();
			} catch (IOException e) {
				Treasure.LOGGER.warn("Unable to close JSON Reader when reading meta file.");
			}
		}
		return resources;
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
	 * @param lootResources
	 */
	private void buildAndExpose(String modID, LootResources lootResources) {
		buildAndExpose(modID, lootResources.getChestResources());
		buildAndExpose(modID, lootResources.getSupportingResources());
		buildAndExpose(modID, lootResources.getSpecialResources());
	}
	
	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	private void buildAndExpose(String modID, List<String> resourcePaths) {
		resourcePaths.forEach(resourcePath -> {
			Treasure.LOGGER.info("TreasureLootTableMaster | buildAndExpose | processing resource path -> {}", resourcePath);
			// this represents the entire file path
			Path fileSystemFilePath = Paths.get(getMod().getConfig().getConfigFolder(), getMod().getId(), resourcePath);
			Treasure.LOGGER.info("TreasureLootTableMaster | buildAndExpose | file system path -> {}", fileSystemFilePath.toString());
			try {
				// check if file already exists
				if (Files.notExists(fileSystemFilePath)) { 
					FileUtils.copyInputStreamToFile(Objects.requireNonNull(Treasure.class.getClassLoader().getResourceAsStream(resourcePath)),
							fileSystemFilePath.toFile());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * 
	 * @param modID
	 */
	@Deprecated
	private void buildAndExpose(String modID) {
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, NON_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
	}
	

	public void load(String modID) {
		// TODO Auto-generated method stub
		for (String location : SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			List<ResourceLocation> specialLocations = getLootTablesResourceLocations(modID, location);
			Treasure.LOGGER.info("TreasureLootTableMaster | load | special locations size -> {}", specialLocations.size());
			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : specialLocations) {
				Path path = Paths.get(loc.getPath());
				Treasure.LOGGER.info("TreasureLootTableMaster | load | special loot location -> {}", loc);
				getLootTableManager().load(loc);
				// TODO add to registLoot
				
			}
		}
		
		Treasure.LOGGER.info("TreasureLoootTableMaster | load | registered loot tables size -> {}", getLootTableManager().registeredLootTables.size());
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
			Treasure.LOGGER.info("special locations size -> {}", specialLocations.size());
			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : specialLocations) {
				Path path = Paths.get(loc.getPath());
				Treasure.LOGGER.info("special loot location -> {}", loc);
				// create loot table
				LootTable lootTable = getLootTableManager().getLootTableFromLocation(loc);
				// add to map
				SpecialLootTables specialLootTables = SpecialLootTables.valueOf(com.google.common.io.Files.getNameWithoutExtension(path.getName(path.getNameCount()-1).toString().toUpperCase()));
				// add to map
				SPECIAL_LOOT_TABLES_MAP.put(specialLootTables, lootTable);
				Treasure.LOGGER.info("tabling loot table: {} {} -> {}", "SPECIAL", specialLootTables, loc);
			}
		}
		for (String location : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getLootTablesResourceLocations(modID, location);

			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getPath());
				Treasure.LOGGER.info("path to resource loc -> {}", path.toString());
				// map the loot table resource location
				Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
				// add to resourcemap
				CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(loc);				
				// create loot table
				LootTable lootTable = getLootTableManager().getLootTableFromLocation(loc);
				// add loot table to map
				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable);
				Treasure.LOGGER.info("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, loc);
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
			Treasure.LOGGER.debug("Adding table entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
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
//			Treasure.LOGGER.debug("Adding table resource location entry to loot table resource location list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
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
	
	/**
	 * 
	 */
	public LootTableManager getLootTableManager() {
		return super.getLootTableManager();
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
