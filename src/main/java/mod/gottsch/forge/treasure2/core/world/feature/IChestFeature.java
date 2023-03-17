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
package mod.gottsch.forge.treasure2.core.world.feature;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * 
 * @author Mark Gottschling on Sep 21, 2022
 *
 */
public interface IChestFeature extends ITreasureFeature {

	/**
	 * 
	 * @param world
	 * @param dimension
	 * @param spawnCoords
	 * @return
	 */
	default public boolean meetsProximityCriteria(ServerLevelAccessor world, ResourceLocation dimension, GeneratorType key, ICoords spawnCoords, int minDistance) {
		if (isRegisteredChestWithinDistance(world, dimension, key, spawnCoords, minDistance)) {
			Treasure.LOGGER.trace("The distance to the nearest treasure chest is less than the minimun required.");
			return false;
		}	
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param dimension
	 * @param key
	 * @param coords
	 * @param minDistance
	 * @return
	 */
	public static boolean isRegisteredChestWithinDistance(ServerLevelAccessor world, ResourceLocation dimension, GeneratorType key, ICoords coords, int minDistance) {
		GeneratedRegistry<? extends GeneratedContext> registry = DimensionalGeneratedRegistry.getChestGeneratedRegistry(dimension, key);
		if (registry == null || registry.getValues().isEmpty()) {
			Treasure.LOGGER.debug("unable to locate the GeneratedRegistry or the registry doesn't contain any values");
			return false;
		}

		// generate a box with coords as center and minDistance as radius
		ICoords startBox = new Coords(coords.getX() - minDistance, 0, coords.getZ() - minDistance);
		ICoords endBox = new Coords(coords.getX() + minDistance, 0, coords.getZ() + minDistance);

		// find if box overlaps anything in the registry
		if (registry.withinArea(startBox, endBox)) {
			return true;
		}
		return false;
	}
}