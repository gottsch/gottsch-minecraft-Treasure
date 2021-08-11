/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
/**
 * @author Mark Gottschling on Aug 7, 2021
 *
 */
public class TreasureNetworking {
	public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
	public static final int POISON_MIST_MESSAGE_ID = 14;
	public static final int WITHER_MIST_MESSAGE_ID = 15;

	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Treasure.MODID, "treasure_channel");

	public static SimpleChannel simpleChannel;    // used to transmit your network messages

	/**
	 * 
	 * @param event
	 */
	public static void common(final FMLCommonSetupEvent event) {
		// register the channel
		simpleChannel = NetworkRegistry.newSimpleChannel(CHANNEL_NAME, () -> MESSAGE_PROTOCOL_VERSION,
	            TreasureNetworking::isThisProtocolAcceptedByClient,
	            TreasureNetworking::isThisProtocolAcceptedByServer);
		
		// register the messages
		simpleChannel.registerMessage(POISON_MIST_MESSAGE_ID, PoisonMistMessageToServer.class,
	            PoisonMistMessageToServer::encode, PoisonMistMessageToServer::decode,
	            PoisonMistMessageHandlerOnServer::onMessageReceived,
	            Optional.of(PLAY_TO_SERVER));
		
		simpleChannel.registerMessage(WITHER_MIST_MESSAGE_ID, WitherMistMessageToServer.class,
	           WitherMistMessageToServer::encode, WitherMistMessageToServer::decode,
	            WitherMistMessageHandlerOnServer::onMessageReceived,
	            Optional.of(PLAY_TO_SERVER));
	}
	
	/**
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
		return MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
	}
	
	/**
	 * 
	 * @param protocolVersion
	 * @return
	 */
	public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
		return MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
	}
}
