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
package com.someguyssoftware.treasure2.api;

import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;

/**
 * 
 * @author Mark Gottschling on Dec 25, 2021
 *
 */
public class TreasureApi {

	public static void registerLootTables(String modID) {
		TreasureLootTableRegistry.register(modID);
	}
	
	/**
	 * For modders who want to add meta files, register your mod here.
	 * @param modID
	 */
	public static void registerMeta(String modID) {
		TreasureMetaRegistry.register(modID);
	}
	
	public static void registerTemplates(String modID) {
		 TreasureTemplateRegistry.register(modID);
	}
	
	public static void registerDecays(String modID) {
		TreasureDecayRegistry.register(modID);
	}
}