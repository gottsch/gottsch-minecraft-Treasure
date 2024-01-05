/**
 * 
 */
package mod.gottsch.forge.treasure2.core.world.gen.structure;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayManager;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.meta.StructureMeta;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Dec 20, 2019
 *
 */
public class TreasureDecayManager extends DecayManager {
	// set to empty/blank list as there is only one location. current design of methods must take in a location or list of locations.
	//	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("");

	public TreasureDecayManager(IMod mod, String resourceFolder) {
		super(mod, resourceFolder);

		// TODO move out to WorldLoadEvent
		// build and expose template/structure folders
		//		if (TreasureConfig.GENERAL.enableDefaultDecayRuleSetsCheck.get()) {
		//			buildAndExpose("data", Treasure.MODID, getBaseResourceFolder(), FOLDER_LOCATIONS);
		//		}
	}

	public static void init(ServerWorld world) {
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
	public void register(String modID, List<String> locations) {
		for (String location : locations) {
			Treasure.LOGGER.debug("registering ruleset files from location -> {}", location);
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);

			// load each ResourceLocation as DecayRuleSet and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getPath());
				if (Treasure.LOGGER.isDebugEnabled()) {
					Treasure.LOGGER.debug("path to ruleset resource loc -> {}", path.toString());
				}

				// load ruleset
				Treasure.LOGGER.debug("attempted to load custom ruleset file  with key -> {}", loc.toString());
				IDecayRuleSet ruleset = load(loc);
				// add the id to the map
				if (ruleset == null) {
					Treasure.LOGGER.debug("Unable to locate ruleset file -> {}", loc.toString());
					continue;
				}
				Treasure.LOGGER.debug("loaded custom ruleset file  with key -> {}", loc.toString());
			}
		}
	}

	/**
	 * Wrapper to hide the ruleSetMap
	 * @param location
	 * @return
	 */
	public IDecayRuleSet get(String key) {
		return  getRuleSetMap().get(key);
	}
}