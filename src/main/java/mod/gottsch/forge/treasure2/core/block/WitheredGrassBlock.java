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

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

/**
 * Grey-brown, dead grass for the Wither Grove's clearings. A SnowyDirtBlock like podzol, so it never
 * spreads and never random-ticks; it drops dirt unless mined with silk touch (see the loot table). A
 * shovel makes a path and a hoe tills it, as with grass.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitheredGrassBlock extends SnowyDirtBlock implements ITreasureBlock {

	public WitheredGrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
		boolean clearAbove = context.getLevel().getBlockState(context.getClickedPos().above()).isAir();
		if (toolAction == ToolActions.SHOVEL_FLATTEN && clearAbove) {
			return Blocks.DIRT_PATH.defaultBlockState();
		}
		if (toolAction == ToolActions.HOE_TILL && clearAbove) {
			return Blocks.FARMLAND.defaultBlockState();
		}
		return super.getToolModifiedState(state, context, toolAction, simulate);
	}
}
