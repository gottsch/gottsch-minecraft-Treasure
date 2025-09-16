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
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlockProxy;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.BoneChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Sep 28, 2022
 *
 */
public class BoneKey extends KeyItem {
	
	public BoneKey(Properties properties) {
		super(properties);
	}

	public BoneKey(Item.Properties properties, int durability) {
		super(properties, durability);
	}

	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, worldIn, tooltip, flag);

		tooltip.add(
				Component.translatable(LangUtil.tooltip("key_lock.specials"),
						ChatFormatting.GOLD + Component.translatable(LangUtil.tooltip("key_lock.bone_key.specials")).getString())
		);
	}

	protected boolean useKeyOnLock(UseOnContext context, Block block, BlockState state, BlockPos chestPos, ITreasureChestBlockEntity blockEntity, LockState lockState) {
		BoneChestBlockEntity boneChestBlockEntity = (BoneChestBlockEntity)blockEntity;

		if (boneChestBlockEntity.isLocked()) {
			if (unlock(lockState.getLock())) {
				doUnlock(context, boneChestBlockEntity, lockState);
				boneChestBlockEntity.sendUpdates();
				return false; // key not broken, successfully unlocked
			}
			return true; // key broken, unlock failed
		} else {
			// lock the chest
			boneChestBlockEntity.setLocked(true);
			boneChestBlockEntity.sendUpdates();
			return false; // key not broken, successfully locked
		}

	}

	/**
	 * 
	 * @param context
	 * @param chestTileEntity
	 * @param lockState
	 */
	@Override
	public void doUnlock(UseOnContext context, ITreasureChestBlockEntity chestTileEntity, LockState lockState) {
		LockItem lock = lockState.getLock();		
		 lock.doUnlock(context.getLevel(), context.getPlayer(), context.getClickedPos(), lockState);
		 // NOTE does not drop the lock
	}
}
