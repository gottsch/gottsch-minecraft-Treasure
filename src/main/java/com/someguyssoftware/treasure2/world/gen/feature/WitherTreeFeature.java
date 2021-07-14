/**
 * 
 */
package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper.Result;
import com.someguyssoftware.treasure2.block.ITreasureBlock;
import com.someguyssoftware.treasure2.block.SpanishMossBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;
import com.someguyssoftware.treasure2.block.WitherSoulLog;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.WitherChestGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author Mark Gottschling on Feb 2, 2021
 *
 */
public class WitherTreeFeature extends Feature<NoFeatureConfig> implements ITreasureFeature {
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
	
	private Map<String, Integer> chunksSinceLastDimensionTree = new HashMap<>();

	public WitherTreeFeature(Codec<NoFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "wither_tree");

		try {
			init();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to instantiate WitherTreeFeature:", e);
		}
	}
	
	@Override
	public void init() {
		// setup dimensional properties
		for (String dimension : TreasureConfig.GENERAL.dimensionsWhiteList.get()) {
			chunksSinceLastDimensionTree.put(dimension, 0);
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return TreasureConfig.WITHER_TREE.enableWitherTree.get();
	}
	
	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {

//		String dimensionName = world.getDimension().getType().getRegistryName().toString();
		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
	   		
		// test the dimension white list
		if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimensionName.toString())) {
			return false;
		}
		
		// spawn @ middle of chunk
		ICoords spawnCoords = new Coords(pos).add(WorldInfo.CHUNK_RADIUS, 0, WorldInfo.CHUNK_RADIUS);

		// increment the chunk counts
		incrementDimensionalTreeChunkCount(dimensionName.toString());
		
