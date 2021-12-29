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
package com.someguyssoftware.treasure2.registry;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

/**
 * 
 * @author Mark Gottschling on Dec 16, 2021
 *
 */
public class TreasureMetaRegistry {
//	@Deprecated
//	private static final String DEFAULT_MANIFEST_PATH = "meta/treasure2/manifest.json";
//	@Deprecated
//	private static final String META_VERSION_FOLDER = "mc1_12";
	private static final String META_FOLDER = "meta";
//	@Deprecated
//	private static final String META_PATH = META_VERSION_FOLDER +"/" + META_FOLDER;
	
	private static final TreasureMetaManager META_MANAGER;
	private static final Set<String> MODS;
	private static final Map<String, Boolean> MODS_LOADED;
	
	private static WorldServer world;

	static {
		META_MANAGER = new TreasureMetaManager();
		MODS = Sets.newHashSet();
		MODS_LOADED = Maps.newHashMap();
	}
	
	/**
	 * 
	 */
//	public TreasureMetaRegistry(IMod mod) {
//		this.mod = mod;
//	}
	private TreasureMetaRegistry() {}

	/**
	 * 
	 * @param world
	 */
	public static void create(WorldServer world) {
		TreasureMetaRegistry.world = world;
		META_MANAGER.setWorldSaveFolder(Paths.get(world.getSaveHandler().getWorldDirectory().getPath()).toFile());
	}
	
	public static void register(String modID) {
		MODS.add(modID);
	}

	public static void onWorldLoad(WorldEvent.Load event) {
		if (WorldInfo.isServerSide(event.getWorld()) && event.getWorld().provider.getDimension() == 0) {
			Treasure.logger.debug("meta registry world load");
			TreasureMetaRegistry.create((WorldServer) event.getWorld());
			
			MODS.forEach(mod -> {
				Treasure.logger.debug("registering mod -> {}", mod);
				load(mod);
			});
		}
	}
	
	// 1.12.2 world save format is
	// [save]/data/meta/[modid]/...
	// [save]/data/loot_tables/teasure2/chests/...
	public static void load(String modID) {
		// don't reload for session
		if (MODS_LOADED.containsKey(modID)) {
			return;
		}
		
		Manifest manifest = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File manifestFile = Paths.get(world.getSaveHandler().getWorldDirectory().getPath(), "data", "meta", modID, "manifest.json").toFile();
		if (manifestFile.exists()) {
			if (manifestFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
					manifest = new GsonBuilder().create().fromJson(json, Manifest.class);
					worldSaveMetaLoaded = true;
					Treasure.logger.debug("loaded meta manifest from file system");
				}
				catch (Exception e) {
					Treasure.logger.warn("Couldn't load meta manifest from {}", manifestFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in meta manifest
				//Path manifestPath = Paths.get(META_FOLDER, modID, "manifest.json");
				manifest = ITreasureResourceRegistry.<Manifest>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(META_FOLDER + "/" + modID + "/manifest.json")), Manifest.class);
				Treasure.logger.debug("loaded meta manifest from jar");
			}
			catch(Exception e) {
				Treasure.logger.warn("Unable to template resources");
			}
		}

		// load meta files
		if (manifest != null) {
			MODS_LOADED.put(modID, true);
			META_MANAGER.register(modID, manifest.getResources());
		}
	}

	/**
	 * 
	 */
	public static void clear() {
		META_MANAGER.clear();
	}

	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
	public static StructureMeta get(String key) {
		return META_MANAGER.get(key);
	}

	public static TreasureMetaManager getMetaManager() {
		return META_MANAGER;
	}

}
