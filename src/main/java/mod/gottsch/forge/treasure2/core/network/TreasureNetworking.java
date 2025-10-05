/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.network;

import java.util.Optional;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @author Mark Gottschling on Aug 7, 2021
 *
 */
public class TreasureNetworking {
	public static final String PROTOCOL_VERSION = "1.0";
	public static final int POISON_MIST_TO_SERVER_ID = 14;
	public static final int WITHER_MIST_TO_SERVER_ID = 15;
	public static final int MIMIC_SPAWN_TO_CLIENT = 17;

	public static SimpleChannel channel;
	
	/**
	 *
	 */
	public static void register() {
		// register the channel
	    channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Treasure.MODID, "treasure_channel"))
		        .networkProtocolVersion(() -> PROTOCOL_VERSION).clientAcceptedVersions(PROTOCOL_VERSION::equals)
		        .serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

	    // TODO could combine poison + wither message into one class and have
	    // separate handler methods ie handlePoison, handleWither
		// register the messages
	    channel.registerMessage(POISON_MIST_TO_SERVER_ID, PoisonMistMessageToServer.class, 
	    		PoisonMistMessageToServer::encode, PoisonMistMessageToServer::decode,
	    		PoisonMistMessageToServer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

	    channel.registerMessage(WITHER_MIST_TO_SERVER_ID, WitherMistMessageToServer.class, 
	    		WitherMistMessageToServer::encode, WitherMistMessageToServer::decode,
	    		WitherMistMessageToServer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

	    channel.registerMessage(MIMIC_SPAWN_TO_CLIENT, MimicSpawnS2C.class, 
	    		MimicSpawnS2C::encode,MimicSpawnS2C::decode, 
	    		MimicSpawnS2C::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

}
