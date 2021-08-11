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

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.network.PacketBuffer;

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
	public static PoisonMistMessageToServer decode(PacketBuffer buf) {
		PoisonMistMessageToServer message = new PoisonMistMessageToServer();
		try {
			message.setPlayerUUID(buf.readUtf());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("An error occurred attempting to read message: ", e);
			return message;
		}
		message.setValid( true);
		return message;
	}
	
	/**
	 * 
	 * @param buf
	 */
	public void encode(PacketBuffer buf) {
		if (!isValid()) {
			return;
		}
		buf.writeUtf(getPlayerUUID());
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
