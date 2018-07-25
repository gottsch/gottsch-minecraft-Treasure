/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.config.IWellConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.generator.well.CanopyWellGenerator;
import com.someguyssoftware.treasure2.generator.well.IWellGenerator;
import com.someguyssoftware.treasure2.generator.well.SimpleWellGenerator;
import com.someguyssoftware.treasure2.generator.well.WoodDrawWellGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public class WellWorldGenerator implements IWorldGenerator {
	// the number of blocks of half a chunk (radius) (a chunk is 16x16)
	public static final int CHUNK_RADIUS = 8;

	private int chunksSinceLastWell;

	// the well geneators
	private Map<Wells, IWellGenerator> generators = new HashMap<>();

	/**
	 * 
	 */
	public WellWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate ChestGenerator:", e);
		}
	}

	private void init() {
		// intialize chunks since last array
		chunksSinceLastWell = 0;

		// setup the well generators
		generators.put(Wells.WISHING_WELL, new SimpleWellGenerator());
		generators.put(Wells.CANOPY_WISHING_WELL, new CanopyWellGenerator());
		generators.put(Wells.WOOD_DRAW_WISHING_WELL,  new WoodDrawWellGenerator());
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimension()){
		case 0:
			generateSurface(world, random, chunkX, chunkZ);
			break;
		default:
			break;
		}
	}		


	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	private void generateSurface(World world, Random random, int chunkX, int chunkZ) {

		// increment the chunk counts
		chunksSinceLastWell++;

		boolean isGenerated = false;	

		// test if min chunks was met
		if (chunksSinceLastWell > TreasureConfig.minChunksPerWell) {
//			Treasure.logger.debug(String.format("Gen: pass first test: chunksSinceLast: %d, minChunks: %d", chunksSinceLastWell, TreasureConfig.minChunksPerWell));

			/*
			 * get current chunk position
			 */            
			// spawn @ middle of chunk
			int xSpawn = chunkX * 16 + 8;
			int zSpawn = chunkZ * 16 + 8;

			// get first surface y (could be leaves, trunk, water, etc)
			int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(8, 8);
			ICoords coords = new Coords(xSpawn, ySpawn, zSpawn);

			// determine what type to generate
			Wells well = Wells.values()[random.nextInt(Wells.values().length)];
//			Treasure.logger.debug("Using Well: {}", well);
			IWellConfig wellConfig = Configs.wellConfigs.get(well);
			if (wellConfig == null) {
				Treasure.logger.warn("Unable to locate a config for well {}.", well);
				return;
			}


			if (chunksSinceLastWell >= wellConfig.getChunksPerWell()) {

				// 1. test if correct biome
				// if not the correct biome, reset the count
				Biome biome = world.getBiome(coords.toPos());

				if (!BiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList())) {
					Treasure.logger.debug(String.format("[%s] is not a valid biome.", biome.getBiomeName()));
					chunksSinceLastWell = 0;
					return;
				}
				
				// 2. test if well meets the probability criteria
				Treasure.logger.debug("{} well probability: {}", well, wellConfig.getGenProbability());
				if (!RandomHelper.checkProbability(random, wellConfig.getGenProbability())) {
					Treasure.logger.debug("Well does not meet generate probability.");
					return;
				}
				else {
					Treasure.logger.debug("Well MEETS generate probability!");
				}


				// 			// 3. check against all registered chests
				// 			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.minDistancePerChest)) {
				//				Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
				// 				return;
				// 			}

				// increment chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
				chunksSinceLastWell++;    	    	

				// generate the well
				Treasure.logger.debug("Attempting to generate a well");
				isGenerated = generators.get(well).generate(world, random, coords, wellConfig); 

				if (isGenerated) {
					// add to registry
					//				ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(rarity, coords));
					chunksSinceLastWell = 0;
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
		List<ChestInfo> infos = ChestRegistry.getInstance().getEntries();

		if (infos == null || infos.size() == 0) {
			Treasure.logger.debug("Unable to locate the Chest Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			//		    Dungeons2.log.debug("Dungeon dist^2: " + distance);
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the chunksSinceLastWell
	 */
	public int getChunksSinceLastWell() {
		return chunksSinceLastWell;
	}

	/**
	 * @param chunksSinceLastWell the chunksSinceLastWell to set
	 */
	public void setChunksSinceLastWell(int chunksSinceLastWell) {
		this.chunksSinceLastWell = chunksSinceLastWell;
	}

	/**
	 * @return the generators
	 */
	public Map<Wells, IWellGenerator> getGenerators() {
		return generators;
	}

	/**
	 * @param generators the generators to set
	 */
	public void setGenerators(Map<Wells, IWellGenerator> generators) {
		this.generators = generators;
	}
}
