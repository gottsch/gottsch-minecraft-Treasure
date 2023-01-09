/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.world.feature;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.PitGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.WeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext.GenType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * NOTE: Feature is the equivalent to 1.12 WorldGenerator
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class TerrestrialChestFeature extends Feature<NoneFeatureConfiguration> implements IChestFeature {

	/**
	 * 
	 * @param configuration
	 */
	public TerrestrialChestFeature(Codec<NoneFeatureConfiguration> configuration) {
		super(configuration);
	}

	/*
	 * The minimum depth from surface for a chest spawn
	 */
	protected static int UNDERGROUND_OFFSET = 5;

	private int waitChunksCount = 0;

	/**
	 * NOTE equivalent to 1.12 generate()
	 * NOTE only use seedReader.setblockState() and that only allows you to access the 3x3 chunk.
	 *  chest/pit spawn IS doable as long as you keep it within the 3x3 chunk area, else would have to use a Structures setup
	 */
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		ServerLevel world = context.level().getLevel();
		ResourceLocation dimension = WorldInfo.getDimension(world);
		
		// test the dimension
		if (!meetsDimensionCriteria(dimension)) { 
			return false;
		}
		
		// get the chest registry
		GeneratedRegistry<ChestGenContext> registry = DimensionalGeneratedRegistry.getChestGeneratedRegistry(dimension, GeneratorType.TERRESTRIAL);
		if (registry == null) {
			return false;
		}
		// get the generator config
		ChestConfiguration config = Config.chestConfigMap.get(dimension);
		if (config == null) {
			return false;
		}
		Generator generatorConfig = config.getGenerator(GeneratorType.TERRESTRIAL.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for generator type -> {}.", GeneratorType.TERRESTRIAL.getName());
			return false;
		}

		// test the world age
		if (!meetsWorldAgeCriteria(world, registry, generatorConfig)) {
			return false;
		}
		
		// the get first surface y (could be leaves, trunk, water, etc)
		ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, context.chunkGenerator(), new Coords(context.origin().offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == Coords.EMPTY) {
			return false;
		}

		// determine what type to generate
		IRarity rarity = (IRarity) WeightedChestGeneratorRegistry.getNextRarity(dimension, GeneratorType.TERRESTRIAL);
		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity == Rarity.NONE) {
			Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}", GeneratorType.TERRESTRIAL);
			return false;
		}
		Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
		if (!rarityConfig.isPresent()) {
			Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
			return false;
		}
		// test if the override (global) biome is allowed
		if (!meetsBiomeCriteria(world, spawnCoords, rarityConfig.get().getBiomeWhitelist(), rarityConfig.get().getBiomeBlacklist())) {
			return false;
		}

		// check against all registered chests
		if (!meetsProximityCriteria(world, dimension, GeneratorType.TERRESTRIAL, spawnCoords, generatorConfig.getMinBlockDistance())) {
			return false;
		}			

		// check if meets the probability criteria
		if (!meetsProbabilityCriteria(context.random(), generatorConfig)) {
			// place a placeholder chest in the registry
			ChestGenContext chestGenContext = new ChestGenContext(rarity, spawnCoords, GenType.NONE);
			registry.register(rarity, spawnCoords, chestGenContext);
			return false;
		}
		
		// generate the chest/pit/chambers
