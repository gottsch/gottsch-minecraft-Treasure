/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
public class TreasureLootTables {
	private static final String LOOT_TABLES_RESOURCE_PATH = "/assets/treasure2/loot_tables/";
	private static final String CUSTOM_LOOT_TABLES_PATH = "loot_tables";
	
	public static final String BUILTIN_LOOT_TABLE_KEY = "BUILTIN";
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";

	public static TreasureLootContext CONTEXT;
	public static TreasureLootTable WITHER_CHEST_LOOT_TABLE;
	public static TreasureLootTable SKULL_CHEST_LOOT_TABLE;

	/*
	 * customized loot table manager
	 */
	public static TreasureLootTableManager lootTableManager;

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
	public static Table<String, Rarity, List<TreasureLootTable>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	// list of special loot table locations
	private static final List<String> TABLES = ImmutableList.of(
			"chests/wither_chest",
			"chests/skull_chest"
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
			"armor",
			"food",
			"items",
			"potions",
			"tools"
			);
	
	static {
		// create paths to custom loot tables if they don't exist
		for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			createLootTableFolder(s);
		}
		for (String s : NON_CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			createLootTableFolder(s);
		}		
		
		// load/expose foreign mod loot tables
		for (String foreignModID : TreasureConfig.enableForeignModIDs) {
			if (Loader.isModLoaded(foreignModID)) {
				for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
					exposeLootTable(foreignModID, s);
				}
				for (String s : NON_CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
					exposeLootTable(foreignModID, s);
				}
			}
		}
		
		// create a new loot table manager for custom file-system loot tables
		lootTableManager = new TreasureLootTableManager(Paths.get(TreasureConfig.MODS_FOLDER).toAbsolutePath().toFile());
