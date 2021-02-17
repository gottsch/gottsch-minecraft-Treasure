package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;

public class MistParticleFactory implements IParticleFactory<MistParticleData> {
	/*
	 * contains a list of textures; choose one using either
	 * newParticle.selectSpriteRandomly(sprites); or newParticle.selectSpriteWithAge(sprites);
	 */
	private final IAnimatedSprite sprites;
	
	/**
	 * 
	 * @param sprite
	 */
	public MistParticleFactory(IAnimatedSprite sprite) {
		this.sprites = sprite;
	}
	
	/**
	 * 
	 */
	@Override
	public Particle makeParticle(MistParticleData data, World world, double x, double y, double z, double xSpeed,
			double ySpeed, double zSpeed) {
		Treasure.LOGGER.debug("making mist particle...");
		MistParticle particle = new MistParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getParentEmitterCoords(), sprites);
		particle.selectSpriteRandomly(sprites);
		return particle;
	}

}
