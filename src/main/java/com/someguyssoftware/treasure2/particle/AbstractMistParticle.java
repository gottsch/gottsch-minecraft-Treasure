/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.MistEmitterTileEntity;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractMistParticle extends SpriteTexturedParticle implements IMistParticle {
	/*
	 * particle lifespan: 200 = 10 seconds (10 * 20 ticks/sec): too short. 400 = 20
	 * seconds (10 * 20 ticks/sec): too long .320 = 16 seconds (16 * 20 ticks/sec):
	 * seems to be a good length
	 */
	public static final int DEFAULT_PARTICLE_MAX_AGE = 320;
	public static final float DEFAULT_PARTICLE_MAX_SIZE =3;//  12;

	public static final float DEFAULT_PARTICLE_ALPHA = 0.08F;
	public static final float DEFAULT_PARTICLE_GRAVITY = 0.001F;

	private static final int TRANSITION_IN_STOP_AGE = 105;
	protected static final int TRANSITION_OUT_START_AGE = 240;
	private static final float TRANSITION_IN_START_SIZE = 1.5F;
	private static final float TRANSITION_OUT_FINAL_SIZE = 0.5F;
	private static final int DEFAULT_BRIGHTNESS = 0xe3e3e3;

	private ICoords parentEmitterCoords;
	private float transitionSize;
	private float transitionInSizeIncrement;
	private float transitionOutSizeIncrement;

	private float transitionQuadSize;
	private float transitionInQuadSizeIncrement;
	private float transitionOutQuadSizeIncrement;

	/*
	 * NOTES:
	 *  x, y, z = current position
	 *  xo, yo, zo, = old position
	 *  xd, yd, zd = motion (or delta)
	 */
	/**
	 * 
	 * @param worldIn
	 * @param posXIn
	 * @param posYIn
	 * @param posZIn
	 */
	protected AbstractMistParticle(ClientWorld worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);

		// add horizontal movement
		this.xd = (Math.random() * 2.0D - 1.0D) * 0.0090200D;//0.10000000149011612D;//0.0050200D;
		this.zd = (Math.random() * 2.0D - 1.0D) * 0.0090200D;//0.10000000149011612D;//0.0050200D;
		// turn off any initial verticle motion
		this.yd = 0;

		Treasure.LOGGER.debug("creation motion x-> {}, z-> {}, scale-> {}", this.xd, this.zd, this.getScale());
	}

	@Override
	public void init() {
		/*
		 * determine scale increments. this is the difference between the max scale size
		 * and the variable scale size divided by variable (ex. stopAge - initial
		 * age(zero) = stopAge). this will not actually result in the scale reaching
		 * max/min size, because that would assume that this method is called every
		 * tick, which is unlikely.
		 */
		transitionSize = provideStartSize();
		transitionInSizeIncrement = ((provideMaxSize() - provideStartSize())) / TRANSITION_IN_STOP_AGE; // NOTE this should be % unless change the sizes.
		transitionOutSizeIncrement = ((provideMaxSize() - TRANSITION_OUT_FINAL_SIZE)) / (provideMaxAge() - TRANSITION_OUT_START_AGE);

		// set default properties
		this.gravity = provideGravity();

		// set the max age
		this.setLifetime(provideMaxAge());

		// a value less than 1 turns on alpha blending. Otherwise, alpha blending is off
		// and the particle won't be transparent.
		this.setAlpha(provideAlpha());

		// initialize particle size and quadsize
		this.scale(provideStartSize()); // NOTE scale does the multipying. ie scaleIn * 0.2

		transitionQuadSize = getQuadSize(0);
		transitionInQuadSizeIncrement = ((provideMaxSize() * transitionQuadSize) / TRANSITION_IN_START_SIZE) / TRANSITION_IN_STOP_AGE;
		transitionOutQuadSizeIncrement = ((provideMaxSize() * transitionQuadSize) / TRANSITION_OUT_FINAL_SIZE) / (provideMaxAge() - TRANSITION_OUT_START_AGE);
	}

	@Override
	public void render(IVertexBuilder vertexBuilder, ActiveRenderInfo activeRenderInfo, float p_225606_3_) {
		// need to turn of depthMask when rendering. this replaces shouldDisableDepth() from 1.12.2
		RenderSystem.depthMask(false);
		super.render(vertexBuilder, activeRenderInfo, p_225606_3_);
	}

	/**
	 * call once per tick to update the Particle position, calculate collisions,
	 * remove when max lifetime is reached, etc
	 */
	@Override
	public void tick() {
		doPlayerCollisions(this.level);
		
		// save the previous location
		xo = x;
		yo = y;
		zo = z;

		// calculate the y motion if not on the ground
		if (!this.onGround) {
			this.yd -= provideGravity(); // gravity
		}

		this.move(this.xd, this.yd, this.zd);

		doTransitions();
		// NOTE the movement coupled with the transitions gives the mist a "roiling"
		// effect.

		// detect a collision while moving upwards (can't move up at all)
		if (yo == y && yd > 0) {
			// do nothing. not used for mist
		}

		// increase the age and test against max age
		if (this.age++ >= this.lifetime) {
			this.remove();
		}
	}

	/**
	 * 
	 */
	public void doTransitions() {
		// transition the mist into the environment
		transitionIn();

		// transition the mist out of the environment
		transitionOut();
	}

	/**
	 * 
	 * @param size
	 * @param quadSize
	 */
	public void updateScale(float size, float quadSize) {
		this.setSize(toScale(size), toScale(size));
		this.quadSize = quadSize;
	}

	/**
	 * 
	 * @param initialScale
	 * @param stopAge
	 */
	@Override
	public void transitionIn() {
		if (this.age < TRANSITION_IN_STOP_AGE && transitionSize < provideMaxSize()) {
			Treasure.LOGGER.debug("p[{}] is growing; a-> {}", this.xd, this.age);
			// increase in size
			this.transitionSize += transitionInSizeIncrement;
			this.transitionQuadSize += transitionInQuadSizeIncrement;
			updateScale(transitionSize, transitionQuadSize);
		}
	}

	/**
	 * 
	 */
	@Override
	public void transitionOut() {
		if (age >=TRANSITION_OUT_START_AGE && transitionQuadSize > 0F) {
			Treasure.LOGGER.debug("p[{}] is shrinking; a -> {}, q-> transitionQuadSize", this.xd, this.age);
			// decrease in size
			this.transitionSize -= transitionOutSizeIncrement;
			this.transitionQuadSize -= transitionOutQuadSizeIncrement;
			if (transitionQuadSize < 0F) {
				transitionQuadSize = 0F;
			}
			updateScale(transitionSize, transitionQuadSize);
			this.setAlpha(this.getAlpha() - 0.00125F);
		}
	}

	/**
	 * 
	 * @param world
	 */
	public void doPlayerCollisions(IWorld world) {
		if (getParentEmitterCoords() == null) {
			Treasure.LOGGER.debug("emitter coords is null");
			return;
		}

		// get the emitter tile entity
		TileEntity emitterTileEntity = world.getBlockEntity(getParentEmitterCoords().toPos());
		if (emitterTileEntity == null || !(emitterTileEntity instanceof MistEmitterTileEntity)) {
			return;
		}

		// create an AxisAlignedBB for the particle
		AxisAlignedBB aabb = new AxisAlignedBB(x - 0.125D, y, z - 0.125D, x + 0.125D, y + 0.25D,
				z + 0.125D);

		// for all the players in the mist emitter tile entity list
		for (PlayerEntity player : ((MistEmitterTileEntity) emitterTileEntity).getPlayersWithinProximity()) {
			if (player.getBoundingBox().intersects(aabb)) {
				inflictEffectOnPlayer(player);
			}
		}
	}

	/**
	 * 
	 * @param player
	 */
	public abstract void inflictEffectOnPlayer(PlayerEntity player);

	/**
	 * 
	 * @param size
	 * @return
	 */
	public float toScale(float size) {
		return size * 0.2F;
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
	//	@Override
	//	public int getFXLayer() {
	//		return 1;
	//	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	/*
	 * can be used to change the brightness of the rendered Particle.
	 */
	//	@Override
	public int getLightColor(float partialTick) {
		return DEFAULT_BRIGHTNESS;
	}


	@Override
	public boolean shouldCull() {
		return false;
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
	public float getGravity() {
		return this.gravity;
	}

	@Override
	public float getScale() {
		return this.bbWidth;
	}

	@Override
	public float getAlpha() {
		return this.alpha;
	}

	@Override
	public float provideGravity() {
		return DEFAULT_PARTICLE_GRAVITY;
	}

	@Override
	public float provideAlpha() {
		return DEFAULT_PARTICLE_ALPHA;
	}

	@Override
	public int provideMaxAge() {
		return DEFAULT_PARTICLE_MAX_AGE;
	}

	@Override
	public float provideStartSize() {
		return TRANSITION_IN_START_SIZE;
	}
	
	@Override
	public float provideMaxSize() {
		return DEFAULT_PARTICLE_MAX_SIZE;
	}

	@Override
	public float getTransitionInSizeIncrement() {
		return transitionInSizeIncrement;
	}

	@Override
	public void setTransitionInSizeIncrement(float transitionInScaleIncrement) {
		this.transitionInSizeIncrement = transitionInScaleIncrement;
	}

	@Override
	public float getTransitionOutSizeIncrement() {
		return transitionOutSizeIncrement;
	}

	@Override
	public void setTransitionOutSizeIncrement(float transitionOutScaleIncrement) {
		this.transitionOutSizeIncrement = transitionOutScaleIncrement;
	}
}