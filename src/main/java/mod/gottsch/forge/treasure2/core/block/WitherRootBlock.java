/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

import java.util.Random;

import mod.gottsch.forge.gottschcore.block.FacingBlock;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.CollidingParticleType;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Mar 26, 2018
 *
 */
public class WitherRootBlock extends FacingBlock implements ITreasureBlock, IMistSupport {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
	
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] shapes = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherRootBlock(Block.Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(3.0F));
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, Boolean.valueOf(false)));
		setShapes(
			new VoxelShape[] {
					Block.box(3, 0, 0, 13, 4, 15),	// S
					Block.box(0, 0, 4, 15, 4, 12),	// W
					Block.box(3, 0, 0, 13, 4, 15),	// N
					Block.box(0, 0, 4, 15, 4, 12)	// E
			});
	}
	
	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED, FACING);
	}

	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {

		if (!Config.CLIENT.gui.enableFog.get()) {
			return;
		}

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		if (!isMistAllowed(world, random, x, y, z)) {
			return;
		}

		double xPos = (x + 0.5D) + (random.nextFloat() * 2.0D) - 1D;
		double yPos = y + 0.125;
		double zPos = (z + 0.5D) + (random.nextFloat() * 2.0D) - 1D;

		// NOTE can override methods here as it is a factory that creates the particle
		CollidingParticleType mistType = TreasureParticles.WITHER_MIST_PARTICLE.get();
		
		try {
			((ClientLevel)world).addParticle(mistType, false, xPos, yPos, zPos, 0, 0, 0);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH:
			return shapes[0];
		case EAST:
			return shapes[1];
		case SOUTH:
			return shapes[2];
		case WEST:
			return shapes[3];
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getShapes() {
		return shapes;
	}

	public WitherRootBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}

}