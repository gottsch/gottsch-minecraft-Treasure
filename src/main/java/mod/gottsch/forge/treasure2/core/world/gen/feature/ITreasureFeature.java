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
package mod.gottsch.forge.treasure2.core.world.gen.feature;

import java.util.List;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.biome.TreasureBiomeHelper;
import mod.gottsch.forge.treasure2.core.biome.TreasureBiomeHelper.Result;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling
 *
 */
public interface ITreasureFeature {

	/**
	 * 
	 * @param dimension
	 * @return
	 */
	default public boolean meetsDimensionCriteria(ResourceLocation dimension) {
		// test the dimension white list
		return TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimension.toString());
	}

	/**
	 * 
	 * @param world
	 * @param spawnCoords
	 * @param chestConfig
	 * @return
	 */
	default public boolean meetsBiomeCriteria(ServerWorld world, ICoords spawnCoords, List<String> whitelist, List<String> blacklist) {
		Biome biome = world.getBiome(spawnCoords.toPos());
		TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome,whitelist, blacklist);
		if(biomeCheck == Result.BLACK_LISTED ) {
			if (WorldInfo.isClientSide(world)) {
				Treasure.LOGGER.debug("Biome {} is not a valid biome @ {}", biome.getRegistryName().toString(), spawnCoords.toShortString());
			}
			else {
				// TODO test if this crashes with the getRegistryName because in 1.12 this was a client side only
				Treasure.LOGGER.debug("Biome {} is not valid @ {}",biome.getRegistryName().toString(), spawnCoords.toShortString());
			}					
			return false;
		}
		return true;
	}
}
