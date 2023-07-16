/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModList;

/**
 * Use this registry to register all your mod's custom loot table for Treasure2.
 * @author Mark Gottschling on Dec 4, 2020
 *
 */
public final class TreasureLootTableRegistry {
	public static final Logger LOGGER = LogManager.getLogger(Treasure.LOGGER.getName());

	public static final String JAR_LOOT_TABLES_ROOT = "data/treasure2/loot_tables/";
	public static final String DATAPACKS_LOOT_TABLES_ROOT = "data/treasure2/loot_tables/";

	public static final List<ILootTableType> LOOT_TABLES_GROUPS = Arrays.asList(LootTableType.CHESTS, LootTableType.WISHABLES, LootTableType.INJECTS);

//	@Deprecated
//	private static final String LOOT_TABLES_FOLDER = "loot_tables";

	/*
	 * Properties to control the loading on mods.
	 * Prevents unnecessary reloads during OnWorldLoadEvent.
	 */
	private static final List<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;

	// the gson serializer for loot tables
	private static final Gson GSON_INSTANCE = Deserializers.createLootTableSerializer().create();

	/*
	 * Master Guava Table of LootTables by Top-Level(Type) ex chests | wishables | injects, IRarity, List<LootTableShell>
	 */
	private final static Table<ILootTableType, IRarity, List<LootTableShell>> MASTER_TABLE = HashBasedTable.create();

	/*
	 * Master Map of LootTables by ResourceLocation
	 */
	private final static Map<ResourceLocation, LootTableShell> MASTER_MAP = new HashMap<>();

	// TODO will have to find ALL datapacks in world save and process each one separately
	// TODO will also have to find ALL exploded dps on the file server and process (gets priority over dp)
	// TODO will have to save ResourceLocation in shell and compare against already existing LTs and replace
	/*
	 * Master Guava Table of LootTables by Top-Level(Type) ex chests | wishables | injects, IRarity, List<LootTableShell>
	 */
	private final static Table<ILootTableType, IRarity, List<LootTableShell>> DATAPACK_TABLE = HashBasedTable.create();

	/*
	 * Master Map of LootTables by ResourceLocation
	 */
	private final static Map<ResourceLocation, LootTableShell> DATAPACK_MAP = new HashMap<>();

	/*
	 * the path to the world save folder
	 */
	private static Path worldSaveFolder;

	static {
		REGISTERED_MODS = new ArrayList<>();
		LOADED_MODS = Maps.newHashMap();
	}

	/*
	 * private constructor
	 */
	private TreasureLootTableRegistry() {}


	/**
	 * 
	 */
	public static void clearDatapacks() {
		DATAPACK_MAP.clear();
		DATAPACK_TABLE.clear();
	}

