/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
 */package mod.gottsch.forge.treasure2.core.loot;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
	Pair<List<ItemStack>, List<ItemStack>> generateLoot(Level world, RandomSource random, ILootTableType type, IRarity rarity, Player player,
			ICoords coords);

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	ItemStack getDefaultLootItem(Random random, IRarity rarity);

}
