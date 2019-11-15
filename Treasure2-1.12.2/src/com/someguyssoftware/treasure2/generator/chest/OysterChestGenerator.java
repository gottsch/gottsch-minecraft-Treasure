/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster.SpecialLootTables;

import net.minecraft.world.World;

public class OysterChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public OysterChestGenerator() {}

	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
		return Treasure.LOOT_TABLES.getSpecialLootTable(SpecialLootTables.OYSTER_CHEST);
	}
	 
	/**
	 * Always select a wither chest.
	 */
//	@Override
//	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
//		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.OYSTER_CHEST;
//		return chest;
//	}
	
	/**
	 * Don't place any markers
	 */
	@Override
	public void addMarkers(World world, Random random, ICoords coords) {
		return;
	}
	
	/**
	 * Oysters will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
}
