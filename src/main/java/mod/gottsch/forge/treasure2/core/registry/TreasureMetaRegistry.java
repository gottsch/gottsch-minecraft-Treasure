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
package mod.gottsch.forge.treasure2.core.registry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.registry.manifest.ResourceManifest;
import mod.gottsch.forge.treasure2.core.structure.StructureMeta;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

// TODO remove totally - too much work and complexity for un/barely used feature
@Deprecated
public class TreasureMetaRegistry {
	private static final String META_FOLDER = "meta";	
	private static final Set<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	
	protected static final Gson GSON_INSTANCE;
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";
	
	private static ServerLevel world;

	/*
	 * the path to the world save folder
	 */
	private static File worldSaveFolder;
	
	private final static Map<String, StructureMeta> META_MAP = Maps.<String, StructureMeta>newHashMap();

	static {
		REGISTERED_MODS = Sets.newHashSet();
		LOADED_MODS = Maps.newHashMap();
		
		GSON_INSTANCE = new GsonBuilder().create();
	}
	
	/**
	 * 
	 */
	private TreasureMetaRegistry() {}
	
	/**
	 * 
	 * @param world
	 */
	public static void create(ServerLevel world) {
		TreasureMetaRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save).getWorldDir();
			setWorldSaveFolder(path.toFile());
		}
	}
	
	/**
	 * 
	 */
	public void clear() {
		getMetaMap().clear();
	}
	
	public static void register(String modID) {
		REGISTERED_MODS.add(modID);
	}
	
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerLevel) event.getWorld())) {
			Treasure.LOGGER.debug("meta registry world load");
			TreasureMetaRegistry.create((ServerLevel) event.getWorld());
			
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
		
		ResourceManifest manifest = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File manifestFile = Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", modID, META_FOLDER, "manifest.json").toFile();
		if (manifestFile.exists()) {
			if (manifestFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
					manifest = new GsonBuilder().create().fromJson(json, ResourceManifest.class);
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
				manifest = ITreasureResourceRegistry.<ResourceManifest>readResourcesFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID +"/" + META_FOLDER + "/manifest.json")), ResourceManifest.class);
				Treasure.LOGGER.debug("loaded meta manifest from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to locate meta resources file");
			}
		}

		// load meta files
		if (manifest != null) {
			LOADED_MODS.put(modID, true);
			register(modID, manifest.getResources());
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	public static void register(String modID, List<String> resourcePaths) {
		Treasure.LOGGER.debug("registering meta resources");
		// create folders if not exist
		createMetaFolder(getWorldSaveFolder(), modID);
		Treasure.LOGGER.debug("created meta folder");
		
		List<ResourceLocation> resourceLocations = getMetaResourceLocations(modID, resourcePaths);
		Treasure.LOGGER.debug("acquired resource locations -> {}", resourceLocations);
		// load each ResourceLocation as Meta and map it.
		resourceLocations.forEach(loc -> {
			// need to test for world save version first
			Treasure.LOGGER.debug("register metas -> loading meta resource loc -> {}", loc.toString());
			tableMeta(loc, loadMeta(loc));
		});
	}

	private static void createMetaFolder(File worldSaveFolder, String modID) {
		Path folder = Paths.get(worldSaveFolder.getPath(), "datapacks", Treasure.MODID, "data", modID, "meta", "structures").toAbsolutePath();
		if (Files.notExists(folder)) {
			Treasure.LOGGER.debug("meta folder \"{}\" will be created.", folder.toString());
			try {
				Files.createDirectories(folder);
			} catch (IOException e) {
				Treasure.LOGGER.warn("Unable to create meta folder \"{}\"", folder.toString());
			}
		}
	}	
	
	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */
	public static List<ResourceLocation> getMetaResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}
	
	private static void tableMeta(ResourceLocation resourceLocation, Optional<StructureMeta> meta) {
		if (meta.isPresent()) {
			Treasure.LOGGER.debug("tabling meta: key -> {}, meta -> {}", resourceLocation.toString(), meta);
			// add meta to map
			getMetaMap().put(resourceLocation.toString(), meta.get());
		}
		else {
			Treasure.LOGGER.debug("unable to table meta from -> {}", resourceLocation);
		}
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static Optional<StructureMeta> loadMeta(ResourceLocation resource) {
		// attempt to load from file system
		Optional<StructureMeta> meta = loadMetaFromWorldSave(getWorldSaveFolder(), resource);
		if (!meta.isPresent()) {
			return loadMetaFromJar(resource);
		}
		return meta;
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	private static Optional<StructureMeta> loadMetaFromJar(ResourceLocation resource) {		
		Optional<StructureMeta> resourceMeta = Optional.empty();
		String relativePath = "data/" + resource.getNamespace() + "/meta/" + resource.getPath();
		Treasure.LOGGER.debug("attempting to load meta {} from jar -> {}", resource, relativePath);
		
		try (InputStream resourceStream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(relativePath);
				Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
			resourceMeta =  Optional.of(loadMeta(reader));
		}
		catch(Exception e) {
			Treasure.LOGGER.error(String.format("couldn't load resource meta %s ", relativePath), e);
		}		
		return resourceMeta;
	}

	/**
	 * 
	 * @param folder
	 * @param resource
	 * @return
	 */
	private static Optional<StructureMeta> loadMetaFromWorldSave(File folder, ResourceLocation resource) {
		if (folder == null) {
			return Optional.empty();
		}
		else {
			File metaFile = Paths.get(folder.getPath(), "datapacks", Treasure.MODID, "data", resource.getNamespace(), "meta", resource.getPath()).toFile();
			Treasure.LOGGER.debug("attempting to load meta {} from {}", resource, metaFile);

			if (metaFile.exists()) {
				if (metaFile.isFile()) {
					String json;
					try {
						json = com.google.common.io.Files.toString(metaFile, StandardCharsets.UTF_8);
					}
					catch (IOException e) {
						Treasure.LOGGER.warn("couldn't load meta {} from {}", resource, metaFile, e);
						return Optional.empty();
					}
					try {
						return Optional.of(loadMeta(json));
					}
					catch (IllegalArgumentException | JsonParseException e) {
						Treasure.LOGGER.error("couldn't load meta {} from {}", resource, metaFile, e);
						return Optional.empty();
					}
				}
				else {
					Treasure.LOGGER.warn("expected to find meta {} at {} but it was a folder.", resource, metaFile);
					return Optional.empty();
				}
			}
			else {
				Treasure.LOGGER.warn("expected to find meta {} at {} but it doesn't exist.", resource, metaFile);
				return Optional.empty();
			}
		}
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 */
	public static StructureMeta loadMeta(Reader reader) {
		return GSON_INSTANCE.fromJson(reader, StructureMeta.class);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static StructureMeta loadMeta(String json) throws IllegalArgumentException, JsonParseException {
		return GSON_INSTANCE.fromJson(json, StructureMeta.class);
	}
	
	/**
	 * Wrapper to hide the metaMap and return StructureMeta
	 * @param location
	 * @return
	 */
	public StructureMeta get(String key) {
		return (StructureMeta) getMetaMap().get(key);
	}
	
	public static File getWorldSaveFolder() {
		return TreasureMetaRegistry.worldSaveFolder;
	}

	public static void setWorldSaveFolder(File worldSaveFolder) {
		TreasureMetaRegistry.worldSaveFolder = worldSaveFolder;
	}
	
	public static Map<String, StructureMeta> getMetaMap() {
		return META_MAP;
	}
}
