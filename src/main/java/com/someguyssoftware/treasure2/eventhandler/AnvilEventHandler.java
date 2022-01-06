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
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.DurabilityCapability;
import com.someguyssoftware.treasure2.capability.DurabilityCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.GemItem;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class AnvilEventHandler {
	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 */
	public AnvilEventHandler(IMod mod) {
		setMod(mod);
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack leftItemStack = event.getLeft();
		ItemStack rightItemStack = event.getRight();

		// add all uses/damage remaining in the right item to the left item.
		if (leftItemStack.getItem() == rightItemStack.getItem() && (leftItemStack.getItem() instanceof KeyItem)) {
			if (leftItemStack.hasCapability(DURABILITY, null)
					&& rightItemStack.hasCapability(DURABILITY, null)) {

				event.setCost(1);
				DurabilityCapability leftItemCap = (DurabilityCapability) leftItemStack.getCapability(DURABILITY, null);
				DurabilityCapability rightItemCap = (DurabilityCapability) rightItemStack.getCapability(DURABILITY, null);

				if (leftItemCap != null && rightItemCap != null) {
					int leftRemainingUses = leftItemCap.getDurability() - leftItemStack.getItemDamage();
					int rightRemainingUses = rightItemCap.getDurability() - rightItemStack.getItemDamage();
					ItemStack outputItem = new ItemStack(leftItemStack.getItem());

					DurabilityCapability outputItemCap = (DurabilityCapability) outputItem.getCapability(DURABILITY, null);

					int remainingUses = leftRemainingUses + rightRemainingUses;
					if (remainingUses > Math.max(leftItemCap.getDurability(), rightItemCap.getDurability())) {
						//						if (logger.isDebugEnabled()) {
						//							logger.debug("output has greater uses -> {} than emd -> {} - update emd", remainingUses, Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
						//						}
						outputItemCap.setDurability(Math.max(leftItemCap.getDurability(), rightItemCap.getDurability()) + leftItemStack.getMaxDamage());
						outputItem.setItemDamage(leftItemStack.getItemDamage() + rightItemStack.getItemDamage());
					}
					else {
						if (remainingUses < Math.min(leftItemCap.getDurability(), rightItemCap.getDurability())) {
							outputItemCap.setDurability(Math.min(leftItemCap.getDurability(), rightItemCap.getDurability()));
						}
						else {
							outputItemCap.setDurability(Math.max(leftItemCap.getDurability(), rightItemCap.getDurability()));
						}
						outputItem.setItemDamage(outputItemCap.getDurability() - remainingUses);
					}
					event.setOutput(outputItem);
				}
			}
		}

		// add bindable (charm) to socketable (ex. adornment)
		else if (leftItemStack.hasCapability(CHARMABLE, null) && rightItemStack.hasCapability(CHARMABLE, null)
				&& leftItemStack.getCapability(CHARMABLE, null).isSocketable()
				&& rightItemStack.getCapability(CHARMABLE, null).isBindable()) {
			event.setCost(2);
			Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, InventoryType.SOCKET);
			if (outputItemStack.isPresent()) {
				event.setOutput(outputItemStack.get());
			}
		}
		/*
		 * order of conditions matters! gems can be charmed, so treat them as charmed first
		 */
		else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof ICharmed) {
			Optional<ItemStack> outputItemStack = addCharmsToCharmable(leftItemStack, rightItemStack);
			if (outputItemStack.isPresent()) {
				event.setCost(1);
				event.setMaterialCost(1);
				event.setOutput(outputItemStack.get());
			}
		}
		//		else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof GemItem) {
		//			Optional<ItemStack> outputItemStack = addSlotsToCharmable(leftItemStack, rightItemStack);
		//			if (outputItemStack.isPresent()) {
		//				event.setCost(1);
		//				event.setMaterialCost(1);
		//				event.setOutput(outputItemStack.get());
		//			}
		//		}
		// add gem to adornment
		else if ((leftItemStack.getItem() instanceof Adornment) 
				&&	leftItemStack.hasCapability(CHARMABLE, null)
				&&	TreasureCharmableMaterials.isSourceItemRegistered(rightItemStack.getItem().getRegistryName())) {
Treasure.logger.debug("left is adornment and right is gem!");
			ICharmableCapability cap = leftItemStack.getCapability(CHARMABLE, null);
			if (cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
				event.setCost(1);
				// build the output item, duplicating the left stack (adornment) with the right stack as the source item
				Optional<ItemStack> outputStack = createCharmableOutputStack(leftItemStack, rightItemStack.getItem().getRegistryName(), cap.getCharmEntities()); // TODO potentially need an empty map here if not found
				if (outputStack.isPresent()) {
					Treasure.logger.debug("outputStack -> {}", outputStack.get());
					ICharmableCapability outputCap = outputStack.get().getCapability(CHARMABLE, null);
					outputCap.setHighestLevel(cap.getHighestLevel());
					// set the hover name
					if (cap.isNamedByMaterial() || cap.isNamedByCharm()) {
						TreasureCharmables.setHoverName(outputStack.get());
					}
					event.setOutput(outputStack.get());
				}
			}
		}
	}

	/**
	 * 
	 * @param event
	 * @param source
	 * @param dest
	 */
	public static Optional<ItemStack> transferCharmToOutput(ItemStack source, ItemStack dest, InventoryType destInventoryType) {
		Optional<ItemStack> ref = Optional.empty();

		// determine if dest is a high enough level for source's bindable level
		ICharmableCapability sourceCap = source.getCapability(CHARMABLE, null);
		ICharmableCapability destCap = dest.getCapability(CHARMABLE, null);

		//				Treasure.LOGGER.debug("book has innate charms -> {}", sourceCap.getCharmEntities()[InventoryType.INNATE.getValue()].size() > 0);
		// looking for innate
		if (sourceCap.getCharmEntities().get(InventoryType.INNATE).size() > 0) {
			// sort the source charm entity list in descending order
			List<ICharmEntity> innateCharms = (List<ICharmEntity>) sourceCap.getCharmEntities().get(InventoryType.INNATE);
			Comparator<ICharmEntity> comparator = Collections.reverseOrder(new CharmableCapability.SortByLevel());						
			Collections.sort(innateCharms, comparator);
			//					Treasure.LOGGER.debug("size of innate charms -> {}", innateCharms.size());
			// shallow duplicate the dest cap charm entities
			Multimap<InventoryType, ICharmEntity> outputEntities = ArrayListMultimap.create(destCap.getCharmEntities());
			// process each charm entity
			for (ICharmEntity entity : innateCharms) {
				if (destCap.getMaxCharmLevel() >= entity.getCharm().getLevel()) {
					addCharmEntity(entity, destCap,  destInventoryType, (List<ICharmEntity>)outputEntities.get(destInventoryType));
				}						
			}
			//					Treasure.LOGGER.debug("result has more charms than input -> {}", outputEntities[destInventoryType.getValue()].size() > destCap.getCharmEntities()[ destInventoryType.getValue()].size());
			// test if resulting output charms is larger than the original (dest) ie charms were added.
			if (outputEntities.get(destInventoryType).size() > destCap.getCharmEntities().get(destInventoryType).size()) {
				Optional<ItemStack> outputStack = createCharmableOutputStack(dest, destCap.getSourceItem(), outputEntities);
				//						Treasure.LOGGER.debug("output stack -> {}", outputStack.getDisplayName().getString());
				if (outputStack.isPresent()) {
					// set the hover name
					if (destCap.isNamedByMaterial() || destCap.isNamedByCharm()) {
						TreasureCharmables.setHoverName(outputStack.get());
					}
					ref = outputStack;
				}
			}
		}
		return ref;
	}

	/**
	 * 
	 * @param item
	 * @param sourceItem
	 * @param charmEntities
	 * @return
	 */
	private static Optional<ItemStack> createCharmableOutputStack(ItemStack itemStack, ResourceLocation sourceItem, Multimap<InventoryType, ICharmEntity> charmEntities) {
		ICharmableCapability sourceCap = itemStack.getCapability(CHARMABLE, null);
//		Treasure.logger.debug("source cap -> {}", sourceCap);
		ResourceLocation material = sourceCap.getBaseMaterial();
//		Treasure.logger.debug("material -> {}", material);
		Adornment sourceAdornment = (Adornment) itemStack.getItem();
//		Treasure.logger.debug("source adornment -> {}", sourceAdornment.getRegistryName());
		Optional<Adornment> adornment = TreasureAdornments.get(sourceAdornment.getType(), sourceAdornment.getSize(), material, sourceItem);
		if (adornment.isPresent()) {
//			Treasure.logger.debug("adornment from registry -> {}",adornment.get().getRegistryName());
			// copy the dest cap to the output cap
			ItemStack outputStack = new ItemStack(adornment.get());
			// set the source item
			ICharmableCapability outputCap = outputStack.getCapability(CHARMABLE, null);
			// add all the charms over
			for (InventoryType type : InventoryType.values()) {
				for (ICharmEntity e : charmEntities.get(type)) {
					outputCap.add(type, e);
				}
			}
			return Optional.of(outputStack);
		}
		return Optional.empty();
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
		// test against the magics inventory limits
		if (destCap.getCurrentSize(type) < destCap.getMaxSize(type)) {
			outputCharms.add(entity);
		}
		return entity;
	}

	/*
	 * 
	 */
	public boolean doKeyMerge() {

		return true;
	}

	/**
	 * 
	 * @param charmableStack
	 * @param gemItem
	 */
	public Optional<ItemStack> addSlotsToCharmable(ItemStack leftStack, ItemStack gemStack) {
		Treasure.logger.debug("add slots to charmable called...");
		if (leftStack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
			ItemStack output = new ItemStack(leftStack.getItem());
			ICharmInventoryCapability outputCharmCap = output.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			ICharmInventoryCapability leftCharmCap = leftStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);

			int currentCharmsSize = leftCharmCap.getCharmEntities().size();

			// initialize output to that of the left
			outputCharmCap.getCharmEntities().addAll(leftCharmCap.getCharmEntities());
			outputCharmCap.setSlots(leftCharmCap.getSlots());

			// check is slots available
			ICharmable outputItem = (ICharmable)output.getItem();
			if (outputItem.getMaxSlots() > 0 && outputCharmCap.getSlots() < outputItem.getMaxSlots() 
					&& outputCharmCap.getSlots() < (outputItem.getMaxSlots() - currentCharmsSize)) {
				// add a slot to the charmable stack
				outputCharmCap.setSlots(outputCharmCap.getSlots() + 1);
				return Optional.of(output);
			}
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param charmableStack
	 * @param charmedStack
	 */
	public Optional<ItemStack> addCharmsToCharmable(ItemStack leftStack, ItemStack rightStack) {
		if (leftStack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {

			ICharmInventoryCapability leftCharmCap = leftStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			ICharmInventoryCapability rightCharmCap = rightStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			ItemStack output = new ItemStack(leftStack.getItem());
			ICharmInventoryCapability outputCharmCap = output.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);

			// copy left's charms to output (initialize)
			outputCharmCap.getCharmEntities().addAll(leftCharmCap.getCharmEntities());
			outputCharmCap.setSlots(leftCharmCap.getSlots());

			ICharmable item = (ICharmable)output.getItem();
			// check is slots available
			if (item.getMaxSlots() > 0 && outputCharmCap.getSlots() > 0 && rightCharmCap.getCharmEntities().size() > 0) {
				// copy charms from right to output
				int freeSlots = outputCharmCap.getSlots();
				for (int x = 0; x < Math.min(freeSlots, rightCharmCap.getCharmEntities().size()); x++) {
					ICharmEntity sourceCharmInstance = rightCharmCap.getCharmEntities().get(x);
					// check for duplicate charm types
					boolean hasCharm = false;
					for (ICharmEntity instance : outputCharmCap.getCharmEntities()) {
						if (instance.getCharm().getType().equalsIgnoreCase(sourceCharmInstance.getCharm().getType()) ||
								instance.getCharm().getName().equals(sourceCharmInstance.getCharm().getName())) {
							//							Treasure.logger.debug("item already has charm -> {}", sourceCharmInstance.getCharm().getName());
							hasCharm = true;
							break;
						}
					};
					if (!hasCharm) {
						outputCharmCap.getCharmEntities().add(sourceCharmInstance);
						outputCharmCap.setSlots(outputCharmCap.getSlots()-1);
						//	                    Treasure.logger.debug("add charm {} from right to output", rightCharmCap.getCharmEntities().get(x).getCharm().getName());
					}
				}
				return Optional.of(output);
			}
		}
		return Optional.empty();
	}

	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}
}
