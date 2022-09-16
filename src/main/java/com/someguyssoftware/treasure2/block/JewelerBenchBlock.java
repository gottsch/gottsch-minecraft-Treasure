/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.treasure2.inventory.JewelerBenchContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * 
 * @author Mark Gottschling on Feb 2, 2022
 *
 */
public class JewelerBenchBlock extends FacingBlock implements ITreasureBlock {
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];
	
	private static final VoxelShape SHAPE = Block.box(0, 4, 0, 16, 16, 16);
//	private static final VoxelShape AABB = VoxelShapes.or(TOP_PART, NW_PART, NE_PART, SW_PART, SE_PART);
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public JewelerBenchBlock(String modID, String name, Properties properties) {
		super(modID, name, properties.sound(SoundType.STONE).strength(3.0F));

		setBounds(
				new VoxelShape[] {
						SHAPE, 	// N
						SHAPE,  	// E
						SHAPE,  	// S
						SHAPE// W
				});
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.getValue(FACING)) {
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
	
	/**
	 * 
	 */
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		// exit if on the client
		if (!worldIn.isClientSide) {
			// get the container provider
			INamedContainerProvider namedContainerProvider = state.getMenuProvider(worldIn, pos);	

			NetworkHooks.openGui((ServerPlayerEntity)player, namedContainerProvider, (packetBuffer)->{});
			return ActionResultType.CONSUME;
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, callable) -> {
			return new JewelerBenchContainer(id, inventory, world, pos, inventory.player);
		}, new TranslationTextComponent("jeweler.bench"));
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getBounds() {
		return bounds;
	}

	public JewelerBenchBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
