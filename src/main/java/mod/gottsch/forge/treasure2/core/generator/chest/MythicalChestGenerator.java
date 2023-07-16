/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;

/**
 * 
 * @author Mark Gottschling on Mar 30, 2022
 *
 */
public class MythicalChestGenerator extends EpicChestGenerator {
	
	/**
	 * 
	 */
	public MythicalChestGenerator() {
		super();
	}

	/**
	 * 
	 */
//	@Override
//	public void addGenerationContext(ITreasureChestBlockEntity blockEntity, IRarity rarity) {
//		GenerationContext generationContext = 
//				((AbstractTreasureChestBlockEntity)blockEntity).new GenerationContext(rarity, ChestGeneratorType.MYTHICAL);
//		blockEntity.setGenerationContext(generationContext);
//	}
	
	/**
	 * Select Locks from Epic rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractTreasureChestBlock chest, ITreasureChestBlockEntity chestBlockEntity, IRarity rarity) {
		// select a rarity locks
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(KeyLockRegistry.getLocks(Rarity.EPIC).stream().map(l -> l.get()).collect(Collectors.toList()));
		addLocks(random, chest, chestBlockEntity, locks);
		locks.clear();
	}
	
	/**
	 * Always select a epic chest.
	 */
	@Override
	public AbstractTreasureChestBlock selectChest(final Random random, final IRarity rarity) {
		return super.selectChest(random, Rarity.EPIC);
	}
}
