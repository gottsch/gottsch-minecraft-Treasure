/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.loot.LootPoolShell;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.marker.StructureMarkerGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator {

	default public GeneratorResult<ChestGeneratorData> generate(final World world, final Random random, ICoords coords,
			final Rarity rarity, IBlockState state) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setSpawnCoords(coords);
		// select a loot table
		Optional<LootTableShell> lootTableShell = selectLootTable2(random, rarity);
		ResourceLocation lootTableResourceLocation = null;
		if (lootTableShell.isPresent()) {
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			logger.debug("Unable to select a LootTable for rarity -> {}", rarity);
			return result.fail();
		}
		
		// select a chest from the rarity
		AbstractChestBlock chest = selectChest(random, rarity);
		if (chest == null) {
			logger.warn("Unable to select a chest for rarity -> {}.", rarity);
			return result.fail();
		}

		// place the chest in the world
		TileEntity tileEntity = null;
		if (state != null) {
			tileEntity = placeInWorld(world, random, coords, chest, state);
		} else {
			tileEntity = placeInWorld(world, random, chest, coords);
		}

		if (tileEntity == null) {
			logger.debug("Unable to locate tile entity for chest -> {}", coords);
			return result.fail();
		}

		// add the loot table
		addLootTable((AbstractTreasureChestTileEntity) tileEntity, lootTableResourceLocation);
		
		// seal the chest
		addSeal((AbstractTreasureChestTileEntity) tileEntity);
		
		// update the backing tile entity's generation contxt
		addGenerationContext((AbstractTreasureChestTileEntity) tileEntity, rarity);
		
		// if (!(chest instanceof IMimicBlock)) {
		// 	logger.debug("Generating loot from loot table for rarity {}", rarity);
		// 	lootTable.fillInventory((IInventory) te, random, Treasure.LOOT_TABLES.getContext());
		// }

		// add locks
		addLocks(random, chest, (AbstractTreasureChestTileEntity) tileEntity, rarity);
		
		// update result
		result.getData().setChestContext(new BlockContext(coords, state));

		return result.success();
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
//	default public List<LootTable> buildLootTableList(Rarity rarity) {
//		return Treasure.LOOT_TABLES.getLootTableByRarity(rarity);
//	}
	
	// TODO this should be a generic call that passes in ManagedTableType
	default public List<LootTableShell> buildLootTableList2(Rarity rarity) {
		return Treasure.LOOT_TABLE_MASTER.getLootTableByRarity(TreasureLootTableMaster2.ManagedTableType.CHEST, rarity);
	}

	default public Optional<List<LootTableShell>> buildInjectedLootTableList(String key, Rarity rarity) {
		return Optional.ofNullable(Treasure.LOOT_TABLE_MASTER.getLootTableByKeyRarity(TreasureLootTableMaster2.ManagedTableType.INJECT, key, rarity));
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public AbstractChestBlock selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(rarity);
		AbstractChestBlock chest = (AbstractChestBlock) chestList
				.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));

		// TODO should have a map of available mimics mapped by chest. for now, since
		// only one mimic, just test for it determine if should be mimic get the config
		IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
		if (chest == TreasureBlocks.WOOD_CHEST) {
			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
				chest = (AbstractChestBlock) TreasureBlocks.WOOD_MIMIC;
				logger.debug("Selecting a WOOD MIMIC chest!");
			}
		} else if (chest == TreasureBlocks.PIRATE_CHEST) {
			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
				chest = (AbstractChestBlock) TreasureBlocks.PIRATE_MIMIC;
				logger.debug("Selecting a PIRATE MIMIC chest!");
			}
		}
		return chest;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param tileEntity
	 * @param lootRarity
	 */
	default public void fillChest(final World world, Random random, final TileEntity tileEntity, final Rarity rarity, EntityPlayer player) {
		Optional<LootTableShell> lootTableShell = null;
		ResourceLocation lootTableResourceLocation = ((AbstractTreasureChestTileEntity)tileEntity).getLootTable();
		Treasure.logger.debug("chest has loot table property of -> {}", lootTableResourceLocation);

		if (lootTableResourceLocation == null) {
			lootTableShell = selectLootTable2(random, rarity);
		}
		else {
			lootTableShell = Treasure.LOOT_TABLE_MASTER.getLootTableByResourceLocation(lootTableResourceLocation);
		}	
		// is valid loot table shell
		if (lootTableShell.isPresent()) {
			Treasure.logger.debug("using loot table shell -> {}, {}", lootTableShell.get().getCategory(), lootTableShell.get().getRarity());
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			Treasure.logger.debug("Unable to select a LootTable for rarity -> {}", rarity);
			return;
		}
		Treasure.logger.debug("loot table resource -> {}", lootTableResourceLocation); 
		
		LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(lootTableResourceLocation);
		if (lootTable == null) {
			logger.warn("Unable to select a lootTable.");
			return;
		}		
		Treasure.logger.debug("selected loot table -> {} from resource -> {}", lootTable, lootTableResourceLocation);
		
		// update rarity from lootTableShell		
		Rarity effectiveRarity = Treasure.LOOT_TABLE_MASTER.getEffectiveRarity(lootTableShell.get(), rarity);		
		logger.debug("Generating loot from loot table for effective rarity {}", effectiveRarity);
		
		// setup lists of items
		List<ItemStack> treasureStacks = new ArrayList<>();
		List<ItemStack> itemStacks = new ArrayList<>();
		
		/*
		 * Using per loot table file - category strategy (instead of per pool strategy)
		 */
		// get a list of loot pools
		List<LootPoolShell> lootPoolShells = lootTableShell.get().getPools();
		if (lootPoolShells != null && lootPoolShells.size() > 0) {
			logger.debug("# of pools -> {}", lootPoolShells.size());
		}
		
		// setup context
		LootContext lootContext = null;
		if (player == null) {
			lootContext = Treasure.LOOT_TABLE_MASTER.getContext();
		}
		else {
			lootContext = new LootContext.Builder((WorldServer) world)
				.withLuck(player.getLuck())
				.withPlayer(player)
				.build();
		}
		
		for (LootPoolShell pool : lootPoolShells) {
			logger.debug("processing pool -> {}", pool.getName());
			// go get the vanilla managed pool
			LootPool lootPool = lootTable.getPool(pool.getName());
			
			// geneate loot from pools
			if (pool.getName().equalsIgnoreCase("treasure")) {
				lootPool.generateLoot(treasureStacks, random, lootContext);
			}
			else {
				lootPool.generateLoot(itemStacks, random, lootContext);
			}
		}
		logger.debug("size of treasure stacks -> {}", treasureStacks.size());
		logger.debug("size of item stacks -> {}", itemStacks.size());
		
		// record original item size (max number of items to pull from final list)
		int lootItemSize = itemStacks.size();
		
		// TODO move to separate method
		// fetch all injected loot tables by category/rarity
		logger.debug("searching for injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
		Optional<List<LootTableShell>> injectLootTableShells = buildInjectedLootTableList(lootTableShell.get().getCategory(), effectiveRarity);
		if (injectLootTableShells.isPresent()) {
			logger.debug("found injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
			logger.debug("size of injectable tables -> {}", injectLootTableShells.get().size());
			itemStacks.addAll(Treasure.LOOT_TABLE_MASTER.getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
		}
		
		// add the treasure items to the chest
		fillInventory((IInventory) tileEntity, random, treasureStacks);
		
		// shuffle the items list
		Collections.shuffle(itemStacks, random);
		
		// fill the chest with items
		fillInventory((IInventory) tileEntity, random, itemStacks.stream().limit(lootItemSize).collect(Collectors.toList()));
	}

	/**
	 * 
	 * @param inventory
	 * @param random
	 * @param context
	 */
	default public void fillInventory(IInventory inventory, Random random, List<ItemStack> list) {
		List<Integer> emptySlots = getEmptySlotsRandomized(inventory, random);
		logger.debug("empty slots size -> {}", emptySlots.size());
		this.shuffleItems(list, emptySlots.size(), random);

		for (ItemStack itemstack : list) {
			// if no more empty slots are available
			if (emptySlots.isEmpty()) {
				return;
			}

			if (itemstack.isEmpty()) {
				inventory.setInventorySlotContents(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), ItemStack.EMPTY);
			} 
			else {
				inventory.setInventorySlotContents(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), itemstack);
			}
		}
	}
	
	/**
	 * 
	 * @param inventory
	 * @param rand
	 * @return
	 */
	default public List<Integer> getEmptySlotsRandomized(IInventory inventory, Random rand) {
		List<Integer> list = Lists.<Integer>newArrayList();

		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				list.add(Integer.valueOf(i));
			}
		}

		Collections.shuffle(list, rand);
		return list;
	}
	
	/**
	 * shuffles items by changing their order (no stack splitting)
	 */
	default public void shuffleItems(List<ItemStack> stacks, int emptySlotsSize, Random rand) {
		Collections.shuffle(stacks, rand);
	}
	
	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
