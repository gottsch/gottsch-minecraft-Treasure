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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.IRunestonesCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.RunestonesCapabilityProvider;
import com.someguyssoftware.treasure2.capability.RunestonesCapabilityStorage;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.runestone.IRunestone;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2022
 *
 */
public class RunestoneItem extends ModItem {
	private static final RunestonesCapabilityStorage CAPABILITY_STORAGE = new RunestonesCapabilityStorage();

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public RunestoneItem(String modID, String name) {
		super(modID, name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Treasure.TREASURE_TAB);
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		Treasure.logger.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		RunestonesCapabilityProvider provider =  new RunestonesCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// charmable info
		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.runestones.usage"));
		IRunestonesCapability cap = getCap(stack);
		if (cap.hasRunestone()) {
			// add a space
			tooltip.add( TextFormatting.DARK_PURPLE + I18n.translateToLocal("tooltip.runestones.effects"));	
			IRunestone runestone = cap.getEntities(InventoryType.INNATE).get(0).getRunestone();
			// lore may be multiple lines, so separate on \n and add to tooltip
			for (String s : StringEscapeUtils.unescapeJava(runestone.getLore()).split("\\R")) {
				tooltip.add(I18n.translateToLocalFormatted("tooltip.indent2", TextFormatting.LIGHT_PURPLE + "" + TextFormatting.ITALIC + s));
			}
		}
	}
	
	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public IRunestonesCapability getCap(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.RUNESTONES, null)) {
		return stack.getCapability(TreasureCapabilities.RUNESTONES, null);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Has Foil
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
//		IRunestonesCapability cap = getCap(stack);
//		if (cap.hasRunestone()) {
//			return true;
//		}
		return false;
	}
	
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
//		Treasure.logger.debug("writing share tag");
		NBTTagCompound tag;
		tag = (NBTTagCompound) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.RUNESTONES,
				stack.getCapability(TreasureCapabilities.RUNESTONES, null),
				null);
		
		return tag;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
//        Treasure.logger.debug("reading share tag");
        if (nbt instanceof NBTTagCompound) {
	       CAPABILITY_STORAGE.readNBT(
	    		   TreasureCapabilities.RUNESTONES, 
					stack.getCapability(TreasureCapabilities.RUNESTONES, null), null, nbt);
        }
    }
}