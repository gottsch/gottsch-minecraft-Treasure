/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTable;
import com.someguyssoftware.treasure2.loot.TreasureLootTables;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.world.storage.loot.LootTable;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class ScarceChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public ScarceChestGenerator() {}
	
//	/*
//	 * @param random
//	 * @param chestRarity
//	 * @return
//	 */
//	@Override
//	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
//		LootTable table = null;
//		
//		// select the loot table by rarity
//		List<LootTable> tables = TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.UNCOMMON);
//		tables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.SCARCE));
//		
//		if (tables != null && !tables.isEmpty()) {
//			/*
//			 * get a random container
//			 */
//			if (tables.size() == 1) {
//				table = tables.get(0);
//			}
//			else {
//				table = tables.get(RandomHelper.randomInt(random, 0, tables.size()-1));
//			}
//		}
//		return table;
//	}
	
	/**
	 * 
	 */
	@Override
	public List<TreasureLootTable> buildLootTableList(final Rarity chestRarity) {
		// get all loot tables by column key
		List<TreasureLootTable> tables = new ArrayList<>();
		Map<String, List<TreasureLootTable>> mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.UNCOMMON);
		// convert to a single list
		for(Entry<String, List<TreasureLootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.SCARCE);
		// convert to a single list
		for(Entry<String, List<TreasureLootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}		
		return tables;
	}
	
	/**
	 * Scarce will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.logger.debug("# of locks to use: {})", numLocks);		
		return numLocks;
	}
	
	/**
	 * Select Locks from Uncommon, Scare, and Rare rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));
		locks.addAll(TreasureItems.locks.get(Rarity.SCARCE));
//		locks.addAll(TreasureItems.locks.get(Rarity.RARE));
		
		addLocks(random, chest, te, locks);
		locks.clear();
	}
}
