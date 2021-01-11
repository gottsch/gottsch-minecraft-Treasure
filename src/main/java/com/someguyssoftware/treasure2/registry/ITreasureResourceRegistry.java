package com.someguyssoftware.treasure2.registry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.someguyssoftware.gottschcore.json.JSMin;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.loot.LootResources;

/**
 * 
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public interface ITreasureResourceRegistry {
	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static <T> T readResourcesFromFromStream(InputStream inputStream, Class<T> resourceClass) throws IOException, Exception {
		Treasure.LOGGER.info("reading resources file from stream.");
		T resources = null;

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
			resources = gson.fromJson(jsonReader, resourceClass);
			Treasure.LOGGER.info("resources -> {}", resources);
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
		return (T) resources;
	}
}