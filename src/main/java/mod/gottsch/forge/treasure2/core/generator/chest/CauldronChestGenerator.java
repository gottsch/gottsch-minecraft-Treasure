/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.loot.LootTableShell;

import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableMaster2.SpecialLootTables;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;

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
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST));
	}

	@Override
	public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, final Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST));
	}

	/**
	 * Always select a cauldron chest.
	 */
	@Override
	public StandardChestBlock  selectChest(final Random random, final Rarity rarity) {
		StandardChestBlock chest = (StandardChestBlock) TreasureBlocks.CAULDRON_CHEST;
		return chest;
	}
}