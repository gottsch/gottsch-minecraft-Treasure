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
import com.someguyssoftware.treasure2.capability.AdornmentCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.DurabilityCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.modifier.GreatAdornmentLevelModifier;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

import baubles.common.items.ItemRing;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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
//	@SubscribeEvent
//	public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
//		final ItemStack stack = event.getObject();
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
//	}


}
