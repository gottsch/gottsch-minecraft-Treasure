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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;


public class WellFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

    private Map<String, Integer> chunksSinceLastDimensionWell = new HashMap<>();

	/**
	 * 
	 */
	public WellFeature(Codec< NoFeatureConfig> configFactory) {
        super(configFactory);
        this.setRegistryName(Treasure.MODID, "wells");

        try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate WellFeature:", e);
		}
	}

	public void init() {
        // setup dimensional properties
		for (String dimension : TreasureConfig.GENERAL.dimensionsWhiteList.get()) {
			chunksSinceLastDimensionWell.put(dimension, 0);
		}
	}
	
	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
//		Treasure.LOGGER.debug("in surface feature for pos @ -> {}", pos.toString());

//    	String dimensionName = world.getDimension().getType().getRegistryName().toString();
		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
   	
        if (!checkDimensionWhiteList(dimensionName.toString())) {
            return false;
        }

    	BlockPos centerOfChunk = pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1);
//		Treasure.LOGGER.debug("center of chunk @ -> {}", centerOfChunk.toShortString());
	
		// spawn @ middle of chunk
//		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1);
//		Biome biome = seedReader.getLevel().getBiome(spawnCoords.toPos());
//        if (checkOceanBiomes(biome)) {
//            return false;
//        }

        // increment the chunk counts
		incrementDimensionalChunkCount(dimensionName.toString());
        
//		Treasure.LOGGER.debug("chunks since dimension {} last well -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionWell.get(dimensionName), TreasureConfig.WELLS.chunksPerWell.get());
		
        GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

        // test if min chunks was met
        int chunksSinceLastCount = chunksSinceLastDimensionWell.get(dimensionName.toString());
		if (chunksSinceLastCount > TreasureConfig.WELLS.chunksPerWell.get()) {
			Treasure.LOGGER.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLastCount, TreasureConfig.WELLS.chunksPerWell.get()));

			int landHeight = generator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			ICoords spawnCoords = new Coords(centerOfChunk.getX(), landHeight, centerOfChunk.getZ());
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());

			// reduce count by 20% (if fails - results in a quicker retry)
			chunksSinceLastDimensionWell.put(dimensionName.toString(), new Double(chunksSinceLastCount - chunksSinceLastCount * 0.2).intValue());
						
			// determine what type to generate
			Wells well = Wells.values()[random.nextInt(Wells.values().length)];
			IWellsConfig wellConfig = TreasureConfig.WELLS;
			if (wellConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a config for well {}.", well);
				return false;
			}
			
			// 1. test if chest meets the probability criteria
			if (!RandomHelper.checkProbability(random, wellConfig.getGenProbability())) {
				Treasure.LOGGER.debug("Well does not meet generate probability.");
				return false;
			}

			// 2. test if the override (global) biome is allowed
			Biome biome = seedReader.getLevel().getBiome(spawnCoords.toPos());
			TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList());
			if(biomeCheck == Result.BLACK_LISTED ) {
				if (WorldInfo.isClientSide(seedReader.getLevel())) {
					Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getRegistryName().toString(), spawnCoords.toShortString());
				}
				else {
					Treasure.LOGGER.debug("Biome is not valid @ {} for Well", spawnCoords.toShortString());
				}					
				return false;
			}
			
			// 3. check against all registered wells
			if (checkWellProximity(seedReader, spawnCoords, TreasureConfig.WELLS.minDistancePerWell.get())) {
				Treasure.LOGGER.debug("The distance to the nearest well is less than the minimun required.");
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
				// reset chunk count
				chunksSinceLastDimensionWell.put(dimensionName.toString(), 0);
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
	public static boolean checkWellProximity(IServerLevel world, ICoords coords, int minDistance) {

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