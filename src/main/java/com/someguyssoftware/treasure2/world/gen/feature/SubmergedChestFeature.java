/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestEnvironment;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.chest.ChestInfo.GenType;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.ruins.SubmergedRuinGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.registry.RegistryType;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Jan 27, 2021
 *
 */
public class SubmergedChestFeature extends Feature<NoFeatureConfig> implements IChestFeature {

	private int waitChunksCount = 0;
	
	/**
	 * 
	 * @param configFactory
	 */
	public SubmergedChestFeature(Codec<NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name - NECESAARY??
		this.setRegistryName(Treasure.MODID, "submerged_chest");
	}

	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		ServerWorld world = seedReader.getLevel();
		ResourceLocation dimensionName = WorldInfo.getDimension(world);

		// test the dimension white list
		if (!meetsDimensionCriteria(dimensionName)) { 
			return false;
		}
		ChestRegistry registry = TreasureData.CHEST_REGISTRIES2.get(dimensionName.toString()).get(RegistryType.SUBMERGED);
		
		if (!meetsWorldAgeCriteria(world, registry)) {
			return false;
		}
		
		ICoords spawnCoords = WorldInfo.getOceanFloorSurfaceCoords(world, generator,
				new Coords(pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == WorldInfo.EMPTY_COORDS) {
			return false;
		}
//		Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());

		// determine what type to generate
		Rarity rarity = (Rarity) TreasureData.RARITIES_MAP.get(WorldGenerators.SUBMERGED_CHEST).next();
//		Treasure.LOGGER.debug("rarity -> {}", rarity);
		IChestConfig chestConfig = TreasureConfig.CHESTS.submergedChests.configMap.get(rarity);
		if (chestConfig == null) {
			Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
			return false;
		}
//		Treasure.LOGGER.debug("config for rarity -> {} = {}", rarity, chestConfig);

		// 2. test if the override (global) biome is allowed
		if (!meetsBiomeCriteria(world, spawnCoords, chestConfig.getBiomeWhitelist(), chestConfig.getBiomeBlacklist())) {
			return false;
		}

		// 3. check against all registered chests
		if (!meetsProximityCriteria(world, dimensionName, RegistryType.SUBMERGED, spawnCoords)) {
			return false;
		}

		// 4. check if meets the probability criteria
		if (!meetsProbabilityCriteria(random)) {
			ChestInfo chestInfo = new ChestInfo(rarity, spawnCoords, GenType.NONE);
			registry.register(rarity, spawnCoords, chestInfo);
			return false;
		}

		// generate the chest/pit/chambers
		Treasure.LOGGER.debug("Attempting to generate submerged chest.");
		Treasure.LOGGER.debug("rarity -> {}", rarity);
		Treasure.LOGGER.debug("randcollection -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).getClass().getSimpleName());
		Treasure.LOGGER.debug("gen -> {}", TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).next().getClass().getSimpleName());
		Treasure.LOGGER.debug("configmap -> {}", TreasureConfig.CHESTS.submergedChests.configMap.get(rarity));

		GeneratorResult<ChestGeneratorData> result = null;
		result = generateChest(seedReader, generator, random, spawnCoords, rarity, 
				TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SUBMERGED_CHEST).next(), TreasureConfig.CHESTS.submergedChests.configMap.get(rarity));

		if (result.isSuccess()) {
			// add to registry
			ChestInfo chestInfo = ChestInfo.from(result.getData());	
			registry.register(rarity, spawnCoords, chestInfo);
			// update the adjusted weight collection
			TreasureData.RARITIES_MAP.put(WorldGenerators.SUBMERGED_CHEST, 
					TreasureData.RARITIES_MAP.get(WorldGenerators.SUBMERGED_CHEST).adjustExcept(1, rarity));
//			List<String> rarityMapDump = TreasureData.RARITIES_MAP.get(WorldGenerators.SUBMERGED_CHEST).dump();
//			rarityMapDump.forEach(s -> {
//				Treasure.LOGGER.info(s);
//			});
		}				

		// save world data
		TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(world);
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean meetsProximityCriteria(ServerWorld world, ResourceLocation dimension, RegistryType key, ICoords spawnCoords) {
		if (IChestFeature.isRegisteredChestWithinDistance(world, dimension, key, spawnCoords, TreasureConfig.CHESTS.submergedChestGen.minBlockDistance.get())) {
			Treasure.LOGGER.trace("The distance to the nearest treasure chest is less than the minimun required.");
			return false;
		}	
		return true;
	}
	
	/**
	 * 
	 * @param world
	 * @param registry
	 * @return
	 */
	private boolean meetsWorldAgeCriteria(ServerWorld world, ChestRegistry registry) {
		// wait count check		
		if (registry.getValues().isEmpty() && waitChunksCount < TreasureConfig.CHESTS.submergedChestGen.waitChunks.get()) {
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
	private boolean meetsProbabilityCriteria(Random random) {
		if (!RandomHelper.checkProbability(random, TreasureConfig.CHESTS.submergedChestGen.probability.get())) {
			Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
			return false;
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
	private GeneratorResult<ChestGeneratorData> generateChest(IServerWorld world, ChunkGenerator chunkGenerator, Random random, ICoords coords, Rarity rarity,
			IChestGenerator chestGenerator, IChestConfig config) {

		// result to return to the caller
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setEnvironment(ChestEnvironment.SUBMERGED);
		// result from chest generation
		GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);

		ICoords chestCoords = null;
		ICoords markerCoords = null;

		// 1. collect location data points
		ICoords surfaceCoords = coords;
		Treasure.LOGGER.debug("ocean floor surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return result.fail();
		}
		// TEMP - if building a structure, markerCoords could be different than original surface coords because for rotation etc.
		markerCoords = surfaceCoords;

		genResult = generateSubmergedRuins(world, chunkGenerator, random, surfaceCoords, config);
		Treasure.LOGGER.debug("submerged result -> {}", genResult.toString());
		if (!genResult.isSuccess() || genResult.getData().getChestContext() == null) {
			return result.fail();
		}
		// update generation meta data
		result.getData().setStructure(true);

		// set the chest coords to the surface pos
		chestCoords = genResult.getData().getChestContext().getCoords();

		// if chest isn't generated, then fail
		if (chestCoords == null) return result.fail();
		markerCoords = genResult.getData().getSpawnCoords();
		if (markerCoords == null) {
			markerCoords = surfaceCoords;
		}
		Treasure.LOGGER.debug("submerged spawn coords -> {}", markerCoords.toShortString());

		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(world, random, chestCoords, rarity, genResult.getData().getChestContext().getState());
		if (!chestResult.isSuccess()) {
			return result.fail();
		}

		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, markerCoords.toShortString());
		result.getData().setChestContext(chestResult.getData().getChestContext());
		result.getData().setRegistryName(chestResult.getData().getRegistryName());
		result.getData().setRarity(rarity);
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
	public GeneratorResult<ChestGeneratorData> generateSubmergedRuins(IServerWorld world, ChunkGenerator chunkGenerator, Random random, ICoords spawnCoords,
			IChestConfig config) {

		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
		result.getData().setSpawnCoords(spawnCoords);

		SubmergedRuinGenerator generator = new SubmergedRuinGenerator();

		// build the structure
		GeneratorResult<ChestGeneratorData> genResult = generator.generate(world, chunkGenerator, random, spawnCoords);
		Treasure.LOGGER.debug("submerged struct result -> {}", genResult);
		if (!genResult.isSuccess()) {
			return result.fail();
		}

		result.setData(genResult.getData());
		return result.success();
	}
}