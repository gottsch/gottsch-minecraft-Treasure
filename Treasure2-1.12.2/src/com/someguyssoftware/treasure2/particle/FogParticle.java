package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.MistEmitterTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FogParticle extends Particle {
	private final ResourceLocation fogResourceLocation = new ResourceLocation(Treasure.MODID, "particle/fog_particle");
	private final float ALPHA_VALUE = 0.1F;
	private final float MAX_SCALE_VALUE = 12;
	
	private ICoords parentGravestoneCoords;
	
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
	public FogParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ICoords parentCoords) {
		super(world, x, y, z);//, velocityX, velocityY, velocityZ);
		// override the vertical speed
//		this.motionY = 0;//velocityY + (Math.random() * 1.0D - 0.50D) * 0.4000000059604645D;
//		this.motionY = (double)(this.rand.nextFloat() * 0.05F + 0.05F);
		this.parentGravestoneCoords = parentCoords;
		
		this.particleGravity = 0.03F; 
		// overriden onUpdate()
		this.particleMaxAge = 300; // not used since we have overridden onUpdate
		this.particleAlpha = ALPHA_VALUE;  // a value less than 1 turns on alpha blending. Otherwise, alpha blending is off
		// and the particle won't be transparent.
		this.particleScale = MAX_SCALE_VALUE; //*= this.rand.nextFloat() * 2.0F + 0.2F;
		// TODO look at Particle.render at scaling and apply to alpha
		// TODO look at Particle.move for collisions.

		// vanilla lava
//        this.motionX *= 0.800000011920929D;
//        this.motionY *= 0.800000011920929D;
//        this.motionZ *= 0.800000011920929D;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
                
		// set the texture to the flame texture, which we have previously added using TextureStitchEvent
		//   (see TextureStitcherBreathFX)
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fogResourceLocation.toString());
		setParticleTexture(sprite);  // initialise the icon to our custom texture

	}

	/**
	 * Used to control what texture and lighting is used for the EntityFX.
	 * Returns 1, which means "use a texture from the blocks + items texture sheet"
	 * The vanilla layers are:
	 * normal particles: ignores world brightness lighting map
	 *   Layer 0 - uses the particles texture sheet (textures\particle\particles.png)
	 *   Layer 1 - uses the blocks + items texture sheet
	 * lit particles: changes brightness depending on world lighting i.e. block light + sky light
	 *   Layer 3 - uses the blocks + items texture sheet (I think)
	 *
	 * @return
	 */
	@Override
	public int getFXLayer() {
		return 1;
	}

	// can be used to change the brightness of the rendered Particle.
	@Override
	public int getBrightnessForRender(float partialTick) {
//		return FULL_BRIGHTNESS_VALUE;
		return 0xe3e3e3;
		// if you want the brightness to be the local illumination (from block light and sky light) you can just use
		//  Entity.getBrightnessForRender() base method, which contains:
		//    BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		//    return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
	}

	/*
	 * this function is used by ParticleManager.addEffect() to determine whether depthmask writing should be on or not.
	 * FlameBreathFX uses alphablending (i.e. the FX is partially transparent) but we want depthmask writing on,
	 * otherwise translucent objects (such as water) render over the top of our breath, even if the particle is in front
	 * of the water and not behind.
	 */ 
	@Override
	public boolean shouldDisableDepth() {
		return true; // works better?
	}

	/**
	 * call once per tick to update the Particle position, calculate collisions, remove when max lifetime is reached, etc
	 */
	@Override
	public void onUpdate() {
		// check if the parent gravestone is active
//		GravestoneTileEntity gravestoneTileEntity = (GravestoneTileEntity) Minecraft.getMinecraft().world.getTileEntity(getParentGravestoneCoords().toPos());
		
//		if (gravestoneTileEntity != null && gravestoneTileEntity.isActive()) {
//			Treasure.logger.debug("Particle proximity alert!");
//		}
		// TODO template pattern - call something here that is overridden by subclasses.
		
		// save the previous location
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		// calculate the new scale
		int halfMaxAge = this.particleMaxAge / 2;
		if (this.particleAge > halfMaxAge) {
			// shrink the particle
			float ageFactor = 1.0F - ((this.particleAge - halfMaxAge) / halfMaxAge);
//			this.particleScale = MAX_SCALE_VALUE * ageFactor;
			this.particleAlpha = ALPHA_VALUE * ageFactor;
		}

		// calculate the motion
        this.motionY -= 0.02D; // gravity
        this.move(this.motionX, this.motionY, this.motionZ);
//        this.motionX *= 0.9990000128746033D;
//        this.motionZ *= 0.9990000128746033D;		

		// collision with a block makes the ball disappear.  But does not collide with entities
//		if (onGround) {
//            this.motionX *= 0.699999988079071D;
//            this.motionZ *= 0.699999988079071D;
//		}

		// detect a collision while moving upwards (can't move up at all) 
		if (prevPosY == posY && motionY > 0) {
		}

		// increase the age and test against max age
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
	}

	public ICoords getParentGravestoneCoords() {
		return parentGravestoneCoords;
	}

	public void setParentGravestoneCoords(ICoords parentGravestoneCoords) {
		this.parentGravestoneCoords = parentGravestoneCoords;
	}
}

