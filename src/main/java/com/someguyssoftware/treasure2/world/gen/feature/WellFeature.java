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
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.config.IWellsConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.registry.SimpleListRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;


public class WellFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

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

//    	String dimensionName = world.getDimension().getType().getRegistryName().toString();
		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
   	
		// test the dimension white list
		if (!meetsDimensionCriteria(dimensionName)) { 
			return false;
		}

    	BlockPos centerOfChunk = pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1);
//		Treasure.LOGGER.debug("center of chunk @ -> {}", centerOfChunk.toShortString());
	
        GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

			int landHeight = generator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			ICoords spawnCoords = new Coords(centerOfChunk.getX(), landHeight, centerOfChunk.getZ());
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());

		// TODO why not getDryLandSurfaceCoords -> check if same as above 
		// ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, generator, new Coords(pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
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
			
			// // 1. test if chest meets the probability criteria
			// if (!RandomHelper.checkProbability(random, wellConfig.getGenProbability())) {
			// 	Treasure.LOGGER.debug("Well does not meet generate probability.");
			// 	return false;
			// }

			// 2. test if the override (global) biome is allowed
			// Biome biome = seedReader.getLevel().getBiome(spawnCoords.toPos());
			// TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList());
			// if(biomeCheck == Result.BLACK_LISTED ) {
			// 	if (WorldInfo.isClientSide(seedReader.getLevel())) {
			// 		Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getRegistryName().toString(), spawnCoords.toShortString());
			// 	}
			// 	else {
			// 		Treasure.LOGGER.debug("Biome is not valid @ {} for Well", spawnCoords.toShortString());
			// 	}					
			// 	return false;
			// }

		// 2. test if the override (global) biome is allowed
		if (!meetsBiomeCriteria(world, spawnCoords, wellConfig)) {
			return false;
		}


			// 3. check against all registered wells
			if (checkWellProximity(seedReader, spawnCoords, TreasureConfig.WELLS.minDistancePerWell.get())) {
				Treasure.LOGGER.debug("The distance to the nearest well is less than the minimun required.");
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
			result = TreasureData.WELL_GEN.generate(seedReader, generator, random, spawnCoords, wellConfig); 
			Treasure.LOGGER.debug("well world gen result -> {}", result.isSuccess());
			if (result.isSuccess()) {
				// add to registry
				Treasure.LOGGER.debug("getting well registry for dimension -> {}", dimensionName.toString());
				TreasureData.WELL_REGISTRIES.get(dimensionName.toString()).register(spawnCoords);
//				TreasureData.WELL_REGISTRIES.get(dimensionName.toString()).dump();
			}
			
			// save world data
			TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(seedReader.getLevel());
			if (savedData != null) {
				savedData.setDirty();
			}
			return true;
        }
		
		return false;
    }
    
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param minDistance
	 * @return
	 */
	public static boolean checkWellProximity(IServerWorld world, ICoords coords, int minDistance) {

		double minDistanceSq = minDistance * minDistance;
		Treasure.LOGGER.debug("checking well registry for proximity in dimension -> {}", WorldInfo.getDimension(world.getLevel()).toString());
		SimpleListRegistry<ICoords> wellRegistry = TreasureData.WELL_REGISTRIES.get(WorldInfo.getDimension(world.getLevel()).toString());
		List<ICoords> wellsList = wellRegistry.getValues();
		if (wellsList.isEmpty()) {
			Treasure.LOGGER.debug("Unable to locate the Well Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ICoords wellCoords : wellsList) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(wellCoords);
			if (distance < minDistanceSq) {
				Treasure.LOGGER.debug("distance -> {} less than required -> {}", distance, minDistanceSq);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param dimensionName
	 */
	private void incrementDimensionalChunkCount(String dimensionName) {
		chunksSinceLastDimensionWell.merge(dimensionName, 1, Integer::sum);		
	}

	@Override
	public Map<String, Integer> getChunksSinceLastDimensionFeature() {
		return chunksSinceLastDimensionWell;
	}

	@Override
	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature() {
		return null;
	}
}