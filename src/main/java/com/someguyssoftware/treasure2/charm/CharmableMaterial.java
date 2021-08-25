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

import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class CharmableMaterial {
	private int id;
	private ResourceLocation name;
	private int maxLevel;
	private int minSpawnLevel = 1;
	private double levelMultiplier = 1.0;
	
	/**
	 * 
	 * @param name
	 * @param maxLevel
	 * @param levelRange
	 */
	public CharmableMaterial(int id, String name, int maxLevel, int minSpawnLevel) {
		this.id = id;
		this.name = ModUtils.asLocation(name);
		this.maxLevel = maxLevel;
		this.minSpawnLevel = minSpawnLevel;
	}

	public CharmableMaterial(int id, String name, int maxLevel) {
		this(id, name, maxLevel, 1);
	}
	
	/**
	 * Full constructor
	 * @param name
	 * @param maxLevel
	 * @param levelRange
	 */
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel, int minSpawnLevel, double levelMultiplier) {
		this.id = id;
		this.name = name;
		this.maxLevel = maxLevel;
		this.minSpawnLevel = minSpawnLevel;
		this.levelMultiplier = levelMultiplier;
	}
	
	/**
	 * 
	 * @param name
	 * @param maxLevel
	 * @param levelRange
	 */
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel, int minSpawnLevel) {
		this(id, name, maxLevel, minSpawnLevel, 1D);
	}
	
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel) {
		this(id, name, maxLevel, 1, 1D);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return new TranslationTextComponent("charmable_component." + name.toString().replace(":", ".")).getString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getMinSpawnLevel() {
		return minSpawnLevel;
	}

	public void setMinSpawnLevel(int minSpawnLevel) {
		this.minSpawnLevel = minSpawnLevel;
	}

	public double getLevelMultiplier() {
		return levelMultiplier;
	}

	public void setLevelMultiplier(double levelMultiplier) {
		this.levelMultiplier = levelMultiplier;
	}
}
