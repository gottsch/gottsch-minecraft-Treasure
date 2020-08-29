/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public class TreasureCommands {

	@SubscribeEvent
	public static void onServerStarting(FMLServerStartingEvent event) {
		SpawnChestCommand.register(event.getCommandDispatcher());
	}
}
