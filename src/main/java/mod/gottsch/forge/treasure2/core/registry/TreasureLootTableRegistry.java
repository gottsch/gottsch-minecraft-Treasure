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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.someguyssoftware.gottschcore.loot.LootTableShell;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * Use this registry to register all your mod's custom loot table for Treasure2.
 * @author Mark Gottschling on Dec 4, 2020
 *
 */
public final class TreasureLootTableRegistry {
	public static final Logger LOGGER = LogManager.getLogger(Treasure.LOGGER.getName());
	
	private static final String LOOT_TABLES_FOLDER = "loot_tables";
	private static final List<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;

//	private static final Gson GSON_INSTANCE = (new GsonBuilder())
//			.registerTypeAdapter(IntRange.class, new IntRange.Serializer()).create();
	
	private static final Gson GSON_INSTANCE = Deserializers.createLootTableSerializer().create();

	public static final String CUSTOM_LOOT_TABLE_KEY = "CUSTOM";
	/*
		MC 1.18.2: net/minecraft/server/MinecraftServer.storageSource
		Name: l => f_129744_ => storageSource
		Side: BOTH
		AT: public net.minecraft.server.MinecraftServer f_129744_ # storageSource
		Type: net/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess
	 */
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";
	
	private static LootResourceManifest lootResources;
//	private static TreasureLootTableMaster2 lootTableMaster;
	private static ServerLevel world;
	
	/////// from LTM2
	/*
	 * Guava Table of LootTableShell for Chests based on LootTableManager-key and Rarity
	 */
	private final static Table<String, IRarity, List<LootTableShell>> CHEST_LOOT_TABLES_TABLE = HashBasedTable.create();

	/*
	 * Map of LootTableShell for Chests base on ResourceLocation
	 */
	private final static Map<ResourceLocation, LootTableShell> CHEST_LOOT_TABLES_MAP = new HashMap<>();

	/*
	 * 
	 */
	private final static Map<SpecialLootTables, LootTableShell> SPECIAL_LOOT_TABLES_MAP = new HashMap<>();

	/*
	 * 
	 */
	private final static Table<String, IRarity, List<LootTableShell>> INJECT_LOOT_TABLES_TABLE = HashBasedTable.create();

	/*
	 * the path to the world save folder
	 */
	private static File worldSaveFolder;

	static {
		REGISTERED_MODS = new ArrayList<>();
		LOADED_MODS = Maps.newHashMap();
	}

	/*
	 * private constructor
	 */
	private TreasureLootTableRegistry() {}

