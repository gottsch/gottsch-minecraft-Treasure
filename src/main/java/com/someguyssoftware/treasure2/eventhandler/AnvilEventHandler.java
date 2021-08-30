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
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY_CAPABILITY;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.KeyItem;

import net.minecraft.item.Item;
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
			ItemStack leftItemStack = event.getLeft();
			ItemStack rightItemStack = event.getRight();

//			Treasure.LOGGER.debug("is imbuing -> {}", leftItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false));
//			Treasure.LOGGER.debug("is imbuable -> {}", rightItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false));
			
			// check for KeyItems and having the durability capability
			if (leftItemStack.getItem() instanceof KeyItem && leftItemStack.getCapability(DURABILITY_CAPABILITY).isPresent()
					&& rightItemStack.getItem() instanceof KeyItem &&  rightItemStack.getCapability(DURABILITY_CAPABILITY).isPresent()) {
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
//					if (Treasure.LOGGER.isDebugEnabled()) {
//						Treasure.LOGGER.debug("output has greater uses -> {} than max durability -> {} - update durability", remainingUses, Math.max(leftDurability, rightDurability));
//					}
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
			// else check for Book -> Charm or Book -> Adornment
//			else if (leftItemStack.getItem() instanceof CharmBook && rightItemStack.getCapability(TreasureCapabilities.CHARMABLE).isPresent()) {}

			// TODO add logging
			// TODO reenable when add adornments
//			else if (leftItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false) &&
//					rightItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false)) {
//				event.setCost(1);
//					if (Treasure.LOGGER.isDebugEnabled()) {
//						Treasure.LOGGER.debug("book -> charm");
//					}
//					doImbueItem(event, leftItemStack, rightItemStack);
//			}
		}
		
		/**
		 * 
		 * @param left
		 * @param right
		 * @return 
		 * @return
		 */
		public static void doImbueItem(AnvilUpdateEvent event, ItemStack left, ItemStack right) {
//			ItemStack outputItem = new ItemStack(right.getItem());
			
			// TODO determine if the right is a high enough level
			left.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(leftCap -> {
				Treasure.LOGGER.debug("have left cap");
				right.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(rightCap -> {
					Treasure.LOGGER.debug("have right cap");
					ICharmEntity leftEntity = leftCap.getCharmEntities()[InventoryType.INNATE.getValue()].get(0);	
					if (Treasure.LOGGER.isDebugEnabled()) {
						Treasure.LOGGER.debug("leftEntity.level -> {}, rightCap.maxLevel -> {}", leftEntity.getCharm().getLevel(), rightCap.getMaxCharmLevel());
					}
					if (rightCap.getMaxCharmLevel() >= leftEntity.getCharm().getLevel()) {
						Treasure.LOGGER.debug("charm can take imbuing");

						Optional<ItemStack> output;
						if (rightCap.isSource()) {
							Treasure.LOGGER.debug("charm is a source -> do innate");
							// TODO create method.  all the same steps, just different inventories
							// check the innate size
//							if (rightCap.getCharmEntities()[InventoryType.INNATE.getValue()].size() < rightCap.getMaxInnateSize()) {								
//								outputItem.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(outputCap -> {
//									// copy existing charms from right to output
//									outputCap.setCharmEntities(rightCap.getCharmEntities());
//									// copy left charm to output innate
//									outputCap.add(InventoryType.INNATE, leftEntity);
//									// TODO set the output
//								});
//							}
							output = createCharmOutputStack(right.getItem(), leftEntity, rightCap, InventoryType.INNATE);
						}
						else {
							Treasure.LOGGER.debug("charm is not a source -> do imbue");
							// check the imbue size
							output = createCharmOutputStack(right.getItem(), leftEntity, rightCap, InventoryType.IMBUE);
						}
						if (output.isPresent()) {
							Treasure.LOGGER.debug("output is present");
							event.setOutput(output.get());
						}
					}
				});
			});
		}
		
		private static Optional<ItemStack> createCharmOutputStack(Item item, ICharmEntity entity, ICharmableCapability rightCap, InventoryType type) {
			ItemStack outputStack = new ItemStack(item);
			AtomicReference<ItemStack> stackRef = new AtomicReference<>();
			if (rightCap.getCharmEntities()[type.getValue()].size() < rightCap.getMaxSize(type)) {				
				outputStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(outputCap -> {
					// copy existing charms from right to output
					outputCap.setCharmEntities(rightCap.getCharmEntities());
					// copy left charm to output innate
					outputCap.add(type, entity);
					// reference the output
					stackRef.set(outputStack);
				});
			}
			return stackRef.get() == null ? Optional.empty() : Optional.of(stackRef.get());
		}
	}
}
