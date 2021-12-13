/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.PoisonMistParticle;
import com.someguyssoftware.treasure2.particle.SpanishMossMistParticle;
import com.someguyssoftware.treasure2.particle.TreasureParticles;
import com.someguyssoftware.treasure2.particle.data.CollidingParticleType;
//import com.someguyssoftware.treasure2.particle.PoisonMistParticle;
import com.someguyssoftware.treasure2.particle.data.ICollidingParticleType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.IParticleData;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BellAttachment;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Mark Gottschling on Jul 25, 2018
 *
 */
public class SpanishMossBlock extends BushBlock implements ITreasureBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	/**
	 * 
	 */
	public SpanishMossBlock(String modID, String name, Block.Properties properties) {
		super(properties.strength(0.6F).noCollission().instabreak().sound(SoundType.WET_GRASS));
		setRegistryName(modID, name);
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}

	/**
	 * 
	 */
	@Override
protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED);
	}

	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {

		if (!TreasureConfig.FOG.enableFog.get()) {
			return;
		}

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		if (RandomHelper.checkProbability(random, 75D)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// initial positions - has a spread area of up to 1.5 blocks
		double xPos = (x + 0.5D);
		double yPos = y - 0.1D;
		double zPos = (z + 0.5D);
		// initial velocities
		double velocityX = 0;
		double velocityY = 0;
		double velocityZ = 0;

		// NOTE can override methods here as it is a factory that creates the particle
		IParticleData particleData = TreasureParticles.SPANISH_MOSS_MIST_PARTICLE_TYPE.get();
		
		try {
			world.addParticle(particleData, false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}

	@Override
	public boolean isPathfindable(BlockState blockState, LevelAccessor reader, BlockPos blockPos, PathType pathType) {
		return pathType == PathType.AIR && !this.hasCollision ? true : super.isPathfindable(blockState, reader, blockPos, pathType);
	}

	@Override
	public boolean canBeReplaced(BlockState state, Fluid fluid) {
		return true;
	}

	@Override
	protected boolean mayPlaceOn(BlockState p_200014_1_, LevelAccessor p_200014_2_, BlockPos p_200014_3_) {
		return true;
	}
}