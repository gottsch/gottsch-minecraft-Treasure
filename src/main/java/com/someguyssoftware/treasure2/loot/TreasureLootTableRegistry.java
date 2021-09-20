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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.someguyssoftware.gottschcore.json.JSMin;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;

/**
 * Use this registry to register all your mod's custom loot table for Treasure2.
 * @author Mark Gottschling on Dec 4, 2020
 *
 */
public final class TreasureLootTableRegistry {
	public static final Logger LOGGER = LogManager.getLogger(Treasure.LOGGER.getName());

	private static final String DEFAULT_RESOURCES_LIST_PATH = "loot_tables/treasure2/default_loot_tables_list.json";	
	private static final String CUSTOM_LOOT_TABLES_RESOURCE_PATH = "/loot_tables/";		
	private static final List<String> REGISTERED_MODS = new ArrayList<>();
	private static LootResources lootResources;

	private static TreasureLootTableMaster2 lootTableMaster;

	static {
		// load master loot resources lists
		try {
			lootResources = readLootResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCES_LIST_PATH))
					);
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to expose loot tables");
		}
	}

	public synchronized static void create(IMod mod) {
		if (lootTableMaster  == null) {
			lootTableMaster = new TreasureLootTableMaster2(mod);
		}
	}

	/**
	 * Convenience wrapper
	 * @param world
	 */
	public static void initialize() {
		lootTableMaster.init();
	}

	/**
	 * Called during WorldEvent.Load event for this mod only
	 * @param modID
	 */
	public static void register() {
		register(Treasure.MODID, lootResources);
	}

	public static void register(final String modID, LootResources lootResources) {
		if (!REGISTERED_MODS.contains(modID)) {
			REGISTERED_MODS.add(modID);
			lootTableMaster.registerChests(modID, lootResources.getChestResources());
			lootTableMaster.registerSpecials(modID, lootResources.getSpecialResources());
			lootTableMaster.registerInjects(modID, lootResources.getInjectResources());
		}
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static LootResources readLootResourcesFromFromStream(InputStream inputStream) throws IOException, Exception {
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