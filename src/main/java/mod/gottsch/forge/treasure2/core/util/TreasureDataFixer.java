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
 */
package mod.gottsch.forge.treasure2.core.util;

import com.google.common.collect.Maps;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.item.PilferersLockPick;
import mod.gottsch.forge.treasure2.core.item.ThiefsLockPick;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.world.item.Item;

import java.util.Map;

/**
 * 
 * @author Mark Gottschling on Jun 30, 2023
 *
 */
public class TreasureDataFixer {
	public static Map<Item, Integer> KEYS_CONFIG_MAP = Maps.newHashMap();

	/**
	 * 
	 */
	public static void fix() {
		// TODO these are fixing the static TreasureItems registry objects - not necessarily the ForgeRegistry object???
		/*
		 * update lockpick probability settings now that the config is loaded
		 */
		((PilferersLockPick)TreasureItems.PILFERERS_LOCK_PICK.get()).setSuccessProbability(
				Config.SERVER.keysAndLocks.pilferersLockPickCommonSuccessProbability.get(),
				Config.SERVER.keysAndLocks.pilferersLockPickUncommonSuccessProbability.get());
		((ThiefsLockPick)TreasureItems.THIEFS_LOCK_PICK.get()).setSuccessProbability(
				Config.SERVER.keysAndLocks.thiefsLockPickCommonSuccessProbability.get(),
				Config.SERVER.keysAndLocks.thiefsLockPickUncommonSuccessProbability.get(),
				Config.SERVER.keysAndLocks.thiefsLockPickScarceSuccessProbability.get());

		/*
		 *  update registered block/item properties using reflection now that
		 *  the config is loaded.
		 *  ex. WealthItem.maxStackSize
		 */
		ModUtil.setItemMaxStackSize(TreasureItems.COPPER_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.SILVER_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.GOLD_COIN.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.TOPAZ.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.ONYX.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.SAPPHIRE.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.RUBY.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.WHITE_PEARL.get(), Config.SERVER.wealth.wealthMaxStackSize.get());
		ModUtil.setItemMaxStackSize(TreasureItems.BLACK_PEARL.get(), Config.SERVER.wealth.wealthMaxStackSize.get());

		// update key default durability to that of config
		KEYS_CONFIG_MAP.put(TreasureItems.WOOD_KEY.get(), Config.SERVER.keysAndLocks.woodKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.STONE_KEY.get(), Config.SERVER.keysAndLocks.stoneKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.LEAF_KEY.get(), Config.SERVER.keysAndLocks.leafKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.EMBER_KEY.get(), Config.SERVER.keysAndLocks.emberKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.LIGHTNING_KEY.get(), Config.SERVER.keysAndLocks.lightningKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.IRON_KEY.get(), Config.SERVER.keysAndLocks.ironKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.GOLD_KEY.get(), Config.SERVER.keysAndLocks.goldKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.METALLURGISTS_KEY.get(), Config.SERVER.keysAndLocks.metallurgistsKeyMaxUses.get());

		KEYS_CONFIG_MAP.put(TreasureItems.DIAMOND_KEY.get(), Config.SERVER.keysAndLocks.diamondKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.EMERALD_KEY.get(), Config.SERVER.keysAndLocks.emeraldKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.TOPAZ_KEY.get(), Config.SERVER.keysAndLocks.topazKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.ONYX_KEY.get(), Config.SERVER.keysAndLocks.onyxKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.RUBY_KEY.get(), Config.SERVER.keysAndLocks.rubyKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.SAPPHIRE_KEY.get(), Config.SERVER.keysAndLocks.sapphireKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.JEWELLED_KEY.get(), Config.SERVER.keysAndLocks.jewelledKeyMaxUses.get());

		KEYS_CONFIG_MAP.put(TreasureItems.SPIDER_KEY.get(), Config.SERVER.keysAndLocks.spiderKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.SKELETON_KEY.get(), Config.SERVER.keysAndLocks.skeletonKeyMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.WITHER_KEY.get(), Config.SERVER.keysAndLocks.witherKeyMaxUses.get());
		// TODO update
		KEYS_CONFIG_MAP.put(TreasureItems.BONE_KEY.get(), 10);

		KEYS_CONFIG_MAP.put(TreasureItems.PILFERERS_LOCK_PICK.get(), Config.SERVER.keysAndLocks.pilferersLockPickMaxUses.get());
		KEYS_CONFIG_MAP.put(TreasureItems.THIEFS_LOCK_PICK.get(), Config.SERVER.keysAndLocks.thiefsLockPickMaxUses.get());
	}
}
