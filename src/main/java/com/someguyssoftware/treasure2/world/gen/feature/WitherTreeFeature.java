/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.world.gen.feature;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
import com.someguyssoftware.treasure2.block.ITreasureBlock;
import com.someguyssoftware.treasure2.block.SpanishMossBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;
import com.someguyssoftware.treasure2.block.WitherSoulLog;
import com.someguyssoftware.treasure2.chest.ChestEnvironment;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.chest.ChestInfo.GenType;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.WitherChestGenerator;
import com.someguyssoftware.treasure2.persistence.TreasureGenerationSavedData;
import com.someguyssoftware.treasure2.registry.RegistryType;
import com.someguyssoftware.treasure2.registry.SimpleListRegistry;

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
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * TODO does this remain it's own feature with its own additional distance check for other withers,
 * or turns into a provider and is called from SurfaceChestFeature
 * @author Mark Gottschling on Feb 2, 2021
 *
 */
public class WitherTreeFeature extends Feature<NoFeatureConfig> implements IChestFeature {
	public static final int VERTICAL_MAX_DIFF = 3;
	private static final int CLEARING_RADIUS = 7;
	private static final int DIRT_REPLACEMENT_PROBABILITY = 90;
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

	/*
	 * During generation a 3x3 (x-z axis) chunk area is available to alter ( = 48 blocks).
	 * From center, there is a 23/24 block radius (since even number).
	 * To be safe, the max gen radius is set to 20.
	 */
	private static final int MAX_GEN_RADIUS = 20;

	@SuppressWarnings("unchecked")
	static List<Direction>[] trunkMatrix = new ArrayList[4];
	static List<Direction> supportTrunkMatrix = new ArrayList<>();
	static List<Direction> topMatrix = new ArrayList<>();

	private int waitChunksCount = 0;
	
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

	/**
	 * 
	 * @param configFactory
	 */
	public WitherTreeFeature(Codec<NoFeatureConfig> configFactory) {
		super(configFactory);
		this.setRegistryName(Treasure.MODID, "wither_tree");
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return TreasureConfig.WITHER_TREE.enableWitherTree.get();
	}

	/**
	 * 
	 */
	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		ServerWorld world = seedReader.getLevel();
		ResourceLocation dimensionName = WorldInfo.getDimension(seedReader.getLevel());
		SimpleListRegistry<ICoords> registry = TreasureData.WITHER_TREE_REGISTRIES.get(dimensionName.toString());
		
		// test the dimension white list
		if (!meetsDimensionCriteria(dimensionName)) { 
			return false;
		}

		if (!meetsWorldAgeCriteria(world, registry)) {
			return false;
		}
		
		// spawn @ middle of chunk
		ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(seedReader, generator,
				new Coords(pos.offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.LOGGER.debug("returning due to surface coords == null or EMPTY_COORDS");
			return false;
		}

		// NOTE biome check is done during registration

		// 3A. check against all registered chests
		if (!meetsProximityCriteria(world, dimensionName, RegistryType.SURFACE, spawnCoords)) {
			return false;
		}

		// TODO add/update a wither tree registry with its own BST and not 1000 tracking. maybe 200
		// 3B. check against all registered wither trees
		if (checkWitherTreeProximity(seedReader, spawnCoords, TreasureConfig.WITHER_TREE.minDistancePerTree.get(), registry)) {
			Treasure.LOGGER.debug("The distance to the nearest wither tree is less than the minimun required.");
			return false;
		}	

		// TODO override probability check as wither tree may have it's own values to check against
		// 4. check if meets the probability criteria
		if (!meetsProbabilityCriteria(random)) {
			ChestInfo chestInfo = new ChestInfo(Rarity.SCARCE, spawnCoords, GenType.NONE);
			TreasureData.CHEST_REGISTRIES2.get(dimensionName.toString()).get(RegistryType.SURFACE).register(Rarity.SCARCE, spawnCoords, chestInfo);
			// TODO update the WITHER TREE REGISTRY with NONE
			return false;
		}

		// generate the well
		Treasure.LOGGER.debug("Attempting to generate a wither tree...");
		GeneratorResult<ChestGeneratorData> result = generate(seedReader, generator, random, spawnCoords);

		if (result.isSuccess()) {
			// add to registries
			ChestInfo chestInfo = ChestInfo.from(result.getData());	
			//TreasureData.CHEST_REGISTRIES.get(dimensionName.toString()).register(Rarity.SCARCE, spawnCoords.toShortString(), chestInfo);
			registry.register(spawnCoords);
			TreasureData.CHEST_REGISTRIES2.get(dimensionName.toString()).get(RegistryType.SURFACE).register(Rarity.SCARCE, spawnCoords, chestInfo);
		}	

		// save world data
		TreasureGenerationSavedData savedData = TreasureGenerationSavedData.get(seedReader.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}

		return true;
	}

