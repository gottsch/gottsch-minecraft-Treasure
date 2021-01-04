/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;

/**
 * Use this registry to register all your mod's custom loot table for Treasure2.
 * @author Mark Gottschling on Dec 4, 2020
 *
 */
public final class TreasureLootTableRegistry {
    // TODO rename to const UPPERCASE
	private static final List<String> registeredMods = new ArrayList<>();
    private static LootResources lootResources;

    static {
        // load master loot resources lists
        try {
            lootResources = readLootResourcesFromFromStream(
                    Objects.requireNonNull(getMod().getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCES_LIST_PATH))
                    );
        }
        catch(Exception e) {
            Treasure.LOGGER.warn("Unable to expose loot tables");
        }
    }

    /**
	 * NOTE who calls this? when? add comments to explain
	 * @param modID
	 */
	private static void buildAndExpose(String modID) {
		// Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		// Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		// Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.POOL_LOOT_TABLE_FOLDER_LOCATIONS);
		// Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.INJECT_LOOT_TABLE_FOLDER_LOCATIONS);
    
        Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, lootResources.getChestResources());
        Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, lootResources.getSpecialResources());
        Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, lootResources.getSupportingResources());
        Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, lootResources.getInjectResources());
    }

	/**
	 * 
	 * @param modID
	 */
	public static void register(final String modID) {
		if (!registeredMods.contains(modID)) {
			buildAndExpose(modID);
			Treasure.lootTableMaster.register(modID);
			registeredMods.add(modID);
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param customFolders
	 */
	public static void register(final String modID, final @Nullable List<String> customFolders) {
		if (!registeredMods.contains(modID)) {
			if (customFolders != null && !customFolders.isEmpty()) {
				Treasure.lootTableMaster.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, customFolders);
			}
			register(modID);
		}
	}

    /**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public LootResources readLootResourcesFromFromStream(InputStream inputStream) throws IOException, Exception {
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

    // TOOD rename to proper camelCase
	public static List<String> getRegisteredmods() {
		return registeredMods;
	}
}