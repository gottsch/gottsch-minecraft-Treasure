/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.generator.chest;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.loot.LootPoolShell;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.chest.ChestInfo.GenType;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
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
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.registry.RegistryType;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator {

	default public GeneratorResult<ChestGeneratorData> generate(final IServerWorld world, final Random random, ICoords coords,
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
		result.getData().setRegistryName(chest.getRegistryName());
		
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

		// add locks
		addLocks(random, chest, (AbstractTreasureChestTileEntity) tileEntity, rarity);
		
		// update result
		result.getData().setChestContext(new BlockContext(coords, state));

		return result.success();
	}

	/**
	 * 
	 * @param chest
	 * @return
	 */
//	default public boolean isChestEnabled(Block chest) {
//		if(chest instanceof TreasureChestBlock &&
//				(!TreasureConfig.CHESTS.chestEnablementMap.containsKey(chest.getRegistryName().getResourcePath()) ||
//						TreasureConfig.CHESTS.chestEnablementMap.get(chest.getRegistryName().getResourcePath()))) {
//			return true;
//		}
//		return false;
//	}
	
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
		Treasure.LOGGER.debug("attempting to get chest list for rarity -> {}", rarity);
		List<Block> chestList = (List<Block>) TreasureData.CHESTS_BY_RARITY.get(rarity);
		Treasure.LOGGER.debug("size of chests lists -> {}", chestList.size());
		AbstractChestBlock chest = null;
		if (!chestList.isEmpty()) {
			chest = (AbstractChestBlock) chestList.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));
		}
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
		
		LootTable lootTable = world.getServer().getLootTables().get(lootTableResourceLocation);
		if (lootTable == null) {
			LOGGER.warn("Unable to select a lootTable.");
			return;
		}		
		Treasure.LOGGER.debug("selected loot table -> {} from resource -> {}", lootTable, lootTableResourceLocation);
		
		// update rarity from lootTableShell		
		Rarity effectiveRarity = TreasureLootTableRegistry.getLootTableMaster().getEffectiveRarity(lootTableShell.get(), rarity);		
		LOGGER.debug("generating loot from loot table for effective rarity {}", effectiveRarity);
		
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
		lootContext = new LootContext.Builder((ServerWorld) world)
				.withLuck(player.getLuck())
				.withParameter(LootParameters.THIS_ENTITY, player)
				.withParameter(LootParameters.ORIGIN, new Vector3d(tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ())).create(LootParameterSets.CHEST);

		
//		LOGGER.debug("loot context -> {}", lootContext);

		for (LootPoolShell pool : lootPoolShells) {
			LOGGER.debug("processing pool (from poolShell) -> {}", pool.getName());
			// go get the vanilla managed pool
			LootPool lootPool = lootTable.getPool(pool.getName());
			
			if (lootPool != null) {
				// geneate loot from pools
				if (pool.getName().equalsIgnoreCase("treasure") ||
						pool.getName().equalsIgnoreCase("charms")) {
					LOGGER.debug("generating loot from treasure/charm pool -> {}", pool.getName());
					lootPool.addRandomItems(treasureStacks::add, lootContext);
				}
				else {
					LOGGER.debug("generating loot from loot pool -> {}", pool.getName());
					lootPool.addRandomItems(itemStacks::add, lootContext);
				}
			}
		}
		LOGGER.debug("size of treasure stacks -> {}", treasureStacks.size());
		LOGGER.debug("size of item stacks -> {}", itemStacks.size());
		
