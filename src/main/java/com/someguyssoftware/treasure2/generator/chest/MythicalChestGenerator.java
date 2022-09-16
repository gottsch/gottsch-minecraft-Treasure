/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity.GenerationContext;

import net.minecraft.block.Block;

/**
 * 
 * @author Mark Gottschling on Mar 30, 2022
 *
 */
public class MythicalChestGenerator extends EpicChestGenerator {
	
	/**
	 * 
	 */
	public MythicalChestGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.MYTHICAL);
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
