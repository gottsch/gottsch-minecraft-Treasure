/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.datafixers.Dynamic;
import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;

/**
 * NOTE: Feature is the equivalent to 1.12 WorldGenerator
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class SurfaceChestFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
	protected static int UNDERGROUND_OFFSET = 5;

	private Map<Rarity, Integer> chunksSinceLastRarityChest;

	private Map<String, Integer> chunksSinceLastDimensionChest = new HashMap<>();
	private Map<String, Map<Rarity, Integer>> chunksSinceLastDimensionRarityChest = new HashMap<>();

	/**
	 * 
	 * @param configFactory
	 */
	public SurfaceChestFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "surface_chest");

		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate SurfaceChestFeature:", e);
		}
	}

	/**
	 * 
	 */
	public void init() {
		// TODO all these values need to be indexed by dimension
		// ie Map<dimName, int chunksSinceLastChest>
		// ie Map<dimName, Map<Rarity, int chunksSinceLastRarityChest>
		// initialize chunks since last array
		//		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);

		// setup temporary rarity-generators map
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}

		// setup dimensional properties
		for (String dimension : TreasureConfig.GENERAL.dimensionsWhiteList.get()) {
			chunksSinceLastDimensionChest.put(dimension, 0);
			chunksSinceLastDimensionRarityChest.put(dimension, new HashMap<>(chunksSinceLastRarityChest));
		}
	}

	/**
	 * NOTE equivalent to 1.12 generate()
	 */
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random,
			BlockPos pos, NoFeatureConfig config) {

		String dimensionName = world.getDimension().getType().getRegistryName().toString();

		// test the dimension white list
		// TODO chests should have a properties for the depth range from "surface"
		if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimensionName)) {
			return false;
		}

		// spawn @ middle of chunk
		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS, 0, WorldInfo.CHUNK_RADIUS);

		// 0. hard check against ocean biomes
		Biome biome = world.getBiome(spawnCoords.toPos());
		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN ||
				BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return false;
		}

		Treasure.LOGGER.debug("in biome -> {} @ {}", biome.getRegistryName(), pos);

		// increment the chunk counts
		incrementDimensionalChestChunkCount(dimensionName);
		//		chunksSinceLastChest++;

		for (Rarity rarity : TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST)) {
			// TODO redo with map.merge(x,x, Integer:sum)
			// TODO add dimensional increment
			//			Integer i = chunksSinceLastRarityChest.get(rarity);
			//			chunksSinceLastRarityChest.put(rarity, ++i);	
			incrementDimensionalRarityChestChunkCount(dimensionName, rarity);
		}
		Treasure.LOGGER.debug("chunks since dimension {} last chest -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionChest.get(dimensionName), TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get());

		// test if min chunks was met
		if (chunksSinceLastDimensionChest.get(dimensionName)/*chunksSinceLastChest*/ > TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get()) {

			// the get first surface y (could be leaves, trunk, water, etc)
			int ySpawn = world.getChunk(pos).getTopBlockY(Heightmap.Type.WORLD_SURFACE, WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
			spawnCoords = spawnCoords.withY(ySpawn);
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());
			//			chunksSinceLastChest = 0;
			chunksSinceLastDimensionChest.put(dimensionName, 0);

			// determine what type to generate
			Rarity rarity = (Rarity) TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).get(random.nextInt(TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).size()));
			Treasure.LOGGER.debug("rarity -> {}", rarity);
			IChestConfig chestConfig = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			if (chestConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
				return false;
			}

			// get the chunks for dimensional rarity chest
			int chunksPerRarity = chunksSinceLastDimensionRarityChest.get(dimensionName).get(rarity);//chunksSinceLastRarityChest.get(rarity);

			Treasure.LOGGER.debug("chunks per rarity {} -> {}, config chunks per chest -> {}", rarity, chunksPerRarity, chestConfig.getChunksPerChest());
			if (chunksPerRarity >= chestConfig.getChunksPerChest()) {
				Treasure.LOGGER.debug("config gen prob -> {}", chestConfig.getGenProbability());
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
					return false;
				}

				// 2. test if the override (global) biome is allowed
				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList());
				if(biomeCheck == Result.BLACK_LISTED ) {
					if (WorldInfo.isClientSide(world.getWorld())) {
						Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getDisplayName().getString(), spawnCoords.toShortString());
					}
					else {
						Treasure.LOGGER.debug("Biome {} is not valid @ {}",rarity.getValue(), spawnCoords.toShortString());
					}					
					return false;
				}
				//				else if (biomeCheck == Result.OK) {
				//				    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeTypeWhiteList(), chestConfig.getBiomeTypeBlackList())) {
				//				    	if (Treasure.LOGGER.isDebugEnabled()) {
				//				    		if (WorldInfo.isClientSide((World)world)) {
				//				    			Treasure.LOGGER.debug("{} is not a valid biome type @ {}", biome.getDisplayName().getString(), spawnCoords.toShortString());
				//				    		}
				//				    		else {
				//				    			Treasure.LOGGER.debug("Biome type of {} is not valid @ {}",rarity.getValue(), spawnCoords.toShortString());
				//				    		}
				//				    	}
				//				    	return false;
				//				    }
				//				}

				// 3. check against all registered chests
				if (isRegisteredChestWithinDistance(world, spawnCoords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest.get())) {
					Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
					return false;
				}				

				// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
				chunksSinceLastDimensionRarityChest.get(dimensionName).put(rarity, 0);

				// generate the chest/pit/chambers
				Treasure.LOGGER.debug("Attempting to generate pit/chest.");
				Treasure.LOGGER.debug("rarity -> {}", rarity);
				Treasure.LOGGER.debug("randcollection -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).getClass().getSimpleName());
				Treasure.LOGGER.debug("gen -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next().getClass().getSimpleName());
				Treasure.LOGGER.debug("configmap -> {}", TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
				
				GeneratorResult<GeneratorData> result = null;
				result = generateChest(world, random, spawnCoords, rarity, TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next(), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));

				if (result.isSuccess()) {
					// add to registry
					TreasureData.CHEST_REGISTRIES.get(dimensionName).register(spawnCoords.toShortString(), new ChestInfo(rarity, spawnCoords));
					// reset the chunk counts
					chunksSinceLastDimensionChest.put(dimensionName, 0);
				}				
			}

			// save world data
			TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(world.getWorld());
			if (savedData != null) {
				savedData.markDirty();
			}
		}
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param rarity
	 * @param next
	 * @param iChestConfig
	 * @return
	 */
	private GeneratorResult<GeneratorData> generateChest(IWorld world, Random random, ICoords coords, Rarity rarity,
			IChestGenerator chestGenerator, IChestConfig config) {

		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// result from chest generation
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);

		ICoords chestCoords = null;
		ICoords markerCoords = null;
		boolean hasMarkers = true;
		boolean isSurfaceChest = false;

		// 1. collect location data points
		ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}
		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
		markerCoords = surfaceCoords;

		// 2. determine if above ground or below ground
		if (config.isSurfaceAllowed() && RandomHelper.checkProbability(random, TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability.get())) {
			isSurfaceChest = true;

			if (RandomHelper.checkProbability(random, TreasureConfig.GENERAL.surfaceStructureProbability.get())) {
				// no markers
				hasMarkers = false;

//				genResult = generateSurfaceRuins(world, random, surfaceCoords, config);
				Treasure.LOGGER.debug("surface result -> {}", genResult.toString());
				if (!genResult.isSuccess()) {
					return result.fail();
				}
				// set the chest coords to the surface pos
				chestCoords = genResult.getData().getChestContext().getCoords();
			}
			else {
				// set the chest coords to the surface pos
				chestCoords = new Coords(markerCoords);
				Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);
			}
		}
		else if (config.isSubterraneanAllowed()) {
			Treasure.LOGGER.debug("else generate pit");
			genResult = generatePit(world, random, rarity, markerCoords, config);
			Treasure.LOGGER.debug("result -> {}", genResult.toString());
			if (!genResult.isSuccess()) {
				return result.fail();
			}
			chestCoords = genResult.getData().getChestContext().getCoords();
		}
		
		// if chest isn't generated, then fail
		if (chestCoords == null) {
			Treasure.LOGGER.debug("chest coords were not provided in result -> {}", genResult.toString());
			return result.fail();
		}

		// add markers (above chest or shaft)
		if (hasMarkers) {
			Treasure.LOGGER.debug("marker coords -> {}", markerCoords);
			chestGenerator.addMarkers(world, random, markerCoords, isSurfaceChest);
		}

		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, rarity, genResult.getData().getChestContext().getState());
		if (!chestResult.isSuccess()) {
			return result.fail();
		}
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, markerCoords.toShortString());
		result.setData(chestResult.getData());
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
	public GeneratorResult<ChestGeneratorData> generatePit(IWorld world, Random random, Rarity chestRarity, ICoords markerCoords, IChestConfig config) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);
		GeneratorResult<ChestGeneratorData> pitResult = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);

		// check if it has 50% land
		if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
			Treasure.LOGGER.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
			return result.fail();
		}

		// determine spawn coords below ground
		ICoords spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());

		if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.LOGGER.debug("Unable to spawn underground @ {}", markerCoords);
			return result.fail();
		}
		Treasure.LOGGER.debug("Below ground @ {}", spawnCoords.toShortString());
		result.getData().setSpawnCoords(markerCoords);

		// select a pit generator
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(random);
		Treasure.LOGGER.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());
		
		// 3. build the pit
		pitResult = pitGenerator.generate(world, random, markerCoords, spawnCoords);

		if (!pitResult.isSuccess()) return result.fail();

		result.setData(pitResult.getData());
		Treasure.LOGGER.debug("Is pit generated: {}", pitResult.isSuccess());
		return result.success();
	}
	
	/**
	 * Land Only
	 * @param world
	 * @param random
	 * @param pos
	 * @param spawnYMin
	 * @return
	 */
	public static ICoords getUndergroundSpawnPos(IWorld world, Random random, ICoords pos, int spawnYMin) {
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
	 * Land Only
	 * @param random
	 * @return
	 */
	public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
		PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.PITS.pitStructureProbability.get()) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
		Treasure.LOGGER.debug("using pit type -> {}", pitType);
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = TreasureData.PIT_GENS.row(pitType).values().stream()
				.collect(Collectors.toList());
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
	
	/**
	 * 
	 * @param dimensionName
	 * @param rarity
	 */
	private void incrementDimensionalRarityChestChunkCount(String dimensionName, Rarity rarity) {
		chunksSinceLastDimensionRarityChest.get(dimensionName).merge(rarity, 1, Integer::sum);
	}

	/**
	 * 
	 * @param dimensionName
	 */
	private void incrementDimensionalChestChunkCount(String dimensionName) {
		chunksSinceLastDimensionChest.merge(dimensionName, 1, Integer::sum);		
	}

	// TODO move to interface or abstract
	/**
	 * 
	 * @param world
	 * @param pos
	 * @param minDistance
	 * @return
	 */
	public boolean isRegisteredChestWithinDistance(IWorld world, ICoords coords, int minDistance) {

		double minDistanceSq = minDistance * minDistance;

		// get a list of dungeons
		List<ChestInfo> infos = TreasureData.CHEST_REGISTRIES.get(world.getDimension().getType().getRegistryName().toString()).getValues();

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

	@Override
	public Map<String, Integer> getChunksSinceLastDimensionFeature() {
		return this.chunksSinceLastDimensionChest;
	}

	@Override
	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature() {
		return this.chunksSinceLastDimensionRarityChest;
	}

}
