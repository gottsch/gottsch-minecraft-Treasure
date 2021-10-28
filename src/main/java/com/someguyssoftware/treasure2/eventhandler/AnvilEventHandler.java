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

import java.util.Optional;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapability;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.GemItem;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

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
			if (leftItemStack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)
					&& rightItemStack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {

				event.setCost(1);
				EffectiveMaxDamageCapability leftItemCap = (EffectiveMaxDamageCapability) leftItemStack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
				EffectiveMaxDamageCapability rightItemCap = (EffectiveMaxDamageCapability) rightItemStack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);

				if (leftItemCap != null && rightItemCap != null) {
					int leftRemainingUses = leftItemCap.getEffectiveMaxDamage() - leftItemStack.getItemDamage();
					int rightRemainingUses = rightItemCap.getEffectiveMaxDamage() - rightItemStack.getItemDamage();
					ItemStack outputItem = new ItemStack(leftItemStack.getItem());

					EffectiveMaxDamageCapability outputItemCap = (EffectiveMaxDamageCapability) outputItem.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);

					int remainingUses = leftRemainingUses + rightRemainingUses;
					if (remainingUses > Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage())) {
//						if (logger.isDebugEnabled()) {
//							logger.debug("output has greater uses -> {} than emd -> {} - update emd", remainingUses, Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
//						}
						outputItemCap.setEffectiveMaxDamage(Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()) + leftItemStack.getMaxDamage());
						outputItem.setItemDamage(leftItemStack.getItemDamage() + rightItemStack.getItemDamage());
					}
					else {
						if (remainingUses < Math.min(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage())) {
							outputItemCap.setEffectiveMaxDamage(Math.min(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
						}
						else {
							outputItemCap.setEffectiveMaxDamage(Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
						}
						outputItem.setItemDamage(outputItemCap.getEffectiveMaxDamage() - remainingUses);
					}
					event.setOutput(outputItem);
				}
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
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof GemItem) {
            Optional<ItemStack> outputItemStack = addSlotsToCharmable(leftItemStack, rightItemStack);
            if (outputItemStack.isPresent()) {
            	event.setCost(1);
            	event.setMaterialCost(1);
                event.setOutput(outputItemStack.get());
            }
        }
    }

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
							Treasure.logger.debug("item already has charm -> {}", sourceCharmInstance.getCharm().getName());
							hasCharm = true;
							break;
						}
                	};
                	if (!hasCharm) {
	                    outputCharmCap.getCharmEntities().add(sourceCharmInstance);
	                    outputCharmCap.setSlots(outputCharmCap.getSlots()-1);
	                    Treasure.logger.debug("add charm {} from right to output", rightCharmCap.getCharmEntities().get(x).getCharm().getName());
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
