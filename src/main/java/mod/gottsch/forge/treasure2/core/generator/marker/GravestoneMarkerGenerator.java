/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.marker;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureBlock;
import mod.gottsch.forge.treasure2.core.block.SkeletonBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.GravestoneProximitySpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

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
	public GeneratorResult<GeneratorData> generate(IWorldGenContext context, ICoords coords) {
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		// check if markers are enabled
		if (!Config.SERVER.markers.enableMarkers.get()) {
			return result.fail();
		}

		int x = coords.getX();
		int z = coords.getZ();

		// for the number of markers configured
		int numberOfMarkers = RandomHelper.randomInt(
				Config.SERVER.markers.minMarkersPerChest.get(),
				Config.SERVER.markers.minMarkersPerChest.get());

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
			int xSpawn = x + (context.random().nextInt(gridSize) * (context.random().nextInt(3) - 1)); // -1|0|1
			int zSpawn = z + (context.random().nextInt(gridSize) * (context.random().nextInt(3) - 1)); // -1|0|1

			ICoords spawnCoords = new Coords(xSpawn, 0, zSpawn);

			// get a valid surface location
			spawnCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, spawnCoords);
			if (spawnCoords == null || spawnCoords == Coords.EMPTY) {
				Treasure.LOGGER.debug("not a valid surface -> {}", coords);
				continue;
			}

			// don't place if the spawnCoords isn't AIR or REPLACEABLE
			BlockContext cube = new BlockContext(context.level(), spawnCoords);
			if (!cube.isAir() && !cube.isReplaceable()) {
				Treasure.LOGGER.debug("marker not placed because block [{}] is not Air nor Replaceable.",
						spawnCoords.toShortString());
				continue;
			}

			// don't place if the block underneath is of GenericBlock ChestConfig or Container
			Block block = context.level().getBlockState(spawnCoords.add(0, -1, 0).toPos()).getBlock();
			if (block instanceof ITreasureBlock || block instanceof MenuProvider) {
				Treasure.LOGGER.debug("marker not placed because block underneath is a chest, container or Treasure block.");
				continue;
			}

			RegistryObject<Block> marker = null;
			// determine if gravestone spawns an entity
			if (Config.SERVER.markers.enableSpawner.get() && 
					RandomHelper.checkProbability(context.random(), Config.SERVER.markers.spawnerProbability.get())) {
				// grab a random spawner marker
				marker = TreasureBlocks.GRAVESTONE_SPAWNERS.get(context.random().nextInt(TreasureBlocks.GRAVESTONE_SPAWNERS.size()));
			}
			else {
				// grab a random marker
				marker = TreasureBlocks.GRAVESTONES.get(context.random().nextInt(TreasureBlocks.GRAVESTONES.size()));
			}

			Treasure.LOGGER.debug("marker class -> {}", marker.getClass().getSimpleName());
			// select a random facing direction
			Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());

			// place the block
			if (marker.get() instanceof SkeletonBlock) {
				Treasure.LOGGER.debug("should be placing skeleton block -> {}", spawnCoords.toShortString());
				GeneratorUtil.placeSkeleton(context, spawnCoords);
			} else {
				context.level().setBlock(spawnCoords.toPos(), marker.get().defaultBlockState().setValue(AbstractTreasureChestBlock.FACING, facing), 3);
			}

			// update the tile entity if any
			GravestoneProximitySpawnerBlockEntity tileEntity = (GravestoneProximitySpawnerBlockEntity) context.level().getBlockEntity(spawnCoords.toPos());
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