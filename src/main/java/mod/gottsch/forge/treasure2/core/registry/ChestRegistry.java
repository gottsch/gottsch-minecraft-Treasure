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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * NOTE this is different from previous versions of Treasure2 ChestRegistry which
 * has been renamed to SpawnedChestRegistry
 * This registry is simply a registry of Chest block and their rarity.
 * @author Mark Gottschling on Nov 15, 2022
 *
 */
public class ChestRegistry {
	/*
	 * a Table registry for rarity/key lookups for keys
	 */
	private static final Multimap<IRarity, RegistryObject<Block>> CHESTS_BY_RARITY;
	private static final Map<ResourceLocation, RegistryObject<Block>> CHESTS_BY_NAME;
	private static final Map<ResourceLocation, IRarity> CHEST_RARITY_BY_NAME;

	static {
		CHESTS_BY_RARITY = ArrayListMultimap.create();
		CHESTS_BY_NAME = Maps.newHashMap();
		CHEST_RARITY_BY_NAME = Maps.newHashMap();
	}
	
	public static void register(RegistryObject<Block> chest) {		
		if (!CHESTS_BY_NAME.containsKey(ModUtil.getName(chest.get()))) {
			CHESTS_BY_NAME.put(chest.getId(), chest);
		}
	}
	
	public static void registerByRarity(IRarity rarity, RegistryObject<Block> chest) {
		if (!CHESTS_BY_RARITY.containsKey(rarity)) {
			CHESTS_BY_RARITY.put(rarity, chest);
			CHEST_RARITY_BY_NAME.put(chest.getId(), rarity);
		}
	}
	
	public static List<RegistryObject<Block>> getAll() {
		return new ArrayList<>(CHESTS_BY_NAME.values());
	}
	
	public static List<ResourceLocation> getNames() {
		return new ArrayList<>(CHESTS_BY_NAME.keySet());
	}
	
	public static void clearByRarity() {
		CHESTS_BY_RARITY.clear();		
		CHEST_RARITY_BY_NAME.clear();
	}
	
	public static IRarity getRarity(ResourceLocation name) {
		if (CHEST_RARITY_BY_NAME.containsKey(name)) {
			return CHEST_RARITY_BY_NAME.get(name);
		}
		return null;
	}
	
	public static IRarity getRarity(AbstractTreasureChestBlock chest) {
		return getRarity(ModUtil.getName(chest));
	}
	
	public static List<RegistryObject<Block>> getChest(IRarity rarity) {
		return (List<RegistryObject<Block>>) CHESTS_BY_RARITY.get(rarity);
	}
	
	public static Optional<RegistryObject<Block>> getChest(ResourceLocation name) {
		return Optional.ofNullable(CHESTS_BY_NAME.get(name));
	}
	
	/**
	 * Select a chest randomly within a particular rarity
	 * @param random
	 * @param rarity
	 * @return
	 */
	public static Optional<RegistryObject<Block>> selectChest(final Random random, final IRarity rarity) {
		Treasure.LOGGER.debug("attempting to get chest list for rarity -> {}", rarity);
		List<RegistryObject<Block>> chestList = getChest(rarity);
		Treasure.LOGGER.debug("size of chests lists -> {}", chestList.size());
		RegistryObject<Block> chest = null;
		if (!chestList.isEmpty()) {
			chest = chestList.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));
		}
		return Optional.ofNullable(chest);
	}
}
