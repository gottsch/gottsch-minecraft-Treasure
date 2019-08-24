package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
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
public class LavaTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public LavaTrapPitGenerator() {
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
			Treasure.logger.debug("Generated Lava Trap Pit at " + spawnCoords.toShortString());
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
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// select mid-point of pit length - topmost coords of trap shaft
		int midY = (surfaceCoords.getY() + coords.getY())/2;
//		ICoords midCoords = new Coords(coords.getX(), midY, coords.getZ());
//		int deltaY = surfaceCoords.delta(midCoords).getY();
		
		// build lava around base
		buildLavaBaseLayer(world, coords.down(1));
		
		// build @ chest layer (overwrites what abstract pit gen does
		build3WideLayer(world, random, coords, Blocks.AIR);
		
		// fill shaft will air to mid-point
		for (int yIndex = coords.getY() + OFFSET_Y; yIndex <= midY; yIndex++) {
			nextCoords = build3WideLayer(world, random, new Coords(coords.getX(), yIndex, coords.getZ()), Blocks.AIR);
		}		
		nextCoords = build3WideLayer(world, random, new Coords(coords.getX(), midY+1, coords.getZ()), Blocks.LOG);
		
		for (int yIndex = nextCoords.getY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			Cube cube = new Cube(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}

			// get the next type of block layer to build
			Block block = col.next();
			if (block == Blocks.LOG) {
				// special log build layer
				nextCoords = buildLogLayer(world, random, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}

			// get the expected coords
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 */
	@Override
	public void buildAboveChestLayers(World world, Random random, ICoords spawnCoords) {
		build3WideLayer(world, random, spawnCoords.add(0, 1, 0), Blocks.AIR);
		build3WideLayer(world, random, spawnCoords.add(0, 2, 0), Blocks.AIR);	
		build3WideLayer(world, random, spawnCoords.add(0, 3, 0), Blocks.AIR);	
		build3WideLayer(world, random, spawnCoords.add(0, 4, 0), Blocks.AIR);	
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build3WideLayer(World world, Random random, ICoords coords, Block block) {
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
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildLavaBaseLayer(World world, ICoords coords) {
		Treasure.logger.debug("Building lava baselayer from @ {} ", coords.toShortString());
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, 0), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, -1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(-1, 0, -1), Blocks.LAVA);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, -1), Blocks.LAVA);		
	}
}
