/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.generator.GeneratorResult;
import com.someguyssoftware.gottschcore.generator.IGeneratorResult;
import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.WorldInfo.SURFACE;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.IMimicBlock;
import com.someguyssoftware.treasure2.block.ITreasureBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.block.WitherChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult;
import com.someguyssoftware.treasure2.generator.TreasureGeneratorResult;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.marker.RandomStructureMarkerGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.submerged.SubmergedStructureGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfoProvider;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * @author Mark Gottschling on Feb 1, 2018
 *
 */
public abstract class AbstractChestGenerator implements IChestGenerator {
	protected static int UNDERGROUND_OFFSET = 5;
	
	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.IChestGenerator#generate(net.minecraft.world.World, java.util.Random, com.someguyssoftware.gottschcore.positional.ICoords, com.someguyssoftware.treasure2.enums.Rarity, com.someguyssoftware.treasure2.config.IChestConfig)
	 */
	@Override
	public boolean generate(World world, Random random, ICoords coords, Rarity chestRarity, IChestConfig config) {
		ICoords chestCoords = null;
		ICoords markerCoords = null;
		ICoords spawnCoords = null;
		boolean isGenerated = false;
		boolean isAboveGround = false;
		boolean isOcean = false;
		boolean hasMarkers = true;
		ITreasureGeneratorResult result = null;
			
		// 1. collect location data points
		ICoords surfaceCoords = WorldInfo.getSurfaceCoords(world, coords);
		Treasure.logger.debug("surface coords -> {}", surfaceCoords.toShortString());
		if (!WorldInfo.isValidY(surfaceCoords)) {
			Treasure.logger.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
			return false;
		}
		
		SURFACE surface = WorldInfo.getSurface(world, surfaceCoords);
		if (!isSurfaceValid(surface)) return false;
		Treasure.logger.debug("surface -> {}", surface.name());
		
		// 2. determine if on the ocean
		if (surface == SURFACE.WATER) {
			Treasure.logger.debug("surface is water!");
			if (isOcean = isOceanBiome(world, coords)) {			
				Treasure.logger.debug("it is ocean biome!");
				
				// TEMP: test against water probability. this is used to reduce the occurences of submerged structures without re-writting the entire generation system.
				if (!RandomHelper.checkProbability(random, TreasureConfig.waterStructureProbability)) return false;
				
				markerCoords = surfaceCoords;
				spawnCoords = WorldInfo.getOceanFloorSurfaceCoords(world, surfaceCoords);
				Treasure.logger.debug("ocean floor coords -> {}", spawnCoords.toShortString());
				// TODO build with submerged/ruin generator
				result = generateSubmergedRuins(world, random, spawnCoords, config);
				chestCoords = result.getChestCoords();
				// TODO need to save isOcean so the correct marker coords (by archetype, type) can be selected.
				// TEMP for now turn off markers for ocean stuff.
				hasMarkers = false;
			}
			else return false;
		}
		else {
			markerCoords = WorldInfo.getDryLandSurfaceCoords(world, surfaceCoords);
			// determine if above ground or below ground
			if (config.isAboveGroundAllowed() &&
					RandomHelper.checkProbability(random, TreasureConfig.surfaceChestProbability)) { 
				isAboveGround = true;
				if (RandomHelper.checkProbability(random, TreasureConfig.surfaceStructureProbability)) {
					// TODO build structure with ruin/complex, etc builder
					// no markers
					hasMarkers = false;
					// TEMP
					// set the chest coords to the surface pos
					chestCoords = new Coords(markerCoords);
					Treasure.logger.debug("Above ground structure @ {}", chestCoords.toShortString());
//					isGenerated = true;
//					result = new GeneratorResult(true);
				}
				else {
					// set the chest coords to the surface pos
					chestCoords = new Coords(markerCoords);
					Treasure.logger.debug("Above ground, chest only @ {}", chestCoords.toShortString());
//					isGenerated = true;
//					result = new GeneratorResult(true);
				}
			}
			else if (config.isBelowGroundAllowed()) {
				// TODO don't like this. ChestGen shouldn't know anything about PitGenResult, should return IStructureInfo ?
				result = generatePit(world, random, chestRarity, markerCoords, config);
				if (result.isSuccess()) {
					chestCoords = result.getChestCoords(); // TODO all TreasureGeneratorResult should have chest coords
				}
			}
		}
		
		// TODO get all those that meet the biome and randomly select.
		////////////////////////////////////
		////////////////////////////////////
			
//		// 1. determine y-coord of land for markers
//		markerCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
//		Treasure.logger.debug("Marker Coords @ {}", markerCoords.toShortString());
//		if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
//			Treasure.logger.debug("Returning due to marker coords == null or EMPTY_COORDS");
//			return false; 
//		}
//		
//		Treasure.logger.debug("Test for above/below");
//		// 2. switch on above or below ground
//		if (config.isAboveGroundAllowed() && random.nextInt(8) == 0) { // TODO should the random be a configurable property
//			// set the chest coords to the surface pos
//			chestCoords = new Coords(markerCoords);
//			Treasure.logger.debug("Above ground @ {}", chestCoords.toShortString());
//			isGenerated = true;
//		}
//		else if (config.isBelowGroundAllowed()) {
//			// 2.5. check if it has 50% land
//			if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
//				Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
//				return false;
//			}
//			
//			// determine spawn coords below ground
//			spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());
//
//			if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
//				Treasure.logger.debug("Unable to spawn underground @ {}", markerCoords);
//				return false;
//			}
//			Treasure.logger.debug("Below ground @ {}", spawnCoords.toShortString());
//			
//			// select a pit generator
//			PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.pitStructureProbability) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
//			List<IPitGenerator> pitGenerators = ChestWorldGenerator.pitGens.row(pitType).values().stream()
//					.collect(Collectors.toList());
//			IPitGenerator pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
//			
//			Treasure.logger.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());
//			
//			// 3. build the pit
//			result = pitGenerator.generate(world, random, markerCoords, spawnCoords);
//			// TEMP
//			isGenerated = result.isSuccess();
//			
//			Treasure.logger.debug("Is pit generated: {}", isGenerated);
//			// 4. build the room
//			
//			// 5. update the chest coords
////			chestCoords = new Coords(spawnCoords);
//
//			// TODO refactor this whole block/method
//			if (isGenerated) {
//				if (pitGenerator instanceof IStructureInfoProvider) {
//					// TODO could extend IStructureInfoProvider for Treasure context that only records a single or main chest
//					IStructureInfoProvider structureInfoProvider = (IStructureInfoProvider)pitGenerator;
//					IStructureInfo structureInfo = structureInfoProvider.getInfo();
//					if (structureInfo != null) {
//						List<ICoords> chestCoordsList = (List<ICoords>) structureInfo
//								.getMap()
//								.get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
//						if (!chestCoordsList.isEmpty()) {
//							chestCoords = chestCoordsList.get(0);
//							Treasure.logger.debug("Using StructureInfo relative chest coords -> {}", chestCoords.toShortString());
//							chestCoords = chestCoords.add((((IStructureInfoProvider)pitGenerator).getInfo().getCoords()));	
//						}
//						else {
//							Treasure.logger.debug("Unable to retrieve Chest from structure");
//						}
//					}
//					else {
//						Treasure.logger.debug("Unable to retrieve StructureInfo");
//					}
//				}
//				else {
//					chestCoords = new Coords(spawnCoords);
//				}
//				
//			}
//			
//		}
//		else { return false; }

		// TODO change to has chest coords ie if (chestCoords != null && chestCoords != WorldInfo.EMPTY_COORDS
		
		// if successfully gen the pit
		if (/*isGenerated*/chestCoords != null) {
			LootTable lootTable = selectLootTable(random, chestRarity);

			if (lootTable == null) {
				Treasure.logger.warn("Unable to select a lootTable.");
				return false;
			}
		
			// select a chest from the rarity
			AbstractChestBlock chest = selectChest(random, chestRarity);	
			if (chest == null) {
				Treasure.logger.warn("Unable to select a chest for rarity {}.", chestRarity);
				return false;
			}
						
			// place the chest in the world
			TileEntity te = placeInWorld(world, random, chest, chestCoords);
			if (te == null) return false;
			
			// populate the chest with items
//			if (chest instanceof WitherChestBlock) {
//				Treasure.logger.debug("Generating loot from loot table for wither chest");
//				lootTable.fillInventory(((AbstractTreasureChestTileEntity)te).getInventoryProxy(), 
//							random,
//							Treasure.LOOT_TABLES.getContext());
//			}
//			else
			if (chest instanceof IMimicBlock) {
				// don't fill
			}
			else {
				Treasure.logger.debug("Generating loot from loot table for rarity {}", chestRarity);
				lootTable.fillInventory(((AbstractTreasureChestTileEntity)te).getInventoryProxy(), 
							random,
							Treasure.LOOT_TABLES.getContext());			
			}
			
			// add locks
			addLocks(random, chest, (AbstractTreasureChestTileEntity)te, chestRarity);
			
			// add markers (above chest or shaft)
			if (hasMarkers) {
				addMarkers(world, random, markerCoords);
			}
			
			Treasure.logger.info("CHEATER! {} chest at coords: {}", chestRarity, chestCoords.toShortString());
			return true;
		}		
		return false;
	}

