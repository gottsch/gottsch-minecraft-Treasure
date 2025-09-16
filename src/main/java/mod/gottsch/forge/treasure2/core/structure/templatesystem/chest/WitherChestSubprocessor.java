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

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import net.minecraft.util.RandomSource;

/**
 * @author by Mark Gottschling on 9/3/2025
 */
public class WitherChestSubprocessor extends ChestSubprocessor {

    public WitherChestSubprocessor() {
    }

    public WitherChestSubprocessor(ChestSubprocessorData data) {
        super(data);
    }

    // TODO override addLocks() to only use the Wither Lock

    @Override
    public int randomizedNumberOfLocks(RandomSource random, LockLayout lockLayout) {
        return RandomHelper.randomInt(random, 1, lockLayout.getMaxLocks());
    }
}
