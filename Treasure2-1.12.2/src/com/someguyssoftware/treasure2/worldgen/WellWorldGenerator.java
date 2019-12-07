/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IWellConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.well.IWellGenerator;
import com.someguyssoftware.treasure2.generator.well.WellGenerator;
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
//	private Map<Wells, IWellGenerator> generators = new HashMap<>();
	private IWellGenerator<GeneratorResult<GeneratorData>> generator = new WellGenerator();

	/**
	 * 
	 */
	public WellWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate SurfaceChestGenerator:", e);
		}
	}

	private void init() {
		// intialize chunks since last array
		chunksSinceLastWell = 0;
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
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

		// test if min chunks was met
		if (chunksSinceLastWell > TreasureConfig.WELL.chunksPerWell) {
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
			IWellConfig wellConfig = TreasureConfig.WELL; //Configs.wellConfig; //Configs.wellConfigs.get(well);
			if (wellConfig == null) {
				Treasure.logger.warn("Unable to locate a config for well {}.", well);
				return;
			}

			if (chunksSinceLastWell >= wellConfig.getChunksPerWell()) {

				// 1. test if correct biome
				// if not the correct biome, reset the count
				Biome biome = world.getBiome(coords.toPos());
				// TODO this whole biome check should be wrapped in a method that returns true/false
				TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeWhiteList(), wellConfig.getBiomeBlackList());
				if(biomeCheck == Result.BLACK_LISTED ) {
					chunksSinceLastWell = 0;
					return;
				}
				else if (biomeCheck == Result.OK) {
					if (!BiomeHelper.isBiomeAllowed(biome, wellConfig.getBiomeTypeWhiteList(), wellConfig.getBiomeTypeBlackList())) {
						if (Treasure.logger.isDebugEnabled()) {
				    		if (WorldInfo.isClientSide(world)) {
				    			Treasure.logger.debug("{} is not a valid biome @ {} for Well", biome.getBiomeName(), coords.toShortString());
				    		}
				    		else {
				    			Treasure.logger.debug("Biome is not valid @ {} for Well", coords.toShortString());
				    		}
						}
						chunksSinceLastWell = 0;
						return;
					}
				}
				
				// 2. test if well meets the probability criteria
//				Treasure.logger.debug("{} well probability: {}", well, wellConfig.getGenProbability());
				if (!RandomHelper.checkProbability(random, wellConfig.getGenProbability())) {
					Treasure.logger.debug("Well does not meet generate probability.");
					return;
				}

				// increment chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
				chunksSinceLastWell++;    	    	

				// generate the well
				Treasure.logger.debug("Attempting to generate a well");
//				isGenerated = generators.get(well)
				result = generator.generate(world, random, coords, wellConfig); 
				Treasure.logger.debug("well world gen result -> {}", result.isSuccess());
				if (result.isSuccess()) {
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
		List<ChestInfo> infos = ChestRegistry.getInstance().getValues();

		if (infos == null || infos.size() == 0) {
			Treasure.logger.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
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

//	/**
//	 * @return the generators
//	 */
//	public Map<Wells, IWellGenerator> getGenerators() {
//		return generators;
//	}
//
//	/**
//	 * @param generators the generators to set
//	 */
//	public void setGenerators(Map<Wells, IWellGenerator> generators) {
//		this.generators = generators;
//	}
}
