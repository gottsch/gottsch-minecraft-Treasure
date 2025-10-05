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

import mod.gottsch.forge.gottschcore.block.AbstractProximityBlock;
import mod.gottsch.forge.gottschcore.block.entity.ProximitySpawnerBlockEntity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.ProximityMobSetSpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureProximityMultiSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * An invisible non-collision block (like AIR) that creates an ProximityMobSetSpawnerBlockEntity
 * @author Mark Gottschling on Aug 16, 2024
 *
 */
public class ProximityMobSetSpawnerBlock extends AbstractProximityBlock implements ITreasureBlock {

	/**
	 *
	 * @param properties
	 */
	public ProximityMobSetSpawnerBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		Treasure.LOGGER.debug("created proximity mobSet spawner entity");
		ProximityMobSetSpawnerBlockEntity blockEntity = null;
		try {
			blockEntity = new ProximityMobSetSpawnerBlockEntity(pos, state);
			blockEntity.setProximity(5.0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (BlockEntity) blockEntity;
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return !level.isClientSide() ? (lvl, pos, blockState, t) -> {
			if (t instanceof ProximityMobSetSpawnerBlockEntity entity) {
				entity.tickServer();
			}
		} : null;
	}

	@Override
	public boolean isAir(BlockState state) {
		return true;
	}
}