	private boolean meetsWorldAgeCriteria(ServerWorld world, SimpleListRegistry<ICoords> registry) {
		if (registry.getValues().isEmpty() && waitChunksCount < TreasureConfig.WITHER_TREE.waitChunks.get()) {
			Treasure.LOGGER.debug("World is too young");
			waitChunksCount++;
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
		if (!RandomHelper.checkProbability(random, TreasureConfig.WITHER_TREE.genProbability.get())) {
			Treasure.LOGGER.debug("Wither tree does not meet generate probability.");
			return false;
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
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords coords) {
		Instant start = Instant.now();

		// result to return to the caller
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setEnvironment(ChestEnvironment.SURFACE);

		AxisAlignedBB witherGroveBounds = new AxisAlignedBB(coords.toPos());

		// ============ build the pit first ==============
		// add pit
		Treasure.LOGGER.debug("generate pit");
		GeneratorResult<ChestGeneratorData> genResult = PitProvider.generatePit(world, random,
				Rarity.SCARCE, coords, TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.SCARCE));
		Treasure.LOGGER.debug("result -> {}", genResult.toString());
		if (!genResult.isSuccess()) {
			return result.fail();
		}
		// ========================================

		// 2. clear the area
		buildClearing(world, generator, random, coords, coords);

		// 3. build the main wither tree
		buildMainTree(world, generator, random, coords, coords);

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

			ICoords c = coords.rotate(xlen, zlen, degrees);

			//			int landHeight = generator.getFirstOccupiedHeight(c.getX(), c.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			//			c = new Coords(c.getX(), landHeight, c.getZ());
			c = WorldInfo.getDryLandSurfaceCoords(world, generator, c);

			// get the yspawn
			//			c = WorldInfo.getDryLandSurfaceCoords(world, c.withY(WorldInfo.getHeight(world, c)));

			// add tree if criteria is met
			if (c != null && c != WorldInfo.EMPTY_COORDS) {
				if (c.getDistanceSq(coords) > 4) {
					if (world.getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHER_LOG) {
						buildClearing(world, generator, random, c, coords);
						buildTree(world, random, c, coords);

						// add tree clearing to the grove size
						AxisAlignedBB witherTreeClearingBounds = new AxisAlignedBB(c.toPos()).expandTowards(CLEARING_RADIUS, 0,
								CLEARING_RADIUS);
						witherGroveBounds = witherGroveBounds.minmax(witherTreeClearingBounds);
					}
				}
			}
		}
		Treasure.LOGGER.debug("size of clearing -> {}", witherGroveBounds.toString());

		buildRocks(world, generator, random, witherGroveBounds);
		buildScrub(world, generator, random, witherGroveBounds);

		// add chest
		ICoords chestCoords = genResult.getData().getChestContext().getCoords();
		if (chestCoords == null) {
			return result.fail();
		}
		WitherChestGenerator chestGen = new WitherChestGenerator();
		GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate((IServerWorld)world, random, chestCoords, Rarity.SCARCE, null);
		if (!chestResult.isSuccess()) {
			return result.fail();
		}

		Treasure.LOGGER.info("CHEATER! wither chest at coords: {}", coords.toShortString());
		result.getData().setChestContext(chestResult.getData().getChestContext());
		result.getData().setRegistryName(chestResult.getData().getRegistryName());
		result.getData().setRarity(Rarity.SCARCE);

		if (Treasure.LOGGER.isDebugEnabled()) {
			Instant finish = Instant.now();
			Treasure.LOGGER.debug("wither tree generate() time -> {}ms", Duration.between(start, finish).toMillis());
		}
		return result.success();
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildClearing(IServerWorld world, ChunkGenerator generator, Random random, ICoords coords, ICoords originalSpawnCoords) {
		Instant start = Instant.now();
		ICoords buildCoords = null;
		Treasure.LOGGER.debug("build clearing at -> {}", coords.toShortString());
		// build clearing
		for (int xOffset = -(CLEARING_RADIUS); xOffset <= CLEARING_RADIUS; xOffset++) {
			for (int zOffset = -(CLEARING_RADIUS); zOffset <= CLEARING_RADIUS; zOffset++) {
				if (Math.abs(xOffset) + Math.abs(zOffset) <= CLEARING_RADIUS) {
					// check if trunk index is outside generation radius
					if (!isGenerationWithinMaxRadius(coords, originalSpawnCoords)) {
						Treasure.LOGGER.debug("outside max radius - skipping");
						continue;
					}

					// find the first surface
					// TODO use vanilla method of getting land height/coords .. maybe... don't want a ITreasureBlock as surface - can't unless pass in the chunkGenerator
					//					int yHeight = WorldInfo.getHeight(world, coords.add(xOffset, 255, zOffset));
					//					buildCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
					//					Instant start1 = Instant.now();
					buildCoords = coords.add(xOffset, 0, zOffset);
					int landHeight = generator.getFirstOccupiedHeight(buildCoords.getX(), buildCoords.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
					buildCoords = buildCoords.withY(landHeight);					
					//					Treasure.LOGGER.debug("clearing surface coords -> {}", buildCoords.toShortString());
					//					Instant finish1 = Instant.now();
					//					Treasure.LOGGER.debug( time -> {}ms", Duration.between(start1, finish1).toMillis());

					if (buildCoords == WorldInfo.EMPTY_COORDS) {
						continue;
					}

					//					Instant start2 = Instant.now();
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
					//					Instant finish2 = Instant.now();
					//					Treasure.LOGGER.debug("dirt replacement time -> {}ms", Duration.between(start2, finish2).toMillis());

					//					Instant start3 = Instant.now();
					// remove existing tree
					BlockContext blockContext = new BlockContext(world, buildCoords);
					ICoords climbCoords = new Coords(buildCoords);
					while (blockContext.equalsMaterial(Material.WOOD) && !(blockContext.getState().getBlock() instanceof ITreasureBlock)) {
						// remove log
						world.setBlock(climbCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
						// climb upwards
						climbCoords = climbCoords.add(0, 1, 0);
						blockContext = new BlockContext(world, climbCoords);
					}
					//					Instant finish3 = Instant.now();
					//					Treasure.LOGGER.debug("removing existing tree time -> {}ms", Duration.between(start3, finish3).toMillis());
				}
			}
		}
		Instant finish = Instant.now();
		Treasure.LOGGER.debug("buildClearing() time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param coords
	 * @param centerCoords
	 * @return
	 */
	public boolean isGenerationWithinMaxRadius(ICoords coords, ICoords centerCoords) {
		// check if trunk index is outside generation radius
		if (Math.abs(coords.getX() - centerCoords.getX()) > MAX_GEN_RADIUS ||
				Math.abs(coords.getZ() - centerCoords.getZ()) > MAX_GEN_RADIUS) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	public void buildMainTree(IWorld world, ChunkGenerator generator, Random random, ICoords coords, ICoords originalSpawnCoords) {
		Instant start = Instant.now();

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
				if (trunkIndex == 2 && y == 2) { // TODO <-- select the right index and the face facing in the right direction

					// check if trunk index is outside generation radius
					if (!isGenerationWithinMaxRadius(trunkCoords[trunkIndex], originalSpawnCoords)) {
						continue;
					}

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
					addRoot(world, random, trunkCoords[trunkIndex], originalSpawnCoords, trunkMatrix[trunkIndex]);
				} else if (y == maxSize - 1) {
					addTop(world, random, trunkCoords[trunkIndex], originalSpawnCoords, y + 1, topMatrix.get(trunkIndex));
				} else if (y >= 3) {
					addBranch(world, random, trunkCoords[trunkIndex], originalSpawnCoords, y, maxSize, trunkMatrix[trunkIndex]);
				}
			}

			// set the new max size
			if (maxSize > 3) {
				maxSize -= RandomHelper.randomInt(random, 1, 3);
				maxSize = Math.max(3, maxSize);
			}
		}
		Instant finish = Instant.now();
		Treasure.LOGGER.debug("buildMainTree time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 */
	public void buildTree(IWorld world, Random random, ICoords coords, ICoords originalSpawnCoords) {
		Instant start = Instant.now();
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
				addRoot(world, random, coords, originalSpawnCoords, supportTrunkMatrix);
			} else if (y == maxSize - 1) {
				addTop(world, random, coords, originalSpawnCoords, y + 1, supportTrunkMatrix.get(random.nextInt(supportTrunkMatrix.size())));
			} else if (y > 3) {
				addBranch(world, random, coords, originalSpawnCoords, y, maxSize, supportTrunkMatrix);
			}
		}
		Instant finish = Instant.now();
		Treasure.LOGGER.debug("buildTree time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param trunkIndex
	 * @param y
	 * @param maxSize
	 * @param is
	 */
	private void addBranch(IWorld world, Random random, ICoords trunkCoords, ICoords originalSpawnCoords, int y, int maxSize,
			List<Direction> directions) {
		Instant start = Instant.now();
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
				ICoords c = trunkCoords.add(0, y, 0);  // 7/2/2021 changed: added .add(0, y, 0)
				// check if trunk index is outside generation radius
				if (!isGenerationWithinMaxRadius(c, originalSpawnCoords)) {
					continue;
				}
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
						WorldInfo.setBlock(world, /*c.add(0, y, 0)*/ c, state);

						// add spanish moss
						if (RandomHelper.checkProbability(random, SPANISH_MOSS_PROBABILITY)) {
							replaceBlockContext = new BlockContext(world, c.add(0, /*y*/ - 1, 0));
							if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
								world.setBlock(replaceBlockContext.getCoords().toPos(), TreasureBlocks.SPANISH_MOSS
										.defaultBlockState().setValue(SpanishMossBlock.ACTIVATED, true), 3);
							}
						}
					} else {
						break;
					}
				}
			}
		}
		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("addBranch() time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param iCoords
	 * @param list
	 */
	private void addRoot(IWorld world, Random random, ICoords coords, ICoords originalSpawnCoords, List<Direction> directions) {
		// for each direction
		for (Direction direction : directions) {
			if (RandomHelper.checkProbability(random, WITHER_ROOT_PROBABILITY)) {
				// update the coords to the correct position
				ICoords newCoords = coords.add(direction, 1);
				// check if trunk index is outside generation radius
				if (!isGenerationWithinMaxRadius(newCoords, originalSpawnCoords)) {
					continue;
				}
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
	private void addTop(IWorld world, Random random, ICoords coords, ICoords originalSpawnCoords, int y, Direction direction) {
		if (direction != null) {
			BlockState state = TreasureBlocks.WITHER_BROKEN_LOG.defaultBlockState().setValue(WitherRootBlock.FACING,
					direction);
			// add the top log to the world
			//			world.setBlockState(coords.add(0, y, 0).toPos(), state, 3);
			ICoords topCoords = coords.add(0, y, 0);
			if (isGenerationWithinMaxRadius(topCoords, originalSpawnCoords)) {
				WorldInfo.setBlock(world, coords.add(0, y, 0), state);
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param generator
	 * @param random
	 * @param witherGroveBounds
	 */
	private void buildScrub(IServerWorld world, ChunkGenerator generator, Random random, AxisAlignedBB witherGroveBounds) {
		Instant start = Instant.now();
		Treasure.LOGGER.debug("adding scrub ...");
		int width = Math.abs((int) (witherGroveBounds.maxX - witherGroveBounds.minX));
		int depth = Math.abs((int) (witherGroveBounds.maxZ - witherGroveBounds.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveBounds.minX + width * 0.5D), (int)witherGroveBounds.minY, (int)(witherGroveBounds.minZ + depth * 0.5D));

		for (int scrubIndex = 0; scrubIndex < RandomHelper.randomInt(MIN_SCRUB, MAX_SCRUB); scrubIndex++) {
			int xOffset = (int) (random.nextFloat() * width - (width/2));
			int zOffset = (int) (random.nextFloat() * depth - (depth/2));
			//			Treasure.LOGGER.debug("finding surface for scrub at -> {}", centerCoords.add(xOffset, 0, zOffset).withY(255).toShortString());
			//			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
			ICoords offsetCoords = centerCoords.add(xOffset, 0, zOffset);

			//			int landHeight = generator.getFirstOccupiedHeight(offsetCoords.getX(), offsetCoords.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			//			ICoords surfaceCoords = offsetCoords.withY(landHeight);	
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, generator, offsetCoords);

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
		Instant finish = Instant.now();
		Treasure.LOGGER.debug("buildScrub time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param witherGroveSize
	 */
	private void buildRocks(IServerWorld world, ChunkGenerator generator, Random random, AxisAlignedBB witherGroveSize) {
		Instant start = Instant.now();
		Treasure.LOGGER.debug("adding rocks ...");
		int width = Math.abs((int) (witherGroveSize.maxX - witherGroveSize.minX));
		int depth = Math.abs((int) (witherGroveSize.maxZ - witherGroveSize.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveSize.minX + width * 0.5D), (int)witherGroveSize.minY, (int)(witherGroveSize.minZ + depth * 0.5D));

		for (int rockIndex = 0; rockIndex < RandomHelper.randomInt(MIN_ROCKS, MAX_ROCKS); rockIndex++) {
			// randomize a position within the aabb
			int xOffset = (int) (random.nextFloat() * width - (width/2));
			int zOffset = (int) (random.nextFloat() * depth - (depth/2));

			//			//ICoords rocksCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
			ICoords offsetCoords = centerCoords.add(xOffset, 0, zOffset);

			//			int landHeight = generator.getFirstOccupiedHeight(offsetCoords.getX(), offsetCoords.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			//			ICoords rocksCoords = offsetCoords.withY(landHeight -1);	
			ICoords rocksCoords = WorldInfo.getDryLandSurfaceCoords(world, generator, offsetCoords);

			//			rocksCoords = rocksCoords.down(1);
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
		Instant finish = Instant.now();
		Treasure.LOGGER.debug("buildRocks time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param minDistance
	 * @return
	 */
	public static boolean checkWitherTreeProximity(IServerWorld world, ICoords coords, int minDistance, SimpleListRegistry<ICoords> registry) {

		double minDistanceSq = minDistance * minDistance;

		List<ICoords> witherTreeList = registry.getValues();
		if (witherTreeList.isEmpty()) {
			Treasure.LOGGER.debug("Unable to locate the Wither Tree Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ICoords witherTreeCoords : witherTreeList) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(witherTreeCoords);
			if (distance < minDistanceSq) {
				return true;
			}
		}		
		return false;
	}
}