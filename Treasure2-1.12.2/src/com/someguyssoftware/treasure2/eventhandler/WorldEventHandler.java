/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
public class WorldEventHandler {

	// reference to the mod.
	private IMod mod;
	
	/**
	 * 
	 */
	public WorldEventHandler(IMod mod) {
		setMod(mod);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
//		Treasure.logger.debug("In world load event for dimension {}", event.getWorld().provider.getDimension());
		
		/*
		 * On load of dimension 0 (overworld), initialize the loot table's context and other static loot tables
		 */
		if (WorldInfo.isServerSide(event.getWorld()) && event.getWorld().provider.getDimension() == 0) {
//			Treasure.logger.debug("server event");
			WorldServer world = (WorldServer) event.getWorld();
//			TreasureLootTables.init(world);
			Treasure.LOOT_TABLES.init(world);
			Treasure.LOOT_TABLES.register(getMod().getId());
			// register any foreign mod loot tables
			for (String foreignModID : TreasureConfig.enableForeignModIDs) {
				if (Loader.isModLoaded(foreignModID)) {		
					Treasure.LOOT_TABLES.register(foreignModID);
				}
			}
			Treasure.TEMPLATE_MANAGER.register(getMod().getId());
		}
	}

	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}

}
