/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Aug 8, 2021
 *
 */
public class SpanishMossMistParticle extends AbstractMistParticle {

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param velocityX
	 * @param velocityY
	 * @param velocityZ
	 * @param parentCoords
	 */
	public SpanishMossMistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z);
		this.xd = (Math.random() * 2.0D - 1.0D) * 0.0090200D;
		this.zd = (Math.random() * 2.0D - 1.0D) * 0.0090200D;

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
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		/**
		 * 
		 * @param sprite
		 */
		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		/**
		 * 
		 */
		@Override
		public Particle createParticle(BasicParticleType data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SpanishMossMistParticle particle = new SpanishMossMistParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(spriteSet);
			return particle;
		}
	}
}
