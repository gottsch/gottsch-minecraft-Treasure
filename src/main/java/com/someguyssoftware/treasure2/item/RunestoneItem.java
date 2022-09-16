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

import org.apache.commons.lang3.StringEscapeUtils;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.IRunestonesCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.RunestonesCapabilityProvider;
import com.someguyssoftware.treasure2.capability.RunestonesCapabilityStorage;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.rune.IRune;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	 * @param properties
	 */
	public RunestoneItem(Properties properties) {
		super(properties.stacksTo(1).tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	@Deprecated
	public RunestoneItem(String modID, String name, Properties properties) {
		super(modID, name, properties.stacksTo(1).tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		Treasure.LOGGER.debug("{} item initiating caps", stack.getItem().getRegistryName().toString());
		RunestonesCapabilityProvider provider =  new RunestonesCapabilityProvider();
		return provider;
	}
	
	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// charmable info
		tooltip.add(new TranslationTextComponent("tooltip.runestones.usage").withStyle(TextFormatting.GOLD).withStyle(TextFormatting.ITALIC));
		IRunestonesCapability cap = getCap(stack);
		if (cap.hasRunestone()) {
			// add a space
			tooltip.add(new TranslationTextComponent("tooltip.runestones.effects").withStyle(TextFormatting.DARK_PURPLE));	
			IRune runestone = cap.getEntities(InventoryType.INNATE).get(0).getRunestone();
			// lore may be multiple lines, so separate on \n and add to tooltip
			TranslationTextComponent lore = new TranslationTextComponent(runestone.getLore());
			for (String s : lore.getString().split("~")) {	
				tooltip.add(new TranslationTextComponent("tooltip.indent2", s).withStyle(TextFormatting.LIGHT_PURPLE).withStyle(TextFormatting.ITALIC));
			}
		}
	}
	
	/**
	 * Convenience method.
	 * @param stack
	 * @return
	 */
	public IRunestonesCapability getCap(ItemStack stack) {
		return stack.getCapability(TreasureCapabilities.RUNESTONES).map(cap -> cap).orElseThrow(() -> new IllegalStateException());
	}
	
	/**
	 * Has Foil
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		IRunestonesCapability cap = getCap(stack);
		if (cap.hasRunestone()) {
			return true;
		}
		return false;
	}
	
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
    public CompoundNBT getShareTag(ItemStack stack) {
//		Treasure.logger.debug("writing share tag");
		CompoundNBT tag;
		tag = (CompoundNBT) CAPABILITY_STORAGE.writeNBT(TreasureCapabilities.RUNESTONES, getCap(stack), null);
		
		return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
//        Treasure.logger.debug("reading share tag");
        if (nbt instanceof CompoundNBT) {
	       CAPABILITY_STORAGE.readNBT(TreasureCapabilities.RUNESTONES, getCap(stack), null, nbt);
        }
    }
}