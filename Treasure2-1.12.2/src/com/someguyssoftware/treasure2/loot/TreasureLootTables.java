/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
public class TreasureLootTables {
	private static final String LOOT_TABLES_PATH = "/assets/treasure2/loot_tables/";
	private static final String CUSTOM_LOOT_TABLES_PATH = "loot_tables";
	
	public static final String BUILTIN_LOOT_TABLE_KEY = "BUILTIN";
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";

	public static LootContext CONTEXT;
	public static LootTable WITHER_CHEST_LOOT_TABLE;

	// TODO test loading a folder location for resources
	public static LootTableManager lootTableManager;

	/*
	 * Map of Loot Table ResourceLocations based on Rarity
	 */
//	public static final Map<Rarity, List<ResourceLocation>> CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP = new HashMap<>();
	/*
	 * Map of Loot Tables based on Rarity
	 */
//	public static final Map<Rarity, List<LootTable>> CHEST_LOOT_TABLE_MAP = new HashMap<>();

	/*
	 * Guava Table of LootTable ResourceLocations based on LootTableManager-key and Rarity 
	 */
	public static Table<String, Rarity, List<ResourceLocation>> CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();

	/*
	 * Guava Table of LootTables based on LootTableManager-key and Rarity
	 */
	public static Table<String, Rarity, List<LootTable>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	// list of special loot table locations
	private static final List<String> TABLES = ImmutableList.of(
			"chests/wither_chest"
			);

	private static final List<String> CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
			"chests/common",
			"chests/uncommon",
			"chests/scarce",
			"chests/rare",
			"chests/epic"
			);

	static {
		Path path = Paths.get("mods"); // TODO should be TreasureConfig.treasureFolder
		Treasure.logger.debug("Path to mods folder -> {}", path.toAbsolutePath().toString());
		
		// create paths to custom loot tables if the don't exist
		for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
		    final Path folder = Paths.get(path.toString(), Treasure.MODID, "loot_tables", s).toAbsolutePath();

		    if(Files.notExists(folder)){
		        Treasure.logger.debug("loot tables folder \"{}\" will be created.", folder.toString());
		        try {
					Files.createDirectories(folder);
				} catch (IOException e) {
					Treasure.logger.warn("Unable to create loot tables folder \"{}\"", folder.toString());
				}
		    }
		}
		
		// create a new loot table manager for custom file-system loot tables
		lootTableManager = new LootTableManager(new File(path.toAbsolutePath().toString()));

		// initialize the maps
		for (Rarity r : Rarity.values()) {
			// TODO remove the MAPs
//			CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.put(r, new ArrayList<ResourceLocation>());
//			CHEST_LOOT_TABLE_MAP.put(r, new ArrayList<LootTable>());
//			
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(BUILTIN_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());

			CHEST_LOOT_TABLES_TABLE.put(BUILTIN_LOOT_TABLE_KEY, r, new ArrayList<LootTable>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTable>());
		}

		Treasure.logger.debug("Registering loot tables");
		// register special tables
		for (String s : TABLES) {
			//			Treasure.logger.debug("Registering loot table -> {}", s);
			LootTableList.register(new ResourceLocation(Treasure.MODID, s));
		}

		// register built-in rarity based tables
		registerLootTables();

		// register custom rarity based tables
		registerCustomLootTables();
	}

	/**
	 * 
	 */
	private TreasureLootTables(WorldServer world) {	}

	/**
	 * 
	 * @param world
	 */
	public static void init(WorldServer world) {
		// create a context
		CONTEXT = new LootContext.Builder(world).build();

		WITHER_CHEST_LOOT_TABLE = world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(Treasure.MODID + ":chests/wither_chest"));

