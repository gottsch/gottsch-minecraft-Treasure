/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.event;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.util.TreasureDataFixer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.nio.file.Path;

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
			if (!isLoaded) {
//					&& Config.SERVER.integration.dimensionsWhiteList.get().contains(dimension.toString())) {

				Treasure.LOGGER.debug("reading in chests config...");

				// fix data
				TreasureDataFixer.fix(); // <-- TODO could this move to Config Load event?
				TreasureSavedData.get((Level) event.getLevel());
				isLoaded = true;
			}
		} else {
			if (!isClientLoaded) {
				TreasureDataFixer.fix();
				isClientLoaded = true;
			}
		}
	}


	public static boolean isServerLoaded() {
		return isLoaded;
	}

	public static boolean isClientLoaded() {
		return isClientLoaded;
	}
}
