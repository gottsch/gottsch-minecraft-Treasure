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

import java.util.List;
import java.util.UUID;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Derived from MinecraftByExample by The Grey Ghost.
 * @author Mark Gottschling on Apr 28, 2020
 *
 */
public class CharmMessageHandlerOnClient implements IMessageHandler<CharmMessageToClient, IMessage> {
	
	  /**
	   * Called when a message is received of the appropriate type.
	   * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD
	   * @param message The message
	   */
	@Override
	public IMessage onMessage(CharmMessageToClient message, MessageContext ctx) {
		if (ctx.side != Side.CLIENT) {
			Treasure.LOGGER.error("CharmedItemMessageToClient received on wrong side -> {}", ctx.side);
		      return null;
		    }
		    if (!message.isMessageValid()) {
		    	Treasure.LOGGER.warn("CharmedItemMessageToClient was invalid -> {}", message.toString());
		      return null;
		    }
		    
		    // we know for sure that this handler is only used on the client side, so it is ok to assume
		    //  that the ctx handler is a client, and that Minecraft exists.
		    // Packets received on the server side must be handled differently!  See MessageHandlerOnServer
		    
		    // This code creates a new task which will be executed by the client during the next tick,
		    //  for example see Minecraft.runGameLoop() , just under section
		    //    this.mcProfiler.startSection("scheduledExecutables");
		    //  In this case, the task is to call messageHandlerOnClient.processMessage(worldclient, message)
		    Minecraft minecraft = Minecraft.getMinecraft();
		    final WorldClient worldClient = minecraft.world;
		    minecraft.addScheduledTask(new Runnable() {
		      public void run() {
		        processMessage(worldClient, message);
		      }
		    });
		    return null;
	}

	  /*
	   *  This message is called from the Server thread.
	   */
	  void processMessage(WorldClient worldClient, CharmMessageToClient message) {
		  Treasure.LOGGER.debug("received charm message -> {}", message);
		  try {
//	        EntityPlayer player = worldClient.getPlayerEntityByName(message.getPlayerName());
	        EntityPlayer player = worldClient.getPlayerEntityByUUID(UUID.fromString(message.getPlayerName()));
	        
	        if (player != null) {
//	        	Treasure.logger.debug("valid player -> {}", message.getPlayerName());
	        	// check hands first
	        	if (message.getHand() != null) {
//		        	Treasure.logger.debug("valid hand -> {}", message.getHand());
	        		// get the item for the hand
	        		ItemStack heldItemStack = player.getHeldItem(message.getHand());
	        		// determine what is being held in hand
	        		if (heldItemStack != null) {
//	        			Treasure.logger.debug("holding item -> {}", heldItemStack.getItem().getRegistryName());
	        			if (heldItemStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
	        				updateCharms(heldItemStack, message, heldItemStack.getCapability(TreasureCapabilities.CHARMABLE, null));
	        			}
	        		}
	        	}
	        	else if (BaublesIntegration.BAUBLES_MOD_ID.equals(message.getSlotProviderId())) {
//	        		Treasure.logger.debug("it is a baubles slot provider");
	        		ItemStack stack = BaublesIntegration.getStackInSlot(player, message.getSlot());
	        		
//	        		if (stack != null) Treasure.logger.debug("item in baubles slot -> {} -> {}", message.getSlot(), stack.getItem().getRegistryName());
	        		
	        		if (stack != null && stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
//		        		Treasure.logger.debug("baubles item has charmable cap");
	        			updateCharms(stack, message, stack.getCapability(TreasureCapabilities.CHARMABLE, null));
	        		}
	        	}
	        	// hotbar
	        	else {
	        		ItemStack stack = player.inventory.getStackInSlot(message.getSlot());
	        		if (stack != null /*&& stack.getItem() instanceof ICharmable*/) {
	        			if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
	        				updateCharms(stack, message, stack.getCapability(TreasureCapabilities.CHARMABLE, null));
	        			}
	        		}
	        	}	        	
	        }
		  }
		  catch(Exception e) {
			  Treasure.LOGGER.error("Unexpected error ->", e);
		  }
	  }

	  /**
	   * 
	   * @param itemStack
	   * @param message
	   * @param capability
	   */
	private void updateCharms(ItemStack itemStack, CharmMessageToClient message, ICharmableCapability capability) {
		// get the charm that is being sent
		ResourceLocation charmName = new ResourceLocation(message.getCharmName());
		// cycle through the charm states to find the named charm
		List<ICharmEntity> entityList = (List<ICharmEntity>) capability.getCharmEntities().get(message.getInventoryType());
		if (entityList != null && !entityList.isEmpty() && entityList.size() > message.getIndex()) {
			ICharmEntity entity = entityList.get(message.getIndex());
//    		Treasure.logger.debug("looking for charm -> {} at index -> {}", entity, message.getIndex());
			if (entity != null && entity.getCharm().getName().equals(charmName)) {
//	        	Treasure.logger.debug("found charm, updating...");
				// update entity properties
				entity.update(message.getEntity());
				
				// NOTE yes, remove innate charms from Adornments - they can't be recharged
				if (message.getInventoryType() == InventoryType.INNATE && entity.getMana() <= 0.0) {
					capability.remove(message.getInventoryType(), message.getIndex());
				}
				// TODO probably need to remove imbue as well
				

				// update Durability 
				if (itemStack.hasCapability(TreasureCapabilities.DURABILITY, null)) {
					itemStack.setItemDamage(message.getItemDamage());
				}
//				break;
			}
		}
	}
}
