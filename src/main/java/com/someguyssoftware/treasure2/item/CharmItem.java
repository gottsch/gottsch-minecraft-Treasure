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
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class CharmItem extends ModItem {
	// TEMP this probably will need to move to CharmableCap or another cap if want to make this work with TAGs.
//	private ResourceLocation sourceItem;
//	private int charmLevel;

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public CharmItem(String modID, String name, Properties properties) {
		// TODO remove later from creative
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP)
				.stacksTo(1));
//		setCharmLevel(7);
        // TODO set default item
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 * @param sourceItem
	 */
//	public CharmItem(String modID, String name, Properties properties, ResourceLocation sourceItem) {
//        this(modID, name, properties);
//        setSourceItem(sourceItem);
//	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		// TODO create new CharmItemCapProvider which includes POUCHABLE cap (not everything that is charmable is pouchable)
		CharmableCapabilityProvider provider =  new CharmableCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// charmable info
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			cap.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}
	
	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public ICharmableCapability getCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.CHARMABLE_CAPABILITY, null).orElseThrow(IllegalStateException::new);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			return true;
		}
		else {
			return false;
		}
	}

//	public ResourceLocation getSourceItem() {
//		return sourceItem;
//	}
//
//	public void setSourceItem(ResourceLocation sourceItem) {
//		this.sourceItem = sourceItem;
//	}

//	public int getCharmLevel() {
//		return charmLevel;
//	}
//
//	public void setCharmLevel(int charmLevel) {
//		this.charmLevel = charmLevel;
//	}
}
