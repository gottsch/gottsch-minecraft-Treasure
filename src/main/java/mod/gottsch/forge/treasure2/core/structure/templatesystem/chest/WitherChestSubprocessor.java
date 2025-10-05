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
package mod.gottsch.forge.treasure2.core.structure.templatesystem.chest;

import mod.gottsch.forge.gottschcore.spatial.Rotate;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * @author by Mark Gottschling on 9/3/2025
 */
public class WitherChestSubprocessor extends ChestSubprocessor {

    public WitherChestSubprocessor() {
    }

    public WitherChestSubprocessor(ChestSubprocessorData data) {
        super(data);
    }

    @Override
    public int randomizedNumberOfLocks(RandomSource random, LockLayout lockLayout) {
        // 1 to 2 locks
        return random.nextInt(2) + 1;
    }

    @Override
    public void addLocks(RandomSource random, CompoundTag tag, LockLayout layout, IRarity defaultRarity, Rotate rotate) {
        // determine the number of locks to use
        int numLocks = randomizedNumberOfLocks(random, layout);
        // force chest to use 1 bone lock
        addLocks(tag, random, layout, List.of((LockItem) TreasureItems.WITHER_LOCK.get()), numLocks, rotate);
    }
}
