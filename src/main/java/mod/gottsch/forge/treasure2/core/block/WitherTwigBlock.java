/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.block;

import mod.gottsch.forge.gottschcore.block.FacingBlock;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.Random;

/**
 * @author Mark Gottschling on Aug 22, 2024
 *
 */
public class WitherTwigBlock extends FacingBlock implements ITreasureBlock {
	private static final VoxelShape NORTH_SHAPE = Block.box(3, 1, 3, 13, 16, 16);
	private static final VoxelShape SOUTH_SHAPE = Block.box(3, 1, 0, 13, 16, 13);
	private static final VoxelShape EAST_SHAPE = Block.box(0, 1, 3, 13, 16, 13);
	private static final VoxelShape WEST_SHAPE = Block.box(3, 1, 3, 16, 16, 13);


	/**
	 *
	 */
	public WitherTwigBlock(Properties properties) {
		super(properties.strength(0.6F).noCollission().instabreak().sound(SoundType.WOOD));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {

		if (RandomHelper.checkProbability(new Random(), 90D)) {
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

		SimpleParticleType particle = TreasureParticles.BLACK_SPORE_PARTICLE.get();

		try {
			world.addParticle(particle, false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
			default:
			case NORTH:
				return NORTH_SHAPE;
			case EAST:
				return EAST_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
		}
	}
}