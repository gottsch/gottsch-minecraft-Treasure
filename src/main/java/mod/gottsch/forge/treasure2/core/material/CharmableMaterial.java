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
package mod.gottsch.forge.treasure2.core.material;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Mark Gottschling on Jan 3, 2022
 *
 */
@SuppressWarnings("deprecation")
public class CharmableMaterial {
	public static final int DEFAULT_DURABILITY = 20;
	public static final int DEFAULT_MAX_REPAIRS = 3;
	
	private int id;
	private ResourceLocation name;
	// the maximum base level - used in formulas to calculate final max level
	private int maxLevel;
	// the minimum level an item made of this material can spawn/generate with
	private int minSpawnLevel = 1;
	// charm level multiplier
	private double levelMultiplier = 1.0;
	
	// base durability - used by Item formula to calculate its final durability
	private int durability;
	// the number of times something made of this material can be repaired
	private int maxRepairs;

	private Predicate<ItemStack> canAffix = p -> true;
	private Predicate<ItemStack> acceptsAffixer = p -> true;
		
	// TODO this really could use a builder
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param maxLevel
	 */
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel) {
		this(id, name, maxLevel, 1, 1D, DEFAULT_DURABILITY, DEFAULT_MAX_REPAIRS);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param maxLevel
	 * @param minSpawnLevel
	 */
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel, int minSpawnLevel) {
		this(id, name, maxLevel, minSpawnLevel, 1D, DEFAULT_DURABILITY, DEFAULT_MAX_REPAIRS );
	}
	
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel, int minSpawnLevel, double levelMultiplier) {
		this(id, name, maxLevel, minSpawnLevel, levelMultiplier, DEFAULT_DURABILITY, DEFAULT_MAX_REPAIRS );
	}
	
	/**
	 * Full constructor
	 * @param id
	 * @param name
	 * @param maxLevel
	 * @param minSpawnLevel
	 * @param levelMultiplier
	 * @param durability
	 * @param maxRepairs
	 */
	public CharmableMaterial(int id, ResourceLocation name, int maxLevel, int minSpawnLevel, double levelMultiplier, int durability, int maxRepairs) {
		this.id = id;
		this.name = name;
		this.maxLevel = maxLevel;
		this.minSpawnLevel = minSpawnLevel;
		this.levelMultiplier = levelMultiplier;
		this.durability = durability;
		this.maxRepairs = maxRepairs;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return new TranslationTextComponent("precious_material." + name.toString().replace(":", ".")).toString();
	}

	public boolean canAffix(ItemStack itemStack) {
		return canAffix.test(itemStack);
	}
	
	public void setCanAffix(Predicate<ItemStack> p) {
		this.canAffix = p;
	}
	
	public boolean acceptsAffixer(ItemStack itemStack) {
		return acceptsAffixer.test(itemStack);
	}
	
	public void setAcceptsAffixer(Predicate<ItemStack> p) {
		this.acceptsAffixer = p;
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

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getMaxRepairs() {
		return maxRepairs;
	}

	public void setMaxRepairs(int maxRepairs) {
		this.maxRepairs = maxRepairs;
	}
}
