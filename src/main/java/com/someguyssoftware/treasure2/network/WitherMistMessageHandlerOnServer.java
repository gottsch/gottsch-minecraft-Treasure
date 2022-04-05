/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class WitherMistMessageHandlerOnServer implements IMessageHandler<WitherMistMessageToServer, IMessage> {
	  /**
	   * Called when a message is received of the appropriate type.
	   * CALLED BY THE NETWORK THREAD
	   * @param message The message
	   */
	@Override
	public IMessage onMessage(WitherMistMessageToServer message, MessageContext ctx) {
		if (ctx.side != Side.SERVER) {
			Treasure.LOGGER.error("WitherMistMessageToServer received on wrong side -> {}", ctx.side);
		      return null;
		    }
		    if (!message.isMessageValid()) {
		    	Treasure.LOGGER.warn("WitherMistMessageToServer was invalid -> {}", message.toString());
		      return null;
		    }
		    
		    // we know for sure that this handler is only used on the server side, so it is ok to assume
		    //  that the ctx handler is a serverhandler, and that WorldServer exists.
		    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

		    final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
		    if (sendingPlayer == null) {
		      Treasure.LOGGER.warn("EntityPlayerMP was null when WitherMistMessageToServer was received.");
		      return null;
		    }
		    
		    // This code creates a new task which will be executed by the server during the next tick,
		    //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section
		    //      this.theProfiler.startSection("jobs");
		    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
		    final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
		    playerWorldServer.addScheduledTask(new Runnable() {
		      public void run() {
		        processMessage(message, sendingPlayer);
		      }
		    });
		    
		    return null;
	}

	  /*
	   *  This message is called from the Server thread.
	   */
	  void processMessage(WitherMistMessageToServer message, EntityPlayerMP sendingPlayer) {
		  try {
	        MinecraftServer minecraftServer = sendingPlayer.mcServer;
	        EntityPlayerMP player = minecraftServer.getPlayerList().getPlayerByUsername(message.getPlayerName());
	        
	        if (player != null) {
		        PotionEffect potionEffect = player.getActivePotionEffect(MobEffects.WITHER);
		    	// if player does not have Wither effect, add it
		    	if (potionEffect == null) {
		    		player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 0));
		    		Treasure.LOGGER.debug("Wither potion effect is null, should be adding....");
		    	}	
	        }
		  }
		  catch(Exception e) {
			  Treasure.LOGGER.error("Unexpected error ->", e);
		  }
	  }
}
