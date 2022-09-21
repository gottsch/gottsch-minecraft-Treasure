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
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.feature.TreasureFeatures;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
//@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WorldEventHandler {

	private boolean isLoaded = false;

	// reference to the mod.
	@Deprecated
	private IMod mod;

	/**
	 * 
	 */
	public WorldEventHandler(IMod mod) {
		setMod(mod);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldLoad(WorldEvent.Load event) {

		/*
		 * On load of dimension 0 (overworld), initialize the loot table's context and other static loot tables
		 */
		if (WorldInfo.isServerSide((World)event.getWorld())) {
			ServerWorld world = (ServerWorld) event.getWorld();
			
			Treasure.LOGGER.info("In world load event for dimension {}", WorldInfo.getDimension(world).toString());
			
			// TODO instead of checking for minecraft:overworld, check for the first whitelist dimension
			if (!isLoaded && TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimension.toString())) {
			// if (WorldInfo.isSurfaceWorld(world)) {

				// register mod's loot tables
				TreasureLootTableRegistry.onWorldLoad(event);
				TreasureMetaRegistry.onWorldLoad(event);
				TreasureTemplateRegistry.onWorldLoad(event);
//				TreasureDecayRegistry.register(getMod().getId());

				/*
				 * clear the current World Gens values and reload
				 */
				TreasureFeatures.PERSISTED_FEATURES.forEach(feature -> {
					feature.init();
				});

				// clear CHEST REGISTRIES
				TreasureData.CHEST_REGISTRIES2.entrySet().forEach(entry -> {
					entry.getValue().clear();
				});
				/*
				 * un-load the chest registry
				 */
				TreasureData.CHEST_REGISTRIES.entrySet().forEach(entry -> {
					//				Treasure.logger.debug("Chest registry size BEFORE cleaning -> {}", ChestRegistry.getInstance().getValues().size());
					entry.getValue().clear();
					//				Treasure.logger.debug("Chest registry size AFTER cleaning -> {}", ChestRegistry.getInstance().getValues().size());
				});		
				TreasureGenerationSavedData.get(world);
				//			Treasure.logger.debug("Chest registry size after world event load -> {}", ChestRegistry.getInstance().getValues().size());

				isLoaded = true;
			}	
		}
	}

	@SubscribeEvent
	public void onServerStopping(FMLServerStoppingEvent event) {
//		Treasure.LOGGER.debug("Closing out of world.");
		// clear all resource managers
//		TreasureLootTableRegistry.clear();
////		TreasureTemplateRegistry.clear();
//		TreasureMetaRegistry.clear();
//		TreasureDecayRegistry.clear();
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
