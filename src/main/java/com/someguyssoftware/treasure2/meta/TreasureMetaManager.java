/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.enums.IRarity;
import com.someguyssoftware.gottschcore.json.JSMin;
import com.someguyssoftware.gottschcore.meta.IMeta;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaTheme;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.meta.MetaManager;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
// TODO need a meta manager for each type of resource
// TODO need specific deserializers for each type of resource or use some lambda

public class TreasureMetaManager extends MetaManager {
    private static final String DEFAULT_RESOURCE_LIST = "meta/default-structure-list.txt";
	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("structures");

	public TreasureMetaManager(IMod mod, String resourceFolder) {
		super(mod, resourceFolder);

		// build and expose template/structure folders
		buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
	}

	/**
	 * 
	 */
	public void clear() {
		super.clear();
	}

	/**
	 * Call in WorldEvent.Load() event handler. Loads and registers meta files from
	 * the file system.
	 * 
	 * @param modID
	 */
	public void register(String modID) {
		for (String location : FOLDER_LOCATIONS) {
			Treasure.LOGGER.debug("registering meta files from location -> {}", location);
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);

			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getPath());
				if (Treasure.LOGGER.isDebugEnabled()) {
					Treasure.LOGGER.debug("path to meta resource loc -> {}", path.toString());
				}

				// load template
				Treasure.LOGGER.debug("attempted to load custom meta file  with key -> {}", loc.toString());
				IMeta meta = load(loc);
				// add the id to the map
				if (meta == null) {
					Treasure.LOGGER.debug("Unable to locate meta file -> {}", loc.toString());
					continue;
				}
				Treasure.LOGGER.debug("loaded custom meta file  with key -> {}", loc.toString());
			}
		}
	}

	/**
	 * TODO need different "reader" classes or lambda to read different concrete metas. ie ChestMeta
	 * TODO move to GottschCore - NO cannot - this is a StructureMeta specific
	 * reads a template from an inputstream
	 */
	@Override
	protected void readFromStream(String id, InputStream stream) throws IOException, Exception {
		Treasure.LOGGER.debug("reading meta file from stream.");
		IMeta meta = null;

		// read json sheet in and minify it
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JSMin minifier = new JSMin(stream, out);
		minifier.jsmin();

		// out minified json into a json reader
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Reader reader = new InputStreamReader(in);
		JsonReader jsonReader = new JsonReader(reader);

		// create a gson builder
		GsonBuilder gsonBuilder = new GsonBuilder();

		/*
		 * create types for all the properties of a StyleSheet
		 */
		Type metaArchetype = new TypeToken<List<IMetaArchetype>>() {
		}.getType();
		Type metaTheme = new TypeToken<List<IMetaTheme>>() {
		}.getType();
		Type rarity = new TypeToken<List<IRarity>>() {
		}.getType();

		/*
		 * register the types with the custom deserializer
		 */
		gsonBuilder.registerTypeAdapter(metaArchetype, new MetaArchetypeDeserializer());
		gsonBuilder.registerTypeAdapter(IMetaType.class, new MetaTypeDeserializer());
		gsonBuilder.registerTypeAdapter(metaTheme, new MetaThemeDeserializer());
		gsonBuilder.registerTypeAdapter(rarity, new RarityDeserializer());
		gsonBuilder.registerTypeAdapter(ICoords.class, new CoordsDeserializer());
		Gson gson = gsonBuilder.create();

		// read minified json into gson and generate objects
		try {
			meta = gson.fromJson(jsonReader, StructureMeta.class);
			Treasure.LOGGER.debug("meta[{}] -> {}", id, meta);
		} catch (JsonIOException | JsonSyntaxException e) {
			// TODO change to custom exception
			throw new Exception("Unable to load meta file:", e);
		} finally {
			// close objects
			try {
				jsonReader.close();
			} catch (IOException e) {
				Treasure.LOGGER.warn("Unable to close JSON Reader when reading meta file.");
			}
		}

		// add meta to map
		this.getMetaMap().put(id, meta);
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
}
