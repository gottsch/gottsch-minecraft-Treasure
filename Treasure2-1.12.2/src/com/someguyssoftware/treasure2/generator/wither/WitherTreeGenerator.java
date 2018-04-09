/**
 * 
 */
package com.someguyssoftware.treasure2.generator.wither;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;
import com.someguyssoftware.treasure2.config.IWitherTreeConfig;
import com.someguyssoftware.treasure2.generator.GenUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public class WitherTreeGenerator {
	public static final int VERTICAL_MAX_DIFF = 3;

	//  branch matrix [0-3][0-3] = [branch index][direction index]
//	static int branchMatrix[][] = {
//				{0, Direction.NORTH.getCode()},
//				{0, Direction.WEST.getCode()},
//				{1, Direction.NORTH.getCode()},
//				{1, Direction.EAST.getCode()},
//				{2, Direction.SOUTH.getCode()},
//				{2, Direction.EAST.getCode()},
//				{3, Direction.SOUTH.getCode()},
//				{3, Direction.WEST.getCode()}
//			};
	
	static List<Direction>[] trunkMatrix = new ArrayList[4];
	
	static {
		trunkMatrix[0] = new ArrayList<>();
		trunkMatrix[1] = new ArrayList<>();
		trunkMatrix[2] = new ArrayList<>();
		trunkMatrix[3] = new ArrayList<>();
		
		trunkMatrix[0].add(Direction.NORTH);
		trunkMatrix[0].add(Direction.WEST);		
		trunkMatrix[1].add(Direction.NORTH);
		trunkMatrix[1].add(Direction.EAST);		
		trunkMatrix[2].add(Direction.SOUTH);
		trunkMatrix[2].add(Direction.WEST);
		trunkMatrix[3].add(Direction.SOUTH);
		trunkMatrix[3].add(Direction.EAST);		

	}
	
	
	/**
	 * 
	 */
	public WitherTreeGenerator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param config
	 * @return
	 */
	public boolean generate(World world, Random random, ICoords coords, IWitherTreeConfig config) {

		ICoords chestCoords = null;
		ICoords surfaceCoords = null;
		ICoords spawnCoords = null;
		boolean isGenerated = false;

		// 1. determine y-coord of land for markers
		surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("Surface Coords @ {}", surfaceCoords.toShortString());
		if (surfaceCoords == null || surfaceCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to surface coords == null or EMPTY_COORDS");
			return false;
		}

		// 2. clear the area
		buildClearing(world, surfaceCoords);

		// 3. build the wither tree
		buildTree(world, random, surfaceCoords, config);

		// TODO determine how many extra "withered" trees to include in the area
		int numTrees = RandomHelper.randomInt(0, config.getMaxSupportingTrees());

		return false;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildClearing(World world, ICoords coords) {
		ICoords buildCoords = null;

		// build clearing
		for (int xOffset = -5; xOffset <= 5; xOffset++) {
			for (int zOffset = -5; zOffset <= 5; zOffset++) {
				if (Math.abs(xOffset) + Math.abs(zOffset) <= 7) {

					/*
					 * replace the ground with wither grass - different shades depending on distance
					 */

					// find the first surface
					int yHeight = WorldInfo.getHeightValue(world, coords.add(xOffset, 255, zOffset));
					// Treasure.logger.debug("Wither Tree Clearing yOffset: " + yHeight);
					// NOTE have to use GenUtil here because it takes into account
					// GenericBlockContainer
					buildCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
					// Treasure.logger.debug("Wither Tree Clearing buildPos: " + buildPos);

					// additional check that it's not a tree and within 2 y-blocks of original
					// TODO replace podzol with wither grass
					if (Math.abs(buildCoords.getY() - coords.getY()) < VERTICAL_MAX_DIFF) {
						world.setBlockState(buildCoords.add(0, -1, 0).toPos(), Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
					}

					Cube cube = new Cube(world, buildCoords);
					ICoords climbCoords = new Coords(buildCoords);
					// remove the tree
					while (cube.equalsBlock(Blocks.LOG) || cube.equalsBlock(Blocks.LOG2)) {
						// remove log
						world.setBlockToAir(climbCoords.toPos());
						// climb upwards
						climbCoords = climbCoords.add(0, 1, 0);
						cube = new Cube(world, climbCoords);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 */
	private void buildTree(World world, Random random, ICoords coords, IWitherTreeConfig config) {
		// build wither tree

		// build the trunk
		buildTrunk(world, random, coords, config);


	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	public void buildTrunk(World world, Random random, ICoords coords, IWitherTreeConfig config) {
		// setup an array of coords
		ICoords[] trunkCoords = new Coords[4];
		trunkCoords[0] = coords;
		trunkCoords[1] = coords.add(1, 0, 0);
		trunkCoords[2] = coords.add(0, 0, 1);
		trunkCoords[3] = coords.add(1, 0, 1);

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, 7, config.getMaxTrunkSize());
		Treasure.logger.debug("maxSize: {}", maxSize);
		
		// build a 2x2 trunk
		for (int trunkIndex = 0; trunkIndex < 4; trunkIndex++) {
			Treasure.logger.debug("Trunk index: {}", trunkIndex);
			Treasure.logger.debug("maxSize: {}", maxSize);
			
			for (int y = 0; y < maxSize; y++) {
				// add the trunk
				world.setBlockState(trunkCoords[trunkIndex].add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG.getDefaultState());
				 Treasure.logger.debug("Wither Tree building trunk @ " +  trunkCoords[trunkIndex].add(0, y, 0).toShortString());
				 
				 // add the branch(es)
				 if (y ==0) {
					 addRoot(world, random, trunkCoords[trunkIndex], trunkMatrix[trunkIndex]);
				 }
				 else if (y > 3) {
					 addBranch(world, random, trunkCoords[trunkIndex], y, maxSize, trunkMatrix[trunkIndex]);
				 }
			}

			// TODO add the heart block
			
			
			

			// set the new max size
			if (maxSize > 3) {
				maxSize -= RandomHelper.randomInt(random, 1, 3);
				maxSize = Math.max(3, maxSize);
			}
		}

	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param iCoords
	 * @param list
	 */
	private void addRoot(World world, Random random, ICoords coords, List<Direction> directions) {
		// for each direction
		for (Direction d : directions) {
			Cube groundCube = new Cube(world, coords.down(1));
			if (groundCube.isSolid() && groundCube.isTopSolid()) {
				if (RandomHelper.checkProbability(random, 50)) {
					// update the coords to the correct position
					ICoords c = coords.add(d, 1);
					// rotate the branch in the right direction
					IBlockState state = TreasureBlocks.WITHER_ROOT.getDefaultState().withProperty(WitherRootBlock.FACING, d.toFacing());
					
					// add the branch to the world
					world.setBlockState(c.toPos(), state);
					 Treasure.logger.debug("Wither Tree building root @ " +  coords.toShortString());					
				}
			}
		}		
	}
	

	/**
	 * 
	 * @param trunkIndex
	 * @param y
	 * @param maxSize
	 * @param is
	 */
	private void addBranch(World world, Random random, ICoords  trunkCoords, int y, int maxSize, List<Direction> directions) {
		int branchSize = (y <= (maxSize/2)) ? 2 : 1;
		
		// for each direction
		for (Direction d : directions) {
			// randomize if a branch is generated
			if (RandomHelper.checkProbability(random, 20)) {
				// for the num of branch segments
				ICoords c = trunkCoords;
				for (int segment = 0; segment < branchSize; segment++) {
					c = c.add(d, 1);
					
					// rotate the branch in the right direction
					IBlockState state = TreasureBlocks.WITHER_BRANCH.getDefaultState().withProperty(WitherBranchBlock.FACING, d.toFacing());
					
					// add the branch to the world
					world.setBlockState(c.add(0, y, 0).toPos(), state);
					 Treasure.logger.debug("Wither Tree building branch @ " +  c.add(0, y, 0).toShortString());
				}
			}
		}
	}
}
