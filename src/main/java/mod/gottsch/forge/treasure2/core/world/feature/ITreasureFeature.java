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

import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.biome.TreasureBiomeHelper;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

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
		return Config.SERVER.integration.dimensionsWhiteList.get().contains(dimension.toString());
	}

	/**
	 *
	 *
	 * @param world
	 * @param spawnCoords
	 * @param whitelist
	 * @param blacklist
	 * @return
	 */
	default public boolean meetsBiomeCriteria(ServerLevel world, ICoords spawnCoords, List<String> whitelist, List<String> blacklist) {
		ResourceLocation name = ModUtil.getName(world.getBiome(spawnCoords.toPos()));

//		Treasure.LOGGER.debug("whitelist -> {}", whitelist);
//		Treasure.LOGGER.debug("blacklist -> {}", blacklist);
//		Treasure.LOGGER.debug("biome -> {}", name);

		TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(name, whitelist, blacklist);
//		Treasure.LOGGER.debug("biomeCheck -> {}", biomeCheck);

		if(biomeCheck == TreasureBiomeHelper.Result.BLACK_LISTED ) {
				Treasure.LOGGER.debug("biome {} is not valid at -> {}", name, spawnCoords.toShortString());
			return false;
		}
		return true;
	}
}
