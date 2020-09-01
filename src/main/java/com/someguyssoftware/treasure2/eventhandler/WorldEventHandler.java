/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
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
		Treasure.LOGGER.debug("In world load event for dimension {}", event.getWorld().getDimension().toString());
		
		/*
		 * On load of dimension 0 (overworld), initialize the loot table's context and other static loot tables
		 */
		if (WorldInfo.isServerSide((World)event.getWorld()) && event.getWorld().getDimension().isSurfaceWorld()) {
//			Treasure.logger.debug("server event");
			ServerWorld world = (ServerWorld) event.getWorld();

			// TODO facade pattern here - make one call to lootTableMaster.start()
			Treasure.lootTableMaster.init(world);
			Treasure.lootTableMaster.load(getMod().getId());
			Treasure.lootTableMaster.register(getMod().getId());
			// register any foreign mod loot tables
//			for (String foreignModID : TreasureConfig.FOREIGN_MODS.enableForeignModIDs) {
//				if (Loader.isModLoaded(foreignModID)) {		
//					Treasure.LOOT_TABLES.register(foreignModID);
//				}
//			}
//			Treasure.META_MANAGER.register(getMod().getId());
//			Treasure.TEMPLATE_MANAGER.register(getMod().getId());
//			Treasure.DECAY_MANAGER.register(getMod().getId());
					
			/*
			 * clear the current World Gens values and reload
			 */
//			for (Entry<WorldGenerators, ITreasureWorldGenerator> worldGenEntry : Treasure.WORLD_GENERATORS.entrySet()) {
//				worldGenEntry.getValue().init();
//			}
//			
//			/*
//			 * un-load the chest registry
//			 */
//			Treasure.logger.debug("Chest registry size BEFORE cleaning -> {}", ChestRegistry.getInstance().getValues().size());
//			ChestRegistry.getInstance().clear();	
//			Treasure.logger.debug("Chest registry size AFTER cleaning -> {}", ChestRegistry.getInstance().getValues().size());
//			
//			GenDataPersistence.get(world);			
//			Treasure.logger.debug("Chest registry size after world event load -> {}", ChestRegistry.getInstance().getValues().size());
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
