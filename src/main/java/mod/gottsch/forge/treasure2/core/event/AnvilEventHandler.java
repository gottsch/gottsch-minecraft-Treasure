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

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
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

			// check for KeyItems and having the durability capability
			if (leftStack.getItem() instanceof KeyItem && leftStack.getCapability(DURABILITY).isPresent()
					&& leftStack.getCapability(DURABILITY).map(h -> !h.isInfinite()).orElse(true)					
					&& rightStack.getItem() instanceof KeyItem &&  rightStack.getCapability(DURABILITY).isPresent()
					&& rightStack.getCapability(DURABILITY).map(h -> !h.isInfinite()).orElse(true)) {

				event.setCost(1);
				LazyOptional<IDurabilityHandler> leftHandler = leftStack.getCapability(DURABILITY);
				LazyOptional<IDurabilityHandler> rightHandler = rightStack.getCapability(DURABILITY);
				int leftDurability = leftHandler.map(c -> c.getDurability()).orElse(leftStack.getMaxDamage());
				int rightDurability = rightHandler.map(c -> c.getDurability()).orElse(rightStack.getMaxDamage());

				int leftRemainingUses = leftDurability - leftStack.getDamageValue();
				int rightRemainingUses = rightDurability - rightStack.getDamageValue();

				ItemStack outputItem = new ItemStack(leftStack.getItem());
				LazyOptional<IDurabilityHandler> outputHandler = outputItem.getCapability(DURABILITY);

				int remainingUses = leftRemainingUses + rightRemainingUses;
				if (remainingUses > Math.max(leftDurability, rightDurability)) {
					outputHandler.ifPresent(cap -> cap.setDurability(Math.max(leftDurability, rightDurability) + leftStack.getMaxDamage()));
					outputItem.setDamageValue(leftStack.getDamageValue() + rightStack.getDamageValue());
				}
				else {
					if (remainingUses < Math.min(leftDurability,  rightDurability)) {
						outputHandler.ifPresent(cap -> cap.setDurability(Math.min(leftDurability, rightDurability)));
					}
					else {
						outputHandler.ifPresent(cap -> cap.setDurability(Math.max(leftDurability, rightDurability)));
					}
					outputItem.setDamageValue(outputHandler.map(c -> c.getDurability()).orElse(leftStack.getMaxDamage()) - remainingUses);
				}
				event.setOutput(outputItem);
			}
		}
	}
}
		
