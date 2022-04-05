/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
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
	
	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.RARE);
		tileEntity.setGenerationContext(generationContext);
	}
	
	@Override
	public List<LootTableShell> buildLootTableList2(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE));
		return tables;
	}	
	
	/**
	 * Rare will have at least one lock.
	 */
	@Override
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		
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
