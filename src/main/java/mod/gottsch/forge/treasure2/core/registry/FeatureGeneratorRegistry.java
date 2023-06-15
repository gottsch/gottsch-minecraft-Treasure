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

import java.util.HashMap;
import java.util.Map;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;

/**
 * 
 * @author Mark Gottschling May 12, 2023
 *
 */
public class FeatureGeneratorRegistry {

	private static final Map<IFeatureType, IFeatureGenerator> MAP = new HashMap<>();

	/**
	 * 
	 */
	private FeatureGeneratorRegistry() {	}

	
	public static void registerGenerator(IFeatureType type, IFeatureGenerator featureGenerator) {
		// first check if featureType is registered
		if (!EnumRegistry.isRegistered("featureType", type)) {
			Treasure.LOGGER.warn("featureType {} is not registered. unable to complete feature generator registration.", type);
			return;
		}
		MAP.put(type, featureGenerator);
	}	

	public IFeatureGenerator get(IFeatureType type) {
		// TODO make more robust
		return MAP.get(type);
	}
}
