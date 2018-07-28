/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.List;
import java.util.Random;

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
 * @author Mark Gottschling on Mar 15, 2018
 *
 */
public class RareChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public RareChestGenerator() {}

	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
		LootTable table = null;
		
		// select the loot table by rarity
		List<LootTable> tables = TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.SCARCE);
		tables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.RARE));
		
		if (tables != null && !tables.isEmpty()) {
			/*
			 * get a random container
			 */
			if (tables.size() == 1) {
				table = tables.get(0);
			}
			else {
				table = tables.get(RandomHelper.randomInt(random, 0, tables.size()-1));
			}
		}
		return table;
	}
	
	/**
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, TreasureChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = (List<LockItem>) TreasureItems.locks.get(Rarity.SCARCE);
		locks.addAll(TreasureItems.locks.get(Rarity.RARE));
		
		addLocks(random, chest, te, locks);
	}
}