//		for (Entry<Rarity, List<ResourceLocation>> entry : CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.entrySet()) {
//			for (ResourceLocation loc : entry.getValue()) {
////				LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(loc);
//				/*
//				 * Does using my own manager mean we can't use resource packs to change things?
//				 * Might have to use a dual manager system. One for built-in, one for custom.
//				 * When generating randomly select built-in or custom.
//				 */
//				LootTable lootTable = lootTableManager.getLootTableFromLocation(loc);
//				CHEST_LOOT_TABLE_MAP.get(entry.getKey()).add(lootTable);
//				Treasure.logger.debug("mapping loot table: {} -> {}", entry.getKey(), loc);
//			}
//		}
		
		// for every entry in the RESOURCE_LOCATION_TABLE add to the LOOT_TABLES_TABLE
		for(Table.Cell<String, Rarity, List<ResourceLocation>> cell : CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.cellSet()) {
			for (ResourceLocation loc : cell.getValue()) {
				LootTable lootTable = null;
				if (cell.getRowKey().equalsIgnoreCase(BUILTIN_LOOT_TABLE_KEY)) {
					lootTable = world.getLootTableManager().getLootTableFromLocation(loc);
				}
				else {
					lootTable = lootTableManager.getLootTableFromLocation(loc);
				}
				CHEST_LOOT_TABLES_TABLE.get(cell.getRowKey(), cell.getColumnKey()).add(lootTable);
				Treasure.logger.debug("tabling loot table: {} {} -> {}", cell.getRowKey(), cell.getColumnKey(), loc);
			}
		}
	}

	/**
	 * Register the mod built-in loot tables
	 */
	public static void registerLootTables() {
		FileSystem fs = null;
		Stream<Path> walk = null;
		Map<String, String> env = new HashMap<>();
		URI uri = null;

		// get the asset resource folder that is unique to this mod
		URL url = Treasure.class.getResource("/assets/" + Treasure.MODID);
		if (url == null) {
			Treasure.logger.error("Unable to locate resource {}", "/assets/" + Treasure.MODID);
			return;
		}

		// convert to a uri
		try {
			uri = url.toURI();
		}
		catch(URISyntaxException e) {
			Treasure.logger.error("An error occurred during loot table processing:", e);
			return;
		}

		// split the uri into 2 parts - jar path and folder path within jar
		String[] array = uri.toString().split("!");
		try {
			fs = FileSystems.newFileSystem(URI.create(array[0]), env);
		}
		catch(IOException e) {
			Treasure.logger.error("An error occurred during loot table processing:", e);
			return;
		}

		for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			try {
				Path path = fs.getPath(LOOT_TABLES_PATH, s);
				// get all the files in the folder
				boolean isFirst = true;
				Rarity key = null;
				walk = Files.walk(path, 1);
				for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
					String tableName = it.next().getFileName().toString();
					//					Treasure.logger.debug("loot_table -> {}", s + "/" + tableName);
					// skip the first file, which is actually the given directory itself
					if (isFirst) {
						// set the key for mapping
						key = Rarity.valueOf(tableName.toUpperCase());
					}
					else {
						ResourceLocation loc = new ResourceLocation(Treasure.MODID + ":" + s + "/" + tableName.replace(".json", ""));
						// register the loot table
						LootTableList.register(loc);
						// map the loot table resource location
//						CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.get(key).add(loc);
//						Treasure.logger.debug("mapping loot table resource location: {} -> {}", key, loc);
						CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(BUILTIN_LOOT_TABLE_KEY, key).add(loc);
						Treasure.logger.debug("tabling loot table resource location: {} {} -> {}", BUILTIN_LOOT_TABLE_KEY, key, loc);
					}
					isFirst = false;
				}
			}
			catch(Exception e) {
				Treasure.logger.error("error:", e);
			}
			finally {
				// close the stream
				if (walk != null) {
					walk.close();
				}
			}			
		}

		// close the file system
		if (fs != null && fs.isOpen()) {
			try {
				fs.close();
			} catch (IOException e) {
				Treasure.logger.debug("An error occurred attempting to close the FileSystem:", e);
			}
		}
	}

	/**
	 * Register the mod external custom loot tables
	 */
	public static void registerCustomLootTables() {
		for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {

			// TODO "mods" needs to point to the config.treasureFolder
			Path path = Paths.get("mods", Treasure.MODID, CUSTOM_LOOT_TABLES_PATH, s).toAbsolutePath();
			Treasure.logger.debug("Path to custom loot table -> {}", path.toString());

			try {
				Files.walk(path)
				.filter(Files::isRegularFile)
				.forEach(f -> {
					Treasure.logger.debug("Custom loot table -> {}", f.toAbsolutePath().toString());							 
					ResourceLocation loc = new ResourceLocation(Treasure.MODID + ":" + CUSTOM_LOOT_TABLES_PATH + "/" + s + "/" + f.getFileName().toString().replace(".json", ""));
					// register the loot table
					LootTableList.register(loc);
					// map the loot table resource location
					Rarity key = Rarity.valueOf(path.getFileName().toString().toUpperCase());
//					CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.get(key).add(loc);
//					Treasure.logger.debug("mapping custom loot table resource location: {} -> {}", key, loc);					
					CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(loc);
					Treasure.logger.debug("tabling custom loot table resource location: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, loc);
				});
			} catch (IOException e) {
				Treasure.logger.error("Error processing custom loot table:", e);
			}
		}
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public static List<LootTable> getLootTableByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		Map<String, List<LootTable>> mapOfLootTables = CHEST_LOOT_TABLES_TABLE.column(rarity);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
//			Treasure.logger.debug("Adding table entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public static List<ResourceLocation> getLootTableResourceByRarity(Rarity rarity) {
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
}
