package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BillowingMistParticle extends AbstractMistParticle {
	private static ResourceLocation mistParticlesResourceLocation = new ResourceLocation(Treasure.MODID,
			"particle/mist_particle");

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
	public BillowingMistParticle(World world, double x, double y, double z, double velocityX, double velocityY,
			double velocityZ, ICoords parentCoords) {
		super(world, x, y, z);// , velocityX, velocityY, velocityZ);
		this.setParentEmitterCoords(parentCoords);

		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(mistParticlesResourceLocation.toString());
		setParticleTexture(sprite);
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
		particleGravity = billowingGravity;
	}

	/**
	 * 
	 */
	@Override
	public void onUpdate() {
		// save the previous location
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		// calculate the y motion if not on the ground
		this.motionY -= provideGravity(); // gravity
		/*
		 * if the motionY value is less than 3x the negative provided gravity value (downwards) then max it
		 * out at 3x.
		 */
		if (this.motionY < -provideGravity() * 3) {
			this.motionY = -provideGravity() * 3;
		}
		this.move(this.motionX, this.motionY, this.motionZ);

		doTransitions();

		// NOTE the movement coupled with the transitions gives the mist a "roiling" effect.

		// detect a collision while moving upwards (can't move up at all)
		if (prevPosY == posY && motionY > 0) {
			// do nothing. not used for mist
		}

		if (this.particleAge % 40 == 0) {
			isAntiGravity = !isAntiGravity;
			if (isAntiGravity) {
				billowingGravity = -DEFAULT_PARTICLE_GRAVITY * 0.3F;
			} else {
				billowingGravity = DEFAULT_PARTICLE_GRAVITY;
			}
			particleGravity = billowingGravity;
		}

		// increase the age and test against max age
		if (this.particleAge++ >= provideMaxAge()) {
			this.setExpired();
		}
	}

	@Override
	public void doPlayerCollisions(World world) {
		// do nothing
	}

	@Override
	public void inflictEffectOnPlayer(EntityPlayer player) {
		// do nothing
	}

	@Override
	public float provideGravity() {
		return billowingGravity;
	}

	@Override
	public float provideMaxScale() {
		return 6;
	}
}