	/**
	 * Called on World load before registered mods are loaded.
	 * TODO this could be called in the Tag load event or anything that loads prior to the world.
	 * The Tag Load event is a good option because it should only load once (not for every dimension)
	 * @param world
	 */
	public static void create(ServerLevel world) {
		TreasureLootTableRegistry.world = world;
		
		// TODO move TLTM2.init() code into here
//		lootTableMaster.init(world);
		// initialize the maps
		for (IRarity r : RarityRegistry.getValues()) {
			CHEST_LOOT_TABLES_TABLE.put(CUSTOM_LOOT_TABLE_KEY, r, new ArrayList<LootTableShell>());
		}
		
		// get path to world save folder
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save).getWorldDir().resolve("datapacks").resolve("treasure2");
			setWorldSaveFolder(path.toFile());
		}
		else {
			// TODO throw error
		}
		
		// create pack.mcmeta if not already exist
		createPack(9);
		createDataPacksFolder();
	}
	
	/**
	 * 
	 */
	protected static void createPack(int packFormat) {
		Path packPath = Paths.get(getWorldSaveFolder().getPath(), "pack.mcmeta");
		try {
			// check if file already exists
			if (Files.notExists(packPath)) { 
				// TODO in future copy file from resources
				FileUtils.write(packPath.toFile(), "{\r\n" + 
						"  \"pack\": {\r\n" + 
						"    \"pack_format\": " + packFormat + ",\r\n" + 
						"    \"description\": \"treasure2\"\r\n" + 
						"  }\r\n" + 
						"}", Charset.defaultCharset());
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("unable to create pack.mcmeta:", e);
		}
	}
	
	protected static void createDataPacksFolder() {
		Path packPath = Paths.get(getWorldSaveFolder().getPath(), "data", Treasure.MODID, "loot_tables");
		try {
			// check if file already exists
			if (Files.notExists(packPath)) { 
				Files.createDirectories(packPath);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("unable to create meta datapacks folder: ", e);
		}
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(String modID) {
		REGISTERED_MODS.add(modID);
	}

	/**
	 * 
	 * @param event
	 */
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerLevel) event.getWorld())) {
			Treasure.LOGGER.debug("loot table registry world load");
			TreasureLootTableRegistry.create((ServerLevel) event.getWorld());

			REGISTERED_MODS.forEach(mod -> {
				Treasure.LOGGER.debug("registering mod -> {}", mod);
				load(mod);
			});
		}
	}
	
	public static void load(String modID) {
		// don't reload for session
		if (LOADED_MODS.containsKey(modID)) {
			return;
		}

		LootResourceManifest lootResources = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File lootResourcesFile = Paths.get(getWorldSaveFolder().getPath(), "data", modID, getResourceFolder(), "manifest.json").toFile();
		if (lootResourcesFile.exists()) {
			if (lootResourcesFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(lootResourcesFile, StandardCharsets.UTF_8);
					lootResources = new GsonBuilder().create().fromJson(json, LootResourceManifest.class);
					worldSaveMetaLoaded = true;
					Treasure.LOGGER.debug("loaded {} loot table manifest from file system", getResourceFolder());
				}
				catch (Exception e) {
					Treasure.LOGGER.warn("Couldn't load {} loot table manifest from {}", getResourceFolder(), lootResourcesFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in loot resources
				lootResources = ITreasureResourceRegistry.<LootResourceManifest>readResourcesFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID +"/" + LOOT_TABLES_FOLDER + "/manifest.json")), LootResourceManifest.class);
				Treasure.LOGGER.debug("loaded loot table manifest from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to loot table manifest");
			}
		}

		// load loot files
		if (lootResources != null) {
			Treasure.LOGGER.warn("adding mod to loaded mods list");
			LOADED_MODS.put(modID, true);
			register(modID, lootResources);
		}
	}
	
	private static String getResourceFolder() {
		return LOOT_TABLES_FOLDER;
	}

	protected static void register(final String modID, LootResourceManifest lootResources) {
		registerChests(modID, lootResources.getChestResources());		
		registerSpecials(modID, lootResources.getSpecialResources());		
		registerInjects(modID, lootResources.getInjectResources());
	}

	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	protected static void registerChests(String modID, List<String> resourcePaths) {
		List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, resourcePaths);

		// load each ResourceLocation as LootTable and map it.
		resourceLocations.forEach(loc -> {
			LOGGER.debug("register chests -> loading loot table shell resource loc -> {}", loc.getPath().toString());
			tableChest(loc, loadLootTable(loc));
		});
	}
	
	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */	
	protected static List<ResourceLocation> getLootTablesResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */	
	protected static Optional<LootTableShell> loadLootTable(ResourceLocation resource) {
		// attempt to load from file system
		Optional<LootTableShell> shell = loadLootTableFromWorldSave(getWorldSaveFolder(), resource);
		if (!shell.isPresent()) {
			return loadLootTableFromJar(resource);
		}
		return shell;
	}
	
	/**
	 * 
	 * @param folder
	 * @param resource
	 * @return
	 */
	private static Optional<LootTableShell> loadLootTableFromWorldSave(File folder, ResourceLocation resource) {
		if (folder == null) {
			return Optional.empty();
		}
		else {
			File lootTableFile = Paths.get(folder.getPath(), "data", resource.getNamespace(), "loot_tables", resource.getPath()).toFile();
			Treasure.LOGGER.debug("attempting to load loot table {} from {}", resource, lootTableFile);

			if (lootTableFile.exists()) {
				if (lootTableFile.isFile()) {
					String json;
					try {
						json = com.google.common.io.Files.toString(lootTableFile, StandardCharsets.UTF_8);
					}
					catch (IOException e) {
						Treasure.LOGGER.warn("couldn't load loot table {} from {}", resource, lootTableFile, e);
						return Optional.empty();
					}
					try {
						return Optional.of(loadLootTable(json));
					}
					catch (IllegalArgumentException | JsonParseException e) {
						Treasure.LOGGER.error("couldn't load loot table {} from {}", resource, lootTableFile, e);
						return Optional.empty();
					}
				}
				else {
					Treasure.LOGGER.warn("expected to find loot table {} at {} but it was a folder.", resource, lootTableFile);
					return Optional.empty();
				}
			}
			else {
				Treasure.LOGGER.warn("expected to find loot table {} at {} but it doesn't exist.", resource, lootTableFile);
				return Optional.empty();
			}
		}
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	private static Optional<LootTableShell> loadLootTableFromJar(ResourceLocation resource) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();
		Path resourceFilePath = Paths.get("data", resource.getNamespace(), "loot_tables", resource.getPath() + ".json");
		Treasure.LOGGER.debug("attempting to load loot table {} from jar -> {}", resource, resourceFilePath);
		
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
	 * @param resourceLocation
	 * @param lootTable
	 */
	private static void tableChest(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation); // TODO update GottschCore.loadLootTable to set this value
			Path path = Paths.get(resourceLocation.getPath());
			// map the loot table resource location
			IRarity key = Rarity.valueOf(path.getName(path.getNameCount()-2).toString().toUpperCase());
			// add loot table to map
			List<LootTableShell> shells = CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key);
			Optional<LootTableShell> shell = shells.stream().filter(s -> s.getResourceLocation().equals(resourceLocation)).findAny();
			if (shell.isPresent()) {
				CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).remove(shell.get());
				CHEST_LOOT_TABLES_MAP.remove(resourceLocation);
			}
			CHEST_LOOT_TABLES_TABLE.get(CUSTOM_LOOT_TABLE_KEY, key).add(lootTable.get());
			LOGGER.debug("tabling loot table: {} {} -> {}", CUSTOM_LOOT_TABLE_KEY, key, resourceLocation);
			CHEST_LOOT_TABLES_MAP.put(resourceLocation, lootTable.get());
		} else {
			LOGGER.debug("unable to load loot table from -> {}", resourceLocation);
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param locations
	 */
	protected static void registerSpecials(String modID, List<String> locations) {
		List<ResourceLocation> specialLocations = getLootTablesResourceLocations(modID, locations);
		LOGGER.debug("size of special chest loot table locations -> {}", specialLocations.size());

		// load each ResourceLocation as LootTable and map it.
		specialLocations.forEach(loc -> {
			LOGGER.debug("register chests -> loading special loot table shell resource loc -> {}", loc.getPath().toString());
			tableSpecialChest(loc, loadLootTable(loc));
		});
	}
	
	/**
	 * 
	 * @param resourceLocation
	 * @param lootTable
	 */
	private static void tableSpecialChest(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation);
			Path path = Paths.get(resourceLocation.getPath());

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
		} else {
			LOGGER.debug("unable to load special loot table from -> {}", resourceLocation);
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param locations
	 */
	protected static void registerInjects(String modID, List<String> locations) {
		List<ResourceLocation> resourceLocations = getLootTablesResourceLocations(modID, locations);
		
		// load each ResourceLocation as LootTable and map it.
		resourceLocations.forEach(loc -> {
			LOGGER.debug("register injects -> loading loot table shell resource loc -> {}", loc.getPath().toString());
			tableInject(loc, loadLootTable(loc));
		});
	}
	
	/**
	 * 
	 * @param resourceLocation
	 * @param lootTable
	 */
	private static void tableInject(ResourceLocation resourceLocation, Optional<LootTableShell> lootTable) {
		if (lootTable.isPresent()) {
			// add resource location to table
			lootTable.get().setResourceLocation(resourceLocation);
			Path path = Paths.get(resourceLocation.getPath());

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
				} else {
					// initialize
					INJECT_LOOT_TABLES_TABLE.put(key, rarity, new ArrayList<LootTableShell>());
				}

				// add
				INJECT_LOOT_TABLES_TABLE.get(key, rarity).add(lootTable.get());
				LOGGER.debug("tabling inject loot table: {} {} -> {}", key, rarity, resourceLocation);
			});
		} else {
			LOGGER.debug("unable to load inject loot table from -> {}", resourceLocation);
		}
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public static List<LootTableShell> getLootTableByRarity(Rarity rarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		Map<String, List<LootTableShell>> mapOfLootTables = CHEST_LOOT_TABLES_TABLE.column(rarity);
		// convert to a single list
		for(Entry<String, List<LootTableShell>> n : mapOfLootTables.entrySet()) {
			Treasure.LOGGER.debug("adding table shell entry to loot table list -> {} {}: size {}", rarity, n.getKey(), n.getValue().size());
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	public static Optional<LootTableShell> getLootTableByResourceLocation(ResourceLocation location) {
		LootTableShell lootTableShell = CHEST_LOOT_TABLES_MAP.get(location);
		return Optional.ofNullable(lootTableShell);
	}
	
	/**
	 * 
	 * @param tableType
	 * @param rarity
	 * @return
	 */
	public static List<LootTableShell> getLootTableByRarity(ManagedTableType tableType, Rarity rarity) {
		Treasure.LOGGER.debug("managed table type -> {}", tableType);
		Table<String, IRarity, List<LootTableShell>> table = (tableType == ManagedTableType.CHEST) ? CHEST_LOOT_TABLES_TABLE : INJECT_LOOT_TABLES_TABLE;
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
	public static List<LootTableShell> getLootTableByKeyRarity(ManagedTableType tableType, String key, Rarity rarity) {
		Table<String, IRarity, List<LootTableShell>> table = (tableType == ManagedTableType.CHEST) ? CHEST_LOOT_TABLES_TABLE : INJECT_LOOT_TABLES_TABLE;
		// get all loot tables by column key
		List<LootTableShell> tables = table.get(key, rarity);
		return tables;
	}
	
	/**
	 * 
	 * @param tableEnum
	 * @return
	 */
	public static Optional<LootTableShell> getSpecialLootTable(SpecialLootTables table) {
		Treasure.LOGGER.debug("searching for special loot table --> {}", table);

		LootTableShell lootTable = SPECIAL_LOOT_TABLES_MAP.get(table);
		return lootTable == null ? Optional.empty() : Optional.of(lootTable);
	}
	
	/**
	 * TODO why is this method in the registry?
	 * @param lootTableShell
	 * @param defaultRarity
	 * @return
	 */
	public IRarity getEffectiveRarity(LootTableShell lootTableShell, IRarity defaultRarity) {
		Optional<IRarity> rarity = RarityRegistry.getRarity(lootTableShell.getRarity().toUpperCase());
		return rarity.isPresent() ? rarity.get() : defaultRarity;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getRegisteredMods() {
		return REGISTERED_MODS;
	}
	
	public static File getWorldSaveFolder() {
		return TreasureLootTableRegistry.worldSaveFolder;
	}

	public static void setWorldSaveFolder(File worldSaveFolder) {
		TreasureLootTableRegistry.worldSaveFolder = worldSaveFolder;
	}
	
	/*
	 * Enum of special loot tables (not necessarily chests)
	 */
	public interface ISpecialLootTables {}
	
	// TODO these enum values need to be registered somewhere and any references to
	// this enum should reference the registry instead.
	public enum SpecialLootTables implements ISpecialLootTables {
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
	
	public static enum ManagedTableType {
		CHEST,
		INJECT
	}
}