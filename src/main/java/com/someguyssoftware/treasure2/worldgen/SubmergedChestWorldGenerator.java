 /**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import static com.someguyssoftware.treasure2.enums.Rarity.COMMON;
import static com.someguyssoftware.treasure2.enums.Rarity.EPIC;
import static com.someguyssoftware.treasure2.enums.Rarity.LEGENDARY;
import static com.someguyssoftware.treasure2.enums.Rarity.MYTHICAL;
import static com.someguyssoftware.treasure2.enums.Rarity.RARE;
import static com.someguyssoftware.treasure2.enums.Rarity.SCARCE;
import static com.someguyssoftware.treasure2.enums.Rarity.UNCOMMON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.CauldronChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CrystalSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.EpicChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.GoldSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.LegendaryChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.MythicalChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.RareChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ScarceChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.SkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.UncommonChestGenerator;
import com.someguyssoftware.treasure2.generator.ruins.SubmergedRuinGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;

/**
 * 
 * @author Mark Gottschling on Dec 3, 2019
 *
 */
public class SubmergedChestWorldGenerator implements ITreasureWorldGenerator {
	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;

	private Map<Rarity, RandomWeightedCollection<IChestGenerator>> chestCollectionGeneratorsMap = new HashMap<>();

	private static final List<Rarity> RARITIES = new ArrayList<>();
	
