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
package mod.gottsch.forge.treasure2.core.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.config.IWellsConfig;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.data.TreasureData;
import mod.gottsch.forge.treasure2.core.enums.Wells;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureGenerationSavedData;
import mod.gottsch.forge.treasure2.core.registry.RegistryType;
import mod.gottsch.forge.treasure2.core.registry.SimpleListRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling
 *
 */
public class WellFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;
	private int waitChunksCount = 0;

	/**
	 * 
	 */
	public WellFeature(Codec< NoFeatureConfig> configFactory) {
		super(configFactory);
		this.setRegistryName(Treasure.MODID, "wells");
	}

	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		//		Treasure.LOGGER.debug("in surface feature for pos @ -> {}", pos.toString());
		ServerWorld world = seedReader.getLevel();
		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
		SimpleListRegistry<ICoords> registry = TreasureData.WELL_REGISTRIES.get(dimensionName.toString());
		
		// test the dimension white list
		if (!meetsDimensionCriteria(dimensionName)) { 
			return false;
		}

		if (!meetsWorldAgeCriteria(world, registry)) {
			return false;
		}
		
		ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(seedReader, generator, new Coords(pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == WorldInfo.EMPTY_COORDS) {
			return false;
		}

		// determine what type to generate
		Wells well = Wells.values()[random.nextInt(Wells.values().length)];
		IWellsConfig wellConfig = TreasureConfig.WELLS;
		if (wellConfig == null) {
			Treasure.LOGGER.warn("Unable to locate a config for well {}.", well);
			return false;
		}

		// 2. test if the override (global) biome is allowed
		if (!meetsBiomeCriteria(world, spawnCoords, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList())) {
			return false;
		}

		// 3A. check against all registered wells
		if (checkWellProximity(seedReader, spawnCoords, TreasureConfig.WELLS.minDistancePerWell.get(), registry)) {
			Treasure.LOGGER.trace("The distance to the nearest well is less than the minimun required.");
			return false;
		}
		
		// 3B. check against all registered chests
		if (!meetsProximityCriteria(world, dimensionName, RegistryType.SURFACE, spawnCoords)) {
			return false;
		}

		// 4. check if meets the probability criteria
		if (!meetsProbabilityCriteria(random)) {
			// TODO place a placeholder well in the registry
			// TODO chestInfo will require a type [WELL | NONE]
			return false;
		}

		// generate the well NOTE use the seedReader, not the level
		Treasure.LOGGER.debug("Attempting to generate a well");
		GeneratorResult<GeneratorData> result = null;
		result = TreasureData.WELL_GEN.generate(seedReader, generator, random, spawnCoords, wellConfig); 
		Treasure.LOGGER.debug("well world gen result -> {}", result.isSuccess());
		if (result.isSuccess()) {
			// add to registry
			Treasure.LOGGER.debug("getting well registry for dimension -> {}", dimensionName.toString());
			// TODO update
			registry.register(spawnCoords);
		}

		// save world data
		TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(seedReader.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param registry
	 * @return
	 */
	private boolean meetsWorldAgeCriteria(ServerWorld world, SimpleListRegistry<ICoords> registry) {
		if (registry.getValues().isEmpty() && waitChunksCount < TreasureConfig.WELLS.waitChunks.get()) {
			Treasure.LOGGER.debug("World is too young");
			waitChunksCount++;
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param random
	 * @return
	 */
	private boolean meetsProbabilityCriteria(Random random) {
		if (!RandomHelper.checkProbability(random, TreasureConfig.WELLS.genProbability.get())) {
			Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param minDistance
	 * @return
	 */
	public boolean checkWellProximity(IServerWorld world, ICoords coords, int minDistance, SimpleListRegistry<ICoords> registry) {

		double minDistanceSq = minDistance * minDistance;
		Treasure.LOGGER.trace("checking well registry for proximity in dimension -> {}", WorldInfo.getDimension(world.getLevel()).toString());
		List<ICoords> wellsList = registry.getValues();

		for (ICoords wellCoords : wellsList) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(wellCoords);
			if (distance < minDistanceSq) {
				Treasure.LOGGER.trace("distance -> {} less than required -> {}", distance, minDistanceSq);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param world
	 * @param dimension
	 * @param key
	 * @param spawnCoords
	 * @return
	 */
	public boolean meetsProximityCriteria(ServerWorld world, ResourceLocation dimension, RegistryType key, ICoords spawnCoords) {
		if (IChestFeature.isRegisteredChestWithinDistance(world, dimension, key, spawnCoords, 48)) {
			Treasure.LOGGER.trace("The distance to the nearest treasure chest is less than the minimun ({}) required.", 48);
			return false;
		}	
		return true;
	}
}