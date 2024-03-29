/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.particle;

import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 26, 2022
 *
 */
public class TreasureParticles {
	public static final RegistryObject<SimpleParticleType> SPANISH_MOSS_PARTICLE = Registration.PARTICLES.register("spanish_moss_particle", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> MIST_PARTICLE = Registration.PARTICLES.register("mist_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BILLOWING_MIST_PARTICLE = Registration.PARTICLES.register("billowing_mist_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<CollidingParticleType> POISON_MIST_PARTICLE = Registration.PARTICLES.register("poison_mist_particle", () -> new CollidingParticleType(null, CollidingParticleType.DESERIALIZER));	
	public static final RegistryObject<CollidingParticleType> WITHER_MIST_PARTICLE = Registration.PARTICLES.register("wither_mist_particle", () -> new CollidingParticleType(null, CollidingParticleType.DESERIALIZER));
	
	public static final RegistryObject<SimpleParticleType> COPPER_COIN_PARTICLE = Registration.PARTICLES.register("copper_coin_particle", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SILVER_COIN_PARTICLE = Registration.PARTICLES.register("silver_coin_particle", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> GOLD_COIN_PARTICLE = Registration.PARTICLES.register("gold_coin_particle", () -> new SimpleParticleType(true));
	
	
	public static void register(IEventBus bus) {
		Registration.registerParticles(bus);
	}
}
