/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class GenUtil {
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
	
//	TODO 1.15.2
//	protected static final int UNDERGROUND_OFFSET = 5;
//	protected static final int VERTICAL_MAX_DIFF = 2;
//	private static final int FOG_RADIUS = 5;
//
//	/**
//	 * convenience method
//	 * 
//	 * @param offset
//	 * @return
//	 */
//	public static Block getMarkerBlock(StructureMarkers marker) {
//		return Treasure.TEMPLATE_MANAGER.getMarkerMap().get(marker);
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param pos
//	 * @return
//	 */
//	public static AbstractTreasureChestTileEntity getChestTileEntity(World world, ICoords coords) {
//		BlockPos pos = coords.toPos();
//
//		// get the tile entity
//		TileEntity te = (TileEntity) world.getTileEntity(pos);
//		if (te == null) {
//			Treasure.LOGGER.warn("Unable to locate ChestConfig TileEntity @: " + pos);
//			return null;
//		}
//		// test if te implements ITreasureChestTileEntityOLD
//		if (!(te instanceof AbstractTreasureChestTileEntity)) {
//			Treasure.LOGGER.warn("ChestConfig TileEntity does not implement TreasureChestTileEntity @: " + pos);
//			return null;
//		}
//
//		// cast
//		return (AbstractTreasureChestTileEntity) te;
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param coords
//	 * @param block
//	 * @return
//	 */
//	public static boolean replaceWithBlock(World world, ICoords coords, Block block) {
//		// don't change if old block is air
//		Cube cube = new Cube(world, coords);
//		if (cube.isAir())
//			return false;
//		world.setBlockState(coords.toPos(), block.getDefaultState());
//		return true;
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param coords
//	 * @param blockState
//	 * @return
//	 */
//	public static boolean replaceWithBlockState(World world, ICoords coords, BlockState blockState) {
//		// don't change if old block is air
//		Cube cube = new Cube(world, coords);
//		if (cube.isAir())
//			return false;
//		world.setBlockState(coords.toPos(), blockState);
//		return true;
//	}
//
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param coords
	 * @return
	 */
	public static boolean replaceBlockWithChest(World world, Random random, Block chest, ICoords coords) {
		// get the old state
		BlockState oldState = world.getBlockState(coords.toPos());

		if (oldState.getProperties().contains(FACING)) {
			Treasure.LOGGER.debug("World Chest marker has FACING property:" + oldState.get(FACING));
			// set the new state
			return placeChest(world, chest, coords, (Direction) oldState.get(FACING));

		} else {
			Treasure.LOGGER.debug("World Chest marker does NOT have a FACING property.");
			return placeChest(world, chest, coords, Direction.byHorizontalIndex(random.nextInt(4)));
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chest
	 * @param state
	 * @return
	 */
	public static boolean replaceBlockWithChest(World world, Random random, ICoords coords, Block chest,
			BlockState state) {
		if (state.getProperties().contains(FACING)) {
			Treasure.LOGGER.debug("Given marker state has FACING property -> {}", state.get(FACING));
			return placeChest(world, chest, coords, (Direction) state.get(FACING));
		}

		if (state.getBlock() == Blocks.CHEST) {
			Treasure.LOGGER.debug("Given marker state is a vanilla chest.");
			Direction direction = (Direction) state.get(ChestBlock.FACING);
			return placeChest(world, chest, coords, direction);
		}

		Treasure.LOGGER.debug("Given marker state neither has FACING nor is a vanilla chest.");
		// else do generic
		return replaceBlockWithChest(world, random, chest, coords);
	}

	/**
	 * 
	 * @param world
	 * @param chest
	 * @param pos
	 * @return
	 */
	public static boolean placeChest(World world, Block chest, ICoords coords, Direction direction) {
		// TODO wrap all Direction in Heading
		// check if spawn pos is valid
		if (!WorldInfo.isValidY(coords)) {
			Treasure.LOGGER.debug("Cannot place chest due to invalid y pos -> {}", coords.toShortString());
			return false;
		}

		try {
			BlockPos pos = coords.toPos();
			// create and place the chest
			world.setBlockState(pos, chest.getDefaultState().with(FACING, direction), 3);
			Treasure.LOGGER.debug("placed chest -> {} into world at coords -> {} with prop -> {}",
					chest.getClass().getSimpleName(), coords.toShortString(), direction);

			// get the direction the block is facing.
			Heading heading = Heading.fromDirection(direction);
			((AbstractChestBlock<?>) chest).rotateLockStates(world, pos, Heading.NORTH.getRotation(heading));

			// get the tile entity
			TileEntity tileEntity = (TileEntity) world.getTileEntity(pos);

			if (tileEntity == null) {
				// remove the chest block
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				Treasure.LOGGER.warn("Unable to create ChestConfig's TileEntity, removing ChestConfig.");
				return false;
			}
			// update the TCTE facing
			((AbstractTreasureChestTileEntity) tileEntity).setFacing(direction.getIndex());
		} catch (Exception e) {
			Treasure.LOGGER.error("An error occurred placing chest: ", e);
			return false;
		}
		return true;
	}

//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param x
//	 * @param y
//	 * @param z
//	 */
//	@Deprecated
//	public static boolean placeMarkers(World world, Random random, ICoords coords) {
//		boolean isSuccess = false;
//
//		Treasure.LOGGER.debug("Using coords {} to seed markers.", coords.toShortString());
//
//		// check if gravestones are enabled
//		if (!TreasureConfig.WORLD_GEN.getMarkerProperties().isGravestonesAllowed) {
//			return false;
//		}
//
////		FogBlock[] fogDensity1 = new FogBlock[] { 
////				TreasureBlocks.MED_FOG_BLOCK, 
////				TreasureBlocks.MED_FOG_BLOCK, 
////				TreasureBlocks.MED_FOG_BLOCK,
////				TreasureBlocks.MED_FOG_BLOCK,
////				TreasureBlocks.LOW_FOG_BLOCK,
////				};
//
//		int x = coords.getX();
////		int y = coords.getY();
//		int z = coords.getZ();
//
//		// for the number of markers configured
//		int numberOfMarkers = RandomHelper.randomInt(
//				TreasureConfig.WORLD_GEN.getMarkerProperties().minGravestonesPerChest,
//				TreasureConfig.WORLD_GEN.getMarkerProperties().maxGravestonesPerChest);
//		// calculate the grid size
//		int gridSize = 4;
//		if (numberOfMarkers < 6) {
//			/* default */ } else if (numberOfMarkers >= 6 && numberOfMarkers <= 8) {
//			gridSize = 5;
//		} else {
//			gridSize = 6;
//		}
//
//		// loop through each marker
//		for (int i = 0; i < numberOfMarkers; i++) {
////			boolean isSkeleton = false;
//
//			// generator random x, z
//			int xSpawn = x + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1
//			int zSpawn = z + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1
//
//			// get the "surface" y
//			int ySpawn = WorldInfo.getHeightValue(world, new Coords(xSpawn, 0, zSpawn));
//			ICoords spawnCoords = new Coords(xSpawn, ySpawn, zSpawn);
//
//			// determine if valid y
//			// ySpawn = getValidSurfaceY(world, xSpawn, ySpawn, zSpawn);
//			if (!WorldInfo.isValidY(spawnCoords)) {
//				Treasure.LOGGER.debug(String.format("[%d] is not a valid y value.", spawnCoords.getY()));
//				continue;
//			}
//
//			// get a valid surface location
////			Treasure.LOGGER.debug("Getting dry land coords for @ {}", spawnCoords.toShortString());
//			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
//			if (spawnCoords == null) {
//				Treasure.LOGGER.debug(String.format("Not a valid surface @ %s", coords));
//				continue;
//			}
////			Treasure.LOGGER.debug("Marker @ {}", spawnCoords.toShortString());
//
//			// don't place if the spawnCoords isn't AIR or FOG or REPLACEABLE
//			Cube cube = new Cube(world, spawnCoords);
//			if (!cube.isAir() && !cube.isReplaceable() && !cube.equalsMaterial(TreasureItems.FOG)) {
//				Treasure.LOGGER.debug("Marker not placed because block  @ [{}] is not Air, Replaceable nor Fog.",
//						spawnCoords.toShortString());
//				continue;
//			}
//
//			// don't place if the block underneath is of GenericBlock ChestConfig or
//			// Container
//			Block block = world.getBlockState(spawnCoords.add(0, -1, 0).toPos()).getBlock();
//			if (block instanceof ContainerBlock || block instanceof AbstractModContainerBlock
//					|| block instanceof ITreasureBlock) {
//				Treasure.LOGGER
//						.debug("Marker not placed because block underneath is a chest, container or Treasure block.");
//				continue;
//			}
//
//			Block marker = null;
//			// grab a random marker
//			marker = TreasureBlocks.gravestones.get(random.nextInt(TreasureBlocks.gravestones.size()));
//
//			// select a random facing direction
//			EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
//			EnumFacing facing = horizontals[random.nextInt(horizontals.length)];
//
//			// place the block
//			world.setBlockState(spawnCoords.toPos(),
//					marker.getDefaultState().withProperty(AbstractChestBlock.FACING, facing));
//
//			// add fog around the block
//			if (TreasureConfig.WORLD_GEN.getGeneralProperties().enableFog && RandomHelper.checkProbability(random,
//					TreasureConfig.WORLD_GEN.getMarkerProperties().gravestoneFogProbability)) {
//				List<FogBlock> fogDensity = new ArrayList<>(5);
//				// randomize the size of the fog
//				int fogSize = RandomHelper.randomInt(1, 4);
//				// populate the fog density list
////				for (int f = 0; f < fogSize; f++)
////					fogDensity.add(TreasureBlocks.MED_FOG_BLOCK);
////				fogDensity.add(TreasureBlocks.LOW_FOG_BLOCK);
//				// add fog around marker
//				addFog(world, random, spawnCoords, fogDensity.toArray(new FogBlock[] {}));
//			}
//
//			isSuccess = true;
//		}
//
//		return isSuccess;
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param random
//	 * @param coords
//	 */
//	public static void placeSkeleton(World world, Random random, ICoords coords) {
//		// select a random facing direction
//		EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
//		EnumFacing facing = horizontals[random.nextInt(horizontals.length)];
//
//		// ICoords coords2 = new Coords(coords.offset(facing));
//		ICoords coords2 = new Coords(coords.toPos().offset(facing));
//
//		Cube cube = new Cube(world, coords);
//		Cube cube2 = new Cube(world, coords2);
//
////		IBlockState state2 = world.getBlockState(coords2.toPos());
//		boolean flag = cube.isReplaceable();
//		boolean flag1 = cube2.isReplaceable();
//		boolean flag2 = flag || cube.isAir();
//		boolean flag3 = flag1 || cube2.isAir();
//
//		// TODO add cube.isSideSolid
//		if (flag2 && flag3
//				&& world.getBlockState(coords.down(1).toPos()).isSideSolid(world, coords.down(1).toPos(), EnumFacing.UP)
//				&& world.getBlockState(coords2.down(1).toPos()).isSideSolid(world, coords2.down(1).toPos(),
//						EnumFacing.UP)) {
//			IBlockState skeletonState = TreasureBlocks.SKELETON.getDefaultState()
//					.withProperty(SkeletonBlock.FACING, facing.getOpposite())
//					.withProperty(SkeletonBlock.PART, SkeletonBlock.EnumPartType.BOTTOM);
//
//			world.setBlockState(coords.toPos(), skeletonState, 3);
//			world.setBlockState(coords2.toPos(),
//					skeletonState.withProperty(SkeletonBlock.PART, SkeletonBlock.EnumPartType.TOP), 3);
//
//			world.notifyNeighborsRespectDebug(coords.toPos(), cube.getState().getBlock(), false);
//			world.notifyNeighborsRespectDebug(coords2.toPos(), cube.getState().getBlock(), false);
//		}
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param coords
//	 */
//	public static void addFog(World world, Random random, ICoords coords, FogBlock[] fogDensity) {
//		ICoords fogCoords = null;
//
//		// add fog around the given coords with a 5 block radius
//		for (int xOffset = -5; xOffset <= 5; xOffset++) {
//			for (int zOffset = -5; zOffset <= 5; zOffset++) {
//				int radius = Math.abs(xOffset) + Math.abs(zOffset);
//				if (radius <= FOG_RADIUS) {
//
//					// skip if at the origin pos
//					if (xOffset == 0 && zOffset == 0) {
//						continue;
//					}
//
//					int yHeight = WorldInfo.getHeightValue(world, coords.add(xOffset, 255, zOffset));
////					Treasure.LOGGER.debug("Wither Tree Clearing yOffset: " + yHeight);
////					fogCoords = GenUtil.getAnySurfacePos(world, new BlockPos(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
//
//					fogCoords = WorldInfo.getDryLandSurfaceCoords(world,
//							new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
////					Treasure.LOGGER.debug("1. fog coords @ {}", fogCoords);
//
//					// ensure that the fog isn't resting on a Treasure2!-related block
//					if (world.getBlockState(fogCoords.down(1).toPos()).getBlock() instanceof ITreasureBlock) {
////						Treasure.LOGGER.debug("eXit - ITreasureBlock at coords");
//						continue;
//					}
//
//					// additional check that it's not a tree and within 2 y-blocks of original
//					ICoords deltaCoords = fogCoords.delta(coords);
//					Block block = null;
//					if (Math.abs(deltaCoords.getY()) > VERTICAL_MAX_DIFF) {
////						Treasure.LOGGER.debug("eXit - delta too great at coords");
//						continue;
//					}
//
//					Cube cube = new Cube(world, fogCoords);
//					// ensure location of fog coords is air.
//					if (!cube.isAir() && !cube.isReplaceable() && !cube.equalsMaterial(TreasureItems.FOG)) {
////						Treasure.LOGGER.debug("eXit - block at coords is {}", cube.getState().getBlock().toString());
//						continue;
//					}
//
//					// select the fog block from the density array
//					if (radius > fogDensity.length)
//						radius = fogDensity.length;
//					if (radius < 1)
//						radius = 1;
//					block = fogDensity[radius - 1];
//
////					Treasure.LOGGER.debug("2. selecting {} fog", ((FogBlock)block).getFog().getSize());
//
//					if (cube.equalsMaterial(TreasureItems.FOG)) {
//						// test if the block at fog coords is already fog, then whichever is bigger
//						// remains
//						FogBlock origBlock = (FogBlock) cube.getState().getBlock();
////						Treasure.LOGGER.debug("3. orig block IS {} at coords", origBlock.getFog().getSize());
//						if (origBlock.getFogHeight().getSize() > ((FogBlock) block).getFogHeight().getSize()) {
////							Treasure.LOGGER.debug("eXit - orig block is bigger than block");
//							continue;
//						}
//					}
//
//					// place the fog block
//					world.setBlockState(fogCoords.toPos(), block.getDefaultState());
////					Treasure.LOGGER.debug("Placed fog block @ {}", fogCoords.toShortString());
//				}
//			}
//		}
//	}
//
//	/**
//	 * Returns a <b>valid</b> pos underground.
//	 * 
//	 * @param world
//	 * @param random
//	 * @param surfaceCoords
//	 * @param spawnYMin
//	 * @return
//	 */
//	public static ICoords getUndergroundSpawnCoords(World world, Random random, ICoords surfaceCoords, int spawnYMin) {
//		ICoords spawnCoords = null;
//
//		// spawn location under ground
//		if (surfaceCoords.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
//			int ySpawn = random.nextInt(surfaceCoords.getY() - (spawnYMin + UNDERGROUND_OFFSET)) + spawnYMin;
//
//			spawnCoords = new Coords(surfaceCoords.getX(), ySpawn, surfaceCoords.getZ());
//			// get floor pos (if in a cavern or tunnel etc)
//			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
//		}
//		return spawnCoords;
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param coords
//	 * @return
//	 */
//	public static ICoords findUndergroundCeiling(World world, ICoords coords) {
//		final int CEILING_FAIL_SAFE = 50;
//		int ceilingHeight = 1;
//
//		// find the ceiling of the cavern
//		while (world.isAirBlock(coords.toPos())) {
//			ceilingHeight++;
//			if (ceilingHeight > world.getHeight() || ceilingHeight == CEILING_FAIL_SAFE) {
//				return null;
//			}
////			coords = coords.add(0, ceilingHeight, 0); 
//			coords = coords.add(0, 1, 0);
//		}
//		// add 1 height to the final pos
////		coords = coords.add(0, ceilingHeight++, 0);		
//		coords = coords.add(0, 1, 0);
//		return coords;
//	}
//
//	/**
//	 * This method assumes that the 3 blocks above the chest has been generated and
//	 * therefor starts at pos.y + 4
//	 * 
//	 * @param world
//	 * @param random
//	 * @param coords
//	 * @param surfaceCoords
//	 */
//	@Deprecated
//	public static void fillSimpleShaftRandomly(World world, Random random, ICoords coords, ICoords surfaceCoords) {
//		ICoords replaceCoords;
//
//		// randomly fill shaft
//		for (int i = coords.getY() + (3 + 1); i <= surfaceCoords.getY() - 5; i++) {
//			// if the block to be replaced is air block then skip to the next pos
//			replaceCoords = new Coords(coords.getX(), i, coords.getZ());
//			if (world.isAirBlock(replaceCoords.toPos())) {
//				continue;
//			}
//
//			BlockState blockState = null;
//			int m = random.nextInt(100);
//			if (m < 50) // 2x as likely to be air
////				world.setBlockState(replaceCoords.toPos(), Blocks.AIR.getDefaultState(), 1);
//				blockState = Blocks.AIR.getDefaultState();
//			else if (m < 75)
////				world.setBlockState(replaceCoords.toPos(), Blocks.SAND.getDefaultState(), 1);
//				blockState = Blocks.SAND.getDefaultState();
//			else if (m < 90)
////				world.setBlockState(replaceCoords.toPos(), Blocks.GRAVEL.getDefaultState(), 1);	
//				blockState = Blocks.GRAVEL.getDefaultState();
//			else if (m < 100)
////				world.setBlockState(replaceCoords.toPos(), Blocks.PLANKS.getDefaultState(), 1);
//				blockState = Blocks.LOG.getDefaultState();
//
//			world.setBlockState(replaceCoords.toPos(), blockState, 3);
//		}
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param coords
//	 * @param surfaceCoords
//	 */
//	public static void fillSimpleShaftWithAir(final World world, final ICoords coords, final ICoords surfaceCoords) {
//		final int ABOVE_CHEST_SIZE = 3; // above chests are always fill with logs, sand, and planks
//		final int BELOW_SURFACE_SIZE = 5; // the number of blocks below the surface
//		for (int i = coords.getY() + (ABOVE_CHEST_SIZE + 1); i <= surfaceCoords.getY() - BELOW_SURFACE_SIZE; i++) {
//			world.setBlockState(new BlockPos(coords.getX(), i, coords.getZ()), Blocks.AIR.getDefaultState(), 3);
//		}
//	}
//
//	/**
//	 * 
//	 * @param world
//	 * @param pos
//	 * @param minDistance
//	 * @return
//	 */
//	public static boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {
//
//		double minDistanceSq = minDistance * minDistance;
//
//		// get a list of chests
//		List<ChestInfo> infos = ChestRegistry.getInstance().getValues();
//
//		if (infos == null || infos.size() == 0) {
//			Treasure.LOGGER
//					.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
//			return false;
//		}
//
//		Treasure.LOGGER.debug("Min distance Sq -> {}", minDistanceSq);
//		for (ChestInfo info : infos) {
//			// calculate the distance to the poi
//			double distance = coords.getDistanceSq(info.getCoords());
//			Treasure.LOGGER.debug("ChestConfig dist^2: " + distance);
//			if (distance < minDistanceSq) {
//				return true;
//			}
//		}
//		return false;
//	}

}