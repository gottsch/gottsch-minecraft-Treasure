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
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldGenContext;
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
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.*;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext.GenType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
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
	 * NOTE only use seedReader.setblockState() and that only allows you to access the 3x3 chunk area.
	 *  chest/pit spawn IS doable as long as you keep it within the 3x3 chunk area, else would have to use a Jigsaw Structures setup
	 */
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel genLevel = context.level();
		ResourceLocation dimension = WorldInfo.getDimension(genLevel.getLevel());
//		Treasure.LOGGER.debug("dimension -> {}", dimension.toString());
		// test the dimension
		if (!meetsDimensionCriteria(dimension)) { 
			return false;
		}
		
		// get the chest registry
		GeneratedRegistry<ChestGenContext> registry = DimensionalGeneratedRegistry.getChestGeneratedRegistry(dimension, FeatureType.TERRESTRIAL);
		if (registry == null) {
			Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRESTRIAL. This shouldn't be. Should be initialized.");
			return false;
		}
		// get the generator config
		ChestConfiguration config = Config.chestConfigMap.get(dimension);
		if (config == null) {
			Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
			return false;
		}

		Generator generatorConfig = config.getGenerator(FeatureType.TERRESTRIAL.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FeatureType.TERRESTRIAL.getName());
			return false;
		}

		// test the world age
		if (!meetsWorldAgeCriteria(genLevel, registry, generatorConfig)) {
			return false;
		}
		
		// TODO add a check against a tag that lists all the build on materials (dirt, stone, cobblestone etc), or a blacklist (bricks, planks, wool, etc)
		
		// the get first surface y (could be leaves, trunk, water, etc)
		ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(genLevel, context.chunkGenerator(), new Coords(context.origin().offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == Coords.EMPTY) {
			return false;
		}
		
		// determine what type to generate
		IRarity rarity = (IRarity) RarityLevelWeightedChestGeneratorRegistry.getNextRarity(dimension, FeatureType.TERRESTRIAL);
//		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity == Rarity.NONE) {
			Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}", FeatureType.TERRESTRIAL);
			return false;
		}
		Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
		if (!rarityConfig.isPresent()) {
			Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
			return false;
		}
		// test if the override (global) biome is allowed
		
		// TODO might have feature generator specific biome and proximity criteria checks. ie Wither
		if (!meetsBiomeCriteria(genLevel.getLevel(), spawnCoords, rarityConfig.get().getBiomeWhitelist(), rarityConfig.get().getBiomeBlacklist())) {
			return false;
		}

		// check against all registered chests
		if (!meetsProximityCriteria(genLevel, dimension, FeatureType.TERRESTRIAL, spawnCoords, generatorConfig.getMinBlockDistance())) {
			return false;
		}			

		// check if meets the probability criteria. this is used as a randomizer so that chests aren't predictably placed.
		if (!meetsProbabilityCriteria(context.random(), generatorConfig)) {
			// place a placeholder chest in the registry
			return failAndPlaceholdChest(genLevel, registry, rarity, spawnCoords);
		}
		
		// select the feature generator
		Optional<IFeatureGeneratorSelector> generatorSelector = FeatureGeneratorSelectorRegistry.getSelector(FeatureType.TERRESTRIAL, rarity);
		if (!generatorSelector.isPresent()) {
			Treasure.LOGGER.warn("unable to obtain a generator selector for rarity - >{}", rarity);
			return failAndPlaceholdChest(genLevel, registry, rarity, spawnCoords);
		}
		
		// select the generator
		IFeatureGenerator featureGenerator = generatorSelector.get().select();
		
		// call generate
		 Optional<GeneratorResult<ChestGeneratorData>> result = featureGenerator.generate(new WorldGenContext(context), spawnCoords, rarity, rarityConfig.get());

		if (result.isPresent()) {
			Treasure.LOGGER.debug("chest gen result -> {}", result.get());
			// add to registry
			ChestGenContext chestGenContext = new ChestGenContext(
					result.get().getData().getRarity(),
					result.get().getData().getSpawnCoords());	
			chestGenContext.setName(result.get().getData().getRegistryName());
			chestGenContext.setPlacement(RegionPlacement.SURFACE);
			Treasure.LOGGER.debug("chestGenContext -> {}", chestGenContext);
			registry.register(rarity, spawnCoords, chestGenContext);

			// update the adjusted weight collection			
			RarityLevelWeightedChestGeneratorRegistry.adjustAllWeightsExcept(dimension, FeatureType.TERRESTRIAL, 1, rarity);
			Map<IFeatureType, RarityLevelWeightedCollection> map = RarityLevelWeightedChestGeneratorRegistry.RARITY_SELECTOR.get(dimension);
			RarityLevelWeightedCollection dumpCol = map.get(FeatureType.TERRESTRIAL);
			List<String> dump = dumpCol.dump();
			Treasure.LOGGER.debug("weighted collection dump -> {}", dump);
		} else {
			return failAndPlaceholdChest(genLevel, registry, rarity, spawnCoords);
		}

		// save world data
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}

	private boolean failAndPlaceholdChest(WorldGenLevel genLevel, GeneratedRegistry<ChestGenContext> registry, IRarity rarity, ICoords spawnCoords) {
		// add placeholder
		ChestGenContext chestGenContext = new ChestGenContext(rarity, spawnCoords, GenType.NONE);
		registry.register(rarity, spawnCoords, chestGenContext);
		// need to save on fail
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return false;
	}
	
	/**
	 * @param world
	 * @param registry
	 * @return
	 */
	private boolean meetsWorldAgeCriteria(ServerLevelAccessor world, GeneratedRegistry<ChestGenContext> registry, Generator generatorConfig) {
		// wait count check		
		if (registry.getValues().isEmpty() && waitChunksCount < generatorConfig.getWaitChunks()) {
			Treasure.LOGGER.debug("world is too young");
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
		if (generatorConfig.getProbability() == null) {
			Treasure.LOGGER.warn("chest generator config -> '{}' is missing 'probability' value", generatorConfig.getKey());
			return false;
		}
		if (!RandomHelper.checkProbability(random, generatorConfig.getProbability())) {
			Treasure.LOGGER.debug("chest gen does not meet generate probability.");
			return false;
		}
		return true;
	}


	/////////////////////////////////	/////////////////////////////////	/////////////////////////////////	/////////////////////////////////
	
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
	@Deprecated
	private Optional<GeneratorResult<ChestGeneratorData>> generateChest(
			IWorldGenContext context, ICoords coords, IRarity rarity, IChestGenerator chestGenerator,
			ChestConfiguration.Generator generatorConfig, ChestConfiguration.ChestRarity config) {
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
		Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isHeightValid(surfaceCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", surfaceCoords.toShortString());
			return Optional.empty();
		}

		// 2. determine if above ground or below ground
		if (RandomHelper.checkProbability(context.random(), generatorConfig.getSurfaceProbability())) { // TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability.get()) {) {
			isSurfaceChest = true;

			if (RandomHelper.checkProbability(context.random(), generatorConfig.getStructureProbability())) {
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
				// update the environment state with a random direction
				// TODO I don't like this
				
				// set the chest coords to the same as the surface pos
				chestCoords = new Coords(surfaceCoords);
				Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);
			}
			generationResult.getData().setPlacement(RegionPlacement.SURFACE);
		}
		else {
			Treasure.LOGGER.debug("else generate pit");
			environmentGenerationResult = generatePit(context, rarity, surfaceCoords, config);
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
		
		BlockState chestState = null;
		if (environmentGenerationResult.isPresent()) {
			chestState = environmentGenerationResult.get().getData().getState();
		}

		// TODO update world
		// TODO at this point if surface check, there isn't any environment gen result except default
		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(context, chestCoords, rarity, chestState);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}

		// add markers (above chest or shaft)
		if (!isStructure) {
			// TODO update world
			chestGenerator.addMarkers(context, surfaceCoords, isSurfaceChest);
			generationResult.getData().setMarkers(true);
		}

		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, surfaceCoords.toShortString());
