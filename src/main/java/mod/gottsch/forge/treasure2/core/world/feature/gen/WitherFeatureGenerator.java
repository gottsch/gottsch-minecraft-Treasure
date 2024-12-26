/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.*;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.WitherChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.registry.PitGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;

/**
 * TODO this probably needs to move to generator.wither package
 * @author Mark Gottschling May 12, 2023
 *
 */
public class WitherFeatureGenerator implements IFeatureGenerator {
	public static final int VERTICAL_MAX_DIFF = 3;
	private static final int CLEARING_RADIUS = 7;
	private static final int DIRT_REPLACEMENT_PROBABILITY = 90;
	private static final int DEGREES = 360;
	private static final double MIN_RADIUS = 5.0;
	private static final double MAX_RADIUS = 10.0;
	private static final int MIN_MAIN_TREE_SIZE = 11;
	private static final int MIN_TREE_SIZE = 7;
	private static final int WITHER_ROOT_PROBABILITY = 50;
	private static final int WITHER_BRANCH_PROBABILITY = 30;
	private static final int SPANISH_MOSS_PROBABILITY = 80;
	private static final int TWIG_PROBABILITY = 40;
	private static final int MAX_ROCKS = 5;
	private static final int MIN_ROCKS = 0;
	private static final int MIN_SCRUB = 5;
	private static final int MAX_SCRUB = 20;
	private static final int MIN_STRANGLE_VINES = 2;
	private static final int MAX_STRANGLE_VINES = 10;
	
	protected static int UNDERGROUND_OFFSET = 3;
	
	private ResourceLocation name = new ResourceLocation(Treasure.MODID, "wither");
	
	@Override
	public ResourceLocation getName() {
		return name;
	}
	
	/*
	 * During generation a 3x3 (x-z axis) chunk area is available to alter ( = 48 blocks).
	 * From center, there is a 23/24 block radius (since even number).
	 * To be safe, the max gen radius is set to 20.
	 */
	private static final int MAX_GEN_RADIUS = 20;
	
	@SuppressWarnings("unchecked")
//	static List<Direction>[] trunkMatrix = new ArrayList[4];
//	static List<Direction> supportTrunkMatrix = new ArrayList<>();
//	static List<Direction> topMatrix = new ArrayList<>();

	private int waitChunksCount = 0;

	private static final int NW = 0;
	private static final int N = 1;
	private static final int NE = 2;
	private static final int W = 3;
	private static final int CORE = 4;
	private static final int E = 5;
	private static final int SW = 6;
	private static final int S = 7;
	private static final int SE = 8;

	private Map<Integer, List<Direction>> buildTrunkMap() {
		Map<Integer, List<Direction>> map = Maps.newHashMap();

		map.put(NW, List.of(Direction.NORTH, Direction.WEST));
		map.put(N, List.of(Direction.NORTH));
		map.put(NE, List.of(Direction.NORTH, Direction.EAST));

		map.put(W, List.of(Direction.WEST));
		map.put(CORE, new ArrayList<>());
		map.put(E, List.of(Direction.EAST));

		map.put(SW, List.of(Direction.SOUTH, Direction.WEST));
		map.put(S, List.of(Direction.SOUTH));
		map.put(SE, List.of(Direction.SOUTH, Direction.EAST));

		return map;
	}

	private List<Direction> buildTrunkTopMap() {
		List<Direction> list = Lists.newArrayList();
		list.add(0, Direction.EAST);
		list.add(1, Direction.SOUTH);
		list.add(2, Direction.SOUTH);
		list.add(3, Direction.EAST);
		list.add(4, Direction.NORTH);
		list.add(5, Direction.WEST);
		list.add(6, Direction.NORTH);
		list.add(7, Direction.NORTH);
		list.add(8, Direction.WEST);
		return list;
	}

