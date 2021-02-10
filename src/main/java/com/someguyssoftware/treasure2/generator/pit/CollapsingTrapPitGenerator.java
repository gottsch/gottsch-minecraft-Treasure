package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


/**
 * 
 * @author Mark Gottschling
 *
 */
public class CollapsingTrapPitGenerator extends AbstractPitGenerator {

	/**
	 * 
	 */
	public CollapsingTrapPitGenerator() {
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = super.generate(world, random, surfaceCoords, spawnCoords);
		if (result.isSuccess()) {
			Treasure.LOGGER.debug("Generated Collapsing Trap Pit at " + spawnCoords.toShortString());
		}
		return result;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	@Override
	public ICoords buildPit(IWorld world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {

		// replace surface and build air shaft
		int minCoordsX = coords.getX() - 2;
		int maxCoordsX = coords.getX() + 2;
		int minCoordsZ = coords.getZ() - 2;
		int maxCoordsZ = coords.getZ() + 2;

		for (int x = minCoordsX; x <= maxCoordsX; x++) {
			for (int z = minCoordsZ; z <= maxCoordsZ; z++ ) {
				ICoords spawnCoords = WorldInfo.getSurfaceCoords(world, new Coords(x, 255, z));
				spawnCoords = spawnCoords.down(1);

				// skip the corners

				BlockState state = world.getBlockState(spawnCoords.toPos());
				if (state.getBlock() == Blocks.GRASS_BLOCK) {	
					if ((x == minCoordsX || x == maxCoordsX) && (z == minCoordsZ || z == maxCoordsZ)) {
					}
					else {
						GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_GRASS.getDefaultState());	
					}
				}
				else if (state.getBlock() == Blocks.SAND)  {
					GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_SAND.getDefaultState());	
				}
				else if (state.getBlock() == Blocks.RED_SAND) {
					GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_RED_SAND.getDefaultState());
				}
				spawnCoords = spawnCoords.down(1);
				while (spawnCoords.getY() >= coords.getY()) {
					GenUtil.replaceWithBlockState(world, spawnCoords,  Blocks.AIR.getDefaultState());
					spawnCoords = spawnCoords.down(1);
				}
			}
		}
		return coords;
	}
}