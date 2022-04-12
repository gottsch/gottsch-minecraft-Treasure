package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;
import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.sun.media.jfxmedia.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;


/**
 * A Volcano Pit requires at least 15 blocks under the surface to construct.
 * @author Mark Gottschling on Sep 5, 2020
 *
 */
public class VolcanoPitGenerator extends AbstractPitGenerator {
	private static final int MIN_VOLCANO_RADIUS = 4;
    private static final int MAX_VOLCANO_RADIUS = 8;
    private static final int MIN_VERTICAL_DISTANCE = 15;
    
	/**
	 * 
	 */
	public VolcanoPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25, Blocks.SAND);
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
    @Override
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = super.generate(world, random, surfaceCoords, spawnCoords);
		if (result.isSuccess()) {
			LOGGER.debug("Generated Volcano Pit at " + spawnCoords.toShortString());
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
        
        // determine size of volcano
		int radius = RandomHelper.randomInt(random, MIN_VOLCANO_RADIUS, MAX_VOLCANO_RADIUS); // min of 4, so diameter = 9 (4*2 + 1 (center)), area = 9x9
        
		// select 2/3 point of pit length - topmost coords of volcano chamber / bottom of pit shaft
		int shaftStartY = coords.getY() + ((surfaceCoords.getY() - coords.getY()) / 3 * 2);

		// build lava around base
		buildLavaBaseLayer(world, coords.down(1), radius);

        nextCoords = coords;
        while (nextCoords.getY() < (shaftStartY - 4)) {
            nextCoords = buildLayer(world, nextCoords, radius, Blocks.AIR, true);
        }

        // taper in until 2/3 point is reached
        while (nextCoords.getY() < shaftStartY && radius > 1) {
            nextCoords = buildLayer(world, nextCoords, radius--, Blocks.AIR, false);
        }

        // build one layer of logs
        nextCoords = buildLogLayer(world, random, nextCoords, Blocks.LOG);
        
        // build shaft
		for (int yIndex = nextCoords.getY() + 1; yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
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
		
	}
	
    /**
     * 
     */
    private ICoords buildLayer(World world, ICoords coords, int radius, Block block, boolean addDecorations) {
		int radiusSquared = radius * radius;
		Integer[] distancesMet = new Integer[radius + 1];
		ICoords spawnCoords = null;
		for (int xOffset = -(radius); xOffset <= radius; xOffset++) {
			for (int zOffset = -(radius); zOffset <= radius; zOffset++) {
				boolean isDistanceMet = false;
				spawnCoords = coords.add(xOffset, 0, zOffset);
				if (distancesMet[Math.abs(xOffset)] != null) {
					if (Math.abs(zOffset) <= distancesMet[Math.abs(xOffset)]) {
						isDistanceMet = true;
					}
				}
				else {
					if (coords.getDistanceSq(spawnCoords) < radiusSquared) {
						distancesMet[Math.abs(xOffset)] = Math.abs(zOffset);
						isDistanceMet = true;
					}
				}

				if (isDistanceMet) {
					Random random = new Random();
                    GenUtil.replaceWithBlock(world, spawnCoords, block);
                    
                    if (addDecorations) {
	                    if (xOffset < 0) {
	                        ICoords replaceCoords = spawnCoords.west(1);
	                        addDecorations(world, random, replaceCoords);
	                    }
	                    else if (xOffset > 0) {
	                        ICoords replaceCoords = spawnCoords.east(1);
	                        addDecorations(world, random, replaceCoords);
	                    }
	
	                    if (zOffset < 0) {
	                        ICoords replaceCoords = spawnCoords.north(1);
	                        addDecorations(world, random, replaceCoords);
	                    }
	                    else if (zOffset > 0) {
	                        ICoords replaceCoords = spawnCoords.south(1);
	                        addDecorations(world, random, replaceCoords);
	                    }
                    }
                }
			}
        }
        
        return coords.up(1);
    }

    /**
     * 
     */
    private void addDecorations(World world, Random random, ICoords coords) {
        if (world.getBlockState(coords.toPos()).getBlock() != Blocks.AIR) {
            if (RandomHelper.checkProbability(random, 30)) {
                world.setBlockState(coords.toPos(), TreasureBlocks.BLACKSTONE.getDefaultState());
            }
            else if (RandomHelper.checkProbability(random, 10)) {
            	world.setBlockState(coords.toPos(), Blocks.LAVA.getDefaultState());
            }
        }
    }

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildLavaBaseLayer(World world, ICoords coords, int radius) {
        LOGGER.debug("Building lava baselayer from @ {} ", coords.toShortString());

        // for circular chamber
        buildLayer(world, coords, radius, Blocks.LAVA, false);

        // add the chest
        GenUtil.replaceWithBlock(world, coords, Blocks.STONE);	
	}
	
	@Override
	public int getMinSurfaceToSpawnDistance() {
		return MIN_VERTICAL_DISTANCE;
	}
}