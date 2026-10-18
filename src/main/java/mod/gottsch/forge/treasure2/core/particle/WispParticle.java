/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A wisp's mote: a small, full-bright, pale green-white glow that drifts slowly upward, wavers, and
 * fades in and out over a few seconds.
 *
 * @author Mark Gottschling on 10/18/2026
 */
@OnlyIn(Dist.CLIENT)
public class WispParticle extends TextureSheetParticle {
	private final float maxAlpha;
	private final float phase;

	protected WispParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
		super(level, x, y, z, 0, 0, 0);
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		this.friction = 0.98F;
		this.gravity = 0F;
		this.hasPhysics = false;
		this.lifetime = 60 + random.nextInt(60);
		this.quadSize = 0.06F + random.nextFloat() * 0.05F;
		float shade = 0.85F + random.nextFloat() * 0.15F;
		setColor(0.72F * shade, 0.92F * shade, 0.78F * shade);
		this.maxAlpha = 0.55F + random.nextFloat() * 0.3F;
		this.phase = random.nextFloat() * Mth.TWO_PI;
		this.alpha = 0F;
	}

	@Override
	public void tick() {
		super.tick();
		// waver sideways a little
		float t = (age + phase * 10) * 0.12F;
		xd += Mth.sin(t) * 0.0006F;
		zd += Mth.cos(t * 0.8F) * 0.0006F;
		// fade in over the first quarter, out over the last half
		float life = (float) age / lifetime;
		alpha = maxAlpha * (life < 0.25F ? life / 0.25F : life > 0.5F ? (1F - life) / 0.5F : 1F);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	protected int getLightColor(float partialTick) {
		return 0xF000F0;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
			WispParticle particle = new WispParticle(level, x, y, z, xd, yd, zd);
			particle.pickSprite(sprites);
			return particle;
		}
	}
}
