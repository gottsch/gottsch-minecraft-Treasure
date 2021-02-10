/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
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

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;


public class WellFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

	// private int chunksSinceLastWell;
    private Map<String, Integer> chunksSinceLastDimensionWell = new HashMap<>();

	/**
	 * 
	 */
	public WellFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
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
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random,
			BlockPos pos, NoFeatureConfig config) {

    	String dimensionName = world.getDimension().getType().getRegistryName().toString();
    	
        if (!checkDimensionWhiteList(dimensionName)) {
            return false;
        }

		// spawn @ middle of chunk
		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS, 0, WorldInfo.CHUNK_RADIUS);
		Biome biome = world.getBiome(spawnCoords.toPos());
        if (checkOceanBiomes(biome)) {
            return false;
        }

        // increment the chunk counts
		incrementDimensionalChunkCount(dimensionName);
        
//		Treasure.LOGGER.debug("chunks since dimension {} last well -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionWell.get(dimensionName), TreasureConfig.WELLS.chunksPerWell.get());
		
        GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

        		// test if min chunks was met
		if (chunksSinceLastDimensionWell.get(dimensionName) > TreasureConfig.WELLS.chunksPerWell.get()) {
//			Treasure.LOGGER.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLastWell, TreasureConfig.minChunksPerWell));

			int ySpawn = world.getChunk(pos).getTopBlockY(Heightmap.Type.WORLD_SURFACE, WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
			spawnCoords = spawnCoords.withY(ySpawn);
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());

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
			TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList());
			if(biomeCheck == Result.BLACK_LISTED ) {
				if (WorldInfo.isClientSide(world.getWorld())) {
					Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getDisplayName().getString(), spawnCoords.toShortString());
				}
				else {
					Treasure.LOGGER.debug("Biome is not valid @ {} for Well", spawnCoords.toShortString());
				}					
				return false;
			}
			
			incrementDimensionalChunkCount(dimensionName);
			
			// generate the well
			Treasure.LOGGER.debug("Attempting to generate a well");
			result = TreasureData.WELL_GEN.generate(world, random, spawnCoords, wellConfig); 
			Treasure.LOGGER.debug("well world gen result -> {}", result.isSuccess());
			if (result.isSuccess()) {
				chunksSinceLastDimensionWell.put(dimensionName, 0);
			}
			
			// save world data
			TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(world.getWorld());
			if (savedData != null) {
				savedData.markDirty();
			}
			return true;
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