	/**
	 * 
	 */
	public SubmergedChestWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate SurfaceChestGenerator:", e);
		}
	}
	
	public void init() {
		RARITIES.clear();
		
		// initialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		
		// setup temporary rarity-generators map
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}
				
		// setup chest collection generator maps
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(COMMON).isEnableChest()) {
			RARITIES.add(COMMON);
			chestCollectionGeneratorsMap.put(COMMON, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(COMMON).add(1, new CommonChestGenerator()); 
		}		
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(UNCOMMON).isEnableChest()) {
			RARITIES.add(UNCOMMON);
			chestCollectionGeneratorsMap.put(UNCOMMON, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(UNCOMMON).add(1, new UncommonChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(SCARCE).isEnableChest()) {
			RARITIES.add(SCARCE);
			chestCollectionGeneratorsMap.put(SCARCE, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(SCARCE).add(75, new ScarceChestGenerator());
			chestCollectionGeneratorsMap.get(SCARCE).add(25, new SkullChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(RARE).isEnableChest()) {
			RARITIES.add(RARE);
			chestCollectionGeneratorsMap.put(RARE, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(RARE).add(85, new RareChestGenerator());
			chestCollectionGeneratorsMap.get(RARE).add(15, new GoldSkullChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(EPIC).isEnableChest()) {
			RARITIES.add(EPIC);
			chestCollectionGeneratorsMap.put(EPIC, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(EPIC).add(85, new EpicChestGenerator());
			chestCollectionGeneratorsMap.get(EPIC).add(15, new CauldronChestGenerator());
			chestCollectionGeneratorsMap.get(EPIC).add(15, new CrystalSkullChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(LEGENDARY).isEnableChest()) {
			RARITIES.add(LEGENDARY);
			chestCollectionGeneratorsMap.put(LEGENDARY, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(LEGENDARY).add(1, new LegendaryChestGenerator()); 
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(MYTHICAL).isEnableChest()) {
			RARITIES.add(MYTHICAL);
			chestCollectionGeneratorsMap.put(MYTHICAL, new RandomWeightedCollection<>());
			chestCollectionGeneratorsMap.get(MYTHICAL).add(1, new MythicalChestGenerator()); 
		}
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (TreasureConfig.WORLD_GEN.getGeneralProperties().getDimensionsWhiteList().contains(Integer.valueOf(world.provider.getDimension()))) {
			generate(world, random, chunkX, chunkZ);
		}
		
//		switch(world.provider.getDimension()){
//		case 0:
//		    generateInOverworld(world, random, chunkX, chunkZ);
//		    break;
//	    default:
//	    	break;
//		}
	}		


	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	private void generate(World world, Random random, int chunkX, int chunkZ) {
 		/*
 		 * get current chunk position
 		 */            
        // spawn @ middle of chunk
        int xSpawn = (chunkX * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        int zSpawn = (chunkZ * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        
		// 0. hard check for ocean biomes
        ICoords coords = new Coords(xSpawn, 0, zSpawn);
		Biome biome = world.getBiome(coords.toPos());
		if (biome != Biomes.OCEAN && biome != Biomes.DEEP_OCEAN && biome != Biomes.FROZEN_OCEAN &&
				!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return;
		}
		
		// increment the chunk counts
		chunksSinceLastChest++;
		for (Rarity rarity : RARITIES) {
			Integer i = chunksSinceLastRarityChest.get(rarity);
			chunksSinceLastRarityChest.put(rarity, ++i);			
		}

		// test if min chunks was met
     	if (chunksSinceLastChest > TreasureConfig.CHESTS.submergedChests.minChunksPerChest) {
           
            // the get first surface y (could be leaves, trunk, water, etc)
            int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
            coords = new Coords(xSpawn, ySpawn, zSpawn);

	    	// determine what type to generate
            Rarity rarity = (Rarity) RARITIES.get(random.nextInt(RARITIES.size()));
			IChestConfig chestConfig = TreasureConfig.CHESTS.submergedChests.configMap.get(rarity); //Configs.chestConfigs.get(rarity);
			if (chestConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
				return;
			}
			
    		if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.LOGGER.debug("Submerged chest does not meet generate probability.");
					return;
				}
				
				// 2. test if the override (global) biome is allowed
				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList());
				if(biomeCheck == Result.BLACK_LISTED ) {
		    		if (WorldInfo.isClientSide(world)) {
		    			Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getBiomeName(), coords.toShortString());
		    		}
		    		else {
		    			Treasure.LOGGER.debug("Biome {} is not valid @ {}",rarity.getValue(), coords.toShortString());
		    		}					
					return;
				}
				else if (biomeCheck == Result.OK) {
				    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeTypeWhiteList(), chestConfig.getBiomeTypeBlackList())) {
				    	if (Treasure.LOGGER.isDebugEnabled()) {
				    		if (WorldInfo.isClientSide(world)) {
				    			Treasure.LOGGER.debug("{} is not a valid biome type @ {}", biome.getBiomeName(), coords.toShortString());
				    		}
				    		else {
				    			Treasure.LOGGER.debug("Biome type of {} is not valid @ {}",rarity.getValue(), coords.toShortString());
				    		}
				    	}
				    	return;
				    }
				}
			    
     			// 3. check against all registered chests
     			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.CHESTS.submergedChests.minDistancePerChest)) {
   					Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
     				return;
     			}
     			     			
    			// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
    			chunksSinceLastRarityChest.put(rarity, 0);
 			
    			// generate the chest/pit/chambers
				Treasure.LOGGER.debug("Attempting to generate pit/chest.");
				GeneratorResult<GeneratorData> result = null;
				result = generate(world, random, coords, rarity, chestCollectionGeneratorsMap.get(rarity).next(), TreasureConfig.CHESTS.submergedChests.configMap.get(rarity));
				
    			if (result.isSuccess()) {
    				// add to registry
    				ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(rarity, coords));
    				// reset the chunk counts
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
	
	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords, Rarity chestRarity, 
			IChestGenerator chestSelector, IChestConfig config) {
		ICoords chestCoords = null;
		ICoords markerCoords = null;

		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);		
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);		

		// 1. collect location data points
		ICoords surfaceCoords = WorldInfo.getOceanFloorSurfaceCoords(world, coords);
		Treasure.LOGGER.debug("ocean floor surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.LOGGER.debug("ocean floor surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}
		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
		markerCoords = surfaceCoords;

		genResult = generateSubmergedRuins(world, random, /*spawnCoords*/ surfaceCoords, config);
		Treasure.LOGGER.debug("submerged result -> {}", genResult.toString());
		if (!genResult.isSuccess()) {
			return result.fail();
		}
		if (genResult.getData().getChestContext() == null) return result.fail();
		
		chestCoords = genResult.getData().getChestContext().getCoords();
		
		// if chest isn't generated, then fail
		if (chestCoords == null) return result.fail();
		markerCoords = genResult.getData().getSpawnCoords();
		if (markerCoords == null) {
			markerCoords = surfaceCoords;
		}
		Treasure.LOGGER.debug("submerged spawn coords -> {}", markerCoords.toShortString());

		GeneratorResult<ChestGeneratorData> chestResult = chestSelector.generate(world, random, chestCoords, chestRarity, genResult.getData().getChestContext().getState());
		if (!chestResult.isSuccess()) {
			return result.fail();
		}
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", chestRarity, markerCoords.toShortString());
		result.setData(chestResult.getData());
		return result.success();
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generateSubmergedRuins(World world, Random random, ICoords spawnCoords,
			IChestConfig config) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
		result.getData().setSpawnCoords(spawnCoords);

		SubmergedRuinGenerator generator = new SubmergedRuinGenerator();

		// build the structure
		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, random, spawnCoords);
		Treasure.LOGGER.debug("submerged struct result -> {}", genResult);
		if (!genResult.isSuccess()) return result.fail();

		result.setData(genResult.getData());
		return result.success();
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
		List<ChestInfo> infos = ChestRegistry.getInstance().getValues();

		if (infos == null || infos.size() == 0) {
			Treasure.LOGGER.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
			return false;
		}
		
		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}

	public int getChunksSinceLastChest() {
		return chunksSinceLastChest;
	}

	public void setChunksSinceLastChest(int chunksSinceLastChest) {
		this.chunksSinceLastChest = chunksSinceLastChest;
	}

	public Map<Rarity, Integer> getChunksSinceLastRarityChest() {
		return chunksSinceLastRarityChest;
	}

	public void setChunksSinceLastRarityChest(Map<Rarity, Integer> chunksSinceLastRarityChest) {
		this.chunksSinceLastRarityChest = chunksSinceLastRarityChest;
	}

	public Map<Rarity, RandomWeightedCollection<IChestGenerator>> getChestCollectionGeneratorsMap() {
		return chestCollectionGeneratorsMap;
	}

	public void setChestCollectionGeneratorsMap(
			Map<Rarity, RandomWeightedCollection<IChestGenerator>> chestCollectionGeneratorsMap) {
		this.chestCollectionGeneratorsMap = chestCollectionGeneratorsMap;
	}
}
