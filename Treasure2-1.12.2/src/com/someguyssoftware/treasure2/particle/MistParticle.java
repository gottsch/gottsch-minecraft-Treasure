package com.someguyssoftware.treasure2.particle;

import java.util.Random;

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
public class MistParticle extends AbstractMistParticle {
	private static ResourceLocation mistParticlesSprites[] = new ResourceLocation[4];

	static {
		mistParticlesSprites[0] = new ResourceLocation(Treasure.MODID, "particle/mist_particle");
		mistParticlesSprites[1] = new ResourceLocation(Treasure.MODID, "particle/mist_particle2");
		mistParticlesSprites[2] = new ResourceLocation(Treasure.MODID, "particle/mist_particle3");
		mistParticlesSprites[3] = new ResourceLocation(Treasure.MODID, "particle/mist_particle4");
	}

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
	public MistParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ,
			ICoords parentCoords) {
		super(world, x, y, z);// , velocityX, velocityY, velocityZ);
		this.setParentEmitterCoords(parentCoords);

		// set the texture one fo the mist textures, which we have previously added using TextureStitchEvent
		// (see TextureStitcherBreathFX)
		// randomly select a mist sprite
		Random random = new Random();
		int index = random.nextInt(4);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(mistParticlesSprites[index].toString());
		setParticleTexture(sprite);
	}

	@Override
	public void init() {
		super.init();
//
//
//
//		// extra test if index == 1 then randomly select it to rise (anti-gravity)
//		if (index == FLOATER_INDEX && RandomHelper.checkProbability(random, FLOATER_PROBABILITY)) {
//			mistGravity = -DEFAULT_PARTICLE_GRAVITY * 0.2F;
//			mistMaxAge = 260; // has to be greater than the transition out age, else it will just disappear and be
//								// choppy
//		} else {
//			mistGravity = DEFAULT_PARTICLE_GRAVITY;
//			mistMaxAge = DEFAULT_PARTICLE_MAX_AGE;
//		}
//		// set properties
//		this.particleGravity = provideGravity();
//		this.particleMaxAge = provideMaxAge();
//		
//		particleAlpha = provideMaxAlpha();
//		particleScale = provideMaxScale();
//		mistGravity = DEFAULT_PARTICLE_GRAVITY;
	}

	@Override
	public void doPlayerCollisions(World world) {
		// do nothing
	}

	@Override
	public void inflictEffectOnPlayer(EntityPlayer player) {
		// do nothing
	}
}
