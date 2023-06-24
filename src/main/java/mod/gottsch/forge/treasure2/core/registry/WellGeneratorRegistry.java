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
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.ruin.IRuinGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.IWellGenerator;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;

/**
 * 
 * @author Mark Gottschling on May 19, 2023
 *
 */
public class WellGeneratorRegistry {
	private static final Multimap<IStructureCategory, IWellGenerator<GeneratorResult<GeneratorData>>> REGISTRY = ArrayListMultimap.create();
	
	private WellGeneratorRegistry() {}
	
	public static void register(IStructureCategory category, IWellGenerator<GeneratorResult<GeneratorData>> generator) {
		if (category != null) {
			REGISTRY.put(category, generator);
		}
	}
	
	public static List<IWellGenerator<GeneratorResult<GeneratorData>>> get(IStructureCategory category) {
		if (REGISTRY.containsKey(category)) {
			return new ArrayList<>(REGISTRY.get(category));
		}
		return new ArrayList<>();
	}
	
	public static List<IWellGenerator<GeneratorResult<GeneratorData>>> getValues() {
		return new ArrayList<>(REGISTRY.values());
	}
}
