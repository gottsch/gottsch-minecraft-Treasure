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
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TreasureTemplateRegistry {
	private static final String DEFAULT_MANIFEST_PATH = "structures/manifest.json";	
	private static final String TEMPLATES_FOLDER = "structures";
	
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "field_71310_m";
	private static final TreasureTemplateManager TEMPLATE_MANAGER;
	private static final Set<String> MODS;
	private static final Map<String, Boolean> MODS_LOADED;
	private static ServerWorld world;
	
	static {
		TEMPLATE_MANAGER = new TreasureTemplateManager(Treasure.instance, "/structures",
				DataFixesManager.getDataFixer());
		MODS = Sets.newHashSet();
		MODS_LOADED = Maps.newHashMap();
	}
	
	/**
	 * 
	 */
	private TreasureTemplateRegistry() {}
	
	/**
	 * 
	 * @param world
	 */
	public static void create(ServerWorld world) {
		TreasureTemplateRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof SaveFormat.LevelSave) {
			Path path = ((SaveFormat.LevelSave) save).getWorldDir();
		TEMPLATE_MANAGER.setWorldSaveFolder(path.toFile());
		}
	}
	
	/**
	 * 
	 */
	public void clear() {
		TEMPLATE_MANAGER.clear();
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(String modID) {
		MODS.add(modID);
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerWorld) event.getWorld())) {
			Treasure.LOGGER.debug("template registry world load");
			TreasureTemplateRegistry.create((ServerWorld) event.getWorld());
			
			MODS.forEach(mod -> {
				Treasure.LOGGER.debug("registering mod -> {}", mod);
				load(mod);
			});
		}
	}
	
	public static void load(String modID) {
		// don't reload for session
		if (MODS_LOADED.containsKey(modID)) {
			return;
		}
		
		Manifest manifest = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File manifestFile = Paths.get(TEMPLATE_MANAGER.getWorldSaveFolder().getPath(), "data", "structures", modID, "manifest.json").toFile();
		if (manifestFile.exists()) {
			if (manifestFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
					manifest = new GsonBuilder().create().fromJson(json, Manifest.class);
					worldSaveMetaLoaded = true;
					Treasure.LOGGER.debug("loaded template manifest from file system");
				}
				catch (Exception e) {
					Treasure.LOGGER.warn("Couldn't load template manifest from {}", manifestFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in meta manifest
				Path manifestPath = Paths.get(TEMPLATES_FOLDER, modID, "manifest.json");
				manifest = ITreasureResourceRegistry.<Manifest>readResourcesFromFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(TEMPLATES_FOLDER + "/" +  modID + "/manifest.json")), Manifest.class);
				Treasure.LOGGER.debug("loaded template manifest from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to load template resources");
			}
		}
		
		// load template files
		if (manifest != null) {
			TEMPLATE_MANAGER.register(modID, manifest.getResources());
		}
	}
	
	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
	public static TemplateHolder get(World world, Random random, StructureArchetype archetype, StructureType type, Biome biome) {
		return TEMPLATE_MANAGER.getTemplate(random, archetype, type, biome);
	}

	public static TreasureTemplateManager getManager() {
		return TEMPLATE_MANAGER;
	}
}
