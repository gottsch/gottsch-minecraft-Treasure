/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.someguyssoftware.treasure2.Treasure;
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
	
	public static LootContext CONTEXT;
	public static LootTable WITHER_CHEST_LOOT_TABLE;

	// NOTE dont need this
	//	private static final List<String> CHESTS = ImmutableList.of(
	//			"armor_tool_chest",
	//			"food_potion_chest",
	//			"general_chest");

	/*
	 * Map of Loot Table ResourceLocations based on Rarity
	 */
	public static final Map<Rarity, List<ResourceLocation>> CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP = new HashMap<>();
	/*
	 * Map of Loot Tables based on Rarity
	 */
	public static final Map<Rarity, List<LootTable>> CHEST_LOOT_TABLE_MAP = new HashMap<>();

	// list of special loot table locations
	private static final List<String> TABLES = ImmutableList.of(
			"chests/wither_chest"
			);

	private static final List<String> CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
//			"/assets/treasure2/loot_tables/chests/common",
//			"/assets/treasure2/loot_tables/chests/uncommon",
//			"/assets/treasure2/loot_tables/chests/scarce",
//			"/assets/treasure2/loot_tables/chests/rare",
//			"/assets/treasure2/loot_tables/chests/epic"
			"chests/common",
			"chests/uncommon",
			"chests/scarce",
			"chests/rare",
			"chests/epic"
			);

	static {
		// initialize the maps
		for (Rarity r : Rarity.values()) {
			CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.put(r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLE_MAP.put(r, new ArrayList<LootTable>());
		}
		
//		Treasure.logger.debug("Registering loot tables");
		// register special tables
		for (String s : TABLES) {
//			Treasure.logger.debug("Registering loot table -> {}", s);
			LootTableList.register(new ResourceLocation(Treasure.MODID, s));
		}
		// register rarity based tables
		registerLootTables();
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

		for (Entry<Rarity, List<ResourceLocation>> entry : CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.entrySet()) {
			for (ResourceLocation loc : entry.getValue()) {
				LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(loc);
				CHEST_LOOT_TABLE_MAP.get(entry.getKey()).add(lootTable);
//				Treasure.logger.debug("mapping loot table: {} -> {}", entry.getKey(), loc);
			}
		}
	}

	/**
	 * Register the mod custom loot tables
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
						CHEST_LOOT_TABLE_RESOURCE_LOCATION_MAP.get(key).add(loc);
						Treasure.logger.debug("mapping loot table resource location: {} -> {}", key, loc);
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
}
