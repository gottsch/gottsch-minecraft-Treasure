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

import static com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType.IMBUE;
import static com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType.INNATE;
import static com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType.SOCKET;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY_CAPABILITY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.WealthItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
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

//			Treasure.LOGGER.debug("left stack imbuable -> {}", leftItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false));
//			Treasure.LOGGER.debug("right stack imbuing -> {}", rightItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false));
			
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

			// add bindable (charm) to socketable (ex. adornment)
			else if (leftItemStack.getCapability(CHARMABLE).map(cap -> cap.isSocketable()).orElse(false) &&
					rightItemStack.getCapability(CHARMABLE).map(cap -> cap.isBindable()).orElse(false)) {
				event.setCost(2);
				Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, SOCKET);
				if (outputItemStack.isPresent()) {
					event.setOutput(outputItemStack.get());
				}
			}
			// add imbuing (charm) to imbuable (ex. adornment)
			else if (leftItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false) && // ie adornment
					rightItemStack.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false)) { // ie charm book
//				Treasure.LOGGER.debug("in the imbuing process");
				event.setCost(2);
				Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, IMBUE);
				if (outputItemStack.isPresent()) {
					event.setOutput(outputItemStack.get());
				}
			}
			// add gem to adornment
			else if ((leftItemStack.getItem() instanceof Adornment) &&
					leftItemStack.getCapability(CHARMABLE).map(cap -> cap.getSourceItem().equals(Items.AIR.getRegistryName())).orElse(false) &&
					TreasureCharms.isSourceItemRegistered(rightItemStack.getItem().getRegistryName())) {

				event.setCost(1);
				// build the output item, duplicating the left stack (adornment) with the right stack as the source item
				ItemStack outputStack = createCharmableOutputStack(leftItemStack.getItem(), 
						rightItemStack.getItem().getRegistryName(), 
						leftItemStack.getCapability(CHARMABLE).map(cap -> cap.getCharmEntities()).orElse( (ArrayList<ICharmEntity>[])new ArrayList[3]));
				
				// set some extra properties
				outputStack.getCapability(CHARMABLE).ifPresent(cap -> {
					leftItemStack.getCapability(CHARMABLE).ifPresent(leftCap -> {
						cap.setHighestLevel(leftCap.getHighestLevel());
						// set the hover name
						if (cap.isNamedByMaterial() || cap.isNamedByCharm()) {
							TreasureCharmables.setHoverName(outputStack);
						}
//						TreasureAdornments.setHoverName(outputStack);
					});
				});
				event.setOutput(outputStack);
			}
		}

		/**
		 * 
		 * @param event
		 * @param source
		 * @param dest
		 */
		public static Optional<ItemStack> transferCharmToOutput(ItemStack source, ItemStack dest, InventoryType destInventoryType) {
			AtomicReference<Optional<ItemStack>> ref = new AtomicReference<>(Optional.empty());
			// determine if dest is a high enough level for source's bindable level
			source.getCapability(CHARMABLE).ifPresent(sourceCap -> {
				dest.getCapability(CHARMABLE).ifPresent(destCap -> {
//					Treasure.LOGGER.debug("book has innate charms -> {}", sourceCap.getCharmEntities()[InventoryType.INNATE.getValue()].size() > 0);
					// looking for innate
					if (sourceCap.getCharmEntities()[InventoryType.INNATE.getValue()].size() > 0) {
						// sort the source charm entity list in descending order
						List<ICharmEntity> innateCharms = sourceCap.getCharmEntities()[INNATE.getValue()];
						Comparator<ICharmEntity> comparator = Collections.reverseOrder(new CharmableCapability.SortByLevel());						
						Collections.sort(innateCharms, comparator);
//						Treasure.LOGGER.debug("size of innate charms -> {}", innateCharms.size());
						// duplicate the dest cap charm entities
						@SuppressWarnings("unchecked")
						ArrayList<ICharmEntity>[] outputEntities = (ArrayList<ICharmEntity>[])new ArrayList[3];
						outputEntities[INNATE.getValue()] = new ArrayList<>(destCap.getCharmEntities()[INNATE.getValue()]);
						outputEntities[IMBUE.getValue()] = new ArrayList<>(destCap.getCharmEntities()[IMBUE.getValue()]);
						outputEntities[SOCKET.getValue()] = new ArrayList<>(destCap.getCharmEntities()[SOCKET.getValue()]);

						// process each charm entity
						for (ICharmEntity entity : innateCharms) {
							if (destCap.getMaxCharmLevel() >= entity.getCharm().getLevel()) {
								addCharmEntity(entity, destCap,  destInventoryType, outputEntities[ destInventoryType.getValue()]);
							}						
						}
//						Treasure.LOGGER.debug("result has more charms than input -> {}", outputEntities[destInventoryType.getValue()].size() > destCap.getCharmEntities()[ destInventoryType.getValue()].size());
						// test if resulting output charms is larger than the original (dest) ie charms were added.
						if (outputEntities[destInventoryType.getValue()].size() > destCap.getCharmEntities()[ destInventoryType.getValue()].size()) {
							ItemStack outputStack = createCharmableOutputStack(dest.getItem(), destCap.getSourceItem(), outputEntities);
//							Treasure.LOGGER.debug("output stack -> {}", outputStack.getDisplayName().getString());
							// set the hover name
							if (destCap.isNamedByMaterial() || destCap.isNamedByCharm()) {
								TreasureCharmables.setHoverName(outputStack);
							}
//							TreasureAdornments.setHoverName(outputStack);
							ref.set(Optional.of(outputStack));
						}
					}
				});
			});
			return ref.get();
		}

		/**
		 * 
		 * @param entity
		 * @param destCap
		 * @param type
		 * @param outputCharms
		 * @return
		 */
		private static ICharmEntity addCharmEntity(ICharmEntity entity, ICharmableCapability destCap, InventoryType type, List<ICharmEntity> outputCharms) {
			if (outputCharms.size() < destCap.getMaxSize(type)) {	
				outputCharms.add(entity);
			}
			return entity;
		}

		/**
		 * 
		 * @param item
		 * @param sourceItem
		 * @param charmEntities
		 * @return
		 */
		private static ItemStack createCharmableOutputStack(Item item, ResourceLocation sourceItem, List<ICharmEntity>[] charmEntities) {
			// copy the dest cap to the output cap
			ItemStack outputStack = new ItemStack(item);
			// set the source item
			outputStack.getCapability(CHARMABLE).ifPresent(outputCap -> {
				outputCap.setSourceItem(sourceItem);
				// add all the charms over
				for (int index = 0; index < 3; index++) {
					for (ICharmEntity e : charmEntities[index]) {
						outputCap.add(InventoryType.getByValue(index), e);
					}
				}
			});
			return outputStack;
		}

		@Deprecated
		private static Optional<ItemStack> createCharmOutputStack(Item item, ICharmEntity entity, ICharmableCapability destinationCap, InventoryType type) {
			ItemStack outputStack = new ItemStack(item);
			AtomicReference<ItemStack> stackRef = new AtomicReference<>();			
			// first check if the existing destination inventory can hold more charms
			if (destinationCap.getCharmEntities()[type.getValue()].size() < destinationCap.getMaxSize(type)) {		
				outputStack.getCapability(CHARMABLE).ifPresent(outputCap -> {
					// copy existing charms from destination to output
					outputCap.setCharmEntities(destinationCap.getCharmEntities());
					// copy charm entity to output innate
					outputCap.add(type, entity);
					// reference the output
					stackRef.set(outputStack);
				});
			}
			return stackRef.get() == null ? Optional.empty() : Optional.of(stackRef.get());
		}

		/**
		 * 
		 * @param left
		 * @param right
		 * @return 
		 * @return
		 */
		@Deprecated
		public static void doImbueItem(AnvilUpdateEvent event, ItemStack left, ItemStack right) {
			//			ItemStack outputItem = new ItemStack(right.getItem());

			// TODO determine if the right is a high enough level
			left.getCapability(CHARMABLE).ifPresent(leftCap -> {
				Treasure.LOGGER.debug("have left cap");
				right.getCapability(CHARMABLE).ifPresent(rightCap -> {
					Treasure.LOGGER.debug("have right cap");
					ICharmEntity leftEntity = leftCap.getCharmEntities()[INNATE.getValue()].get(0);	
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
							output = createCharmOutputStack(right.getItem(), leftEntity, rightCap, INNATE);
						}
						else {
							Treasure.LOGGER.debug("charm is not a source -> do imbue");
							// check the imbue size
							output = createCharmOutputStack(right.getItem(), leftEntity, rightCap, IMBUE);
						}
						if (output.isPresent()) {
							Treasure.LOGGER.debug("output is present");
							event.setOutput(output.get());
						}
					}
				});
			});
		}
	}
}
