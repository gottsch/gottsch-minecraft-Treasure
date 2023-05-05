/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.gottschcore.block.entity.ProximitySpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * TODO can't pass no-arg anymore in 1.18 - is this class still necessary?
 * Wrapper on PromixitySpawnerBlockEntity, providing a no-arg constructor that provides the block entity type to the super.
 * Researching why I couldn't pass the TET with reflection....
 * @author Mark Gottschling on Jan 29, 2021
 *
 */
public class TreasureProximitySpawnerBlockEntity extends ProximitySpawnerBlockEntity {

	public TreasureProximitySpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.TREASURE_PROXIMITY_SPAWNER_ENTITY_TYPE.get(), pos, state);
	}
}
