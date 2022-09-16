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
package com.someguyssoftware.treasure2.rune;

import java.util.List;

import com.someguyssoftware.treasure2.capability.TreasureCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * This runestone grants an adornment with Infinity durability.
 * @author Mark Gottschling on Jan 15, 2022
 *
 */
public class AnvilRune extends Rune {

	protected AnvilRune(Builder builder) {
		super(builder);
	}
	
	@Override
	public boolean isValid(ItemStack itemStack) {		
		if (!itemStack.getCapability(TreasureCapabilities.DURABILITY).map(cap -> cap.isInfinite()).orElse(true)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void apply(ItemStack itemStack, IRuneEntity entity) {
		if (!isValid(itemStack) || entity.isApplied()) {
			return;
		}

		itemStack.getCapability(TreasureCapabilities.DURABILITY).ifPresent(cap -> {
			cap.setInfinite(true);
			entity.setApplied(true);
		});
	}

	@Override
	public void undo(ItemStack itemStack, IRuneEntity entity) {
		itemStack.getCapability(TreasureCapabilities.DURABILITY).ifPresent(cap -> {
			cap.setInfinite(false);
			entity.setApplied(false);
		});
	}

	public static class Builder extends Rune.Builder {
		public Builder(ResourceLocation name) {
			super(name);
		}
		@Override
		public IRune build() {
			return new AnvilRune(this);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, IRuneEntity entity) {
        TextFormatting color = TextFormatting.LIGHT_PURPLE;       
		tooltip.add(new TranslationTextComponent("tooltip.indent2", new TranslationTextComponent("runestone." + getName().toString().replace(":", "."))).withStyle(color));		
	}
}
