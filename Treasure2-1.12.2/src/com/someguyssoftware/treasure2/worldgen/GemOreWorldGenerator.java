/**
* 
*/
package com.someguyssoftware.treasure2.worldgen;

import java.util.Random;

import com.google.common.base.Predicate;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.ModConfig;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
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
		sapphireGenerator = new WorldGenMinable(TreasureBlocks.SAPPHIRE_ORE.getDefaultState(), ModConfig.GEMS_ORES.sapphireOreVeinSize);
		rubyGenerator = new WorldGenMinable(TreasureBlocks.RUBY_ORE.getDefaultState(), ModConfig.GEMS_ORES.rubyOreVeinSize);
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

		// Treasure.logger.debug("chunks since last ore -> {}, minChunks -> {}",
		// chunksSinceLastOre, TreasureConfig.minChunksPerGemOre);
//		if (chunksSinceLastOre >= 1) {
			int flip = RandomHelper.randomInt(0, 1);
			// Treasure.logger.debug("flip -> {}", flip);

			double prob = 0d;
			int veinsPerChunk = 0;
			int maxY, minY = 0;
			WorldGenMinable gen = null;
//			String gem = "";

			// temp
			if (flip == 0) {
				// sapphire
				prob = ModConfig.GEMS_ORES.sapphireGenProbability;
				veinsPerChunk = ModConfig.GEMS_ORES.sapphireOreVeinsPerChunk;
				maxY = ModConfig.GEMS_ORES.sapphireOreMaxY;
				minY = ModConfig.GEMS_ORES.sapphireOreMinY;
				gen = sapphireGenerator;
//				gem = "Sapphire";
			} else {
				// ruby
				prob = ModConfig.GEMS_ORES.rubyGenProbability;
				veinsPerChunk = ModConfig.GEMS_ORES.rubyOreVeinsPerChunk;
				maxY = ModConfig.GEMS_ORES.rubyOreMaxY;
				minY = ModConfig.GEMS_ORES.rubyOreMinY;
				gen = rubyGenerator;
//				gem = "Ruby";
			}

			if (!RandomHelper.checkProbability(random, prob)) {
				// Treasure.logger.debug("Gem Ore vein does not meet generate probability.");
				return;
			}

			for (int veinIndex = 0; veinIndex < veinsPerChunk; veinIndex++) {
				xSpawn = xSpawn + random.nextInt(16);
				int ySpawn = random.nextInt(maxY) + minY;
				zSpawn = zSpawn + random.nextInt(16);

				gen.generate(world, random, new BlockPos(xSpawn, ySpawn, zSpawn));
			}
			// reset count
			chunksSinceLastOre = 0;
//		}

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

	/**
	 * Re-created this class for debugging purposes.
	 * @author Mark Gottschling on Dec 10, 2018
	 *
	 */
	public class WorldGenMinable extends WorldGenerator {
		private final IBlockState oreBlock;
		/** The number of blocks to generate. */
		private final int numberOfBlocks;
		private final Predicate<IBlockState> predicate;

		public WorldGenMinable(IBlockState state, int blockCount) {
			this(state, blockCount, new GemOreWorldGenerator.StonePredicate());
		}

		public WorldGenMinable(IBlockState state, int blockCount, Predicate<IBlockState> p_i45631_3_) {
			this.oreBlock = state;
			this.numberOfBlocks = blockCount;
			this.predicate = p_i45631_3_;
		}

		public boolean generate(World worldIn, Random rand, BlockPos position) {
			float f = rand.nextFloat() * (float) Math.PI;
			double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
			double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
			double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
			double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
			double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
			double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

			for (int i = 0; i < this.numberOfBlocks; ++i) {
				float f1 = (float) i / (float) this.numberOfBlocks;
				double d6 = d0 + (d1 - d0) * (double) f1;
				double d7 = d4 + (d5 - d4) * (double) f1;
				double d8 = d2 + (d3 - d2) * (double) f1;
				double d9 = rand.nextDouble() * (double) this.numberOfBlocks / 16.0D;
				double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
				double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
				int j = MathHelper.floor(d6 - d10 / 2.0D);
				int k = MathHelper.floor(d7 - d11 / 2.0D);
				int l = MathHelper.floor(d8 - d10 / 2.0D);
				int i1 = MathHelper.floor(d6 + d10 / 2.0D);
				int j1 = MathHelper.floor(d7 + d11 / 2.0D);
				int k1 = MathHelper.floor(d8 + d10 / 2.0D);

				for (int l1 = j; l1 <= i1; ++l1) {
					double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

					if (d12 * d12 < 1.0D) {
						for (int i2 = k; i2 <= j1; ++i2) {
							double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

							if (d12 * d12 + d13 * d13 < 1.0D) {
								for (int j2 = l; j2 <= k1; ++j2) {
									double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

									if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
										BlockPos blockpos = new BlockPos(l1, i2, j2);

										IBlockState state = worldIn.getBlockState(blockpos);
										if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate)) {
											worldIn.setBlockState(blockpos, this.oreBlock, 2);
//											Treasure.logger.debug("CHEATER! {} @ {}", state.getBlock().getRegistryName(), new Coords(blockpos).toShortString());
										}
									}
								}
							}
						}
					}
				}
			}

			return true;
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Dec 10, 2018
	 *
	 */
	static class StonePredicate implements Predicate<IBlockState> {
		private StonePredicate() {
		}

		public boolean apply(IBlockState state) {
			if (state != null && state.getBlock() == Blocks.STONE) {
				BlockStone.EnumType blockstone$enumtype = (BlockStone.EnumType) state.getValue(BlockStone.VARIANT);
				return blockstone$enumtype.isNatural();
			} else {
				return false;
			}
		}
	}
}
