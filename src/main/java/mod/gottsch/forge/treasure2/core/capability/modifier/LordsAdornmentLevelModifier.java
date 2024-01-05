/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.capability.modifier;

/**
 * 
 * @author Mark Gottschling on Jan 5, 2022
 *
 */
public class LordsAdornmentLevelModifier implements ILevelModifier {
	
	@Override
	public int modifyMaxLevel(int level) {
		return level + 5;
	}

	@Override
	public double modifyLevelMultiplier(double multiplier) {
		return multiplier + 0.5D;
	}
}
