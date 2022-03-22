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

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * 
 * @author Mark
 *
 */
@Deprecated
// not needed as only the adornment item is only updated in the anvil event
public class RepairMessageToClient implements IMessage {
	private boolean  valid;
	private String playerName;
	private EnumHand hand;
	private Integer slot;
	private String slotProviderId;
	private Integer maxRepairs;

	
	public RepairMessageToClient(String playerName, EnumHand hand, Integer slot, String providerId) {
		valid = true;
		this.playerName = playerName;
		this.hand = hand;
		this.slot = slot;
		this.slotProviderId = providerId;
	}
	
	public RepairMessageToClient() {
		valid = false;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
	    if (!valid) {
	    	return;
	    }
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public EnumHand getHand() {
		return hand;
	}

	public void setHand(EnumHand hand) {
		this.hand = hand;
	}

	public Integer getSlot() {
		return slot;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}

	public String getSlotProviderId() {
		return slotProviderId;
	}

	public void setSlotProviderId(String slotProviderId) {
		this.slotProviderId = slotProviderId;
	}
}
