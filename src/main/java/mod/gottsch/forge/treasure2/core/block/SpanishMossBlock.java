/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.block;

import java.util.Random;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.CollidingParticleType;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Mark Gottschling on Jul 25, 2018
 *
 */
public class SpanishMossBlock extends BushBlock implements ITreasureBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);

	/**
	 * 
	 */
	public SpanishMossBlock(Block.Properties properties) {
		super(properties.strength(0.6F).noCollission().instabreak().sound(SoundType.WET_GRASS));
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}

	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED);
	}

	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {

		// NOTE 12/22/2024 - moss emits poisonous fog again. this needs to be a server config option
		if (!Config.SERVER.witherTree.enablePoisonFog.get()) {
			return;
		}

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		if (RandomHelper.checkProbability(new Random(), 75D)) {
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
		double velocityY = -0.1; //0
		double velocityZ = 0;

		// NOTE can override methods here as it is a factory that creates the particle
		try {
			if (world.random.nextInt(10) < 8) {
				world.addParticle(TreasureParticles.SPANISH_MOSS_PARTICLE.get(), false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
			} else {
				CollidingParticleType mistType = TreasureParticles.POISON_MIST_PARTICLE.get();
				world.addParticle(mistType, false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}

	@Override
	public boolean isPathfindable(BlockState blockState, BlockGetter getter, BlockPos blockPos, PathComputationType pathType) {
		return pathType == PathComputationType.AIR && !this.hasCollision ? true : super.isPathfindable(blockState, getter, blockPos, pathType);
	}

	@Override
	public boolean canBeReplaced(BlockState state, Fluid fluid) {
		return true;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}