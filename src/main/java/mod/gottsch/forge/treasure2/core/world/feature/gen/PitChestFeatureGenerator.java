/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.IMarkerType;
import mod.gottsch.forge.treasure2.core.enums.MarkerType;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.IMarkerGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.registry.MarkerGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.PitGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling May 12, 2023
 *
 */
public class PitChestFeatureGenerator implements IFeatureGenerator {
	/*
	 * The minimum depth from surface for a chest spawn
	 */
	protected static int UNDERGROUND_OFFSET = 5;
	
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords spawnCoords,
			IRarity rarity, ChestRarity rarityConfig) {

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}
		
		// check if it has 50% land
		if (!WorldInfo.isSolidBase(context.level(), spawnCoords, 2, 2, 50)) {
			Treasure.LOGGER.debug("coords -> {} does not meet solid base requires for {} x {}", spawnCoords.toShortString(), 2, 2);
			return Optional.empty();
		}
		
		Treasure.LOGGER.debug("generate pit");
		Optional<GeneratorResult<ChestGeneratorData>> pitGenerationResult = generatePit(context, rarity, spawnCoords, rarityConfig);
		if (pitGenerationResult.isEmpty()) {
			return Optional.empty();
		}
		Treasure.LOGGER.debug("result -> {}", pitGenerationResult.get().toString());
		ICoords chestCoords = pitGenerationResult.get().getData().getCoords();
		
		// if chest isn't generated, then fail
		if (chestCoords == null) {
			Treasure.LOGGER.debug("chest coords were not provided in result -> {}", pitGenerationResult.toString());
			return Optional.empty();
		}
		
		BlockState chestState = null;
		if (pitGenerationResult.isPresent()) {
			chestState = pitGenerationResult.get().getData().getState();
		}
		
		// get the chest generator
		IChestGenerator chestGenerator = RarityLevelWeightedChestGeneratorRegistry.getNextGenerator(rarity, FeatureType.TERRESTRIAL);
		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(context, chestCoords, rarity, chestState);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}
		
		// TODO markers really shouldn't be part of IChestGenerator, should be separate and selectable like a Chest Gen or Pit Gen.
		// add markers
//		chestGenerator.addMarkers(context, spawnCoords, false);
		
		// select a marker generator
		IMarkerGenerator<GeneratorResult<GeneratorData>> markerGenerator = selectMarkerGenerator(context.random());
		// generate marker
		markerGenerator.generate(context, spawnCoords);
		
		GeneratorResult<ChestGeneratorData> generationResult = new GeneratorResult<>(ChestGeneratorData.class);
		// TODO review the use/need of RegionPlacement
//		generationResult.getData().setPlacement(RegionPlacement.SUBTERRANEAN);
		generationResult.getData().setPit(true);
		generationResult.getData().setCoords(chestCoords);
		generationResult.getData().setSpawnCoords(spawnCoords);
		generationResult.getData().setRegistryName(chestResult.getData().getRegistryName());
		generationResult.getData().setRarity(rarity);
		generationResult.getData().setMarkers(true);
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, spawnCoords.toShortString());
		return Optional.ofNullable(generationResult);
	}

	/**
	 * 
	 * @param context
	 * @param rarity
	 * @param spawnCoords
	 * @param rarityConfig
	 * @return
	 */
	private Optional<GeneratorResult<ChestGeneratorData>> generatePit(IWorldGenContext context, IRarity rarity,
			ICoords markerCoords, ChestRarity config) {

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

	/**
	 * TODO possible update to be like the FeatureGeneratorSelector
	 * @param random
	 * @return
	 */
	public IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
		PitType pitType = RandomHelper.checkProbability(random, Config.SERVER.pits.structureProbability.get()) ? PitType.STRUCTURE : PitType.STANDARD;
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = PitGeneratorRegistry.get(pitType);
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
	
	/**
	 * 
	 * @param random
	 * @return
	 */
	public IMarkerGenerator<GeneratorResult<GeneratorData>> selectMarkerGenerator(Random random) {
		IMarkerType markerType;
		if (Config.SERVER.markers.enableMarkerStructures.get() 
				&& RandomHelper.checkProbability(random, Config.SERVER.markers.structureProbability.get())) {
						markerType = MarkerType.STRUCTURE;
		} else {
			markerType = MarkerType.STANDARD;
		}
		
		List<IMarkerGenerator<GeneratorResult<GeneratorData>>> markerGenerators = MarkerGeneratorRegistry.get(markerType);
		IMarkerGenerator<GeneratorResult<GeneratorData>> markerGenerator = markerGenerators.get(random.nextInt(markerGenerators.size()));
		Treasure.LOGGER.debug("using MarkerType -> {}, gen -> {}", markerType, markerGenerator.getClass().getSimpleName());

		return markerGenerator;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param startingCoords
	 * @param minDepth
	 * @param maxDepth
	 * @return
	 */
	public static Optional<ICoords> getUndergroundSpawnPos(ServerLevelAccessor world, Random random, ICoords startingCoords, int minDepth, int maxDepth) {
		int depth = RandomHelper.randomInt(minDepth, maxDepth);
		int ySpawn = Math.max(UNDERGROUND_OFFSET, startingCoords.getY() - depth);
		Treasure.LOGGER.debug("ySpawn -> {}", ySpawn);
		ICoords coords = new Coords(startingCoords.getX(), ySpawn, startingCoords.getZ());
		// get floor pos (if in a cavern or tunnel etc)
		coords = WorldInfo.getSubterraneanSurfaceCoords(world, coords);

		return (coords == null || coords == Coords.EMPTY) ? Optional.empty() : Optional.of(coords);
	}
}
