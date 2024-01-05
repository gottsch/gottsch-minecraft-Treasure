/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;

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
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.UNCOMMON);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * 
	 */
	@Override
	public List<LootTableShell> buildLootTableList2(final Rarity chestRarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.COMMON));
		tables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.UNCOMMON));
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