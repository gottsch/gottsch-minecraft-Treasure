/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.WorldInfo.SURFACE;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
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
import com.someguyssoftware.treasure2.generator.ruins.SurfaceRuinGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.material.Material;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

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
	public SurfaceChestFeature(Codec<NoFeatureConfig> configFactory) {
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
	 * NOTE only use seedReader.setblockState() and that only allows you to access the 3x3 chunk.
	 *  chest/pit spawn IS doable as long as you keep it within the 3x3 chunk area, else would have to use a Structures setup
	 */
	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		ServerWorld world = seedReader.getLevel();
//		Treasure.LOGGER.debug("in surface feature for pos @ -> {}", pos.toShortString());
		ResourceLocation dimension = WorldInfo.getDimension(world);
   		
		// TODO surely this check could be done somewhere else, like registering in BiomeLoadingEvent ?
		// test the dimension white list
		if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimension.toString())) {
			return false;
		}
		
		BlockPos centerOfChunk = pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1);
//		Treasure.LOGGER.debug("center of chunk @ -> {}", centerOfChunk.toShortString());
		
		// spawn @ middle of chunk
//		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS, 254, WorldInfo.CHUNK_RADIUS);


		// increment the chunk counts
		incrementDimensionalChestChunkCount(dimension.toString());

		for (Rarity rarity : TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST)) {
			incrementDimensionalRarityChestChunkCount(dimension.toString(), rarity);
		}
//		Treasure.LOGGER.debug("chunks since dimension {} last chest -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionChest.get(dimensionName), TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get());

		// test if min chunks was met
		if (chunksSinceLastDimensionChest.get(dimension.toString())/*chunksSinceLastChest*/ > TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get()) {
			Treasure.LOGGER.debug("passed min chunks test");
			// the get first surface y (could be leaves, trunk, water, etc)
//			int ySpawn = seedReader.getChunk(pos).getHeight(Heightmap.Type.WORLD_SURFACE, pos.getX() + WorldInfo.CHUNK_RADIUS, pos.getZ() + WorldInfo.CHUNK_RADIUS);
			int landHeight = generator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			ICoords spawnCoords = new Coords(centerOfChunk.getX(), landHeight, centerOfChunk.getZ());
			Treasure.LOGGER.debug("height @ spawnCoords2 (landHeight/surface coords) -> {}", spawnCoords.toShortString());

//Treasure.LOGGER.debug("checking for height @ -> {}", spawnCoords.toShortString());
//			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(seedReader, spawnCoords);
//ICoords surfaceCoords = new Coords(pos.getX(), 100, pos.getY());
//ICoords surfaceCoords = new Coords(spawnCoords);
//			if (surfaceCoords == null) {
//				Treasure.LOGGER.debug("invalid surface type");
//				return false;
//			}
//			Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
			// TODO check if surface is land
			
//			Treasure.LOGGER.debug("ySpawn -> {}", surfaceCoords.getY());
//			spawnCoords = spawnCoords.withY(surfaceCoords.getY());
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());

			chunksSinceLastDimensionChest.put(dimension.toString(), 0);

			// determine what type to generate
			Rarity rarity = (Rarity) TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).get(random.nextInt(TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).size()));
			Treasure.LOGGER.debug("rarity -> {}", rarity);
			IChestConfig chestConfig = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			if (chestConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
				return false;
			}
			Treasure.LOGGER.debug("config for rarity -> {} = {}", rarity, chestConfig);
			// get the chunks for dimensional rarity chest
			int chunksPerRarity = chunksSinceLastDimensionRarityChest.get(dimension.toString()).get(rarity);//chunksSinceLastRarityChest.get(rarity);

			Treasure.LOGGER.debug("chunks per rarity {} -> {}, config chunks per chest -> {}", rarity, chunksPerRarity, chestConfig.getChunksPerChest());
			if (chunksPerRarity >= chestConfig.getChunksPerChest()) {
				Treasure.LOGGER.debug("config gen prob -> {}", chestConfig.getGenProbability());
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
					return false;
				}
				
				// 2. test if the override (global) biome is allowed
				Biome biome = world.getBiome(spawnCoords.toPos());
				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList());
				if(biomeCheck == Result.BLACK_LISTED ) {
					if (WorldInfo.isClientSide(world)) {
						Treasure.LOGGER.debug("{} is not a valid biome @ {}", biome.getRegistryName().toString(), spawnCoords.toShortString());
					}
					else {
						Treasure.LOGGER.debug("Biome {} is not valid @ {}",rarity.getValue(), spawnCoords.toShortString());
					}					
					return false;
				}
				
				// 3. check against all registered chests
				if (ITreasureFeature.isRegisteredChestWithinDistance(world, spawnCoords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest.get())) {
					Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
					return false;
				}				

				// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
				chunksSinceLastDimensionRarityChest.get(dimension.toString()).put(rarity, 0);

				// generate the chest/pit/chambers
				Treasure.LOGGER.debug("Attempting to generate pit/chest.");
				Treasure.LOGGER.debug("rarity -> {}", rarity);
				Treasure.LOGGER.debug("randcollection -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).getClass().getSimpleName());
				Treasure.LOGGER.debug("gen -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next().getClass().getSimpleName());
				Treasure.LOGGER.debug("configmap -> {}", TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
				
				GeneratorResult<GeneratorData> result = null;
				result = generateChest(seedReader, random, spawnCoords, rarity, TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next(), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));

				if (result.isSuccess()) {
					// add to registry
					TreasureData.CHEST_REGISTRIES.get(dimension.toString()).register(spawnCoords.toShortString(), new ChestInfo(rarity, spawnCoords));
					// reset the chunk counts
					chunksSinceLastDimensionChest.put(dimension.toString(), 0);
				}				
			}

			// save world data
			TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(world);
			if (savedData != null) {
				savedData.setDirty();
			}
			return true;
		}
		return false;
	}
	
	/////////////////////////////////
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
	private GeneratorResult<GeneratorData> generateChest(IServerWorld world, Random random, ICoords coords, Rarity rarity,
			IChestGenerator chestGenerator, IChestConfig config) {

		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// result from chest generation
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);

		ICoords chestCoords = null;
		boolean isSurfaceChest = false;

		// 1. collect location data points
		ICoords surfaceCoords = coords;
