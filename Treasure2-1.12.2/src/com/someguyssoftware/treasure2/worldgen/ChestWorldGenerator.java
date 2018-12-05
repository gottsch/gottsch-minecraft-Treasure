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
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.chest.AbstractChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.EpicChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.GoldSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.RareChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ScarceChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.SkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.UncommonChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.AirPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.MobTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.TntTrapPitGenerator;
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

	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	
	// the chest generators
	private Map<Rarity, AbstractChestGenerator> generators = new HashMap<>();
	private Map<Rarity, RandomWeightedCollection<IChestGenerator>> gens = new HashMap<>();
	
	// TODO probably should be moved to AbstractChestGenerator
	// the pit generators
	public static Map<Pits, IPitGenerator> pitGenerators = new HashMap<>();
	
	/**
	 * 
	 */
	public ChestWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate ChestGenerator:", e);
		}
	}
	
	private void init() {
		// initialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}

		// setup the chest  generators
		generators.put(Rarity.COMMON, new CommonChestGenerator());
		generators.put(Rarity.UNCOMMON, new UncommonChestGenerator());
		generators.put(Rarity.SCARCE, new ScarceChestGenerator());
		generators.put(Rarity.RARE, new RareChestGenerator());
		generators.put(Rarity.EPIC, new EpicChestGenerator());
				
		gens.put(Rarity.COMMON, new RandomWeightedCollection<>());
		gens.put(Rarity.UNCOMMON, new RandomWeightedCollection<>());
		gens.put(Rarity.SCARCE, new RandomWeightedCollection<>());
		gens.put(Rarity.RARE, new RandomWeightedCollection<>());
		gens.put(Rarity.EPIC, new RandomWeightedCollection<>());
		
		gens.get(Rarity.COMMON).add(1, new CommonChestGenerator());
		gens.get(Rarity.UNCOMMON).add(1, new UncommonChestGenerator());
		gens.get(Rarity.SCARCE).add(75, new ScarceChestGenerator());
		gens.get(Rarity.SCARCE).add(25, new SkullChestGenerator());
		gens.get(Rarity.RARE).add(85, new RareChestGenerator());
		gens.get(Rarity.RARE).add(15, new GoldSkullChestGenerator());
		gens.get(Rarity.EPIC).add(1, new EpicChestGenerator());
		
		// setup the pit generators
		pitGenerators.put(Pits.SIMPLE_PIT, new SimplePitGenerator());
		pitGenerators.put(Pits.TNT_TRAP_PIT, new TntTrapPitGenerator());
		pitGenerators.put(Pits.AIR_PIT,  new AirPitGenerator());
		pitGenerators.put(Pits.LAVA_TRAP_PIT, new LavaTrapPitGenerator());
		pitGenerators.put(Pits.MOB_TRAP_PIT, new MobTrapPitGenerator());
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
			chunksSinceLastRarityChest.put(rarity, ++i);
		}
        boolean isGenerated = false;	
        
		// test if min chunks was met
     	if (chunksSinceLastChest > TreasureConfig.minChunksPerChest) {
//     		Treasure.logger.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLastChest, TreasureConfig.minChunksPerChest));

     		/*
     		 * get current chunk position
     		 */            
            // spawn @ middle of chunk
            int xSpawn = chunkX * 16 + 8;
            int zSpawn = chunkZ * 16 + 8;
            
            // the get first surface y (could be leaves, trunk, water, etc)
            int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(8, 8);
            ICoords coords = new Coords(xSpawn, ySpawn, zSpawn);

	    	// determine what type to generate
        	Rarity rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
//			Treasure.logger.debug("Using Rarity: {}", rarity );
			IChestConfig chestConfig = Configs.chestConfigs.get(rarity);
			if (chestConfig == null) {
				Treasure.logger.warn("Unable to locate a chest for rarity {}.", rarity);
				return;
			}
//			Treasure.logger.debug("Chunks since last {} chest: {}", rarity,  chunksSinceLastRarityChest.get(rarity) );
//			Treasure.logger.debug("Chunks per {} chest: {}", rarity, chestConfig.getChunksPerChest());
    		if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
    			
				// 1. test if chest meets the probability criteria
//    			Treasure.logger.debug("{} chest probability: {}", rarity, chestConfig.getGenProbability());
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.logger.debug("Chest does not meet generate probability.");
					return;
				}
//				else {
//					Treasure.logger.debug("Chest MEETS generate probability!");
//				}
				
				// 2. test if correct biome
				Biome biome = world.getBiome(coords.toPos());

			    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList())) {
			    	if (Treasure.logger.isDebugEnabled()) {
			    		if (world.isRemote) {
			    			Treasure.logger.debug("{} is not a valid biome @ {}", biome.getBiomeName(), coords.toShortString());
			    		}
			    		else {
			    			Treasure.logger.debug("Biome is not valid @ {}", coords.toShortString());
			    		}
			    	}
			    	return;
			    }
			    
     			// 3. check against all registered chests
     			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.minDistancePerChest)) {
   					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
     				return;
     			}
     			
    			// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
//    			Integer i = chunksSinceLastRarityChest.get(rarity);
//    			i = 0;
    			chunksSinceLastRarityChest.put(rarity, 0);
 			
    			// generate the chest/pit/chambers
				Treasure.logger.debug("Attempting to generate pit/chest.");
				isGenerated = gens.get(rarity).next().generate(world, random, coords, rarity, Configs.chestConfigs.get(rarity)); 
//    			isGenerated = generators.get(rarity).generate(world, random, coords, rarity, Configs.chestConfigs.get(rarity)); 

    			if (isGenerated) {
    				// add to registry
    				ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(rarity, coords));
    				// reset the chunk counts
        			chunksSinceLastChest = 0;
//        			chunksSinceLastRarityChest.put(rarity, 0);
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
		
//		Treasure.logger.debug("Min distance Sq -> {}", minDistanceSq);
		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
//			Treasure.logger.debug("Chest dist^2: " + distance);
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

	/**
	 * @return the generators
	 */
	public Map<Rarity, AbstractChestGenerator> getGenerators() {
		return generators;
	}

	/**
	 * @param generators the generators to set
	 */
	public void setGenerators(Map<Rarity, AbstractChestGenerator> generators) {
		this.generators = generators;
	}

	/**
	 * @return the gens
	 */
	public Map<Rarity, RandomWeightedCollection<IChestGenerator>> getGens() {
		return gens;
	}

	/**
	 * @param gens the gens to set
	 */
	public void setGens(Map<Rarity, RandomWeightedCollection<IChestGenerator>> gens) {
		this.gens = gens;
	}
}
