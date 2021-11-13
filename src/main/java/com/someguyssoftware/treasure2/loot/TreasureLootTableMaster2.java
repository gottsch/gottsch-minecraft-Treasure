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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.loot.LootTableMaster2;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.version.BuildVersion;
import com.someguyssoftware.gottschcore.version.VersionChecker;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.loot.RandomValueRange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/*
 * SRG names for MinecraftServer
 * 
 * field_240767_f_ -> Impl
 * field name -> field_152367_a, class -> File
 * field name -> field_213219_c, class -> WorldSettings
 * field_71310_m, class -> LevelSave
 * field_240766_e_, class -> PlayerData
 * field_240767_f_, class -> Impl
 * field_110456_c, class -> Proxy
 * field_71311_j, class -> long[]
 * field_240768_i_, class -> ServerWorldInfo
 * field_147145_h, class -> Logger
 * field_71307_n, class -> Snooper
 * field_71322_p, class -> ArrayList
 * field_240769_m_, class -> TimeTracker
 * field_71304_b, class -> EmptyProfiler
 * field_147144_o, class -> NetworkSystem
 * field_213220_d, class -> Minecraft$$Lambda$5286/1287817936
 * field_147147_p, class -> ServerStatusResponse
 * field_147146_q, class -> Random
 * field_184112_s, class -> DataFixerUpper
 * field_195576_ac = DataPackRegistries
 * field_240765_ak_ = TemplateManager
 */

/**
 * Custom Loot Table (custom data) locations:
 * MC1.12 - (Minecraft) / saves / (world) / data /  loot_tables / (modID) / 
 * MC1.13+ (Minecraft) / saves / (world) / datapacks / (modID) / data / (modID) / loot_tables /
 * @author Mark Gottschling on Dec 2, 2020
 *
 */
public class TreasureLootTableMaster2 extends LootTableMaster2 {
	public static enum ManagedTableType {
		CHEST,
		INJECT
	}

	public static Logger LOGGER = LogManager.getLogger(Treasure.LOGGER.getName());