//		List<ItemStack> tempStacks = lootTable.getRandomItems(lootContext);
//		tempStacks.forEach(stack -> {
//			LOGGER.debug("gen from tempStacks -> {}", stack.getDisplayName().getString());
//		});

		// record original item size (max number of items to pull from final list)
		int treasureLootItemSize = treasureStacks.size();
		int lootItemSize = itemStacks.size();
		
		// TODO move to separate method
		// fetch all injected loot tables by category/rarity
		LOGGER.debug("searching for injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
		Optional<List<LootTableShell>> injectLootTableShells = buildInjectedLootTableList(lootTableShell.get().getCategory(), effectiveRarity);
		if (injectLootTableShells.isPresent()) {
			LOGGER.debug("found injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
			LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.get().size());
			
			// seperate the inject tables into 2 groups of pools
//			List<LootPoolShell> treasureLootPoolShells = new ArrayList<>();
//			List<LootPoolShell> otherLootPoolShells = new ArrayList<>();
//			injectLootTableShells.get().forEach(tableShell -> {
//				treasureLootPoolShells.addAll(tableShell.getPools().stream()
//						.filter(pool -> pool.getName().equalsIgnoreCase("treasure") || pool.getName().equalsIgnoreCase("charms"))
//						.collect(Collectors.toList()));
//				otherLootPoolShells.addAll(tableShell.getPools().stream()
//						.filter(pool -> !pool.getName().equalsIgnoreCase("treasure") && !pool.getName().equalsIgnoreCase("charms"))
//						.collect(Collectors.toList()));				
//			});
			
//			injectLootTableShells.get().forEach(shell -> {
//				getInjectedLootItems2(world, random, shell, lootContext);
//			});
			
			// add predicate
			treasureStacks.addAll(getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext, p -> {
				return p.getName().equalsIgnoreCase("treasure") || p.getName().equalsIgnoreCase("charms");
			}));
			itemStacks.addAll(getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext, p -> {
				return !p.getName().equalsIgnoreCase("treasure") && !p.getName().equalsIgnoreCase("charms");
			}));
//			itemStacks.addAll(TreasureLootTableRegistry.getLootTableMaster().getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
		}
		
		// add the treasure items to the chest
		Collections.shuffle(treasureStacks, random);
		fillInventory((IInventory) tileEntity, random, treasureStacks.stream().limit(treasureLootItemSize).collect(Collectors.toList()));
		
		// add a treasure map if there is still space
		addTreasureMap(world, random, (IInventory)tileEntity, new Coords(tileEntity.getBlockPos()), rarity);
		
		// shuffle the items list
		Collections.shuffle(itemStacks, random);		
		// fill the chest with items
		fillInventory((IInventory) tileEntity, random, itemStacks.stream().limit(lootItemSize).collect(Collectors.toList()));
	}
	
	//////////////////
	// TODO add predicate to signature and use it instead of "treasure" filter
	default public List<ItemStack> getInjectedLootItems(World world, Random random, List<LootTableShell> lootTableShells,
			LootContext lootContext, Predicate<LootPoolShell> predicate) {

		List<ItemStack> itemStacks = new ArrayList<>();		

		for (LootTableShell injectLootTableShell : lootTableShells) {			
			LOGGER.debug("injectable resource -> {}", injectLootTableShell.getResourceLocation());

			// get the vanilla managed loot table
			LootTable injectLootTable = world.getServer().getLootTables().get(injectLootTableShell.getResourceLocation());
						
			if (injectLootTable != null) {
				// filter the pool
				List<LootPoolShell> lootPoolShells = injectLootTableShell.getPools().stream()
						.filter(pool -> predicate.test(pool) )
						.collect(Collectors.toList());
				
				lootPoolShells.forEach(poolShell -> {
					// get the vanilla managed loot pool
					LootPool lootPool = injectLootTable.getPool(poolShell.getName());					
					if (lootPool != null) {
						// add loot from tables to itemStacks
						lootPool.addRandomItems(itemStacks::add, lootContext);
					}
				});
				LOGGER.debug("size of item stacks after inject -> {}", itemStacks.size());
			}
		}
		return itemStacks;
	}
	/////////////////
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param tileEntity
	 * @param chestCoords
	 * @param rarity
	 */
	default public void addTreasureMap(World world, Random random, IInventory tileEntity, ICoords chestCoords, Rarity rarity) {
		//check for open slots first
		List<Integer> emptySlots = getEmptySlotsRandomized(tileEntity, random);
		if (!emptySlots.isEmpty() && RandomHelper.checkProbability(random, TreasureConfig.CHESTS.treasureMapProbability.get())) { 
			// determine what level of rarity map to generate
			Rarity mapRarity = getBoostedRarity(rarity, getRarityBoostAmount());
			ResourceLocation dimension = WorldInfo.getDimension(world);
			Treasure.LOGGER.debug("get rarity chests for dimension -> {}", dimension.toString());
			// TODO how to merge surface and submerged
			Optional<List<ChestInfo>> chestInfos = TreasureData.CHEST_REGISTRIES2.get(dimension.toString()).get(RegistryType.SURFACE).getByRarity(mapRarity);
			if (chestInfos.isPresent()) {
				Treasure.LOGGER.debug("got chestInfos by rarity -> {}", mapRarity);
				List<ChestInfo> validChestInfos = chestInfos.get().stream()
						.filter(c -> c.getGenType() != GenType.NONE && !c.isDiscovered() && !c.isTreasureMapOf())
						.collect(Collectors.toList());
				if (!validChestInfos.isEmpty()) {
					Treasure.LOGGER.debug("got valid chestInfos; size -> {}", validChestInfos.size());
					ChestInfo chestInfo = validChestInfos.get(random.nextInt(validChestInfos.size()));
					Treasure.LOGGER.debug("using chestInfo -> {}", chestInfo);
					// build a map
					ItemStack mapStack = createMap(world, chestInfo.getCoords(), mapRarity, (byte)2);

					// add map to chest
					(tileEntity).setItem(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), mapStack);

					// update the chest info in the registry that the map is referring to with this chest's coords
					chestInfo.setTreasureMapFrom(chestCoords);

					// get this chest info from the registry
					Optional<ChestInfo> thisChestInfo = TreasureData.CHEST_REGISTRIES2.get(dimension.toString()).get("submerged").get(rarity, chestCoords.toShortString());
					if (thisChestInfo.isPresent()) {
						thisChestInfo.get().setDiscovered(true);
					}
				}
			}			
		}	
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param rarity
	 * @param zoom
	 * @return
	 */
	default public ItemStack createMap(World world, ICoords coords, Rarity rarity, byte zoom) {
		ItemStack itemStack = FilledMapItem.create(world, coords.getX(), coords.getZ(), zoom, true, true);
		FilledMapItem.renderBiomePreviewMap((ServerWorld) world, itemStack);
		MapData.addTargetDecoration(itemStack, coords.toPos(), "+", MapDecoration.Type.RED_X);
		 itemStack.setHoverName(new TranslationTextComponent("display.treasure_map." + rarity.getValue()));
		 return itemStack;
	}

	/**
	 * 
	 * @param rarity
	 * @param amount
	 * @return
	 */
	default public Rarity getBoostedRarity(Rarity rarity, int amount) {
		return Rarity.getByCode(Math.min(rarity.getCode() + amount, Rarity.EPIC.getCode()));
	}

	/**
	 * 
	 * @return
	 */
	default public int getRarityBoostAmount() {
		int rarityBoost = 1;
		double mapProbability = RandomHelper.randomDouble(0, 100);
		if (mapProbability < 5.0) {
			rarityBoost = 3;
		}
		else if (mapProbability < 15.0) {
			rarityBoost = 2;
		}
		else if (mapProbability < 25.0) {
			rarityBoost = 1;
		}
		return rarityBoost;
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
				inventory.setItem(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), ItemStack.EMPTY);
			} 
			else {
				inventory.setItem(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), itemstack);
			}
		}
	}
	
	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
