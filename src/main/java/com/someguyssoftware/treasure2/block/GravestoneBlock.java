/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.FlameParticleData;
import com.someguyssoftware.treasure2.particle.MistParticleData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Jan 29, 2018
 *
 */
public class GravestoneBlock extends FacingBlock implements ITreasureBlock, IMistSupport {

	/*
	 * An array of VoxelShape bounds for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		
		// set the default bounds/shape (full block)
		VoxelShape shape = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
		setBounds(
				new VoxelShape[] {
						shape, 	// N
						shape,  	// E
						shape,  	// S
						shape	// W
				});
	}

	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.get(FACING)) {
		default:
		case NORTH:
			return bounds[0];
		case EAST:
			return bounds[1];
		case SOUTH:
			return bounds[2];
		case WEST:
			return bounds[3];
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockState = this.getDefaultState().with(FACING,
				context.getPlacementHorizontalFacing().getOpposite());
		return blockState;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

		if (world.isRemote) {
//		if (!TreasureConfig.GENERAL.enableFog) {
//			return;
//		}

//		int x = pos.getX();
//		int y = pos.getY();
//		int z = pos.getZ();
//
//		boolean isCreateParticle = checkTorchPrevention(world, random, x, y, z);
//		if (!isCreateParticle) {
//			return;
//		}
//
//		// initial positions - has a spread area of up to 1.5 blocks
//		double xPos = (x + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
//		double yPos = y;
//		// + state.getBoundingBox(world, pos).maxY; // + 1.0;
//		double zPos = (z + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
//		// initial velocities
//		double velocityX = 0;
//		double velocityY = 0;
//		double velocityZ = 0;
//		
//		final boolean IGNORE_RANGE_CHECK = false; // if true, always render particle regardless of how far away the player is
//		
		// create particle
//		AbstractMistParticle mistParticle = null;

//		if (RandomHelper.checkProbability(random, 80)) {
//			mistParticle = new MistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ, new Coords(pos));
//		} else {
//			mistParticle = new BillowingMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
//					new Coords(pos));
//		}
		
		
//		MistParticleData mistParticleData = new MistParticleData(new Coords(pos));
//
//		try {
//			if (RandomHelper.checkProbability(random, 80)) {
//				Treasure.LOGGER.debug("adding particle...");
//				world.addParticle(mistParticleData, IGNORE_RANGE_CHECK, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
//			}
//		}
//		catch(Exception e) {
//			Treasure.LOGGER.error("error with particle:", e);
//		}
		
		     // first example:
		      // spawn a vanilla particle of LAVA type (smoke from lava)
		      //  The starting position is the [x,y,z] of the tip of the pole (i.e. at [0.5, 1.0, 0.5] relative to the block position)
		      //  Set the initial velocity to zero.
		      // When the particle is spawned, it will automatically add a random amount of velocity - see EntityLavaFX constructor and
		      //   Particle constructor.  This can be a nuisance if you don't want your Particle to have a random starting velocity!  See
		      //  second example below for more information.

		      // starting position = top of the pole
		      double xpos = pos.getX() + 0.5;
		      double ypos = pos.getY() + 1.0;
		      double zpos = pos.getZ() + 0.5;
		      double velocityX = 0; // increase in x position every tick
		      double velocityY = 0; // increase in y position every tick;
		      double velocityZ = 0; // increase in z position every tick

		      final boolean IGNORE_RANGE_CHECK = false; // if true, always render particle regardless of how far away the player is
		      final double PERCENT_CHANCE_OF_LAVA_SPAWN = 10;  // only spawn Lava particles occasionally (visually distracting if too many)

		      if (random.nextDouble() < PERCENT_CHANCE_OF_LAVA_SPAWN / 100.0) {
		        world.addParticle(ParticleTypes.LAVA, IGNORE_RANGE_CHECK,
		                xpos, ypos, zpos, velocityX, velocityY, velocityZ);
		      }

		      // second example:
		      // spawn a custom Particle ("FlameParticle") with a texture we have added ourselves.
		      // FlameParticle also has custom movement and collision logic - it moves in a straight line until it hits something.
		      // To make it more interesting, the stream of fireballs will target the nearest non-player entity within 16 blocks at
		      //   the height of the pole or above.

		      // starting position = top of the pole
		      xpos = pos.getX() + 0.5;
		      ypos = pos.getY() + 1.0;
		      zpos = pos.getZ() + 0.5;

		      // add a small amount of wobble to stop particles rendering directly on top of each other (z-fighting) which makes them look weird
		      final double POSITION_WOBBLE_AMOUNT = 0.01;
		      xpos += POSITION_WOBBLE_AMOUNT * (random.nextDouble() - 0.5);
		      zpos += POSITION_WOBBLE_AMOUNT * (random.nextDouble() - 0.5);

		      MonsterEntity mobTarget = getNearestTargetableMob(world, xpos, ypos, zpos);
		      Vec3d fireballDirection;
		      if (mobTarget == null) { // no target: fire straight upwards
		        fireballDirection = new Vec3d(0.0, 1.0, 0.0);
		      } else {  // otherwise: aim at the mob
		        // the direction that the fireball needs to travel is calculated from the starting point (the pole) and the
		        //   end point (the mob's eyes).  A bit of googling on vector maths will show you that you calculate this by
		        //  1) subtracting the start point from the end point
		        //  2) normalising the vector (if you don't do this, then the fireball will fire faster if the mob is further away

		        final float PARTIAL_TICKS = 1.0F;
		        fireballDirection = mobTarget.getEyePosition(PARTIAL_TICKS).subtract(xpos, ypos, zpos);  // NB this method only works on client side
		        fireballDirection = fireballDirection.normalize();
		      }

		      // the velocity vector is now calculated as the fireball's speed multiplied by the direction vector.

		      final double SPEED_IN_BLOCKS_PER_SECOND = 2.0;
		      final double TICKS_PER_SECOND = 20;
		      final double SPEED_IN_BLOCKS_PER_TICK = SPEED_IN_BLOCKS_PER_SECOND / TICKS_PER_SECOND;

		      velocityX = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.x; // how much to increase the x position every tick
		      velocityY = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.y; // how much to increase the y position every tick
		      velocityZ = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.z; // how much to increase the z position every tick

		      Color tint = getTint(pos);
		      double diameter = getDiameter(pos);

		      FlameParticleData flameParticleData = new FlameParticleData(tint, diameter);
		      world.addParticle(flameParticleData, IGNORE_RANGE_CHECK,
		              xpos, ypos, zpos, velocityX, velocityY, velocityZ);
		    }

	}

	  // choose a semi-random colour based on the block's position
	  //  the texture has basically no blue in it so we don't bother varying that
	  private Color getTint(BlockPos blockPos) {
	    Color [] tints = {
	            new Color(1.00f, 1.00f, 1.0f),  // no tint (full white)
	            new Color(1.00f, 0.75f, 1.0f),  // redder
	            new Color(1.00f, 0.50f, 1.0f),  // much redder
	            new Color(0.75f, 1.00f, 1.0f),  // greener
	            new Color(0.50f, 1.00f, 1.0f),  // much greener
	    };

	    Random random = new Random(blockPos.hashCode());
	    random.nextInt(); random.nextInt();  // iterate a couple of times (the first nextInt() isn't very random)
	    int idx = random.nextInt(tints.length);
	    return tints[idx];
	  }
	  
	  // choose a semi-random size based on the block's position
	  private double getDiameter(BlockPos blockPos) {
	    Random random = new Random(blockPos.hashCode());
	    random.nextDouble(); random.nextDouble();    // iterate a couple of times (the first nextDouble() isn't very random)

	    final double MIN_DIAMETER = 0.05;
	    final double MAX_DIAMETER = 0.35;
	    return MIN_DIAMETER + (MAX_DIAMETER - MIN_DIAMETER) * random.nextDouble();
	  }
	  
	  private MonsterEntity getNearestTargetableMob(World world, double xpos, double ypos, double zpos) {
		    final double TARGETING_DISTANCE = 16;
		    AxisAlignedBB targetRange = new AxisAlignedBB(xpos - TARGETING_DISTANCE,
		                                                  ypos,
		                                                  zpos - TARGETING_DISTANCE,
		                                                  xpos + TARGETING_DISTANCE,
		                                                  ypos + TARGETING_DISTANCE,
		                                                  zpos + TARGETING_DISTANCE);

		    List<MonsterEntity> allNearbyMobs = world.getEntitiesWithinAABB(MonsterEntity.class, targetRange);
		    MonsterEntity nearestMob = null;
		    double closestDistance = Double.MAX_VALUE;
		    for (MonsterEntity nextMob : allNearbyMobs) {
		      double nextClosestDistance = nextMob.getDistanceSq(xpos, ypos, zpos);
		      if (nextClosestDistance < closestDistance) {
		        closestDistance = nextClosestDistance;
		        nearestMob = nextMob;
		      }
		    }
		    return nearestMob;
		  }
	  
	/*
	 * This is for animateTick()
	 */	
	@Override
	public int tickRate(IWorldReader worldIn) {
		return 10;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	 * fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getBounds() {
		return bounds;
	}

	public GravestoneBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}