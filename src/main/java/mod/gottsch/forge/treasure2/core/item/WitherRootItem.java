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

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.WitherBranchBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


/**
 * @author Mark Gottschling on Nov 14, 2014
 *
 */
public class WitherRootItem extends BlockItem {
	
	/**
	 * 
	 * @param block
	 * @param properties
	 */
	public WitherRootItem(Block block, Item.Properties properties) {
		super(block, properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (WorldInfo.isClientSide(context.getLevel())) {
            return InteractionResult.PASS;
        }
		BlockState state = TreasureBlocks.WITHERWOOD_ROOT.get().defaultBlockState().setValue(WitherBranchBlock.FACING, context.getHorizontalDirection().getOpposite());
		
 		ItemStack heldItem = context.getPlayer().getItemInHand(context.getHand());	     		
 		this.placeBlock(new BlockPlaceContext(context), state);     		
 		heldItem.shrink(1);            
        return InteractionResult.SUCCESS;
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @param itemStack
	 */
	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
		// set the block
		context.getLevel().setBlock(context.getClickedPos(), state, 3);
		return true;
	}
}