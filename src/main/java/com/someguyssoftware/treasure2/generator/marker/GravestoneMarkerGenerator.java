/**
 * 
 */
package com.someguyssoftware.treasure2.generator.marker;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
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
import net.minecraft.block.BlockContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 27, 2019
 *
 */
public class GravestoneMarkerGenerator implements IMarkerGenerator<GeneratorResult<GeneratorData>> {

	/**
	 * 
	 */
	public GravestoneMarkerGenerator() {
	}

	/**
	 * 
	 */
	@Override
	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords) {
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// check if gravestones are enabled
		if (!TreasureConfig.WORLD_GEN.getMarkerProperties().isGravestonesAllowed) {
			return result.fail();
		}

		int x = coords.getX();
		int z = coords.getZ();

		// for the number of markers configured
		int numberOfMarkers = RandomHelper.randomInt(
				TreasureConfig.WORLD_GEN.getMarkerProperties().minGravestonesPerChest,
				TreasureConfig.WORLD_GEN.getMarkerProperties().maxGravestonesPerChest);
		// calculate the grid size
		int gridSize = 4;
		if (numberOfMarkers < 6) {
			/* default */ 
		} else if (numberOfMarkers >= 6 && numberOfMarkers <= 8) {
			gridSize = 5;
		} else {
			gridSize = 6;
		}

		// record the first valid spawn coords
		ICoords markerCoords = null;

		// loop through each marker
		for (int i = 0; i < numberOfMarkers; i++) {

			// generator random x, z
			int xSpawn = x + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1
			int zSpawn = z + (random.nextInt(gridSize) * (random.nextInt(3) - 1)); // -1|0|1

			// get the "surface" y
			int ySpawn = WorldInfo.getHeightValue(world, new Coords(xSpawn, 0, zSpawn));
			ICoords spawnCoords = new Coords(xSpawn, ySpawn, zSpawn);

			// determine if valid y
			if (!WorldInfo.isValidY(spawnCoords)) {
				LOGGER.debug(String.format("[%d] is not a valid y value.", spawnCoords.getY()));
				continue;
			}

			// get a valid surface location
			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
			if (spawnCoords == null) {
				LOGGER.debug(String.format("Not a valid surface @ %s", coords));
				continue;
			}

			// don't place if the spawnCoords isn't AIR or FOG or REPLACEABLE
			Cube cube = new Cube(world, spawnCoords);
			if (!cube.isAir() && !cube.isReplaceable()) {
				LOGGER.debug("Marker not placed because block  @ [{}] is not Air, Replaceable.",
						spawnCoords.toShortString());
				continue;
			}

			// don't place if the block underneath is of GenericBlock ChestConfig or Container
			Block block = world.getBlockState(spawnCoords.add(0, -1, 0).toPos()).getBlock();
			if (block instanceof BlockContainer || block instanceof AbstractModContainerBlock
					|| block instanceof ITreasureBlock) {
				LOGGER
						.debug("Marker not placed because block underneath is a chest, container or Treasure block.");
				continue;
			}

			Block marker = null;
			// determine if gravestone spawns an entity
			if (TreasureConfig.WORLD_GEN.markerProperties.isGravestoneSpawnMobAllowed && RandomHelper
					.checkProbability(random, TreasureConfig.WORLD_GEN.markerProperties.gravestoneMobProbability)) {
				// grab a random spawner marker
				marker = TreasureBlocks.gravestoneSpawners.get(random.nextInt(TreasureBlocks.gravestoneSpawners.size()));
			}
			else {
				// grab a random marker
				marker = TreasureBlocks.gravestones.get(random.nextInt(TreasureBlocks.gravestones.size()));
			}

			LOGGER.debug("marker class -> {}", marker.getClass().getSimpleName());
			// select a random facing direction
			EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
			EnumFacing facing = horizontals[random.nextInt(horizontals.length)];

			// place the block
			if (marker instanceof SkeletonBlock) {
				LOGGER.debug("should be placing skeleton block -> {}", spawnCoords.toShortString());
				GenUtil.placeSkeleton(world, random, spawnCoords);
			} else {
				world.setBlockState(spawnCoords.toPos(), marker.getDefaultState().withProperty(AbstractChestBlock.FACING, facing));
			}

			// update the tile entity if any
			GravestoneProximitySpawnerTileEntity tileEntity = (GravestoneProximitySpawnerTileEntity) world.getTileEntity(spawnCoords.toPos());
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