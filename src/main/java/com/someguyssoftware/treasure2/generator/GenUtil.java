/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.SkeletonBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class GenUtil {
	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.class);
	protected static final int UNDERGROUND_OFFSET = 5;
	protected static final int VERTICAL_MAX_DIFF = 2;
	private static final int FOG_RADIUS = 5;

	/**
	 * convenience method
	 * 
	 * @param offset
	 * @return
	 */
	public static Block getMarkerBlock(StructureMarkers marker) {
		return TreasureTemplateRegistry.getManager().getMarkerMap().get(marker);
	}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public static AbstractTreasureChestTileEntity getChestTileEntity(World world, ICoords coords) {
		BlockPos pos = coords.toPos();

		// get the tile entity
		TileEntity te = (TileEntity) world.getTileEntity(pos);
		if (te == null) {
			Treasure.logger.warn("Unable to locate ChestConfig TileEntity @: " + pos);
			return null;
		}
		// test if te implements ITreasureChestTileEntityOLD
		if (!(te instanceof AbstractTreasureChestTileEntity)) {
			Treasure.logger.warn("ChestConfig TileEntity does not implement TreasureChestTileEntity @: " + pos);
			return null;
		}

		// cast
		return (AbstractTreasureChestTileEntity) te;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	public static boolean replaceWithBlock(World world, ICoords coords, Block block) {
		// don't change if old block is air
		Cube cube = new Cube(world, coords);
		if (cube.isAir())
			return false;
		world.setBlockState(coords.toPos(), block.getDefaultState());
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param blockState
	 * @return
	 */
	public static boolean replaceWithBlockState(World world, ICoords coords, IBlockState blockState) {
		// don't change if old block is air
		Cube cube = new Cube(world, coords);
		if (cube.isAir())
			return false;
		world.setBlockState(coords.toPos(), blockState);
		return true;
	}

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
		IBlockState oldState = world.getBlockState(coords.toPos());

		if (oldState.getProperties().containsKey(FACING)) {
			Treasure.logger.debug("World Chest marker has FACING property:" + oldState.getValue(FACING));
			// set the new state
			return placeChest(world, chest, coords, (EnumFacing) oldState.getValue(FACING));

//			world.setBlockState(pos, this.getChest().getDefaultState().withProperty(FACING, oldState.getValue(FACING)), 3);
		} else {
			Treasure.logger.debug("World Chest marker does NOT have a FACING property.");
//			world.setBlockState(chestCoords.toBlockPos(), this.getChest().getDefaultState(), 3);
			return placeChest(world, chest, coords,
					EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)]);
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
			IBlockState state) {
		if (state.getProperties().containsKey(FACING)) {
			Treasure.logger.debug("Given marker state has FACING property -> {}", state.getValue(FACING));
			return placeChest(world, chest, coords, (EnumFacing) state.getValue(FACING));
		}

		if (state.getBlock() == Blocks.CHEST) {
			Treasure.logger.debug("Given marker state is a vanilla chest.");
			EnumFacing facing = (EnumFacing) state.getValue(BlockChest.FACING);
			return placeChest(world, chest, coords, facing);
		}

		Treasure.logger.debug("Given marker state neither has FACING nor is a vanilla chest.");
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
	public static boolean placeChest(World world, Block chest, ICoords coords, EnumFacing facing) {
		// check if spawn pos is valid
		if (!WorldInfo.isValidY(coords)) {
			Treasure.logger.debug("Cannot place chest due to invalid y pos -> {}", coords.toShortString());
			return false;
		}

		try {
			BlockPos pos = coords.toPos();
			// create and place the chest
			// world.setBlockState(pos, chest.getStateFromMeta(meta), 3);
			world.setBlockState(pos, chest.getDefaultState().withProperty(FACING, facing), 3);
			Treasure.logger.debug("placed chest -> {} into world at coords -> {} with prop -> {}",
					chest.getClass().getSimpleName(), coords.toShortString(), facing);
			// world.setBlockMetadataWithNotify(coords.getX(), coords.getY(), coords.getZ(),
			// meta, 3);

			// get the direction the block is facing.
			Direction direction = Direction.fromFacing(facing);
			((AbstractChestBlock) chest).rotateLockStates(world, pos, Direction.NORTH.getRotation(direction));

			// get the tile entity
			TileEntity te = (TileEntity) world.getTileEntity(pos);

			if (te == null) {
				// remove the chest block
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				Treasure.logger.warn("Unable to create ChestConfig's TileEntity, removing ChestConfig.");
				return false;
			}
			// update the TCTE facing
			((AbstractTreasureChestTileEntity) te).setFacing(facing.getIndex());
		} catch (Exception e) {
			Treasure.logger.error("An error occurred placing chest: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 */
	public static void placeSkeleton(World world, Random random, ICoords coords) {
		// select a random facing direction
		EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
		EnumFacing facing = horizontals[random.nextInt(horizontals.length)];

		// ICoords coords2 = new Coords(coords.offset(facing));
		ICoords coords2 = new Coords(coords.toPos().offset(facing));

		Cube cube = new Cube(world, coords);
		Cube cube2 = new Cube(world, coords2);

//		IBlockState state2 = world.getBlockState(coords2.toPos());
		boolean flag = cube.isReplaceable();
		boolean flag1 = cube2.isReplaceable();
		boolean flag2 = flag || cube.isAir();
		boolean flag3 = flag1 || cube2.isAir();

		// TODO add cube.isSideSolid
		if (flag2 && flag3
				&& world.getBlockState(coords.down(1).toPos()).isSideSolid(world, coords.down(1).toPos(), EnumFacing.UP)
				&& world.getBlockState(coords2.down(1).toPos()).isSideSolid(world, coords2.down(1).toPos(),
						EnumFacing.UP)) {
			IBlockState skeletonState = TreasureBlocks.SKELETON.getDefaultState()
					.withProperty(SkeletonBlock.FACING, facing.getOpposite())
					.withProperty(SkeletonBlock.PART, SkeletonBlock.EnumPartType.BOTTOM);

			world.setBlockState(coords.toPos(), skeletonState, 3);
			world.setBlockState(coords2.toPos(),
					skeletonState.withProperty(SkeletonBlock.PART, SkeletonBlock.EnumPartType.TOP), 3);

			world.notifyNeighborsRespectDebug(coords.toPos(), cube.getState().getBlock(), false);
			world.notifyNeighborsRespectDebug(coords2.toPos(), cube.getState().getBlock(), false);
		}
	}

	/**
	 * Returns a <b>valid</b> pos underground.
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnYMin
	 * @return
	 */
	public static ICoords getUndergroundSpawnCoords(World world, Random random, ICoords surfaceCoords, int spawnYMin) {
		ICoords spawnCoords = null;

		// spawn location under ground
		if (surfaceCoords.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
			int ySpawn = random.nextInt(surfaceCoords.getY() - (spawnYMin + UNDERGROUND_OFFSET)) + spawnYMin;

			spawnCoords = new Coords(surfaceCoords.getX(), ySpawn, surfaceCoords.getZ());
			// get floor pos (if in a cavern or tunnel etc)
			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
		}
		return spawnCoords;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords findUndergroundCeiling(World world, ICoords coords) {
		final int CEILING_FAIL_SAFE = 50;
		int ceilingHeight = 1;

		// find the ceiling of the cavern
		while (world.isAirBlock(coords.toPos())) {
			ceilingHeight++;
			if (ceilingHeight > world.getHeight() || ceilingHeight == CEILING_FAIL_SAFE) {
				return null;
			}
//			coords = coords.add(0, ceilingHeight, 0); 
			coords = coords.add(0, 1, 0);
		}
		// add 1 height to the final pos
//		coords = coords.add(0, ceilingHeight++, 0);		
		coords = coords.add(0, 1, 0);
		return coords;
	}

	/**
	 * This method assumes that the 3 blocks above the chest has been generated and
	 * therefor starts at pos.y + 4
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param surfaceCoords
	 */
	@Deprecated
	public static void fillSimpleShaftRandomly(World world, Random random, ICoords coords, ICoords surfaceCoords) {
		ICoords replaceCoords;

		// randomly fill shaft
		for (int i = coords.getY() + (3 + 1); i <= surfaceCoords.getY() - 5; i++) {
			// if the block to be replaced is air block then skip to the next pos
			replaceCoords = new Coords(coords.getX(), i, coords.getZ());
			if (world.isAirBlock(replaceCoords.toPos())) {
				continue;
			}

			IBlockState blockState = null;
			int m = random.nextInt(100);
			if (m < 50) // 2x as likely to be air
//				world.setBlockState(replaceCoords.toPos(), Blocks.AIR.getDefaultState(), 1);
				blockState = Blocks.AIR.getDefaultState();
			else if (m < 75)
//				world.setBlockState(replaceCoords.toPos(), Blocks.SAND.getDefaultState(), 1);
				blockState = Blocks.SAND.getDefaultState();
			else if (m < 90)
//				world.setBlockState(replaceCoords.toPos(), Blocks.GRAVEL.getDefaultState(), 1);	
				blockState = Blocks.GRAVEL.getDefaultState();
			else if (m < 100)
//				world.setBlockState(replaceCoords.toPos(), Blocks.PLANKS.getDefaultState(), 1);
				blockState = Blocks.LOG.getDefaultState();

			world.setBlockState(replaceCoords.toPos(), blockState, 3);
		}
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param surfaceCoords
	 */
	public static void fillSimpleShaftWithAir(final World world, final ICoords coords, final ICoords surfaceCoords) {
		final int ABOVE_CHEST_SIZE = 3; // above chests are always fill with logs, sand, and planks
		final int BELOW_SURFACE_SIZE = 5; // the number of blocks below the surface
		for (int i = coords.getY() + (ABOVE_CHEST_SIZE + 1); i <= surfaceCoords.getY() - BELOW_SURFACE_SIZE; i++) {
			world.setBlockState(new BlockPos(coords.getX(), i, coords.getZ()), Blocks.AIR.getDefaultState(), 3);
		}
	}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @param minDistance
	 * @return
	 */
	public static boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {

		double minDistanceSq = minDistance * minDistance;

		// get a list of chests
		List<ChestInfo> infos = ChestRegistry.getInstance().getValues();

		if (infos == null || infos.size() == 0) {
			Treasure.logger
					.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
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

}