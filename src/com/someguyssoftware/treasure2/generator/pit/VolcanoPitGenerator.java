package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


/**
 * A Volcano Pit requires at least 25 blocks under the surface to construct.
 * @author Mark Gottschling on Sep 5, 2020
 *
 */
public class VolcanoPitGenerator extends AbstractPitGenerator {
	private static final int MIN_VOLCANO_RADIUS = 3;
    private static final int MAX_VOCANO_RADIUS = 7;
    
	/**
	 * 
	 */
	public LavaTrapPitGenerator() {
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
			Treasure.logger.debug("Generated Volcano Pit at " + spawnCoords.toShortString());
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
		int radius = RandomHelper.randomInt(random, MIN_VOLCANO_RADIUS, MAX_VOLCANO_RADIUS); // min of 3, so diameter = 7 (3*2 + 1 (center)), area = 7x7
        AxisAlignedBB oasisBounds = new AxisAlignedBB(coords.add(-radius, 0, -radius).toPos() , coords.add(radius, 0, radius).toPos());
        
		// select 2/3 point of pit length - topmost coords of volcano chamber / bottom of pit shaft
		int midY = (surfaceCoords.getY() + coords.getY()) / 3 * 2;

		// build lava around base
		buildLavaBaseLayer(world, coords.down(1), radius);
        
        // TODO build at least 5-7 layers at widest
        int layerIndex = 0;
        nextCoords = coords;
        while (layerIndex < 7 && nextCoords.getY() < midY) {
            nextCoords = buildLayer(world, nextCoords, radius, Blocks.AIR);
            layerIndex++;
        }

        // taper in until 2/3 point is reached
        while (nextCoords.getY() < midY && radius > 1) {
            nextCoords = builderLayer(world, nextCoords, radius--, Blocks.AIR);
        }

        // build shaft
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
	}
	
    /**
     * 
     */
    private ICoords buildLayer(World world, ICoords coords, int radius, Block block) {
        // TODO add random obsidian blocks at outer edges
        // TODO add random lava falls (lava blocks at outer edges)
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
                    GenUtil.replaceWithBlock(world, spawnCoords, block);
				}
			}
		}
        return coords.up(1);
    }

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildLavaBaseLayer(World world, ICoords coords, int radius) {
        Treasure.logger.debug("Building lava baselayer from @ {} ", coords.toShortString());

        // for circular chamber
        buildLayer(world, coords, radius, Blocks.LAVA);

        // add the chest
        GenUtil.replaceWithBlock(world, coords, Blocks.STONE);

        // for square chamber
        // for (int z = -3; z <= 3; z++) {
        //     for (int x = -3; x <= 3; x++) {
        //         if (z == 0 && x == 0) {
        //             continue;
        //         }
        //         GenUtil.replaceWithBlock(world, coords.add(x, 0, z), Blocks.LAVA);
        //     }
        // }	
	}
}