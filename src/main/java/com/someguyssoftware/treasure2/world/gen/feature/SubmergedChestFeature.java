/**
 * 
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
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.ruins.SubmergedRuinGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Jan 27, 2021
 *
 */
public class SubmergedChestFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {

	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	private Map<String, Integer> chunksSinceLastDimensionChest = new HashMap<>();
	private Map<String, Map<Rarity, Integer>> chunksSinceLastDimensionRarityChest = new HashMap<>();

	/**
	 * 
	 * @param configFactory
	 */
	public SubmergedChestFeature(Codec<NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "submerged_chest");

		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate SubmergedChestFeature:", e);
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
//
//	/**
//	 * NOTE equivalent to 1.12 generate()
//	 */
	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
//		ServerWorld world = seedReader.getLevel();
//		
////		String dimensionName = world.getDimension().getType().getRegistryName().toString();
//		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
//		
//		// test the dimension white list
//		if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimensionName.toString())) {
//			return false;
//		}
//
//		// spawn @ middle of chunk
//		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS, 0, WorldInfo.CHUNK_RADIUS);
//
//		// 0. hard check for ocean biomes
//		// NOTE change - on feature register, only register with ocean biomes
//		Biome biome = world.getBiome(spawnCoords.toPos());
////		if (biome != Biomes.OCEAN && biome != Biomes.DEEP_OCEAN && biome != Biomes.FROZEN_OCEAN &&
////				!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
////			return false;
////		}
//
////		Treasure.LOGGER.debug("in biome -> {} @ {}", biome.getRegistryName(), pos);
//
//		// increment the chunk counts
//		incrementDimensionalChestChunkCount(dimensionName.toString());
//
//		for (Rarity rarity : TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST)) {
//			incrementDimensionalRarityChestChunkCount(dimensionName.toString(), rarity);
//		}
////		Treasure.LOGGER.debug("chunks since dimension {} last chest -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionChest.get(dimensionName), TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get());
//
//		// test if min chunks was met
//		if (chunksSinceLastDimensionChest.get(dimensionName.toString())/*chunksSinceLastChest*/ > TreasureConfig.CHESTS.submergedChests.minChunksPerChest.get()) {
//
//			// the get first surface y (could be leaves, trunk, water, etc)
//			int ySpawn = world.getChunk(pos).getHeight(Heightmap.Type.WORLD_SURFACE, WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
//			spawnCoords = spawnCoords.withY(ySpawn);
//			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());
//			//			chunksSinceLastChest = 0;
//			chunksSinceLastDimensionChest.put(dimensionName.toString(), 0);
//
//			// determine what type to generate
//			Rarity rarity = (Rarity) TreasureData.RARITIES_MAP.get(WorldGenerators.SUBMERGED_CHEST).get(random.nextInt(TreasureData.RARITIES_MAP.get(WorldGenerators.SUBMERGED_CHEST).size()));
//			Treasure.LOGGER.debug("rarity -> {}", rarity);
//			IChestConfig chestConfig = TreasureConfig.CHESTS.submergedChests.configMap.get(rarity);
//			if (chestConfig == null) {
//				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
//				return false;
//			}
//			Treasure.LOGGER.debug("config for rarity -> {} = {}", rarity, chestConfig);
//			// get the chunks for dimensional rarity chest
//			int chunksPerRarity = chunksSinceLastDimensionRarityChest.get(dimensionName.toString()).get(rarity);//chunksSinceLastRarityChest.get(rarity);
//
//			Treasure.LOGGER.debug("chunks per rarity {} -> {}, config chunks per chest -> {}", rarity, chunksPerRarity, chestConfig.getChunksPerChest());
//			if (chunksPerRarity >= chestConfig.getChunksPerChest()) {
//				Treasure.LOGGER.debug("config gen prob -> {}", chestConfig.getGenProbability());
//				// 1. test if chest meets the probability criteria
//				double prob = RandomHelper.randomDouble(random, 0.0, 100.0);
//				Treasure.LOGGER.debug("probability -> {}, chest config threshold -> {}", prob, chestConfig.getGenProbability());
//				if (chestConfig.getGenProbability() < prob) {
////				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
//					Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
//					return false;
//				}
//
//				// 2. test if the override (global) biome is allowed
//				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList());
//				if(biomeCheck == Result.BLACK_LISTED ) {
//					if (WorldInfo.isClientSide(world)) {
//						Treasure.LOGGER.debug("biome {} is not a valid biome @ {}", biome.getRegistryName().toString(), spawnCoords.toShortString());
//					}
//					else {
//						Treasure.LOGGER.debug("biome {} is not valid @ {}",biome.getRegistryName(), spawnCoords.toShortString());
//					}					
//					return false;
//				}
//				//				else if (biomeCheck == Result.OK) {
//				//				    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeTypeWhiteList(), chestConfig.getBiomeTypeBlackList())) {
//				//				    	if (Treasure.LOGGER.isDebugEnabled()) {
//				//				    		if (WorldInfo.isClientSide((World)world)) {
//				//				    			Treasure.LOGGER.debug("{} is not a valid biome type @ {}", biome.getDisplayName().getString(), spawnCoords.toShortString());
//				//				    		}
//				//				    		else {
//				//				    			Treasure.LOGGER.debug("Biome type of {} is not valid @ {}",rarity.getValue(), spawnCoords.toShortString());
//				//				    		}
//				//				    	}
//				//				    	return false;
//				//				    }
//				//				}
//
//				// 3. check against all registered chests
//				if (isRegisteredChestWithinDistance(world, spawnCoords, TreasureConfig.CHESTS.submergedChests.minDistancePerChest.get())) {
//					Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
//					return false;
//				}				
//
//				// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
//				chunksSinceLastDimensionRarityChest.get(dimensionName.toString()).put(rarity, 0);
//
//				// generate the chest/pit/chambers
//				Treasure.LOGGER.debug("Attempting to generate submerged chest.");
//				Treasure.LOGGER.debug("rarity -> {}", rarity);
//				Treasure.LOGGER.debug("randcollection -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).getClass().getSimpleName());
//				Treasure.LOGGER.debug("gen -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).next().getClass().getSimpleName());
//				Treasure.LOGGER.debug("configmap -> {}", TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
//				
//				GeneratorResult<GeneratorData> result = null;
//				result = generateChest(world, random, spawnCoords, rarity, TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).next(), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
//
//				if (result.isSuccess()) {
//					// add to registry
//					TreasureData.CHEST_REGISTRIES.get(dimensionName.toString()).register(spawnCoords.toShortString(), new ChestInfo(rarity, spawnCoords));
//					// reset the chunk counts
//					chunksSinceLastDimensionChest.put(dimensionName.toString(), 0);
//				}				
//			}
//
//			// save world data
//			TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(world);
//			if (savedData != null) {
//				savedData.setDirty();
//			}
//		}
		return true;
	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param coords
//	 * @param rarity
//	 * @param next
//	 * @param iChestConfig
//	 * @return
//	 */
//	private GeneratorResult<GeneratorData> generateChest(World world, Random random, ICoords coords, Rarity rarity,
//			IChestGenerator chestGenerator, IChestConfig config) {
//
//		// result to return to the caller
//		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
//		// result from chest generation
//		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);
//
//		ICoords chestCoords = null;
//		ICoords markerCoords = null;
//
//		// 1. collect location data points
//		ICoords surfaceCoords = WorldInfo.getOceanFloorSurfaceCoords(world, coords);
//		Treasure.LOGGER.debug("ocean floor surface coords -> {}", surfaceCoords.toShortString());
//		if (!WorldInfo.isValidY(surfaceCoords)) {
//			Treasure.LOGGER.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
//			return result.fail();
//		}
//		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
//		markerCoords = surfaceCoords;
//
//		genResult = generateSubmergedRuins(world, random, /*spawnCoords*/ surfaceCoords, config);
//		Treasure.LOGGER.debug("submerged result -> {}", genResult.toString());
//		if (!genResult.isSuccess() || genResult.getData().getChestContext() == null) {
//			return result.fail();
//		}
//
//		// set the chest coords to the surface pos
//		chestCoords = genResult.getData().getChestContext().getCoords();
//		
//		// if chest isn't generated, then fail
//		if (chestCoords == null) return result.fail();
//		markerCoords = genResult.getData().getSpawnCoords();
//		if (markerCoords == null) {
//			markerCoords = surfaceCoords;
//		}
//		Treasure.LOGGER.debug("submerged spawn coords -> {}", markerCoords.toShortString());
//
//		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, rarity, genResult.getData().getChestContext().getState());
//		if (!chestResult.isSuccess()) {
//			return result.fail();
//		}
//		
//		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, markerCoords.toShortString());
//		result.setData(chestResult.getData());
//		return result.success();		
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param spawnCoords
//	 * @param config
//	 * @return
//	 */
//	public GeneratorResult<ChestGeneratorData> generateSubmergedRuins(World world, Random random, ICoords spawnCoords,
//			IChestConfig config) {
//		
//		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
//		result.getData().setSpawnCoords(spawnCoords);
//
//		SubmergedRuinGenerator generator = new SubmergedRuinGenerator();
//
//		// build the structure
//		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, random, spawnCoords);
//		Treasure.LOGGER.debug("submerged struct result -> {}", genResult);
//		if (!genResult.isSuccess()) {
//			return result.fail();
//		}
//		
//		result.setData(genResult.getData());
//		return result.success();
//	}
//	
//	/**
//	 * 
//	 * @param dimensionName
//	 * @param rarity
//	 */
//	private void incrementDimensionalRarityChestChunkCount(String dimensionName, Rarity rarity) {
//		chunksSinceLastDimensionRarityChest.get(dimensionName).merge(rarity, 1, Integer::sum);
//	}
//
//	/**
//	 * 
//	 * @param dimensionName
//	 */
//	private void incrementDimensionalChestChunkCount(String dimensionName) {
//		chunksSinceLastDimensionChest.merge(dimensionName, 1, Integer::sum);		
//	}
//
//	// TODO move to interface or abstract
//	/**
//	 * 
//	 * @param world
//	 * @param pos
//	 * @param minDistance
//	 * @return
//	 */
//	public boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {
//
//		double minDistanceSq = minDistance * minDistance;
//
//		// get a list of dungeons
//		List<ChestInfo> infos = TreasureData.CHEST_REGISTRIES.get(WorldInfo.getDimension(world).toString()).getValues();
//
//		if (infos == null || infos.size() == 0) {
//			Treasure.LOGGER.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
//			return false;
//		}
//
//		for (ChestInfo info : infos) {
//			// calculate the distance to the poi
//			double distance = coords.getDistanceSq(info.getCoords());
//			if (distance < minDistanceSq) {
//				return true;
//			}
//		}
//		return false;
//	}
//
	@Override
	public Map<String, Integer> getChunksSinceLastDimensionFeature() {
		return this.chunksSinceLastDimensionChest;
	}

	@Override
	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature() {
		return this.chunksSinceLastDimensionRarityChest;
	}

}
