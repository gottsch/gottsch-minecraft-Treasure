/**
 * 
 */
package com.someguyssoftware.treasure2.generator.wither;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.FogBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.WitherBranchBlock;
import com.someguyssoftware.treasure2.block.WitherLogSoulBlock;
import com.someguyssoftware.treasure2.block.WitherRootBlock;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.config.IWitherTreeConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.chest.WitherChestGenerator;

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
	private static final int CLEARING_RADIUS = 3;

	FogBlock[] fogDensity = new FogBlock[] { 
			TreasureBlocks.WITHER_FOG, 
			TreasureBlocks.WITHER_FOG,
			TreasureBlocks.HIGH_WITHER_FOG, 
			TreasureBlocks.HIGH_WITHER_FOG, 
			TreasureBlocks.MED_WITHER_FOG,
			TreasureBlocks.MED_WITHER_FOG,
			TreasureBlocks.LOW_WITHER_FOG
			};
	
	FogBlock[] poisonFogDensity = new FogBlock[] {
		TreasureBlocks.POISON_FOG,
		TreasureBlocks.POISON_FOG,
		TreasureBlocks.HIGH_POISON_FOG,
		TreasureBlocks.HIGH_POISON_FOG, 
		TreasureBlocks.MED_POISON_FOG,
		TreasureBlocks.MED_POISON_FOG,
		TreasureBlocks.LOW_POISON_FOG
	};
	
	static List<Direction>[] trunkMatrix = new ArrayList[4];
	static List<Direction> supportTrunkMatrix = new ArrayList<>();
	static List<Direction> topMatrix = new ArrayList<>();
	
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
		
		supportTrunkMatrix.add(Direction.NORTH);
		supportTrunkMatrix.add(Direction.EAST);
		supportTrunkMatrix.add(Direction.SOUTH);
		supportTrunkMatrix.add(Direction.WEST);
		
		topMatrix.add(Direction.EAST);
		topMatrix.add(Direction.SOUTH);
		topMatrix.add(null);
		topMatrix.add(null);
	}
	
	
	/**
	 * 
	 */
	public WitherTreeGenerator() {
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

		ICoords surfaceCoords = null;
		ICoords witherTreeCoords = null;

		// 1. determine y-coord of land for markers
		surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("Surface Coords @ {}", surfaceCoords.toShortString());
		if (surfaceCoords == null || surfaceCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to surface coords == null or EMPTY_COORDS");
			return false;
		}

		// 2. clear the area
		buildClearing(world, random, surfaceCoords);

		// 3. build the main wither tree
		buildTrunk(world, random, surfaceCoords, config);

		// 4. add the fog
		if (TreasureConfig.enableWitherFog) {
			GenUtil.addFog(world, random, surfaceCoords, fogDensity);
		}
		witherTreeCoords = surfaceCoords;
		
		// determine how many extra "withered" trees to include in the area
		int numTrees = RandomHelper.randomInt(config.getMinSupportingTrees(), config.getMaxSupportingTrees());
//		Treasure.logger.debug("number of witherED trees to gen: {}", numTrees);
		
		for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
			// find a random location around a radius from the tree
			// ie. rand x-radius, rand z-radius = new point (+x,+z), rand degrees of rotation from origin
			double xlen = RandomHelper.randomDouble(5, 10);
			double zlen = RandomHelper.randomDouble(5, 10);
			int degrees = RandomHelper.randomInt(0, 360);
			
			ICoords c = witherTreeCoords.rotate(xlen, zlen, degrees);
//			Treasure.logger.debug("Rotating by x{}, z{}, deg{} to {}", xlen, zlen, degrees, c);
			
			// get the yspawn
			c = WorldInfo.getDryLandSurfaceCoords(world, c);
			
//			Treasure.logger.debug("Attempting to gen witherED tree @ {}", c.toShortString());
			
			// add tree if criteria is met
			if (c != null && c != WorldInfo.EMPTY_COORDS) {
				if (c.getDistanceSq(witherTreeCoords) > 4) {
					if (world.getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHER_LOG) {						
//						Treasure.logger.debug("adding witherED tree @ {}", c.toShortString());
						buildClearing(world, random, c);
						buildTree(world, random, c, config);						
						if (TreasureConfig.enablePoisonFog) {
							GenUtil.addFog(world, random, c, poisonFogDensity);
						}
					}
				}
			}
		}
		
		// add pit/chest
		WitherChestGenerator chestGen = new WitherChestGenerator();
		chestGen.generate2(world, random, coords, Rarity.SCARCE, Configs.chestConfigs.get(Rarity.SCARCE)); 
		
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 */
	private void buildClearing(World world, Random random, ICoords coords) {
		ICoords buildCoords = null;

		// build clearing
		for (int xOffset = -(CLEARING_RADIUS); xOffset <= CLEARING_RADIUS; xOffset++) {
			for (int zOffset = -(CLEARING_RADIUS); zOffset <= CLEARING_RADIUS; zOffset++) {
				if (Math.abs(xOffset) + Math.abs(zOffset) <= 2) {

					// find the first surface
					int yHeight = WorldInfo.getHeightValue(world, coords.add(xOffset, 255, zOffset));
					// Treasure.logger.debug("Wither Tree Clearing yOffset: " + yHeight);
					// NOTE have to use GenUtil here because it takes into account
					// GenericBlockContainer
					buildCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(coords.getX() + xOffset, yHeight, coords.getZ() + zOffset));
					// Treasure.logger.debug("Wither Tree Clearing buildPos: " + buildPos);

					// additional check that it's not a tree and within 2 y-blocks of original
					if (Math.abs(buildCoords.getY() - coords.getY()) < VERTICAL_MAX_DIFF) {
						if (RandomHelper.checkProbability(random, 75)) {
							world.setBlockState(buildCoords.add(0, -1, 0).toPos(), Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
						}
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
	public void buildTree(World world, Random random, ICoords coords, IWitherTreeConfig config) {
		// build a small wither  tree ie one trunk

		// determine the size of the main trunk
		int maxSize = RandomHelper.randomInt(random, 7, config.getMaxTrunkSize());

		boolean hasLifeBeenAdded = false;
		for (int y = 0; y < maxSize; y++) {
			if (y == 2) {
				if (!hasLifeBeenAdded) {
					world.setBlockState(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG_SOUL.getDefaultState());
					 hasLifeBeenAdded = true;
					 continue;
				}
			}
			
			// add the trunk
			world.setBlockState(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG.getDefaultState());
			 
			 // add the branches/roots
			 if (y ==0) {
				 addRoot(world, random, coords, supportTrunkMatrix);
			 }
			 else if (y == maxSize-1) {
				 addTop(world, random, coords, y+1, supportTrunkMatrix.get(random.nextInt(supportTrunkMatrix.size())));
			 }
			 else if (y > 3) {
				 addBranch(world, random, coords, y, maxSize, supportTrunkMatrix);
			 }
		}		 
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
		int maxSize = RandomHelper.randomInt(random, 9, config.getMaxTrunkSize());
	
		// build a 2x2 trunk
		boolean hasLifeBeenAdded = false;
		for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {
			
			for (int y = 0; y < maxSize; y++) {
				if (trunkIndex == 2 && y == 2) { // TODO <-- select the right index and the face facing in the right direction
					if (!hasLifeBeenAdded) {
						world.setBlockState(trunkCoords[trunkIndex].add(0, y, 0).toPos(), 
								TreasureBlocks.WITHER_LOG_SOUL.getDefaultState()
									.withProperty(WitherLogSoulBlock.APPEARANCE, WitherLogSoulBlock.Appearance.FACE)
									.withProperty(WitherLogSoulBlock.FACING, EnumFacing.SOUTH));
//						 Treasure.logger.debug("Wither Tree building life trunk @ " +  trunkCoords[trunkIndex].add(0, y, 0).toShortString());
						 hasLifeBeenAdded = true;
						 continue;
					}
				}
				
				// add the trunk
				world.setBlockState(trunkCoords[trunkIndex].add(0, y, 0).toPos(), TreasureBlocks.WITHER_LOG.getDefaultState());
//				 Treasure.logger.debug("Wither Tree building trunk @ " +  trunkCoords[trunkIndex].add(0, y, 0).toShortString());
				 
				 // add the decorations (branches, roots, top)
				 if (y ==0) {
					 addRoot(world, random, trunkCoords[trunkIndex], trunkMatrix[trunkIndex]);
				 }
				 else if (y == maxSize-1) {
					 addTop(world, random, trunkCoords[trunkIndex], y+1, topMatrix.get(trunkIndex));
				 }
				 else if (y >= 3) {
					 addBranch(world, random, trunkCoords[trunkIndex], y, maxSize, trunkMatrix[trunkIndex]);
				 }
			}

			// set the new max size
			if (maxSize > 3) {
				maxSize -= RandomHelper.randomInt(random, 1, 3);
				maxSize = Math.max(3, maxSize);
//				Treasure.logger.debug("master tree new maxSize: {}", maxSize);
			}
		}

	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param topMatrix2
	 */
	private void addTop(World world, Random random, ICoords coords, int y, Direction direction) {
		if (direction != null) {
			IBlockState state = TreasureBlocks.WITHER_BROKEN_LOG.getDefaultState().withProperty(WitherRootBlock.FACING, direction.toFacing());
			// add the top log to the world
			world.setBlockState(coords.add(0, y, 0).toPos(), state);
//			 Treasure.logger.debug("Wither Tree building top log @ " +  coords.toShortString());			
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
			if (RandomHelper.checkProbability(random, 50)) {			
				// update the coords to the correct position
				ICoords c = coords.add(d, 1);
				Cube groundCube = new Cube(world, c.down(1));
				Cube replaceCube = new Cube(world, c);
				if (groundCube.isSolid() && groundCube.isTopSolid() && (replaceCube.isAir() || replaceCube.isReplaceable())) {
					// rotate the branch in the right direction
					IBlockState state = TreasureBlocks.WITHER_ROOT.getDefaultState().withProperty(WitherRootBlock.FACING, d.toFacing());
					
					// add the branch to the world
					world.setBlockState(c.toPos(), state);
//					 Treasure.logger.debug("Wither Tree building root @ " +  coords.toShortString());					
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
		int branchSize =0;// (y <= (maxSize/3)) ? 3 : (y <= (maxSize * 2/3)) ? 2 : 1;
		if (y < maxSize/3 || y > maxSize/4) branchSize =2;
		else branchSize=1;
		
		// for each direction
		for (Direction d : directions) {
			// randomize if a branch is generated
			if (RandomHelper.checkProbability(random, 30)) {
				// for the num of branch segments
				ICoords c = trunkCoords;
				for (int segment = 0; segment < branchSize; segment++) {
					c = c.add(d, 1);
					Cube replaceCube = new Cube(world, c);
					
					// if there is a branch directly below, don't build
					if (world.getBlockState(c.down(1).toPos()).getBlock() instanceof WitherBranchBlock) break;
					
					// if able to place branch here
					if (replaceCube.isAir() || replaceCube.isReplaceable()) {
						// rotate the branch in the right direction
						IBlockState state = TreasureBlocks.WITHER_BRANCH.getDefaultState().withProperty(WitherBranchBlock.FACING, d.toFacing());
						
						// add the branch to the world
						world.setBlockState(c.add(0, y, 0).toPos(), state);
//						 Treasure.logger.debug("Wither Tree building branch @ " +  c.add(0, y, 0).toShortString());
						
						// add spanish moss
						if (RandomHelper.checkProbability(random, 20)) continue;
						
						replaceCube = new Cube(world, c.add(0, y-1, 0));
						if (replaceCube.isAir() || replaceCube.isReplaceable()) {
							world.setBlockState(c.add(0, y-1, 0).toPos(), TreasureBlocks.SPANISH_MOSS.getDefaultState());
						}

					}
					else {
						break;
					}
				}
			}
		}
	}
}
