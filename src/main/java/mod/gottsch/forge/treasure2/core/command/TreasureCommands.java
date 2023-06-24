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
package mod.gottsch.forge.treasure2.core.command;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Mark Gottschling on Apr 30, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public class TreasureCommands {
	@SubscribeEvent
	public static void onServerStarting(RegisterCommandsEvent event) {
		SpawnChestCommand.register(event.getDispatcher());
		SpawnPitCommand.register(event.getDispatcher());
		SpawnRuinsCommand.register(event.getDispatcher());
		SpawnMarkerCommand.register(event.getDispatcher());
		SpawnWellCommand.register(event.getDispatcher());
		SpawnWitherTreeCommand.register(event.getDispatcher());
	}
}
