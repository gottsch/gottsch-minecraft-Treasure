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
public class PoisonMistMessageHandlerOnServer implements IMessageHandler<PoisonMistMessageToServer, IMessage> {
	  /**
	   * Called when a message is received of the appropriate type.
	   * CALLED BY THE NETWORK THREAD
	   * @param message The message
	   */
	@Override
	public IMessage onMessage(PoisonMistMessageToServer message, MessageContext ctx) {
		if (ctx.side != Side.SERVER) {
			Treasure.logger.error("PoisonMistMessageToServer received on wrong side -> {}", ctx.side);
		      return null;
		    }
		    if (!message.isMessageValid()) {
		    	Treasure.logger.warn("PoisonMistMessageToServer was invalid -> {}", message.toString());
		      return null;
		    }
		    
		    // we know for sure that this handler is only used on the server side, so it is ok to assume
		    //  that the ctx handler is a serverhandler, and that WorldServer exists.
		    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

		    final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
		    if (sendingPlayer == null) {
		      Treasure.logger.warn("EntityPlayerMP was null when PoisonMistMessageToServer was received.");
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
	  void processMessage(PoisonMistMessageToServer message, EntityPlayerMP sendingPlayer) {
		  try {
	        MinecraftServer minecraftServer = sendingPlayer.mcServer;
	        EntityPlayerMP player = minecraftServer.getPlayerList().getPlayerByUsername(message.getPlayerName());
	        
	        if (player != null) {
		        PotionEffect potionEffect = player.getActivePotionEffect(MobEffects.POISON);
		    	// if player does not have poison effect, add it
		    	if (potionEffect == null) {
		    		player.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 0));
		    		Treasure.logger.debug("poison potion effect is null, should be adding....");
		    	}	
	        }
		  }
		  catch(Exception e) {
			  Treasure.logger.error("Unexpected error ->", e);
		  }
	  }
}