//		new File(path.toAbsolutePath().toString())

		// initialize the maps
		for (Rarity r : Rarity.values()) {
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(BUILTIN_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());

			CHEST_LOOT_TABLES_TABLE.put(BUILTIN_LOOT_TABLE_KEY, r, new ArrayList<TreasureLootTable>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<TreasureLootTable>());
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
		for (String foreignModID : TreasureConfig.enableForeignModIDs) {
			registerCustomLootTables(foreignModID);
		}
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
		CONTEXT = new TreasureLootContext.Builder(world).build();

		// add the special loot tables
		WITHER_CHEST_LOOT_TABLE = lootTableManager.getLootTableFromLocation(new ResourceLocation(Treasure.MODID + ":chests/wither_chest"));
		SKULL_CHEST_LOOT_TABLE = lootTableManager.getLootTableFromLocation(new ResourceLocation(Treasure.MODID + ":chests/skull_chest"));

		// for every entry in the RESOURCE_LOCATION_TABLE add to the LOOT_TABLES_TABLE
		for(Table.Cell<String, Rarity, List<ResourceLocation>> cell : CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.cellSet()) {
			for (ResourceLocation loc : cell.getValue()) {
				TreasureLootTable lootTable = null;
				// TODO don't have to separate by builtin/custom because they all use the same custom loot table manager
				if (cell.getRowKey().equalsIgnoreCase(BUILTIN_LOOT_TABLE_KEY)) {
					lootTable = lootTableManager.getLootTableFromLocation(loc);
				}
				else {
					lootTable = lootTableManager.getLootTableFromLocation(loc);
				}
				
				/*
				 *  NOTE look into doing this with enabled foreign mod's loot instead of whole new chests (just inject mod stuff into current chests)
				 *  ie. if populating a common chest, get a list of all common mod tables (items, armor, potions, food, etc) and inject into the table
				 *  https://mcforge.readthedocs.io/en/latest/items/loot_tables/
				 *  https://github.com/Vazkii/Botania/tree/e38556d265fcf43273c99ea1299a35400bf0c405/src/main/resources/assets/botania/loot_tables/inject
				 *  https://github.com/Vazkii/Botania/blob/e38556d265fcf43273c99ea1299a35400bf0c405/src/main/java/vazkii/botania/common/core/loot/LootHandler.java
				 *  LootEntry entry = new LootEntryTable(new ResourceLocation("mymod:inject/simple_dungeon"), 0, 0, null, null); // weight doesn't matter since it's the only entry in the pool. Other params set as you wish.
				 *	LootPool pool = new LootPool(new LootEntry[] {entry}, <conditions>, <rolls>, <bonusRolls>, <name>); // Other params set as you wish.
				 *	lootTable.addPool(pool);
				 *	NOTE not going to do it this way. this would introduce new pools for every foreign mod to the regular tables with there own
				 *	rolls values which could ballon the # of items found in chests.
				 */				
				
				CHEST_LOOT_TABLES_TABLE.get(cell.getRowKey(), cell.getColumnKey()).add(lootTable);
//				Treasure.logger.debug("tabling loot table: {} {} -> {}", cell.getRowKey(), cell.getColumnKey(), loc);
			}
		}
	}

	/**
	 * Register the mod built-in loot tables.
	 * This involves adding the resource locations to the static member property map and
	 * registers the resource location with the LootTableManager.
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
				Path path = fs.getPath(LOOT_TABLES_RESOURCE_PATH, s);
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
//						Treasure.logger.debug("tabling loot table resource location: {} {} -> {}", BUILTIN_LOOT_TABLE_KEY, key, loc);
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
	 * 
	 */
	public static void registerCustomLootTables() {
		registerCustomLootTables("");
	}
	
	/**
	 * Register the mod external custom loot tables
	 */
	public static void registerCustomLootTables(String subFolder) {
		for (String s : CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {

			// TODO "mods" needs to point to the config.treasureFolder
			Path path = Paths.get("mods", Treasure.MODID, CUSTOM_LOOT_TABLES_PATH, subFolder, s).toAbsolutePath();
//			Treasure.logger.debug("Path to custom loot table -> {}", path.toString());
			// check if path/folder exists
			if (Files.notExists(path)) {
				Treasure.logger.debug("Unable to locate -> {}", path.toString());
				continue;
			}
			
			try {
				Files.walk(path)
				.filter(Files::isRegularFile)
				.forEach(f -> {
//					Treasure.logger.debug("Custom loot table -> {}", f.toAbsolutePath().toString());							 
					ResourceLocation loc = new ResourceLocation(Treasure.MODID + ":" + CUSTOM_LOOT_TABLES_PATH + "/" + ((!subFolder.equals("")) ? (subFolder + "/") : "") + s + "/" + f.getFileName().toString().replace(".json", ""));
					// register the loot table
//					LootTableList.register(loc);
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
	 * @param subFolder
	 * @param location
	 */
	private static void exposeLootTable(String subFolder, String location) {
		Path folder = null;
		Stream<Path> walk = null;
		
		FileSystem fs = getResourceAsFileSystem(subFolder, location);
		if (fs == null) return;
		
		try {
			// get the base path of the resource (for foreign mod loot tables)
			Path resourceBasePath = fs.getPath("/loot_tables", subFolder, location);
//			Treasure.logger.debug("foreign mod resource base path -> {}", resourceBasePath.toString());
			
			// if resourceBasePath doesn't exist, just create the folder on the disk file system.
			if (Files.notExists(resourceBasePath)) {
				// create the file system folder if it doesn't exist
				folder = Paths.get(
						TreasureConfig.MODS_FOLDER, 
						Treasure.MODID, 
						CUSTOM_LOOT_TABLES_PATH, 
						subFolder, 
						location).toAbsolutePath();
			}
			
			boolean isFirst = true;
			// proces all the files in the folder			
			walk = Files.walk(resourceBasePath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path resourceFilePath = it.next();
//				String tableName = resourceFilePath.getFileName().toString();
//				Treasure.logger.debug("foreign mod loot_table -> {}", resourceFilePath.toString());
				// check the first file, which is actually the given directory itself
				if (isFirst) {
					// create the file system folder if it doesn't exist
					folder = Paths.get(
							TreasureConfig.MODS_FOLDER, 
							Treasure.MODID, 
							CUSTOM_LOOT_TABLES_PATH, 
							subFolder, 
							location).toAbsolutePath();
					
				    if(Files.notExists(folder)) {
				        Treasure.logger.debug("foreign mod loot tables folder \"{}\" will be created.", folder.toString());
				        Files.createDirectories(folder);
				    }
				}
				else {
					// test if file exists on the file system
					Path folderLootTablePath = Paths.get(folder.toString(),
							resourceFilePath.getFileName().toString()).toAbsolutePath();
					Treasure.logger.debug("folderLootTablePath -> {}", folderLootTablePath.toString());
					
					if(Files.notExists(folderLootTablePath)) {
						// copy from resource/classpath to file path
						InputStream is = TreasureLootTables.class.getResourceAsStream(resourceFilePath.toString());
						try (FileOutputStream fos = new FileOutputStream(folderLootTablePath.toFile())) {
							byte[] buf = new byte[2048];
							int r;
							while ((r = is.read(buf)) != -1) {
								fos.write(buf,  0,  r);
							}
						}
						catch(IOException e) {
							Treasure.logger.error("Error exposing chestsheet resource to file system.");
						}
					}
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
	 * 
	 * @param subFolder
	 * @param location
	 * @return
	 */
	private static FileSystem getResourceAsFileSystem(String subFolder, String location) {
		FileSystem fs = null;
		Map<String, String> env = new HashMap<>();
		URI uri = null;

		// get the asset resource folder that is unique to this mod
		URL url = Treasure.class.getResource("/loot_tables/" + subFolder + "/" + location);
		if (url == null) {
			Treasure.logger.error("Unable to locate resource {}", "/loot_tables/" + subFolder + "/" + location);
			return null;
		}

		// convert to a uri
		try {
			uri = url.toURI();
		}
		catch(URISyntaxException e) {
			Treasure.logger.error("An error occurred during loot table processing:", e);
			return null;
		}

		// split the uri into 2 parts - jar path and folder path within jar
		String[] array = uri.toString().split("!");
		try {
			fs = FileSystems.newFileSystem(URI.create(array[0]), env);
		}
		catch(IOException e) {
			Treasure.logger.error("An error occurred during loot table processing:", e);
			return null;
		}
		
		return fs;
	}
	
	/**
	 * 
	 * @param folder
	 */
	private static void createLootTableFolder(String location) {
		Path modsPath = Paths.get("mods"); // <-- get from config
		Path folder = Paths.get(modsPath.toString(), Treasure.MODID, CUSTOM_LOOT_TABLES_PATH, location).toAbsolutePath();
		
	    if(Files.notExists(folder)){
	        Treasure.logger.debug("loot tables folder \"{}\" will be created.", folder.toString());
	        try {
				Files.createDirectories(folder);
				
			} catch (IOException e) {
				Treasure.logger.warn("Unable to create loot tables folder \"{}\"", folder.toString());
			}
	    }
	}

	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public static List<TreasureLootTable> getLootTableByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<TreasureLootTable> tables = new ArrayList<>();
		Map<String, List<TreasureLootTable>> mapOfLootTables = CHEST_LOOT_TABLES_TABLE.column(rarity);
		// convert to a single list
		for(Entry<String, List<TreasureLootTable>> n : mapOfLootTables.entrySet()) {
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
