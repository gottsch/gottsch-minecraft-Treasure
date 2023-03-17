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
package mod.gottsch.forge.treasure2.core.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.random.WeightedCollection;


/**
 * This class was designed with the assumption that there is a 1-1 mapping from T (rarity) to weight.
 * 
 * @author Mark Gottschling on Sep 22, 2022
 *
 */
public class LevelWeightedCollection<T> {
	/*
	 * the mapping from T to weight pairs
	 * where the left is the original value, and right is the adjusted value
	 */
	Map<T, Pair<Integer, Integer>> original;
	/*
	 * the weighted collection of weight to T
	 */
	WeightedCollection<Integer, T> collection;

	/**
	 * 
	 */
	public LevelWeightedCollection() {
		this(new Random());
	}

	/**
	 * 
	 * @param random
	 */
	public LevelWeightedCollection(Random random) {
		original = new HashMap<>();
		collection = new WeightedCollection<Integer, T>(random);
	}

	/**
	 * 
	 * @param weight
	 * @param item
	 * @return
	 */
	public LevelWeightedCollection<T> add(Integer weight, T item) {
		original.put(item, Pair.of(weight, weight));
		collection.add(weight, item);
		return this;
	}
	
	/**
	 * 
	 * @param weightPair
	 * @param item
	 * @return
	 */
	public LevelWeightedCollection<T> add(Pair<Integer, Integer> weightPair, T item) {
		original.put(item, weightPair);
		collection.add(weightPair.getRight(), item);
		return this;
	}

	/**
	 * 
	 * @param incrementAmount
	 * @param type
	 * @return
	 */
	public LevelWeightedCollection<T> adjustExcept(Integer incrementAmount, T type) {
		LevelWeightedCollection<T> col = new LevelWeightedCollection<>();
		original.forEach((key, pair) -> {
			if (key.equals(type)) {	
				// add original value to the collection
				col.collection.add(pair.getLeft(), key);
				// reset the value of the pair
				col.original.put(key, Pair.of(pair.getLeft(), pair.getLeft()));
			}
			else {
				// add adjusted value to the collection
				col.collection.add(pair.getRight() + incrementAmount, key);
				
				// adjust the value of the pair (NOTE value == right)
				col.original.put(key, Pair.of(pair.getLeft(), pair.getRight() + incrementAmount));
			}
		});
		// update original
		return col;
	}
	
	/**
	 * 
	 * @return
	 */
	public T next() {
		return collection.next();
	}
	
	public List<String> dump() {
		List<String> list = new ArrayList<>();
		list.add("Collection:");
		this.collection.getMap().forEach((key, value) -> {
			list.add(String.format("%s = %s", key, value));
		});
		list.add("Pairs:");
		this.original.forEach((left, right) -> {
			list.add(String.format("left -> %s, right ->  %s", left, right));
		});
		return list;
	}

	public Map<T, Pair<Integer, Integer>> getOriginal() {
		return original;
	}
}
