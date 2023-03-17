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

import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * 
 * @author Mark Gottschling Feb 10, 2023
 *
 */
public class RarityLevelWeightedCollection extends LevelWeightedCollection<IRarity> {

	public RarityLevelWeightedCollection() {
		super();
	}
	
	public RarityLevelWeightedCollection(Random random) {
		super(random);
	}
	
	public RarityLevelWeightedCollection add(Integer weight, IRarity item) {
		return this.add(weight, item);
	}
	
	public RarityLevelWeightedCollection add(Pair<Integer, Integer> weightPair, IRarity item) {
		return this.add(weightPair, item);
	}
	
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
	
	public void load(CompoundTag list) {
		// TODO
	}
}
