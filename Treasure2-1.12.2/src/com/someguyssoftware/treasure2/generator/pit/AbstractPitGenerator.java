package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Mar 7, 2018
 *
 */
public abstract class AbstractPitGenerator implements IPitGenerator {

	protected static final int Y_OFFSET = 5;
	protected static final int Y_SURFACE_OFFSET = 6;

	private RandomWeightedCollection<Block> blockLayers = new RandomWeightedCollection<>();
	
	/**
	 * 
	 */
	public AbstractPitGenerator() {
		super();
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	@Override
	public boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		IBlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.logger.debug("Spawn coords is in cavern.");
			inCavern = true;
		}
		
		if (inCavern) {
			Treasure.logger.debug("Shaft is in cavern... finding ceiling.");
			spawnCoords = GenUtil.findUndergroundCeiling(world, spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.logger.warn("Exiting: Unable to locate cavern ceiling.");
				return false;
			}
		}
	
		// generate shaft
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.logger.debug("Distance to ySurface =" + yDist);
	
		if (yDist > 6) {			
			Treasure.logger.debug("Generating shaft @ " + spawnCoords.toShortString());
			// at chest level
			buildLayer(world, spawnCoords, Blocks.AIR);
			
			// above the chest
			buildAboveChestLayers(world, random, spawnCoords);

			// shaft enterance
			buildLogLayer(world, random, surfaceCoords.add(0, -3, 0), Blocks.LOG);
			buildLayer(world, surfaceCoords.add(0, -4, 0), Blocks.SAND);
			buildLogLayer(world, random, surfaceCoords.add(0, -5, 0), Blocks.LOG);

			// build the pit
			buildPit(world, random, spawnCoords, surfaceCoords, getBlockLayers());
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}		
//		Treasure.logger.debug("Generated Pit at " + spawnCoords.toShortString());
		return true;
	}	
	
	/**
	 * 
	 * @param world
	 * @param spawnCoords
	 */
	public void buildAboveChestLayers(World world, Random random, ICoords spawnCoords) {
		buildLayer(world, spawnCoords.add(0, 1, 0), Blocks.AIR);
		buildLayer(world, spawnCoords.add(0, 2, 0), Blocks.AIR);
		buildLogLayer(world, random, spawnCoords.add(0, 3, 0), Blocks.LOG);
		buildLayer(world, spawnCoords.add(0, 4, 0), Blocks.SAND);
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	public ICoords buildPit(World world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + Y_OFFSET; yIndex <= surfaceCoords.getY() - Y_SURFACE_OFFSET; yIndex++) {
			
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
				// ie. LayerBuilder.build(world, coords, block)
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 * @param index
	 * @param coords
	 * @param expectedCoords
	 * @return
	 */
	protected int autoCorrectIndex(final int index, final ICoords coords, final ICoords expectedCoords) {
		int newIndex = index;
		if (!coords.equals(expectedCoords)) {
			// find the difference in y int and add to yIndex;
			Treasure.logger.debug("Next coords does not equal expected coords. next: {}; expected: {}", coords.toShortString(), expectedCoords.toShortString());
			// NOTE the difference should = 1, there remove 1 from the diff to find unexpected difference
			int diff = coords.getY() - expectedCoords.getY() - 1;
			if (diff > 0) {
				newIndex = coords.getY();
				Treasure.logger.debug("Difference of: {}. Updating yIndex to {}", diff, newIndex);
			}
		}
		return newIndex;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLayer(World world, ICoords coords, Block block) {
//		Treasure.logger.debug("Building layer from {} @ {} ", block.getLocalizedName(), coords.toShortString());
		GenUtil.replaceWithBlock(world, coords, block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), block);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), block);
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLogLayer(final World world, final Random random, final ICoords coords, final Block block) {
//		Treasure.logger.debug("Building log layer from {} @ {} ", block.getLocalizedName(), coords.toShortString());
		// ensure that block is of type LOG/LOG2
		if (block != Blocks.LOG && block != Blocks.LOG2) return coords;
		
		 // determine the direction the logs are facing - north/south (8) or east/west (4)
		int meta = random.nextInt() % 2 == 0 ? 8 : 4;
		IBlockState blockState = block.getStateFromMeta(meta);
				
		// core 4-square
		GenUtil.replaceWithBlockState(world, coords, blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 1), blockState);
		
		if (meta == 8) {			
			// north of
			GenUtil.replaceWithBlockState(world, coords.add(0, 0, -1), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(1, 0, -1), blockState);
			
			// south of
			GenUtil.replaceWithBlockState(world, coords.add(0, 0, 2), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(1, 0, 2), blockState);
		}
		else {
			// west of
			GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 0), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 1), blockState);
			// east of 
			GenUtil.replaceWithBlockState(world, coords.add(2, 0, 0), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(2, 0, 1), blockState);
		}
		return coords.add(0, 1, 0);
	}
	
	/**
	 * @return the blockLayers
	 */
	public RandomWeightedCollection<Block> getBlockLayers() {
		return blockLayers;
	}

	/**
	 * @param blockLayers the blockLayers to set
	 */
	public void setBlockLayers(RandomWeightedCollection<Block> blockLayers) {
		this.blockLayers = blockLayers;
	}
}