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

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * @author by Mark Gottschling on 9/16/2025
 */
public class BoneChestBlock extends StandardChestBlock {

    public BoneChestBlock(Class<? extends AbstractTreasureChestBlockEntity> blockEntityClass, LockLayout type) {
        super(blockEntityClass, type, Properties.of().mapColor(MapColor.WOOD));
    }

    public BoneChestBlock(Class<? extends AbstractTreasureChestBlockEntity> blockEntityClass, LockLayout type, Properties properties) {
        super(blockEntityClass, type, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        AbstractTreasureChestBlockEntity chestTileEntity = (AbstractTreasureChestBlockEntity) super.newBlockEntity(pos, state);
        chestTileEntity.getLockStates().get(0).setLock((LockItem) TreasureItems.BONE_LOCK.get());
        return chestTileEntity;
    }
}
