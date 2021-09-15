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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 30, 2021
 *
 */
public class HotbarEquipmentCharmHandler implements IEquipmentCharmHandler {
	private static final int MAX_HOTBAR_CHARMS = 4;

	@Override
	public List<CharmContext> handleEquipmentCharms(Event event, ServerPlayerEntity player) {
		final List<CharmContext> contexts = new ArrayList<>(5);
		AtomicInteger adornmentCount = new AtomicInteger(0);
		AtomicReference<String> hotbarSlotStr = new AtomicReference<>("");
		// check hotbar - get the context at each slot
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			hotbarSlotStr.set(String.valueOf(hotbarSlot));
			ItemStack inventoryStack = player.inventory.getItem(hotbarSlot);
			if (inventoryStack != player.getItemInHand(Hand.MAIN_HAND)) {
				if (inventoryStack.getCapability(CHARMABLE).map(cap -> cap.isExecuting()).orElse(false)) {
					inventoryStack.getCapability(CHARMABLE).ifPresent(cap -> {
						for (InventoryType type : InventoryType.values()) {
							AtomicInteger index = new AtomicInteger();
							// requires indexed for-loop
							for (int i = 0; i < cap.getCharmEntities()[type.getValue()].size(); i++) {
								ICharmEntity entity =  cap.getCharmEntities()[type.getValue()].get(i);
								if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
									continue;
								}
								index.set(i);
								CharmContext context  = new CharmContext.Builder().with($ -> {
									$.slotProviderId = "minecraft";
									$.slot =String.valueOf(hotbarSlotStr.get());
									$.itemStack = inventoryStack;
									$.capability = cap;
									$.type = type;
									$.index = index.get();
									$.entity = entity;
								}).build();
								contexts.add(context);								
							}
						}
					});
					adornmentCount.getAndIncrement();
					if (adornmentCount.get() >= MAX_HOTBAR_CHARMS) {
						break;
					}
				}
			}
		}
		return contexts;
	}
}