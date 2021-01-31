package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MistParticle extends AbstractMistParticle {
//	private static ResourceLocation mistParticlesSprites[] = new ResourceLocation[4];

	private final IAnimatedSprite sprites;  // contains a list of textures; choose one using either
	  // newParticle.selectSpriteRandomly(sprites); or newParticle.selectSpriteWithAge(sprites);
	
//	static {
//		mistParticlesSprites[0] = new ResourceLocation(Treasure.MODID, "particle/mist_particle");
//		mistParticlesSprites[1] = new ResourceLocation(Treasure.MODID, "particle/mist_particle2");
//		mistParticlesSprites[2] = new ResourceLocation(Treasure.MODID, "particle/mist_particle3");
//		mistParticlesSprites[3] = new ResourceLocation(Treasure.MODID, "particle/mist_particle4");
//	}

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
			ICoords parentCoords, IAnimatedSprite sprites) {
		super(world, x, y, z);// , velocityX, velocityY, velocityZ);
		Treasure.LOGGER.debug("creating particle");
		this.setParentEmitterCoords(parentCoords);

		this.sprites = sprites; //?
		
		// set the texture one fo the mist textures, which we have previously added
		// using TextureStitchEvent
		// (see TextureStitcherBreathFX)
		// randomly select a mist sprite
//		Random random = new Random();
//		int index = random.nextInt(4);
//		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
//				.getAtlasSprite(mistParticlesSprites[index].toString());
//		setParticleTexture(sprite);
		init();
	}

	@Override
	public void init() {
		super.init();
	}
	
	  // can be used to change the skylight+blocklight brightness of the rendered Particle.
	  @Override
	public int getBrightnessForRender(float partialTick) {
	    final int BLOCK_LIGHT = 15;  // maximum brightness
	    final int SKY_LIGHT = 15;    // maximum brightness
	    final int FULL_BRIGHTNESS_VALUE = LightTexture.packLight(BLOCK_LIGHT, SKY_LIGHT);
	    return FULL_BRIGHTNESS_VALUE;

	    // if you want the brightness to be the local illumination (from block light and sky light) you can just use
	    //  the Particle.getBrightnessForRender() base method, which contains:
	    //    BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);
	    //    return this.world.isBlockLoaded(blockPos) ? WorldRenderer.getCombinedLight(this.world, blockPos) : 0;
	  }
	  
	  // There are several useful predefined types:
	  // PARTICLE_SHEET_TRANSLUCENT semi-transparent (translucent) particles
	  // PARTICLE_SHEET_OPAQUE    opaque particles
	  // TERRAIN_SHEET            particles drawn from block or item textures
	  // PARTICLE_SHEET_LIT       appears to be the same as OPAQUE.  Not sure of the difference.  In previous versions of minecraft,
	  //                          "lit" particles changed brightness depending on world lighting i.e. block light + sky light
	  public IParticleRenderType getRenderType() {
	    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	  }

	@Override
	public void inflictEffectOnPlayer(PlayerEntity player) {
		return;		
	}
}