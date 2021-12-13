/**
 * 
 */
package com.someguyssoftware.treasure2.registry;

import java.util.Objects;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.world.gen.structure.DecayResources;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateManifest;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;

import net.minecraft.world.level.block.Block;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.world.server.ServerLevel;

/**
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TreasureTemplateRegistry {
	private static final String DEFAULT_MANIFEST_PATH = "structures/manifest.json";	
	
	private static TreasureTemplateManager templateManager;
	private static TemplateManifest templateManifest;
	
	static {
		// load template manifest
		try {
			templateManifest = ITreasureResourceRegistry.<TemplateManifest>readResourcesFromFromStream(
					Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream(DEFAULT_MANIFEST_PATH)), TemplateManifest.class);
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to template resources");
		}
	}
	
	/**
	 * 
	 */
	private TreasureTemplateRegistry() {}
	
	/**
	 * 
	 * @param mod
	 */
	public synchronized static void create(IMod mod) {
		if (templateManager  == null) {
			templateManager = new TreasureTemplateManager(Treasure.instance, "mc1_16/structures", DataFixesManager.getDataFixer());
		}
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(final String modID) {
		buildAndExpose(modID);
		templateManager.register(modID, templateManifest.getResources());		
	}

	/**
	 * 
	 * @param modID
	 */
	private static void buildAndExpose(String modID) {
		templateManager.buildAndExpose("data", modID, "mc1_16", "structures", templateManifest.getResources());		
	}
	
	/**
	 * 
	 * @return
	 */
	public static TreasureTemplateManager getTemplateManager() {
		return templateManager;
	}
	
	/**
	 * convenience method
	 * 
	 * @param offset
	 * @return
	 */
	public static Block getMarkerBlock(StructureMarkers marker) {
		return templateManager.getMarkerMap().get(marker);
	}

	/**
	 * 
	 * @param world
	 */
	public static void initialize(ServerLevel world) {
		templateManager.init(world);
		
	}
}