	/**
	 * 
	 */
	public static void clearAll() {
		MASTER_MAP.clear();
		MASTER_TABLE.clear();
		clearDatapacks();
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(String modID) {
		// add mod to the registered list
		REGISTERED_MODS.add(modID);

		/*
		 *  register initial resource (mod jar) supplied loot tables
		 */
		Treasure.LOGGER.debug("reading loot tables...");
		Path jarPath = ModList.get().getModFileById(modID).getFile().getFilePath();

		registerFromJar(jarPath);
	}

	/**
	 * 
	 * @param jarPath
	 */
	private static void registerFromJar(Path jarPath) {
		LOOT_TABLES_GROUPS.forEach(category -> {
			List<Path> lootTablePaths;
			try {
				// get all the paths in folder
				lootTablePaths = ModUtil.getPathsFromResourceJAR(jarPath, JAR_LOOT_TABLES_ROOT + category.getValue());

				for (Path path : lootTablePaths) {
					Treasure.LOGGER.debug("loot table path -> {}", path);
					// load the shell from the jar
					Optional<LootTableShell> shell = loadFromJar(path);
					// register
					registerLootTable(category, path, shell);
				}
			} catch (Exception e) {
				Treasure.LOGGER.warn("unable to locate file in jar -> {}", JAR_LOOT_TABLES_ROOT + category.getValue());
//				e.printStackTrace();
			}
		});		
	}

	/**
	 * 
	 * @param key
	 * @param path
	 * @param shell
	 */
	public static void registerLootTable(ILootTableType key, Path path, Optional<LootTableShell> shell) {
		if (shell.isPresent()) {
			// determine rarity TODO maybe should go the other way. path.getName(4)
			Optional<IRarity> rarity = TreasureApi.getRarity(path.getName(path.getNameCount()-2).toString().toUpperCase());
			if (rarity.isPresent()) {
				// generate a ResourceLocation
				ResourceLocation resourceLocation = asResourceLocation(path);
				Treasure.LOGGER.debug("resource location -> {}", resourceLocation);

				// add resourceLocation to shell
				shell.get().setResourceLocation(resourceLocation);

				if(!MASTER_TABLE.contains(key, rarity.get())) {
					MASTER_TABLE.put(key, rarity.get(), new ArrayList<>());
				}
				MASTER_TABLE.get(key, rarity.get()).add(shell.get());
				Treasure.LOGGER.debug("registering in table -> {} {} : {}", key, rarity.get(), resourceLocation.toString());
				MASTER_MAP.put(resourceLocation, shell.get());
				Treasure.LOGGER.debug("registering in map -> {}", resourceLocation.toString());
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param path
	 * @param shell
	 */
	public static void registerDatapacksLootTable(ILootTableType key, Path path, Optional<LootTableShell> shell) {
		if (shell.isPresent()) {
			// determine rarity TODO maybe should go the other way. path.getName(4)
			Optional<IRarity> rarity = TreasureApi.getRarity(path.getName(path.getNameCount()-2).toString().toUpperCase());
			if (rarity.isPresent()) {
				// generate a ResourceLocation
				ResourceLocation resourceLocation = asResourceLocation(path);
				Treasure.LOGGER.debug("resource location -> {}", resourceLocation);

				// add resourceLocation to shell
				shell.get().setResourceLocation(resourceLocation);

				if(!DATAPACK_TABLE.contains(key, rarity.get())) {
					DATAPACK_TABLE.put(key, rarity.get(), new ArrayList<>());
				}
				// compare resource location to all shells at this table location
				List<LootTableShell> list = DATAPACK_TABLE.get(key, rarity.get());
				Optional<LootTableShell> foundShell = list.stream().filter(lts -> lts.getResourceLocation().equals(shell.get().getResourceLocation())).findFirst();
				// remove element if it matches that new shell
				if (foundShell.isPresent()) {
					list.remove(foundShell.get());
				}
				// add the shell
				list.add(shell.get());

				Treasure.LOGGER.debug("registering datapack in table -> {} {} : {}", key, rarity.get(), resourceLocation.toString());
				DATAPACK_MAP.put(resourceLocation, shell.get());
				Treasure.LOGGER.debug("registering datapack in map -> {}", resourceLocation.toString());
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static ResourceLocation asResourceLocation(Path path) {
		Treasure.LOGGER.debug("path in -> {}", path.toString());

		// extract the namespace (moot - should always be value of Treasure.MOD_ID)
		String namespace = path.getName(1).toString();
		// get everything after loot_tables/ as the name
		String name = path.toString().replace("\\", "/");
		if (name.startsWith("/")) {
			name = name.substring(1, name.length());
		}
		name = name.substring(name.indexOf("loot_tables/") + 12).replace(".json", "");
		return new ResourceLocation(namespace, name);
	}

	/**
	 * 
	 * @param resourceFilePath
	 * @return
	 */
	public static Optional<LootTableShell> loadFromJar(Path resourceFilePath) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();
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
	 * This is not the most efficient way of doing this ie opening the zip file for each resource i want.
	 * Could open the zip file elsewhere and pass into this method.
	 * @param zipPath
	 * @param resourceFilePath
	 * @return
	 */
	public static Optional<LootTableShell> loadFromZip(Path zipPath, Path resourceFilePath) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();

		try {
			ZipFile zipFile = new ZipFile(zipPath.toFile());
			ZipEntry zipEntry = zipFile.getEntry(resourceFilePath.toString());
			InputStream stream = zipFile.getInputStream(zipEntry);
			Reader reader = new InputStreamReader(stream);
			// load the loot table
			resourceLootTable =  Optional.of(loadLootTable(reader));
			// close resources
			reader.close();
			stream.close();
			zipFile.close();
		} catch(Exception e) {
			Treasure.LOGGER.error(String.format("Couldn't load resource loot table from zip file ->  {}", resourceFilePath), e);
		}
		return resourceLootTable;
	}

	/**
	 * 
	 * @param zipFile
	 * @param resourceFilePath
	 * @return
	 */
	public static Optional<LootTableShell> loadFromZip(ZipFile zipFile, Path resourceFilePath) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();

		try {
			ZipEntry zipEntry = zipFile.getEntry(resourceFilePath.toString());
			InputStream stream = zipFile.getInputStream(zipEntry);
			Reader reader = new InputStreamReader(stream);
			// load the loot table
			resourceLootTable =  Optional.of(loadLootTable(reader));
			// close resources
			reader.close();
			stream.close();

		} catch(Exception e) {
			Treasure.LOGGER.error(String.format("Couldn't load resource loot table from zip file ->  {}", resourceFilePath), e);
		}
		return resourceLootTable;
	}

	// TODO NEW onWorldLoad() only happens in main Treasure mod
	// step through all registered loot tables and replace with file system versions if they exists
	// these should be stored in their own data space (map) which is cleared and reloaded on world load
	// these should be check first when accessing and default to the jar versions.

	/**
	 * Only load once - not per  registered mod.
	 * @param modID
	 */
	public static void loadDataPacks(String modID_xxx) {
		String worldSaveFolderPathName = getWorldSaveFolder().toString();

		// load/register exploded datapacks from world save folder
		LOOT_TABLES_GROUPS.forEach(category -> {
			List<Path> lootTablePaths;
			// build the path
			Path folderPath = Paths.get(worldSaveFolderPathName, DATAPACKS_LOOT_TABLES_ROOT, category.getValue());
			try {
				lootTablePaths = ModUtil.getPathsFromFlatDatapacks(folderPath);

				for (Path path : lootTablePaths) {
					Treasure.LOGGER.debug("loot table path -> {}", path);
					// load the shell from the jar
					Optional<LootTableShell> shell = loadFromFileSystem(path);
					// extra step - strip the beginning path from the path, so it is just data/treasure2/...
					String p = path.toString().replace(worldSaveFolderPathName, "");
					// register
					registerDatapacksLootTable(category, Paths.get(p), shell);
				}

			} catch(NoSuchFileException e) {
				// silently sallow exception
			} catch (Exception e) {
				Treasure.LOGGER.error("An error occurred attempting to register a loot table from the world save datapacks folder: ", e);
			}
		});

		/*
		 *  load/register datapacks .zip files from world save folder
		 */
		Treasure.LOGGER.debug("loading datapack files ...");
		// get all .zip files in the folder (non-recursive)
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(worldSaveFolderPathName))) {
			for (Path jarPath : stream) {
				Treasure.LOGGER.debug("path -> {}", jarPath);
				if (Files.isRegularFile(jarPath, new LinkOption[] {})) {
					if (jarPath.getFileName().toString().endsWith(".zip")) {
						// process this zip file
						Treasure.LOGGER.debug("datapack file -> {}", jarPath.toString());
						try (ZipFile zipFile = new ZipFile(jarPath.toFile())) {

							/////////////////
							LOOT_TABLES_GROUPS.forEach(category -> {
								List<Path> lootTablePaths;
								try {
									// get all the paths in folder
									lootTablePaths = ModUtil.getPathsFromResourceJAR(jarPath, JAR_LOOT_TABLES_ROOT + category.getValue());

									for (Path path : lootTablePaths) {
										Treasure.LOGGER.debug("loot table path -> {}", path);
										// load the shell from the jar
										Optional<LootTableShell> shell = loadFromZip(zipFile, path);//loadFromZip(jarPath, path);
										// register
										registerDatapacksLootTable(category, path, shell);
									}
								} catch (Exception e) {
									// minimal message
									Treasure.LOGGER.warn("warning: unable to load datapack -> {}", jarPath + "/" + JAR_LOOT_TABLES_ROOT + category.getValue());
								}
							});
						}
						///////////////////
					}
				}
			}
		} catch(Exception e) {
			Treasure.LOGGER.error("error: unable to load datapack:", e);
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Optional<LootTableShell> loadFromFileSystem(Path path) {
		try {
			Reader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8);
			Optional<LootTableShell>resourceLootTable =  Optional.of(loadLootTable(reader));
			return resourceLootTable;
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to loot table manifest");
		}	
		return Optional.empty();
	}

	/**
	 * 
	 * @param event
	 */
	public static void onWorldLoad(WorldEvent.Load event, Path worldSavePath) {
		setWorldSaveFolder(worldSavePath);
		clearDatapacks();
		if (!event.getWorld().isClientSide()) {
			loadDataPacks(Treasure.MODID);	
		}
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	protected static LootTableShell loadLootTable(String json) throws IllegalArgumentException, JsonParseException {
		return GSON_INSTANCE.fromJson(json, LootTableShell.class);
	}

	/**
	 * 
	 * @param reader
	 * @return
	 */
	protected static LootTableShell loadLootTable(Reader reader) {
		return GSON_INSTANCE.fromJson(reader, LootTableShell.class);
	}

	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
	public static List<LootTableShell> getLootTableByRarity(ILootTableType key, IRarity rarity) {
		Treasure.LOGGER.debug("table type -> {}", key);

		// get all the tables from the datapacks
		final List<LootTableShell> datapackTables = getDatapackLootTablesByTypeRarity(key, rarity);
		
		// get all loot tables by column key
		final List<LootTableShell> tables = getLootTablesByTypeRarity(key, rarity);
		
		List<LootTableShell> result = new ArrayList<>();
		// compare datapack tables to master tables. if datapack table name == master table name, use the datapack table.
		if (!tables.isEmpty() && !datapackTables.isEmpty()) {
			result = tables.stream().filter(t -> !datapackTables.stream().anyMatch(t2 -> t.getResourceLocation().equals(t2.getResourceLocation())))
            .collect(Collectors.toList());
		} else if (datapackTables.isEmpty()) {
			result.addAll(tables);
		}		
		result.addAll(datapackTables);
		
		return result;
	}
	
	/**
	 * 
	 * @param type
	 * @param rarity
	 * @return
	 */
	public static List<LootTableShell> getDatapackLootTablesByTypeRarity(ILootTableType type, IRarity rarity) {
		List<LootTableShell> datapackTables = DATAPACK_TABLE.get(type, rarity);
		if (datapackTables == null) {
			datapackTables =  new ArrayList<>();
		}
		return datapackTables;
	}
	
	public static List<LootTableShell> getLootTablesByTypeRarity(ILootTableType type, IRarity rarity) {
		List<LootTableShell> datapackTables = MASTER_TABLE.get(type, rarity);
		if (datapackTables == null) {
			datapackTables =  new ArrayList<>();
		}
		return datapackTables;
	}
	
	/**
	 * 
	 * @param key
	 * @param location
	 * @return
	 */
	public static Optional<LootTableShell> getLootTableByResourceLocation(ILootTableType key, ResourceLocation location) {
		LootTableShell lootTableShell = DATAPACK_MAP.get(location);
		if (lootTableShell == null) {
			lootTableShell = MASTER_MAP.get(location);
		}
		return Optional.ofNullable(lootTableShell);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getRegisteredMods() {
		return REGISTERED_MODS;
	}

	public static Path getWorldSaveFolder() {
		return TreasureLootTableRegistry.worldSaveFolder;
	}

	public static void setWorldSaveFolder(Path worldSaveFolder) {
		TreasureLootTableRegistry.worldSaveFolder = worldSaveFolder;
	}
	
	/**
	 * TODO why is this method in the registry? move to IChestGenerator
	 * @param lootTableShell
	 * @param defaultRarity
	 * @return
	 */
	public static IRarity getEffectiveRarity(LootTableShell lootTableShell, IRarity defaultRarity) {
		Optional<IRarity> rarity = TreasureApi.getRarity(lootTableShell.getRarity().toUpperCase());
		return rarity.isPresent() ? rarity.get() : defaultRarity;
	}
}