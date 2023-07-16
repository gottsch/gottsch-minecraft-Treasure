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

import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;

/**
 * 
 * @author Mark Gottschling on Jun 1, 2023
 *
 */
public class AquaticChestFeatureGeneratorSelector implements IFeatureGeneratorSelector {

	@Override
	public IFeatureGenerator select() {
		return TreasureFeatureGenerators.SUBAQUATIC_FEATURE_GENERATOR;
	}
}
