/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.registry.ITreasureResourceRegistry;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Use this registry to register all your mod's custom loot table for Treasure2.
 * @author Mark Gottschling on Dec 4, 2020
 *
 */
public final class TreasureLootTableRegistry {
	//	private static final List<String> registeredMods = new ArrayList<>();

	public static final Logger logger = LogManager.getLogger(Treasure.LOGGER.getName());

	private static final String LOOT_TABLES_FOLDER = "loot_tables";
	private static final List<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	private static LootResources lootResources;
	private static TreasureLootTableMaster2 lootTableMaster;
	private static WorldServer world;

	static {
		lootTableMaster = new TreasureLootTableMaster2(null);
		REGISTERED_MODS = new ArrayList<>();
		LOADED_MODS = Maps.newHashMap();
	}

	private TreasureLootTableRegistry() {}

	/**
	 * 
	 * @param mod
	 */
	@Deprecated
	public synchronized static void create(IMod mod) {
		if (lootTableMaster  == null) {
			lootTableMaster = new TreasureLootTableMaster2(mod);
		}
	}

	/**
	 * 
	 * @param world
	 */
	public static void create(WorldServer world) {
		TreasureLootTableRegistry.world = world;
		lootTableMaster.init(world);
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
		if (WorldInfo.isServerSide(event.getWorld()) && event.getWorld().provider.getDimension() == 0) {
			Treasure.LOGGER.debug("loot table registry world load");
			TreasureLootTableRegistry.create((WorldServer) event.getWorld());

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

		LootResources lootResources = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File lootResourcesFile = Paths.get(world.getSaveHandler().getWorldDirectory().getPath(), "data", getResourceFolder(), modID, "loot_tables_list.json").toFile();
		if (lootResourcesFile.exists()) {
			if (lootResourcesFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(lootResourcesFile, StandardCharsets.UTF_8);
					lootResources = new GsonBuilder().create().fromJson(json, LootResources.class);
					worldSaveMetaLoaded = true;
					Treasure.LOGGER.debug("loaded {} loot resources from file system", getResourceFolder());
				}
				catch (Exception e) {
					Treasure.LOGGER.warn("Couldn't load {} loot resources from {}", getResourceFolder(), lootResourcesFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in loot resources
				lootResources = ITreasureResourceRegistry.<LootResources>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(LOOT_TABLES_FOLDER + "/" +  modID + "/loot_tables_list.json")), LootResources.class);
				Treasure.LOGGER.debug("loaded loot resources from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to loot resources");
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

	public static void register(final String modID, LootResources lootResources) {
		lootTableMaster.registerChests(modID, lootResources.getChestResources());
		lootTableMaster.registerChestsFromWorldSave(modID, lootResources.getChestLootTableFolderLocations());

		lootTableMaster.registerSpecials(modID, lootResources.getSpecialResources());
		lootTableMaster.registerSpecialsFromWorldSave(modID, lootResources.getSpecialLootTableFolderLocations());

		lootTableMaster.registerInjects(modID, lootResources.getInjectResources());
		lootTableMaster.registerInjectsFromWorldSave(modID, lootResources.getInjectLootTableFolderLocations());
	}

	public static List<String> getRegisteredMods() {
		return REGISTERED_MODS;
	}

	public static TreasureLootTableMaster2 getLootTableMaster() {
		return lootTableMaster;
	}

	public static void setLootTableMaster(TreasureLootTableMaster2 lootTableMaster) {
		TreasureLootTableRegistry.lootTableMaster = lootTableMaster;
	}
}
