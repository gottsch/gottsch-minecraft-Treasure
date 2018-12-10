/**
* 
*/
package com.someguyssoftware.treasure2.worldgen;

import java.util.Random;

import com.google.common.base.Predicate;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2018
 *
 */
public class GemOreWorldGenerator implements IWorldGenerator {

	private WorldGenMinable sapphireGenerator;
	private WorldGenMinable rubyGenerator;
	private int chunksSinceLastOre = 1;

	/**
	 * 
	 */
	public GemOreWorldGenerator() {
		sapphireGenerator = new WorldGenMinable(TreasureBlocks.SAPPHIRE_ORE.getDefaultState(), TreasureConfig.sapphireOreVeinSize);
		rubyGenerator = new WorldGenMinable(TreasureBlocks.RUBY_ORE.getDefaultState(), TreasureConfig.rubyOreVeinSize);
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
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
	 * @param chunkX
	 * @param chunkZ
	 */
	private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		// increment the chunk count
		chunksSinceLastOre++;

		// get spawn position @ chunk
		int xSpawn = chunkX * 16;
		int zSpawn = chunkZ * 16;

//		Treasure.logger.debug("chunks since last ore -> {}, minChunks -> {}", chunksSinceLastOre, TreasureConfig.minChunksPerGemOre);
		if (chunksSinceLastOre >= 1) {
			int flip = RandomHelper.randomInt(0, 1);
//			Treasure.logger.debug("flip -> {}", flip);

			double prob = 0d;
			int veinsPerChunk = 0;
			int maxY, minY = 0;
			WorldGenMinable gen = null;
//			String gem = "";

			if (flip == 0) {
				// sapphire
				prob = TreasureConfig.sapphireGenProbability;
				veinsPerChunk = TreasureConfig.sapphireOreVeinsPerChunk;
				maxY = TreasureConfig.sapphireOreMaxY;
				minY = TreasureConfig.sapphireOreMinY;
				gen = sapphireGenerator;
//				gem = "Sapphire";
			} else {
				// ruby
				prob = TreasureConfig.rubyGenProbability;
				veinsPerChunk = TreasureConfig.rubyOreVeinsPerChunk;
				maxY = TreasureConfig.rubyOreMaxY;
				minY = TreasureConfig.rubyOreMinY;
				gen = rubyGenerator;
//				gem = "Ruby";
			}

			if (!RandomHelper.checkProbability(random, prob)) {
//				Treasure.logger.debug("Gem Ore vein does not meet generate probability.");
				return;
			}

			for (int veinIndex = 0; veinIndex < veinsPerChunk; veinIndex++) {
				xSpawn = xSpawn + random.nextInt(16);
				int ySpawn = random.nextInt(maxY) + minY;
				zSpawn = zSpawn + random.nextInt(16);

				gen.generate(world, random, new BlockPos(xSpawn, ySpawn, zSpawn));
//				Treasure.logger.info("CHEATER! {} generated around {} {} {}", gem, xSpawn, ySpawn, zSpawn);
			}
			// reset count
			chunksSinceLastOre = 0;
		}

		// save world data
		GenDataPersistence savedData = GenDataPersistence.get(world);
		if (savedData != null) {
			savedData.markDirty();
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
	private void generateNether(World world, Random random, int i, int j) {
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	@SuppressWarnings("unused")
	private void generateEnd(World world, Random random, int i, int j) {
	}

	/**
	 * @return the chunksSinceLastOre
	 */
	public int getChunksSinceLastOre() {
		return chunksSinceLastOre;
	}

	/**
	 * @param chunksSinceLastOre
	 *            the chunksSinceLastOre to set
	 */
	public void setChunksSinceLastOre(int chunksSinceLastOre) {
		this.chunksSinceLastOre = chunksSinceLastOre;
	}

}
