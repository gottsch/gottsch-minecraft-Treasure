/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.registry;

import java.util.*;

import net.minecraft.resources.ResourceLocation;

/**
 * NOTE 9/24/2025 -currently, this is an mod api association only.
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
	public static synchronized void register(ResourceLocation chest, ResourceLocation mimic) {
		MAP.put(chest, mimic);
	}
	
	/**
	 * 
	 * @param chest
	 * @return
	 */
	public static synchronized Optional<ResourceLocation> getMimic(ResourceLocation chest) {
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


