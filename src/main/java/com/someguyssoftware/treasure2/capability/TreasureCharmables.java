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

import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.charm.TreasureCharms;

import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on Sep 10, 2021
 *
 */
public class TreasureCharmables {

	/*
	 * Wrapper for TreasureCharms and TreasureAdornments
	 */
	public static void setHoverName(ItemStack stack) {
		if (TreasureAdornments.getAll().contains(stack.getItem())) {
			TreasureAdornments.setHoverName(stack);
		}
		else if(TreasureCharms.getAll().contains(stack.getItem())) {
			TreasureCharms.setHoverName(stack);
		}
	}
}
