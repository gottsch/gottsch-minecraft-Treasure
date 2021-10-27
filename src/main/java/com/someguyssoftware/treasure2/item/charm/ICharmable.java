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
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;

import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * NOTE: if I go this route, then this means that an Item has the CHARM CAPABILITY but the CharmInstances may be empty.
 * ie. an item has the ABILITY to be charmed but isn't necessarily charmed.
 * All checks against hasCapability() then would also have to check against the capabilities CharmInstances state.
 * ie. !getCapability().getCharmInstances().isEmpty()
 * NOTE: this also throws a wrench into items like Coins, where they are stackable, but if ICharmed, or ICharmable, they can not be stacked.
 * So in this case then you still would need multiple Item classes to represent each type of charm state.
 * Things like Rings, Amulets, Bracelets, Broches and other Adornments makes sense to be non-stackable, and thus would use ICharmable.
 */

/**
 * 
 * @author Mark Gottschling on Dec 19, 2020
 *
 */
public interface ICharmable {
	
	/**
	 * 
	 * @return
	 */
	int getMaxSlots();
	
	/**
	 * 
	 * @param stack
	 * @return
	 */
	default public boolean isCharmed(ItemStack stack) {
		if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability cap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
			if (cap.getCharmInstances().size() > 0) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * 
     * @param stack
     * @param world
     * @param tooltip
     * @param flag
     */
    default public void addCharmedInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed.adornment"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmInventoryCapability cap = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		if (cap != null) {
			List<ICharmEntity> charmEntities = cap.getCharmEntities();
			for (ICharmEntity entity : charmEntities) {
                entity.getCharm().addInformation(stack, world, tooltip, flag, entity);
            }
        }
    }
    
    /**
     * 
     * @param stack
     * @param world
     * @param tooltip
     * @param flag
     */
    @SuppressWarnings("deprecation")
	default public void addSlotsInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
     	ICharmableCapability charmableCap = stack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
     	ICharmCapability charmCap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
     	
     	tooltip.add(TextFormatting.YELLOW.toString() + I18n.translateToLocalFormatted("tooltip.label.charmable.slots", TextFormatting.GRAY.toString()));
		tooltip.add(" " + TextFormatting.WHITE.toString() + I18n.translateToLocalFormatted("tooltip.label.charmable.slots.stats", 
				String.valueOf(charmableCap.getSlots()),
				String.valueOf(charmCap.getCharmInstances().size() + charmableCap.getSlots()),
				String.valueOf(((ICharmable)stack.getItem()).getMaxSlots())));
    }
}