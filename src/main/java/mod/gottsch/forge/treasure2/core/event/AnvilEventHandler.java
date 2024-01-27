/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;

/**
 * 
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class AnvilEventHandler {
	
	@EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class RegistrationHandler {

		private static final int MAX_DURABILITY = 100;

		@SubscribeEvent
		public static void onAnvilUpdate(AnvilUpdateEvent event) {
			ItemStack leftStack = event.getLeft();
			ItemStack rightStack = event.getRight();

			// check for KeyItems and having the durability capability
			if (leftStack.getItem() instanceof KeyItem
					&& leftStack.getItem() == rightStack.getItem()
					&& leftStack.getCapability(DURABILITY).isPresent()
					&& leftStack.getCapability(DURABILITY).map(h -> !h.isInfinite()).orElse(true)					
					&& rightStack.getCapability(DURABILITY).isPresent()
					&& rightStack.getCapability(DURABILITY).map(h -> !h.isInfinite()).orElse(true)
					) {

				event.setCost(1);
				LazyOptional<IDurabilityHandler> leftHandler = leftStack.getCapability(DURABILITY);
				LazyOptional<IDurabilityHandler> rightHandler = rightStack.getCapability(DURABILITY);
				int leftDurability = leftHandler.map(c -> c.getDurability()).orElse(leftStack.getMaxDamage());
				int rightDurability = rightHandler.map(c -> c.getDurability()).orElse(rightStack.getMaxDamage());

				int leftRemainingUses = leftDurability - leftStack.getDamageValue();
				int rightRemainingUses = rightDurability - rightStack.getDamageValue();

				// create output stack
				ItemStack outputStack = new ItemStack(leftStack.getItem());
				LazyOptional<IDurabilityHandler> outputHandler = outputStack.getCapability(DURABILITY);

				Treasure.LOGGER.debug("left damange -> {}", leftStack.getDamageValue());
				Treasure.LOGGER.debug("right damange -> {}", rightStack.getDamageValue());

				int remainingUses = leftRemainingUses + rightRemainingUses;
				if (remainingUses > Math.max(leftDurability, rightDurability)) {
					outputHandler.ifPresent(cap -> cap.setDurability(Math.min(remainingUses, MAX_DURABILITY)));
					outputStack.setDamageValue(0);
				}
				else {
					if (remainingUses < Math.min(leftDurability, rightDurability)) {
						outputHandler.ifPresent(cap -> cap.setDurability(Math.min(leftDurability, rightDurability)));
					}
					else {
						outputHandler.ifPresent(cap -> cap.setDurability(Math.max(leftDurability, rightDurability)));
					}
					outputStack.setDamageValue(outputHandler.map(c -> c.getDurability()).orElse(leftStack.getMaxDamage()) - remainingUses);
				}
				event.setOutput(outputStack);
			}
		}
	}
}