//		Treasure.LOGGER.debug("Attempting to generate pit/chest.");
//		Treasure.LOGGER.debug("rarity -> {}", rarity);
//		Treasure.LOGGER.debug("randcollection -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).getClass().getSimpleName());
//		Treasure.LOGGER.debug("gen -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next().getClass().getSimpleName());
//		Treasure.LOGGER.debug("configmap -> {}", TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));

		Optional<GeneratorResult<ChestGeneratorData>> result = generateChest(world, context.chunkGenerator(), context.random(), 
				spawnCoords, rarity, WeightedChestGeneratorRegistry.getNextGenerator(rarity, GeneratorType.TERRESTRIAL), 
				generatorConfig, rarityConfig.get());

		if (result.isPresent()) {
			// add to registry
			ChestGenContext chestGenContext = new ChestGenContext(
					result.get().getData().getRarity(),
					result.get().getData().getSpawnCoords());	
			chestGenContext.setName(result.get().getData().getRegistryName());
			chestGenContext.setPlacement(RegionPlacement.SURFACE);
			registry.register(rarity, spawnCoords, chestGenContext);

			// update the adjusted weight collection
			
			WeightedChestGeneratorRegistry.adjustAllWeightsExcept(dimension, GeneratorType.TERRESTRIAL, 1, rarity);
//			List<String> rarityMapDump = TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).dump();
//			rarityMapDump.forEach(s -> {
//				Treasure.LOGGER.info(s);
//			});
		}

		// save world data
		TreasureSavedData savedData = TreasureSavedData.get(world);
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;

	}

	/**
	 * TODO move the retrieving of the chest config out and pass it in - can remove dimesion too then
	 * @param world
	 * @param registry
	 * @return
	 */
	private boolean meetsWorldAgeCriteria(ServerLevel world, GeneratedRegistry<ChestGenContext> registry, Generator generatorConfig) {
		// wait count check		
		if (registry.getValues().isEmpty() && waitChunksCount < generatorConfig.getWaitChunks()) {
			Treasure.LOGGER.debug("World is too young");
			this.waitChunksCount++;
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param random
	 * @return
	 */
	private boolean meetsProbabilityCriteria(Random random, Generator generatorConfig) {
		if (!RandomHelper.checkProbability(random, generatorConfig.getProbability())) {
			Treasure.LOGGER.debug("chest gen does not meet generate probability.");
			return false;
		}
		return true;
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
	private Optional<GeneratorResult<ChestGeneratorData>> generateChest(ServerLevel world, ChunkGenerator chunkGenerator,
			Random random, ICoords coords, IRarity rarity, IChestGenerator chestGenerator,
			ChestConfiguration.Generator generatorConfig,
			ChestConfiguration.ChestRarity config) {
//	private GeneratorResult<ChestGeneratorData> generateChest(IServerWorld world, ChunkGenerator generator, Random random, ICoords coords, Rarity rarity,
//			IChestGenerator chestGenerator, IChestConfig config) {

		// TODO don't like this - creating results - should use Optional<>
		// result to return to the caller
		GeneratorResult<ChestGeneratorData> generationResult = new GeneratorResult<>(ChestGeneratorData.class);
		// result from environment (pit | ruins) generation
		Optional<GeneratorResult<ChestGeneratorData>> environmentGenerationResult = Optional.empty(); //new GeneratorResult<>(ChestGeneratorData.class);

		ICoords chestCoords = null;
		boolean isSurfaceChest = false;
		boolean isStructure = false;

		// 1. collect location data points
		ICoords surfaceCoords = coords;
		//		Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isHeightValid(surfaceCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", surfaceCoords.toShortString());
			return Optional.empty();
		}

		// 2. determine if above ground or below ground
		if (RandomHelper.checkProbability(random, generatorConfig.getSurfaceProbability())) { // TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability.get()) {) {
			isSurfaceChest = true;

			if (RandomHelper.checkProbability(random, generatorConfig.getStructureProbability())) {
				isStructure = true;

//				environmentGenerationResult = generateSurfaceRuins(world, generator, random, surfaceCoords, config);
//				Treasure.LOGGER.debug("surface result -> {}", environmentGenerationResult.toString());
//				if (!environmentGenerationResult.isSuccess()) {
//					return environmentGenerationResult.fail();
//				}
//				// update generation meta data
//				generationResult.getData().setStructure(true);
//
//				// set the chest coords to the surface pos
//				chestCoords = environmentGenerationResult.getData().getChestContext().getCoords();
			}
			else {
				// set the chest coords to the same as the surface pos
				chestCoords = new Coords(surfaceCoords);
				Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);
			}
			generationResult.getData().setPlacement(RegionPlacement.SURFACE);
		}
		else {
			Treasure.LOGGER.debug("else generate pit");
			environmentGenerationResult = generatePit(world, random, rarity, surfaceCoords, config);
			if (environmentGenerationResult.isEmpty()) {
				return Optional.empty();
			}
			Treasure.LOGGER.debug("result -> {}",environmentGenerationResult.get().toString());
			chestCoords = environmentGenerationResult.get().getData().getCoords();
			generationResult.getData().setPlacement(RegionPlacement.SUBTERRANEAN);
			generationResult.getData().setPit(true);
		}

		// if chest isn't generated, then fail
		if (chestCoords == null) {
			Treasure.LOGGER.debug("chest coords were not provided in result -> {}", environmentGenerationResult.toString());
			return Optional.empty();
		}

		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, rarity, environmentGenerationResult.get().getData().getState());
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}

		// add markers (above chest or shaft)
		if (!isStructure) {
			chestGenerator.addMarkers(world, chunkGenerator, random, surfaceCoords, isSurfaceChest);
			generationResult.getData().setMarkers(true);
		}

		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, surfaceCoords.toShortString());
