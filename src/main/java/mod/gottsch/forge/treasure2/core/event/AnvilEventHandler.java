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
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import net.minecraft.world.item.ItemStack;
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
			ItemStack leftStack = event.getLeft();
			ItemStack rightStack = event.getRight();

			// check for the same type of KeyItems that are non-infinite
			if (leftStack.getItem() instanceof KeyItem && !((KeyItem)leftStack.getItem()).isInfinite()
					&& rightStack.getItem() instanceof KeyItem && !((KeyItem)rightStack.getItem()).isInfinite()
					&& leftStack.getItem() == rightStack.getItem()) {

				event.setCost(1);

				int leftDurability = ((KeyItem)leftStack.getItem()).getDurability(leftStack, leftStack.getMaxDamage());
				int rightDurability = ((KeyItem)rightStack.getItem()).getDurability(rightStack, rightStack.getMaxDamage());

				int leftRemainingUses = leftDurability - leftStack.getDamageValue();
				int rightRemainingUses = rightDurability - rightStack.getDamageValue();

				ItemStack outputStack = new ItemStack(leftStack.getItem());

				int remainingUses = leftRemainingUses + rightRemainingUses;
				if (remainingUses > Math.max(leftDurability, rightDurability)) {
					// merge the keys
					((KeyItem)outputStack.getItem()).setDurability(outputStack, leftDurability + rightDurability);
					outputStack.setDamageValue(leftStack.getDamageValue() + rightStack.getDamageValue());
				}
				else {
					if (remainingUses < Math.min(leftDurability,  rightDurability)) {
						((KeyItem)outputStack.getItem()).setDurability(outputStack, Math.min(leftDurability, rightDurability));
					}
					else {
						((KeyItem)outputStack.getItem()).setDurability(outputStack, Math.max(leftDurability, rightDurability));
					}
					outputStack.setDamageValue(leftStack.getDamageValue() + rightStack.getDamageValue());
				}
				event.setOutput(outputStack);
			}
		}
	}
}
		

