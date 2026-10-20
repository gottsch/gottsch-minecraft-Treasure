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
 * A wisp: a soft, full-bright, pale green-white orb, a third of a block or more across, that wanders
 * lazily on a slowly turning heading, bobs, swells and shrinks a little, and fades in and out over five
 * to eight seconds. Each one wanders its own way, so several from the same wisp drift apart.
 *
 * @author Mark Gottschling on 10/18/2026
 */
@OnlyIn(Dist.CLIENT)
public class WispParticle extends TextureSheetParticle {
	private final float maxAlpha;
	private final float phase;
	/** the way it is wandering, turned a little every tick */
	private float heading;
	private final float turn;

	protected WispParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
		super(level, x, y, z, 0, 0, 0);
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		// with WANDER this caps the drift at about half a block a second
		this.friction = 0.96F;
		this.gravity = 0F;
		this.hasPhysics = false;
		this.lifetime = 100 + random.nextInt(60);
		// big enough to read as a wisp across the clearing, not a speck
		this.quadSize = 0.3F + random.nextFloat() * 0.15F;
		float shade = 0.85F + random.nextFloat() * 0.15F;
		setColor(0.72F * shade, 0.92F * shade, 0.78F * shade);
		this.maxAlpha = 0.7F + random.nextFloat() * 0.2F;
		this.phase = random.nextFloat() * Mth.TWO_PI;
		this.heading = random.nextFloat() * Mth.TWO_PI;
		// some curve left, some right, some hardly at all
		this.turn = (random.nextFloat() - 0.5F) * 0.12F;
		this.alpha = 0F;
	}

	private static final float WANDER = 0.0012F;
	private static final float BOB = 0.0008F;

	@Override
	public void tick() {
		super.tick();
		// wander: a heading that curves and wobbles, and a slow bob
		heading += turn + (random.nextFloat() - 0.5F) * 0.3F;
		xd += Mth.cos(heading) * WANDER;
		zd += Mth.sin(heading) * WANDER;
		yd += Mth.sin((age + phase * 10) * 0.08F) * BOB;
		// fade in over the first quarter, out over the last half
		float life = (float) age / lifetime;
		alpha = maxAlpha * (life < 0.25F ? life / 0.25F : life > 0.5F ? (1F - life) / 0.5F : 1F);
	}

	/** a slow swell and shrink, so it seems alive */
	@Override
	public float getQuadSize(float partialTick) {
		return quadSize * (0.9F + 0.1F * Mth.sin((age + partialTick) * 0.15F + phase));
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
