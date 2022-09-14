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

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.gottschcore.block.ModContainerBlock;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.inventory.CharmingTableContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * 
 * @author Mark Gottschling on Feb 2, 2022
 *
 */
public class CharmingTableBlock extends ModBlock {
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public CharmingTableBlock(String modID, String name, Properties properties) {
		super(modID, name, properties.sound(SoundType.STONE).strength(3.0F));
		
		// set the default shapes/shape (full block)
		VoxelShape shape = Block.box(0.5, 0, 0.5, 15.5, 16, 15.5);
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		Treasure.LOGGER.debug("trying to activate charming bench.");
		// exit if on the client
		if (!worldIn.isClientSide) {
			// get the container provider
			//			INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);			
			INamedContainerProvider namedContainerProvider = state.getMenuProvider(worldIn, pos);	

			//			playerIn.openGui(Treasure.instance, GuiHandler.CHARMING_BENCH, worldIn, pos.getX(), pos.getY(), pos.getZ());
			NetworkHooks.openGui((ServerPlayerEntity)player, namedContainerProvider, (packetBuffer)->{});
			return ActionResultType.CONSUME;
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, callable) -> {
			return new CharmingTableContainer(id, inventory, world, pos, inventory.player);
		}, new TranslationTextComponent("charming.table"));
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getBounds() {
		return bounds;
	}

	public CharmingTableBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
