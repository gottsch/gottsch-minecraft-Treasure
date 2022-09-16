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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * 
 * @author Mark Gottschling on Aug 30, 2021
 *
 */
public class CuriosEquipmentCharmHandler implements IEquipmentCharmHandler {
	private static final String CURIOS_ID = "curios";
	private static final List<String> CURIOS_SLOTS = Arrays.asList("necklace", "bracelet", "ring", "charm");

	@Override
	public List<CharmContext> handleEquipmentCharms(Event event, ServerPlayerEntity player) {
		List<CharmContext> contexts = new ArrayList<>();

		// check curio slots
		LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
		handler.ifPresent(itemHandler -> {
			// curios type names -> head, necklace, back, bracelet, hands, ring, belt, charm, feet
			CURIOS_SLOTS.forEach(slot -> {
				Optional<ICurioStacksHandler> stacksOptional = itemHandler.getStacksHandler(slot);
				stacksOptional.ifPresent(stacksHandler -> {
					ItemStack curiosStack = stacksHandler.getStacks().getStackInSlot(0);
					curiosStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
						for (InventoryType type : InventoryType.values()) {
							AtomicInteger index = new AtomicInteger();
							// requires indexed for-loop
							for (int i = 0; i < cap.getCharmEntities().get(type).size(); i++) {
								ICharmEntity entity =  ((List<ICharmEntity>)cap.getCharmEntities().get(type)).get(i);
								if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
									//	Treasure.LOGGER.debug("charm type -> {} is not register for this event -> {}", entity.getCharm().getType(), event.getClass().getSimpleName());
									continue;
								}
								index.set(i);
								CharmContext curiosContext = new CharmContext.Builder().with($ -> {
									$.slotProviderId = CURIOS_ID;
									$.slot = slot;
									$.itemStack = curiosStack;
									$.capability = cap;
									$.type = type;
									$.index = index.get();
									$.entity = entity;
								}).build();
								contexts.add(curiosContext);
							}
						}
					});
				});
			});
		});
		return contexts;
	}
}
