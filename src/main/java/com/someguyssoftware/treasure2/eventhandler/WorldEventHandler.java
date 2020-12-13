/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.worldgen.ITreasureWorldGenerator;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldLoad(WorldEvent.Load event) {
		Treasure.logger.debug("In world load event for dimension {}", event.getWorld().provider.getDimension());
		
		/*
		 * On load of dimension 0 (overworld), initialize the loot table's context and other static loot tables
		 */
		if (WorldInfo.isServerSide(event.getWorld()) && event.getWorld().provider.getDimension() == 0) {
//			Treasure.logger.debug("server event");
			WorldServer world = (WorldServer) event.getWorld();

			// called once to initiate world-level properties in the LootTableMaster
			Treasure.LOOT_TABLE_MASTER.init(world);
			
			// register mod's loot tables
			TreasureLootTableRegistry.register(mod.getId());
			
			// register files with their respective managers
			Treasure.META_MANAGER.register(getMod().getId());
			Treasure.TEMPLATE_MANAGER.register(getMod().getId());
			Treasure.DECAY_MANAGER.register(getMod().getId());
					
			/*
			 * clear the current World Gens values and reload
			 */
			for (Entry<WorldGeneratorType, ITreasureWorldGenerator> worldGenEntry : Treasure.WORLD_GENERATORS.entrySet()) {
				worldGenEntry.getValue().init();
			}
			
			/*
			 * un-load the chest registry
			 */
			Treasure.logger.debug("Chest registry size BEFORE cleaning -> {}", ChestRegistry.getInstance().getValues().size());
			ChestRegistry.getInstance().clear();	
			Treasure.logger.debug("Chest registry size AFTER cleaning -> {}", ChestRegistry.getInstance().getValues().size());
			
			GenDataPersistence.get(world);			
			Treasure.logger.debug("Chest registry size after world event load -> {}", ChestRegistry.getInstance().getValues().size());
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
