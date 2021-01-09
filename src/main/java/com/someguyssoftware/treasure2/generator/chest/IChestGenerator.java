/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

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
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator {

	default public GeneratorResult<ChestGeneratorData> generate(final World world, final Random random, ICoords coords,
			final Rarity rarity, BlockState state) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setSpawnCoords(coords);
		// select a loot table
		Optional<LootTableShell> lootTableShell = selectLootTable2(random, rarity);
		ResourceLocation lootTableResourceLocation = null;
		if (lootTableShell.isPresent()) {
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
			return result.fail();
		}
		
		// select a chest from the rarity
		AbstractChestBlock chest = selectChest(random, rarity);
		if (chest == null) {
			LOGGER.warn("Unable to select a chest for rarity -> {}.", rarity);
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
			LOGGER.debug("Unable to locate tile entity for chest -> {}", coords);
			return result.fail();
		}

		// add the loot table
		addLootTable((AbstractTreasureChestTileEntity) tileEntity, lootTableResourceLocation);
		
		// seal the chest
		addSeal((AbstractTreasureChestTileEntity) tileEntity);
		
		// update the backing tile entity's generation contxt
		addGenerationContext((AbstractTreasureChestTileEntity) tileEntity, rarity);
		
		// if (!(chest instanceof IMimicBlock)) {
		// 	LOGGER.debug("Generating loot from loot table for rarity {}", rarity);
		// 	lootTable.fillInventory((IInventory) te, random, Treasure.LOOT_TABLES.getContext());
		// }

		// add locks
		addLocks(random, chest, (AbstractTreasureChestTileEntity) tileEntity, rarity);
		
		// update result
		result.getData().setChestContext(new BlockContext(coords, state));

		return result.success();
	}
	
//	default public GeneratorResult<ChestGeneratorData> generate(final World world, final Random random, ICoords coords,
//			final Rarity rarity, BlockState state) {
//		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
//		result.getData().setSpawnCoords(coords);
//
//		// select a loot table
//		LootTable lootTable = selectLootTable(random, rarity);
//		if (lootTable == null) {
//			Treasure.LOGGER.warn("Unable to select a lootTable.");
//			return result.fail();
//		}
//
//		// select a chest from the rarity
//		AbstractChestBlock<?> chest = selectChest(random, rarity);
//		if (chest == null) {
//			Treasure.LOGGER.warn("Unable to select a chest for rarity {}.", rarity);
//			return result.fail();
//		}
//
//		// place the chest in the world
//		TileEntity te = null;
//		if (state != null) {
//			te = placeInWorld(world, random, coords, chest, state);
//		} else {
//			te = placeInWorld(world, random, chest, coords);
//		}
//
//		if (te == null) {
//			Treasure.LOGGER.debug("Unable to locate tile entity for chest -> {}", coords);
//			return result.fail();
//		}
//		
////	TODO 1.15.2
////		if (!(chest instanceof IMimicBlock)) {
////			Treasure.LOGGER.debug("Generating loot from loot table for rarity {}", rarity);
////			lootTable.fillInventory((IInventory) te, random, Treasure.LOOT_TABLES.getContext());
////		}
//
//		// add locks
//		addLocks(random, chest, (AbstractTreasureChestTileEntity) te, rarity);
//
//		// update result
//		result.getData().setChestContext(new BlockContext(coords, state));
//
//		return result.success();
//	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public List<LootTable> buildLootTableList(Rarity rarity) {
//		TODO 1.15.2
//		return Treasure.LOOT_TABLES.getLootTableByRarity(rarity);
		return new ArrayList<LootTable>();
	}

	// TODO this should be a generic call that passes in ManagedTableType
	default public List<LootTableShell> buildLootTableList2(Rarity rarity) {
		return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(TreasureLootTableMaster2.ManagedTableType.CHEST, rarity);
	}

	default public Optional<List<LootTableShell>> buildInjectedLootTableList(String key, Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getLootTableByKeyRarity(TreasureLootTableMaster2.ManagedTableType.INJECT, key, rarity));
	}
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public AbstractChestBlock selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureData.CHESTS_BY_RARITY.get(rarity); //TreasureBlocks.chests.get(rarity);
		AbstractChestBlock chest = (AbstractChestBlock) chestList
				.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));

		// TODO should have a map of available mimics mapped by chest. for now, since
		// only one mimic, just test for it determine if should be mimic get the config
