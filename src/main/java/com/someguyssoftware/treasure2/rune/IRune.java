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

import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public interface IRune {
	
	IRuneEntity createEntity();
	
	boolean isValid(ItemStack itemStack);
	void apply(ItemStack itemStack, IRuneEntity entity);
	void undo(ItemStack itemStack, IRuneEntity entity);
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	NBTTagCompound save(NBTTagCompound nbt);

	ResourceLocation getName();

	String getType();

	Rarity getRarity();
	
	String getLore();
	void setLore(String lore);
	
	void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, IRuneEntity entity);
}
