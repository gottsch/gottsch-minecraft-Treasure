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

import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.enums.AdornmentType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * 
 * @author Mark Gottschling on Jan 20, 2022
 *
 */
public class NamedAdornment extends Adornment {

	public NamedAdornment(AdornmentType type, Item.Properties properties) {
		super(type, properties);
	}
	
	public NamedAdornment(AdornmentType type, AdornmentSize size, Item.Properties properties) {
		super(type, size, properties);
	}
	
	@Deprecated
	public NamedAdornment(String modID, String name, AdornmentType type, Item.Properties properties) {
		super(modID, name, type, properties);
	}
	@Deprecated
	public NamedAdornment(String modID, String name, AdornmentType type, AdornmentSize size, Item.Properties properties) {
		super(modID, name, type, size, properties);
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		return ((IFormattableTextComponent)super.getName(stack)).withStyle(TextFormatting.YELLOW);
	}
}
