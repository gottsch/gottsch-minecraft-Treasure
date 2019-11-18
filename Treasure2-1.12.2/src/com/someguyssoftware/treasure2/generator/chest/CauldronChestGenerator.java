/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster.SpecialLootTables;

/**
 * 
 * @author Mark Gottschling on Jan 30, 2019
 *
 */
public class CauldronChestGenerator extends EpicChestGenerator {
	
	/**
	 * 
	 */
	public CauldronChestGenerator() {}
	
	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
		return Treasure.LOOT_TABLES.getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST);
	}
	 
	/**
	 * Always select a wither chest.
	 */
	@Override
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.CAULDRON_CHEST;
		return chest;
	}
}