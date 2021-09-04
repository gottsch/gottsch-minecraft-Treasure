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
package com.someguyssoftware.treasure2.loot;

import com.someguyssoftware.treasure2.loot.function.CharmRandomly;
import com.someguyssoftware.treasure2.loot.function.RandomAdornment;
import com.someguyssoftware.treasure2.loot.function.SetSourceItem;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.registry.Registry;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class TreasureLootFunctions {

	public static LootFunctionType CHARM_RANDOMLY;
	public static LootFunctionType SET_SOURCE_ITEM;
	public static LootFunctionType RANDOM_ADORNMENT;
	
	/**
	 * 
	 */
	public static void register() {
		CHARM_RANDOMLY = register("charm_randomly", new LootFunctionType(new CharmRandomly.Serializer())); 
		SET_SOURCE_ITEM = register("set_source_item", new LootFunctionType(new SetSourceItem.Serializer()));
		RANDOM_ADORNMENT = register("random_adornment", new LootFunctionType(new RandomAdornment.Serializer()));
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public static LootFunctionType register(String name, LootFunctionType type) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, ModUtils.asLocation(name), type);
	}
}
