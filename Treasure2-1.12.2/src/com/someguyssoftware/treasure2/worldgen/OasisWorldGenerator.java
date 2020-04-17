/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.oasis.DesertOasisGenerator;
import com.someguyssoftware.treasure2.generator.oasis.IOasisGenerator;
import com.someguyssoftware.treasure2.generator.oasis.OasisInfo;
import com.someguyssoftware.treasure2.registry.OasisRegistry;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * @author Mark
 *
 */
public class OasisWorldGenerator implements ITreasureWorldGenerator {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

	private Map<Integer, Integer> chunksSinceLastDimensionOasis;
	// Map<DimenionId, Map<BiomeID, count>>
	private Map<Integer, Map<Integer, Integer>> chunksSinceLastDimensionBiomeOasis;
	
	private Multimap<Integer, IOasisGenerator<?>> oasisGenerators;

	/**
	 * 
	 */
	public OasisWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate OasisGenerator:", e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void init() {
		// initialize chunks since last map
		chunksSinceLastDimensionOasis = new HashMap<>();
		chunksSinceLastDimensionBiomeOasis = new HashMap<>();		
		for (Integer dimensionId : TreasureConfig.WORLD_GEN.getGeneralProperties().getDimensionsWhiteList()) {
			chunksSinceLastDimensionOasis.put(dimensionId, 0);
			chunksSinceLastDimensionBiomeOasis.put(dimensionId, new HashMap<>());
			// NOTE do not need to set biome count to 0. this can be checked/updated at time of biome check in generate()
		}	
		
