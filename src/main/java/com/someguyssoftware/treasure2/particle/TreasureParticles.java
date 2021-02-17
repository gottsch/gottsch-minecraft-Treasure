/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
	public static ParticleType<FlameParticleData> flameParticleType;

//	static {
//		mistParticleType = new MistParticleType();
//		mistParticleType.setRegistryName(new ResourceLocation(Treasure.MODID, "mist_particle_type"));
//	}
	
		@SubscribeEvent
		public static void onParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) {
			mistParticleType = new MistParticleType();
			mistParticleType.setRegistryName(new ResourceLocation(Treasure.MODID, "mist_particle_type"));
			flameParticleType = new FlameParticleType();
			flameParticleType.setRegistryName(new ResourceLocation(Treasure.MODID, "flame_particle_type"));
			Treasure.LOGGER.info("registering particle type -> {}", mistParticleType.getRegistryName()); 
			System.out.println("registering particle type -> " + mistParticleType.getRegistryName()); // NOTE shows up in latest.log
			event.getRegistry().register(mistParticleType);
			event.getRegistry().register(flameParticleType);
			Treasure.LOGGER.info("verifying particle registration -> {}", Registry.PARTICLE_TYPE.getKey(mistParticleType));
		}
}
