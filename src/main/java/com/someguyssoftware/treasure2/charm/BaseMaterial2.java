/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.charm;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Aug 19, 2021
 *
 */
@Deprecated
public class BaseMaterial2 {
// use a class instead of an enum so more materials can easily be added. might have special charms/adornments like platinum etc.
	private ResourceLocation name;
	private int maxLevel;
	private Quantity levelRange;
	
	/**
	 * 
	 * @param name
	 * @param maxLevel
	 * @param levelRange
	 */
	public BaseMaterial2(String name, int maxLevel, Quantity levelRange) {
		this.name = ModUtils.asLocation(name);
		this.maxLevel = maxLevel;
		this.levelRange = levelRange;
	}
	
	/**
	 * 
	 * @param name
	 * @param maxLevel
	 * @param minRange
	 * @param maxRange
	 */
	public BaseMaterial2(String name, int maxLevel, Double minRange, Double maxRange) {
		this(name, maxLevel, new Quantity(minRange, maxRange));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return new TranslationTextComponent("base_material." + name.toString().replace(":", ".")).getString();
	}

	public ResourceLocation getName() {
		return name;
	}

	public void setName(ResourceLocation name) {
		this.name = name;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public Quantity getLevelRange() {
		return levelRange;
	}

	public void setLevelRange(Quantity levelRange) {
		this.levelRange = levelRange;
	}
	
	/**
	 * Convenience methods.
	 * @return
	 */
	public double getMinRangeLevel() {
		return getLevelRange().getMin();
	}
	
	public double getMaxRangeLevel() {
		return getLevelRange().getMax();
	}
}
