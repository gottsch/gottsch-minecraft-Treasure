/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.WealthItem;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling on Sep 4, 2021
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ItemEventHandler {
	// caching mechanism
	public static Item lastTossed;
	public static boolean isWishable;
	
	// NOTE this has been superceded by PlayerEventHandler.playerTick()
	/**
	 * 
	 * @param event
	 */
//	@SubscribeEvent
//	public static void onTossWishableEvent(ItemTossEvent event) {
//		if (WorldInfo.isClientSide(event.getPlayer().level)) {
//			return;
//		}
//		
//		// TODO do we want to re-enable IWishable ? or just check tags ?
//		// then DROPPED_BY would have to go somewhere else
//		
//		//		Treasure.LOGGER.debug("is remote? -> {}", !event.getPlayer().level.isClientSide);
//		Treasure.LOGGER.debug("{} tossing item -> {}", event.getPlayer().getName().getString(), event.getEntityItem().getItem().getDisplayName().getString());
//		ItemStack stack = event.getEntityItem().getItem();
//		Item item = stack.getItem();
//
//		// short circuit using cache if the item is the same as the last non-wishable
//		if (item == lastTossed && !isWishable) {
//			return;
//		}
//		
//		if (item instanceof WealthItem || stack.is(TreasureTags.Items.WISHABLES)) {
//			CompoundTag nbt = new CompoundTag();
//			nbt.putUUID(WealthItem.DROPPED_BY, event.getPlayer().getUUID());
//			Treasure.LOGGER.debug("adding nbt tag to wishable stack...");
//			stack.setTag(nbt);
//			isWishable = true;
//		} else {
//			isWishable = false;
//		}
//		lastTossed = item;
//	}
}