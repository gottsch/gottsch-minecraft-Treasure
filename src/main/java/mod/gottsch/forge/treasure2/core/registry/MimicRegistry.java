/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling
 *
 */
public class MimicRegistry {
	/**
	 * A map from Chest name to Mimic name.
	 */
	private static final Map<ResourceLocation, ResourceLocation> MAP = new HashMap<>();

	/**
	 * 
	 */
	private MimicRegistry() {	}
	
	/**
	 * 
	 * @param chest
	 * @param mimic
	 */
	public static void register(ResourceLocation chest, ResourceLocation mimic) {
		MAP.put(chest, mimic);
	}
	
	/**
	 * 
	 * @param chest
	 * @return
	 */
	public static Optional<ResourceLocation> getMimic(ResourceLocation chest) {
		if (MAP.containsKey(chest)) {
			return Optional.of(MAP.get(chest));
		}
		return Optional.empty();
	}
	
	public static List<ResourceLocation> getNames() {
		return new ArrayList<>(MAP.keySet());
	}
	
	public static List<ResourceLocation> getMimics() {
		return new ArrayList<>(MAP.values());
	}
}


