/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import java.util.Map.Entry;
import java.util.Optional;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.loot.LootTableShell;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.worldgen.ITreasureWorldGenerator;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
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
		// TODO revisit this!!!! 1) not obeying multi-dimension rules 2) what if they saved off in a different dimension ?
		if (WorldInfo.isServerSide(event.getWorld()) && event.getWorld().provider.getDimension() == 0) {
//			Treasure.logger.debug("server event");
			WorldServer world = (WorldServer) event.getWorld();
//			TreasureLootTables.init(world);
			
			///////////////////////////
			/// New Loot Table Master
			///============
			// called once to initiate world-level properties in the LootTableMaster
			Treasure.LOOT_TABLE_MASTER.init(world);
			// register mod's loot tables with the LootTableMaster
			Treasure.LOOT_TABLE_MASTER.register(mod.getId());
			
			// TEST ///
//			ResourceLocation loc = new ResourceLocation("treasure2", "test/one");
//			Optional<LootTableShell> lootTableShell = Treasure.LOOT_TABLE_MASTER.loadLootTable(Treasure.LOOT_TABLE_MASTER.getWorldDataBaseFolder(), loc);
//			if (lootTableShell.isPresent()) {
//				Treasure.logger.debug("Found world data loot table with version -> {}, # of pools -> {}", lootTableShell.get().getVersion(), lootTableShell.get().getPools().size());
//				// register it with MC
//				ResourceLocation newLoc = LootTableList.register(loc);
//				Treasure.logger.debug("registered world data loot table -> {}", newLoc);
//				LootTable table = world.getLootTableManager().getLootTableFromLocation(newLoc);
//				Treasure.logger.debug("got the loot table -> {}", table);
//			}
//			else {
//				Treasure.logger.debug("Couldn't find world data loot table -> {}", loc);
//			}
			// END TEST ///
			///////////////////////////

			// TODO deprecated calls
			Treasure.LOOT_TABLES.init(world);
//			Treasure.LOOT_TABLES.register(getMod().getId());
			
			// TODO deprecated system
			// register any foreign mod loot tables
//			for (String foreignModID : TreasureConfig.FOREIGN_MODS.enableForeignModIDs) {
//				if (Loader.isModLoaded(foreignModID)) {		
//					Treasure.LOOT_TABLES.register(foreignModID);
//				}
//			}
			
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
