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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.item.Adornment;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BaublesEquipmentCharmHandler implements IEquipmentCharmHandler {

	@Override
	public List<CharmContext> handleEquipmentCharms(Event event, EntityPlayerMP player) {
		final List<CharmContext> contexts = new ArrayList<>(5);
		if (BaublesIntegration.isEnabled()) {
			AtomicInteger slot = new AtomicInteger(-1);
			for (Integer baublesSlot : BaublesIntegration.BAUBLES_SLOTS) {
				slot.set(baublesSlot);
				ItemStack itemStack = BaublesIntegration.getStackInSlot(player, baublesSlot);
				if (itemStack != ItemStack.EMPTY && itemStack.getItem() instanceof Adornment) {
					if (itemStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
						ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
						if (cap.isExecuting()) {
							for (InventoryType type : InventoryType.values()) {
								AtomicInteger index = new AtomicInteger();
								for (int i = 0; i < cap.getCharmEntities().get(type).size(); i++) {
									ICharmEntity entity = ((List<ICharmEntity>)cap.getCharmEntities().get(type)).get(i);
									if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
										continue;
									}
									index.set(i);
									CharmContext context = new CharmContext.Builder().with($ -> {
										$.slotProviderId = BaublesIntegration.BAUBLES_MOD_ID;
										$.slot = slot.get();
										$.itemStack = itemStack;
										$.capability = cap;
										$.type = type;
										$.index = index.get();
										$.entity = entity;
									}).build();
									contexts.add(context);
								}
							}
						}
					}
				}
			}
		}
		return contexts;
	}

}
