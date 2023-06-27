/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.gottschcore.block.AbstractProximityBlock;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureProximitySpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An invisible non-collision block (like AIR) that creates an IProximityTileEntity.
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class ProximityBlock extends AbstractProximityBlock implements ITreasureBlock {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public ProximityBlock(Block.Properties properties) {
		super(properties);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		Treasure.LOGGER.debug("created proximity spawner be");
		TreasureProximitySpawnerBlockEntity blockEntity = null;
		try {
			blockEntity = new TreasureProximitySpawnerBlockEntity(pos, state);
			blockEntity.setProximity(5.0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (BlockEntity) blockEntity;
	}

	@Override
	public boolean isAir(BlockState state) {
		return true;
	}
}