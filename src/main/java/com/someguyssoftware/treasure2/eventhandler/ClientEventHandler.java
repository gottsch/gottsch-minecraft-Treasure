/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import java.util.Map;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.FlameParticleFactory;
import com.someguyssoftware.treasure2.particle.MistParticleData;
import com.someguyssoftware.treasure2.particle.MistParticleFactory;
import com.someguyssoftware.treasure2.particle.TreasureParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Mark Gottschling on Jan 31, 2021
 *
 */
public class ClientEventHandler {
	private final Map<ResourceLocation, IParticleFactory<?>> factories = new java.util.HashMap<>();
	public static final ClientEventHandler clientEventHandler = new ClientEventHandler();
	
	@SubscribeEvent
	public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
		Treasure.LOGGER.info("registering particle factory");
		System.out.println("registering particle factory");
		Treasure.LOGGER.info("particle type -> {}", TreasureParticles.mistParticleType.getRegistryName());
		Minecraft.getInstance().particles.registerFactory(TreasureParticles.mistParticleType, sprite -> new MistParticleFactory(sprite));
		Minecraft.getInstance().particles.registerFactory(TreasureParticles.flameParticleType, sprite -> new FlameParticleFactory(sprite));
//		Minecraft.getInstance().particles.registerFactory(DeferredRegistration.HEART_CRYSTAL_PARTICLE.get(), new SHParticle.Factory(Color.FIREBRICK)); // ??
		clientEventHandler.factories.put(Registry.PARTICLE_TYPE.getKey(TreasureParticles.mistParticleType), new MistParticleFactory(null));
		Treasure.LOGGER.info("verifying particle factory registration -> {}", clientEventHandler.factories.get(Registry.PARTICLE_TYPE.getKey(TreasureParticles.mistParticleType)));
		MistParticleData mistParticleData = new MistParticleData(new Coords(0,0,0));
		Treasure.LOGGER.info("verifying particle factory registration2 -> {}", clientEventHandler.factories.get(Registry.PARTICLE_TYPE.getKey(mistParticleData.getType())));
	}
}
