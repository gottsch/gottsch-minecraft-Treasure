package com.someguyssoftware.treasure2.loot;

import java.util.List;

import com.someguyssoftware.gottschcore.mod.IMod;

import net.minecraft.world.WorldServer;

public interface ILootTableMaster {

	/**
	 * Call in WorldEvent.Load event handler.
	 * @param world
	 */
	void init(WorldServer world);

	void clear();

	/**
	 * CREATES CONFIG FOLDERS/RESOURCES - on mod/manager creation
	 * Creates all the necessary folder and resources before actual loading of loot tables.
	 * Call in your @Mod class in preInt() or int().
	 * 
	 * @param resourceRootPath
	 * @param modID
	 */
	void buildAndExpose(String resourceRootPath, String modID, List<String> locations);

	/**
	 * ONE STOP SHOP to tell where the resource is and to add to the maps and to register with minecraft
	 * 
	 * Call in WorldEvent.Load event handler.
	 * Overide this method if you have a different cache mechanism.
	 * @param location
	 */
	void register(WorldServer world, String modID, List<String> locations);

	IMod getMod();

}