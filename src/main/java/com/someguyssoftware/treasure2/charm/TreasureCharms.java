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
package com.someguyssoftware.treasure2.charm;

import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * 
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class TreasureCharms {
	public static final ICharm HEALING_1;
	public static final ICharm HEALING_2;
	public static final ICharm HEALING_3;
	public static final ICharm HEALING_4;
	
	public static final ICharm HEALING_15;
	
	static {
		HEALING_1 = new HealingCharm.Builder(1).with($ -> {
			$.value = 20.0;
			$.allowMultipleUpdates = true;
		})	.build();

		HEALING_2 = new HealingCharm.Builder(2).with($ -> {
			$.value = 40.0;
			$.allowMultipleUpdates = true;
		})	.build();
		
		HEALING_3 = new HealingCharm.Builder(3).with($ -> {
			$.value = 60.0;
			$.allowMultipleUpdates = true;
		})	.build();
		
		HEALING_4 = new HealingCharm.Builder(4).with($ -> {
			$.value = 80.0;
			$.allowMultipleUpdates = true;
			$.rarity = Rarity.UNCOMMON;
		})	.build();
		
		HEALING_15 = new HealingCharm.Builder(5).with($ -> {
			$.value = 300.0;
			$.allowMultipleUpdates = true;
			$.rarity = Rarity.EPIC;
		})	.build();
		
		TreasureCharmRegistry.register(HEALING_1);
		TreasureCharmRegistry.register(HEALING_2);
		TreasureCharmRegistry.register(HEALING_3);
		TreasureCharmRegistry.register(HEALING_4);
		
		TreasureCharmRegistry.register(HEALING_15);
	}
}
