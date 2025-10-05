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


import java.util.List;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.wishable.IWishable;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;


/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class WealthItem extends Item implements IWishable {

	/**
	 * 
	 * @param properties
	 */
	public WealthItem(Properties properties) {
		// TODO can't do this here as the config values won't be read in yet. ie items are init before config
		// could apply some config settings after config is init with reflection
		super(properties); //.stacksTo(Config.SERVER.wealth.wealthMaxStackSize.get()));
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// standard coin info
		tooltip.add(Component.translatable(LangUtil.tooltip("wishable")).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
	}

	/**
	 * 
	 */
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		Level level = entityItem.level();
		if (WorldInfo.isClientSide(level)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}

		// check if registered
		if (!stack.is(TreasureTags.Items.WISHABLES)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}

		// get the wishable handler
		IWishableHandler handler = getHandler();
		if (handler.isValidLocation(entityItem)) {
			handler.doWishable(entityItem);
			return true;
		}
		return super.onEntityItemUpdate(stack, entityItem);
	}

	@Override
	public IWishableHandler getHandler() {
		return TreasureWishableHandlers.DEFAULT_WISHABLE_HANDLER;
	}
}
