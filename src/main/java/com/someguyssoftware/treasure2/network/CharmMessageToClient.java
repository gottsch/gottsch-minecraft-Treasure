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

import java.util.Objects;
import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.ICooldownCharmEntity;
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
	private boolean valid;
	private String playerName;
	private String charmName;
	private ICharmEntity entity;
	// location properties of charm(s) - who, what slot
	private EnumHand hand;
	private Integer slot;
	private String slotProviderId;
	private InventoryType inventoryType;
	private int index;
	private int itemDamage;
	
	/**
	 * 
	 * @param uuid
	 * @param context
	 */
	public CharmMessageToClient(String playerName, CharmContext context) {
		valid = true;
		this.playerName = playerName;
		this.charmName = context.getEntity().getCharm().getName().toString();
		this.entity = context.getEntity();
		this.hand = context.getHand();
		this.slotProviderId = context.getSlotProviderId();
		this.slot = context.getSlot();
		this.inventoryType = context.getType();
		this.index = context.getIndex();
		this.itemDamage = context.getItemStack().getItemDamage();
	}
		
	/**
	 * 
	 */
	public CharmMessageToClient() {
		valid = false;
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
	    	// create the charm entity
	    	Optional<ICharm> optionalCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));
	    	if (!optionalCharm.isPresent()) {
	    		throw new RuntimeException(String.format("Unable to find charm %s in registry.", charmName));
	    	}
	    	entity = optionalCharm.get().createEntity();
	    	entity.setCharm(optionalCharm.get());
	    	// get entity data
	    	entity.setMana(buf.readDouble());	    	
	    	// get class specific entity data
	    	if (entity instanceof ICooldownCharmEntity) {
	    		((ICooldownCharmEntity) entity).setCooldownEnd(buf.readDouble());
	    	}
	    	
	    	String handStr = ByteBufUtils.readUTF8String(buf);
	    	if (!handStr.isEmpty()) {
	    		this.hand = EnumHand.valueOf(handStr);
	    	}
	    	this.slot = buf.readInt();
	    	this.slotProviderId = ByteBufUtils.readUTF8String(buf);
			String inventoryType = ByteBufUtils.readUTF8String(buf);
			if (!inventoryType.isEmpty()) {
				this.setInventoryType(InventoryType.valueOf(inventoryType.toUpperCase()));
			}
			this.setIndex(buf.readInt());
			this.setItemDamage(buf.readInt());
	      } catch (RuntimeException e) {
	        Treasure.logger.error("Exception while reading CharmMessageToClient: ", e);
	        return;
	      }
	      valid = true;
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    if (!valid) {
	    	return;
	    }
	    ByteBufUtils.writeUTF8String(buf, playerName);
	    ByteBufUtils.writeUTF8String(buf, charmName);
	    
	    // write entity data
	    buf.writeDouble(entity.getMana());
	    // write specific entity data
	    if (entity instanceof ICooldownCharmEntity) {
	    	buf.writeDouble(((ICooldownCharmEntity) entity).getCooldownEnd());
	    }
	    
	    String handStr = "";
	    if (hand != null) {
	    	handStr = hand.name();
	    }
	    ByteBufUtils.writeUTF8String(buf, handStr);	    
	    buf.writeInt(this.slot);
	    ByteBufUtils.writeUTF8String(buf, Objects.toString(slotProviderId, ""));
		String typeAsString = "";
		if (inventoryType != null) {
			typeAsString = inventoryType.name().toLowerCase();
		}
		ByteBufUtils.writeUTF8String(buf, typeAsString);
		buf.writeInt(index);
		buf.writeInt(this.itemDamage);
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
		return valid;
	}

	public void setValid(boolean messageIsValid) {
		this.valid = messageIsValid;
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

	public String getSlotProviderId() {
		return slotProviderId;
	}

	public void setSlotProviderId(String slotProviderId) {
		this.slotProviderId = slotProviderId;
	}

	public boolean isValid() {
		return valid;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setCharmName(String charmName) {
		this.charmName = charmName;
	}

	public void setEntity(ICharmEntity entity) {
		this.entity = entity;
	}

	public void setHand(EnumHand hand) {
		this.hand = hand;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}
	
	public InventoryType getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "CharmMessageToClient [valid=" + valid + ", playerName=" + playerName + ", charmName=" + charmName
				+ ", entity=" + entity + ", hand=" + hand + ", slot=" + slot + ", slotProviderId=" + slotProviderId
				+ ", inventoryType=" + inventoryType + ", index=" + index + ", itemDamage=" + itemDamage + "]";
	}

	public int getItemDamage() {
		return itemDamage;
	}

	public void setItemDamage(int itemDamage) {
		this.itemDamage = itemDamage;
	}
}
