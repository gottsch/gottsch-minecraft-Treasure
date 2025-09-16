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
package mod.gottsch.forge.treasure2.core.random;

import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * 
 * @author Mark Gottschling 8/26/2025
 *
 */
public class RarityAdjustingWeightedCollection extends AdjustingWeightedCollection<IRarityEntry> {

	private static final String COLLECTION = "collection";
	private static final String ORIGINAL = "original";

	public RarityAdjustingWeightedCollection() {
		super();
	}

	public RarityAdjustingWeightedCollection(AdjustingWeightedCollection<IRarityEntry> col) {
		super();
		this.collection = col.collection;
		this.original = col.original;
	}

	public RarityAdjustingWeightedCollection(Random random) {
		super(random);
	}

	/**
	 * convenience casting
	 */
	public RarityAdjustingWeightedCollection add(Integer weight, IRarityEntry item) {
		return (RarityAdjustingWeightedCollection) super.add(weight, item);
	}

	public RarityAdjustingWeightedCollection only(List<IRarityEntry> rarities) {
		return (RarityAdjustingWeightedCollection) super.only(rarities);
	}

	public RarityAdjustingWeightedCollection only(IRarityEntry... rarities) {
		return only(Arrays.stream(rarities).toList());
	}

	// TODO add adjustExcept()

	/**
	 * Convenience casting
	 */
	public RarityAdjustingWeightedCollection add(Pair<Integer, Integer> weightPair, IRarityEntry item) {
		return (RarityAdjustingWeightedCollection) super.add(weightPair, item);
	}

	// TODO filter() methods that take a list in and built a new collection with only those values.
	// 		this would be a view which doesn't change the backing values.

	/**
	 * 
	 * @return
	 */
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();

		ListTag originalList = new ListTag();
		original.forEach((key, value) -> {
			CompoundTag element = new CompoundTag();
			TreasureRarities.getKey(key).ifPresent(rarityName -> {
				element.putString("key", rarityName.toString());
				element.putInt("right", value.getRight());
				originalList.add(element);
			});

		});
		tag.put("original", originalList);
		
		return tag;
	}
	
	/**
	 * load() will load and set the original.right collection, and then add() to the weighted collection
	 * so that the properly weights are used and built.
	 * @param tag
	 */
	public void load(CompoundTag tag) {
		collection.clear();

		if (tag.contains("original")) {
			ListTag collectionList = tag.getList("original", Tag.TAG_COMPOUND);
			collectionList.forEach(element -> {
				CompoundTag e = (CompoundTag)element;
				if (e.contains("key") && e.contains("right")) {
					TreasureRarities.getRarityByName(ModUtil.asLocation(e.getString("key"))).ifPresent(rarity -> {
					Integer weight = e.getInt("right");

					Pair<Integer, Integer> persistedPair = original.get(rarity);
					super.add(Pair.of(persistedPair.getLeft(), weight), rarity);
					});
				}
			});

		}
	}
}
