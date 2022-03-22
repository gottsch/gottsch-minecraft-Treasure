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

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityStorage;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class CharmItem extends ModItem {
	private static final CharmableCapabilityStorage CAPABILITY_STORAGE = new CharmableCapabilityStorage();

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public CharmItem(String modID, String name) {
		super(modID, name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Treasure.TREASURE_TAB);
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		// TODO create new CharmItemCapProvider which includes POUCHABLE cap (not everything that is charmable is pouchable)
		Treasure.logger.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		CharmableCapabilityProvider provider =  new CharmableCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		// charmable info
		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.charmable.usage"));
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
		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
		return stack.getCapability(TreasureCapabilities.CHARMABLE, null);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		ICharmableCapability cap = getCap(stack);
		if (cap.isCharmed()) {
			return true;
		}
		return false;
	}
	
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound charmableTag;
		charmableTag = (NBTTagCompound) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARMABLE,
				stack.getCapability(TreasureCapabilities.CHARMABLE, null),
				null);
		
		return charmableTag;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);

        if (nbt instanceof NBTTagCompound) {
	       CAPABILITY_STORAGE.readNBT(
	    		   TreasureCapabilities.CHARMABLE, 
					stack.getCapability(TreasureCapabilities.CHARMABLE, null), null, nbt);
        }
    }
}