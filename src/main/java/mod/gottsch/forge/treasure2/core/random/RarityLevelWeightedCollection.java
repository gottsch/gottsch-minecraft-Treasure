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
package mod.gottsch.forge.treasure2.core.random;

import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/**
 * 
 * @author Mark Gottschling Feb 10, 2023
 *
 */
public class RarityLevelWeightedCollection extends LevelWeightedCollection<IRarity> {

	public RarityLevelWeightedCollection() {
		super();
	}

	public RarityLevelWeightedCollection(LevelWeightedCollection<IRarity> col) {
		super();
		this.collection = col.collection;
		this.original = col.original;
	}
	
	public RarityLevelWeightedCollection(Random random) {
		super(random);
	}
	
	/**
	 * Convenience casting
	 */
	public RarityLevelWeightedCollection add(Integer weight, IRarity item) {
		return (RarityLevelWeightedCollection) super.add(weight, item);
	}
	
	/**
	 * Convenience casting
	 */
	public RarityLevelWeightedCollection add(Pair<Integer, Integer> weightPair, IRarity item) {
		return (RarityLevelWeightedCollection) super.add(weightPair, item);
	}
	
	/**
	 * 
	 * @return
	 */
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		
		ListTag originalList = new ListTag();
		original.forEach((key, value) -> {
			// create an element tag
			CompoundTag element = new CompoundTag();
			element.putString("key", key.getName());
			element.putInt("left", value.getLeft());
			element.putInt("right", value.getRight());
			// add element to list
			originalList.add(element);
		});
		tag.put("original", originalList);
		
		ListTag collectionList = new ListTag();
		collection.getMap().forEach((key, value) -> {
			CompoundTag element = new CompoundTag();
			element.putDouble("key", key);
			element.putString("value", value.getName());
			collectionList.add(element);
		});
		tag.put("collection", collectionList);
		
		return tag;
	}
	
	/**
	 * 
	 * @param tag
	 */
	public void load(CompoundTag tag) {
		if (tag.contains("original")) {
			ListTag originalList = tag.getList("original", Tag.TAG_COMPOUND);
			originalList.forEach(element -> {
				CompoundTag elementTag = (CompoundTag)element;
				if (elementTag.contains("key")) {
					String key = elementTag.getString("key");
					int left = 0;
					int right = 0;
					if (elementTag.contains("left")) {
						left = elementTag.getInt("left");
					}
					if (elementTag.contains("right")) {
						right = elementTag.getInt("right");
					}
					Optional<IRarity> rarity = TreasureApi.getRarity(key);
					if (rarity.isPresent()) {
						getOriginal().put(rarity.get(), Pair.of(left, right));
					}
				}
			});
		}
		/*
		 *  don't bother to load collection as it is a view of the original.getPair().getRight();
		 */
		// clear the collection
		collection.clear();
		getOriginal().forEach((rarity, pair) -> {
			collection.add(pair.getRight(), rarity);
		});
//		if (tag.contains("collection")) {
//			ListTag originalList = tag.getList("collection", Tag.TAG_COMPOUND);
//			originalList.forEach(element -> {
//				CompoundTag elementTag = (CompoundTag)element;
//				if (elementTag.contains("key")) {
//					double key = elementTag.getDouble("key");
//					String value = "";
//					if (elementTag.contains("value")) {
//						value = elementTag.getString("value");
//					}
//					Optional<IRarity> rarity = TreasureApi.getRarity(value);
//					if (rarity.isPresent()) {
//						collection.add(Integer.valueOf((int)key), rarity.get());
//					}
//				}
//			});
//		}
	}
}
