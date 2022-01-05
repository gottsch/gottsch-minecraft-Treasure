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
import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.capability.AdornmentCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmInventoryCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmInventoryCapabilityStorage;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityStorage;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapabilityStorage;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.item.charm.ICharmable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public class Adornment extends ModItem {
    private static final CharmableCapabilityStorage CAPABILITY_STORAGE = new CharmableCapabilityStorage();
	private static final MagicsInventoryCapabilityStorage MAGICS_STORAGE = new MagicsInventoryCapabilityStorage();
	
	private Type type;
	private AdornmentSize size;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 */
	public Adornment(String modID, String name, Type type) {
		this(modID, name, type, TreasureAdornments.STANDARD);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 * @param size
	 */
	public Adornment(String modID, String name, Type type, AdornmentSize size) {
		super();
		setItemName(modID, name);
		setType(type);
		setSize(size);
		setMaxStackSize(1);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		AdornmentCapabilityProvider provider =  new AdornmentCapabilityProvider();
		return provider;

		// TODO will have to create a new CapabilityProvider that includes both CharmInventory and TreasureBaubleProvider
//		CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
//		return BaublesIntegration.isEnabled() ? new BaublesIntegration.AdornmentProvider(type) : new CharmInventoryCapabilityProvider();
//		return provider;

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
	 * TODO why did I override this?
	 */
	@Override
    public String getItemStackDisplayName(ItemStack stack) {
    	String name = super.getItemStackDisplayName(stack);
		return name;
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
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.charmable.usage.adornment"));
		ICharmableCapability cap = getCap(stack);
		cap.appendHoverText(stack, world, tooltip, flag);
//		if (isCharmed(stack)) {
//			addCharmedInfo(stack, world, tooltip, flag);
//		}
//		else {
//			tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmable"));
//		}
//		addSlotsInfo(stack, world, tooltip, flag);
	}
	
	/**
	 * NOTE getNBTShareTag() and readNBTShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands
	 * or having the client update when the Anvil GUI is open.
	 */
	@Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {

		NBTTagCompound magicsTag;
		// read cap -> write nbt
		magicsTag = (NBTTagCompound) MAGICS_STORAGE.writeNBT(
				TreasureCapabilities.MAGICS,
				stack.getCapability(TreasureCapabilities.MAGICS, null),
				null);
		NBTTagCompound charmableTag;
		charmableTag = (NBTTagCompound) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.CHARMABLE,
				stack.getCapability(TreasureCapabilities.CHARMABLE, null),
				null);
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("magics", magicsTag);
		tag.setTag("charmable", charmableTag);
		
		return tag;
	}
	
    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        
        // read nbt -> write key item
        if (nbt.hasKey("magics")) {
        	NBTTagCompound tag = nbt.getCompoundTag("magics");
	        MAGICS_STORAGE.readNBT(
	     		   TreasureCapabilities.MAGICS, 
	 				stack.getCapability(TreasureCapabilities.MAGICS, null), 
	 				null,
	 				tag);
        }
        if (nbt.hasKey("charmable")) {
        	NBTTagCompound tag = nbt.getCompoundTag("charmable");
	       CAPABILITY_STORAGE.readNBT(
	    		   TreasureCapabilities.CHARMABLE, 
					stack.getCapability(TreasureCapabilities.CHARMABLE, null), 
					null,
					tag);
        }
    }
    
	public Type getType() {
		return type;
	};
	
	private void setType(Type type) {
		this.type = type;
	}
	
	public AdornmentSize getSize() {
		return size;
	}

	private void setSize(AdornmentSize size) {
		this.size = size;
	}
	
	public enum Type {
		BRACELET,
		EARRING,
		NECKLACE,
		RING;
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
