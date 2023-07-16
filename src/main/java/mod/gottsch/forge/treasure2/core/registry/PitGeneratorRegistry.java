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
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.treasure2.core.enums.IPitType;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;

/**
 * 
 * @author Mark Gottschling on Jan 4, 2023
 *
 */
public class PitGeneratorRegistry {
	private static final Multimap<IPitType, IPitGenerator<GeneratorResult<ChestGeneratorData>>> REGISTRY = ArrayListMultimap.create();
	
	private PitGeneratorRegistry() { }
	
	public static void register(IPitType type, IPitGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		if (type != null) {
			REGISTRY.put(type, generator);
		}
	}
	
	public static List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> get(IPitType type) {
		if (REGISTRY.containsKey(type)) {
			return new ArrayList<>(REGISTRY.get(type));
		}
		return new ArrayList<>();
	}
	
	public static List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> getValues() {
		return new ArrayList<>(REGISTRY.values());
	}
}
