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
package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.someguyssoftware.treasure2.capability.CharmableCapability.CharmInventoryType;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

/**
 * CharmableCapability is a capability that has an ICharmEntity inventory
 * 
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public interface ICharmableCapability {
	public List<ICharmEntity>[] getCharmEntities();
	public void setCharmEntities(List<ICharmEntity>[] entities);
	
	/**
	 * Convenience method.
	 * @param type
	 * @param entity
	 */	
	void add(CharmInventoryType type, ICharmEntity entity);
	
	public boolean isCharmed();
	
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag);
	
	// an originator of a charm ie coins, gems
//	public boolean isCharmSource();
//	public void setCharmSource(boolean isSource);
}