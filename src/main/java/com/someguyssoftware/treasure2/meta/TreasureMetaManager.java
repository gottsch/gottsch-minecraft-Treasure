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
package com.someguyssoftware.treasure2.meta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.someguyssoftware.gottschcore.enums.IRarity;
import com.someguyssoftware.gottschcore.meta.IMeta;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaTheme;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public class TreasureMetaManager {
	
	protected static final Gson GSON_INSTANCE;
	private File worldSaveFolder;
	private final Map<String, IMeta> metaMap = Maps.<String, IMeta>newHashMap();

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Type metaArchetype = new TypeToken<List<IMetaArchetype>>() {}.getType();
		Type metaTheme = new TypeToken<List<IMetaTheme>>() {}.getType();
		Type rarity = new TypeToken<List<IRarity>>() {	}.getType();

		GSON_INSTANCE = gsonBuilder.registerTypeAdapter( metaArchetype, new MetaArchetypeDeserializer())
		.registerTypeAdapter(IMetaType.class, new MetaTypeDeserializer())
		.registerTypeAdapter(metaTheme, new MetaThemeDeserializer())
		.registerTypeAdapter(rarity, new RarityDeserializer())
		.registerTypeAdapter(ICoords.class, new CoordsDeserializer())
		.create();
	}
	
	public TreasureMetaManager() {
	}

	public static void init(ServerWorld world) {	
	}
	
	/**
	 * 
	 */
	public void clear() {
		getMetaMap().clear();
	}

	/**
	 * Call in WorldEvent.Load() event handler. Loads and registers meta files from
	 * the file system.
	 * 
	 * @param modID
	 */
	public void register(String modID, List<String> resourcePaths) {
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

	private void createMetaFolder(File worldSaveFolder, String modID) {
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
	
	private void tableMeta(ResourceLocation resourceLocation, Optional<StructureMeta> meta) {
		if (meta.isPresent()) {
			Treasure.LOGGER.debug("tabling meta: key -> {}, meta -> {}", resourceLocation.toString(), meta);
			// add meta to map
			this.getMetaMap().put(resourceLocation.toString(), meta.get());
		}
		else {
			Treasure.LOGGER.debug("unable to table meta from -> {}", resourceLocation);
		}
	}

	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */
	public List<ResourceLocation> getMetaResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Optional<StructureMeta> loadMeta(ResourceLocation resource) {
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
	private Optional<StructureMeta> loadMetaFromJar(ResourceLocation resource) {		
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
	private Optional<StructureMeta> loadMetaFromWorldSave(File folder, ResourceLocation resource) {
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
	public StructureMeta loadMeta(Reader reader) {
		return GSON_INSTANCE.fromJson(reader, StructureMeta.class);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public StructureMeta loadMeta(String json) throws IllegalArgumentException, JsonParseException {
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

	// TODO could move all these to a GsonDeserializerHelper
	public static class MetaArchetypeDeserializer implements JsonDeserializer<List<IMetaArchetype>> {
		@Override
		public List<IMetaArchetype> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			List<IMetaArchetype> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement e : array) {
				list.add(StructureArchetype.valueOf(e.getAsString().toUpperCase()));
			}
			return list;
		}
	}

	public static class MetaTypeDeserializer implements JsonDeserializer<StructureType> {
		@Override
		public StructureType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return StructureType.valueOf(json.getAsString().toUpperCase());
		}
	}

	public static class MetaThemeDeserializer implements JsonDeserializer<List<IMetaTheme>> {
		@Override
		public List<IMetaTheme> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			List<IMetaTheme> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement e : array) {
				list.add(StructureTheme.valueOf(e.getAsString().toUpperCase()));
			}
			return list;
		}
	}

	public static class RarityDeserializer implements JsonDeserializer<List<IRarity>> {
		@Override
		public List<IRarity> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			List<IRarity> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement e : array) {
				list.add(Rarity.valueOf(e.getAsString().toUpperCase()));
			}
			return list;
		}
	}

	public static class CoordsDeserializer implements JsonDeserializer<Coords> {
		@Override
		public Coords deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return context.deserialize(json, Coords.class);
		}
	}
	
	public File getWorldSaveFolder() {
		return worldSaveFolder;
	}

	public void setWorldSaveFolder(File worldSaveFolder) {
		this.worldSaveFolder = worldSaveFolder;
	}

	public Map<String, IMeta> getMetaMap() {
		return metaMap;
	}
}