//		Treasure.LOGGER.debug("chunks since dimension {} last wither tree -> {}, min chunks -> {}", dimensionName, chunksSinceLastDimensionTree.get(dimensionName), TreasureConfig.WITHER_TREE.chunksPerTree.get());

		// test if min chunks was met
		if (chunksSinceLastDimensionTree.get(dimensionName.toString()) > TreasureConfig.WITHER_TREE.chunksPerTree.get()) {
			// the get first surface y (could be leaves, trunk, water, etc)
			int ySpawn = seedReader.getLevel().getChunk(pos).getHeight(Heightmap.Type.WORLD_SURFACE, WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
			spawnCoords = spawnCoords.withY(ySpawn);
			Treasure.LOGGER.debug("spawns coords -> {}", spawnCoords.toShortString());
			
			// 1. test if the override (global) biome is allowed
			Biome biome = seedReader.getLevel().getBiome(spawnCoords.toPos());
			TreasureBiomeHelper.Result biomeCheck =TreasureBiomeHelper.isBiomeAllowed(biome, TreasureConfig.WITHER_TREE.getBiomeWhiteList(),
					TreasureConfig.WITHER_TREE.getBiomeBlackList());
			if(biomeCheck == Result.BLACK_LISTED ) {
				if (WorldInfo.isClientSide(seedReader.getLevel())) {
					Treasure.LOGGER.debug("{} is not a valid biome @ {} for wither tree", biome.getRegistryName().toString(), spawnCoords.toShortString());
				}
				else {
					Treasure.LOGGER.debug("Biome {} is not valid @ {} for wither tree", spawnCoords.toShortString());
				}
				chunksSinceLastDimensionTree.put(dimensionName.toString(), 0);
				return false;
			}
			
			// 2. test if chest meets the probability criteria
			if (!RandomHelper.checkProbability(random, TreasureConfig.WITHER_TREE.genProbability.get())) {
				Treasure.LOGGER.debug("ChestConfig does not meet generate probability.");
				return false;
			}
			
			// 3. check against all registered chests
			if (ITreasureFeature.isRegisteredChestWithinDistance(seedReader.getLevel(), spawnCoords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest.get())) {
				Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
				return false;
			}
			
			// reset chunks since last tree regardless of successful generation - results in a more rare, realistic and configurable generation.
			chunksSinceLastDimensionTree.put(dimensionName.toString(), 0);
			
			// generate the well
			Treasure.LOGGER.debug("Attempting to generate a wither tree");
			GeneratorResult<GeneratorData> result = generate(seedReader.getLevel(), random, spawnCoords);

			if (result.isSuccess()) {
				// add to registry
				TreasureData.CHEST_REGISTRIES.get(dimensionName.toString()).register(spawnCoords.toShortString(), new ChestInfo(Rarity.SCARCE, spawnCoords));
			}	
		}
		
		// save world data
		TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(seedReader.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @return
	 */
	public GeneratorResult<GeneratorData> generate(IServerWorld world, Random random, ICoords coords) {
		// result to return to the caller
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

		ICoords surfaceCoords = null;
		ICoords witherTreeCoords = null;
		
		// 1. determine y-coord of land for markers
		surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.LOGGER.debug("surface coords @ {}", surfaceCoords.toShortString());
		if (surfaceCoords == null || surfaceCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.LOGGER.debug("returning due to surface coords == null or EMPTY_COORDS");
			return result.fail();
		}
		witherTreeCoords = surfaceCoords;

		AxisAlignedBB witherGroveBounds = new AxisAlignedBB(surfaceCoords.toPos());
		
		// ============ build the pit first ==============
		// add pit
		Treasure.LOGGER.debug("generate pit");
		GeneratorResult<ChestGeneratorData> genResult = PitProvider.generatePit(world, random,
				Rarity.SCARCE, witherTreeCoords, TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.SCARCE));
		Treasure.LOGGER.debug("result -> {}", genResult.toString());
		if (!genResult.isSuccess()) {
			return result.fail();
		}
		// ========================================
		
		// 2. clear the area
		buildClearing(world, random, surfaceCoords);
		
		// 3. build the main wither tree
		buildMainTree(world, random, surfaceCoords);
		
		// update size of grove
		witherGroveBounds = witherGroveBounds.expandTowards(CLEARING_RADIUS, 0, CLEARING_RADIUS);
			
		// determine how many extra "withered" trees to include in the area
		int numTrees = RandomHelper.randomInt(TreasureConfig.WITHER_TREE.minSupportingTrees.get(), TreasureConfig.WITHER_TREE.maxSupportingTrees.get());
		Treasure.LOGGER.debug("number of trees -> {}", numTrees);
		for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
			// find a random location around a radius from the tree
			// ie. rand x-radius, rand z-radius = new point (+x,+z), rand degrees of
			// rotation from origin
			double xlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			double zlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			int degrees = RandomHelper.randomInt(0, DEGREES);

			ICoords c = witherTreeCoords.rotate(xlen, zlen, degrees);

			// get the yspawn
			c = WorldInfo.getDryLandSurfaceCoords(world, c.withY(WorldInfo.getHeight(world, c)));

			// add tree if criteria is met
			if (c != null && c != WorldInfo.EMPTY_COORDS) {
				if (c.getDistanceSq(witherTreeCoords) > 4) {
					if (world.getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHER_LOG) {
						buildClearing(world, random, c);
						buildTree(world, random, c);

						// add tree clearing to the grove size
						AxisAlignedBB witherTreeClearingBounds = new AxisAlignedBB(c.toPos()).expandTowards(CLEARING_RADIUS, 0,
								CLEARING_RADIUS);
						witherGroveBounds = witherGroveBounds.minmax(witherTreeClearingBounds);
					}
				}
			}
		}
		Treasure.LOGGER.debug("size of clearing -> {}", witherGroveBounds.toString());
		
		buildRocks(world, random, witherGroveBounds);
		buildScrub(world, random, witherGroveBounds);
		
		// add chest
		ICoords chestCoords = genResult.getData().getChestContext().getCoords();
		if (chestCoords == null) {
			return result.fail();
		}
		WitherChestGenerator chestGen = new WitherChestGenerator();
		GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(world, random, chestCoords, Rarity.SCARCE, null);
		if (!chestResult.isSuccess()) {
			return result.fail();
		}

		Treasure.LOGGER.info("CHEATER! wither chest at coords: {}", witherTreeCoords.toShortString());
		result.setData(chestResult.getData());
		
		return result.success();
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildClearing(IServerWorld world, Random random, ICoords coords) {
		ICoords buildCoords = null;
		Treasure.LOGGER.debug("build clearing at -> {}", coords.toShortString());
		// build clearing
		for (int xOffset = -(CLEARING_RADIUS); xOffset <= CLEARING_RADIUS; xOffset++) {
			for (int zOffset = -(CLEARING_RADIUS); zOffset <= CLEARING_RADIUS; zOffset++) {
				if (Math.abs(xOffset) + Math.abs(zOffset) <= CLEARING_RADIUS) {

					// find the first surface
					int yHeight = WorldInfo.getHeight(world, coords.add(xOffset, 255, zOffset));
					buildCoords = WorldInfo.getDryLandSurfaceCoords(world,
							new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
//					Treasure.LOGGER.debug("clearing surface coords -> {}", buildCoords.toShortString());
					if (buildCoords == WorldInfo.EMPTY_COORDS) {
						continue;
					}
					// additional check that it's not a tree and within 2 y-blocks of original
					if (Math.abs(buildCoords.getY() - coords.getY()) < VERTICAL_MAX_DIFF) {
						BlockContext cube = new BlockContext(world, buildCoords.down(1));
						if (cube.isLiquid()) {
							continue;
						}
						if (RandomHelper.checkProbability(random, DIRT_REPLACEMENT_PROBABILITY)) {
							if (Math.abs(xOffset) < 4 && Math.abs(zOffset) < 4 && !(Math.abs(xOffset) == 3 && Math.abs(zOffset) == 3)) { // TODO magic numbers?!
								world.setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.PODZOL.defaultBlockState(), 3);
							} else {
								world.setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.DIRT.defaultBlockState(), 3);
							}
						}
					}
//					Treasure.LOGGER.debug("create new block context for buildCoords -> {}", buildCoords.toShortString());
					BlockContext blockContext = new BlockContext(world, buildCoords);
					ICoords climbCoords = new Coords(buildCoords);
					// remove the tree
					while (blockContext.equalsMaterial(Material.WOOD) && !(blockContext.getState().getBlock() instanceof ITreasureBlock)) {
						// remove log
						world.setBlock(climbCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
						// climb upwards
						climbCoords = climbCoords.add(0, 1, 0);
						blockContext = new BlockContext(world, climbCoords);
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
	public void buildMainTree(IWorld world, Random random, ICoords coords) {
		// setup an array of coords
		ICoords[] trunkCoords = new Coords[4];
		trunkCoords[0] = coords;
		trunkCoords[1] = coords.add(1, 0, 0);
		trunkCoords[2] = coords.add(0, 0, 1);
		trunkCoords[3] = coords.add(1, 0, 1);

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, MIN_MAIN_TREE_SIZE, TreasureConfig.WITHER_TREE.maxTrunkSize.get());

		// build a 2x2 trunk
		boolean hasLifeBeenAdded = false;
		for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {

			for (int y = 0; y < maxSize; y++) {
				if (trunkIndex == 2 && y == 2) { // TODO <-- select the right index and the face facing in the right
													// direction
					if (!hasLifeBeenAdded) {
						world.setBlock(trunkCoords[trunkIndex].add(0, y, 0).toPos(),
								TreasureBlocks.WITHER_SOUL_LOG.defaultBlockState()
										.setValue(WitherSoulLog.APPEARANCE, WitherSoulLog.Appearance.FACE)
										.setValue(WitherSoulLog.FACING, Direction.SOUTH), 3);
						hasLifeBeenAdded = true;
						continue;
					}
				}

				// add the trunk
				world.setBlock(trunkCoords[trunkIndex].add(0, y, 0).toPos(),
						TreasureBlocks.WITHER_LOG.defaultBlockState(), 3);

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
	 */
	public void buildTree(IWorld world, Random random, ICoords coords) {
		// build a small wither tree ie one trunk

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, MIN_TREE_SIZE, TreasureConfig.WITHER_TREE.maxTrunkSize.get());

		boolean hasLifeBeenAdded = false;
		for (int y = 0; y < maxSize; y++) {
			if (y == 0) {
				if (!hasLifeBeenAdded) {
					world.setBlock(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_SOUL_LOG.defaultBlockState(), 3);
					hasLifeBeenAdded = true;
					continue;
				}
			}

			// add the trunk
			world.setBlock(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG.defaultBlockState(), 3);

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
	 * @param trunkIndex
	 * @param y
	 * @param maxSize
	 * @param is
	 */
	private void addBranch(IWorld world, Random random, ICoords trunkCoords, int y, int maxSize,
			List<Direction> directions) {
		int branchSize = 0;// (y <= (maxSize/3)) ? 3 : (y <= (maxSize * 2/3)) ? 2 : 1;
		if (y < maxSize / 3 || y > maxSize / 4)
			branchSize = 2;
		else
			branchSize = 1;

		// for each direction
		for (Direction direction : directions) {
			// randomize if a branch is generated
			if (RandomHelper.checkProbability(random, WITHER_BRANCH_PROBABILITY)) {
				// for the num of branch segments
				ICoords c = trunkCoords;
				for (int segment = 0; segment < branchSize; segment++) {
					c = c.add(direction, 1);
					BlockContext replaceBlockContext = new BlockContext(world, c);

					// if there is a branch directly below, don't build
					if (world.getBlockState(c.down(1).toPos()).getBlock() instanceof WitherBranchBlock)
						break;

					// if able to place branch here
					if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
						// rotate the branch in the right direction
						BlockState state = TreasureBlocks.WITHER_BRANCH.defaultBlockState()
								.setValue(WitherBranchBlock.FACING, direction);

						// add the branch to the world
//						world.setBlockState(c.add(0, y, 0).toPos(), state, 3);
						WorldInfo.setBlock(world, c.add(0, y, 0), state);
						
						// add spanish moss
						if (RandomHelper.checkProbability(random, SPANISH_MOSS_PROBABILITY)) {
							replaceBlockContext = new BlockContext(world, c.add(0, y - 1, 0));
							if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
								world.setBlock(c.add(0, y - 1, 0).toPos(), TreasureBlocks.SPANISH_MOSS
										.defaultBlockState().setValue(SpanishMossBlock.ACTIVATED, true), 3);
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
	 * @param iCoords
	 * @param list
	 */
	private void addRoot(IWorld world, Random random, ICoords coords, List<Direction> directions) {
		// for each direction
		for (Direction direction : directions) {
			if (RandomHelper.checkProbability(random, WITHER_ROOT_PROBABILITY)) {
				// update the coords to the correct position
				ICoords newCoords = coords.add(direction, 1);
				BlockContext groundBlockContext = new BlockContext(world, newCoords.down(1));
				BlockContext replaceBlockContext = new BlockContext(world, newCoords);
				if (groundBlockContext.isSolid()
						&& (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable())) {
					// rotate the branch in the right direction
					BlockState state = TreasureBlocks.WITHER_ROOT.defaultBlockState()
							.setValue(WitherRootBlock.FACING, direction)
							.setValue(WitherRootBlock.ACTIVATED, true);

					// add the branch to the world
//					world.setBlockState(c.toPos(), state, 3);
					WorldInfo.setBlock(world, newCoords, state);
//					 Treasure.logger.debug("Wither Tree building root @ " +  coords.toShortString());					
				}
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
	private void addTop(IWorld world, Random random, ICoords coords, int y, Direction direction) {
		if (direction != null) {
			BlockState state = TreasureBlocks.WITHER_BROKEN_LOG.defaultBlockState().setValue(WitherRootBlock.FACING,
					direction);
			// add the top log to the world
//			world.setBlockState(coords.add(0, y, 0).toPos(), state, 3);
			WorldInfo.setBlock(world, coords.add(0, y, 0), state);
		}
	}
	
	private void buildScrub(IServerWorld world, Random random, AxisAlignedBB witherGroveBounds) {
		Treasure.LOGGER.debug("adding scrub ...");
		int width = Math.abs((int) (witherGroveBounds.maxX - witherGroveBounds.minX));
		int depth = Math.abs((int) (witherGroveBounds.maxZ - witherGroveBounds.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveBounds.minX + width * 0.5D), (int)witherGroveBounds.minY, (int)(witherGroveBounds.minZ + depth * 0.5D));

		for (int scrubIndex = 0; scrubIndex < RandomHelper.randomInt(MIN_SCRUB, MAX_SCRUB); scrubIndex++) {
			int xOffset = (int) (random.nextFloat() * width - (width/2));
			int zOffset = (int) (random.nextFloat() * depth - (depth/2));
			Treasure.LOGGER.debug("finding surface for scrub at -> {}", centerCoords.add(xOffset, 0, zOffset).withY(255).toShortString());
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
			Treasure.LOGGER.debug("adding scrub at -> {}", surfaceCoords.toShortString());
			if (surfaceCoords == WorldInfo.EMPTY_COORDS) {
				Treasure.LOGGER.debug("bad surfaceCoords -> {}", surfaceCoords.toShortString());
				continue;
			}
			// check if current block is a dirt, podzol, coarse dirt or sand
			Block supportBlock = world.getBlockState(surfaceCoords.down(1).toPos()).getBlock();
			if (supportBlock == Blocks.DIRT || supportBlock == Blocks.SAND) {
				// randomize between bush and stump
				if (RandomHelper.checkProbability(random, 25)) {
					world.setBlock(surfaceCoords.toPos(), Blocks.OAK_LOG.defaultBlockState(), 3);
				}
				else {
					world.setBlock(surfaceCoords.toPos(), Blocks.DEAD_BUSH.defaultBlockState(), 3);
				}
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param witherGroveSize
	 */
	private void buildRocks(IServerWorld world, Random random, AxisAlignedBB witherGroveSize) {
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
			try {
			if (rocksCoords == WorldInfo.EMPTY_COORDS || world.getBlockState(rocksCoords.toPos()).getBlock() instanceof ITreasureBlock) {
				continue;
			}
			}
			catch(Exception e) {
				Treasure.LOGGER.debug("bad rockCoords -> {}", rocksCoords.toShortString());
				continue;
			}
			// build rock
			for (int y = 0; y < 2; y++) {
				for (int z = 0; z < 2; z++) {
					for (int x = 0; x < 2; x++) {
						if (RandomHelper.checkProbability(random, 70)) {
							ICoords spawnCoords = new Coords(rocksCoords).add(x, y, z);
							world.setBlock(spawnCoords.toPos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param dimensionName
	 */
	private void incrementDimensionalTreeChunkCount(String dimensionName) {
		chunksSinceLastDimensionTree.merge(dimensionName, 1, Integer::sum);		
	}
	
	@Override
	public Map<String, Integer> getChunksSinceLastDimensionFeature() {
		return chunksSinceLastDimensionTree;
	}
	
	@Override
	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature() {
		return null;
	}
}