//		generationResult.getData().setChestContext(chestResult.getData().getChestContext());
		generationResult.getData().setRegistryName(chestResult.getData().getRegistryName());
		generationResult.getData().setRarity(rarity);
		return Optional.ofNullable(generationResult);
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
	public Optional<GeneratorResult<ChestGeneratorData>> generatePit(ServerLevel world, Random random, IRarity chestRarity, ICoords markerCoords, ChestConfiguration.ChestRarity config) {

		// check if it has 50% land
		if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
			Treasure.LOGGER.debug("coords -> {} does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
			return Optional.empty();
		}

		// determine spawn coords below ground
		Optional<ICoords> spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinDepth(), config.getMaxDepth());

		if (spawnCoords.isEmpty()) {
			Treasure.LOGGER.debug("unable to spawn underground @ {}", markerCoords);
			return Optional.empty();
		}
		Treasure.LOGGER.debug("below ground -> {}", spawnCoords.get().toShortString());


		// select a pit generator
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(random);
		Treasure.LOGGER.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());

		// 3. build the pit
		Optional<GeneratorResult<ChestGeneratorData>> pitResult = pitGenerator.generate(world, random, markerCoords, spawnCoords.get());

		if (pitResult.isEmpty()) {
			return Optional.empty();
		}

		// override the spawn coords to that of the marker
		pitResult.get().getData().setSpawnCoords(markerCoords);
		
		return pitResult;
	}

//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param spawnCoords
//	 * @param config
//	 * @return
//	 */
//	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(IServerWorld world, ChunkGenerator generator, Random random, ICoords spawnCoords,
//			IChestConfig config) {
//		return generateSurfaceRuins(world, generator, random, spawnCoords, null, null, config);
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param spawnCoords
//	 * @param decayProcessor
//	 * @param config
//	 * @return
//	 */
//	public GeneratorResult<ChestGeneratorData> generateSurfaceRuins(IServerWorld world, ChunkGenerator chunkGenerator, Random random, ICoords spawnCoords,
//			TemplateHolder holder, IDecayRuleSet decayRuleSet, IChestConfig config) {
//
//		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
//		result.getData().setSpawnCoords(spawnCoords);
//
//		SurfaceRuinGenerator generator = new SurfaceRuinGenerator();
//
//		// build the structure
//		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, chunkGenerator, random, spawnCoords, holder, decayRuleSet);
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
	 * @param startingCoords
	 * @param minDepth
	 * @param maxDepth
	 * @return
	 */
	public static Optional<ICoords> getUndergroundSpawnPos(ServerLevel world, Random random, ICoords startingCoords, int minDepth, int maxDepth) {
		int depth = RandomHelper.randomInt(minDepth, maxDepth);
		int ySpawn = Math.max(UNDERGROUND_OFFSET, startingCoords.getY() - depth);
		Treasure.LOGGER.debug("ySpawn -> {}", ySpawn);
		ICoords coords = new Coords(startingCoords.getX(), ySpawn, startingCoords.getZ());
		// get floor pos (if in a cavern or tunnel etc)
		coords = WorldInfo.getSubterraneanSurfaceCoords(world, coords);

		return (coords == null || coords == Coords.EMPTY) ? Optional.empty() : Optional.of(coords);
	}

	/**
	 * Land Only
	 * @param random
	 * @return
	 */
	public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
		PitType pitType = RandomHelper.checkProbability(random, Config.SERVER.pits.structureProbability.get()) ? PitType.STRUCTURE : PitType.STANDARD;
		Treasure.LOGGER.debug("using pit type -> {}", pitType);
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = PitGeneratorRegistry.get(pitType);
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
}