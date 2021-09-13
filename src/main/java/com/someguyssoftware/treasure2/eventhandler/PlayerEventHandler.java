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
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.IWishable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
public class PlayerEventHandler {

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().level)) {
			return;
		}
		//		Treasure.LOGGER.debug("is remote? -> {}", !event.getPlayer().level.isClientSide);
				Treasure.LOGGER.debug("{} tossing item -> {}", event.getPlayer().getName().getString(), event.getEntityItem().getItem().getDisplayName().getString());
		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString(IWishable.DROPPED_BY_KEY, event.getPlayer().getName().getString());
			//			Treasure.LOGGER.debug("adding nbt tag to wishable stack...");
			stack.setTag(nbt);			
		}		
	}
}