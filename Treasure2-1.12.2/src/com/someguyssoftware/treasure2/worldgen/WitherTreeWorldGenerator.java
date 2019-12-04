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
import com.someguyssoftware.treasure2.config.IWitherTreeConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.wither.WitherTreeGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public class WitherTreeWorldGenerator implements IWorldGenerator {
	private int chunksSinceLastTree;
	private WitherTreeGenerator generator;
	/**
	 * 
	 */
	public WitherTreeWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate ChestGenerator:", e);
		}
	}

	/**
	 * 
	 */
	private void init() {
		// intialize chunks since last array
		chunksSinceLastTree = 0;

		// setup the generator
		generator = new WitherTreeGenerator();
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
		chunksSinceLastTree++;

		boolean isGenerated = false;	

		// test if min chunks was met
		if (chunksSinceLastTree > TreasureConfig.WITHER_TREE.chunksPerTree) {

			/*
			 * get current chunk position
			 */            
			// spawn @ middle of chunk
			int xSpawn = chunkX * 16 + WorldInfo.CHUNK_RADIUS;
			int zSpawn = chunkZ * 16 + WorldInfo.CHUNK_RADIUS;

			// get first surface y (could be leaves, trunk, water, etc)
			int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(8, 8);
			ICoords coords = new Coords(xSpawn, ySpawn, zSpawn);

			// determine what type to generate
//			IWitherTreeConfig treeConfig = Configs.witherTreeConfig;
			IWitherTreeConfig treeConfig = TreasureConfig.WITHER_TREE;
			if (treeConfig == null) {
				Treasure.logger.warn("Unable to locate a config for wither tree {}.", treeConfig);
				return;
			}

			if (chunksSinceLastTree >= treeConfig.getChunksPerTree()) {
//				Treasure.logger.debug(String.format("Gen: pass second test: chunksSinceLast: %d, chunksPerTree: %d", chunksSinceLastTree, treeConfig.getChunksPerTree()));

				// 1. test if correct biome
				// if not the correct biome, reset the count
				Biome biome = world.getBiome(coords.toPos());
				TreasureBiomeHelper.Result result =TreasureBiomeHelper.isBiomeAllowed(biome, treeConfig.getBiomeWhiteList(), treeConfig.getBiomeBlackList());
				if(result == Result.BLACK_LISTED ) {
					chunksSinceLastTree = 0;
					return;
				}
				else if (result == Result.OK) {
					if (!BiomeHelper.isBiomeAllowed(biome, treeConfig.getBiomeTypeWhiteList(), treeConfig.getBiomeTypeBlackList())) {
						if (Treasure.logger.isDebugEnabled()) {
				    		if (WorldInfo.isClientSide(world)) {
				    			Treasure.logger.debug("{} is not a valid biome @ {} for Wither Tree", biome.getBiomeName(), coords.toShortString());
				    		}
				    		else {
				    			Treasure.logger.debug("Biome is not valid @ {} for Wither Tree", coords.toShortString());
				    		}
						}
						chunksSinceLastTree = 0;
						return;
					}
				}
				
				// 2. test if well meets the probability criteria
//				Treasure.logger.debug("wither tree probability: {}", treeConfig.getGenProbability());
				if (!RandomHelper.checkProbability(random, treeConfig.getGenProbability())) {
					Treasure.logger.debug("Wither does not meet generate probability.");
					return;
				}
//				else {
//					Treasure.logger.debug("Wither Tree MEETS generate probability!");
//				}

	 			// 3. check against all registered chests
	 			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest)) {
					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
	 				return;
	 			}

				// increment chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
				chunksSinceLastTree = 0;   	    	

				// generate the well
				Treasure.logger.debug("Attempting to generate a wither tree");
				isGenerated = generator.generate(world, random, coords, treeConfig); 

				if (isGenerated) {
					// add to registry
					ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(Rarity.SCARCE, coords));
//					chunksSinceLastTree = 0;
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

		Treasure.logger.debug("Min distance Sq -> {}", minDistanceSq);
		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			Treasure.logger.debug("ChestConfig dist^2: " + distance);
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the chunksSinceLastTree
	 */
	public int getChunksSinceLastTree() {
		return chunksSinceLastTree;
	}

	/**
	 * 
	 * @param chunksSinceLastTree
	 */
	public void setChunksSinceLastTree(int chunksSinceLastTree) {
		this.chunksSinceLastTree = chunksSinceLastTree;
	}
}