//	default public LootTable selectLootTable(Random random, final Rarity rarity) {
//		LootTable table = null;
//
//		// select the loot table by rarity
//		List<LootTable> tables = buildLootTableList(rarity);
//
//		// select a random table from the list
//		if (tables != null && !tables.isEmpty()) {
//			int index = 0;
//			/*
//			 * get a random container
//			 */
//			if (tables.size() == 1) {
//				table = tables.get(0);
//			} else {
//				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
//				table = tables.get(index);
//			}
//			Treasure.LOGGER.debug("Selected loot table index --> {}", index);
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
			LOGGER.debug("Selected loot table shell index --> {}, shell -> {}", index, lootTableShell.getCategories());
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
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te,
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
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te,
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

		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			if (inventory.getItem(i).isEmpty()) {
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
	default public void addMarkers(IServerWorld world, ChunkGenerator generator, Random random, ICoords coords, final boolean isSurfaceChest) {
		if (!isSurfaceChest && TreasureConfig.MARKERS.markerStructuresAllowed.get() && RandomHelper
				.checkProbability(random, TreasureConfig.MARKERS.markerStructureProbability.get())) {
			Treasure.LOGGER.debug("generating a random structure marker -> {}", coords.toShortString());
			new StructureMarkerGenerator().generate(world, random, coords);
		} else {
			new GravestoneMarkerGenerator().generate(world, generator, random, coords);
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
	default public TileEntity placeInWorld(IServerWorld world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getBlockEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				((ServerWorld)world).removeBlockEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
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
	default public TileEntity placeInWorld(IServerWorld world, Random random, ICoords chestCoords, AbstractChestBlock chest,
			BlockState state) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		Treasure.LOGGER.debug("isPlaced -> {}", isPlaced);
		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getBlockEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)
			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				((ServerWorld)world).removeBlockEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}