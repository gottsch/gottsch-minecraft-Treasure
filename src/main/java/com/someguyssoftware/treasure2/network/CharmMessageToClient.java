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
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class CharmMessageToClient implements IMessage {
	private boolean messageIsValid;
	private String charmName;
	private ICharmEntity entity;
	// location properties of charm(s) - who, what hand or what pouch slot
	private String playerName;
	private EnumHand hand;
	private Integer slot;
	
	/**
	 * 
	 * @param playerName
	 */
	public CharmMessageToClient(String playerName, ICharmEntity instance, EnumHand hand, Integer slot) {
		messageIsValid = true;
		this.playerName = playerName;
		this.charmName = instance.getCharm().getName().toString();
		this.entity = instance;
		this.hand = hand;
		if (slot == null) {
			this.slot = -1;
		}
		else {
			this.slot = slot;
		}
	}
	
	/**
	 * 
	 */
	public CharmMessageToClient() {
		messageIsValid = false;
	}
	
    // these methods may also be of use for your code:
    // for Itemstacks - ByteBufUtils.readItemStack()
    // for NBT tags ByteBufUtils.readTag();
    // for Strings: ByteBufUtils.readUTF8String();
	@Override
	public void fromBytes(ByteBuf buf) {
	    try {
	    	this.playerName = ByteBufUtils.readUTF8String(buf);
	    	this.charmName = ByteBufUtils.readUTF8String(buf);
	    	double value = buf.readDouble();
	    	int duration = buf.readInt();
	    	double percent = buf.readDouble();

	    	Optional<ICharm> optionalCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));
	    	if (!optionalCharm.isPresent()) {
	    		throw new RuntimeException(String.format("Unable to find charm %s in registry.", charmName));
	    	}
	    	entity = optionalCharm.get().createEntity();
	    	entity.setCharm(optionalCharm.get());
	    	entity.setDuration(duration);
	    	entity.setPercent(percent);
	    	entity.setValue(value);
	    	String handStr = ByteBufUtils.readUTF8String(buf);
	    	if (!handStr.isEmpty()) {
	    		this.hand = EnumHand.valueOf(handStr);
	    	}
	    	this.slot = buf.readInt();
	      } catch (RuntimeException e) {
	        Treasure.logger.error("Exception while reading CharmMessageToClient: ", e);
	        return;
	      }
	      messageIsValid = true;
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    if (!messageIsValid) {
	    	return;
	    }
	    ByteBufUtils.writeUTF8String(buf, playerName);
	    ByteBufUtils.writeUTF8String(buf, charmName);
	    buf.writeDouble(entity.getValue());
	    buf.writeInt(entity.getDuration());
	    buf.writeDouble(entity.getPercent());
	    String handStr = "";
	    if (hand != null) {
	    	handStr = hand.name();
	    }
	    ByteBufUtils.writeUTF8String(buf, handStr);	    
	    buf.writeInt(this.slot);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMessageValid() {
		if (charmName != null && playerName != null && entity != null) {
			return true;
		}
		return false;
	}

	public boolean isMessageIsValid() {
		return messageIsValid;
	}

	public void setMessageIsValid(boolean messageIsValid) {
		this.messageIsValid = messageIsValid;
	}

	public ICharmEntity getEntity() {
		return entity;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public EnumHand getHand() {
		return hand;
	}

	public Integer getSlot() {
		return slot;
	}

	public String getCharmName() {
		return charmName;
	}

	@Override
	public String toString() {
		return "CharmMessageToClient [messageIsValid=" + messageIsValid + ", charmName=" + charmName + ", entity="
				+ entity + ", playerName=" + playerName + ", hand=" + hand + ", slot=" + slot + "]";
	}
}
