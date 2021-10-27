/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
		  Treasure.logger.debug("received charm message -> {}", message);
		  try {
	        EntityPlayer player = worldClient.getPlayerEntityByName(message.getPlayerName());

	        if (player != null) {
//	        	Treasure.logger.debug("valid player -> {}", message.getPlayerName());
	        	// check hands first
	        	if (message.getHand() != null) {
		        	Treasure.logger.debug("valid hand -> {}", message.getHand());
	        		// get the item for the hand
	        		ItemStack heldItemStack = player.getHeldItem(message.getHand());
	        		// determine what is being held in hand
	        		if (heldItemStack != null) {
	        			Treasure.logger.debug("holding item -> {}", heldItemStack.getItem().getRegistryName());
	        			if (heldItemStack.hasCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null)) {
	        				Treasure.logger.debug("has pouch cap");
	        				// pouch - get item from slot
	        				if (message.getSlot() != null && message.getSlot() > -1) {
	        					IItemHandler pouchCap = heldItemStack.getCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null);
	        					ItemStack charmedItemStack = pouchCap.getStackInSlot(message.getSlot());
	        					if (heldItemStack.getItem() instanceof ICharmed) {
	        						updateCharms(charmedItemStack, message, charmedItemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null));
	        					}
	        					else if (heldItemStack.getItem() instanceof ICharmable) {
	        						updateCharms(charmedItemStack, message, charmedItemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null));
	        					}
	        				}
	        			}
	        			else if (heldItemStack.getItem() instanceof ICharmed) {
	        				Treasure.logger.debug("has charmED cap");
		        			updateCharms(heldItemStack, message, heldItemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null));
		        		}
	        			else if (heldItemStack.getItem() instanceof ICharmable) {
	        				Treasure.logger.debug("has charmABLE cap");
		        			updateCharms(heldItemStack, message, heldItemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null));
		        		}
	        		}
	        	}
	        	else {
	        		ItemStack stack = player.inventory.getStackInSlot(message.getSlot());
	        		if (stack.getItem() instanceof ICharmable) {
	        			updateCharms(stack, message, stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null));
	        		}
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
	   * @param capability
	   */
	private void updateCharms(ItemStack heldItemStack, CharmMessageToClient message, ICharmInventoryCapability capability) {
		// get the charm that is being sent
		ResourceLocation charmName = new ResourceLocation(message.getCharmName());
		// cycle through the charm states to find the named charm
		for(ICharmEntity entity : capability.getCharmEntities()) {
			if (entity.getCharm().getName().equals(charmName)) {
				entity.update(message.getEntity());
				if (entity.getValue() <= 0.0) {
					// TODO should each charm have it's own way of checking empty?
					capability.getCharmEntities().remove(entity);
				}
				break;
			}
		}
	}
}
