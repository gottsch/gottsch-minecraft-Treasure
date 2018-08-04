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
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
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
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		Map<String, List<LootTable>> mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.UNCOMMON);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}
		mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.SCARCE);
		// convert to a single list
		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
			tables.addAll(n.getValue());
		}		
		return tables;
	}
	
	/**
	 * Select Locks from Uncommon, Scare, and Rare rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, TreasureChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = (List<LockItem>) TreasureItems.locks.get(Rarity.UNCOMMON);
		locks.addAll(TreasureItems.locks.get(Rarity.SCARCE));
		locks.addAll(TreasureItems.locks.get(Rarity.RARE));
		
		addLocks(random, chest, te, locks);
	}
}
