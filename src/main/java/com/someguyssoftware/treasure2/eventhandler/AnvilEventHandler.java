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

import static com.someguyssoftware.treasure2.capability.DurabilityCapability.DURABILITY_CAPABILITY;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.DurabilityCapabilityProvider;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class AnvilEventHandler {
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void onAnvilUpdate(AnvilUpdateEvent event) {
			Treasure.LOGGER.debug("in anvil update event");
			ItemStack leftItemStack = event.getLeft();
			ItemStack rightItemStack = event.getRight();

			// add all uses/durability remaining in the right item to the left item.
			//		if (leftItemStack.getItem() == rightItemStack.getItem() && (leftItemStack.getItem() instanceof KeyItem)) {
			//			if (leftItemStack.hasCapability(DurabilityCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)
			//					&& rightItemStack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {
			//				
			//			}
			//		}
			if (leftItemStack.getCapability(DURABILITY_CAPABILITY).isPresent()
					&& rightItemStack.getCapability(DURABILITY_CAPABILITY).isPresent()) {
				Treasure.LOGGER.debug("has durability caps");
				event.setCost(1);
				LazyOptional<IDurabilityCapability> leftItemCap = leftItemStack.getCapability(DURABILITY_CAPABILITY);
				LazyOptional<IDurabilityCapability> rightItemCap = rightItemStack.getCapability(DURABILITY_CAPABILITY);
				int leftDurability = leftItemCap.map(c -> c.getDurability()).orElse(leftItemStack.getMaxDamage());
				int rightDurability = rightItemCap.map(c -> c.getDurability()).orElse(rightItemStack.getMaxDamage());

				int leftRemainingUses = leftDurability - leftItemStack.getDamageValue();
				int rightRemainingUses = rightDurability - rightItemStack.getDamageValue();

				ItemStack outputItem = new ItemStack(leftItemStack.getItem());
				LazyOptional<IDurabilityCapability> outputItemCap = outputItem.getCapability(DURABILITY_CAPABILITY);

				int remainingUses = leftRemainingUses + rightRemainingUses;
				if (remainingUses > Math.max(leftDurability, rightDurability)) {
					if (Treasure.LOGGER.isDebugEnabled()) {
						Treasure.LOGGER.debug("output has greater uses -> {} than max durability -> {} - update durability", remainingUses, Math.max(leftDurability, rightDurability));
					}
					outputItemCap.ifPresent(cap -> cap.setDurability(Math.max(leftDurability, rightDurability) + leftItemStack.getMaxDamage()));
					outputItem.setDamageValue(leftItemStack.getDamageValue() + rightItemStack.getDamageValue());
				}
				else {
					if (remainingUses < Math.min(leftDurability,  rightDurability)) {
						outputItemCap.ifPresent(cap -> cap.setDurability(Math.min(leftDurability, rightDurability)));
					}
					else {
						outputItemCap.ifPresent(cap -> cap.setDurability(Math.max(leftDurability, rightDurability)));
					}
					outputItem.setDamageValue(outputItemCap.map(c -> c.getDurability()).orElse(leftItemStack.getMaxDamage()) - remainingUses);
				}
				event.setOutput(outputItem);
			}
		}
	}
}
