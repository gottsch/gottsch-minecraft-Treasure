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

import java.util.Optional;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;

/**
 * 
 * @author Mark Gottschling on May 12, 2023
 *
 */
public class FeatureGeneratorSelectorRegistry {
	public static final Table<IFeatureType, IRarity, IFeatureGeneratorSelector> REGISTRY = HashBasedTable.create();
	
	/**
	 * 
	 * @param type
	 * @param rarity
	 * @param selector
	 */
	public static void registerSelector(IFeatureType type, IRarity rarity, IFeatureGeneratorSelector selector) {
		// first check if featureType is registered
		if (!EnumRegistry.isRegistered(TreasureApi.FEATURE_TYPE, type)) {
			return;
		}

		REGISTRY.put(type, rarity, selector);
	}

	/**
	 * 
	 * @param type
	 * @param rarity
	 * @return
	 */
	public static Optional<IFeatureGeneratorSelector> getSelector(IFeatureType type, IRarity rarity) {
		return Optional.ofNullable(REGISTRY.get(type, rarity));
	}
}
