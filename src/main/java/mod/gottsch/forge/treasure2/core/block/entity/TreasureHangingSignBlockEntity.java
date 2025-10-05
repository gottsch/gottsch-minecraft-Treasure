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
package mod.gottsch.forge.treasure2.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * @author Mark Gottschling on Aug 25, 2024
 *
 */
public class TreasureHangingSignBlockEntity extends SignBlockEntity {

    public TreasureHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TreasureBlockEntities.TREASURE_HANGING_SIGN.get(), blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return TreasureBlockEntities.TREASURE_HANGING_SIGN.get();
    }
}
