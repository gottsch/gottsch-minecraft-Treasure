 /**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import static com.someguyssoftware.treasure2.enums.Rarity.COMMON;
import static com.someguyssoftware.treasure2.enums.Rarity.EPIC;
import static com.someguyssoftware.treasure2.enums.Rarity.RARE;
import static com.someguyssoftware.treasure2.enums.Rarity.SCARCE;
import static com.someguyssoftware.treasure2.enums.Rarity.UNCOMMON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.CauldronChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.EpicChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.GoldSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.RareChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ScarceChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.SkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.UncommonChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.AirPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.BigBottomMobTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.CollapsingTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaSideTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.MobTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.StructurePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.TntTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.ruins.SurfaceRuinGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class SurfaceChestWorldGenerator implements ITreasureWorldGenerator {
	protected static int UNDERGROUND_OFFSET = 5;
	
	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	
	// the chest chestGeneratorsMap
	private Map<Rarity, RandomWeightedCollection<IChestGenerator>> chestGenMap = new HashMap<>();

	// the pit chestGeneratorsMap
	public static Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGens =  HashBasedTable.create();
	
	public static final List<Rarity> RARITIES = new ArrayList<>();
	
	/**
	 * 
	 */
	public SurfaceChestWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate SurfaceChestGenerator:", e);
		}
	}
	
	@Override
	public void init() {
		// TODO all these values need to be indexed by dimension
		// initialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		
		// setup temporary rarity-generators map
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}
	
		// setup chest collection generator maps
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(COMMON).isEnableChest()) {
			RARITIES.add(COMMON);
			chestGenMap.put(COMMON, new RandomWeightedCollection<>());
			chestGenMap.get(COMMON).add(1, new CommonChestGenerator());
		}		
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(UNCOMMON).isEnableChest()) {
			RARITIES.add(UNCOMMON);
			chestGenMap.put(UNCOMMON, new RandomWeightedCollection<>());
			chestGenMap.get(UNCOMMON).add(1, new UncommonChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(SCARCE).isEnableChest()) {
			RARITIES.add(SCARCE);
			chestGenMap.put(SCARCE, new RandomWeightedCollection<>());
			chestGenMap.get(SCARCE).add(75, new ScarceChestGenerator());
			chestGenMap.get(SCARCE).add(25, new SkullChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(RARE).isEnableChest()) {
			RARITIES.add(RARE);
			chestGenMap.put(RARE, new RandomWeightedCollection<>());
			chestGenMap.get(RARE).add(85, new RareChestGenerator());
			chestGenMap.get(RARE).add(15, new GoldSkullChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(EPIC).isEnableChest()) {
			RARITIES.add(EPIC);
			chestGenMap.put(EPIC, new RandomWeightedCollection<>());
			chestGenMap.get(EPIC).add(85, new EpicChestGenerator());
			chestGenMap.get(EPIC).add(15, new CauldronChestGenerator());
		}		
		
		// setup pit generators map
		pitGens.put(PitTypes.STANDARD, Pits.SIMPLE_PIT, new SimplePitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.SIMPLE_PIT, new StructurePitGenerator(new SimplePitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.TNT_TRAP_PIT, new TntTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.TNT_TRAP_PIT, new StructurePitGenerator(new TntTrapPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.AIR_PIT,  new AirPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.AIR_PIT, new StructurePitGenerator(new AirPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.LAVA_TRAP_PIT, new LavaTrapPitGenerator());
		// NONE for STRUCTURE
		
		pitGens.put(PitTypes.STANDARD, Pits.MOB_TRAP_PIT, new MobTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.MOB_TRAP_PIT, new StructurePitGenerator(new MobTrapPitGenerator()));
				
		pitGens.put(PitTypes.STANDARD, Pits.LAVA_SIDE_TRAP_PIT, new LavaSideTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.LAVA_SIDE_TRAP_PIT, new StructurePitGenerator(new LavaSideTrapPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.BIG_BOTTOM_MOB_TRAP_PIT, new BigBottomMobTrapPitGenerator());
		// NONE for STRUCTURE
		
		pitGens.put(PitTypes.STANDARD, Pits.COLLAPSING_TRAP_PIT,  new CollapsingTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.COLLAPSING_TRAP_PIT, new StructurePitGenerator(new CollapsingTrapPitGenerator()));
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
	 * @param chunkX
	 * @param chunkZ
	 */
	private void generate(World world, Random random, int chunkX, int chunkZ) {
 		/*
 		 * get current chunk position
 		 */            
        // spawn @ middle of chunk
        int xSpawn = (chunkX * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        int zSpawn = (chunkZ * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
        
		// 0. hard check against ocean biomes
        ICoords coords = new Coords(xSpawn, 0, zSpawn);
		Biome biome = world.getBiome(coords.toPos());
		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN ||
				BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return;
		}
		
		// increment the chunk counts
		chunksSinceLastChest++;

		for (Rarity rarity : RARITIES) {
			Integer i = chunksSinceLastRarityChest.get(rarity);
			chunksSinceLastRarityChest.put(rarity, ++i);			
		}

		// test if min chunks was met
     	if (chunksSinceLastChest > TreasureConfig.CHESTS.surfaceChests.minChunksPerChest) {
            
            // the get first surface y (could be leaves, trunk, water, etc)
            int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
            coords = new Coords(xSpawn, ySpawn, zSpawn);

	    	// determine what type to generate
            Rarity rarity = (Rarity) RARITIES.get(random.nextInt(RARITIES.size()));
			IChestConfig chestConfig = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			if (chestConfig == null) {
				Treasure.logger.warn("Unable to locate a chest for rarity {}.", rarity);
				return;
			}
//			Treasure.logger.debug("Chunks since last {} chest: {}", rarity,  chunksSinceLastRarityChest.get(rarity) );
//			Treasure.logger.debug("Chunks per {} chest: {}", rarity, chestConfig.getChunksPerChest());
    		if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
    			    			
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
//					Treasure.logger.debug("ChestConfig does not meet generate probability.");
					return;
				}
				
				// 2. test if the override (global) biome is allowed
				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList());
				if(biomeCheck == Result.BLACK_LISTED ) {
		    		if (WorldInfo.isClientSide(world)) {
		    			Treasure.logger.debug("{} is not a valid biome @ {}", biome.getBiomeName(), coords.toShortString());
		    		}
		    		else {
		    			Treasure.logger.debug("Biome {} is not valid @ {}",rarity.getValue(), coords.toShortString());
		    		}					
					return;
				}
				else if (biomeCheck == Result.OK) {
				    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeTypeWhiteList(), chestConfig.getBiomeTypeBlackList())) {
				    	if (Treasure.logger.isDebugEnabled()) {
				    		if (WorldInfo.isClientSide(world)) {
				    			Treasure.logger.debug("{} is not a valid biome type @ {}", biome.getBiomeName(), coords.toShortString());
				    		}
				    		else {
				    			Treasure.logger.debug("Biome type of {} is not valid @ {}",rarity.getValue(), coords.toShortString());
				    		}
				    	}
				    	return;
				    }
				}
			    
     			// 3. check against all registered chests
     			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest)) {
   					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
     				return;
     			}
     			     			
    			// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
    			chunksSinceLastRarityChest.put(rarity, 0);
 			
    			// generate the chest/pit/chambers
				Treasure.logger.debug("Attempting to generate pit/chest.");
				GeneratorResult<GeneratorData> result = null;
				result = generate(world, random, coords, rarity, chestGenMap.get(rarity).next(), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
				
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
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chestRarity
	 * @param chestGenerator
	 * @param config
	 * @return
	 */
	private GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords, Rarity chestRarity,
			IChestGenerator chestGenerator, IChestConfig config) {
		
		ICoords chestCoords = null;
		ICoords markerCoords = null;
		boolean hasMarkers = true;
		boolean isSurfaceChest = false;

		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// result from chest generation
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);		

		// 1. collect location data points
		ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.logger.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}
		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
		markerCoords = surfaceCoords;
		
		// 2. determine if above ground or below ground
		if (config.isSurfaceAllowed() && RandomHelper.checkProbability(random, TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability)) {
			isSurfaceChest = true;
			
			if (RandomHelper.checkProbability(random, TreasureConfig.WORLD_GEN.getGeneralProperties().surfaceStructureProbability)) {
				// no markers
				hasMarkers = false;
				
				genResult = generateSurfaceRuins(world, random, surfaceCoords, config);
				Treasure.logger.debug("surface result -> {}", genResult.toString());
				if (!genResult.isSuccess()) {
					return result.fail();
				}
				// set the chest coords to the surface pos
				chestCoords = genResult.getData().getChestContext().getCoords();
			}
			else {
				// set the chest coords to the surface pos
				chestCoords = new Coords(markerCoords);
			}
		}
		else if (config.isSubterraneanAllowed()) {
			Treasure.logger.debug("else generate pit");
			genResult = generatePit(world, random, chestRarity, markerCoords, config);
			Treasure.logger.debug("result -> {}", genResult.toString());
			if (!genResult.isSuccess()) {
				return result.fail();
			}
			chestCoords = genResult.getData().getChestContext().getCoords();
		}			
				
		// if chest isn't generated, then fail
		if (chestCoords == null) {
			Treasure.logger.debug("Chest coords were not provided in result -> {}", genResult.toString());
			return result.fail();
		}

		// add markers (above chest or shaft)
		if (hasMarkers) {
			chestGenerator.addMarkers(world, random, markerCoords, isSurfaceChest);
		}		
		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, chestRarity, genResult.getData().getChestContext().getState());
		if (!chestResult.isSuccess()) {
			return result.fail();
		}
		
		Treasure.logger.info("CHEATER! {} chest at coords: {}", chestRarity, markerCoords.toShortString());
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
	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(World world, Random random, ICoords spawnCoords,
			IChestConfig config) {
		return generateSurfaceRuins(world, random, spawnCoords, null, null, config);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param decayProcessor
	 * @param config
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(World world, Random random, ICoords spawnCoords,
			TemplateHolder holder, IDecayRuleSet decayRuleSet, IChestConfig config) {

		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
		result.getData().setSpawnCoords(spawnCoords);

		SurfaceRuinGenerator generator = new SurfaceRuinGenerator();

		// build the structure
		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, random, spawnCoords, holder, decayRuleSet);
		Treasure.logger.debug("surface struct result -> {}", genResult);
		if (!genResult.isSuccess()) return result.fail();

		result.setData(genResult.getData()); // TODO this step is unneeded now.
		return result.success();
	}
	
	/**
	 * Land Only
	 * @param world
	 * @param random
	 * @param chestRarity
	 * @param markerCoords
	 * @param config
	 * @return
	 */
	public static GeneratorResult<ChestGeneratorData> generatePit(World world, Random random, Rarity chestRarity, ICoords markerCoords, IChestConfig config) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);
		GeneratorResult<ChestGeneratorData> pitResult = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);

		// 2.5. check if it has 50% land
		if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
			return result.fail();
		}

		// determine spawn coords below ground
		ICoords spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());

		if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Unable to spawn underground @ {}", markerCoords);
			return result.fail();
		}
		Treasure.logger.debug("Below ground @ {}", spawnCoords.toShortString());
		result.getData().setSpawnCoords(markerCoords);

		// select a pit generator
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(random);
		Treasure.logger.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());
		
		// 3. build the pit
		pitResult = pitGenerator.generate(world, random, markerCoords, spawnCoords);

		if (!pitResult.isSuccess()) return result.fail();

		result.setData(pitResult.getData());
		Treasure.logger.debug("Is pit generated: {}", pitResult.isSuccess());
		return result.success();
	}

	/**
	 * Land Only
	 * @param random
	 * @return
	 */
	public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
		PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.PIT.pitStructureProbability) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = pitGens.row(pitType).values().stream()
				.collect(Collectors.toList());
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.logger.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}

	/**
	 * Land Only
	 * @param world
	 * @param random
	 * @param pos
	 * @param spawnYMin
	 * @return
	 */
	public static ICoords getUndergroundSpawnPos(World world, Random random, ICoords pos, int spawnYMin) {
		ICoords spawnPos = null;

		// spawn location under ground
		if (pos.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
			int ySpawn = random.nextInt(pos.getY()
					- (spawnYMin + UNDERGROUND_OFFSET))
					+ spawnYMin;

			spawnPos = new Coords(pos.getX(), ySpawn, pos.getZ());
			// get floor pos (if in a cavern or tunnel etc)
			spawnPos = WorldInfo.getDryLandSurfaceCoords(world, spawnPos);
		}
		return spawnPos;
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

	// TODO move to interface or abstract
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
			Treasure.logger.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
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

	public Map<Rarity, RandomWeightedCollection<IChestGenerator>> getChestGenMap() {
		return chestGenMap;
	}

	public void setChestGenMap(Map<Rarity, RandomWeightedCollection<IChestGenerator>> chestGenMap) {
		this.chestGenMap = chestGenMap;
	}

	public static Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> getPitGens() {
		return pitGens;
	}

	public static void setPitGens(Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGens) {
		SurfaceChestWorldGenerator.pitGens = pitGens;
	}
}
