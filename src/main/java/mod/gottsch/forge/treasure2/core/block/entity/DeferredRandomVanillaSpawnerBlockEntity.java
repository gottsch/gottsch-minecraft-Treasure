/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.DungeonHooks;

/**
 *
 * @author Mark Gottschling on Sep 29, 2023
 *
 */
public class DeferredRandomVanillaSpawnerBlockEntity extends BlockEntity {

    public DeferredRandomVanillaSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.DEFERRED_RANDOM_VANILLA_SPAWNER_ENTITY_TYPE.get(), pos, state);
    }

    public void tickServer() {
        if (Config.SERVER.markers.enableSpawner.get()) {
            if (this.level.isClientSide()) {
                return;
            }
            getLevel().setBlock(getBlockPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity) getLevel().getBlockEntity(getBlockPos());
			if (spawnerBlockEntity != null) {
                EntityType<?> r = DungeonHooks.getRandomDungeonMob(getLevel().getRandom());
                spawnerBlockEntity.getSpawner().setEntityId(r, getLevel(), getLevel().getRandom(), getBlockPos());
            }
            else {
                Treasure.LOGGER.warn("unable to get spawner block entity at -> {}", getBlockPos());
            }
        }
    }
}