	/**
	 * 
	 * @param surface
	 * @return
	 */
	private boolean isSurfaceValid(SURFACE surface) {
		if (surface != SURFACE.LAND && surface != SURFACE.WATER) return false;
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	private boolean isOceanBiome(World world, ICoords coords) {
		Biome biome = world.getBiome(coords.toPos());
		boolean isOcean = (biome == Biomes.DEEP_OCEAN || biome == Biomes.OCEAN) ? true : false;
		return isOcean;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public ITreasureGeneratorResult generateSubmergedRuins(World world, Random random, ICoords spawnCoords,
			IChestConfig config) {
		
		ITreasureGeneratorResult result = new TreasureGeneratorResult(true, spawnCoords);
		ICoords chestCoords = null;
		
		// check if it has 50% land
		if (!WorldInfo.isSolidBase(world, spawnCoords, 2, 2, 30)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", spawnCoords.toShortString(), 3, 3);
			return result.fail();
		}
		
		SubmergedStructureGenerator generator = new SubmergedStructureGenerator();
		
		// build the structure
		boolean isGenerated = generator.generate(world, random, spawnCoords);
		Treasure.logger.debug("is submerged struct generated -> {}", isGenerated);
		if (isGenerated) {
			IStructureInfoProvider structureInfoProvider = (IStructureInfoProvider)generator;
			IStructureInfo structureInfo = structureInfoProvider.getInfo();
			if (structureInfo != null) {
				List<ICoords> chestCoordsList = (List<ICoords>) structureInfo
						.getMap()
						.get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
				if (!chestCoordsList.isEmpty()) {
					chestCoords = chestCoordsList.get(0);
					Treasure.logger.debug("Using StructureInfo relative chest coords -> {}", chestCoords.toShortString());
					chestCoords = chestCoords.add((((IStructureInfoProvider)generator).getInfo().getCoords()));
					result.setChestCoords(chestCoords);
				}
				else {
					Treasure.logger.debug("Unable to retrieve Chest from structure");
				}
			}
			else {
				Treasure.logger.debug("Unable to retrieve StructureInfo");
			}
		}
		Treasure.logger.debug("Is submerged generated: {}", result.isSuccess());
		return result;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chestRarity
	 * @param markerCoords
	 * @param config
	 * @return
	 */
	public ITreasureGeneratorResult generatePit(World world, Random random, Rarity chestRarity, ICoords markerCoords, IChestConfig config) {
		ITreasureGeneratorResult result = new TreasureGeneratorResult(true, markerCoords);
		
		// 2.5. check if it has 50% land
		if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
			return result.fail();
		}
		
		// determine spawn coords below ground
		ICoords spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());

		if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Unable to spawn underground @ {}", markerCoords);
			return result.fail();
		}
		Treasure.logger.debug("Below ground @ {}", spawnCoords.toShortString());
		
		// select a pit generator
		IPitGenerator pitGenerator = selectPitGenerator(random);
		
		// 3. build the pit
		result = pitGenerator.generate(world, random, markerCoords, spawnCoords);
		
		ICoords chestCoords = null;
		if (result.isSuccess()) {
			// TODO handle all this within pitGenerator, updating the IGeneratorResult with the chest coods
			if (pitGenerator instanceof IStructureInfoProvider) {
				IStructureInfoProvider structureInfoProvider = (IStructureInfoProvider)pitGenerator;
				IStructureInfo structureInfo = structureInfoProvider.getInfo();
				if (structureInfo != null) {
					List<ICoords> chestCoordsList = (List<ICoords>) structureInfo
							.getMap()
							.get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
					if (!chestCoordsList.isEmpty()) {
						chestCoords = chestCoordsList.get(0);
						Treasure.logger.debug("Using StructureInfo relative chest coords -> {}", chestCoords.toShortString());
						chestCoords = chestCoords.add((((IStructureInfoProvider)pitGenerator).getInfo().getCoords()));
						result.setChestCoords(chestCoords);
					}
					else {
						Treasure.logger.debug("Unable to retrieve Chest from structure");
					}
				}
				else {
					Treasure.logger.debug("Unable to retrieve StructureInfo");
				}
			}
			else {
				chestCoords = result.getChestCoords(); 
			}
			
		}
		Treasure.logger.debug("Is pit generated: {}", result.isSuccess());
		return result;
	}
	
