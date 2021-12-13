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

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerBlockEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;

import net.minecraft.world.level.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;

/**
 * 
 * @author Mark Gottschling on Sep 14, 2020
 *
 */
public class GravestoneSpawnerBlock extends GravestoneBlock	implements ITreasureBlock, ITileEntityProvider {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneSpawnerBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
	}

	@Override
	public TileEntity newBlockEntity(LevelAccessor world) {
		Treasure.LOGGER.debug("created spawner te");
		GravestoneProximitySpawnerTileEntity tileEntity = new GravestoneProximitySpawnerTileEntity(TreasureTileEntities.GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE);
		return (TileEntity) tileEntity;
	}
}