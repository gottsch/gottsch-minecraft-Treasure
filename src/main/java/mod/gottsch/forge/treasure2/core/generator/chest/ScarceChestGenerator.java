/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity.GenerationContext;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class ScarceChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public ScarceChestGenerator(IChestGeneratorType type) {
		super(type);
	}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(ITreasureChestBlockEntity blockEntity, IRarity rarity) {
		GenerationContext generationContext = 
				((AbstractTreasureChestBlockEntity)blockEntity).new GenerationContext(rarity, ChestGeneratorType.SCARCE);
		blockEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public List<LootTableShell> buildLootTableList(final IRarity chestRarity) {
		// get all loot tables by column key
		List<LootTableShell> tables = new ArrayList<>();
		tables.addAll(TreasureLootTableRegistry.getLootTableByRarity(Rarity.UNCOMMON));
		tables.addAll(TreasureLootTableRegistry.getLootTableByRarity(Rarity.SCARCE));
		return tables;
	}	
	
	/**
	 * Select Locks from Common and Uncommon rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractTreasureChestBlock chest, ITreasureChestBlockEntity chestBlockEntity, IRarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(KeyLockRegistry.getLocks(Rarity.UNCOMMON).stream().map(l -> l.get()).collect(Collectors.toList()));
		locks.addAll(KeyLockRegistry.getLocks(Rarity.SCARCE).stream().map(l -> l.get()).collect(Collectors.toList()));

		addLocks(random, chest, chestBlockEntity, locks);
		locks.clear();
	}
}