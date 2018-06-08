/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.inventory.InventoryPopulator;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jun 1, 2018
 *
 */
public class WitherChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public WitherChestGenerator() {}

	/**
	 * Override to retrieve the WITHER CHEST container regardless of rarity.
	 * @param rarity
	 * @return
	 */
	@Override
	public LootContainer selectContainer(Random random, final Rarity rarity) {
		LootContainer container = DbManager.getInstance().selectContainer("wither_chest");
		return container;
	}
	
	/**
	 * Always select a wither chest.
	 */
	@Override
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		// TODO change to wither chest
		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.COMPRESSOR_CHEST;
		return chest;
	}
	
	/**
	 * Don't place any markers
	 */
	@Override
	public void addMarkers(World world, Random random, ICoords coords) {
		return;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.chest.AbstractChestGenerator#addLocks(java.util.Random, com.someguyssoftware.treasure2.block.TreasureChestBlock, com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity, com.someguyssoftware.treasure2.enums.Rarity)
	 */
	@Override
	public void addLocks(Random random, TreasureChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// get the chest type
		TreasureChestType type = chest.getChestType();
		// get the lock states
		List<LockState> lockStates = te.getLockStates();	
		// determine the number of locks to add (must have at least 1 lock)
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		for (int i = 0; i < numLocks; i++) {
			// TEMP select a SPIDER LOCK for now... change to WITHER LOCK
			LockItem lock = TreasureItems.SPIDER_LOCK;
			Treasure.logger.debug("adding lock: {}", lock);
			// add the lock to the chest
			lockStates.get(i).setLock(lock);
		}
	}
}
