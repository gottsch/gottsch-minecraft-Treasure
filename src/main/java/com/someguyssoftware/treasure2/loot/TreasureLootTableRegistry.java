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
	private static final List<String> registeredMods = new ArrayList<>();

	/**
	 * 
	 * @param modID
	 */
	public static void register(final String modID) {
		if (!registeredMods.contains(modID)) {
			buildAndExpose(modID);
			Treasure.LOOT_TABLE_MASTER.register(modID);
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
				Treasure.LOOT_TABLE_MASTER.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, customFolders);
			}
			register(modID);
		}
	}
	
	/**
	 * 
	 * @param modID
	 */
	private static void buildAndExpose(String modID) {
		Treasure.LOOT_TABLE_MASTER.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		Treasure.LOOT_TABLE_MASTER.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.SPECIAL_CHEST_LOOT_TABLE_FOLDER_LOCATIONS);
		Treasure.LOOT_TABLE_MASTER.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.POOL_LOOT_TABLE_FOLDER_LOCATIONS);
		Treasure.LOOT_TABLE_MASTER.buildAndExpose(TreasureLootTableMaster2.CUSTOM_LOOT_TABLES_RESOURCE_PATH, modID, TreasureLootTableMaster2.INJECT_LOOT_TABLE_FOLDER_LOCATIONS);
	}

	public static List<String> getRegisteredmods() {
		return registeredMods;
	}
}
