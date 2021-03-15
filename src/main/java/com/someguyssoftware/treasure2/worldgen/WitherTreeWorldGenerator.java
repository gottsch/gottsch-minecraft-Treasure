/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.block.FogBlock;
import com.someguyssoftware.treasure2.block.ITreasureBlock;
import com.someguyssoftware.treasure2.block.SpanishMossBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherLogSoulBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IWitherTreeConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.WitherChestGenerator;
import com.someguyssoftware.treasure2.generator.oasis.OasisInfo;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.registry.OasisRegistry;
import com.someguyssoftware.treasure2.registry.WitherTreeRegistry;
import com.someguyssoftware.treasure2.registry.WitherTreeRegistry.WitherTreeInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public class WitherTreeWorldGenerator implements ITreasureWorldGenerator {
	public static final int VERTICAL_MAX_DIFF = 3;
	private static final int CLEARING_RADIUS = 7;
	private static final int DIRT_REPLACEMENT_PROBABILITY = 90;// 75;
	private static final int DEGREES = 360;
	private static final double MIN_RADIUS = 5.0;
	private static final double MAX_RADIUS = 10.0;
	private static final int MIN_MAIN_TREE_SIZE = 9;
	private static final int MIN_TREE_SIZE = 7;
	private static final int WITHER_ROOT_PROBABILITY = 50;
	private static final int WITHER_BRANCH_PROBABILITY = 30;
	private static final int SPANISH_MOSS_PROBABILITY = 80;
	private static final int MAX_ROCKS = 5;
	private static final int MIN_ROCKS = 0;
	private static final int MIN_SCRUB = 5;
	private static final int MAX_SCRUB = 20;

	@SuppressWarnings("unchecked")
	static List<Direction>[] trunkMatrix = new ArrayList[4];
	static List<Direction> supportTrunkMatrix = new ArrayList<>();
	static List<Direction> topMatrix = new ArrayList<>();

	static {
		trunkMatrix[0] = new ArrayList<>();
		trunkMatrix[1] = new ArrayList<>();
		trunkMatrix[2] = new ArrayList<>();
		trunkMatrix[3] = new ArrayList<>();

		trunkMatrix[0].add(Direction.NORTH);
		trunkMatrix[0].add(Direction.WEST);
		trunkMatrix[1].add(Direction.NORTH);
		trunkMatrix[1].add(Direction.EAST);
		trunkMatrix[2].add(Direction.SOUTH);
		trunkMatrix[2].add(Direction.WEST);
		trunkMatrix[3].add(Direction.SOUTH);
		trunkMatrix[3].add(Direction.EAST);

		supportTrunkMatrix.add(Direction.NORTH);
		supportTrunkMatrix.add(Direction.EAST);
		supportTrunkMatrix.add(Direction.SOUTH);
		supportTrunkMatrix.add(Direction.WEST);

		topMatrix.add(Direction.EAST);
		topMatrix.add(Direction.SOUTH);
		topMatrix.add(null);
		topMatrix.add(null);
	}

	private int chunksSinceLastTree;

	/**
	 * 
	 */
	public WitherTreeWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate SurfaceChestGenerator:", e);
		}
	}

	/**
	 * 
	 */
	public void init() {
		// intialize chunks since last array
		chunksSinceLastTree = 0;
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (TreasureConfig.WORLD_GEN.getGeneralProperties().getDimensionsWhiteList()
				.contains(Integer.valueOf(world.provider.getDimension()))) {
			generate(world, random, chunkX, chunkZ);
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	private void generate(World world, Random random, int chunkX, int chunkZ) {
		/*
		 * get current chunk position
		 */
		// spawn @ middle of chunk
		int xSpawn = chunkX * 16 + WorldInfo.CHUNK_RADIUS;
		int zSpawn = chunkZ * 16 + WorldInfo.CHUNK_RADIUS;

		Integer dimensionID = Integer.valueOf(world.provider.getDimension());
		
		// 0. hard check against ocean biomes
		ICoords coords = new Coords(xSpawn, 0, zSpawn);
		Biome biome = world.getBiome(coords.toPos());
		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN
				|| BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return;
		}
		Integer biomeID = Biome.getIdForBiome(biome);
		
		// increment the chunk counts
		chunksSinceLastTree++;

		// test if min chunks was met
		if (chunksSinceLastTree > TreasureConfig.WITHER_TREE.chunksPerTree) {

			// get first surface y (could be leaves, trunk, water, etc)
			int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(8, 8);
			coords = new Coords(xSpawn, ySpawn, zSpawn);

			// determine what type to generate
			IWitherTreeConfig treeConfig = TreasureConfig.WITHER_TREE;
			if (treeConfig == null) {
				Treasure.logger.warn("Unable to locate a config for wither tree {}.", treeConfig);
				return;
			}

			if (chunksSinceLastTree >= treeConfig.getChunksPerTree()) {
				// 1. test if correct biome
				// if not the correct biome, reset the count
				TreasureBiomeHelper.Result biomeCheck = TreasureBiomeHelper.isBiomeAllowed(biome,
						treeConfig.getBiomeWhiteList(), treeConfig.getBiomeBlackList());
				if (biomeCheck == Result.BLACK_LISTED) {
					chunksSinceLastTree = 0;
					return;
				} else if (biomeCheck == Result.OK) {
					if (!BiomeHelper.isBiomeAllowed(biome, treeConfig.getBiomeTypeWhiteList(),
							treeConfig.getBiomeTypeBlackList())) {
						if (Treasure.logger.isDebugEnabled()) {
							if (WorldInfo.isClientSide(world)) {
								Treasure.logger.debug("{} is not a valid biome @ {} for Wither Tree",
										biome.getBiomeName(), coords.toShortString());
							} else {
								Treasure.logger.debug("Biome is not valid @ {} for Wither Tree",
										coords.toShortString());
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

				// 3. check against all registered chests
				if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest)) {
					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
					return;
				}
				
				// 4. check against all wither trees
				if (isRegisteredWitherTreeWithinDistance(world, coords, dimensionID, TreasureConfig.WITHER_TREE.minDistancePerWitherTree)) {
					Treasure.logger.debug("The distance to the nearest wither tree is less than the minimun required.");
					return;
				}

				// increment chunks since last tree regardless of successful generation - makes
				// more rare and realistic and configurable generation.
				chunksSinceLastTree = 0;

				// generate the well
				Treasure.logger.debug("Attempting to generate a wither tree");
				GeneratorResult<GeneratorData> result = generate(world, random, coords, treeConfig);

				if (result.isSuccess()) {
					// add to registry
					ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(Rarity.SCARCE, coords));
    				WitherTreeRegistry.getInstance().register(dimensionID, coords, biomeID);
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
	 * @param coords
	 * @param config
	 * @return
	 */
	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords,
			IWitherTreeConfig config) {
		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

		ICoords surfaceCoords = null;
		ICoords witherTreeCoords = null;

		// 1. determine y-coord of land for markers
		surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("Surface Coords @ {}", surfaceCoords.toShortString());
		if (surfaceCoords == null || surfaceCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to surface coords == null or EMPTY_COORDS");
			return result.fail();
		}
		witherTreeCoords = surfaceCoords;

		AxisAlignedBB witherGroveSize = new AxisAlignedBB(surfaceCoords.toPos());

		// ============ build the pit first ==============
		// TODO wither tree world gen should probably extend SurfaceChestWorldGen
		// TODO don't like that using static call to SurfaceChestWorldGenerator. Looks
		// like more refactoring in the future.
		// add pit
		Treasure.logger.debug("generate pit");
		GeneratorResult<ChestGeneratorData> genResult = SurfaceChestWorldGenerator.generatePit(world, random,
				Rarity.SCARCE, witherTreeCoords, TreasureConfig.CHESTS.surfaceChests.scarceChestProperties);
		Treasure.logger.debug("result -> {}", genResult.toString());
		if (!genResult.isSuccess()) {
			return result.fail();
		}

		// ========================================

		// 2. clear the area
		buildClearing(world, random, surfaceCoords);

		// 3. build the main wither tree
		buildMainTree(world, random, surfaceCoords, config);

		// update size of grove
		witherGroveSize = witherGroveSize.expand(CLEARING_RADIUS, 0, CLEARING_RADIUS);

		// 4. add the fog
//		if (TreasureConfig.WORLD_GEN.getGeneralProperties().enableWitherFog) {
//			GenUtil.addFog(world, random, surfaceCoords, fogDensity);
//		}

		// determine how many extra "withered" trees to include in the area
		int numTrees = RandomHelper.randomInt(config.getMinSupportingTrees(), config.getMaxSupportingTrees());

		for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
			// find a random location around a radius from the tree
			// ie. rand x-radius, rand z-radius = new point (+x,+z), rand degrees of
			// rotation from origin
			double xlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			double zlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			int degrees = RandomHelper.randomInt(0, DEGREES);

			ICoords c = witherTreeCoords.rotate(xlen, zlen, degrees);

			// get the yspawn
			c = WorldInfo.getDryLandSurfaceCoords(world, c.withY(WorldInfo.getHeightValue(world, c)));

			// add tree if criteria is met
			if (c != null && c != WorldInfo.EMPTY_COORDS) {
				if (c.getDistanceSq(witherTreeCoords) > 4) {
					if (world.getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHER_LOG) {
						buildClearing(world, random, c);
						buildTree(world, random, c, config);
//						if (TreasureConfig.WORLD_GEN.getGeneralProperties().enablePoisonFog) {
//							GenUtil.addFog(world, random, c, poisonFogDensity);
//						}
						// add tree clearing to the grove size
						AxisAlignedBB witherTreeClearingSize = new AxisAlignedBB(c.toPos()).expand(CLEARING_RADIUS, 0,
								CLEARING_RADIUS);
						witherGroveSize = witherGroveSize.union(witherTreeClearingSize);
					}
				}
			}
		}
		Treasure.logger.debug("size of clearing -> {}", witherGroveSize.toString());

		buildRocks(world, random, witherGroveSize);

		buildScrub(world, random, witherGroveSize);
		
		// add chest
		ICoords chestCoords = genResult.getData().getChestContext().getCoords();
		if (chestCoords == null) {
			return result.fail();
		}
		WitherChestGenerator chestGen = new WitherChestGenerator();
		GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(world, random, chestCoords, Rarity.SCARCE,
				null);
		if (!chestResult.isSuccess()) {
			return result.fail();
		}

		Treasure.logger.info("CHEATER! wither chest at coords: {}", witherTreeCoords.toShortString());
		result.setData(chestResult.getData());
		return result.success();
	}

	private void buildScrub(World world, Random random, AxisAlignedBB witherGroveSize) {
//		Treasure.logger.debug("adding scrub ...");
		int width = Math.abs((int) (witherGroveSize.maxX - witherGroveSize.minX));
		int depth = Math.abs((int) (witherGroveSize.maxZ - witherGroveSize.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveSize.minX + width * 0.5D), (int)witherGroveSize.minY, (int)(witherGroveSize.minZ + depth * 0.5D));

		for (int scrubIndex = 0; scrubIndex < RandomHelper.randomInt(MIN_SCRUB, MAX_SCRUB); scrubIndex++) {
			int xOffset = (int) (random.nextFloat() * width - (width/2));
			int zOffset = (int) (random.nextFloat() * depth - (depth/2));
			
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
//			Treasure.logger.debug("adding scrub at -> {}", surfaceCoords.toShortString());
			// check if current block is a dirt, podzol, coarse dirt or sand
			Block supportBlock = world.getBlockState(surfaceCoords.down(1).toPos()).getBlock();
			if (supportBlock == Blocks.DIRT || supportBlock == Blocks.SAND) {
				// randomize between bush and stump
				if (RandomHelper.checkProbability(random, 25)) {
					world.setBlockState(surfaceCoords.toPos(), Blocks.LOG.getDefaultState());
				}
				else {
					world.setBlockState(surfaceCoords.toPos(), Blocks.DEADBUSH.getDefaultState());
				}
			}
		}
	}

	private void buildRocks(World world, Random random, AxisAlignedBB witherGroveSize) {
//		Treasure.logger.debug("adding rocks ...");
		int width = Math.abs((int) (witherGroveSize.maxX - witherGroveSize.minX));
		int depth = Math.abs((int) (witherGroveSize.maxZ - witherGroveSize.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveSize.minX + width * 0.5D), (int)witherGroveSize.minY, (int)(witherGroveSize.minZ + depth * 0.5D));
		
		for (int rockIndex = 0; rockIndex < RandomHelper.randomInt(MIN_ROCKS, MAX_ROCKS); rockIndex++) {
			// randomize a position within the aabb
			int xOffset = (int) (random.nextFloat() * width - (width/2));
			int zOffset = (int) (random.nextFloat() * depth - (depth/2));
			
			ICoords rocksCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
			rocksCoords = rocksCoords.down(1);
//Treasure.logger.debug("adding rocks at -> {}", rocksCoords.toShortString());
			// check if current block is a tree or any treasure block
			if (world.getBlockState(rocksCoords.toPos()).getBlock() instanceof ITreasureBlock) {
				continue;
			}

			// build rock
			for (int y = 0; y < 2; y++) {
				for (int z = 0; z < 2; z++) {
					for (int x = 0; x < 2; x++) {
						if (RandomHelper.checkProbability(random, 70)) {
							ICoords spawnCoords = new Coords(rocksCoords).add(x, y, z);
							world.setBlockState(spawnCoords.toPos(), Blocks.MOSSY_COBBLESTONE.getDefaultState());
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildClearing(World world, Random random, ICoords coords) {
		ICoords buildCoords = null;

		// build clearing
		for (int xOffset = -(CLEARING_RADIUS); xOffset <= CLEARING_RADIUS; xOffset++) {
			for (int zOffset = -(CLEARING_RADIUS); zOffset <= CLEARING_RADIUS; zOffset++) {
				if (Math.abs(xOffset) + Math.abs(zOffset) <= CLEARING_RADIUS) {

					// find the first surface
					int yHeight = WorldInfo.getHeightValue(world, coords.add(xOffset, 255, zOffset));
					buildCoords = WorldInfo.getDryLandSurfaceCoords(world,
							new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));

					// additional check that it's not a tree and within 2 y-blocks of original
					if (Math.abs(buildCoords.getY() - coords.getY()) < VERTICAL_MAX_DIFF) {
						Cube cube = new Cube(world, buildCoords.down(1));
						if (cube.isLiquid()) {
							continue;
						}
						if (RandomHelper.checkProbability(random, DIRT_REPLACEMENT_PROBABILITY)) {
							if (Math.abs(xOffset) < 4 && Math.abs(zOffset) < 4 && !(Math.abs(xOffset) == 3 && Math.abs(zOffset) == 3)) {
								world.setBlockState(buildCoords.add(0, -1, 0).toPos(),
										Blocks.DIRT.getDefaultState()
										.withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
							} else {
								world.setBlockState(buildCoords.add(0, -1, 0).toPos(),
										Blocks.DIRT.getDefaultState()
										.withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
							}
						}
					}

					Cube cube = new Cube(world, buildCoords);
					ICoords climbCoords = new Coords(buildCoords);
					// remove the tree
					while (cube.equalsBlock(Blocks.LOG) || cube.equalsBlock(Blocks.LOG2)) {
						// remove log
						world.setBlockToAir(climbCoords.toPos());
						// climb upwards
						climbCoords = climbCoords.add(0, 1, 0);
						cube = new Cube(world, climbCoords);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 */
	public void buildTree(World world, Random random, ICoords coords, IWitherTreeConfig config) {
		// build a small wither tree ie one trunk

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, MIN_TREE_SIZE, config.getMaxTrunkSize());

		boolean hasLifeBeenAdded = false;
		for (int y = 0; y < maxSize; y++) {
			if (y == 0) {
				if (!hasLifeBeenAdded) {
					world.setBlockState(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG_SOUL.getDefaultState());
					hasLifeBeenAdded = true;
					continue;
				}
			}

			// add the trunk
			world.setBlockState(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG.getDefaultState());

			// add the branches/roots
			if (y == 0) {
				addRoot(world, random, coords, supportTrunkMatrix);
			} else if (y == maxSize - 1) {
				addTop(world, random, coords, y + 1, supportTrunkMatrix.get(random.nextInt(supportTrunkMatrix.size())));
			} else if (y > 3) {
				addBranch(world, random, coords, y, maxSize, supportTrunkMatrix);
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	public void buildMainTree(World world, Random random, ICoords coords, IWitherTreeConfig config) {
		// setup an array of coords
		ICoords[] trunkCoords = new Coords[4];
		trunkCoords[0] = coords;
		trunkCoords[1] = coords.add(1, 0, 0);
		trunkCoords[2] = coords.add(0, 0, 1);
		trunkCoords[3] = coords.add(1, 0, 1);

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, MIN_MAIN_TREE_SIZE, config.getMaxTrunkSize());

		// build a 2x2 trunk
		boolean hasLifeBeenAdded = false;
		for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {

			for (int y = 0; y < maxSize; y++) {
				if (trunkIndex == 2 && y == 2) { // TODO <-- select the right index and the face facing in the right
													// direction
					if (!hasLifeBeenAdded) {
						world.setBlockState(trunkCoords[trunkIndex].add(0, y, 0).toPos(),
								TreasureBlocks.WITHER_LOG_SOUL.getDefaultState()
										.withProperty(WitherLogSoulBlock.APPEARANCE, WitherLogSoulBlock.Appearance.FACE)
										.withProperty(WitherLogSoulBlock.FACING, EnumFacing.SOUTH));
						hasLifeBeenAdded = true;
						continue;
					}
				}

				// add the trunk
				world.setBlockState(trunkCoords[trunkIndex].add(0, y, 0).toPos(),
						TreasureBlocks.WITHER_LOG.getDefaultState());

				// add the decorations (branches, roots, top)
				if (y == 0) {
					addRoot(world, random, trunkCoords[trunkIndex], trunkMatrix[trunkIndex]);
				} else if (y == maxSize - 1) {
					addTop(world, random, trunkCoords[trunkIndex], y + 1, topMatrix.get(trunkIndex));
				} else if (y >= 3) {
					addBranch(world, random, trunkCoords[trunkIndex], y, maxSize, trunkMatrix[trunkIndex]);
				}
			}

			// set the new max size
			if (maxSize > 3) {
				maxSize -= RandomHelper.randomInt(random, 1, 3);
				maxSize = Math.max(3, maxSize);
			}
		}

	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param topMatrix2
	 */
	private void addTop(World world, Random random, ICoords coords, int y, Direction direction) {
		if (direction != null) {
			IBlockState state = TreasureBlocks.WITHER_BROKEN_LOG.getDefaultState().withProperty(WitherRootBlock.FACING,
					direction.toFacing());
			// add the top log to the world
			world.setBlockState(coords.add(0, y, 0).toPos(), state);
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param iCoords
	 * @param list
	 */
	private void addRoot(World world, Random random, ICoords coords, List<Direction> directions) {
		// for each direction
		for (Direction d : directions) {
			if (RandomHelper.checkProbability(random, WITHER_ROOT_PROBABILITY)) {
				// update the coords to the correct position
				ICoords c = coords.add(d, 1);
				Cube groundCube = new Cube(world, c.down(1));
				Cube replaceCube = new Cube(world, c);
				if (groundCube.isSolid() && groundCube.isTopSolid()
						&& (replaceCube.isAir() || replaceCube.isReplaceable())) {
					// rotate the branch in the right direction
					IBlockState state = TreasureBlocks.WITHER_ROOT.getDefaultState()
							.withProperty(WitherRootBlock.FACING, d.toFacing())
							.withProperty(WitherRootBlock.ACTIVATED, true);

					// add the branch to the world
					world.setBlockState(c.toPos(), state);
//					 Treasure.logger.debug("Wither Tree building root @ " +  coords.toShortString());					
				}
			}
		}
	}

	/**
	 * 
	 * @param trunkIndex
	 * @param y
	 * @param maxSize
	 * @param is
	 */
	private void addBranch(World world, Random random, ICoords trunkCoords, int y, int maxSize,
			List<Direction> directions) {
		int branchSize = 0;// (y <= (maxSize/3)) ? 3 : (y <= (maxSize * 2/3)) ? 2 : 1;
		if (y < maxSize / 3 || y > maxSize / 4)
			branchSize = 2;
		else
			branchSize = 1;

		// for each direction
		for (Direction d : directions) {
			// randomize if a branch is generated
			if (RandomHelper.checkProbability(random, WITHER_BRANCH_PROBABILITY)) {
				// for the num of branch segments
				ICoords c = trunkCoords;
				for (int segment = 0; segment < branchSize; segment++) {
					c = c.add(d, 1);
					Cube replaceCube = new Cube(world, c);

					// if there is a branch directly below, don't build
					if (world.getBlockState(c.down(1).toPos()).getBlock() instanceof WitherBranchBlock)
						break;

					// if able to place branch here
					if (replaceCube.isAir() || replaceCube.isReplaceable()) {
						// rotate the branch in the right direction
						IBlockState state = TreasureBlocks.WITHER_BRANCH.getDefaultState()
								.withProperty(WitherBranchBlock.FACING, d.toFacing());

						// add the branch to the world
						world.setBlockState(c.add(0, y, 0).toPos(), state);

						// add spanish moss
						if (RandomHelper.checkProbability(random, SPANISH_MOSS_PROBABILITY)) {
							replaceCube = new Cube(world, c.add(0, y - 1, 0));
							if (replaceCube.isAir() || replaceCube.isReplaceable()) {
								world.setBlockState(c.add(0, y - 1, 0).toPos(), TreasureBlocks.SPANISH_MOSS
										.getDefaultState().withProperty(SpanishMossBlock.ACTIVATED, true));
							}
						}
					} else {
						break;
					}
				}
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
			Treasure.logger
					.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param dimensionID
	 * @param minDistance
	 * @return
	 */
	public boolean isRegisteredWitherTreeWithinDistance(World world, ICoords coords, Integer dimensionID, int minDistance) {
		double minDistanceSq = minDistance * minDistance;
		List<WitherTreeInfo> infos = WitherTreeRegistry.getInstance().getValues(dimensionID);
		
		if (infos == null || infos.size() == 0) {
			Treasure.logger.debug("Unable to locate the Wither Tree Registry or the Registry doesn't contain any values");
			return false;
		}
		
		for (WitherTreeInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
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
