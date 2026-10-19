/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fallen withered leaves on the Wither Grove's floor: one to four per block, like pink petals,
 * and stacked the same way by placing more on them. Unlike petals, bone meal does nothing; they are dead,
 * and they lie on any solid top, not just dirt.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitheredLeafLitterBlock extends PinkPetalsBlock implements ITreasureBlock {

	public WitheredLeafLitterBlock(Properties properties) {
		super(properties);
	}

	/** dead leaves settle on anything with a solid top: logs and stones as well as dirt */
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isFaceSturdy(level, pos, Direction.UP);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
}
