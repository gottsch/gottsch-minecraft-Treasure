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
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.registry.ITreasureResourceRegistry;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;

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
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "field_71310_m";

	private static LootResources lootResources;
	private static TreasureLootTableMaster2 lootTableMaster;
	private static ServerWorld world;

	static {
		lootTableMaster = new TreasureLootTableMaster2(null);
		REGISTERED_MODS = new ArrayList<>();
		LOADED_MODS = Maps.newHashMap();
	}

	private TreasureLootTableRegistry() {}

	/**
	 * Called on World load before registered mods are loaded.
	 * @param world
	 */
	public static void create(ServerWorld world) {
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
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerWorld) event.getWorld())) {
			Treasure.LOGGER.debug("loot table registry world load");
			TreasureLootTableRegistry.create((ServerWorld) event.getWorld());

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
		File lootResourcesFile = Paths.get(lootTableMaster.getWorldSaveFolder().getPath(), "data", modID, getResourceFolder(), "manifest.json").toFile();
		if (lootResourcesFile.exists()) {
			if (lootResourcesFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(lootResourcesFile, StandardCharsets.UTF_8);
					lootResources = new GsonBuilder().create().fromJson(json, LootResources.class);
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
				lootResources = ITreasureResourceRegistry.<LootResources>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID +"/" + LOOT_TABLES_FOLDER + "/manifest.json")), LootResources.class);
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

	public static void register(final String modID, LootResources lootResources) {
		lootTableMaster.registerChests(modID, lootResources.getChestResources());
		lootTableMaster.registerChestsFromWorldSave(modID, lootResources.getChestLootTableFolderLocations());
		
		lootTableMaster.registerSpecials(modID, lootResources.getSpecialResources());
		lootTableMaster.registerSpecialsFromWorldSave(modID, lootResources.getSpecialLootTableFolderLocations());
		
		lootTableMaster.registerInjects(modID, lootResources.getInjectResources());
		lootTableMaster.registerInjects(modID, lootResources.getInjectResources());
	}

	/**
	 * 
	 * @return
	 */
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