/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.network;

import java.util.UUID;
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

/**
 * 
 * @author Mark Gottschling on Aug 7, 2021
 *
 */
public class PoisonMistMessageToServer {
	private String playerUUID;
	private boolean valid;
	
	/**
	 * 
	 */
	private PoisonMistMessageToServer() {
		valid = false;
	}
	
	/**
	 * 
	 * @param playerUUID
	 */
	public PoisonMistMessageToServer(String playerUUID) {
		setPlayerUUID(playerUUID);
		valid = true;
	}
	
	/**
	 * 
	 * @param buf
	 * @return
	 */
	public static PoisonMistMessageToServer decode(FriendlyByteBuf buf) {
		PoisonMistMessageToServer message = new PoisonMistMessageToServer();
		try {
			message.setPlayerUUID(buf.readUtf());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred attempting to read message: ", e);
			return message;
		}
		message.setValid( true);
		return message;
	}
	
	/**
	 * 
	 * @param buf
	 */
	public void encode(FriendlyByteBuf buf) {
		if (!isValid()) {
			return;
		}
		buf.writeUtf(getPlayerUUID());
	}
	
	/**
	 * 
	 * @param message
	 * @param ctxSupplier
	 */
	public static void handle(final PoisonMistMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();

		if (sideReceived != LogicalSide.SERVER) {
			Treasure.LOGGER.warn("PoisonMistMessageToServer received on wrong side -> {}", ctx.getDirection().getReceptionSide());
			return;
		}
		if (!message.isValid()) {
			Treasure.LOGGER.warn("PoisonMessageToServer was invalid -> {}", message.toString());
			return;
		}
		final ServerPlayer sendingPlayer = ctx.getSender();
		if (sendingPlayer == null) {
			Treasure.LOGGER.warn("PlayerEntityMP was null when PoisonMistMessageToServer was received");
		}

		// This code creates a new task which will be executed by the server during the next tick,
		//  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
		ctx.enqueueWork(() -> processMessage(message, sendingPlayer));
		
		ctx.setPacketHandled(true);
	}
	
	/**
	 * 
	 * @param message
	 * @param sendingPlayer
	 */
	static void processMessage(PoisonMistMessageToServer message, ServerPlayer sendingPlayer) {

		try {
			MinecraftServer minecraftServer = sendingPlayer.server;
			ServerPlayer player = minecraftServer.getPlayerList().getPlayer(UUID.fromString(message.getPlayerUUID()));

			if (player != null) {
				boolean isAffected = false;
				for (MobEffectInstance effectInstance : player.getActiveEffects()) {
					if (effectInstance.getEffect() == MobEffects.POISON) {
						isAffected = true;
					}
				}
				
				// if player does not have poison effect, add it
				if (!isAffected) {
					player.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 0));
				}	
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unexpected error ->", e);
		}
	}
	
	public String getPlayerUUID() {
		return playerUUID;
	}
	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean messageIsValid) {
		this.valid = messageIsValid;
	}
}