	private static final Gson GSON_INSTANCE = (new GsonBuilder())
			.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).create();

	//	public static final String CUSTOM_LOOT_TABLES_RESOURCE_PATH = "/loot_tables/";
	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "field_71310_m";
	
	/*
	 * Guava Table of loot table ResourceLocations for Chests based on LootTableManager-key and Rarity 
	 */
	//	@Deprecated
	//	private final Table<String, Rarity, List<ResourceLocation>> CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE = HashBasedTable.create();

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

	/*
	 * 
	 */
	//	public static TreasureLootTableMaster2 instance;

	/**
	 * 
	 * @param mod
	 */
	public TreasureLootTableMaster2(IMod mod) {
		super(mod);
	}

	// TODO possibly remove or get new name
	// TEMP
	public void init(ServerWorld world) {
		Treasure.LOGGER.debug("initializing ...");
		// initialize the maps
		for (Rarity r : Rarity.values()) {
			//			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<ResourceLocation>());
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTableShell>());
		}
		
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof SaveFormat.LevelSave) {
			Path path = ((SaveFormat.LevelSave) save).getWorldDir().resolve("datapacks").resolve("treasure2");
			setWorldDataBaseFolder(path.toFile());
		}
		else {
			// TODO throw error
		}
		// create pack.mcmeta if not already exist
		createPack();
	}

	/**
	 * 
	 */
	@Override
	public void clear() {
		super.clear();
		CHEST_LOOT_TABLES_TABLE.clear();
		//		CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.clear();
		CHEST_LOOT_TABLES_MAP.clear();
		SPECIAL_LOOT_TABLES_MAP.clear();
		INJECT_LOOT_TABLES_TABLE.clear();
		INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE.clear();
	}

	/**
	 * 
	 */
	protected void createPack() {
		Path packPath = Paths.get(getWorldDataBaseFolder().getPath(), "pack.mcmeta");
		try {
			// check if file already exists
			if (Files.notExists(packPath)) { 
				// TODO in future copy file from resources
				FileUtils.write(packPath.toFile(), "{\r\n" + 
						"  \"pack\": {\r\n" + 
						"    \"pack_format\": 5,\r\n" + 
						"    \"description\": \"treasure2\"\r\n" + 
						"  }\r\n" + 
						"}", Charset.defaultCharset());
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to create pack.mcmeta:", e);
		}
	}
	
	/**
	 * TODO move this to GottschCore as this will be the defacto way to do it in 1.15+
	 * NOTE this doesn't check the versions of the resource file vs the file system file - just checks for the existance on the file system.
	 * @param modID
	 * @param resourceRelativePaths
	 */
	@Deprecated
	public void buildAndExpose(String basePath, String modID, List<String> resourceRelativePaths) {
		resourceRelativePaths.forEach(resourceRelativePath -> {
			Treasure.LOGGER.debug("TreasureLootTableMaster2 | buildAndExpose | processing relative resource path -> {}", resourceRelativePath);
			// this represents the entire file path
			Path fileSystemFilePath = Paths.get(getMod().getConfig().getConfigFolder(), getMod().getId(), "mc1_16", basePath, resourceRelativePath);
			Treasure.LOGGER.debug("TreasureLootTableMaster2 | buildAndExpose | file system path -> {}", fileSystemFilePath.toString());
			try {
				Path resourcePath = Paths.get("data", modID, basePath, resourceRelativePath);
				Treasure.LOGGER.debug("TreasureLootTableMaster2 | buildAndExpose | full resource path -> {}", resourcePath.toString());

				// TODO don't like the FileUtils.copyInputStreamToFile is duplicated in both conditions.
				// TODO don't like that resourcePath is gotten 2x as a stream - once in isFilesystemVersionCurrent and again after that.

				// check if file already exists
				if (Files.notExists(fileSystemFilePath)) { 
					FileUtils.copyInputStreamToFile(Objects.requireNonNull(Treasure.class.getClassLoader().getResourceAsStream(resourcePath.toString())),
							fileSystemFilePath.toFile());
					// TODO add a flag if a file was copied over - save it in the manager
				}
				// NOTE remember, i couldn't load the json as a resource because it had some bizarre file path and wouldn't let me load.
				else {
					boolean isCurrent = false;
					try {
						isCurrent = isFileSystemVersionCurrent(resourcePath, fileSystemFilePath);
					}
					catch(Exception e) {
						Treasure.LOGGER.warn(e.getMessage(), e);
						return;
					}
					Treasure.LOGGER.error("is file system (config) loot table current -> {}", isCurrent);
					if (!isCurrent) {
						// make a backup of the file system file
						FileUtils.copyFile(fileSystemFilePath.toFile(), Paths.get(fileSystemFilePath.toString() + ".bak").toFile());
						// copy the resource to the file system
						FileUtils.copyInputStreamToFile(Objects.requireNonNull(Treasure.class.getClassLoader().getResourceAsStream(resourcePath.toString())),
								fileSystemFilePath.toFile());
					}
				}
			} catch (Exception e) {
				Treasure.LOGGER.error("Copying loot table resources error:", e);
			}
		});
	}

	/**
	 * TODO Replace the version in GottschCore with this one.
	 */
	@Deprecated
	@Override
	protected boolean isFileSystemVersionCurrent(Path resourceFilePath, Path fileSystemFilePath) throws Exception {
		boolean result = true;
		Treasure.LOGGER.debug("Verifying the most current version for the loot table -> {} ...", fileSystemFilePath.getFileName());

		// file system loot table - can't load as a resource at this location
		String configJson;
		try {
			configJson = com.google.common.io.Files.toString(fileSystemFilePath.toFile(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			LOGGER.warn("Couldn't load config loot table from {}", fileSystemFilePath.toString(), e);
			return false;
		}
		LootTableShell fileSystemLootTable = loadLootTable(configJson);

		// jar resource loot table
		LootTableShell resourceLootTable =  null;
		try {
			InputStream resourceStream = Treasure.class.getClassLoader().getResourceAsStream(resourceFilePath.toString());
			Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8);
			resourceLootTable =  loadLootTable(reader);
		}
		catch(Exception e) {
			throw new Exception(String.format("Couldn't load resource loot table %s ", resourceFilePath), e);
		}		
		Treasure.LOGGER.debug("TreasureLootTableMaster2 | buildAndExpose | resource version -> {}", resourceLootTable.getVersion());
		Treasure.LOGGER.debug("\n\t...file system loot table -> {}\n\t...version -> {}\n\t...resource loot table -> {}\n\t...version -> {}",
				fileSystemFilePath.toString(),
				fileSystemLootTable.getVersion(),
				resourceFilePath.toString(),
				resourceLootTable.getVersion());

		// compare versions
		if (resourceLootTable != null && fileSystemLootTable != null) {
			BuildVersion resourceVersion = new BuildVersion(resourceLootTable.getVersion());
			BuildVersion fsVersion = new BuildVersion(fileSystemLootTable.getVersion());			
			result = VersionChecker.checkVersionUsingForge(resourceVersion, fsVersion);
		}
		return result;

	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Optional<LootTableShell> loadLootTable(ResourceLocation resource) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();
		Path resourceFilePath = Paths.get("data", resource.getNamespace(), "loot_tables", resource.getPath() + ".json");
		try (InputStream resourceStream = Treasure.class.getClassLoader().getResourceAsStream(resourceFilePath.toString());
				Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
			resourceLootTable =  Optional.of(loadLootTable(reader));
		}
		catch(Exception e) {
			Treasure.LOGGER.error(String.format("Couldn't load resource loot table %s ", resourceFilePath), e);
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
			LOGGER.debug("loading loot table shell resource loc -> {}, {}", getWorldDataBaseFolder().getPath(), loc.getPath().toString());
			tableChest(loc, loadLootTable(getWorldDataBaseFolder(), loc));
		});

		//		// load each ResourceLocation as LootTable and map it.
		//		for (ResourceLocation resourceLocation : resourceLocations) {
		//			Path path = Paths.get(resourceLocation.getPath());
		//			LOGGER.debug("path to resource loc -> {}", path.toString());
		//			// map the loot table resource location
		//			Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
		//			// add to resourcemap
		////			CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(resourceLocation);
		//			// create loot table
		//			LOGGER.debug("loading loot table shell resource loc -> {}",path.toString());
		//			// TODO need 2 versions of this. one builds fom jar resources, the other loads from file system.
		//			Optional<LootTableShell> lootTable = loadLootTable(resourceLocation);
		//			if (lootTable.isPresent()) {
		//				// add resource location to table
		//				lootTable.get().setResourceLocation(resourceLocation);
		//				// add loot table to map
		//				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable.get());
		//				LOGGER.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, resourceLocation);
		//				CHEST_LOOT_TABLES_MAP.put(resourceLocation, lootTable.get());
		//			}
		//			else {
		//				LOGGER.debug("unable to load loot table from -> {}", resourceLocation);
		//			}
		//		}
	}

	/**
	 * 
	 * @param modID
	 * @param resourceFolders
	 */
	public void registerChestsFromWorldSave(String modID, List<String> resourceFolders) {
		for (String folder : resourceFolders) {
			// get loot table files as ResourceLocations from the file system location (using GottschCore's version)
			List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, folder);

			// load each ResourceLocation as LootTable and map it.
			resourceLocations.forEach(loc -> {
				LOGGER.debug("loading loot table shell resource loc -> {}, {}", getWorldDataBaseFolder().getPath(), loc.getPath().toString());
				tableChest(loc, loadLootTable(getWorldDataBaseFolder(), loc));
			});
		}
	}

	private void tableChest(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation); // TODO update GottschCore.loadLootTable to set this value
			Path path = Paths.get(resourceLocation.getPath());
			// map the loot table resource location
			Rarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
			// add loot table to map
			List<LootTableShell> shells = CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key);
			Optional<LootTableShell> shell = shells.stream().filter(s -> s.getResourceLocation().equals(resourceLocation)).findAny();
			if (shell.isPresent()) {
				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).remove(shell.get());
			}
			CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable.get());
			LOGGER.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, resourceLocation);
			CHEST_LOOT_TABLES_MAP.put(resourceLocation, lootTable.get());
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
		for (ResourceLocation resourceLocation : specialLocations) {
			Path path = Paths.get(resourceLocation.getPath());
			LOGGER.debug("path to special resource loc -> {}", path.toString());
			// create loot table
			LOGGER.debug("loading loot table shell resource loc -> {}", path.toString());
			Optional<LootTableShell> lootTable = loadLootTable(resourceLocation);
			if (lootTable.isPresent()) {
				// add resource location to table
				lootTable.get().setResourceLocation(resourceLocation);
				// add to map
				SpecialLootTables specialLootTables = SpecialLootTables.valueOf(com.google.common.io.Files.getNameWithoutExtension(path.getName(path.getNameCount()-1).toString().toUpperCase()));
				LOGGER.debug("special loot tables enum -> {}", specialLootTables);
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
	}

	/**
	 * 
	 * @param modID
	 * @param locations
	 */
	public void registerInjects(String modID, List<String> locations) {
		List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, locations);
		for (ResourceLocation resourceLocation : resourceLocations) {
			Path path = Paths.get(resourceLocation.getPath());
			LOGGER.debug("path to inject resource loc -> {}", path.toString());
			// map the loot table resource location
			Rarity rarity = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
			// load loot table to get categories
			// create loot table
			LOGGER.debug("loading loot table shell resource loc -> {}", path.toString());
			Optional<LootTableShell> lootTable = loadLootTable(resourceLocation);
			if (lootTable.isPresent()) {
				// add resource location to table
				lootTable.get().setResourceLocation(resourceLocation);
				LOGGER.debug("loaded inject loot table shell -> {}", resourceLocation);
				List<String> keys = lootTable.get().getCategories();
				keys.forEach(key -> {
					LOGGER.debug("using inject key to table -> {}", key);
					key = key.isEmpty() ? "general" : key;
					if (!INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE.containsRow(key)) {
						// initialize 
						for (Rarity r : Rarity.values()) {
							INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE.put(key, r, new ArrayList<ResourceLocation>());
							INJECT_LOOT_TABLES_TABLE.put(key, r, new ArrayList<LootTableShell>());
						}
					}
					INJECT_LOOT_TABLES_RESOURCE_LOCATION_TABLE.get(key, rarity).add(resourceLocation);	
					INJECT_LOOT_TABLES_TABLE.get(key, rarity).add(lootTable.get());
					LOGGER.debug("tabling inject loot table: {} {} -> {}", key, rarity, resourceLocation);
				});
			}
			else {
				LOGGER.debug("unable to load inject loot table from -> {}", resourceLocation);
			}
		}
	}

	/**
	 * 
	 * @param modID
	 * @param location
	 */
	@Deprecated
	protected void moveLootTables(String modID, String location) {
		Path configFilePath = Paths.get(getMod().getConfig().getConfigFolder(), modID, "mc1_16", LOOT_TABLES_FOLDER, location).toAbsolutePath();
		//		Path worldDataFilePath = Paths.get(getWorldDataBaseFolder().toString(), modID, location).toAbsolutePath();
		Path worldDataFilePath = Paths.get(getWorldDataBaseFolder().toString(), "data", modID, "loot_tables", location).toAbsolutePath();

		Set<String> fileList = new HashSet<>();
		try {
			Files.walkFileTree(configFilePath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					// grab everything after loot_tables
					String destinationStr = dir.toString();			        	
					String partial = destinationStr.substring(destinationStr.indexOf(LOOT_TABLES_FOLDER) + LOOT_TABLES_FOLDER.length());
					Path destinationFilePath = Paths.get(worldDataFilePath.toString(), partial);
					LOGGER.debug("destination folder to be tested/created -> {}", destinationFilePath.toString());
					if (Files.notExists(destinationFilePath)) {
						try {
							Files.createDirectories(destinationFilePath);
						} catch (IOException e) {
							LOGGER.warn("Unable to create world data loot tables folder \"{}\"", destinationFilePath.toString());
						}
					}					
					return super.preVisitDirectory(dir, attrs);
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					LOGGER.debug("walking file -> {}", file.toString());
					fileList.add(file.getFileName().toString());
					String destinationStr = file.toString();			        	
					String partial = destinationStr.substring(destinationStr.indexOf(LOOT_TABLES_FOLDER) + LOOT_TABLES_FOLDER.length());
					Path destinationFilePath = Paths.get(worldDataFilePath.toString(), partial);
					LOGGER.debug("new destination -> {}", destinationFilePath.toString());
					if (Files.notExists(destinationFilePath)) {
						// copy from resource/classpath to file path
						try {
							Files.copy(file, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
						}
						catch(IOException e ) {
							LOGGER.error(String.format("could not copy file %s to %s", file.toString(), destinationFilePath.toString()), e);
						}
					}
					else {
						boolean isCurrent  = isWorldDataVersionCurrent(file, destinationFilePath);
						LOGGER.debug("is world data loot table {} current -> {}", destinationFilePath, isCurrent);
						if (!isCurrent) {
							Files.move(
									destinationFilePath, 
									Paths.get(destinationFilePath.getFileName().toString() + ".bak").toAbsolutePath(), 
									StandardCopyOption.REPLACE_EXISTING);
							Files.copy(file, destinationFilePath);
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			LOGGER.error(String.format("an errored while file walking the location -> %s:", configFilePath), e);
			return;
		}
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
		// convert to a single list
		for(Entry<String, List<LootTableShell>> n : mapOfLootTables.entrySet()) {
			Treasure.LOGGER.debug("Adding table shell entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
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
		Treasure.LOGGER.debug("managed table type -> {}", tableType);
		Table<String, Rarity, List<LootTableShell>> table = (tableType == ManagedTableType.CHEST) ? CHEST_LOOT_TABLES_TABLE : INJECT_LOOT_TABLES_TABLE;
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		Map<String, List<LootTableShell>> mapOfLootTables = table.column(rarity);
		// convert to a single list
		for(Entry<String, List<LootTableShell>> n : mapOfLootTables.entrySet()) {
			Treasure.LOGGER.debug("Adding table shell entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
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

	//	/**
	//	 * 
	//	 * @param rarity
	//	 * @return
	//	 */
	//	public List<ResourceLocation> getLootTableResourceByRarity(Rarity rarity) {
	//		// get all loot tables by column key
	//		List<ResourceLocation> tables = new ArrayList<>();
	//		Map<String, List<ResourceLocation>> mapOfLootTableResourceLocations = CHEST_LOOT_TABLES_RESOURCE_LOCATION_TABLE.column(rarity);
	//		// convert to a single list
	//		for(Entry<String, List<ResourceLocation>> n : mapOfLootTableResourceLocations.entrySet()) {
	//			tables.addAll(n.getValue());
	//		}
	//		return tables;		
	//	}

	/**
	 * 
	 * @param tableEnum
	 * @return
	 */
	public LootTableShell getSpecialLootTable(SpecialLootTables table) {
		Treasure.LOGGER.debug("searching for special loot table --> {}", table);

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