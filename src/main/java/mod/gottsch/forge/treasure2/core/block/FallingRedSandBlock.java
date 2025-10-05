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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Mark Gottschling on Feb 22, 2020
 *
 */
public class FallingRedSandBlock extends AbstractTreasureFallingBlock implements ITreasureBlock {

	public FallingRedSandBlock(Block.Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}
	
	@Override
	   public void onLand(Level worldIn, BlockPos pos, BlockState state, BlockState p_52071_, FallingBlockEntity entity) {
		worldIn.setBlockAndUpdate(pos, Blocks.RED_SAND.defaultBlockState());
	}
}