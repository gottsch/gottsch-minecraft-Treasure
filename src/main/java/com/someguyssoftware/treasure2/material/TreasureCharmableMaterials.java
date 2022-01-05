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
package com.someguyssoftware.treasure2.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.IAdornment;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author Mark Gottschling on Jan 3, 2022
 *
 */
public class TreasureCharmableMaterials {
	// TODO have Size property ("normal", "great", "lord") or have a separate material -> GREAT_IRON = new PreciousMaterial(1, ResourceLocationUtil.create("iron"), 5, 1, 0.5D);
	
	public static final CharmableMaterial IRON = new CharmableMaterial(1, ResourceLocationUtil.create("iron"), 2, 1, 0.25D);
	public static final CharmableMaterial COPPER = new CharmableMaterial(2, ResourceLocationUtil.create("copper"), 4, 1, 0.5D);
	public static final CharmableMaterial SILVER = new CharmableMaterial(3, ResourceLocationUtil.create("silver"), 6, 1, 0.75D);
	public static final CharmableMaterial GOLD = new CharmableMaterial(4, ResourceLocationUtil.create("gold"), 8);
	public static final CharmableMaterial BLOOD = new CharmableMaterial(5, ResourceLocationUtil.create("blood"), 10);
	public static final CharmableMaterial BONE = new CharmableMaterial(4, ResourceLocationUtil.create("bone"), 12);
	public static final CharmableMaterial ATIUM = new CharmableMaterial(4, ResourceLocationUtil.create("atium"), 14);
	public static final CharmableMaterial CHARM_BOOK = new CharmableMaterial(100, ResourceLocationUtil.create("charm_book"), 100);
	
	public static final CharmableMaterial GREAT_IRON = new CharmableMaterial(1, ResourceLocationUtil.create("great_iron"), 5, 3, 0.5D);
	public static final CharmableMaterial GREAT_COPPER = new CharmableMaterial(1, ResourceLocationUtil.create("great_copper"), 7, 5, 0.75D);
	// ...
		
	// TEST
	public static class AdornmentSize {
		private int id;
		private ResourceLocation name;
		private double maxLevelMultiplier = 1.0;
		private double minSpawnLevelMultiplier = 1.0;
		private double levelMultiplier = 1.0;
		
		public AdornmentSize(int id, ResourceLocation name) {
			this.id = id;
			this.name = name;
		}
		
		// use multiplier ?
		public void setMaxLevelMultiplier(double value) {
			this.maxLevelMultiplier = value;
		}
		
		// OR getter method
		public double getMaxLevel(double value) {
			return value + 3;
		}

	}
	
	// TEST
	public static final AdornmentSize STANDARD = new AdornmentSize(1, ResourceLocationUtil.create("standard"));
	public static final AdornmentSize GREAT = new AdornmentSize(1, ResourceLocationUtil.create("great"));
	static {
		GREAT.setMaxLevelMultiplier(2.5);
	}
	public static final AdornmentSize LORDS = new AdornmentSize(1, ResourceLocationUtil.create("lords"));
	
	public static CharmableMaterial DIAMOND;
	public static CharmableMaterial EMERALD;
	public static CharmableMaterial AMETHYST;
	public static CharmableMaterial ONYX;
	public static CharmableMaterial RUBY;
	public static CharmableMaterial SAPPHIRE;
	public static CharmableMaterial WHITE_PEARL;
	public static CharmableMaterial BLACK_PEARL;
	
	private static final Map<ResourceLocation, CharmableMaterial> METAL_REGISTRY = new HashMap<>();
	private static final Map<ResourceLocation, CharmableMaterial> GEM_REGISTRY = new HashMap<>();
	
	static {
		// register
		METAL_REGISTRY.put(IRON.getName(), IRON);
		METAL_REGISTRY.put(COPPER.getName(), COPPER);
		METAL_REGISTRY.put(SILVER.getName(), SILVER);
		METAL_REGISTRY.put(GOLD.getName(), GOLD);
	}
	
	// TODO register to event AFTER item are setup.
	public static void setup() {
		AMETHYST = new CharmableMaterial(1, TreasureItems.AMETHYST.getRegistryName(), 2, 1);
		DIAMOND = new CharmableMaterial(2, Items.DIAMOND.getRegistryName(), 4, 3);
		ONYX = new CharmableMaterial(3, TreasureItems.ONYX.getRegistryName(), 6, 3);
		EMERALD = new CharmableMaterial(4, Items.EMERALD.getRegistryName(), 8, 3);
		RUBY = new CharmableMaterial(5, TreasureItems.RUBY.getRegistryName(), 10, 4);
		SAPPHIRE = new CharmableMaterial(6, TreasureItems.SAPPHIRE.getRegistryName() , 12, 6);
		WHITE_PEARL = new CharmableMaterial(7, TreasureItems.WHITE_PEARL.getRegistryName() , 10, 8);
		BLACK_PEARL = new CharmableMaterial(8, TreasureItems.BLACK_PEARL.getRegistryName() , 12, 10);
		
		// regerister
		GEM_REGISTRY.put(DIAMOND.getName(), DIAMOND);
		GEM_REGISTRY.put(EMERALD.getName(), EMERALD);
		GEM_REGISTRY.put(AMETHYST.getName(), AMETHYST);
		GEM_REGISTRY.put(ONYX.getName(), ONYX);
		GEM_REGISTRY.put(RUBY.getName(), RUBY);
		GEM_REGISTRY.put(SAPPHIRE.getName(), SAPPHIRE);
		GEM_REGISTRY.put(WHITE_PEARL.getName(), WHITE_PEARL);
		GEM_REGISTRY.put(BLACK_PEARL.getName(), BLACK_PEARL);
	}
	
	public static Optional<CharmableMaterial> getBaseMaterial(ResourceLocation name) {
		if (name != null && METAL_REGISTRY.containsKey(name)) {
			return Optional.of(METAL_REGISTRY.get(name));
		}
		return Optional.empty();
	}
	
	/**
	 * Accessor wrapper method to return Optional sourceItem
	 * @param name
	 * @return
	 */
	public static Optional<CharmableMaterial> getSourceItem(ResourceLocation sourceItem) {
		if (sourceItem != null && GEM_REGISTRY.containsKey(sourceItem)) {
			return Optional.of(GEM_REGISTRY.get(sourceItem));
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param sourceItem
	 * @return
	 */
	public static boolean isSourceItemRegistered(ResourceLocation sourceItem) {
		Optional<CharmableMaterial> material = getSourceItem(sourceItem);
		if (material.isPresent()) {
			return true;
		}
		return false;
	}
}
