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

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.PouchableCapabilityProvider;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.RunestoneItem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public final class AdornmentsEventHandler {
	private static final ResourceLocation ADORNMENT_ID = new ResourceLocation(Treasure.MODID, "adornment");

	// TEMP testing attaching caps to foreign mod item
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {

		final ItemStack stack = event.getObject();
		if (stack != null && (stack.getItem() instanceof RunestoneItem
				|| TreasureConfig.WEALTH.pouchables.contains(stack.getItem().getRegistryName()))) {
//			Treasure.logger.debug("adding pouchable cap to item -> {}", stack.getDisplayName());
			event.addCapability(new ResourceLocation(Treasure.MODID, "pouchable"), new PouchableCapabilityProvider());
		}
		
//		if (stack != null && stack.getItem() == Items.WOODEN_HOE) {
//			// TODO if registered item instead of instanceof
//			
//			Treasure.logger.debug("attching cap to -> {}", stack.getItem().getRegistryName());
//			ICharmableCapability cap = new CharmableCapability.Builder(new MagicsInventoryCapability(0, 0, 2)).with($ -> { // use STONE as source Item so it can't be upgraded
//				$.innate = false;
//				$.imbuable = false;
//				$.socketable = true;
//				$.source = false;
//				$.executing = true;
//				$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
//				$.sourceItem =  Item.getItemFromBlock(Blocks.STONE).getRegistryName();
//				$.levelModifier = new GreatAdornmentLevelModifier();
//			}).build();
//
//			IDurabilityCapability durabilityCap = new DurabilityCapability(100, 100);
//			ICapabilityProvider provider = BaublesIntegration.isEnabled()
//					? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, durabilityCap) 
//							: new AdornmentCapabilityProvider(cap, durabilityCap);				
//			event.addCapability(ADORNMENT_ID, provider);
//		}
	}


}
