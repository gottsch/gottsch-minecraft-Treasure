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
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BillowingMistParticle extends MistParticle {

	private boolean isAntiGravity = false;
	private float billowingGravity;// = -DEFAULT_PARTICLE_GRAVITY * 0.3F;

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
	public BillowingMistParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSet, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z, spriteSet, velocityZ, velocityZ, velocityZ);// , velocityX, velocityY, velocityZ);
	}

	/**
	 * 
	 */
	@Override
	public void init() {
		super.init();
		
		if (isAntiGravity) {
			billowingGravity = -DEFAULT_PARTICLE_GRAVITY * 0.3F;
		} else {
			billowingGravity = DEFAULT_PARTICLE_GRAVITY;
		}
		this.gravity = billowingGravity;
	}

	/**
	 * 
	 */
	@Override
	public void tick() {
		// save the previous location
		xo = x;
		yo = y;
		zo = z;

		// calculate the y motion if not on the ground
		this.yd -= getMistGravity(); // gravity
		
		/*
		 * if the motionY value is less than 3x the negative provided gravity value (downwards) then max it
		 * out at 3x.
		 */
		if (this.yd < -getMistGravity() * 3) {
			this.yd = -getMistGravity() * 3;
		}
		this.move(this.xd, this.yd, this.zd);

		doTransitions();

		// NOTE the movement coupled with the transitions gives the mist a "roiling" effect.

		// detect a collision while moving upwards (can't move up at all)
		if (yo == y && yd > 0) {
			// do nothing. not used for mist
		}

		if (this.age % 40 == 0) {
			isAntiGravity = !isAntiGravity;
			if (isAntiGravity) {
				billowingGravity = -DEFAULT_PARTICLE_GRAVITY * 0.3F;
			} else {
				billowingGravity = DEFAULT_PARTICLE_GRAVITY;
			}
			gravity = billowingGravity;
		}

		// increase the age and test against max age
		if (this.age++ >= this.lifetime) {
			this.remove();
		}
	}

	@Override
	public float getMistGravity() {
		return billowingGravity;
	}
	
	@Override
	public float getStartSize() {
		return 1F;
	}
	
	@Override
	public float getMaxSize() {
		return 3;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		/*
		 * contains a list of textures; choose one using either
		 * newParticle.selectSpriteRandomly(spriteSet); or newParticle.selectSpriteWithAge(spriteSet);
		 */
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
			BillowingMistParticle particle = new BillowingMistParticle(world, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
			return particle;
		}
	}
}
