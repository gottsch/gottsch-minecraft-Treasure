/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.event;

import java.nio.file.Path;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.cache.FeatureCaches;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.util.TreasureDataFixer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class WorldEventHandler {

	private static Path worldSavePath;
	private static boolean isLoaded = false;
	private static boolean isClientLoaded = false;

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(LevelEvent.Load event) {
		Treasure.LOGGER.info("In world load event");

		if (WorldInfo.isServerSide((Level)event.getLevel())) {
			/* 
			 * NOTE:
			 *  this has to happen here or some event AFTER the FMLCommonSetup
			 *  when all blocks, items, etc are registered and tags are read in.
			 */

			ResourceLocation dimension = WorldInfo.getDimension((Level) event.getLevel());			
			Treasure.LOGGER.info("In world load event for dimension {}", dimension.toString());

			/*
			 *  cache the world save folder and pass into each registry.
			 */
			Optional<Path> worldSavePath = ModUtil.getWorldSaveFolder((ServerLevel)event.getLevel());
			if (worldSavePath.isPresent()) {
				if ((!isLoaded && Config.SERVER.integration.dimensionsWhiteList.get().contains(dimension.toString())) ||
						!worldSavePath.get().equals(WorldEventHandler.worldSavePath)) {
					
					// initialize feature caches
					FeatureCaches.initialize();
					
					// fix data		
					TreasureDataFixer.fix();
					
					// cache the folder
					WorldEventHandler.worldSavePath = worldSavePath.get();
					
					// register mod's loot tables
					TreasureLootTableRegistry.onWorldLoad(event, WorldEventHandler.worldSavePath);
					TreasureTemplateRegistry.onWorldLoad(event, WorldEventHandler.worldSavePath);				
					TreasureSavedData.get((Level)event.getLevel());
					isLoaded = true;
				}

			} else {
				Treasure.LOGGER.warn("unable to locate the world save folder.");
			}
		} else {
			if (!isClientLoaded) {
				TreasureDataFixer.fix();
				isClientLoaded = true;
			}
		}
	}
}
