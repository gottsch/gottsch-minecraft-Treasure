/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.structure.grove;

import net.minecraft.util.RandomSource;

/**
 * The kinds of procedural tree a Wither Grove is made of.
 * The name is saved with each WitherTreePiece, so never rename a constant.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public enum WitherTreeArchetype {
	/** a single log column (the old WitherTreeGenerator); the grove's supporting trees */
	WITHER,
	/** a 3x3 trunk with a stripped core (the old GreatWitherTreeGenerator) */
	GREAT;

	/**
	 * Picks the archetype for a grove tree.
	 * @param distance the tree's distance from the main tree, as a fraction of the grove's radius (0 to 1)
	 */
	public static WitherTreeArchetype choose(double distance, RandomSource random) {
		// the grove's supporting trees were always single columns; the main tree is the great one
		return WITHER;
	}

	public static WitherTreeArchetype byName(String name) {
		for (WitherTreeArchetype archetype : values()) {
			if (archetype.name().equals(name)) {
				return archetype;
			}
		}
		return WITHER;
	}
}
