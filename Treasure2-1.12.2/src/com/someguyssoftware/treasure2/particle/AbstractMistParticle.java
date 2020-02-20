/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.MistEmitterTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public abstract class AbstractMistParticle extends Particle implements IMistParticle {
	/*
	 * particle lifespan: 200 = 10 seconds (10 * 20 ticks/sec): too short
	 * 400 = 20 seconds (10 * 20 ticks/sec): too long
	 *  320 = 16 seconds (16 * 20 ticks/sec): seems to be a good length
	 */
	private static final int DEFAULT_PARTICLE_MAX_AGE = 320;
	private static final float DEFAULT_PARTICLE_GRAVITY = 0.001F;

	private static final int TRANSITION_IN_STOP_AGE = 105;
	private static final int TRANSITION_OUT_START_AGE = 240;
	private static final float TRANSITION_IN_START_SCALE = 2;
	private static final float TRANSITION_OUT_FINAL_SCALE = 2;
	
	private ICoords parentEmitterCoords;
	private float transitionInScaleIncrement;
	private float transitionOutScaleIncrement;

	/**
	 * 
	 * @param worldIn
	 * @param posXIn
	 * @param posYIn
	 * @param posZIn
	 */
	protected AbstractMistParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);

		/*
		 *  determine scale increments. this is the difference between the max scale size and the variable scale size divided by variable (ex. stopAge - initial age(zero) = stopAge).
		 *  this will not actually result in the scale reaching max/min size, because that would assume that this method is called every tick, which is unlikely.
		 */
		transitionInScaleIncrement = (getMaxScale() - TRANSITION_IN_START_SCALE) / TRANSITION_IN_STOP_AGE;
		transitionOutScaleIncrement = (getMaxScale() - TRANSITION_OUT_FINAL_SCALE) / (getMaxAge() - TRANSITION_OUT_START_AGE);
				
		// add horizontal movement
        this.motionX = (Math.random() * 2.0D - 1.0D) * 0.0050200D;
        this.motionZ = (Math.random() * 2.0D - 1.0D) * 0.0050200D;
		// turn off any initial verticle motion
		this.motionY = 0;
		
		// set default properties
		this.particleGravity = getGravity();
		
		// set the max age
		this.particleMaxAge = getMaxAge();
		
		// a value less than 1 turns on alpha blending. Otherwise, alpha blending is off
		this.particleAlpha = getMaxAlpha();

		// and the particle won't be transparent.
		this.particleScale = TRANSITION_IN_START_SCALE;
	}

	/**
	 * call once per tick to update the Particle position, calculate collisions, remove when max lifetime is reached, etc
	 */
	@Override
	public void onUpdate() {
		doPlayerCollisions(Minecraft.getMinecraft().world);

		// save the previous location
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		// calculate the y motion if not on the ground
		if (!this.onGround) {
			this.motionY -= getGravity(); // gravity
		}
		this.move(this.motionX, this.motionY, this.motionZ);

		// transition the mist into the environment
		transitionIn(TRANSITION_IN_START_SCALE, TRANSITION_IN_STOP_AGE, transitionInScaleIncrement); 
		
		// transition the mist out of the environment
		transitionOut(TRANSITION_OUT_FINAL_SCALE, TRANSITION_OUT_START_AGE, transitionOutScaleIncrement);
		
		// NOTE the movement coupled with the transitions gives the mist a "roiling" effect.
		
		// detect a collision while moving upwards (can't move up at all) 
		if (prevPosY == posY && motionY > 0) {
			// do nothing. not used for mist
		}

		// increase the age and test against max age
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}
	}

	/**
	 * 
	 * @param initialScale
	 * @param stopAge
	 */
	public void transitionIn(float initialScale, int stopAge, float scaleIncrement) {
		if (particleAge < stopAge &&
			particleScale < getMaxScale()) {
			// increase in size
			particleScale += scaleIncrement;
		}
	}
	
	public void transitionOut(float finalScale, int startAge, float scaleIncrement) {
		if (particleAge >= startAge &&
				particleScale > finalScale) {
			// decrease in size
			particleScale -= scaleIncrement;			
		}
	}
	
	/**
	 * 
	 * @param world
	 */
	public void doPlayerCollisions(World world) {
		if (getParentEmitterCoords() == null) {
			Treasure.logger.debug("emitter coords is null");
			return;
		}

		// get the emitter tile entity
		TileEntity emitterTileEntity = world.getTileEntity(getParentEmitterCoords().toPos());
		if (emitterTileEntity == null) {
			if (emitterTileEntity == null) {
				Treasure.logger.debug("emitter tile entity is null");
			}
			return;
		}

		// create an AxisAlignedBB for the particle
		AxisAlignedBB aabb = new AxisAlignedBB(posX - 0.125D, posY, posZ - 0.125D, posX + 0.125D, posY + 0.25D, posZ + 0.125D);

		// for all the players in the mist emitter tile entity list
		for(EntityPlayer player : ((MistEmitterTileEntity)emitterTileEntity).getPlayersWithinProximity()) {
			if (player.getEntityBoundingBox().intersects(aabb)) {
				inflictEffectOnPlayer(player);
			}
		}
	}

	/**
	 * 
	 * @param player
	 */
	public abstract void inflictEffectOnPlayer(EntityPlayer player);

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

	/*
	 *  can be used to change the brightness of the rendered Particle.
	 */
	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xe3e3e3; // TODO make const
	}

	/*
	 * this function is used by ParticleManager.addEffect() to determine whether depthmask writing should be on or not.
	 * FlameBreathFX uses alphablending (i.e. the FX is partially transparent) but we want depthmask writing on,
	 * otherwise translucent objects (such as water) render over the top of our breath, even if the particle is in front
	 * of the water and not behind.
	 */ 
	@Override
	public boolean shouldDisableDepth() {
		return true;
	}

	@Override
	public ICoords getParentEmitterCoords() {
		return parentEmitterCoords;
	}

	@Override
	public void setParentEmitterCoords(ICoords parentEmitterCoords) {
		this.parentEmitterCoords = parentEmitterCoords;
	}
	
	@Override
	public int getMaxAge() {
		return DEFAULT_PARTICLE_MAX_AGE;
	}
	
	@Override
	public float getGravity() {
		return DEFAULT_PARTICLE_GRAVITY;
	}
	
	@Override
	public abstract float getMaxScale();
	@Override
	public abstract float getMaxAlpha();
}
