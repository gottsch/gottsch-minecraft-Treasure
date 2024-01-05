/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestType;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity.GenerationContext;

/**
 * 
 * @author Mark Gottschling on Mar 30, 2022
 *
 */
public class LegendaryChestGenerator extends EpicChestGenerator {
	
	/**
	 * 
	 */
	public LegendaryChestGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.LEGENDARY);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * Select Locks from Epic.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(Rarity.EPIC));		
		addLocks(random, chest, te, locks);
		locks.clear();
	}
	
	/**
	 * Always select a epic chest.
	 */
	@Override
	public AbstractChestBlock  selectChest(final Random random, final Rarity rarity) {
		return super.selectChest(random, Rarity.EPIC);
	}
}
