/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;


/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class ScarceChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public ScarceChestGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		Map<String, List<LootTable>> mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.UNCOMMON);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.SCARCE);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
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
		
		addLocks(random, chest, te, locks);
		locks.clear();
	}
}