	/**
	 * 	
	 * @param random
	 * @return
	 */
	public IPitGenerator selectPitGenerator(Random random) {
		PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.pitStructureProbability) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
		List<IPitGenerator> pitGenerators = ChestWorldGenerator.pitGens.row(pitType).values().stream()
				.collect(Collectors.toList());
		IPitGenerator pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.logger.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param pos
	 * @param spawnYMin
	 * @return
	 */
	public ICoords getUndergroundSpawnPos(World world, Random random, ICoords pos, int spawnYMin) {
		ICoords spawnPos = null;
		
		// spawn location under ground
		if (pos.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
			int ySpawn = random.nextInt(pos.getY()
					- (spawnYMin + UNDERGROUND_OFFSET))
					+ spawnYMin;
			
			spawnPos = new Coords(pos.getX(), ySpawn, pos.getZ());
			// get floor pos (if in a cavern or tunnel etc)
			spawnPos = WorldInfo.getDryLandSurfaceCoords(world, spawnPos);
		}
		return spawnPos;
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	@Override
	public AbstractChestBlock  selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(rarity);
		AbstractChestBlock chest = (AbstractChestBlock) chestList.get(RandomHelper.randomInt(random, 0, chestList.size()-1));	

		// TODO should have a map of available mimics mapped by chest. for now, since only one mimic, just test for it
		// determine if should be mimic
		if (chest == TreasureBlocks.WOOD_CHEST) {
			// get the config
			IChestConfig config = Configs.chestConfigs.get(rarity);
			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
				chest = (AbstractChestBlock) TreasureBlocks.WOOD_MIMIC;
				Treasure.logger.debug("Selecting a MIMIC chest!");
			}
		}
		return chest;
	}
	
	/**
	 * v1.0 - Currently chests aren't mapped by Category
	 * @param random
	 * @param category
	 * @return
	 */
	public TreasureChestBlock  selectChest(final Random random, final Category category) {
		return null;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	public TileEntity placeInWorld(World world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);
		
		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());
				
		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.logger.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
	
	/**
	 * 
	 * @param chestRarity
	 * @return
	 */
