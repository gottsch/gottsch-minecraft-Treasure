/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;

/**
 * NOTE: Feature is the equivalent to 1.12 WorldGenerator
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class SurfaceChestFeature extends Feature<NoFeatureConfig> {
	protected static int UNDERGROUND_OFFSET = 5;

	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;

	/**
	 * 
	 * @param configFactory
	 */
	public SurfaceChestFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "treasure_chest");

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
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);

		// setup temporary rarity-generators map
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}
	}

	/**
	 * NOTE equivalent to 1.12 generate()
	 */
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random,
			BlockPos pos, NoFeatureConfig config) {

		// test the dimension white list
		// TODO chests should have a properties for the depth range from "surface"
		if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(world.getDimension().getType().getRegistryName().toString())) {
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

		// increment the chunk counts
		chunksSinceLastChest++;

		for (Rarity rarity : TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST)) {
			Integer i = chunksSinceLastRarityChest.get(rarity);
			chunksSinceLastRarityChest.put(rarity, ++i);			
		}
		Treasure.LOGGER.debug("chunks since last chest -> {}, min chunks -> {}", chunksSinceLastChest, TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get());

		// test if min chunks was met
		if (chunksSinceLastChest > TreasureConfig.CHESTS.surfaceChests.minChunksPerChest.get()) {

			// the get first surface y (could be leaves, trunk, water, etc)
			// TODO this probably wont work
			//			int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
			int ySpawn = world.getChunk(pos).getTopBlockY(Heightmap.Type.WORLD_SURFACE, WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
			spawnCoords = spawnCoords.withY(ySpawn);
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());
			chunksSinceLastChest = 0;


			// determine what type to generate
			Rarity rarity = (Rarity) TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).get(random.nextInt(TreasureData.RARITIES_MAP.get(WorldGenerators.SURFACE_CHEST).size()));
			Treasure.LOGGER.debug("rarity -> {}", rarity);
			IChestConfig chestConfig = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			if (chestConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
				return false;
			}

			// TMEP
			int chunksPerRarity = chunksSinceLastRarityChest.get(rarity);
			Treasure.LOGGER.debug("chunks per rarity {} -> {}, config chunks per chest -> {}", rarity, chunksPerRarity, chestConfig.getChunksPerChest());
			if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
				Treasure.LOGGER.debug("config gen prob -> {}", chestConfig.getGenProbability());
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
					Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
					return false;
				}

			}
		}
		return true;
	}

}
