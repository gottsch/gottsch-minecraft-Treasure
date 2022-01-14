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
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
			Treasure.logger.debug("left is charmable and socketable && right is charmable and bindable");
			event.setCost(2);
			Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, InventoryType.SOCKET);
			if (outputItemStack.isPresent()) {
				event.setOutput(outputItemStack.get());
			}
		}
		// add imbuing (charm) to imbuable (ex. adornment)
		else if (leftItemStack.hasCapability(CHARMABLE, null) && leftItemStack.getCapability(CHARMABLE, null).isImbuable()
			&& rightItemStack.hasCapability(CHARMABLE, null) && rightItemStack.getCapability(CHARMABLE, null).isImbuing()) {
			Treasure.logger.debug("in the imbuing process");
			event.setCost(2);
			Optional<ItemStack> outputItemStack = transferCharmToOutput(rightItemStack, leftItemStack, InventoryType.IMBUE);
			if (outputItemStack.isPresent()) {
				event.setOutput(outputItemStack.get());
			}
		}
		// add gem to adornment
		else if ((leftItemStack.getItem() instanceof Adornment) 
				&&	leftItemStack.hasCapability(CHARMABLE, null)
				&&	TreasureCharmableMaterials.isSourceItemRegistered(rightItemStack.getItem().getRegistryName())) {
			//Treasure.logger.debug("left is adornment and right is gem!");
			ICharmableCapability cap = leftItemStack.getCapability(CHARMABLE, null);
			if (cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
				event.setCost(1);
				event.setMaterialCost(1);

				// build the output item, duplicating the left stack (adornment) with the right stack as the source item
				Optional<Adornment> adornment = getAdornment(leftItemStack, rightItemStack);
				if (adornment.isPresent()) {
					ItemStack outputStack = newCharmableStack(adornment.get(), cap.getCharmEntities());
					//					Treasure.logger.debug("outputStack -> {}", outputStack.get().getItem().getRegistryName());
					ICharmableCapability outputCap = outputStack.getCapability(CHARMABLE, null);
					outputCap.setHighestLevel(cap.getHighestLevel());
					// set the hover name
					if (cap.isNamedByMaterial() || cap.isNamedByCharm()) {
						TreasureCharmables.setHoverName(outputStack);
					}
					event.setOutput(outputStack);
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
		Treasure.logger.debug("transfering charms...");
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
			// process each charm entity (only innate as charmItems can have innate)
			for (ICharmEntity entity : innateCharms) {
				if (destCap.getMaxCharmLevel() >= entity.getCharm().getLevel()) {
					addCharmEntity(entity, destCap,  destInventoryType, (List<ICharmEntity>)outputEntities.get(destInventoryType));
				}						
			}
			//					Treasure.LOGGER.debug("result has more charms than input -> {}", outputEntities[destInventoryType.getValue()].size() > destCap.getCharmEntities()[ destInventoryType.getValue()].size());
			// test if resulting output charms is larger than the original (dest) ie charms were added.
			if (outputEntities.get(destInventoryType).size() > destCap.getCharmEntities().get(destInventoryType).size()) {
				//				Treasure.logger.debug("output stack -> {}", outputStack.get().getDisplayName());
				ItemStack outputStack = newCharmableStack(dest.getItem(), outputEntities);
				// set the hover name
				if (destCap.isNamedByMaterial() || destCap.isNamedByCharm()) {
					TreasureCharmables.setHoverName(outputStack);
				}
				ref = Optional.of(outputStack);
			}
		}
		return ref;
	}

	private static Optional<Adornment> getAdornment(ItemStack baseStack, ItemStack stoneStack) {
		if (baseStack.hasCapability(TreasureCapabilities.CHARMABLE, null) && baseStack.getItem() instanceof Adornment) {
			ICharmableCapability cap = baseStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			Adornment sourceAdornment = (Adornment) baseStack.getItem();
			return TreasureAdornments.get(sourceAdornment.getType(), sourceAdornment.getSize(), cap.getBaseMaterial(), stoneStack.getItem().getRegistryName());
		}
		return Optional.empty();
	}

	//	private static Optional<Adornment> getAdornment(ItemStack itemStack, ResourceLocation material, ResourceLocation source) {
	//		// TODO this is for building a dynamic adornment name and fetching from registry -> ie adding gems to standard adornments.
	//		Treasure.logger.debug("material -> {}", material);
	//		Adornment sourceAdornment = (Adornment) itemStack.getItem();
	//		Treasure.logger.debug("source adornment -> {}", sourceAdornment.getRegistryName());
	//		return TreasureAdornments.get(sourceAdornment.getType(), sourceAdornment.getSize(), material, source);
	//	}

	/**
	 * 
	 * @param item
	 * @param sourceItem
	 * @param charmEntities
	 * @return
	 */
	private static ItemStack newCharmableStack(Item adornment, Multimap<InventoryType, ICharmEntity> charmEntities) {

		// copy the dest cap to the output cap
		ItemStack outputStack = new ItemStack(adornment);//.get());
		// set the source item
		ICharmableCapability outputCap = outputStack.getCapability(CHARMABLE, null);
		// clear the original charms if any (specials like Angel's Ring)
		// TODO this needs to be wrapped because the magics size need to be reset too. probably shouldn't expost charmentities directly
		outputCap.clearCharms();
		// add all the charms over
		for (InventoryType type : InventoryType.values()) {
			for (ICharmEntity e : charmEntities.get(type)) {
				outputCap.add(type, e);
			}
		}
		return outputStack;
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
		Treasure.logger.debug("sizes for type -> {}, current -> {}, max -> {}", type, destCap.getCurrentSize(type), destCap.getMaxSize(type));
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
//	public Optional<ItemStack> addSlotsToCharmable(ItemStack leftStack, ItemStack gemStack) {
//		Treasure.logger.debug("add slots to charmable called...");
//		if (leftStack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
//			ItemStack output = new ItemStack(leftStack.getItem());
//			ICharmInventoryCapability outputCharmCap = output.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
//			ICharmInventoryCapability leftCharmCap = leftStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
//
//			int currentCharmsSize = leftCharmCap.getCharmEntities().size();
//
//			// initialize output to that of the left
//			outputCharmCap.getCharmEntities().addAll(leftCharmCap.getCharmEntities());
//			outputCharmCap.setSlots(leftCharmCap.getSlots());
//
//			// check is slots available
//			ICharmable outputItem = (ICharmable)output.getItem();
//			if (outputItem.getMaxSlots() > 0 && outputCharmCap.getSlots() < outputItem.getMaxSlots() 
//					&& outputCharmCap.getSlots() < (outputItem.getMaxSlots() - currentCharmsSize)) {
//				// add a slot to the charmable stack
//				outputCharmCap.setSlots(outputCharmCap.getSlots() + 1);
//				return Optional.of(output);
//			}
//		}
//		return Optional.empty();
//	}

//	/**
//	 * 
//	 * @param charmableStack
//	 * @param charmedStack
//	 */
//	public Optional<ItemStack> addCharmsToCharmable(ItemStack leftStack, ItemStack rightStack) {
//		if (leftStack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
//
//			ICharmInventoryCapability leftCharmCap = leftStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
//			ICharmInventoryCapability rightCharmCap = rightStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
//			ItemStack output = new ItemStack(leftStack.getItem());
//			ICharmInventoryCapability outputCharmCap = output.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
//
//			// copy left's charms to output (initialize)
//			outputCharmCap.getCharmEntities().addAll(leftCharmCap.getCharmEntities());
//			outputCharmCap.setSlots(leftCharmCap.getSlots());
//
//			ICharmable item = (ICharmable)output.getItem();
//			// check is slots available
//			if (item.getMaxSlots() > 0 && outputCharmCap.getSlots() > 0 && rightCharmCap.getCharmEntities().size() > 0) {
//				// copy charms from right to output
//				int freeSlots = outputCharmCap.getSlots();
//				for (int x = 0; x < Math.min(freeSlots, rightCharmCap.getCharmEntities().size()); x++) {
//					ICharmEntity sourceCharmInstance = rightCharmCap.getCharmEntities().get(x);
//					// check for duplicate charm types
//					boolean hasCharm = false;
//					for (ICharmEntity instance : outputCharmCap.getCharmEntities()) {
//						if (instance.getCharm().getType().equalsIgnoreCase(sourceCharmInstance.getCharm().getType()) ||
//								instance.getCharm().getName().equals(sourceCharmInstance.getCharm().getName())) {
//							//							Treasure.logger.debug("item already has charm -> {}", sourceCharmInstance.getCharm().getName());
//							hasCharm = true;
//							break;
//						}
//					};
//					if (!hasCharm) {
//						outputCharmCap.getCharmEntities().add(sourceCharmInstance);
//						outputCharmCap.setSlots(outputCharmCap.getSlots()-1);
//						//	                    Treasure.logger.debug("add charm {} from right to output", rightCharmCap.getCharmEntities().get(x).getCharm().getName());
//					}
//				}
//				return Optional.of(output);
//			}
//		}
//		return Optional.empty();
//	}

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
