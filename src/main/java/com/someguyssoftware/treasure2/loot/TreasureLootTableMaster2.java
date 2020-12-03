/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * @author Mark Gottschling on Dec 2, 2020
 *
 */
public class TreasureLootTableMaster2 extends LootTableMaster2 {
	private static final String CUSTOM_LOOT_TABLES_RESOURCE_PATH = "/loot_tables/";
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";
	/*
	 * relative location of chest loot tables - in resource path or file system
	 */
	private static final List<String> BASE_CHEST_LOOT_TABLE_FOLDER_LOCATIONS = ImmutableList.of(
			"base/chests/common"
			);
	
	/*
	 * Guava Table of LootTable ResourceLocations based on LootTableManager-key and Rarity 
	 */
	private final Table<String, Rarity, List<ResourceLocation>> CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();
	
	/*
	 * Guava Table of LootTables based on LootTableManager-key and Rarity
	 */
	private final Table<String, Rarity, List<LootTableShell>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();
	
	/**
	 * 
	 * @param mod
	 */
	public TreasureLootTableMaster2(IMod mod) {
		super(mod);
		buildAndExpose(Treasure.MODID);
		
		// initialize the maps
		for (Rarity r : Rarity.values()) {
			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTableShell>());
		}
	}

	/**
	 * 
	 * @param modID
	 */
	private void buildAndExpose(String modID) {
		buildAndExpose(CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, BASE_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
	}
	
	/**
	 * Call in WorldEvent.Load event handler.
	 * Overide this method if you have a different cache mechanism.
	 * @param world
	 * @param modID
	 */
	public void register(String modID) {
		
		for (String location : BASE_CHEST_LOOT_TABLE_FOLDER_LOCATIONS) {
			// create the destination folders
			createWorldDataLootTableFolder(modID, location);
			// copy from config location to world data location
			moveLootTable(modID, location);

			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, location);
			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation resourceLocation : resourceLocations) {
				Path path = Paths.get(resourceLocation.getResourcePath());
				Treasure.logger.debug("path to resource loc -> {}", path.toString());
				// map the loot table resource location
				Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
				// add to resourcemap
				CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(resourceLocation);
				// create loot table
				Optional<LootTableShell> lootTable = loadLootTable(getWorldDataBaseFolder(), resourceLocation);
				if (lootTable.isPresent()) {
					// add loot table to map
					CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable.get());
					Treasure.logger.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, resourceLocation);
				}
				
				// register it with MC
				ResourceLocation newLoc = LootTableList.register(resourceLocation);
			}
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param location
	 */
	protected void moveLootTable(String modID, String location) {
		Path configFilePath = Paths.get(getMod().getConfig().getConfigFolder(), modID, LOOT_TABLES_FOLDER, location).toAbsolutePath();
		Path worldDataFilePath = Paths.get(getWorldDataBaseFolder().getParent(), LOOT_TABLES_FOLDER, modID, location).toAbsolutePath();
				
		// get all the files under the path
		Set<String> fileList = new HashSet<>();
	    try {
			Files.walkFileTree(configFilePath, new SimpleFileVisitor<Path>() {
			    @Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			      throws IOException {
			        if (!Files.isDirectory(file)) {
			            fileList.add(file.getFileName().toString());

			            Path destinationFilePath = worldDataFilePath.resolve(file.getFileName());
			            if (Files.notExists(destinationFilePath)) {
							// copy from resource/classpath to file path
			            	try {
			            		Files.copy(file, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			            	}
			            	catch(IOException e ) {
			            		GottschCore.logger.error(String.format("could not copy file %s to %s", file.toString(), destinationFilePath.toString()), e);
			            	}
						}
			            else {
			            	boolean isCurrent  = isWorldDataVersionCurrent(file, destinationFilePath);
							GottschCore.logger.error("is file system (world data) loot table current -> {}", isCurrent);
							if (!isCurrent) {
								Files.move(
										destinationFilePath, 
										Paths.get(destinationFilePath.getFileName().toString() + ".bak").toAbsolutePath(), 
										StandardCopyOption.REPLACE_EXISTING);
								Files.copy(file, destinationFilePath);
							}
			            }
			        }
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			Treasure.logger.error(String.format("an errored while file walking the location -> %s:", configFilePath), e);
			return;
		}
	}
}
