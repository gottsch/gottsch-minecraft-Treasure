package com.someguyssoftware.treasure2.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoundSoulMistParticle extends MistParticle {

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param velocityX
	 * @param velocityY
	 * @param velocityZ
	 */
	public BoundSoulMistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY,
			double velocityZ) {
		super(world, x, y, z, velocityZ, velocityZ, velocityZ);
	}

	@Override
	public int getMaxAge() {
		return 160;
	}

	@Override
	public float getTransitionInStopAge() {
		return 110;
	}
	
	@Override
	public float getTransitionOutStartAge() {
		return 111;
	}
	
	// TODO provide transitionInStop and transitionOutStart
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
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
		public Particle createParticle(BasicParticleType data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			BoundSoulMistParticle particle = new BoundSoulMistParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(spriteSet);
			return particle;
		}
	}
}
