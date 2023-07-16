/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Aug 8, 2021
 *
 */
public class SpanishMossParticle extends AbstractMistParticle {

	protected SpanishMossParticle(ClientLevel level, double x, double y, double z,	SpriteSet spriteSet, double xd, double yd, double zd) {
		super(level, x, y, z);
		this.pickSprite(spriteSet);
		init();
	}
	
	@Override
	public float getMistGravity() {
		return 0.0001F;
	}

	@Override
	public float getMistAlpha() {
		return DEFAULT_PARTICLE_ALPHA;
	}

	@Override
	public float getMaxSize() {
		return 2.5F;
	}
	
	@Override
	public float getStartSize() {
		return 1F;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;
		
		/**
		 * 
		 * @param sprite
		 */
		public Provider(SpriteSet sprite) {
			this.spriteSet = sprite;
		}
		
		/**
		 * 
		 */
		@Override
		public Particle createParticle(SimpleParticleType data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SpanishMossParticle particle = new SpanishMossParticle(world, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
			return particle;
		}
	}
}
