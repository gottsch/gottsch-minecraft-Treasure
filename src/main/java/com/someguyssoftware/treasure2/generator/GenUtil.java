/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.SkeletonBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class GenUtil {
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
	
	/**
	 * convenience method
	 * 
	 * @param offset
	 * @return
	 */
	public static Block getMarkerBlock(StructureMarkers marker) {
		return TreasureTemplateRegistry.getManager().getMarkerMap().get(marker);
	}
	
//	TODO 1.15.2
//	protected static final int UNDERGROUND_OFFSET = 5;
//	protected static final int VERTICAL_MAX_DIFF = 2;
//	private static final int FOG_RADIUS = 5;



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

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	public static boolean replaceWithBlock(IWorld world, ICoords coords, Block block) {
		// don't change if old block is air
		BlockContext context = new BlockContext(world, coords);
		if (context.isAir()) {
			return false;
		}
		world.setBlock(coords.toPos(), block.defaultBlockState(), 3);
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param blockState
	 * @return
	 */
	public static boolean replaceWithBlockState(IWorld world, ICoords coords, BlockState blockState) {
		// don't change if old block is air
//		Treasure.LOGGER.debug("getting block context...");
		BlockContext context = new BlockContext(world, coords);
//		Treasure.LOGGER.debug("block context -> {}", context);
		if (context.isAir()) {
//			Treasure.LOGGER.debug("is air");
			return false;
		}
//		Treasure.LOGGER.debug("about to change state in world -> {}", world.getWorldInfo());
		world.setBlock(coords.toPos(), blockState, 3);
//		Treasure.LOGGER.debug("updated the state.");
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
	public static boolean replaceBlockWithChest(IWorld world, Random random, Block chest, ICoords coords) {
		// get the old state
		BlockState oldState = world.getBlockState(coords.toPos());

		if (oldState.getProperties().contains(FACING)) {
			Treasure.LOGGER.debug("World Chest marker has FACING property:" + oldState.getValue(FACING));
			// set the new state
			return placeChest(world, chest, coords, (Direction) oldState.getValue(FACING));

		} else {
			Treasure.LOGGER.debug("World Chest marker does NOT have a FACING property.");
			return placeChest(world, chest, coords, Direction.from2DDataValue(random.nextInt(4)));
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
	public static boolean replaceBlockWithChest(IWorld world, Random random, ICoords coords, Block chest,
			BlockState state) {
		if (state.getProperties().contains(FACING)) {
			Treasure.LOGGER.debug("Given marker state has FACING property -> {}", state.getValue(FACING));
			return placeChest(world, chest, coords, (Direction) state.getValue(FACING));
		}

		if (state.getBlock() == Blocks.CHEST) {
			Treasure.LOGGER.debug("Given marker state is a vanilla chest.");
			Direction direction = (Direction) state.getValue(ChestBlock.FACING);
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
	public static boolean placeChest(IWorld world, Block chest, ICoords coords, Direction direction) {
		// TODO wrap all Direction in Heading
		// check if spawn pos is valid
		if (!WorldInfo.isValidY(coords)) {
			Treasure.LOGGER.debug("Cannot place chest due to invalid y pos -> {}", coords.toShortString());
			return false;
		}

		try {
			BlockPos pos = coords.toPos();
			// create and place the chest
			WorldInfo.setBlock(world, coords, chest.defaultBlockState().setValue(FACING, direction));
//			world.setBlock(pos, chest.defaultBlockState().setValue(FACING, direction), 3);
			Treasure.LOGGER.debug("placed chest -> {} into world at coords -> {} with prop -> {}",
					chest.getClass().getSimpleName(), coords.toShortString(), direction);

			// get the direction the block is facing.
			Heading heading = Heading.fromDirection(direction);
			((AbstractChestBlock) chest).rotateLockStates(world, pos, Heading.NORTH.getRotation(heading));

			// get the tile entity
			TileEntity tileEntity = (TileEntity) world.getBlockEntity(pos); // TODO make wrapper in WorldInfo

			if (tileEntity == null) {
				// remove the chest block
				WorldInfo.setBlock(world, coords, Blocks.AIR.defaultBlockState());
//				world.setBlockState(pos, Blocks.AIR.defaultBlockState(), 3);
				Treasure.LOGGER.warn("Unable to create ChestConfig's TileEntity, removing ChestConfig.");
				return false;
			}
			// update the TCTE facing
			((AbstractTreasureChestTileEntity) tileEntity).setFacing(direction.get3DDataValue());
		} catch (Exception e) {
			Treasure.LOGGER.error("An error occurred placing chest: ", e);
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
	public static void placeSkeleton(IWorld world, Random random, ICoords coords) {
		// select a random facing direction
//		Direction[] horizontals = EnumFacing.HORIZONTALS;
//		EnumFacing facing = horizontals[random.nextInt(horizontals.length)];
		Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
		ICoords coords2 = new Coords(coords.toPos().relative(facing));

		BlockContext blockContext = new BlockContext(world, coords);
		BlockContext blockContext2 = new BlockContext(world, coords2);

		boolean flag = blockContext.isReplaceable();
		boolean flag1 = blockContext2.isReplaceable();
		boolean flag2 = flag || blockContext.isAir();
		boolean flag3 = flag1 || blockContext2.isAir();

		// TODO add cube.isSideSolid
		if (flag2 && flag3
				&& world.getBlockState(coords.down(1).toPos()).isSolidRender(world, coords.down(1).toPos())
				&& world.getBlockState(coords2.down(1).toPos()).isSolidRender(world, coords2.down(1).toPos())) {
			BlockState skeletonState = TreasureBlocks.SKELETON.defaultBlockState()
					.setValue(SkeletonBlock.FACING, facing.getOpposite())
					.setValue(SkeletonBlock.PART, SkeletonBlock.EnumPartType.BOTTOM);

			world.setBlock(coords.toPos(), skeletonState, 3);
			world.setBlock(coords2.toPos(),
					skeletonState.setValue(SkeletonBlock.PART, SkeletonBlock.EnumPartType.TOP), 3);

			world.blockUpdated(coords.toPos(), blockContext.getState().getBlock());
			world.blockUpdated(coords2.toPos(), blockContext.getState().getBlock());
		}
	}


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

	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords findUndergroundCeiling(IWorld world, ICoords coords) {
		final int CEILING_FAIL_SAFE = 50;
		int ceilingHeight = 1;

		// find the ceiling of the cavern
		while (world.isEmptyBlock(coords.toPos())) { // TODO add wrapper to WorldInfo
			ceilingHeight++;
			if (ceilingHeight > world.getHeight() || ceilingHeight == CEILING_FAIL_SAFE) {
				return null;
			}
			coords = coords.add(0, 1, 0);
		}
		// add 1 height to the final pos
		coords = coords.add(0, 1, 0);
		return coords;
	}
}