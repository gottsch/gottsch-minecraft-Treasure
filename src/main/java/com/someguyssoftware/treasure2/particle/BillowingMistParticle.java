package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.particle.data.BillowingMistParticleData;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
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
	public BillowingMistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY,
			double velocityZ, ICoords parentCoords) {
		super(world, x, y, z, velocityZ, velocityZ, velocityZ, parentCoords);// , velocityX, velocityY, velocityZ);
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
		this.yd -= provideGravity(); // gravity
		
		/*
		 * if the motionY value is less than 3x the negative provided gravity value (downwards) then max it
		 * out at 3x.
		 */
		if (this.yd < -provideGravity() * 3) {
			this.yd = -provideGravity() * 3;
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
	public float provideGravity() {
		return billowingGravity;
	}
	
	@Override
	public float provideStartSize() {
		return 0.75F;
	}
	
	@Override
	public float provideMaxSize() {
		return 2;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BillowingMistParticleData> {
		/*
		 * contains a list of textures; choose one using either
		 * newParticle.selectSpriteRandomly(spriteSet); or newParticle.selectSpriteWithAge(spriteSet);
		 */
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
		public Particle createParticle(BillowingMistParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			BillowingMistParticle particle = new BillowingMistParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getSourceCoords());
			particle.pickSprite(spriteSet);
			return particle;
		}
	}
}