	private List<Direction> buildSupportTrunkMap() {
		List<Direction> list = Lists.newArrayList();
		list.add(Direction.NORTH);
		list.add(Direction.EAST);
		list.add(Direction.SOUTH);
		list.add(Direction.WEST);
		return list;
	}

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext context, ICoords spawnCoords,
			IRarity rarity, ChestRarity config) {

		// NOTE spawnCoords will be 1 block in the ground due to the deferred placement. the wither tree should be on top of the ground
		spawnCoords = spawnCoords.add(0, 1, 0);

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}

		// determine spawn coords below ground
		Optional<ICoords> undergroundCoords = getUndergroundSpawnPos(context.level(), context.random(), spawnCoords, config.getMinDepth(), config.getMaxDepth());

		if (undergroundCoords.isEmpty()) {
			Treasure.LOGGER.debug("unable to spawn underground @ {}", spawnCoords);
			return Optional.empty();
		}
		Treasure.LOGGER.debug("below ground -> {}", undergroundCoords.get().toShortString());
		
		
		// setup a AABB around the spawn coords
		AABB witherGroveBounds = new AABB(spawnCoords.toPos());
		// add pit
		Treasure.LOGGER.debug("generate pit");
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(context.random());
		Treasure.LOGGER.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());
		Optional<GeneratorResult<ChestGeneratorData>> pitResult = pitGenerator.generate(context, spawnCoords, undergroundCoords.get());

		if (pitResult.isEmpty()) {
			return Optional.empty();
		}
		// TODO move inside buildMainTree
		// clear the area
//		buildClearing(context, spawnCoords, spawnCoords);
		// build the main wither tree
		buildMainTree(context, spawnCoords, spawnCoords);
		// update size of grove
		witherGroveBounds = witherGroveBounds.expandTowards(CLEARING_RADIUS, 0, CLEARING_RADIUS);

		// TODO move to own method
		// determine how many extra "withered" trees to include in the area
		int numTrees = RandomHelper.randomInt(Config.SERVER.witherTree.minSupportingTrees.get(), Config.SERVER.witherTree.maxSupportingTrees.get());
		Treasure.LOGGER.debug("number of trees -> {}", numTrees);
		for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
			// find a random location around a radius from the tree
			// ie. rand x-radius, rand z-radius = new point (+x,+z), rand degrees of
			// rotation from origin
			double xlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			double zlen = RandomHelper.randomDouble(MIN_RADIUS, MAX_RADIUS);
			int degrees = RandomHelper.randomInt(0, DEGREES);

			ICoords c = spawnCoords.rotate(xlen, zlen, degrees);
			c = WorldInfo.getDryLandSurfaceCoordsWG(context, c);
			// add tree if criteria is met
			if (c != null && c != Coords.EMPTY) {
				if (c.getDistanceSq(spawnCoords) > 4) {
					if (context.level().getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHERWOOD_LOG.get()) {
						buildClearing(context, c, spawnCoords);
						buildTree(context, c, spawnCoords);

						// add tree clearing to the grove size
						AABB witherTreeClearingBounds = new AABB(c.toPos()).expandTowards(CLEARING_RADIUS, 0,
								CLEARING_RADIUS);
						witherGroveBounds = witherGroveBounds.minmax(witherTreeClearingBounds);
					}
				}
			}
		}
		Treasure.LOGGER.debug("size of clearing -> {}", witherGroveBounds.toString());

		buildRocks(context, witherGroveBounds);
		buildScrub(context, witherGroveBounds);
//		buildStrangleVines(context, witherGroveBounds)
//		buildBlight(context, witherGroveBounds)
		// TODO add one-time spawners using mobSets

		// add chest
		ICoords chestCoords = pitResult.get().getData().getCoords();
		if (chestCoords == null) {
			return Optional.empty();
		}
		WitherChestGenerator chestGen = new WitherChestGenerator();
		GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(context, chestCoords, SpecialRarity.WITHER, null);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}
		
		Treasure.LOGGER.info("CHEATER! WITHER chest at coords: {}", spawnCoords.toShortString());
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setCoords(chestCoords);
		result.getData().setRegistryName(chestResult.getData().getRegistryName());
		result.getData().setRarity(SpecialRarity.WITHER);
		
		return Optional.of(result);
	}

	/**
	 * 
	 * @param random
	 * @return
	 */
	public IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(RandomSource random) {
		PitType pitType = RandomHelper.checkProbability(random, Config.SERVER.pits.structureProbability.get()) ? PitType.STRUCTURE : PitType.STANDARD;
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = PitGeneratorRegistry.get(pitType);
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("using pitType -> {}, gen -> {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
	
	private void buildClearing(IWorldGenContext context, ICoords coords, ICoords originalSpawnCoords) {
//		Instant start = Instant.now();
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
//						Instant start1 = Instant.now();
					buildCoords = coords.add(xOffset, 0, zOffset);
					buildCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, buildCoords);
//						Instant finish1 = Instant.now();
//						Treasure.LOGGER.debug("find surface time -> {}ms", Duration.between(start1, finish1).toMillis());

					if (buildCoords == Coords.EMPTY) {
						continue;
					}

//						Instant start2 = Instant.now();
					// additional check that it's not a tree and within 2 y-blocks of original
					if (Math.abs(buildCoords.getY() - coords.getY()) < VERTICAL_MAX_DIFF) {
						BlockContext cube = new BlockContext(context.level(), buildCoords.down(1));
						if (cube.isFluid()) {
							continue;
						}
						if (RandomHelper.checkProbability(context.random(), DIRT_REPLACEMENT_PROBABILITY)) {
							switch (context.random().nextInt(10)) {
								case 0 -> context.level().setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.MYCELIUM.defaultBlockState(), 3);
								case 1 -> context.level().setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.PODZOL.defaultBlockState(), 3);
								case 2, 3 -> context.level().setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.MUD.defaultBlockState(), 3);
								case 4, 5 -> context.level().setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.COARSE_DIRT.defaultBlockState(), 3);
								default -> context.level().setBlock(buildCoords.add(0, -1, 0).toPos(), Blocks.DIRT.defaultBlockState(), 3);
							}
						}
					}
