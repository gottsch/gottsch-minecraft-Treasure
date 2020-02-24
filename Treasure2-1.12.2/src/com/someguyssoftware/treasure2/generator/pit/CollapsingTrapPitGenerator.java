package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
		// TODO remove all
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25,  Blocks.SAND);
		getBlockLayers().add(15, Blocks.COBBLESTONE);
		getBlockLayers().add(15, Blocks.GRAVEL);
		getBlockLayers().add(10, Blocks.LOG);
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
			Treasure.logger.debug("Generated Collapsing Trap Pit at " + spawnCoords.toShortString());
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
	public ICoords buildPit(World world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {

		// replace surface and build air shaft
		int minCoordsX = coords.getX() - 2;
		int maxCoordsX = coords.getX() +2;
		int minCoordsZ = coords.getZ() - 2;
		int maxCoordsZ = coords.getZ() + 2;
		
		for (int x = minCoordsX; x <= maxCoordsX; x++) {
			for (int z = minCoordsZ; z <= maxCoordsZ; z++ ) {
				ICoords spawnCoords = WorldInfo.getSurfaceCoords(world, new Coords(x, 255, z));
				spawnCoords = spawnCoords.down(1);
				
				if (world.getBlockState(spawnCoords.toPos()).getBlock() == Blocks.GRASS) {
					// skip the corners
					if ((x == minCoordsX || x == maxCoordsX) && (z == minCoordsZ || z == maxCoordsZ)) {
					}
					else {
						GenUtil.replaceWithBlockState(world, spawnCoords, TreasureBlocks.FALLING_GRASS.getDefaultState());
					}
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
	
	/**
	 * TODO could make this into a generic method in abstract buildXWideLayer
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build5WideLayer(World world, Random random, ICoords coords, Block block) {
//		Treasure.logger.debug("Building 3 wide layer from {} @ {} ", block.getUnlocalizedName(), coords.toShortString());
		IBlockState blockState = block.getDefaultState();
		if (block == Blocks.LOG || block == Blocks.LOG2) {
			int meta = random.nextInt() % 2 == 0 ? 8 : 4;
			blockState = block.getStateFromMeta(meta);
		}
		
		GenUtil.replaceWithBlockState(world, coords, blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, -1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(-1, 0, -1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, -1), blockState);	
		
		for (int x = coords.getX() - 2; x <= coords.getX() + 2; x++) {
			for (int z = coords.getZ() -2; z <=coords.getZ() + 2; z++ ) {
				// fill with AIR
				GenUtil.replaceWithBlockState(world, coords.add(x, 0, z), blockState);	
			}
		}
		return coords.add(0, 1, 0);
	}
}
