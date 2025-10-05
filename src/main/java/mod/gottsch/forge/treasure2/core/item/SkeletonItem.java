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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.treasure2.core.block.SkeletonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonItem extends BlockItem {
//	public static final int MAX_STACK_SIZE = 64;

	/**
	 * 
	 * @param block
	 * @param properties
	 */
	public SkeletonItem(Block block, Item.Properties properties) {
		super(block, properties); //.stacksTo(MAX_STACK_SIZE));
	}

	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
		BlockPos blockPos = context.getClickedPos().relative(state.getValue(SkeletonBlock.FACING).getOpposite());
		BlockContext blockContext = new BlockContext(context.getLevel(), blockPos);
		if (blockContext.isAir() || blockContext.isReplaceable()) {
			return context.getLevel().setBlock(context.getClickedPos(), state, 26);
		}
		return false;
	}
}