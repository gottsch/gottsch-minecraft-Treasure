/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;

/**
 * Allows the replacement of generator mappings.
 * @author Mark Gottschling on Dec 2, 2022
 *
 */
public class ChestGeneratorRegistry {
//	private static final Map<IChestGeneratorType, IChestGenerator> REGISTRY = Maps.newHashMap();
	private static final Map<IRarity, IChestGenerator> MAP = Maps.newHashMap();
	
	private ChestGeneratorRegistry() {}
	
//	@Deprecated
//	public static void registerGeneator(IChestGeneratorType type, IChestGenerator generator) {
//		if (/*!REGISTRY.containsKey(type) && */type != null) {
//			REGISTRY.put(type, generator);
//		}
//	}
	
//	public static void registerGeneator(IChestGenerator generator) {
//		if (generator.getChestGeneratorType() != null /*&& !REGISTRY.containsKey(generator.getChestGeneratorType())*/) {
//			IChestGenerator originalGenerator = REGISTRY.put(generator.getChestGeneratorType(), generator);
//			if (originalGenerator != null) {
//				Treasure.LOGGER.debug("Replaced generator -> {} with -> {} for type -> {}", originalGenerator.getClass().getSimpleName(), generator.getClass().getSimpleName(), generator.getChestGeneratorType().getName());
//			}
//		}
//	}
	
	public static void registerGeneator(IRarity rarity, IChestGenerator generator) {
//		if (generator.getChestGeneratorType() != null) {
			IChestGenerator originalGenerator = MAP.put(rarity, generator);
			if (originalGenerator != null) {
				Treasure.LOGGER.debug("Replaced generator -> {} with -> {} for rarity -> {}", originalGenerator.getClass().getSimpleName(), generator.getClass().getSimpleName(), rarity);
			}
//		}
	}
	
//	public static Optional<IChestGenerator> get(IChestGeneratorType type) {
//		if (REGISTRY.containsKey(type)) {
//			return Optional.of(REGISTRY.get(type));
//		}
//		return Optional.empty();
//	}
	
	public static Optional<IChestGenerator> get(IRarity rarity) {
		if (MAP.containsKey(rarity)) {
			return Optional.of(MAP.get(rarity));
		}
		return Optional.empty();
	}
	
//	public static List<IChestGenerator> getValues() {
//		return new ArrayList<>(REGISTRY.values());
//	}
	
	public static List<IChestGenerator> getValues() {
		return new ArrayList<>(MAP.values());
	}
}
