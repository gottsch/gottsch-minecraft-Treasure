package com.someguyssoftware.treasure2.registry;

import java.util.Objects;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.worldgen.structure.DecayResources;
import com.someguyssoftware.treasure2.worldgen.structure.TreasureDecayManager;

public class TreasureDecayRegistry implements ITreasureResourceRegistry {
	private static final String DEFAULT_RESOURCES_LIST_PATH = "decay/default_ruleset_list.json";	
	
	private static TreasureDecayManager decayManager;
	private static DecayResources decayResources;
	
	static {
		// load master loot resources lists
		try {
			decayResources = ITreasureResourceRegistry.<DecayResources>readResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCES_LIST_PATH)), DecayResources.class);
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to expose loot tables");
		}
	}
	
	/**
	 * 
	 */
	private TreasureDecayRegistry() {}

	/**
	 * 
	 * @param mod
	 */
	public synchronized static void create(IMod mod) {
		if (decayManager  == null) {
			decayManager = new TreasureDecayManager(mod, "decay");
		}
	}
	
	/**
	 * Wrapper method
	 * @param modID
	 */
	public static void register(final String modID) {
		buildAndExpose(modID);
		decayManager.register(modID, decayResources.getResources());
	}
	
	/**
	 * 
	 * @param modID
	 */
	private static void buildAndExpose(String modID) {
		decayManager.buildAndExpose("data", modID, "decay", decayResources.getResources());		
	}
	
	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
	public static IDecayRuleSet get(String key) {
		return decayManager.get(key);
	}

	public static TreasureDecayManager getDecayManager() {
		return decayManager;
	}
}
