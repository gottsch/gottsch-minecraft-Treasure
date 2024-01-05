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
package mod.gottsch.forge.treasure2.core.rune;

import java.util.List;

import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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
	CompoundNBT save(CompoundNBT nbt);

	ResourceLocation getName();

	String getType();

	Rarity getRarity();
	
	String getLore();
	void setLore(String lore);
	
	void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, IRuneEntity entity);
}
