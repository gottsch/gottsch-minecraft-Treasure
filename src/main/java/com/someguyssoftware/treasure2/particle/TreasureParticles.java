/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Mark Gottschling on Jan 29, 2021
 *
 */
public class TreasureParticles {

	public static ParticleType<MistParticleData> mistParticleType;

		@SubscribeEvent
		public static void onParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) {
			Treasure.LOGGER.info("registering particle type");
			System.out.println("registering particle type");
			mistParticleType = new MistParticleType();
			mistParticleType.setRegistryName("treasure2:mist_particle_type");
			event.getRegistry().register(mistParticleType);
		}
}
