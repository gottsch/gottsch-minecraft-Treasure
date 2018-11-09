/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTable;
import com.someguyssoftware.treasure2.loot.TreasureLootTables;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class UncommonChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public UncommonChestGenerator() {}
	
//	/**
//	 * 
//	 * @param random
//	 * @param chestRarity
//	 * @return
//	 */
//	@Override
//	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
//		LootTable table = null;
//		
//		// select the loot table by rarity
//		List<LootTable> tables = TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.COMMON);
//		tables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.UNCOMMON));
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
		List<TreasureLootTable> tables = new ArrayList<>();
		
		// get all loot tables by column key
		Map<String, List<TreasureLootTable>> mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.COMMON);
		// convert to a single list
		for(Entry<String, List<TreasureLootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.UNCOMMON);
		for(Entry<String, List<TreasureLootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
//		Treasure.logger.debug("adding locks from -> {} chest generator using rarity -> {}", this.getClass().getSimpleName(), rarity);
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(Rarity.COMMON));
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));
//		Treasure.logger.debug("locks to choose from are:");
//		for (LockItem lock : locks) {
//			Treasure.logger.debug("lock -> {}", lock.toString());
//		}
		addLocks(random, chest, te, locks);
		locks.clear();
	}
}
