/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public class TreasureCommands {

	@SubscribeEvent
	public static void onServerStarting(RegisterCommandsEvent event) {
		SpawnChestCommand.register(event.getDispatcher());
		SpawnPitCommand.register(event.getDispatcher());
		SpawnWellCommand.register(event.getDispatcher());
		SpawnRuinsCommand.register(event.getDispatcher());
		SpawnProximitySpawnerCommand.register(event.getDispatcher());
		SpawnCharmCommand.register(event.getDispatcher());
	}
}
