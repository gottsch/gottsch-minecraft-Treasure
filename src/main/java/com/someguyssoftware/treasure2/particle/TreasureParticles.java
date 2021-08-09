/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.data.CollidingParticleType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Mark Gottschling on Jan 29, 2021
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TreasureParticles {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Treasure.MODID);

	public static final RegistryObject<BasicParticleType> MIST_PARTICLE_TYPE = PARTICLE_TYPES.register("mist_particle_type", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> BILLOWING_MIST_PARTICLE_TYPE = PARTICLE_TYPES.register("billowing_mist_particle_type", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> BOUND_SOUL_TYPE = PARTICLE_TYPES.register("bound_soul_mist_particle_type", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> SPANISH_MOSS_MIST_PARTICLE_TYPE = PARTICLE_TYPES.register("spanish_moss_mist_particle_type", () -> new BasicParticleType(false));
	public static final RegistryObject<CollidingParticleType> POISON_MIST_PARTICLE_TYPE = PARTICLE_TYPES.register("poison_mist_particle_type", () -> new CollidingParticleType(null, CollidingParticleType.DESERIALIZER));
	public static final RegistryObject<CollidingParticleType> WITHER_MIST_PARTICLE_TYPE = PARTICLE_TYPES.register("wither_mist_particle_type", () -> new CollidingParticleType(null, CollidingParticleType.DESERIALIZER));
	
	
	/**
	 * 
	 * @param event
	 */
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent event) {
		@SuppressWarnings("resource")
		ParticleManager particles = Minecraft.getInstance().particleEngine;
		
		particles.register(BOUND_SOUL_TYPE.get(), BoundSoulMistParticle.Factory::new);
		particles.register(MIST_PARTICLE_TYPE.get(), MistParticleFactory::new);
		particles.register(BILLOWING_MIST_PARTICLE_TYPE.get(), BillowingMistParticle.Factory::new);
		particles.register(SPANISH_MOSS_MIST_PARTICLE_TYPE.get(), SpanishMossMistParticle.Factory::new);
		particles.register(POISON_MIST_PARTICLE_TYPE.get(), PoisonMistParticle.Factory::new);
		particles.register(WITHER_MIST_PARTICLE_TYPE.get(), WitherMistParticle.Factory::new);
	}
}