//						Instant finish2 = Instant.now();
//						Treasure.LOGGER.debug("dirt replacement time -> {}ms", Duration.between(start2, finish2).toMillis());

//						Instant start3 = Instant.now();
					// remove existing tree
					BlockContext blockContext = new BlockContext(context.level(), buildCoords);
					ICoords climbCoords = new Coords(buildCoords);
					while (blockContext.getState().getBlock().defaultMapColor() == MapColor.WOOD 
							&& !(blockContext.getState().getBlock() instanceof ITreasureBlock)) {
						
						// remove log
						context.level().setBlock(climbCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
						// climb upwards
						climbCoords = climbCoords.add(0, 1, 0);
						blockContext = new BlockContext(context.level(), climbCoords);
					}
//						Instant finish3 = Instant.now();
//						Treasure.LOGGER.debug("removing existing tree time -> {}ms", Duration.between(start3, finish3).toMillis());
				}
			}
		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("buildClearing() time -> {}ms", Duration.between(start, finish).toMillis());
	}

	private ICoords[] buildTrunkCoords(ICoords coords) {
		ICoords[] trunkCoords = new Coords[9];
		trunkCoords[NW] = coords;
		trunkCoords[N] = coords.east(1);
		trunkCoords[NE] = coords.east(2);
		trunkCoords[W] = coords.south(1);
		trunkCoords[CORE] = coords.add(1, 0, 1);
		trunkCoords[E] = coords.add(2, 0, 1);
		trunkCoords[SW] = coords.south(2);
		trunkCoords[S] = coords.add(1, 0, 2);
		trunkCoords[SE] = coords.add(2, 0, 2);

		return trunkCoords;
	}

	/**
	 * 
	 * @param context
	 * @param coords
	 * @param originalSpawnCoords
	 */
	public void buildMainTree(IWorldGenContext context, ICoords coords, ICoords originalSpawnCoords) {
//		Instant start = Instant.now();

		// clear the area
		buildClearing(context, coords, coords);

		Map<Integer, List<Direction>> trunkMatrix = buildTrunkMap();
		List<Direction> trunkTopMatrix = buildTrunkTopMap();
		ICoords[] trunkCoords = buildTrunkCoords(coords);

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(context.random(),
				Math.min(MIN_MAIN_TREE_SIZE, Config.SERVER.witherTree.maxTrunkSize.get()),
				Config.SERVER.witherTree.maxTrunkSize.get());

		int size = RandomHelper.randomInt(context.random(), Math.min(MIN_MAIN_TREE_SIZE, maxSize), maxSize);
		int tallestSize = 0;

		// build a 2x2 trunk
		boolean hasLifeBeenAdded = false;
		for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {
			// select the log
			BlockState trunkBlockState;
			trunkBlockState = (trunkIndex == CORE) ? trunkBlockState = TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState()
					: TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState();

			for (int y = 0; y < size; y++) {
//				if (trunkIndex == 2 && y == 2) {

					// check if trunk index is outside generation radius
//					if (!isGenerationWithinMaxRadius(trunkCoords[trunkIndex], originalSpawnCoords)) {
//						continue;
//					}

//					if (!hasLifeBeenAdded) {
//						context.level().setBlock(trunkCoords[trunkIndex].add(0, y, 0).toPos(),
//								TreasureBlocks.WITHERWOOD_SOUL_LOG.get().defaultBlockState()
//								.setValue(WitherSoulLog.APPEARANCE, WitherSoulLog.Appearance.FACE)
//								.setValue(WitherSoulLog.FACING, Direction.SOUTH), 3);
//						hasLifeBeenAdded = true;
//						continue;
//					}
//				}

				// add the trunk
				context.level().setBlock(trunkCoords[trunkIndex].add(0, y, 0).toPos(),	trunkBlockState, 3);

				// add the decorations (branches, roots, top)
				if (trunkIndex != CORE) {
					if (y == 0) {
						addRoot(context, trunkCoords[trunkIndex], originalSpawnCoords, trunkMatrix.get(trunkIndex));
					} else if (y == size - 1 && trunkIndex % 2 == 0) {
						addTop(context, trunkCoords[trunkIndex], originalSpawnCoords, y + 1, trunkTopMatrix.get(trunkIndex));
					} else if (y >= 3) {
						addBranch(context, trunkCoords[trunkIndex], originalSpawnCoords, y, size, trunkMatrix.get(trunkIndex));
					}
				}
			}

			// TODO replace mid-low center position with heart

			// store the tallest size.
			tallestSize = Math.max(size, tallestSize);

			// set the new size
			if (trunkIndex == 4) {
				size = RandomHelper.randomInt(context.random(), Math.min(MIN_MAIN_TREE_SIZE, tallestSize - 1), tallestSize - 1);
			} else {
				size = RandomHelper.randomInt(context.random(), Math.min(MIN_MAIN_TREE_SIZE, maxSize), maxSize);
			}
		}

		// generate strangle vine around the tree
		for (int i = MIN_STRANGLE_VINES; i < MAX_STRANGLE_VINES; i++) {

		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("buildMainTree time -> {}ms", Duration.between(start, finish).toMillis());
	}
	
	public void buildTree(IWorldGenContext context, ICoords coords, ICoords originalSpawnCoords) {
//		Instant start = Instant.now();
		List<Direction> supportTrunkMatrix = buildSupportTrunkMap();

		// determine the size of the main trunk
		int  maxSize = RandomHelper.randomInt(context.random(), MIN_TREE_SIZE, Config.SERVER.witherTree.maxTrunkSize.get());

		for (int y = 0; y < maxSize; y++) {

			// add the trunk
			context.level().setBlock(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState(), 3);

			// add the branches/roots
			if (y == 0) {
				addRoot(context, coords, originalSpawnCoords, supportTrunkMatrix);
			} else if (y == maxSize - 1) {
				addTop(context, coords, originalSpawnCoords, y + 1, supportTrunkMatrix.get(context.random().nextInt(supportTrunkMatrix.size())));
			} else if (y > 3) {
				addBranch(context, coords, originalSpawnCoords, y, maxSize, supportTrunkMatrix);
			}
		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("buildTree time -> {}ms", Duration.between(start, finish).toMillis());
	}

	private void addRoot(IWorldGenContext context, ICoords coords, ICoords originalSpawnCoords, List<Direction> directions) {
		// for each direction
		for (Direction direction : directions) {
			if (RandomHelper.checkProbability(context.random(), WITHER_ROOT_PROBABILITY)) {
				// update the coords to the correct position
				ICoords newCoords = coords.add(direction, 1);
				// check if trunk index is outside generation radius
				if (!isGenerationWithinMaxRadius(newCoords, originalSpawnCoords)) {
					continue;
				}
				BlockContext groundBlockContext = new BlockContext(context.level(), newCoords.down(1));
				BlockContext replaceBlockContext = new BlockContext(context.level(), newCoords);
				if (groundBlockContext.isSolid()
						&& (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable())) {
					BlockState state = TreasureBlocks.WITHERWOOD_ROOT.get().defaultBlockState()
							.setValue(WitherRootBlock.FACING, direction)
							.setValue(WitherRootBlock.ACTIVATED, true);
					WorldInfo.setBlock(context.level(), newCoords, state);
				}
			}
		}
	}
	
	private void addBranch(IWorldGenContext context, ICoords trunkCoords, ICoords originalSpawnCoords, int y, int maxSize,
			List<Direction> directions) {
//		Instant start = Instant.now();
		int branchSize = 0;
		if (y < maxSize / 3 || y > maxSize / 4)
			branchSize = 2;
		else
			branchSize = 1;

		// for each direction
		for (Direction direction : directions) {
			ICoords c = trunkCoords.add(0, y, 0);  // 7/2/2021 changed: added .add(0, y, 0)
			// randomize if a branch is generated
			if (RandomHelper.checkProbability(context.random(), WITHER_BRANCH_PROBABILITY)) {

				// check if trunk index is outside generation radius
				if (!isGenerationWithinMaxRadius(c, originalSpawnCoords)) {
					continue;
				}
				// for the num of branch segments
				for (int segment = 0; segment < branchSize; segment++) {
					c = c.add(direction, 1);
					BlockContext replaceBlockContext = new BlockContext(context.level(), c);

					// if there is a branch directly below, don't add another branch but potentially add a twig
					if (context.level().getBlockState(c.down(1).toPos()).getBlock() instanceof WitherBranchBlock) {
						if (segment == 0 && RandomHelper.checkProbability(context.random(), TWIG_PROBABILITY)) {
							addTwig(context, c, direction);
						}
						break;
					}

					// if able to place branch here
					if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
						// rotate the branch in the right direction
						BlockState state = TreasureBlocks.WITHERWOOD_BRANCH.get().defaultBlockState()
								.setValue(WitherBranchBlock.FACING, direction);

						// add the branch to the world
						WorldInfo.setBlock(context.level(), c, state);

						// add spanish moss
						if (RandomHelper.checkProbability(context.random(), SPANISH_MOSS_PROBABILITY)) {
							replaceBlockContext = new BlockContext(context.level(), c.add(0, /*y*/ - 1, 0));
							if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
								context.level().setBlock(replaceBlockContext.getCoords().toPos(), TreasureBlocks.SPANISH_MOSS
										.get().defaultBlockState().setValue(SpanishMossBlock.ACTIVATED, true), 3);
							}
						}
					} else {
						break;
					}
				}
			} else if (RandomHelper.checkProbability(context.random(), TWIG_PROBABILITY)) {
				addTwig(context, c.add(direction, 1), direction);
			}
		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("addBranch() time -> {}ms", Duration.between(start, finish).toMillis());
	}

	private void addTwig(IWorldGenContext context, ICoords coords, Direction direction) {
		WorldInfo.setBlock(context.level(), coords, TreasureBlocks.WITHERWOOD_TWIG.get().defaultBlockState()
				.setValue(WitherTwigBlock.FACING, direction));
	}

	private void addTop(IWorldGenContext context, ICoords coords, ICoords originalSpawnCoords, int y, Direction direction) {
		if (direction != null) {
			BlockState state = TreasureBlocks.WITHERWOOD_BROKEN_LOG.get().defaultBlockState().setValue(WitherRootBlock.FACING, direction);
			ICoords topCoords = coords.add(0, y, 0);
			if (isGenerationWithinMaxRadius(topCoords, originalSpawnCoords)) {
				WorldInfo.setBlock(context.level(), coords.add(0, y, 0), state);
			}
		}
	}
	
	private void buildScrub(IWorldGenContext context, AABB witherGroveBounds) {
//		Instant start = Instant.now();
//		Treasure.LOGGER.debug("adding scrub ...");
		int width = Math.abs((int) (witherGroveBounds.maxX - witherGroveBounds.minX));
		int depth = Math.abs((int) (witherGroveBounds.maxZ - witherGroveBounds.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveBounds.minX + width * 0.5D), (int)witherGroveBounds.minY, (int)(witherGroveBounds.minZ + depth * 0.5D));

		for (int scrubIndex = 0; scrubIndex < RandomHelper.randomInt(MIN_SCRUB, MAX_SCRUB); scrubIndex++) {
			int xOffset = (int) (context.random().nextFloat() * width - (width/2));
			int zOffset = (int) (context.random().nextFloat() * depth - (depth/2));
			//			Treasure.LOGGER.debug("finding surface for scrub at -> {}", centerCoords.add(xOffset, 0, zOffset).withY(255).toShortString());
			//			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, centerCoords.add(xOffset, 0, zOffset).withY(255));
			ICoords offsetCoords = centerCoords.add(xOffset, 0, zOffset);

			//			int landHeight = generator.getFirstOccupiedHeight(offsetCoords.getX(), offsetCoords.getZ(), Heightmap.Type.WORLD_SURFACE_WG) + 1;
			//			ICoords surfaceCoords = offsetCoords.withY(landHeight);	
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, offsetCoords);

			Treasure.LOGGER.debug("adding scrub at -> {}", surfaceCoords.toShortString());
			if (surfaceCoords == Coords.EMPTY) {
				Treasure.LOGGER.debug("bad surfaceCoords -> {}", surfaceCoords.toShortString());
				continue;
			}
			// check if current block is a dirt, podzol, coarse dirt or sand
			Block supportBlock = context.level().getBlockState(surfaceCoords.down(1).toPos()).getBlock();
			if (supportBlock == Blocks.DIRT || supportBlock == Blocks.SAND) {
				// randomize between bush and stump
				if (RandomHelper.checkProbability(context.random(), 25)) {
					context.level().setBlock(surfaceCoords.toPos(), Blocks.OAK_LOG.defaultBlockState(), 3);
				}
				else {
					context.level().setBlock(surfaceCoords.toPos(), Blocks.DEAD_BUSH.defaultBlockState(), 3);
				}
			}
		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("buildScrub time -> {}ms", Duration.between(start, finish).toMillis());
	}

	/**
	 * 
	 * @param context
	 * @param witherGroveSize
	 */
	private void buildRocks(IWorldGenContext context, AABB witherGroveSize) {
//		Instant start = Instant.now();
//		Treasure.LOGGER.debug("adding rocks ...");
		int width = Math.abs((int) (witherGroveSize.maxX - witherGroveSize.minX));
		int depth = Math.abs((int) (witherGroveSize.maxZ - witherGroveSize.minZ));
		ICoords centerCoords = new Coords((int)(witherGroveSize.minX + width * 0.5D), (int)witherGroveSize.minY, (int)(witherGroveSize.minZ + depth * 0.5D));

		for (int rockIndex = 0; rockIndex < RandomHelper.randomInt(MIN_ROCKS, MAX_ROCKS); rockIndex++) {
			// randomize a position within the aabb
			int xOffset = (int) (context.random().nextFloat() * width - (width/2));
			int zOffset = (int) (context.random().nextFloat() * depth - (depth/2));

			ICoords offsetCoords = centerCoords.add(xOffset, 0, zOffset);
			ICoords rocksCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, offsetCoords);

			//			rocksCoords = rocksCoords.down(1);
			//Treasure.logger.debug("adding rocks at -> {}", rocksCoords.toShortString());
			// check if current block is a tree or any treasure block
			try {
				if (rocksCoords == Coords.EMPTY || context.level().getBlockState(rocksCoords.toPos()).getBlock() instanceof ITreasureBlock) {
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
						if (RandomHelper.checkProbability(context.random(), 70)) {
							ICoords spawnCoords = new Coords(rocksCoords).add(x, y, z);
							context.level().setBlock(spawnCoords.toPos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
						}
					}
				}
			}
		}
//		Instant finish = Instant.now();
//		Treasure.LOGGER.debug("buildRocks time -> {}ms", Duration.between(start, finish).toMillis());
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
	 * TODO Duplicate to PitChestFeature
	 * @param level
	 * @param random
	 * @param startingCoords
	 * @param minDepth
	 * @param maxDepth
	 * @return
	 */
	public static Optional<ICoords> getUndergroundSpawnPos(ServerLevelAccessor level, RandomSource random, ICoords startingCoords, int minDepth, int maxDepth) {
		int depth = RandomHelper.randomInt(minDepth, maxDepth);
		int ySpawn = Math.max(UNDERGROUND_OFFSET, startingCoords.getY() - depth);
		Treasure.LOGGER.debug("ySpawn -> {}", ySpawn);
		ICoords coords = new Coords(startingCoords.getX(), ySpawn, startingCoords.getZ());
		// get floor pos (if in a cavern or tunnel etc)
		coords = WorldInfo.getSubterraneanSurfaceCoords(level, coords);

		return (coords == null || coords == Coords.EMPTY) ? Optional.empty() : Optional.of(coords);
	}
}