//	@Deprecated
//	default public LootTable selectLootTable(Random random, final Rarity rarity) {
//		LootTable table = null;
//
//		// select the loot table by rarity
//		List<LootTable> tables = buildLootTableList(rarity);
//
//		// select a random table from the list
//		if (tables != null && !tables.isEmpty()) {
//			int index = 0;
//			if (tables.size() == 1) {
//				table = tables.get(0);
//			} else {
//				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
//				table = tables.get(index);
//			}
//			logger.debug("Selected loot table index --> {}", index);
//		}
//		return table;
//    }
	
	/**
	 * 
	 * @param factory
	 * @param rarity
	 * @return
	 */
//	@Deprecated
//    default public LootTable selectLootTable(Supplier<Random> factory, final Rarity rarity) {
//		LootTable table = null;
//
//		// select the loot table by rarity
//		List<LootTable> tables = buildLootTableList(rarity);
//
//		// select a random table from the list
//		if (tables != null && !tables.isEmpty()) {
//			int index = 0;
//			if (tables.size() == 1) {
//				table = tables.get(0);
//			} else {
//				index = RandomHelper.randomInt(factory.get(), 0, tables.size() - 1);
//				table = tables.get(index);
//			}
//			logger.debug("Selected loot table index --> {}", index);
//		}
//		return table;
//	}
    
	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
    default public Optional<LootTableShell> selectLootTable2(Random random, final Rarity rarity) {
    	LootTableShell lootTableShell = null;

		// select the loot table by rarity
		List<LootTableShell> tables = buildLootTableList2(rarity);
		if (tables !=null)logger.debug("tables size -> {}", tables.size());
		
		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			logger.debug("Selected loot table shell index --> {}", index);
		}
		return Optional.ofNullable(lootTableShell);
	}
    
    /**
     * 
     * @param factory
     * @param rarity
     * @return
     */
    default public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, Rarity rarity) {
    	LootTableShell lootTableShell = null;

		// select the loot table by rarity
		List<LootTableShell> tables = buildLootTableList2(rarity);
		if (tables !=null)logger.debug("tables size -> {}", tables.size());
		
		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(factory.get(), 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			logger.debug("Selected loot table shell index --> {}", index);
		}
		return Optional.ofNullable(lootTableShell);	
    }

	/**
	 * 
	 * @param tileEntity
	 * @param rarity
	 */
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity);
	
	/**
	 * 
	 * @param tileEntity
	 * @param location
	 */
	default public void addLootTable(AbstractTreasureChestTileEntity tileEntity, ResourceLocation location) {
		tileEntity.setLootTable(location);
	}
	
	/**
	 * 
	 * @param tileEntity
	 */
	default public void addSeal(AbstractTreasureChestTileEntity tileEntity) {
		tileEntity.setSealed(true);
	}
	
	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * 
	 * @param chest
	 */
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity chestTileEntity,
			Rarity rarity) {
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(rarity));
		addLocks(random, chest, chestTileEntity, locks);
		locks.clear();
	}

	/**
	 * 
	 * @param random
	 * @param chest
	 * @param te
	 * @param locks
	 */
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity chestTileEntity,
			List<LockItem> locks) {
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getChestType());

		// get the lock states
		List<LockState> lockStates = chestTileEntity.getLockStates();

		for (int i = 0; i < numLocks; i++) {
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size() - 1));
			logger.debug("adding lock: {}", lock);
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
	default public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 0, type.getMaxLocks());
		logger.debug("# of locks to use: {})", numLocks);
		return numLocks;
	}

	/**
	 * Wrapper method so that is can be overridden (as used in the Template Pattern)
	 * 
	 * @param world
	 * @param random
	 * @param coods
	 */
	default public void addMarkers(World world, Random random, ICoords coords, final boolean isSurfaceChest) {
//		boolean isChestOnSurface = false;
		// don't place if the block underneath is of GenericBlock ChestConfig or
		// Container
//		Block block = world.getBlockState(coords/*.add(0, -1, 0)*/.toPos()).getBlock();
//		if(block instanceof BlockContainer || block instanceof AbstractModContainerBlock || block instanceof ITreasureBlock) {
//			isChestOnSurface = true;
//		}
		// GenUtil.placeMarkers(world, random, coords);
		if (!isSurfaceChest && TreasureConfig.WORLD_GEN.getMarkerProperties().isMarkerStructuresAllowed && RandomHelper
				.checkProbability(random, TreasureConfig.WORLD_GEN.getMarkerProperties().markerStructureProbability)) {
			logger.debug("generating a random structure marker -> {}", coords.toShortString());
			new StructureMarkerGenerator().generate(world, random, coords);
		} else {
			new GravestoneMarkerGenerator().generate(world, random, coords);
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest
		TileEntity tileEntity = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			logger.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (tileEntity != null && (tileEntity instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (tileEntity == null || !(tileEntity instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return tileEntity;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chestCoords
	 * @param chest
	 * @param state
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, ICoords chestCoords, AbstractChestBlock chest,
			IBlockState state) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		logger.debug("isPlaced -> {}", isPlaced);
		// get the backing tile entity of the chest
		TileEntity tileEntity = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			logger.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (tileEntity != null && (tileEntity instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (tileEntity == null || !(tileEntity instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return tileEntity;
	}	
}
