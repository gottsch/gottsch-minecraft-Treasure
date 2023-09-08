/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.event;

import java.util.List;
import java.util.Objects;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.item.WealthItem;
import mod.gottsch.forge.treasure2.core.registry.WishableRegistry;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Mark Gottschling May 26, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public class PlayerEventHandler {

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;

		if (!(player instanceof ServerPlayer)) {
			return;
		}

		if (event.phase == TickEvent.Phase.END && player.tickCount % 5 == 0) {
			checkForWishables(player);
		}
	}

	/**
	 * 
	 * @param player
	 */
	private static void checkForWishables(Player player) {
		// gather items within x radius
		List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(Config.SERVER.wells.scanForItemRadius.get()));
		for (ItemEntity item : items) {
			// if non-wealth, wishables-tag item
			if (!(item.getItem().getItem() instanceof WealthItem) && item.getItem().is(TreasureTags.Items.WISHABLES)) {
				// if player is the source of the drop
				if (item.getOwner() != null && Objects.equals(item.getOwner().getUUID(), player.getUUID())) {
					// get the WishableHandler for the Item
					IWishableHandler handler = WishableRegistry.getHandler(item.getItem().getItem()).orElse(TreasureWishableHandlers.DEFAULT_WISHABLE_HANDLER);
					// check if valid location
					if (handler.isValidLocation(item)) {
						// generate loot
						handler.doWishable(item);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onItemInfo(ItemTooltipEvent event) {
		if (!(event.getItemStack().getItem() instanceof WealthItem) && event.getItemStack().is(TreasureTags.Items.WISHABLES)) {
			event.getToolTip().add(Component.translatable(LangUtil.tooltip("wishable")).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
		}
	}
}
