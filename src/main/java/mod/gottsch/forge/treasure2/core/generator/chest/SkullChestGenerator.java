/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestType;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableMaster2.SpecialLootTables;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class SkullChestGenerator implements IChestGenerator {

	/**
	 * 
	 */
	public SkullChestGenerator() {}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.SKULL);
		tileEntity.setGenerationContext(generationContext);
	}

	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public Optional<LootTableShell> selectLootTable2(Random random, final Rarity chestRarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.SKULL_CHEST));
	}

	@Override
	public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, final Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.SKULL_CHEST));
	}

	/**
	 * Always select a skull chest.
	 */
	@Override
	public StandardChestBlock  selectChest(final Random random, final Rarity rarity) {
		StandardChestBlock chest = (StandardChestBlock) TreasureBlocks.SKULL_CHEST;
		return chest;
	}

	/**
	 * Skull chest will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);

		return numLocks;
	}

	/**
	 * Select Locks from Uncommon and Scare rarities.
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