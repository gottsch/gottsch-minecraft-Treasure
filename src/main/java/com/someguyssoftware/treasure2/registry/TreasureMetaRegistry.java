/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.registry;

import java.util.Objects;

import com.someguyssoftware.gottschcore.meta.MetaManager;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.MetaManifest;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;

/**
 * 
 * @author Mark Gottschling on Dec 16, 2021
 *
 */
public class TreasureMetaRegistry {
	private static final String DEFAULT_MANIFEST_PATH = "meta/manifest.json";	
	private static final String META_VERSION_FOLDER = "mc1_12";
	private static final String META_FOLDER = "meta";
	private static final String META_PATH = META_VERSION_FOLDER +"/" + META_FOLDER;
	
	private static TreasureMetaManager metaManager;
	private static MetaManifest metaManifest;

	static {
		// load template manifest
		try {
			metaManifest = ITreasureResourceRegistry.<MetaManifest>readResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_MANIFEST_PATH)), MetaManifest.class);
		}
		catch(Exception e) {
			Treasure.logger.warn("Unable to template resources");
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
			metaManager = new TreasureMetaManager(mod, META_PATH);
		}
	}
	
	/**
	 * TODO this strategy only grabs the listed resources from config folder. need to grab all of them so users can add their own
	 * TODO may not want to put in config, but rather in world save so that they can override and/or add
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
		metaManager.buildAndExpose(MetaManager.ASSETS_FOLDER, modID, META_VERSION_FOLDER, META_FOLDER, metaManifest.getResources());		
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
