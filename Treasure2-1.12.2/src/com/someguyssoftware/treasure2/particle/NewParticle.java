/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 28, 2020
 *
 */
public class NewParticle extends Particle {
	private static ResourceLocation mistParticlesResourceLocation = new ResourceLocation(Treasure.MODID,
			"particle/mist_particle");

	public NewParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
//		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
		this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
		this.motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;

		particleScale = 2F;
		particleMaxAge = 200;
		particleAge = 0;

		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(mistParticlesResourceLocation.toString());
		setParticleTexture(sprite);

	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		this.motionY -= 0.04D * 1;// (double)this.particleGravity;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			// TODO re-arrange settings so that if on ground then gravity is flipped.

		}
	}

	/**
	 * Used to control what texture and lighting is used for the EntityFX. Returns
	 * 1, which means "use a texture from the blocks + items texture sheet" The
	 * vanilla layers are: normal particles: ignores world brightness lighting map
	 * Layer 0 - uses the particles texture sheet (textures\particle\particles.png)
	 * Layer 1 - uses the blocks + items texture sheet lit particles: changes
	 * brightness depending on world lighting i.e. block light + sky light Layer 3 -
	 * uses the blocks + items texture sheet (I think)
	 *
	 * @return
	 */
	@Override
	public int getFXLayer() {
		return 1;
	}

	/*
	 * can be used to change the brightness of the rendered Particle.
	 */
	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xe3e3e3; // TODO make const
	}

	/*
	 * this function is used by ParticleManager.addEffect() to determine whether
	 * depthmask writing should be on or not. FlameBreathFX uses alphablending (i.e.
	 * the FX is partially transparent) but we want depthmask writing on, otherwise
	 * translucent objects (such as water) render over the top of our breath, even
	 * if the particle is in front of the water and not behind.
	 */
	@Override
	public boolean shouldDisableDepth() {
		return true;
	}
}
