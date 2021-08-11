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
package com.someguyssoftware.treasure2.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * 
 * @author Mark Gottschling on Aug 7, 2021
 *
 */
public class WitherMistMessageHandlerOnServer {

	/**
	 * Called when a message is received of the appropriate type.
	 * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD
	 */
	public static void onMessageReceived(final WitherMistMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if (sideReceived != LogicalSide.SERVER) {
			Treasure.LOGGER.warn("WitherMistMessageToServer received on wrong side -> {}", ctx.getDirection().getReceptionSide());
			return;
		}
		if (!message.isValid()) {
			Treasure.LOGGER.warn("WitherMessageToServer was invalid -> {}", message.toString());
			return;
		}

		// we know for sure that this handler is only used on the server side, so it is ok to assume
		//  that the ctx handler is a serverhandler, and that ServerPlayerEntity exists
		// Packets received on the client side must be handled differently!  See MessageHandlerOnClient

		final ServerPlayerEntity sendingPlayer = ctx.getSender();
		if (sendingPlayer == null) {
			Treasure.LOGGER.warn("EntityPlayerMP was null when WitherMistMessageToServer was received");
		}

		// This code creates a new task which will be executed by the server during the next tick,
		//  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
		ctx.enqueueWork(() -> processMessage(message, sendingPlayer));
	}

	// This message is called from the Server thread.
	//   It spawns a random number of the given projectile at a position above the target location
	static void processMessage(WitherMistMessageToServer message, ServerPlayerEntity sendingPlayer) {

		try {
			MinecraftServer minecraftServer = sendingPlayer.server;
			ServerPlayerEntity player = minecraftServer.getPlayerList().getPlayer(UUID.fromString(message.getPlayerUUID()));

			if (player != null) {
				boolean isAffected = false;
				for (EffectInstance effectInstance : player.getActiveEffects()) {
					if (effectInstance.getEffect() == Effects.WITHER) {
						isAffected = true;
					}
				}
				
				// if player does not have poison effect, add it
				if (!isAffected) {
					player.addEffect(new EffectInstance(Effects.WITHER, 300, 0));
				}	
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unexpected error ->", e);
		}
	}
}