//		Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}

		// 2. determine if above ground or below ground
		if (config.isSurfaceAllowed() && RandomHelper.checkProbability(random, TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability.get())) {
			isSurfaceChest = true;

//			if (RandomHelper.checkProbability(random, TreasureConfig.GENERAL.surfaceStructureProbability.get())) {
//				// no markers
//				hasMarkers = false;
//
//				genResult = generateSurfaceRuins(world, random, surfaceCoords, config);
//				Treasure.LOGGER.debug("surface result -> {}", genResult.toString());
//				if (!genResult.isSuccess()) {
//					return result.fail();
//				}
//				// set the chest coords to the surface pos
//				chestCoords = genResult.getData().getChestContext().getCoords();
//			}
//			else {
//				// set the chest coords to the surface pos
//				chestCoords = new Coords(markerCoords);
//				Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);
//			}
			chestCoords = new Coords(surfaceCoords);
		}
		else if (config.isSubterraneanAllowed()) {
			Treasure.LOGGER.debug("else generate pit");
			genResult = generatePit(world, random, rarity, surfaceCoords, config);
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

		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, rarity, genResult.getData().getChestContext().getState());
		if (!chestResult.isSuccess()) {
			return result.fail();
		}
		
		// add markers (above chest or shaft)
		chestGenerator.addMarkers(world, random, surfaceCoords, isSurfaceChest);
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, surfaceCoords.toShortString());
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
	public GeneratorResult<ChestGeneratorData> generatePit(IServerWorld world, Random random, Rarity chestRarity, ICoords markerCoords, IChestConfig config) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);
		GeneratorResult<ChestGeneratorData> pitResult = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);

		// check if it has 50% land
		if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
			Treasure.LOGGER.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
			return result.fail();
		}

		// determine spawn coords below ground
		ICoords spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinDepth(), config.getMaxDepth());

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
		pitResult = pitGenerator.generate((IServerWorld)world, random, markerCoords, spawnCoords);

		if (!pitResult.isSuccess()) return result.fail();

		result.setData(pitResult.getData());
		Treasure.LOGGER.debug("Is pit generated: {}", pitResult.isSuccess());
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
//	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(IServerWorld world, Random random, ICoords spawnCoords,
//			IChestConfig config) {
//		return generateSurfaceRuins(world, random, spawnCoords, null, null, config);
//	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param decayProcessor
	 * @param config
	 * @return
	 */
//	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(IServerWorld world, Random random, ICoords spawnCoords,
//			TemplateHolder holder, IDecayRuleSet decayRuleSet, IChestConfig config) {
//
//		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
//		result.getData().setSpawnCoords(spawnCoords);
//
//		SurfaceRuinGenerator generator = new SurfaceRuinGenerator();
//
//		// build the structure
//		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, random, spawnCoords, holder, decayRuleSet);
//		Treasure.LOGGER.debug("surface struct result -> {}", genResult);
//		if (!genResult.isSuccess()) return result.fail();
//
//		result.setData(genResult.getData());
//		return result.success();
//	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param pos
	 * @param minDepth
	 * @param maxDepth
	 * @return
	 */
	public static ICoords getUndergroundSpawnPos(IServerWorld world, Random random, ICoords pos, int minDepth, int maxDepth) {
		ICoords spawnPos = null;

		int depth = RandomHelper.randomInt(minDepth, maxDepth);
		int ySpawn = Math.max(UNDERGROUND_OFFSET, pos.getY() - depth);
		Treasure.LOGGER.debug("ySpawn -> {}", ySpawn);
		spawnPos = new Coords(pos.getX(), ySpawn, pos.getZ());
		// get floor pos (if in a cavern or tunnel etc)
		spawnPos = WorldInfo.getDryLandSurfaceCoords(world, spawnPos);

		return spawnPos;
	}
	
	/**
	 * Land Only
	 * @param random
	 * @return
	 */
	public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
//		PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.PITS.pitStructureProbability.get()) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
		PitTypes pitType = PitTypes.STANDARD;
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

	@Override
	public Map<String, Integer> getChunksSinceLastDimensionFeature() {
		return this.chunksSinceLastDimensionChest;
	}

	@Override
	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature() {
		return this.chunksSinceLastDimensionRarityChest;
	}
}