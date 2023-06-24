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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.treasure2.core.enums.IMarkerType;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.marker.IMarkerGenerator;

/**
 * 
 * @author Mark Gottschling May 22, 2023
 *
 */
public class MarkerGeneratorRegistry {
	private static final Multimap<IMarkerType, IMarkerGenerator<GeneratorResult<GeneratorData>>> REGISTRY = ArrayListMultimap.create();
	
	private MarkerGeneratorRegistry() { }
	
	public static void register(IMarkerType type, IMarkerGenerator<GeneratorResult<GeneratorData>> generator) {
		if (type != null) {
			REGISTRY.put(type, generator);
		}
	}
	
	public static List<IMarkerGenerator<GeneratorResult<GeneratorData>>> get(IMarkerType type) {
		if (REGISTRY.containsKey(type)) {
			return new ArrayList<>(REGISTRY.get(type));
		}
		return new ArrayList<>();
	}
	
	public static List<IMarkerGenerator<GeneratorResult<GeneratorData>>> getValues() {
		return new ArrayList<>(REGISTRY.values());
	}
}
