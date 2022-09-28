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
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.Map;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.registry.RegistryType;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

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
	default public boolean meetsProximityCriteria(ServerWorld world, ResourceLocation dimension, RegistryType key, ICoords spawnCoords) {
		if (isRegisteredChestWithinDistance(world, dimension, key, spawnCoords, TreasureConfig.CHESTS.surfaceChestGen.minBlockDistance.get())) {
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
	public static boolean isRegisteredChestWithinDistance(IWorld world, ResourceLocation dimension, RegistryType key, ICoords coords, int minDistance) {

		Map<RegistryType, ChestRegistry> registries = TreasureData.CHEST_REGISTRIES2.get(dimension.toString());
		if (registries == null || registries.size() == 0) {
			Treasure.LOGGER.debug("Unable to locate the ChestRegistry for dimension -> {}", dimension.toString());
			return false;
		}
		ChestRegistry registry = registries.get(key);
		if (registry == null) {
			Treasure.LOGGER.debug("Unable to locate the ChestRegistry or the Registry doesn't contain any values");
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