//			// add bindable (charm) to socketable (ex. adornment)
//			else if (leftStack.getCapability(CHARMABLE).map(cap -> cap.isSocketable()).orElse(false) &&
//					rightStack.getCapability(CHARMABLE).map(cap -> cap.isBindable()).orElse(false)) {
//				event.setCost(2);
//
//				// NOTE this block is just logging
//				leftStack.getCapability(RUNESTONES).ifPresent(cap -> {
//					cap.getEntities(InventoryType.INNATE).forEach(entity -> {
//						Treasure.LOGGER.debug("binding charm: sourceStack.appliedTo -> {}", entity.getAppliedTo());
//					});
//				});
//
//				AtomicReference<Optional<ItemStack>> atomicOutStack = new AtomicReference<>(Optional.empty());
//				// check that they charm type doesn't already exist on the adornment
//				leftStack.getCapability(CHARMABLE).ifPresent(cap -> {
//					if (cap.hasCharmType(rightStack, leftStack, InventoryType.INNATE, InventoryType.SOCKET)) {
//						return;
//					}
//					// check that there is room to add charms
//					if (cap.getCharmEntities().get(InventoryType.SOCKET).size() < cap.getMaxSocketSize()) {
//						// build the output item, add the charm to the adornment
//						Optional<ItemStack> outStack = TreasureAdornmentRegistry.transferCapabilities(rightStack, leftStack, InventoryType.INNATE, InventoryType.SOCKET);
//						if (outStack.isPresent()) {
//							outStack.get().getCapability(RUNESTONES).ifPresent(outCap -> {
//								//								if (outStack.get().hasCapability(RUNESTONES, null)) {
//								outCap.getEntities(InventoryType.SOCKET).forEach(entity -> {
//									entity.getRunestone().apply(outStack.get(), entity);
//								});
//								//								}
//							});
//							atomicOutStack.set(outStack);
//						}
//					}
//				});
//
//				if (atomicOutStack.get().isPresent()) {
//					event.setOutput(atomicOutStack.get().get());
//				}
//			}
//
//			// OLD pre-v2.0 way
//			//				Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, SOCKET);
//			//				if (outputItemStack.isPresent()) {
//			//					event.setOutput(outputItemStack.get());
//			//				}
//			//		}
//			
//			// add imbuing (charm) to imbuable (ex. adornment)
//			else if (leftStack.getCapability(CHARMABLE).map(cap -> cap.isImbuable()).orElse(false) && // ie adornment
//					rightStack.getCapability(CHARMABLE).map(cap -> cap.isImbuing()).orElse(false)) { // ie charm book
//				//				Treasure.LOGGER.debug("in the imbuing process");
//				event.setCost(2);
//				// TODO use the above method (binding) to transfer from book to adornment
//				Optional<ItemStack> outputItemStack = transferCapabilities(rightStack, leftStack, InventoryType.INNATE, InventoryType.IMBUE);
//				if (outputItemStack.isPresent()) {
//					event.setOutput(outputItemStack.get());
//				}
//			}
//			// add gem to adornment
//			else if ((leftStack.getItem() instanceof Adornment) &&
//					leftStack.getCapability(CHARMABLE).isPresent() &&
//					TreasureCharmableMaterials.isSourceItemRegistered(rightStack.getItem().getRegistryName())) {
//
//				leftStack.getCapability(CHARMABLE).ifPresent(cap -> {
//					if (cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
//						
//	        			// get the base and source materials
//	        			Optional<CharmableMaterial> baseMaterial = TreasureCharmableMaterials.getBaseMaterial(cap.getBaseMaterial());
//	        			Optional<CharmableMaterial> sourceMaterial = TreasureCharmableMaterials.getSourceItem(rightStack.getItem().getRegistryName());
//
//	        			if (baseMaterial.get().acceptsAffixer(rightStack) && sourceMaterial.get().canAffix(leftStack)) {
//	            			
//							event.setCost(1);
//							event.setMaterialCost(1);
//	
//							// build the output item, duplicating the left stack (adornment) with the right stack as the source item
//							Optional<Adornment> adornment = getAdornment(leftStack, rightStack);
//							Treasure.LOGGER.debug("adornment -> {}", adornment.get().getRegistryName());
//							if (adornment.isPresent()) {
//								ItemStack outputStack = copyStack(leftStack, new ItemStack(adornment.get()));
//								outputStack.getCapability(CHARMABLE).ifPresent(outputCap -> {
//									outputCap.setHighestLevel(cap.getHighestLevel());
//								});
//								event.setOutput(outputStack);
//						}
//						}
//					}
//				});
//			}
//		}
//
//		@Deprecated
//		public static Optional<ItemStack> transferCapabilities(ItemStack source, ItemStack dest, InventoryType sourceType, InventoryType destType) {
//			Treasure.LOGGER.debug("transfering caps...");
//
//			// create a new dest item stack
//			ItemStack stack = new ItemStack(dest.getItem());
//
//			/*
//			 * transfer existing state of dest to stack plus any relevant state from source to stack
//			 */
//			dest.getCapability(TreasureCapabilities.DURABILITY).ifPresent(cap -> {
//				cap.copyTo(stack);
//			});
//
//			// transfer
//			AtomicBoolean charmSizeChanged = new AtomicBoolean(false);
//			AtomicBoolean runeSizeChanged = new AtomicBoolean(false);
//
//			stack.getCapability(CHARMABLE).ifPresent(stackCap -> {
//				stackCap.clearCharms();			
//				dest.getCapability(CHARMABLE).ifPresent(destCap -> {
//					destCap.copyTo(stack);
//					source.getCapability(CHARMABLE).ifPresent(sourceCap -> {
//						sourceCap.transferTo(stack, sourceType, destType);
//						// check if size has changed. indicates at least 1 charm was transfered. if not, return empty
//						if (stackCap.getCurrentSize(destType) > destCap.getCurrentSize(destType)) {
//							charmSizeChanged.set(true);
//						}
//					});
//				});
//			});
//
//			dest.getCapability(RUNESTONES).ifPresent(destCap -> {
//				stack.getCapability(RUNESTONES).ifPresent(stackCap -> {
//					stackCap.clear();
//					Treasure.LOGGER.debug("before copyTo, runes size -> {}", stackCap.getEntitiesCopy().size());
//					destCap.copyTo(stack);
//					Treasure.LOGGER.debug("after copyTo, runes size -> {}", stackCap.getEntitiesCopy().size());
//					source.getCapability(RUNESTONES).ifPresent(sourceCap -> { // this is the rune
//						Treasure.LOGGER.debug("source(runestone)'s runes ->");
//						sourceCap.getEntities(InventoryType.INNATE).forEach(entity -> {
//							Treasure.LOGGER.debug("source entity -> {}", entity);
//						});
//						sourceCap.transferTo(stack, sourceType, destType); // transfer from rune to output
//						Treasure.LOGGER.debug("after transferTo, runes size -> {}", stackCap.getEntitiesCopy().size());
//						if (stackCap.getCurrentSize(destType) > destCap.getCurrentSize(destType)) {
//							runeSizeChanged.set(true);
//						}
//					});					
//				});
//			});
//			
//			if (charmSizeChanged.get() | runeSizeChanged.get()) {
//				return Optional.of(stack);
//			}
//			return Optional.empty();
//		}
//
//		/**
//		 * 
//		 * @param entity
//		 * @param destCap
//		 * @param type
//		 * @param outputCharms
//		 * @return
//		 */
//		private static ICharmEntity addCharmEntity(ICharmEntity entity, ICharmableCapability destCap, InventoryType type, List<ICharmEntity> outputCharms) {
//			if (outputCharms.size() < destCap.getMaxSize(type)) {	
//				outputCharms.add(entity);
//			}
//			return entity;
//		}
//
//		/**
//		 * 
//		 * @param item
//		 * @param sourceItem
//		 * @param charmEntities
//		 * @return
//		 */
//		private static ItemStack createCharmableOutputStack(Item item, ResourceLocation sourceItem, List<ICharmEntity>[] charmEntities) {
//			// copy the dest cap to the output cap
//			ItemStack outputStack = new ItemStack(item);
//			// set the source item
//			outputStack.getCapability(CHARMABLE).ifPresent(outputCap -> {
//				outputCap.setSourceItem(sourceItem);
//				// add all the charms over
//				for (int index = 0; index < 3; index++) {
//					for (ICharmEntity e : charmEntities[index]) {
//						outputCap.add(InventoryType.getByValue(index), e);
//					}
//				}
//			});
//			return outputStack;
//		}
//	}
//	
//	@Deprecated
//	// use TreasureAdornments version
//	private static Optional<Adornment> getAdornment(ItemStack baseStack, ItemStack stoneStack) {
//		AtomicReference<Optional<Adornment>> ref = new AtomicReference<>(Optional.empty());
//		if (baseStack.getCapability(TreasureCapabilities.CHARMABLE).isPresent() && baseStack.getItem() instanceof Adornment) {
//			baseStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
//				Adornment sourceAdornment = (Adornment) baseStack.getItem();
//				Optional<Adornment> adornment = TreasureAdornmentRegistry.get(sourceAdornment.getType(), sourceAdornment.getSize(), cap.getBaseMaterial(), stoneStack.getItem().getRegistryName());
//				ref.set(adornment);
//			});
//		}
//		return ref.get();
//	}
//	
//	/**
//	 * 
//	 * @param dest
//	 * @param sourceItem
//	 * @param charmEntities
//	 * @return
//	 */
//	@Deprecated
//	// use TreasureAdornments version
//	private static ItemStack copyStack(final ItemStack source, final ItemStack dest) {
//		ItemStack resultStack = dest.copy(); // <-- is this necessary?
//		// save the source item
//		ResourceLocation sourceItem = resultStack.getCapability(CHARMABLE).map(c -> c.getSourceItem()).orElse(null);
//
//		// copy item damage
//		resultStack.setDamageValue(source.getDamageValue());
//
//		// copy the capabilities
//		source.getCapability(TreasureCapabilities.DURABILITY).ifPresent(cap -> {
//			Treasure.LOGGER.debug("calling durability copyTo()");
//			cap.copyTo(resultStack);
//		});
//
//		resultStack.getCapability(CHARMABLE).ifPresent(resultCap -> {
//			resultCap.clearCharms();			
//			source.getCapability(CHARMABLE).ifPresent(sourceCap -> {
//				sourceCap.copyTo(resultStack);
//			});			
//		});
//
//
//		resultStack.getCapability(RUNESTONES).ifPresent(resultCap -> {
//			resultCap.clear();			
//			source.getCapability(RUNESTONES).ifPresent(sourceCap -> {
//				sourceCap.copyTo(resultStack);
//			});
//		});	
//
//		// reset the source item
//		resultStack.getCapability(CHARMABLE).ifPresent(cap -> {
//			cap.setSourceItem(sourceItem);
//		}); 
//		
//		return resultStack;
//	}
//}
