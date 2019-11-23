/**
 * 
 */
package com.someguyssoftware.treasure2.generator.well;

import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IWellConfig;
import com.someguyssoftware.treasure2.config.ModConfig;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 18, 2018
 *
 */
public interface IWellGenerator<RESULT extends IGeneratorResult<?>> {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public abstract RESULT generate(World world, Random random, ICoords spawnCoords, IWellConfig config);
	
	/**
	 * Default implementation based on a 3x3 well structure.
	 * @param world
	 * @param random
	 * @param coords
	 */
	public default void addDecoration(World world, Random random, ICoords coords) {
		// coords matrix centered around the input coords
		ICoords[] circle = new Coords[16];
		circle[0] = coords.add(-2, 0, -2);
		circle[1] = coords.add(-1, 0, -2);
		circle[2] = coords.add(0, 0, -2);
		circle[3] = coords.add(1, 0, -2);
		circle[4] = coords.add(2, 0, -2);
		
		circle[5] = coords.add(-2, 0, -1);
		circle[6] = coords.add(2, 0, -1);
		circle[7] = coords.add(-2, 0, 0);
		circle[8] = coords.add(2, 0, 0);
		circle[9] = coords.add(-2, 0, 1);
		circle[10] = coords.add(2, 0, 1);
		
		circle[11] = coords.add(-2, 0, 2);
		circle[12] = coords.add(-1, 0, 2);
		circle[13] = coords.add(0, 0, 2);
		circle[14] = coords.add(1, 0, 2);
		circle[15] = coords.add(2, 0, 2);

		IBlockState blockState = null;
		for (int i = 0; i < 16; i++) {
			if (random.nextInt(2) == 0) {
				// check if the block is dry land
				ICoords markerCoords = WorldInfo.getDryLandSurfaceCoords(world, circle[i]);
				if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
					Treasure.logger.debug("Returning due to marker coords == null or EMPTY_COORDS");
					continue; 
				}
				Cube markerCube = new Cube(world, markerCoords.add(0, -1, 0));
//				Treasure.logger.debug("Marker on block: {}", markerCube.getState());
				if (markerCube.equalsBlock(Blocks.GRASS)) {
					blockState = getDecorationBlockState(world, Blocks.RED_FLOWER);
				}
				else if (markerCube.equalsBlock(Blocks.DIRT)) {
					DirtType dirtType = markerCube.getState().getValue(BlockDirt.VARIANT);
					if (dirtType == DirtType.DIRT) {
						blockState = getDecorationBlockState(world, Blocks.RED_FLOWER);
					}
					else if (dirtType == DirtType.PODZOL) {
//						Treasure.logger.debug("On podzol block");
						Block mushBlock = random.nextInt(2) == 0 ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM;
						blockState = getDecorationBlockState(world, mushBlock);
					}
					else {
//						Treasure.logger.debug("On coarse dirt block");
//						Block grassBlock = Blocks.TALLGRASS;
//						blockState = grassBlock.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.values()[meta]);			
						blockState = getDecorationBlockState(world, Blocks.TALLGRASS);
					}
				}
				else if (markerCube.equalsBlock(Blocks.MYCELIUM)) {
//					Treasure.logger.debug("On mycelium block");
					Block mushBlock = random.nextInt(2) == 0 ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM;
					blockState = getDecorationBlockState(world, mushBlock);					
				}
				else {
//					Treasure.logger.debug("On other block");
					blockState = getDecorationBlockState(world, Blocks.TALLGRASS);
			}				
				// set the block state
				world.setBlockState(circle[i].toPos(), blockState, 3);
//				Treasure.logger.debug("Generating blockstate: {}", blockState);
			}
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param block
	 * @param meta
	 */
	public default IBlockState getDecorationBlockState(World world, Block block) {
		Random random = new Random();
		if (block == Blocks.RED_FLOWER) {
			return getRedFlowerBlockState(world, random, block);
		}
		else if (block == Blocks.BROWN_MUSHROOM || block == Blocks.RED_MUSHROOM) {
			return getMushroomBlockState(world, random, block);
		}
		else if (block == Blocks.TALLGRASS) {
			return getGrassBlockState(world, random, block);
		}
		return block.getDefaultState();
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param block
	 * @return
	 */
	public default IBlockState getRedFlowerBlockState(World world, Random random, Block block) {
		int meta = random.nextInt(8)+1;
		IBlockState blockState =block.getDefaultState().withProperty(((BlockFlower)block).getTypeProperty(), BlockFlower.EnumFlowerType.values()[meta]);
		return blockState;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param block
	 * @return
	 */
	public default IBlockState getMushroomBlockState(World world, Random random, Block block) {
		return block.getDefaultState();
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param block
	 * @return
	 */
	public default IBlockState getGrassBlockState(World world, Random random, Block block) {
		int meta = random.nextInt(3);
		return block.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.values()[meta]);	
	}
}
