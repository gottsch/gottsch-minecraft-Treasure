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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.meta.MetaManager;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.MetaManifest;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;

import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Dec 16, 2021
 *
 */
public class TreasureMetaRegistry {
	private static final String DEFAULT_MANIFEST_PATH = "meta/treasure2/manifest.json"; // "meta/manifest.json" as already will be in the treasure2 namespace
	@Deprecated
	private static final String META_VERSION_FOLDER = "mc1_12";
	private static final String META_FOLDER = "meta";
	@Deprecated
	private static final String META_PATH = META_VERSION_FOLDER +"/" + META_FOLDER;
	private static TreasureMetaManager metaManager;
	private static MetaManifest metaManifest;
	private static File worldDataBaseFolder;
	
	static {
		// load template manifest
		try {
			// TODO should look in world save for a meta manifest file first and use it? instead
			// of searching for individual meta docs? that way you could remove metas if you wanted.
			// ie. metas aren't required for mod to work, whereas default loot tables is required.
			// TODO this should be exectue on register, not static
			metaManifest = ITreasureResourceRegistry.<MetaManifest>readResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_MANIFEST_PATH)), MetaManifest.class);
		}
		catch(Exception e) {
			Treasure.logger.warn("Unable to template resources");
		}
	}

	/**
	 * 
	 */
	private TreasureMetaRegistry() {}
	
	/**
	 * TODO does this even need to exist as separate method
	 * @param mod
	 */
	public synchronized static void create(IMod mod) {
		if (metaManager  == null) {
			metaManager = new TreasureMetaManager(mod, META_PATH);
		}
	}
	
	/**
	 * 
	 * @param mod
	 * @param folder
	 */
	public synchronized static void create(IMod mod, File folder) {
		create(mod);
		worldDataBaseFolder = folder;
	}
	
	/**
	 * TODO this strategy only grabs the listed resources from config folder. need to grab all of them so users can add their own
	 * TODO may not want to put in config, but rather in world save so that they can override and/or add
	 * Wrapper method
	 * @param modID
	 */
	public static void register(final String modID) {
		buildAndExpose(modID);
		metaManager.register(modID, metaManifest.getResources());
	}
	
	// 1.12.2 world save format is
	// [save]/data/meta/[modid]/...
	// [save]/data/loot_tables/teasure2/chests/...
	public static void register2(final String modID) {
		boolean worldSaveMetaLoaded = false;
		if (getWorldDataBaseFolder() != null) {
			// create folders if not exist
			createMetaFolder(getWorldDataBaseFolder(), modID);
			
			// read from file location
			File manifestFile = Paths.get(getWorldDataBaseFolder().getPath(), DEFAULT_MANIFEST_PATH).toFile();
			if (manifestFile.exists()) {
				if (manifestFile.isFile()) {
					String json;
					try {
						json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
						metaManifest = new GsonBuilder().create().fromJson(json, MetaManifest.class);
						worldSaveMetaLoaded = true;
					}
					catch (Exception e) {
						Treasure.logger.warn("Couldn't load meta manifest from {}", manifestFile, e);
					}
				}
			}
		}
		
		// load default built-in meta manifest
		if (!worldSaveMetaLoaded) {
			try {
				metaManifest = ITreasureResourceRegistry.<MetaManifest>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_MANIFEST_PATH)), MetaManifest.class);
			}
			catch(Exception e) {
				Treasure.logger.warn("Unable to template resources");
			}
		}
		
		// load meta files
		metaManager.register(modID, metaManifest.getResources());
	}
	
	/**
	 * 
	 * @param modID
	 * @param location
	 */
	private void createMetaFolder(File file, String modID) {

		/*
		 *  build a path to the specified location
		 *  ie ../[WORLD SAVE]/data/meta/[MODID]/structures
		 */
		// TODO need world save folder here
//		Path configPath = Paths.get(getMod().getConfig().getConfigFolder());
		Path folder = Paths.get(file.getPath(), "data/meta", modID, "structures").toAbsolutePath();

		if (Files.notExists(folder)) {
			Treasure.logger.debug("meta folder \"{}\" will be created.", folder.toString());
			try {
				Files.createDirectories(folder);

			} catch (IOException e) {
				Treasure.logger.warn("Unable to create meta folder \"{}\"", folder.toString());
			}
		}
	}

	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
	public static StructureMeta get(String key) {
		return metaManager.get(key);
	}
	
	public static TreasureMetaManager getMetaManager() {
		return metaManager;
	}
	
	public static File getWorldDataBaseFolder() {
		return worldDataBaseFolder;
	}
}
