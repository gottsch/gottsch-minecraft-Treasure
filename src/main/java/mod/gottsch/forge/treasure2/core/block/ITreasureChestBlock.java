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

import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 
 * @author Mark Gottschling on Nov 15, 2022
 *
 */
public interface ITreasureChestBlock {	
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
	public static final BooleanProperty DISCOVERED = BooleanProperty.create("discovered");
	
	/**
	 * Convenience method.
	 * @param state
	 * @return
	 */
	public static Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}
	
	Class<?> getBlockEntityClass();

	LockLayout getLockLayout();

    IRarity getRarity(HolderLookup.Provider provider);

    // TODO review - shouldn't need these in general
	// maybe create a method createBounds() that take a functional interface instead.
	VoxelShape[] getBounds();

	ITreasureChestBlock setBounds(VoxelShape[] bounds);

    int getInventorySize();
}