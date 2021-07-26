/**
 * 
 */
package com.someguyssoftware.treasure2.generator.marker;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.block.ModContainerBlock;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureBlock;
import com.someguyssoftware.treasure2.block.SkeletonBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.util.Direction;
import net.minecraft.world.IServerWorld;

/**
 * @author Mark Gottschling on Jan 27, 2019
 *
 */
public class GravestoneMarkerGenerator implements IMarkerGenerator<GeneratorResult<GeneratorData>> {
	private static final int SMALL_GRID = 4;
	private static final int MEDIUM_GRID = 5;
	private static final int LARGE_GRID = 6;
	private static final int LARGE_GRID_THRESHOLD = 8;
	private static final int MEDIUM_GRID_THRESHOLD = 5;
	
	/**
	 * 
	 */
	public GravestoneMarkerGenerator() {
	}

	/**
	 * 
	 */
	@Override
	public GeneratorResult<GeneratorData> generate(IServerWorld world, Random random, ICoords coords) {
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// check if markers are enabled
		if (!TreasureConfig.MARKERS.markersAllowed.get()) {
			return result.fail();
		}

		int x = coords.getX();
		int z = coords.getZ();

		// for the number of markers configured
		int numberOfMarkers = RandomHelper.randomInt(
				TreasureConfig.MARKERS.minGravestonesPerChest.get(),
				TreasureConfig.MARKERS.maxGravestonesPerChest.get());

		// calculate the grid size
		int gridSize = SMALL_GRID;
		if (numberOfMarkers > LARGE_GRID_THRESHOLD) {
			gridSize = LARGE_GRID;
		}
		else if (numberOfMarkers > MEDIUM_GRID_THRESHOLD) {
			gridSize = MEDIUM_GRID;
		}
		
		// record the first valid spawn coords
		ICoords markerCoords = null;

		// loop through each marker
		for (int i = 0; i < numberOfMarkers; i++) {

			// generator random x, z
			int xSpawn = x + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1
			int zSpawn = z + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1

			// get the "surface" y
			int ySpawn = WorldInfo.getHeight(world, new Coords(xSpawn, 0, zSpawn));
			ICoords spawnCoords = new Coords(xSpawn, ySpawn, zSpawn);

			// determine if valid y
			if (!WorldInfo.isValidY(spawnCoords)) {
				LOGGER.debug(String.format("[%d] is not a valid y value.", spawnCoords.getY()));
				continue;
			}

			// get a valid surface location
			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
			if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
				LOGGER.debug(String.format("Not a valid surface @ %s", coords));
				continue;
			}

			// don't place if the spawnCoords isn't AIR or FOG or REPLACEABLE
			BlockContext cube = new BlockContext(world, spawnCoords);
			if (!cube.isAir() && !cube.isReplaceable()) {
				LOGGER.debug("Marker not placed because block  @ [{}] is not Air nor Replaceable.",
						spawnCoords.toShortString());
				continue;
			}

			// don't place if the block underneath is of GenericBlock ChestConfig or Container
			Block block = world.getBlockState(spawnCoords.add(0, -1, 0).toPos()).getBlock();
			if (block instanceof ITreasureBlock || block instanceof ModContainerBlock || block instanceof ContainerBlock) {
				LOGGER.debug("Marker not placed because block underneath is a chest, container or Treasure block.");
				continue;
			}

			Block marker = null;
			// determine if gravestone spawns an entity
			if (TreasureConfig.MARKERS.gravestoneSpawnMobAllowed.get() && 
					RandomHelper.checkProbability(random, TreasureConfig.MARKERS.gravestoneMobProbability.get())) {
				// grab a random spawner marker
				marker = TreasureBlocks.GRAVESTONE_SPAWNERS.get(random.nextInt(TreasureBlocks.GRAVESTONE_SPAWNERS.size()));
			}
			else {
				// grab a random marker
				marker = TreasureBlocks.GRAVESTONES.get(random.nextInt(TreasureBlocks.GRAVESTONES.size()));
			}

			LOGGER.debug("marker class -> {}", marker.getClass().getSimpleName());
			// select a random facing direction
			Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);

			// place the block
			if (marker instanceof SkeletonBlock) {
				LOGGER.debug("should be placing skeleton block -> {}", spawnCoords.toShortString());
				GenUtil.placeSkeleton(world, random, spawnCoords);
			} else {
				world.setBlock(spawnCoords.toPos(), marker.defaultBlockState().setValue(AbstractChestBlock.FACING, facing), 3);
			}

			// update the tile entity if any
			GravestoneProximitySpawnerTileEntity tileEntity = (GravestoneProximitySpawnerTileEntity) world.getBlockEntity(spawnCoords.toPos());
			if (tileEntity != null) {
				tileEntity.setHasEntity(true);
			}

			// record the first valid spawn coords
			if (markerCoords == null) {
				markerCoords = spawnCoords;
			}
		} // end of for

		result.getData().setSpawnCoords(markerCoords);
		return result.success();
	}
}