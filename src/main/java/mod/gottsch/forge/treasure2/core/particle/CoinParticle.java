/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on May 30, 2023
 *
 */
public class CoinParticle extends TextureSheetParticle {
	private final SpriteSet sprites;
	
	/**
	 * 
	 * @param level
	 * @param x
	 * @param y
	 * @param z
	 * @param spriteSet
	 * @param xd
	 * @param yd
	 * @param zd
	 */
	protected CoinParticle(ClientLevel level, double x, double y, double z,
			SpriteSet spriteSet, double xd, double yd, double zd) {
		
		super(level, x, y, z, xd, yd, zd);
		this.sprites = spriteSet;
		
		this.friction = 0.8F;
		// velocity
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		// size
		this.quadSize *= 0.85F;
		// life in ticks
		this.lifetime = 30;
		// set the image
		this.setSpriteFromAge(spriteSet);
		
		// use full color
		this.rCol = 1f;
		this.gCol = 1f;
		this.bCol = 1f;
	}

	@Override
	public void tick() {
//		super.tick();
	      this.xo = this.x;
	      this.yo = this.y;
	      this.zo = this.z;
	      if (this.age++ >= this.lifetime) {
	         this.remove();
	      } else {
	         this.yd -= (double)this.gravity;
	         this.move(this.xd, this.yd, this.zd);
	         this.setSpriteFromAge(this.sprites);
	      }
		fadeOut();
	}
	
	private void fadeOut() {
		this.alpha=  (-(1/(float)lifetime) * age + 1);
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CoinParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
