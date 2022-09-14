/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class TreasureMetaRegistry {
	private static final String META_FOLDER = "meta";	
	private static final TreasureMetaManager META_MANAGER;
	private static final Set<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "field_71310_m";

	private static ServerWorld world;

	static {
		META_MANAGER = new TreasureMetaManager();
		REGISTERED_MODS = Sets.newHashSet();
		LOADED_MODS = Maps.newHashMap();
	}
	
	/**
	 * 
	 */
	private TreasureMetaRegistry() {}
	
	/**
	 * 
	 * @param world
	 */
	public static void create(ServerWorld world) {
		TreasureMetaRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof SaveFormat.LevelSave) {
			Path path = ((SaveFormat.LevelSave) save).getWorldDir();
			META_MANAGER.setWorldSaveFolder(path.toFile());
		}
		
	}
	
	public static void register(String modID) {
		REGISTERED_MODS.add(modID);
	}
	
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerWorld) event.getWorld())) {
			Treasure.LOGGER.debug("meta registry world load");
			TreasureMetaRegistry.create((ServerWorld) event.getWorld());
			
			REGISTERED_MODS.forEach(mod -> {
				Treasure.LOGGER.debug("registering mod -> {}", mod);
				load(mod);
			});
		}
	}

	// 1.16.5 world save format is
	// [save]/data/[treasure2]/meta/[modid]?...
	// [save]/data/loot_tables/teasure2/chests/...
	public static void load(String modID) {
		// don't reload for session
		if (LOADED_MODS.containsKey(modID)) {
			return;
		}
		
		Manifest manifest = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File manifestFile = Paths.get(META_MANAGER.getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", modID, META_FOLDER, "manifest.json").toFile();
		if (manifestFile.exists()) {
			if (manifestFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
					manifest = new GsonBuilder().create().fromJson(json, Manifest.class);
					worldSaveMetaLoaded = true;
					Treasure.LOGGER.debug("loaded meta manifest from file system");
				}
				catch (Exception e) {
					Treasure.LOGGER.warn("Couldn't load meta manifest from {}", manifestFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in meta manifest
				manifest = ITreasureResourceRegistry.<Manifest>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID +"/" + META_FOLDER + "/manifest.json")), Manifest.class);
				Treasure.LOGGER.debug("loaded meta manifest from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to locate meta resources file");
			}
		}

		// load meta files
		if (manifest != null) {
			LOADED_MODS.put(modID, true);
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
