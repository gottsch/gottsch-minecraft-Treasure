/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestType;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;


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
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.SCARCE);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * 
	 */
	@Override
	public List<LootTableShell> buildLootTableList2(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.UNCOMMON));
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
		return tables;
	}	
	
	/**
	 * Scarce will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);		
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