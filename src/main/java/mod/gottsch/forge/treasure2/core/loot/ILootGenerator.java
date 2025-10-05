/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.loot;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
  * 
  * @author Mark Gottschling Jun 12, 2023
  *
  */
public interface ILootGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param rarity
	 * @return
	 */
	Pair<List<ItemStack>, List<ItemStack>> generateLoot(Level world, RandomSource random, ILootTableTypes type, IRarity rarity, Player player,
                                                        ICoords coords);

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	ItemStack getDefaultLootItem(RandomSource random, IRarity rarity);

}
