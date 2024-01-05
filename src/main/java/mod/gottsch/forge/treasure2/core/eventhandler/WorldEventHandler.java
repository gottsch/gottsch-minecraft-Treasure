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
package mod.gottsch.forge.treasure2.core.eventhandler;

import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.data.TreasureData;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.persistence.TreasureGenerationSavedData;
import mod.gottsch.forge.treasure2.core.registry.TreasureMetaRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

/**
 * @author Mark Gottschling on Jun 29, 2018
 *
 */
//@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class WorldEventHandler {

	private boolean isLoaded = false;


//	@SubscribeEvent
//    public static void onRenderTooltip(final RenderTooltipEvent.Pre event) {
//		Treasure.LOGGER.info("In render tooltipevent");
//    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldLoad(WorldEvent.Load event) {
		Treasure.LOGGER.info("In world load event");
		/*
		 * On load of dimension 0 (overworld), initialize the loot table's context and other static loot tables
		 */
		if (WorldInfo.isServerSide((World)event.getWorld())) {
			ServerWorld world = (ServerWorld) event.getWorld();
			ResourceLocation dimension = WorldInfo.getDimension(world);
			
			Treasure.LOGGER.info("In world load event for dimension {}", dimension.toString());
			
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
//				TreasureFeatures.PERSISTED_FEATURES.forEach(feature -> {
//					feature.init();
//				});

				/*
				 * un-load the chest registry
				 */
				TreasureData.CHEST_REGISTRIES2.entrySet().forEach(entry -> {
					entry.getValue().forEach((key, value) -> {
						value.clear();
					});
				});		
				TreasureGenerationSavedData.get(world);

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
}
