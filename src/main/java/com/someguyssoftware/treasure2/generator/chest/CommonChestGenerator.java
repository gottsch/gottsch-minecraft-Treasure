/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class CommonChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public CommonChestGenerator() {}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.COMMON);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
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
	 * Select Locks from Common and Uncommon rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity chestTileEntity, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll((List<LockItem>) TreasureItems.locks.get(Rarity.COMMON));
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));

		addLocks(random, chest, chestTileEntity, locks);
		locks.clear();
	}
}
