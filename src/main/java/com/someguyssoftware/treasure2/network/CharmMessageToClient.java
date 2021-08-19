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

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.CharmEntity;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class CharmMessageToClient {
	private boolean valid;
	private String playerName;
	private String charmName;
	private ICharmEntity entity;	
	private Hand hand;
	private String slot;
	private String slotProviderId;
	private InventoryType inventoryType;
	
	/**
	 * 
	 * @param playerName
	 */
	public CharmMessageToClient(String playerName, ICharmEntity instance, Hand hand, String slot, String slotProviderId, InventoryType inventoryType) {
		valid = true;
		this.playerName = playerName;
		this.charmName = instance.getCharm().getName().toString();
		this.entity = instance;
		this.hand = hand;
		this.slot = slot;
		this.slotProviderId = slotProviderId;
		this.inventoryType = inventoryType;
	}
	
	/**
	 * 
	 */
	public CharmMessageToClient() {
		valid = false;
	}
	
	/**
	 * 
	 * @param buf
	 * @return
	 */
	public static CharmMessageToClient decode(PacketBuffer buf) {
		CharmMessageToClient message = new CharmMessageToClient();
		try {
			message.setPlayerName(buf.readUtf());
			message.setCharmName(buf.readUtf());
			double value = buf.readDouble();
			int duration = buf.readInt();
			double percent = buf.readDouble();
			Optional<ICharm> optionalCharm = TreasureCharmRegistry.get(ModUtils.asLocation(message.getCharmName()));
			if (!optionalCharm.isPresent()) {
				throw new RuntimeException(String.format("Unable to find charm %s in registry.", message.getCharmName()));
			}
			ICharmEntity entity = new CharmEntity();
			entity.setCharm(optionalCharm.get());
			entity.setDuration(duration);
			entity.setPercent(percent);
			entity.setValue(value);
			message.setEntity(entity);
			String handAsString = buf.readUtf();
			if (!handAsString.isEmpty()) {
				message.hand = Hand.valueOf(handAsString);
			}
			message.setSlot(buf.readUtf());		
			message.setSlotProviderId(buf.readUtf());
			String inventoryType = buf.readUtf();
			if (!inventoryType.isEmpty()) {
				message.setInventoryType(InventoryType.valueOf(inventoryType.toUpperCase()));
			}
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
		buf.writeUtf(getPlayerName());
		buf.writeUtf(getCharmName());
		buf.writeDouble(entity.getValue());
		buf.writeInt(entity.getDuration());
		buf.writeDouble(entity.getPercent());
		String handAsString = "";
		if (hand != null) {
			handAsString = hand.name();
		}
		buf.writeUtf(handAsString);
		buf.writeUtf(slot);
		buf.writeUtf(slotProviderId);
		buf.writeUtf(inventoryType.name().toLowerCase());
	}
	

	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean messageIsValid) {
		this.valid = messageIsValid;
	}

	public String getCharmName() {
		return charmName;
	}

	public void setCharmName(String charmName) {
		this.charmName = charmName;
	}

	public ICharmEntity getEntity() {
		return entity;
	}

	public void setEntity(ICharmEntity data) {
		this.entity = data;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getSlotProviderId() {
		return slotProviderId;
	}

	public void setSlotProviderId(String slotProviderId) {
		this.slotProviderId = slotProviderId;
	}

	public InventoryType getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}
}
