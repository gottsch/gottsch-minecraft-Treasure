package com.someguyssoftware.treasure2.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Simple class to count the number of ticks on the client; used for animation purposes
 * Created by TGG on Mar 10, 2020.
 */
public class AnimationTickCounter {
	private static long totalElapsedTicksInGame = 0;

	public static long getTotalElapsedTicksInGame() {
		return totalElapsedTicksInGame;
	}

	@SubscribeEvent
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (!Minecraft.getInstance().isGamePaused()) {
				totalElapsedTicksInGame++;
			}
		}
	}
}
