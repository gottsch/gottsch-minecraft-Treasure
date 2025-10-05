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
package mod.gottsch.forge.treasure2.core.cache;

import mod.gottsch.forge.treasure2.core.cache.data.TreasureChestCacheData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.*;

/**
 *
 * @author Mark Gottschling on 9/2/2025
 *
 */
public class TreasureChestCache {

	/*
	 * a Linked List registry to maintain descending age of insertion of chests
	 */
	private static final List<TreasureChestCacheData> CACHE = Collections.synchronizedList(new LinkedList<>());
	public static final String TAG_NAME = "TREASURE_CHEST_CACHE";

	// TODO expose as configurable/data property?
	private static int registrySize = 256;

	private TreasureChestCache() {}

	public synchronized static void cache(TreasureChestCacheData chestSpawn) {
		// if bigger than max size of registry, remove the first (oldest) element
		if (CACHE.size() >= getRegistrySize()) {
			Optional<TreasureChestCacheData> oldestDiscovered = CACHE.stream()
					.filter(TreasureChestCacheData::isDiscovered)
					.findFirst();

			if (oldestDiscovered.isPresent()) {
				CACHE.remove(oldestDiscovered.get());
			} else {
				CACHE.remove(0);
			}
		}
		CACHE.add(chestSpawn);
	}

	public static List<TreasureChestCacheData> getCache() {
		return List.copyOf(CACHE);
	}

	public static int getRegistrySize() {
		return registrySize;
	}

	public synchronized static void setRegistrySize(int size) {
		registrySize = size;
	}

	public static synchronized CompoundTag save(CompoundTag tag) {
		ListTag listTag = new ListTag();
		CACHE.forEach(chest -> {
			CompoundTag dataTag = new CompoundTag();
			chest.save(dataTag);
			listTag.add(dataTag);
		});
		tag.put(TAG_NAME, listTag);
		return tag;
	}

	public static synchronized void load(CompoundTag tag) {
		CACHE.clear();
		if (tag.contains(TAG_NAME)) {
			tag.getList(TAG_NAME, CompoundTag.TAG_COMPOUND).stream()
					.filter(elementTag -> elementTag instanceof CompoundTag)
					.map(elementTag -> (CompoundTag) elementTag)
					.forEach(compoundTag -> {
						TreasureChestCacheData data = new TreasureChestCacheData();
						data.load(compoundTag);
						CACHE.add(data);
					});
		}
	}
}