//		TODO 1.15.2
//				IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
//		if (chest == TreasureBlocks.WOOD_CHEST) {
//			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
//				chest = (AbstractChestBlock) TreasureBlocks.WOOD_MIMIC;
//				Treasure.LOGGER.debug("Selecting a WOOD MIMIC chest!");
//			}
//		} else if (chest == TreasureBlocks.PIRATE_CHEST) {
//			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
//				chest = (AbstractChestBlock) TreasureBlocks.PIRATE_MIMIC;
//				Treasure.LOGGER.debug("Selecting a PIRATE MIMIC chest!");
//			}
//		}
		return chest;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param tileEntity
	 * @param lootRarity
	 */
	default public void fillChest(final World world, Random random, final TileEntity tileEntity, final Rarity rarity, PlayerEntity player) {
		Optional<LootTableShell> lootTableShell = null;
		ResourceLocation lootTableResourceLocation = ((AbstractTreasureChestTileEntity)tileEntity).getLootTable();
		Treasure.LOGGER.debug("chest has loot table property of -> {}", lootTableResourceLocation);

		if (lootTableResourceLocation == null) {
			lootTableShell = selectLootTable2(random, rarity);
		}
		else {
			lootTableShell = TreasureLootTableRegistry.getLootTableMaster().getLootTableByResourceLocation(lootTableResourceLocation);
		}	
		// is valid loot table shell
		if (lootTableShell.isPresent()) {
			Treasure.LOGGER.debug("using loot table shell -> {}, {}", lootTableShell.get().getCategory(), lootTableShell.get().getRarity());
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			Treasure.LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
			return;
		}
		Treasure.LOGGER.debug("loot table resource -> {}", lootTableResourceLocation); 
		
		LootTable lootTable = world.getServer().getLootTableManager().getLootTableFromLocation(lootTableResourceLocation);
		if (lootTable == null) {
			LOGGER.warn("Unable to select a lootTable.");
			return;
		}		
		Treasure.LOGGER.debug("selected loot table -> {} from resource -> {}", lootTable, lootTableResourceLocation);
		
		// update rarity from lootTableShell		
		Rarity effectiveRarity = TreasureLootTableRegistry.getLootTableMaster().getEffectiveRarity(lootTableShell.get(), rarity);		
		LOGGER.debug("Generating loot from loot table for effective rarity {}", effectiveRarity);
		
		// setup lists of items
		List<ItemStack> treasureStacks = new ArrayList<>();
		List<ItemStack> itemStacks = new ArrayList<>();
		
		/*
		 * Using per loot table file - category strategy (instead of per pool strategy)
		 */
		// get a list of loot pools
		List<LootPoolShell> lootPoolShells = lootTableShell.get().getPools();
		if (lootPoolShells != null && lootPoolShells.size() > 0) {
			LOGGER.debug("# of pools -> {}", lootPoolShells.size());
		}
		
		// setup context
		LootContext lootContext = null;
//		if (player == null) {
//			lootContext = Treasure.getLootTableMaster().getContext(); // TODO review this
//		}
//		else {
			lootContext = new LootContext.Builder((ServerWorld) world)
					.withLuck(player.getLuck())
					.withParameter(LootParameters.THIS_ENTITY, player)
					.withParameter(LootParameters.POSITION, new BlockPos(tileEntity.getPos())).build(LootParameterSets.CHEST);
//		}
		
		LOGGER.debug("loot context -> {}", lootContext);

		for (LootPoolShell pool : lootPoolShells) {
			LOGGER.debug("processing pool (from poolShell) -> {}", pool.getName());
			// go get the vanilla managed pool
			LootPool lootPool = lootTable.getPool(pool.getName());
			LOGGER.debug("loot pool object (from lootTable) -> {}", lootPool);
			
			if (lootPool != null) {
				// geneate loot from pools
				if (pool.getName().equalsIgnoreCase("treasure")) {
					lootPool.generate(itemStacks::add, lootContext);
				}
				else {
					LOGGER.debug("generating loot from loot pool -> {}", pool.getName());
					lootPool.generate(itemStacks::add, lootContext);
				}
			}
		}
		LOGGER.debug("size of treasure stacks -> {}", treasureStacks.size());
		LOGGER.debug("size of item stacks -> {}", itemStacks.size());
		
		List<ItemStack> tempStacks = lootTable.generate(lootContext);
		tempStacks.forEach(stack -> {
			LOGGER.debug("gen from tempStacks -> {}", stack.getDisplayName());
		});

		// record original item size (max number of items to pull from final list)
		int lootItemSize = itemStacks.size();
		
		// TODO move to separate method
		// fetch all injected loot tables by category/rarity
		LOGGER.debug("searching for injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
		Optional<List<LootTableShell>> injectLootTableShells = buildInjectedLootTableList(lootTableShell.get().getCategory(), effectiveRarity);
		if (injectLootTableShells.isPresent()) {
			LOGGER.debug("found injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
			LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.get().size());
			itemStacks.addAll(TreasureLootTableRegistry.getLootTableMaster().getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
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
		LOGGER.debug("empty slots size -> {}", emptySlots.size());
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
	 * @param random
	 * @param rarity
	 * @return
	 */
	default public LootTable selectLootTable(Random random, final Rarity rarity) {
		LootTable table = null;

		// select the loot table by rarity
		List<LootTable> tables = buildLootTableList(rarity);

		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			/*
			 * get a random container
			 */
			if (tables.size() == 1) {
				table = tables.get(0);
			} else {
				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
				table = tables.get(index);
			}
			Treasure.LOGGER.debug("Selected loot table index --> {}", index);
		}
		return table;
	}

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
		if (tables !=null) { 
			LOGGER.debug("tables size -> {}", tables.size());
		}
		
		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			LOGGER.debug("Selected loot table shell index --> {}", index);
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
		if (tables !=null) {
			LOGGER.debug("tables size -> {}", tables.size());
		}
		
		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(factory.get(), 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			LOGGER.debug("Selected loot table shell index --> {}", index);
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
	default public void addLocks(Random random, AbstractChestBlock<?> chest, AbstractTreasureChestTileEntity te,
			Rarity rarity) {
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
	default public void addLocks(Random random, AbstractChestBlock<?> chest, AbstractTreasureChestTileEntity te,
			List<LockItem> locks) {
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getChestType());

		// get the lock states
		List<LockState> lockStates = te.getLockStates();

		for (int i = 0; i < numLocks; i++) {
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size() - 1));
			Treasure.LOGGER.debug("adding lock: {}", lock);
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
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		return numLocks;
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
	 * Wrapper method so that is can be overridden (as used in the Template Pattern)
	 * 
	 * @param world
	 * @param random
	 * @param coods
	 */
	default public void addMarkers(World world, Random random, ICoords coords, final boolean isSurfaceChest) {
//		TODO 1.15.2
//		if (!isSurfaceChest && TreasureConfig.WORLD_GEN.getMarkerProperties().isMarkerStructuresAllowed && RandomHelper
//				.checkProbability(random, TreasureConfig.WORLD_GEN.getMarkerProperties().markerStructureProbability)) {
//			Treasure.LOGGER.debug("generating a random structure marker -> {}", coords.toShortString());
//			new StructureMarkerGenerator().generate(world, random, coords);
//		} else {
//			new GravestoneMarkerGenerator().generate(world, random, coords);
//		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, AbstractChestBlock<?> chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockState(chestCoords.toPos(), Blocks.AIR.getDefaultState());
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
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
	default public TileEntity placeInWorld(World world, Random random, ICoords chestCoords, AbstractChestBlock<?> chest,
			BlockState state) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		Treasure.LOGGER.debug("isPlaced -> {}", isPlaced);
		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)
			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockState(chestCoords.toPos(), Blocks.AIR.getDefaultState());
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}