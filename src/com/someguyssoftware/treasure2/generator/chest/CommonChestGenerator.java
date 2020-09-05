/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
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
public class CommonChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public CommonChestGenerator() {}

	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		tables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.COMMON));
		tables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.UNCOMMON));
		return tables;
	}	
	
	/**
	 * Select Locks from Common and Uncommon rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity chestTileEntity, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll((List<LockItem>) TreasureItems.locks.get(Rarity.COMMON));
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));

		addLocks(random, chest, chestTileEntity, locks);
		locks.clear();
	}
}
