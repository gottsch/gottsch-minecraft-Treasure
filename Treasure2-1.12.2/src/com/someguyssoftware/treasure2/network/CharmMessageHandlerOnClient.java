/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.item.charm.ICharmState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;

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
			Treasure.logger.error("CharmedItemMessageToClient received on wrong side -> {}", ctx.side);
		      return null;
		    }
		    if (!message.isMessageValid()) {
		    	Treasure.logger.warn("CharmedItemMessageToClient was invalid -> {}", message.toString());
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
//		  Treasure.logger.debug("received charm message -> {}", message);
		  try {
	        EntityPlayer player = worldClient.getPlayerEntityByName(message.getPlayerName());

	        if (player != null) {
//	        	Treasure.logger.debug("valid player -> {}", message.getPlayerName());
	        	// check hands first
	        	if (message.getHand() != null) {
//		        	Treasure.logger.debug("valid hand -> {}", message.getHand());
	        		// get the item for the hand
	        		ItemStack heldItemStack = player.getHeldItem(message.getHand());
	        		// determine what is being held in hand
	        		if (heldItemStack != null) {
	        			if (heldItemStack.hasCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null)) {
	        				// pouch - get item from slot
	        				if (message.getSlot() != null && message.getSlot() > -1) {
	        					IItemHandler pouchCap = heldItemStack.getCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null);
	        					ItemStack charmedItemStack = pouchCap.getStackInSlot(message.getSlot());
	        					updateCharms(charmedItemStack, message);
	        				}
	        			}
		        		else if (heldItemStack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
		        			updateCharms(heldItemStack, message);
		        		}
	        		}
	        	}
	        	else {
	        		// TODO need to add hotbar index if not held to message
	        		// TODO find same item in pouch
	        	}
	        	
	        }
		  }
		  catch(Exception e) {
			  Treasure.logger.error("Unexpected error ->", e);
		  }
	  }

	  /**
	   * 
	   * @param heldItemStack
	   * @param message
	   */
	private void updateCharms(ItemStack heldItemStack, CharmMessageToClient message) {
		ICharmCapability heldItemCaps = heldItemStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
		// get the charm that is being sent
		String charmName = message.getCharmName();
		// cycle through the charm states to find the named charm
		for(ICharmState state : heldItemCaps.getCharmStates()) {
			if (state.getCharm().getName().equals(charmName)) {
//	        	Treasure.logger.debug("found charm, updating vitals to -> {}", message.getVitals());
				// update vitals
				state.setVitals(message.getVitals());
			}
		}
	}
}
