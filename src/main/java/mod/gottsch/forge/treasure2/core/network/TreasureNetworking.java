/**
 * 
 */
package mod.gottsch.forge.treasure2.core.network;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.Optional;

import mod.gottsch.forge.treasure2.core.Treasure;
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
	public static final int CHARM_MESSAGE_ID = 16;
	public static final int MIMIC_SPAWN_TO_CLIENT = 17;

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
		
		simpleChannel.registerMessage(CHARM_MESSAGE_ID, CharmMessageToClient.class,
		           CharmMessageToClient::encode, CharmMessageToClient::decode,
		            CharmMessageHandlerOnClient::onMessageReceived,
		            Optional.of(PLAY_TO_CLIENT));
		
	    simpleChannel.registerMessage(MIMIC_SPAWN_TO_CLIENT, MimicSpawnS2C.class, 
	    		MimicSpawnS2C::encode,MimicSpawnS2C::decode, 
	    		MimicSpawnS2C::handle, Optional.of(PLAY_TO_CLIENT));

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