		// initialize oasis generators
		oasisGenerators = ArrayListMultimap.create();
		if (TreasureConfig.OASES.desertOasisProperties.isEnableOasis()) {
			registerOasisByBiomes(new DesertOasisGenerator());
		}
	}
	
	/**
	 * 
	 * @param oasisGenerator
	 */
	private void registerOasisByBiomes(IOasisGenerator<?> oasisGenerator) {
		List<Biome> biomeWhiteList = oasisGenerator.getConfig().getBiomeWhiteList();
		List<Biome> biomeBlackList = oasisGenerator.getConfig().getBiomeBlackList();
		
		if (biomeWhiteList.isEmpty() && biomeBlackList.isEmpty()) {
			Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValuesCollection();
			for (Biome biome : biomes) {
					Integer biomeID = Biome.getIdForBiome(biome);
					if (!oasisGenerators.containsKey(biomeID)) {
						oasisGenerators.put(biomeID, oasisGenerator);
					}				
			}
		}
		else {
			if (!biomeWhiteList.isEmpty()) {
				for (Biome biome : biomeWhiteList) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (!oasisGenerators.containsKey(biomeID)) {
							oasisGenerators.put(biomeID, oasisGenerator);
						}
				}
			} else if (!biomeBlackList.isEmpty()) {
				/*
				 * add all the black listed biome IDs to a list
				 */
				List<Integer> blackListBiomeIDs = new ArrayList<>();
				for (Biome biome : biomeBlackList) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (biomeID != null) blackListBiomeIDs.add(biomeID);
				}
				// get the set of all biomes
				Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValuesCollection();
				// for each biome is the list
				for (Biome biome : biomes) {
					if (!blackListBiomeIDs.contains(Biome.getIdForBiome(biome))) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (!oasisGenerators.containsKey(biomeID)) {
							oasisGenerators.put(biomeID, oasisGenerator);
						}
					}
				}
			}
		}		
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		
		if (TreasureConfig.WORLD_GEN.getGeneralProperties().getDimensionsWhiteList().contains(Integer.valueOf(world.provider.getDimension()))) {
			generate(world, random, chunkX, chunkZ);
		}
		
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 */
	public void generate(World world, Random random, int chunkX, int chunkZ) {
 		/*
 		 * get current chunk position
 		 */            
        // spawn @ middle of chunk
        int xSpawn = (chunkX * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        int zSpawn = (chunkZ * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        		
        Integer dimensionID = Integer.valueOf(world.provider.getDimension());
        
		// increment the general chunk count
		Integer chunksSinceLast = chunksSinceLastDimensionOasis.get(dimensionID);
		chunksSinceLastDimensionOasis.put(dimensionID, ++chunksSinceLast);	
		
        ICoords coords = new Coords(xSpawn, 255, zSpawn);
		Biome biome = world.getBiome(coords.toPos());
		Integer biomeID = Biome.getIdForBiome(biome);
		
		// TEMP
    	if (Treasure.logger.isDebugEnabled()) {
    		if (WorldInfo.isClientSide(world)) {
    			Treasure.logger.debug("current biome {} @ {}", biome.getBiomeName(), coords.toShortString());
    		}
    		else {
    			Treasure.logger.debug("current biomeID {} @ {}", biomeID, coords.toShortString());
    		}
    	}
    	
		// check if the minimum chunks have been met
		if (chunksSinceLast >= TreasureConfig.OASES.minChunksPerOasis) {
//			Treasure.logger.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLast, TreasureConfig.OASES.minChunksPerOasis));
			// get a generator for the biome
			Optional<List<IOasisGenerator<?>>> generators = Optional.ofNullable((List<IOasisGenerator<?>>) oasisGenerators.get(biomeID));
			if (!generators.isPresent() || generators.get().isEmpty()) {
				return;
			}
			IOasisGenerator<?> generator = generators.get().get(random.nextInt(generators.get().size()));
			
			// check if the min chunks per biome have been met
			Integer chunksPerDimensionBiome = chunksSinceLastDimensionBiomeOasis.get(dimensionID).get(biomeID);
			if (chunksPerDimensionBiome == null) chunksPerDimensionBiome = 0;
			chunksSinceLastDimensionBiomeOasis.get(dimensionID).put(biomeID, ++chunksPerDimensionBiome);
			
//			Treasure.logger.debug("config chunks per oasis by biome -> {}, chunksPerDimensionBiome -> {}", generator.getConfig().getChunksPerOasis(), chunksPerDimensionBiome);
			if (chunksPerDimensionBiome >= generator.getConfig().getChunksPerOasis()) {
				// 1. test if oasis meets the probability criteria
				if (!RandomHelper.checkProbability(random, generator.getConfig().getGenProbability())) {
					Treasure.logger.debug("Oasis does not meet generate probability of -> {}", generator.getConfig().getGenProbability());
					return;
				}
				
     			// 2. check against all registered oasis by dimension
     			if (isRegisteredOasisWithinDistance(world, coords, dimensionID, TreasureConfig.OASES.minDistancePerOasis)) {
   					Treasure.logger.debug("The distance to the nearest oasis is less than the minimun required.");
     				return;
     			}
     			
    			// reset chunks since last dimensional/biome oasis regardless of successful generation - makes more rare and realistic and configurable generation.
     			chunksSinceLastDimensionBiomeOasis.get(dimensionID).put(biomeID, 0);
 			
    			// generate the chest/pit/chambers
				Treasure.logger.debug("Attempting to generate oasis @ {}", coords.toShortString());
				Optional<GeneratorResult<GeneratorData>> result = Optional.ofNullable(generator.generate(world, random, coords));
				
    			if (result.isPresent() && result.get().isSuccess()) {
    				// add to registry
    				OasisRegistry.getInstance().register(dimensionID, coords.toShortString(), new OasisInfo(result.get().getData().getSpawnCoords(), dimensionID, biomeID));
    				// reset the chunk counts
    				chunksSinceLastDimensionOasis.put(dimensionID, 0);	
    			}
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param dimensionID
	 * @param minDistance
	 * @return
	 */
	private boolean isRegisteredOasisWithinDistance(World world, ICoords coords, Integer dimensionID,
			int minDistance) {
		
		double minDistanceSq = minDistance * minDistance;
		
		// get a list of oasis
		List<OasisInfo> oasisInfos = OasisRegistry.getInstance().getValues(dimensionID);

		if (oasisInfos == null || oasisInfos.size() == 0) {
			Treasure.logger.debug("Unable to locate the OasisConfig Registry or the Registry doesn't contain any values");
			return false;
		}
		
		for (OasisInfo info : oasisInfos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}

	public Multimap<Integer, IOasisGenerator<?>> getOasisGenerators() {
		return oasisGenerators;
	}

	public void setOasisGenerators(Multimap<Integer, IOasisGenerator<?>> oasisGenerators) {
		this.oasisGenerators = oasisGenerators;
	}

	public Map<Integer, Integer> getChunksSinceLastDimensionOasis() {
		return chunksSinceLastDimensionOasis;
	}

	public void setChunksSinceLastDimensionOasis(Map<Integer, Integer> chunksSinceLastDimensionOasis) {
		this.chunksSinceLastDimensionOasis = chunksSinceLastDimensionOasis;
	}

	public Map<Integer, Map<Integer, Integer>> getChunksSinceLastDimensionBiomeOasis() {
		return chunksSinceLastDimensionBiomeOasis;
	}

	public void setChunksSinceLastDimensionBiomeOasis(
			Map<Integer, Map<Integer, Integer>> chunksSinceLastDimensionBiomeOasis) {
		this.chunksSinceLastDimensionBiomeOasis = chunksSinceLastDimensionBiomeOasis;
	}

}
