/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class GenUtil {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static int UNDERGROUND_OFFSET = 5;
		
	/**
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public static TreasureChestTileEntity getChestTileEntity(World world, ICoords coords) {
		BlockPos pos = coords.toPos();
		
		// get the tile entity
		TileEntity te = (TileEntity) world.getTileEntity(pos);
		if (te == null) {
			Treasure.logger.warn("Unable to locate Chest TileEntity @: " + pos);
			return null;
		}
		// test if te implements ITreasureChestTileEntityOLD
		if (!(te instanceof TreasureChestTileEntity)) {
			Treasure.logger.warn("Chest TileEntity does not implement TreasureChestTileEntity @: " + pos);
			return null;
		}
		
		// cast		
		return (TreasureChestTileEntity) te;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param coords
	 * @return
	 */
	public static boolean replaceBlockWithChest(World world, Random random, Block chest, ICoords coords) {
		// get the old state
		IBlockState oldState = world.getBlockState(coords.toPos());
//		Treasure.logger.info("World Chest Marker coords:" + pos);
//		Treasure.logger.info("World Chest Block:" + oldState.getClass().getCanonicalName());
//		Treasure.logger.info("Treasure Chest Block:" + chest.getClass().getCanonicalName());
		
		if (oldState.getProperties().containsKey(FACING)) {
			Treasure.logger.info("World Chest marker has FACING property:" + oldState.getValue(FACING));
			// set the new state
			return placeChest(world, chest, coords, (EnumFacing)oldState.getValue(FACING));
			
//			world.setBlockState(pos, this.getChest().getDefaultState().withProperty(FACING, oldState.getValue(FACING)), 3);
		}
		else {
//			world.setBlockState(chestCoords.toBlockPos(), this.getChest().getDefaultState(), 3);
			return placeChest(world, chest, coords, EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)]);
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param chest
	 * @param pos
	 * @return
	 */
	public static boolean placeChest(World world, Block chest, ICoords coords, EnumFacing facing) {
		// check if spawn pos is valid
		if (!WorldInfo.isValidY(coords)) {
			return false;
		}
		
		try {
			BlockPos pos = coords.toPos();
			// create and place the chest
			//world.setBlockState(pos, chest.getStateFromMeta(meta), 3);
			world.setBlockState(pos,chest.getDefaultState().withProperty(FACING, facing), 3);
			//world.setBlockMetadataWithNotify(coords.getX(), coords.getY(), coords.getZ(), meta, 3);
		
			// get the tile entity
			TileEntity te = (TileEntity) world.getTileEntity(pos);
			
			if (te == null) {
				// remove the chest block
				world.setBlockState(pos, Blocks.AIR.getDefaultState());				
				Treasure.logger.warn("Unable to create Chest's TileEntity, removing Chest.");
				return false;
			}
		}
		catch(Exception e) {
			Treasure.logger.error("An error occurred placing chest: ", e);
			return false;
		}		
		return true;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param z
	 */
	public static boolean placeMarkers(World world, Random random, BlockPos pos) {		
		boolean isSuccess = false;
		
		// check if gravestones are enabled
		if (!TreasureConfig.isGravestonesAllowed) {
			return false;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		// for the number of markers configured
		int numberOfMarkers = RandomHelper.randomInt(TreasureConfig.minGravestonesPerChest, TreasureConfig.maxGravestonesPerChest);
		// calculate the grid size
		int gridSize = 4;
		if (numberOfMarkers < 6) { /* default */ }
		else if (numberOfMarkers >= 6 && numberOfMarkers <= 8) {
			gridSize = 5;
		}
		else {
			gridSize = 6;
		}
		
		// loop through each marker
		for (int i = 0; i < numberOfMarkers; i++) {
			boolean isSkeleton = false;

			// generator random x, z
			int xSpawn = x + (random.nextInt(gridSize) * (random.nextInt(3) -1)); // -1|0|1
			int zSpawn = z + (random.nextInt(gridSize) * (random.nextInt(3) -1)); // -1|0|1
			
			// get the "surface" y
			int ySpawn = WorldInfo.getHeightValue(world, new Coords(xSpawn, 0, zSpawn));
			ICoords spawnCoords = new Coords(xSpawn, ySpawn, zSpawn);
			
			// determine if valid y
			//ySpawn = getValidSurfaceY(world, xSpawn, ySpawn, zSpawn);
			if (!WorldInfo.isValidY(spawnCoords)) {
				Treasure.logger.debug(String.format("[%d] is not a valid y value.", spawnCoords.getY()));
				continue;
			}			
			
			//  get a valid surface location
			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
			if (spawnCoords == null) {
				Treasure.logger.debug(String.format("Not a valid surface @ %s", pos));
				continue;
			}
			
			// don't place if the block underneath is of GenericBlock Chest or Container
			Block block = world.getBlockState(spawnCoords.add(0, -1, 0).toPos()).getBlock();
			if(block instanceof BlockContainer || block instanceof AbstractModContainerBlock) {
				Treasure.logger.debug("Marker not placed because block underneath is a chest or a block container.");
				continue;
			}
			
			// place a random grave/bones block
			int markerIndex = random.nextInt(8);
			Block marker = null;
			switch (markerIndex) {
//			case 0:
//				marker = TreasureBlocks.skullBones;
//				break;
//			case 1:
//				marker = TreasureBlocks.gravestone1;
//				break;
//			case 2:
//				marker = TreasureBlocks.gravestone1b;
//				break;
//			case 3:
//				marker = TreasureBlocks.gravestone2;
//				break;
//			case 4:
//				marker = TreasureBlocks.gravestone3;
//				break;
//			case 5:
//				marker = TreasureBlocks.gravestone4;
//				break;
//			case 6:
//				marker = TreasureBlocks.gravestone5;
//				break;
			default:
//				marker = TreasureBlocks.skeleton;
				isSkeleton = true;
			}
			
			// select a random facing direction
			EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
			EnumFacing facing = horizontals[random.nextInt(horizontals.length)];
			
			int xOffset = 0;
			int zOffset = 0;
//			if (isSkeleton) {
//				switch(facing) {
//					//case 2: // north
//					case NORTH:
//						zOffset = 1;
//						break;
//					//case 3: // south
//					case SOUTH:
//						zOffset = -1;
//						break;
//					//case 4: // west
//					case WEST:
//						xOffset = 1;
//						break;
//					//case 5: // east
//					case EAST:
//						xOffset = -1;
//						break;
//					default: // nothing					
//				}
//				block = world.getBlockState(spawnCoords.add(xOffset, -1, zOffset).toPos()).getBlock();
//				// ensure that the block at xs, ys, zs isn't a grave or skeleton/placeholder ie extends GenericBlockContainer or TreasureChestBlock
//				if(block instanceof AbstractModContainerBlock || block instanceof TreasureChestBlock || block instanceof BlockContainer) {
//					Treasure.logger.debug("Marker not placed because block is a chest or a block container.");
//					continue;
//				}
//				
//				// place the placeholder
//				world.setBlockState(
//						spawnCoords.add(xOffset, ySpawn, zOffset)/*xSpawn + xOffset, ySpawn, zSpawn + zOffset*/,
//						TreasureBlocks.skeletonPlaceholder.getDefaultState().withProperty(GenericBlockContainer.FACING, facing));
//			}

			// place the block
			world.setBlockState(spawnCoords.toPos(), marker.getDefaultState().withProperty(TreasureChestBlock.FACING, facing));
			
//			Treasure.logger.info("Placed marker @ " + spawnPos);
			isSuccess = true;
		} // end of for
		
		return isSuccess;
	}

//	/**
//	 * Gets the surface position. Any aboveground land surface or surface of body of water/lava.
//	 * Also takes into account Treasure blocks - gravestones, chests etc.
//	 * Not necessarily valid.
//	 * @param world
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @return
//	 */
//	public static BlockPos getAnySurfacePos(final World world, final BlockPos posIn) {
//		BlockPos pos = new BlockPos(posIn);		
//		boolean isGoodBlock = false;
//		
//		// check if the block below the chest is viable
//		while (!isGoodBlock) {
//			IBlockState blockState = world.getBlockState(pos.add(0, -1, 0));
//			Block block = blockState.getBlock();
//			
//			if (blockState.getBlock() == Blocks.lava || blockState.getBlock() == Blocks.water) {
//				return pos;		
//			}
//
//			if (blockState != null && (block.isAir(world, pos) || block.isBurning(world, pos) || block.isLeaves(world, pos) || block.isFoliage(world, pos))
//					|| block.getMaterial().isReplaceable() || block == Blocks.log || block == Blocks.log2
//					|| block instanceof GenericBlockContainer || block instanceof IBoundsBlock) {
//				pos = pos.add(0, -1, 0);
//			}
//			else {
//				isGoodBlock = true;
//			}
//			
//			if (pos.getY() < 0) {
//				return null;
//			}
//		}		
//		return pos;
//	}
	
	/**
	 * Gets the first land surface position, can be under water/lava,  from the given starting point.
	 * Also takes into account Treasure blocks - gravestones, chests etc.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
//	public static BlockPos getAnyLandSurfacePos(final World world, final BlockPos posIn) {
//		BlockPos pos = new BlockPos(posIn);		
//		boolean isGoodBlock = false;
//		
//		// check if the block below the chest is viable
//		while (!isGoodBlock) {
//			IBlockState blockState = world.getBlockState(pos.add(0, -1, 0));
//			Block block = blockState.getBlock();
//
//			/*
//			 * also check against ice as it is not a valid "land" surface - it can generate onto top of water.
//			 */
//			if (blockState != null && (block.isAir(world, pos) || block == Blocks.water || block == Blocks.lava
//					|| block.getMaterial().isReplaceable() || block.isBurning(world, pos) || block.isLeaves(world, pos) || block.isFoliage(world, pos))
//					|| block == Blocks.log || block == Blocks.log2 || block == Blocks.ice
//					|| block instanceof GenericBlockContainer || block instanceof IBoundsBlock) {
//				pos = pos.add(0, -1, 0);
//			}
//			else {
//				isGoodBlock = true;
//			}
//			
//			if (pos.getY() < 0) {
//				return null;
//			}
//		}		
//		return pos;
//	}
	
	/**
	 * Gets the first <b>valid</b>  land surface position (not on water or on lava) from the given starting point.
	 * Also takes into account Treasure blocks - gravestones, chests etc.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
//	public static BlockPos getDryLandSurfacePos(final World world, final BlockPos posIn) {
//		BlockPos pos = new BlockPos(posIn);		
//		boolean isGoodBlock = false;
//		
//		// check if the block below the chest is viable
//		while (!isGoodBlock) {
//			IBlockState blockState = world.getBlockState(pos.add(0, -1, 0));
//			Block block = blockState.getBlock();
//			
//			if (blockState.getBlock() == Blocks.lava || blockState.getBlock() == Blocks.water) {
//				// return without generating.
//				return null;				
//			}
//
//			/*
//			 * also check against ice as it is not a valid "land" surface - it can generate onto top of water.
//			 */
//			if (blockState != null && (block.isAir(world, pos) || block.isBurning(world, pos) || block.isLeaves(world, pos) || block.isFoliage(world, pos))
//					|| block.getMaterial().isReplaceable() || block == Blocks.log || block == Blocks.log2 || block == Blocks.ice
//					|| block instanceof GenericBlockContainer || block instanceof IBoundsBlock) {
//				pos = pos.add(0, -1, 0);
//			}
//			else {
//				isGoodBlock = true;
//			}
//			
//			if (pos.getY() < 0) {
//				return null;
//			}
//		}		
//		return pos;
//	}
	
	/**
	 * Returns a <b>valid</b> pos underground.
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnYMin
	 * @return
	 */
	public static ICoords getUndergroundSpawnCoords(World world, Random random, ICoords surfaceCoords, int spawnYMin) {
		ICoords spawnCoords = null;
		
		// spawn location under ground
		if (surfaceCoords.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
			int ySpawn = random.nextInt(surfaceCoords.getY()
					- (spawnYMin + UNDERGROUND_OFFSET))
					+ spawnYMin;
			
			spawnCoords = new Coords(surfaceCoords.getX(), ySpawn, surfaceCoords.getZ());
			// get floor pos (if in a cavern or tunnel etc)
			spawnCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
		}
		return spawnCoords;
	}
	
//	/**
//	 * Only return a chest iff it is of type TREASURE
//	 * @param realizedPlan
//	 * @param targetNam
//	 * @return
//	 */
//	public static ICoords findTreasureChestCoords(IRealizedPlan realizedPlan, String targetName) {
//		ICoords chestCoords = null;
//		if (realizedPlan.getContainers() != null && realizedPlan.getContainers().size() > 0) {
//			Treasure.logger.debug("Containers in plan:" + realizedPlan.getContainers().size());
//				List<IContainer> chests = GenUtil.findTargetTreasureChests(realizedPlan, targetName);
//				if (chests != null && chests.size() > 0) {
//					chestCoords = chests.get(0).getCoords();
//				}
//		}
//		return chestCoords;
//	}
	
//	/**
//	 * NOTE searching for null or empty string means the name is unimportant. ie return all chests of treasure type.
//	 * @param plan
//	 * @param targetName
//	 * @return
//	 */
//	public static List<IContainer> findTargetTreasureChests(IRealizedPlan plan, String targetName) {
//
//		if (plan == null || plan.getContainers() == null || plan.getSpawners().size() == 0)
//			return null;
//		
//		// convert targetName to lower case
//		if (targetName != null) {
//			targetName = targetName.toLowerCase();
//		}
//		
//		for (IContainer c : plan.getContainers()) {
//			Treasure.logger.debug("Container ChestType:" + c.getType());
//		}
//		// retrieve only "TREASURE" chests
////		List<IContainer> containers = select(
////				plan.getContainers(),
////				having(on(IContainer.class).getType(),
////						Matchers.equalToIgnoringCase(ChestType.TREASURE.toString())));
//		
//		List<IContainer> containers = plan.getContainers().stream().filter(
//				c -> c.getType().equalsIgnoreCase(ChestType.TREASURE.toString())).collect(Collectors.toList());
//
//		Treasure.logger.debug("Found # TREASURE chests in plan:" + containers.size());
//		List<IContainer> result = new ArrayList<>();
//		for (IContainer s : containers) {
//			String name = "";
//			if (s != null && s.getName() != null) {
//				name = s.getName().trim().toLowerCase();
//			}
//			if (targetName == null || targetName.equals("") || name.equalsIgnoreCase(targetName)) {
//				result.add(s.copy());
//				Treasure.logger.debug("Found a TREASURE Chest @" + s.getCoords());
//			}
//		}
//		return result;
//	}
	
	// TODO not sure that this belongs here, but somewhere in Plans API
//	/**
//	 * 
//	 * @param plan
//	 * @return
//	 */
//	public static List<IConnector> findTargetConnectors(IRealizedPlan plan, String targetName) {
//		if (plan == null || plan.getConnectors() == null
//				|| plan.getConnectors().size() == 0)
//			return null;
//		
//		return findTargetConnectors(plan.getConnectors(), targetName);
//	}
	
//	/**
//	 * 
//	 * @param connectorsIn
//	 * @param targetName
//	 * @return
//	 */
//	public static List<IConnector> findTargetConnectors(List<IConnector> connectorsIn, String targetName) {
//		if (connectorsIn == null || connectorsIn.size() == 0)
//			return null;
//
//		List<IConnector> result = new ArrayList<>();
//
//		// retrieve only unrestricted connectors
//		ConnectorType[] unrestricted = { ConnectorType.PLUG, ConnectorType.SOCKET, ConnectorType.PLUG_SOCKET };
//		List<ConnectorType> unrestrictedList = Arrays.<ConnectorType>asList(unrestricted);
////		List<IConnector> connectors = select(
////				connectorsIn,
////				having(on(IConnector.class).getType(),
////						Matchers.isIn(unrestricted)));
//
//		List<IConnector> connectors = connectorsIn.stream().filter(c -> unrestrictedList.contains(c.getType())).collect(Collectors.toList());
//		
//		for (IConnector c : connectors) {
//			String name = c.getName().trim().toLowerCase();
//			if (name.equals(targetName)) {
//				result.add(c.copy());
//			}
//		}
//		return result;
//	}
	
//	/**
//	 * 
//	 * @param plan
//	 * @param targetName
//	 * @return
//	 */
//	public static List<ISpawner> findTargetSpawners(IRealizedPlan plan, String targetName) {
//		
//		// convert targetName to lower case
//		targetName = targetName.toLowerCase();
//		
//		if (plan == null || plan.getSpawners() == null
//				|| plan.getSpawners().size() == 0)
//			return null;
//
//		List<ISpawner> result = new ArrayList<>();
//
//		for (ISpawner s : plan.getSpawners()) {
//			String name = s.getName().trim().toLowerCase();
//			if (name.equals(targetName)) {
//				result.add(s.copy());
//			}
//		}
//		return result;
//	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords findUndergroundCeiling(World world, ICoords coords) {
		final int CEILING_FAIL_SAFE = 50;
		int ceilingHeight = 1;		
		
		// find the ceiling of the cavern	
		while (world.isAirBlock(coords.toPos())) {				
			ceilingHeight++;
			if (ceilingHeight > world.getHeight() || ceilingHeight == CEILING_FAIL_SAFE) {
				return null;
			}
			coords = coords.add(0, ceilingHeight, 0);
		}
		// add 1 height to the final pos
		coords = coords.add(0, ceilingHeight++, 0);		
		return coords;
	}
	
	/**
	 * This method assumes that the 3 blocks above the chest has been generated and therefor starts at pos.y + 4 
	 * @param world
	 * @param random
	 * @param coords
	 * @param surfaceCoords
	 */
	public static void fillSimpleShaftRandomly(World world, Random random, ICoords coords, ICoords surfaceCoords) {
		ICoords replaceCoords;

		// randomly fill shaft
		for (int i= coords.getY() + (3 + 1); i <= surfaceCoords.getY() - 5; i++) {
			// if the block to be replaced is air block then skip to the next pos
			replaceCoords = new Coords(coords.getX(), i, coords.getZ());
			if (world.isAirBlock(replaceCoords.toPos())) {
				continue;
			}
			
			IBlockState blockState = null;
			int m = random.nextInt(100);
			if (m < 50) // 2x as likely to be air
//				world.setBlockState(replaceCoords.toPos(), Blocks.AIR.getDefaultState(), 1);
				blockState = Blocks.AIR.getDefaultState();
			else if (m < 75)
//				world.setBlockState(replaceCoords.toPos(), Blocks.SAND.getDefaultState(), 1);
				blockState = Blocks.SAND.getDefaultState();
			else if (m < 90)
//				world.setBlockState(replaceCoords.toPos(), Blocks.GRAVEL.getDefaultState(), 1);	
				blockState = Blocks.GRAVEL.getDefaultState();
			else if (m < 100)
				// TODO change to logs and align them correctly
//				world.setBlockState(replaceCoords.toPos(), Blocks.PLANKS.getDefaultState(), 1);
				blockState = Blocks.LOG.getDefaultState();
			
			world.setBlockState(replaceCoords.toPos(), blockState, 3);
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param surfaceCoords
	 */
	public static void fillSimpleShaftWithAir(final World world, final ICoords coords, final ICoords surfaceCoords) {
		final int ABOVE_CHEST_SIZE = 3; // above chests are always fill with logs, sand, and planks
		final int BELOW_SURFACE_SIZE = 5; // the number of blocks below the surface
		for (int i = coords.getY() + (ABOVE_CHEST_SIZE + 1); i <= surfaceCoords.getY() - BELOW_SURFACE_SIZE; i ++) {
			world.setBlockState(new BlockPos(coords.getX(), i, coords.getZ()), Blocks.AIR.getDefaultState(), 3);
		}
	}
	
//	/**
//	 * TODO move to GenUtil
//	 * @param world
//	 * @param pos
//	 * @param proximity
//	 * @return
//	 */
//	public static boolean isPoiWithinDistance(World world, BlockPos pos, int proximity) {
//		double proximitySq = proximity * proximity;
//		
//		// get a list of pois based on chest name
//		Map<ICoords, PlaceOfInterest> pois = Treasure.getPoiRegistry().getPlaces();
//
//		if (pois == null || pois.size() == 0) {
//			Treasure.logger.info("Unable to locate POIs registry or the registry doesn't contain any values");
//			return false;
//		}
//
//		for (PlaceOfInterest poi : pois.values()) {
//			// calculate the distance to the poi
//			double distance = pos.distanceSq(poi.getCoords().getX(), poi.getCoords().getY(), poi.getCoords().getZ());
//			if (distance < proximitySq) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	/**
//	 * 
//	 * @param world
//	 * @param pos
//	 * @param radius
//	 * @return
//	 */
//	public static boolean isLabyrinthWithinDistance(World world, BlockPos pos, int radius) {
//		// check if Labyrinth API mod is loaded
//		if (!Loader.isModLoaded("LabyrinthsAPI") ) {
//			// check if the LabyrinthRegistry class is loaded
//			try {
//				Class.forName("com.someguyssoftware.labyrinths.registry.LabyrinthsRegistry");
//			}
//			catch(ClassNotFoundException e) {
//				// it does not exist on the classpath
//				Treasure.logger.info("Labyrinths Registry not present.");
//				return false;
//			}
//		}
//		
//		double radiusSq = radius * radius;
//		
//		// get a list of pois based on chest name
//		List<ILabyrinthMeta> metas = LabyrinthsRegistry.getInstance().getEntries();
//
//		if (metas == null || metas.size() == 0) {
//			Treasure.logger.debug("Unable to locate Labyrinth registry or the registry doesn't contain any values");
//			return false;
//		}
//
//		for (ILabyrinthMeta meta : metas) {
//			// calculate the distance to the poi
//			double distance = pos.distanceSq(meta.getCoords().getX(), meta.getCoords().getY(), meta.getCoords().getZ());
//			if (distance < radiusSq) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	/**
//	 * 
//	 * @param world
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @return
//	 */
//	public static ICoords getNearestPoiChestCoords(World world, BlockPos pos) {
//		Treasure.logger.info("Getting the nearest coords.");
//		// get a list of pois based on chest name
//		List<PlaceOfInterest> chestsByCategory = Treasure.getPoiRegistry().getByCategory(PoiType.CHEST.getName());
//
//		if (chestsByCategory == null) {
//			Treasure.logger.info("Unable to locate a chests by category");
//			return null;
//		}
//		
//		Treasure.logger.info("Got the the registered chests; count=" + chestsByCategory.size());
//		
//		PlaceOfInterest nearestPoi = null;
//		double nearestDistance = -1;
//		for (PlaceOfInterest poi : chestsByCategory) {
//			TreasureChestBlock chest = null;
//    		Block block = world.getBlockState(poi.getCoords().toBlockPos()).getBlock();
//    		if (block instanceof TreasureChestBlock) {
//    			// NOTE not sure why this block doesn't register right away as TreasureChestBlock.  However, it works in game, so i'm happy.
//			//TreasureChestBlock chest = (TreasureChestBlock) world.getBlock(poi.getCoords().getX(), poi.getCoords().getY(), poi.getCoords().getZ());
//    			chest = (TreasureChestBlock)block;
//    		}
//    		else {
//    			Treasure.logger.info("Chest location " + poi.getCoords() + " does not resolve to a Chest Block, but block=" + block);
//    			continue;
//    		}
//    		
//			// test if the chest is the right type
//    		List<String> chestTypes = new ArrayList<String>();
//    		// capitalize all the elements
//    		for (String s: GeneralConfig.obsidianKeyGenChests) {
//    			chestTypes.add(s.toUpperCase());    			
//    		}
//    		if (chestTypes.contains(chest.getChestType().getName())) {
////			if (chest.getChestType() == Chests.PIRATE_CHEST || chest.getChestType() == Chests.DREAD_PIRATE_CHEST || chest.getChestType() == Chests.WITHER_CHEST) {
//				Treasure.logger.info("Found a chest of type " + chest.getChestType());
//				
//				// ensure that this chest hasn't already been filled by checking the slot 0
//				AbstractTreasureChestTileEntity te   = (AbstractTreasureChestTileEntity) 
//						world.getTileEntity(poi.getCoords().toBlockPos());
//				if (te.getProxy() !=null && te.getProxy().getStackInSlot(UNIQUE_KEY_SLOT) != null && te.getProxy().getStackInSlot(0).getItem() instanceof UniqueKeyItem) {
//					Treasure.logger.info("Nearest chest already contains a unique key.");
//					continue;
//				}
//
//				// calculate the distance to the poi
//				double distance = pos.distanceSq(poi.getCoords().getX(), poi.getCoords().getY(), poi.getCoords().getZ());
//				// save if closer than the last
//				if (nearestDistance == -1 || distance < nearestDistance) {
//					nearestDistance = distance;
//					nearestPoi = poi;
//				}
//			}
//		}
//		
//		if (nearestPoi == null) {
//			Treasure.logger.info("Unable to located a chest of type PIRATE_CHEST|DREAD|WITHER_CHEST. Placing in the first chest in POI.");
//			// ensure that this chest is a chest
//			PlaceOfInterest poi = chestsByCategory.get(0);
//			Block block = world.getBlockState(poi.getCoords().toBlockPos()).getBlock();
//			if (block instanceof TreasureChestBlock) {
//				return poi.getCoords();
//			}
//			else {
//				return null;
//			}
////			return chestsByCategory.get(0).getCoords();
//		}
//		Treasure.logger.info("The closest chest is at " + nearestPoi.getCoords() + " at a distance of  " + Math.sqrt(nearestDistance) + " blocks.");
//		return nearestPoi.getCoords();
//	}
	
//	/**
//	 * 
//	 * @param nearestChestCoords
//	 * @param chest
//	 */
//	public static void placeKeyInChest(World world, ICoords nearestChestCoords, TileEntity chest) {
//		// get the chest tile entity at the nearest poi
//		AbstractTreasureChestTileEntity nearestChestTileEntity   = (AbstractTreasureChestTileEntity) world.getTileEntity(nearestChestCoords.toBlockPos());
//
//		// create an item stack of obsidian key
//		ItemStack keyStack = new ItemStack(TreasureItems.obsidianKey);
//        if (!keyStack.hasTagCompound()) {
//        	keyStack.setTagCompound(new NBTTagCompound());
//        }
//        // save the key cuts to the key item stack
//        keyStack.getTagCompound().setString(AbstractUniqueTreasureChestTileEntity.NBT_KEY_CUTS, ((IUniqueTreasureChestTileEntity)chest).getKeyCuts());
//
//		// place obsidian key in chest
//		nearestChestTileEntity.getProxy().setInventorySlotContents(UNIQUE_KEY_SLOT, keyStack);
//		
//		Treasure.logger.info(String.format("CHEATER! Obsidian Key generated @ %d %d %d", nearestChestCoords.getX(), nearestChestCoords.getY(), nearestChestCoords.getZ()));
//	}
}
