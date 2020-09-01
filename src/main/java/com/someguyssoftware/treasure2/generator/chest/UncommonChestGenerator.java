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
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class UncommonChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public UncommonChestGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		List<LootTable> tables = new ArrayList<>();
		
		// get all loot tables by column key
		Map<String, List<LootTable>> mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.COMMON);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		
		mapOfLootTables = Treasure.LOOT_TABLES.getChestLootTablesTable().column(Rarity.UNCOMMON);
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		return tables;
	}
	
	/**
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(Rarity.COMMON));
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));
		addLocks(random, chest, te, locks);
		locks.clear();
	}
}