//	public LootContainer selectContainer(Random random, final Rarity chestRarity) {
//		LootContainer container = LootContainer.EMPTY_CONTAINER;
//		// select the loot container by rarity
//		List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(chestRarity);
//		if (containers != null && !containers.isEmpty()) {
//			/*
//			 * get a random container
//			 */
//			if (containers.size() == 1) {
//				container = containers.get(0);
//			}
//			else {
//				container = containers.get(RandomHelper.randomInt(random, 0, containers.size()-1));
//			}
//			Treasure.logger.info("Chosen chest container:" + container);
//		}
//		return container;
//	}
	
	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
		LootTable table = null;

		// select the loot table by rarity
		List<LootTable> tables = buildLootTableList(chestRarity);
		
		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;		
			/*
			 * get a random container
			 */
			if (tables.size() == 1) {
				table = tables.get(0);
			}
			else {
				index = RandomHelper.randomInt(random, 0, tables.size()-1);
				table = tables.get(index);
			}
			Treasure.logger.debug("Selected loot table index --> {}", index);
		}
		return table;
	}	
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	@Override
	public List<LootTable> buildLootTableList(Rarity rarity) {
		return Treasure.LOOT_TABLES.getLootTableByRarity(rarity);
	}
	
	/**
	 * Wrapper method so that is can be overridden (as used in the Template Pattern)
	 * @param world
	 * @param random
	 * @param coods
	 */
	public void addMarkers(World world, Random random, ICoords coords) {
		boolean isChestOnSurface = false;
		// don't place if the block underneath is of GenericBlock Chest or Container
		Block block = world.getBlockState(coords.add(0, -1, 0).toPos()).getBlock();
		if(block instanceof BlockContainer || block instanceof AbstractModContainerBlock || block instanceof ITreasureBlock) {
			isChestOnSurface = true;
		}
//		GenUtil.placeMarkers(world, random, coords);
		if (!isChestOnSurface && TreasureConfig.isStructureMarkersAllowed && RandomHelper.checkProbability(random, TreasureConfig.structureMarkerProbability)) {
			Treasure.logger.debug("generating a random structure marker -> {}", coords.toShortString());
			new RandomStructureMarkerGenerator().generate(world, random, coords);
		}
		else {
			new GravestoneMarkerGenerator().generate(world, random, coords);			
		}
	}
	
	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * @param chest
	 */
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(rarity));
		addLocks(random, chest, te, locks);
		locks.clear();
	}
	
	/**
	 * 
	 * @param random
	 * @param chest
	 * @param te
	 * @param locks
	 */
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, List<LockItem> locks) {
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getChestType());
		
		// get the lock states
		List<LockState> lockStates = te.getLockStates();	

		for (int i = 0; i < numLocks; i++) {			
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size()-1));
			Treasure.logger.debug("adding lock: {}", lock);
			// add the lock to the chest
			lockStates.get(i).setLock(lock);				
		}
	}
	
	/**
	 * 
	 * @param random
	 * @param type
	 * @return
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 0, type.getMaxLocks());		
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
}
