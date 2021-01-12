/**
 * 
 */
package com.someguyssoftware.treasure2.init;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.feature.TreasureFeatures;

import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * TODO probably get rid of this class and place back into main Treasure class
 * @author Mark Gottschling on Jan 5, 2021
 *
 */
public class TreasureSetup implements IModSetup {
	/**
	 * 
	 * @param event
	 */
	public static void common(final FMLCommonSetupEvent event) {
		// add mod specific logging
		IModSetup.addRollingFileAppender(Treasure.instance.getName(), null);
		
		// initialize all the data lists, maps, etc
		TreasureData.initialize();
		
		// start the registries
		TreasureLootTableRegistry.create(Treasure.instance);
		TreasureMetaRegistry.create(Treasure.instance);
		TreasureTemplateRegistry.create(Treasure.instance);
		TreasureDecayRegistry.create(Treasure.instance);
		
		// add features to biomes
		DeferredWorkQueue.runLater(TreasureFeatures::addFeatureToBiomes);
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void clientSetup(final FMLClientSetupEvent event) {
		Treasure.LOGGER.info("in client setup event");
	}

}
