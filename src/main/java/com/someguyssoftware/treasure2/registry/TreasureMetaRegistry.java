package com.someguyssoftware.treasure2.registry;

import java.util.Objects;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.MetaManifest;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;

public class TreasureMetaRegistry {
	private static final String DEFAULT_MANIFEST_PATH = "meta/manifest.json";	
	
	private static TreasureMetaManager metaManager;
	private static MetaManifest metaManifest;

	static {
		// load template manifest
		try {
			metaManifest = ITreasureResourceRegistry.<MetaManifest>readResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_MANIFEST_PATH)), MetaManifest.class);
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to template resources");
		}
	}

	/**
	 * 
	 */
	private TreasureMetaRegistry() {}
	
	/**
	 * 
	 * @param mod
	 */
	public synchronized static void create(IMod mod) {
		if (metaManager  == null) {
			metaManager = new TreasureMetaManager(mod, "mc1_16/meta");
		}
	}
	
	/**
	 * Wrapper method
	 * @param modID
	 */
	public static void register(final String modID) {
		buildAndExpose(modID);
		metaManager.register(modID, metaManifest.getResources());
	}
	
	/**
	 * 
	 * @param modID
	 */
	private static void buildAndExpose(String modID) {
		metaManager.buildAndExpose("data", modID, "mc1_16", "meta", metaManifest.getResources());		
	}
	
	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
	public static StructureMeta get(String key) {
		return metaManager.get(key);
	}
	
	public static TreasureMetaManager getMetaManager() {
		return metaManager;
	}	
}
