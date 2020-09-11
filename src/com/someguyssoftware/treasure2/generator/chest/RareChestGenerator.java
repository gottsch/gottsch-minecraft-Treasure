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
public class RareChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public RareChestGenerator() {}
	
	@Override
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		List<LootTable> tables = new ArrayList<>();
		Map<String, List<LootTable>> mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.SCARCE);
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.RARE);
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}		
		return tables;
	}
	
	/**
	 * Rare will have at least one lock.
	 */
	@Override
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
	
	/**
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(Rarity.SCARCE));
		locks.addAll(TreasureItems.locks.get(Rarity.RARE));
		
		addLocks(random, chest, te, locks);
		locks.clear();
	}
}
