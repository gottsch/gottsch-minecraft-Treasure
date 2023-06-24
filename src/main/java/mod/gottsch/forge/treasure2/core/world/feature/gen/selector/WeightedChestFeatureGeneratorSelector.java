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
package mod.gottsch.forge.treasure2.core.world.feature.gen.selector;

import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;

public class WeightedChestFeatureGeneratorSelector implements IFeatureGeneratorSelector {
	private static final WeightedCollection<Number, IFeatureGenerator> COL = new WeightedCollection<>();
	
	public void clear() {
		COL.clear();
	}
	
	@Override
	public IFeatureGenerator select() {
		return COL.next();
	}

	/**
	 * 
	 * @param weight
	 * @param generator
	 */
	public void add(Number weight, IFeatureGenerator generator) {
		COL.add(weight, generator);
	}
}
