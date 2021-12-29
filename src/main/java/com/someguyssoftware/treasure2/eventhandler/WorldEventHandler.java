/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.worldgen.ITreasureWorldGenerator;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
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

			/*
			 *  called once to initiate world-level properties in the registries.
			 *  not to be called by modders - they only call register();
			 */
//			TreasureLootTableRegistry.getLootTableMaster().init(world);
//			TreasureMetaRegistry.create(world);
			
			// register mod's loot tables
//			TreasureLootTableRegistry.register();
			TreasureLootTableRegistry.onWorldLoad(event);

			// register files with their respective managers
//			Treasure.META_MANAGER.register(getMod().getId());
//	-->		TreasureMetaRegistry.register(getMod().getId());
			TreasureMetaRegistry.onWorldLoad(event);
//			Treasure.TEMPLATE_MANAGER.register(getMod().getId());
			TreasureTemplateRegistry.onWorldLoad(event);	
//			Treasure.DECAY_MANAGER.register(getMod().getId());
			TreasureDecayRegistry.onWorldLoad(event);

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

	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent event) {
		if (event.getName().toString().equals(LootTableList.CHESTS_SIMPLE_DUNGEON.toString()/*"minecraft:chests/simple_dungeon"*/)) {

			// load a loot table
			ResourceLocation location = new ResourceLocation(Treasure.MODID, "pools/treasure/scarce");
			LootEntry entry = new LootEntryTable(location, 1, 1, new LootCondition[] {}, "treasure");
			LootPool pool = new LootPool(new LootEntry[] {entry}, new LootCondition[] {}, new RandomValueRange(1), new RandomValueRange(1), "treasure");
			event.getTable().addPool(pool);		
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
