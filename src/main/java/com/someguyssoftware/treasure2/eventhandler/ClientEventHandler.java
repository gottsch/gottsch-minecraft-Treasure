/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.MistParticleFactory;
import com.someguyssoftware.treasure2.particle.TreasureParticles;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Mark Gottschling on Jan 31, 2021
 *
 */
public class ClientEventHandler {
	@SubscribeEvent
	public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
		Treasure.LOGGER.info("registering particle factory");
		System.out.println("registering particle factory");
		Minecraft.getInstance().particles.registerFactory(TreasureParticles.mistParticleType, sprite -> new MistParticleFactory(sprite));
	}
}
