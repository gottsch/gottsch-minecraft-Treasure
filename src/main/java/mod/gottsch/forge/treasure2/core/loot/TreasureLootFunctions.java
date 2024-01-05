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
package mod.gottsch.forge.treasure2.core.loot;

import mod.gottsch.forge.treasure2.core.loot.function.CharmRandomly;
import mod.gottsch.forge.treasure2.core.loot.function.RandomAdornment;
import mod.gottsch.forge.treasure2.core.loot.function.RandomCharm;
import mod.gottsch.forge.treasure2.core.loot.function.RandomRunestone;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.registry.Registry;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class TreasureLootFunctions {

	public static LootFunctionType CHARM_RANDOMLY;
	public static LootFunctionType RANDOM_ADORNMENT;
	public static LootFunctionType RANDOM_CHARM;
	public static LootFunctionType RANDOM_RUNESTONE;
	/**
	 * 
	 */
	public static void register() {
		CHARM_RANDOMLY = register("charm_randomly", new LootFunctionType(new CharmRandomly.Serializer())); 
		RANDOM_ADORNMENT = register("random_adornment", new LootFunctionType(new RandomAdornment.Serializer()));
		RANDOM_CHARM = register("random_charm", new LootFunctionType(new RandomCharm.Serializer()));
		RANDOM_RUNESTONE = register("random_runestone", new LootFunctionType(new RandomRunestone.Serializer()));

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
