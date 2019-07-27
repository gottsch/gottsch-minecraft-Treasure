/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.IMimicBlock;
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
import com.someguyssoftware.treasure2.enums.StructureMarkers;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.marker.RandomStructureMarkerGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfo;
import com.someguyssoftware.treasure2.world.gen.structure.IStructureInfoProvider;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
		
		RandomHelper.checkProbability(random, config.getGenProbability());
	
		// 1. determine y-coord of land for markers
		markerCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("Marker Coords @ {}", markerCoords.toShortString());
		if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return false; 
		}
		
		Treasure.logger.debug("Test for above/below");
		// 2. switch on above or below ground
		if (config.isAboveGroundAllowed() && random.nextInt(8) == 0) { // TODO should the random be a configurable property
			// set the chest coords to the surface pos
			chestCoords = new Coords(markerCoords);
			Treasure.logger.debug("Above ground @ {}", chestCoords.toShortString());
			isGenerated = true;
		}
		else if (config.isBelowGroundAllowed()) {
			// 2.5. check if it has 50% land
			if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
				Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
				return false;
			}
			
			// determine spawn coords below ground
			spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());

			if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
				Treasure.logger.debug("Unable to spawn underground @ {}", markerCoords);
				return false;
			}
			Treasure.logger.debug("Below ground @ {}", spawnCoords.toShortString());
			
			// select a pit generator
			PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.structurePitProbability) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
			List<IPitGenerator> pitGenerators = ChestWorldGenerator.pitGens.row(pitType).values().stream()
					.collect(Collectors.toList());
			IPitGenerator pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
			
			Treasure.logger.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());
			
			// 3. build the pit
			isGenerated = pitGenerator.generate(world, random, markerCoords, spawnCoords);
			Treasure.logger.debug("Is pit generated: {}", isGenerated);
			// 4. build the room
			
			// 5. update the chest coords
//			chestCoords = new Coords(spawnCoords);

			// TODO refactor this whole block/method
			if (isGenerated) {
				if (pitGenerator instanceof IStructureInfoProvider) {
					// TODO could extend IStructureInfoProvider for Treasure context that only records a single or main chest
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
					chestCoords = new Coords(spawnCoords);
				}
				
			}
			
		}
		else { return false; }

		// TODO change to has chest coords ie if (chestCoords != null && chestCoords != WorldInfo.EMPTY_COORDS
		
		// if successfully gen the pit
		if (/*isGenerated*/chestCoords != null) {
//			Treasure.logger.debug("isGenerated = TRUE");
			LootTable lootTable = selectLootTable(random, chestRarity);

			if (lootTable == null) {
				Treasure.logger.warn("Unable to select a lootTable.");
				return false;
			}
//			Treasure.logger.debug("Selected loot table -> {}", lootTable.toString());
			
			// select a chest from the rarity
			AbstractChestBlock chest = selectChest(random, chestRarity);	
			if (chest == null) {
//				Treasure.logger.warn("Unable to select a chest for rarite {} and container {}.", chestRarity, container.getName());
				Treasure.logger.warn("Unable to select a chest for rarity {}.", chestRarity);
				return false;
			}
//			Treasure.logger.debug("Choosen chest: {}", chest.getUnlocalizedName());
						
			// place the chest in the world
			TileEntity te = placeInWorld(world, random, chest, chestCoords);
			if (te == null) return false;
			
			// populate the chest with items
			if (chest instanceof WitherChestBlock) {
				Treasure.logger.debug("Generating loot from loot table for wither chest");
//				List<ItemStack> stacks = TreasureLootTables.WITHER_CHEST_LOOT_TABLE.generateLootForPools(random, TreasureLootTables.CONTEXT);
//				Treasure.logger.debug("Generated loot:");
//				for (ItemStack stack : stacks) {
//					Treasure.logger.debug(stack.getDisplayName());
//				}
				
				lootTable.fillInventory(((AbstractTreasureChestTileEntity)te).getInventoryProxy(), 
							random,
							Treasure.LOOT_TABLES.getContext());
			}
			else if (chest instanceof IMimicBlock) {
				// don't fill
			}
			else {
//				InventoryPopulator pop = new InventoryPopulator();
//				pop.populate(((AbstractTreasureChestTileEntity)te).getInventoryProxy(), container);
				Treasure.logger.debug("Generating loot from loot table for rarity {}", chestRarity);
//				List<ItemStack> stacks = lootTable.generateLootFromPools(random, TreasureLootTables.CONTEXT);
//				Treasure.logger.debug("Generated loot:");
//				for (ItemStack stack : stacks) {
//					Treasure.logger.debug("item -> {}, size -> {}", stack.getDisplayName(), stack.getCount());
//				}				
				lootTable.fillInventory(((AbstractTreasureChestTileEntity)te).getInventoryProxy(), 
							random,
							Treasure.LOOT_TABLES.getContext());			
			}
			
			// add locks
			addLocks(random, chest, (AbstractTreasureChestTileEntity)te, chestRarity);
			
			// add markers (above chest or shaft)
			addMarkers(world, random, markerCoords);

			Treasure.logger.info("CHEATER! {} chest at coords: {}", chestRarity, chestCoords.toShortString());
			return true;
		}		
		return false;
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
//		GenUtil.placeMarkers(world, random, coords);
		if (TreasureConfig.isStructureMarkersAllowed && RandomHelper.checkProbability(random, TreasureConfig.structureMarkerProbability)) {
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
