 /**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestWorldGenerator implements IWorldGenerator {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

	// TODO biomes might be chest/rarity specific - ie need any array/map
	// biome white/black lists
//	private List<BiomeTypeHolder> biomeWhiteList;
//	private List<BiomeTypeHolder> biomeBlackList;

	private int chunksSinceLastChest;
	// TODO make a map instead of array
	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	
	// the dungeon geneator
//	private DungeonGenerator generator;
	private Map<Rarity, ChestGenerator> generators = new HashMap<>();
	
	/**
	 * 
	 */
	public ChestWorldGenerator() {
		// setup the dungeon generator
		try {
//			generator = new DungeonGenerator();
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate ChestGenerator:", e);
		}
	}
	
	private void init() {
		// intialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}
		
		// initialize white/black lists
//		biomeWhiteList = new ArrayList<>(5);
//		biomeBlackList = new ArrayList<>(5);

		// populate white/black lists with values from config
//		BiomeHelper.loadBiomeList(TreasureConfig.generalChestBiomeWhiteList, biomeWhiteList);
//		BiomeHelper.loadBiomeList(TreasureConfig.generalChestBiomeBlackList, biomeBlackList);
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimension()){
		case 0:
		    generateSurface(world, random, chunkX, chunkZ);
		    break;
	    default:
	    	break;
		}
	}		


	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		
		// increment the chunk counts
		chunksSinceLastChest++;
		for (Rarity rarity : Rarity.values()) {
			Integer i = chunksSinceLastRarityChest.get(rarity);
			i++;
		}
        boolean isGenerated = false;	
        
		// test if min chunks was met
     	if (chunksSinceLastChest > TreasureConfig.minChunksPerChest) {
     		Treasure.logger.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLastChest, TreasureConfig.minChunksPerChest));

     		/*
     		 * get current chunk position
     		 */            
            // spawn @ middle of chunk
            int xSpawn = chunkX * 16 + 8;
            int zSpawn = chunkZ * 16 + 8;
            
            // the get first surface y (could be leaves, trunk, water, etc)
            int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(8, 8);
            ICoords coords = new Coords(xSpawn, ySpawn, zSpawn);
            
            // TODO then call a factory to get the correct ChestGenerator.
            // TODO chest generator calls the a pit factory for the type of pit to generate.
            // TODO filling the chest can come from an abstract method. for the most part it will always be the same
	    	// determine what type to generate
        	Rarity rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
			IChestConfig chestConfig = Treasure.chestConfigs.get(rarity);
    		if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
    			
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.logger.debug("Chest does not meet generate probability.");
					return;
				}	
				
				// 2. test if correct biome
				Biome biome = world.getBiome(coords.toPos());

			    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList())) {
			    	Treasure.logger.debug(String.format("[%s] is not a valid biome.", biome.getBiomeName()));
			    	return;
			    }
			    
     			// 3. check against all registered dungeons
     			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.minDistancePerChest)) {
   					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
     				return;
     			}
     			
    			// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
    			Integer i = chunksSinceLastRarityChest.get(rarity);
    			i = 0;
    			
    			// generate the chest/pit/chambers
    			isGenerated = generators.get(rarity).generate(world, random, coords, rarity, Treasure.chestConfigs.get(rarity)); 

    			if (isGenerated) {
        			chunksSinceLastChest = 0;
    			}
    		}

	     	// save world data
    		GenDataPersistence savedData = GenDataPersistence.get(world);
	    	if (savedData != null) {
	    		savedData.markDirty();
	    	}
     	}
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	@SuppressWarnings("unused")
	private void generateNether(World world, Random random, int i, int j) {}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	@SuppressWarnings("unused")
	private void generateEnd(World world, Random random, int i, int j) {}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @param minDistance
	 * @return
	 */
	public boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {
		
		double minDistanceSq = minDistance * minDistance;
		
		// get a list of dungeons
		List<ChestInfo> infos = ChestRegistry.getInstance().getEntries();

		if (infos == null || infos.size() == 0) {
			Treasure.logger.debug("Unable to locate the Chest Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
//		    Dungeons2.log.debug("Dungeon dist^2: " + distance);
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the chunksSinceLastChest
	 */
	public int getChunksSinceLastChest() {
		return chunksSinceLastChest;
	}

	/**
	 * @param chunksSinceLastChest the chunksSinceLastChest to set
	 */
	public void setChunksSinceLastChest(int chunksSinceLastChest) {
		this.chunksSinceLastChest = chunksSinceLastChest;
	}

	/**
	 * @return the chunksSinceLastRarityChest
	 */
	public Map<Rarity, Integer> getChunksSinceLastRarityChest() {
		return chunksSinceLastRarityChest;
	}

	/**
	 * @param chunksSinceLastRarityChest the chunksSinceLastRarityChest to set
	 */
	public void setChunksSinceLastRarityChest(Map<Rarity, Integer> chunksSinceLastRarityChest) {
		this.chunksSinceLastRarityChest = chunksSinceLastRarityChest;
	}
}
