/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayManager;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Dec 20, 2019
 *
 */
public class TreasureDecayManager extends DecayManager {
	// set to empty/blank list as there is only one location. current design of methods must take in a location or list of locations.
	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("");

	public TreasureDecayManager(IMod mod, String resourceFolder) {
		super(mod, resourceFolder);

		// build and expose template/structure folders
		if (TreasureConfig.MOD.enableDefaultDecayRuletSetsCheck) {
			buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
		}
	}

	/**
	 * 
	 */
	public void clear() {
		super.clear();
	}

	/**
	 * Call in WorldEvent.Load() event handler. Loads and registers ruleset files from
	 * the file system.
	 * 
	 * @param modID
	 */
	public void register(String modID) {
		// set location to empty because there is only one location where decay files are.
		String location = "";
		Treasure.logger.debug("registering ruleset files from location -> {}", location);
		// get loot table files as ResourceLocations from the file system location
		List<ResourceLocation> locs = getResourceLocations(modID, location);

		// load each ResourceLocation as DecayRuleSet and map it.
		for (ResourceLocation loc : locs) {
			Path path = Paths.get(loc.getResourcePath());
			if (Treasure.logger.isDebugEnabled()) {
				Treasure.logger.debug("path to ruleset resource loc -> {}", path.toString());
			}

			// load ruleset
			Treasure.logger.debug("attempted to load custom ruleset file  with key -> {}", loc.toString());
			IDecayRuleSet ruleset = load(loc);
			// add the id to the map
			if (ruleset == null) {
				Treasure.logger.debug("Unable to locate ruleset file -> {}", loc.toString());
				continue;
			}
			Treasure.logger.debug("loaded custom ruleset file  with key -> {}", loc.toString());
		}
	}
}