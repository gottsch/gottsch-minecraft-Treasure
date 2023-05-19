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
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.ruin.IRuinGenerator;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;

/**
 * 
 * @author Mark Gottschling on May 18, 2023
 *
 */
public class RuinGeneratorRegistry {
	private static final Multimap<IStructureCategory, IRuinGenerator<GeneratorResult<ChestGeneratorData>>> REGISTRY = ArrayListMultimap.create();
	
	private RuinGeneratorRegistry() {}
	
	public static void register(IStructureCategory category, IRuinGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		if (category != null) {
			REGISTRY.put(category, generator);
		}
	}
	
	public static List<IRuinGenerator<GeneratorResult<ChestGeneratorData>>> get(IStructureCategory category) {
		if (REGISTRY.containsKey(category)) {
			return new ArrayList<>(REGISTRY.get(category));
		}
		return new ArrayList<>();
	}
	
	public static List<IRuinGenerator<GeneratorResult<ChestGeneratorData>>> getValues() {
		return new ArrayList<>(REGISTRY.values());
	}
}