//		generationResult.getData().setChestContext(chestResult.getData().getChestContext());
		generationResult.getData().setCoords(chestCoords);
		generationResult.getData().setSpawnCoords(surfaceCoords);
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
	@Deprecated
	public Optional<GeneratorResult<ChestGeneratorData>> generatePit(IWorldGenContext context, IRarity chestRarity, ICoords markerCoords, ChestConfiguration.ChestRarity config) {

		// check if it has 50% land
		if (!WorldInfo.isSolidBase(context.level(), markerCoords, 2, 2, 50)) {
			Treasure.LOGGER.debug("coords -> {} does not meet solid base requires for {} x {}", markerCoords.toShortString(), 2, 2);
			return Optional.empty();
		}

		// determine spawn coords below ground
		Optional<ICoords> spawnCoords = getUndergroundSpawnPos(context.level(), context.random(), markerCoords, config.getMinDepth(), config.getMaxDepth());

		if (spawnCoords.isEmpty()) {
			Treasure.LOGGER.debug("unable to spawn underground @ {}", markerCoords);
			return Optional.empty();
		}
		Treasure.LOGGER.debug("below ground -> {}", spawnCoords.get().toShortString());


		// select a pit generator
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(context.random());
		Treasure.LOGGER.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());

		// 3. build the pit
		Optional<GeneratorResult<ChestGeneratorData>> pitResult = pitGenerator.generate(context, markerCoords, spawnCoords.get());

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
	@Deprecated
	public static Optional<ICoords> getUndergroundSpawnPos(ServerLevelAccessor world, Random random, ICoords startingCoords, int minDepth, int maxDepth) {
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
	@Deprecated
	public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
		PitType pitType = RandomHelper.checkProbability(random, Config.SERVER.pits.structureProbability.get()) ? PitType.STRUCTURE : PitType.STANDARD;
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = PitGeneratorRegistry.get(pitType);
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
}