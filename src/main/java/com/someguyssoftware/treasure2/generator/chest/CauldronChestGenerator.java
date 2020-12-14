/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2.SpecialLootTables;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class CauldronChestGenerator extends EpicChestGenerator {

	/**
	 * 
	 */
	public CauldronChestGenerator() {}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.CAULDRON);
		tileEntity.setGenerationContext(generationContext);
	}

	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public Optional<LootTableShell> selectLootTable2(Random random, final Rarity chestRarity) {
		return Optional.ofNullable(Treasure.LOOT_TABLE_MASTER.getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST));
	}

	@Override
	public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, final Rarity rarity) {
		return Optional.ofNullable(Treasure.LOOT_TABLE_MASTER.getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST));
	}

	/**
	 * Always select a cauldron chest.
	 */
	@Override
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.CAULDRON_CHEST;
		return chest;
	}
}