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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 3, 2022
 *
 */
public class TreasureCharmableMaterials {

	// TODO add maxRepairs, repairs, repairable to Durability Cap ?? or repairable and maxRepairs to CharmableMaterial (ie things made of this can be repaired), repairs to Durability
	// Charmable might even have an override for repairable? for special items that normally are repairable but don't want them to be
	// TODO add durability factor property to CharmableMaterial to calculate the final durability of an item?
	//  ex. IRON.factor = 20. An iron ring has (1, 1, 1) charm slots = 3 slots, and a max level of 2, so base durability = 3*2*20 = 120 durability. With a maxRepairs of 5x. So iron would last a long time.
	/*
	 * IRON
	 * 	durability=75
	 * 	maxLevel=2
	 *	maxRepairs=5
	 *	slots=2
	 *	base = 2*2*75 = 300
	 *
	 * COPPER
	 * 	durability=10
	 * 	maxLevel=4
	 * 	maxRepairs=4
	 * 	slots=2+1
	 * 	base=3*4*10 = 120
	 * 
	 * SILVER
	 * 	durability=20
	 * 	maxLevel=6
	 * 	maxRepairs=3
	 * 	slots=2+2
	 * 	base=4*6*20 = 480
	 * 
	 * GOLD
	 * 	durability=25
	 * 	maxLevel=8
	 * 	maxRepairs=2
	 * 	slots=2+3
	 * 	base=5*8*25 = 1000
	 */
	// can't recharge innates so maybe they should be calculated differently.
	
	public static final CharmableMaterial IRON = new CharmableMaterial(1, ModUtils.asLocation("iron"), 2, 1, 0.25D, 75, 5);
	public static final CharmableMaterial COPPER = new CharmableMaterial(2, ModUtils.asLocation("copper"), 4, 1, 0.5D, 10, 4);
	public static final CharmableMaterial SILVER = new CharmableMaterial(3, ModUtils.asLocation("silver"), 6, 3, 0.75D, 20, 3);
	public static final CharmableMaterial GOLD = new CharmableMaterial(4, ModUtils.asLocation("gold"), 8, 5, 1D, 25, 2);
	public static final CharmableMaterial BLOOD = new CharmableMaterial(5, ModUtils.asLocation("blood"), 10, 6, 1D, 15, 1);
	public static final CharmableMaterial BONE = new CharmableMaterial(6, ModUtils.asLocation("bone"), 12, 8, 1.15D, 50, 0);
	public static final CharmableMaterial BLACK = new CharmableMaterial(7, ModUtils.asLocation("black"), 12, 10, 1.15D, 35, 2);
	public static final CharmableMaterial ATIUM = new CharmableMaterial(8, ModUtils.asLocation("atium"), 14, 10, 1.25D, 100, 5);
	
	public static final CharmableMaterial CHARM_BOOK = new CharmableMaterial(100, ModUtils.asLocation("charm_book"), 100);
	
	// special for high-end charm construction
	public static final CharmableMaterial LEGENDARY = new CharmableMaterial(9, ModUtils.asLocation("legendary"), 24);
	public static final CharmableMaterial MYTHICAL = new CharmableMaterial(10, ModUtils.asLocation("mythical"), 28);
	
	
	public static CharmableMaterial DIAMOND;
	public static CharmableMaterial EMERALD;
	public static CharmableMaterial TOPAZ;
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
		METAL_REGISTRY.put(BLOOD.getName(), BLOOD);
		METAL_REGISTRY.put(BONE.getName(), BONE);
		METAL_REGISTRY.put(BLACK.getName(), BLACK);
//		METAL_REGISTRY.put(ATIUM.getName(), ATIUM);
		
		METAL_REGISTRY.put(LEGENDARY.getName(), LEGENDARY);
		METAL_REGISTRY.put(MYTHICAL.getName(), MYTHICAL);
	}
	
	public static class SortByLevel implements Comparator<CharmableMaterial> {
		@Override
		public int compare(CharmableMaterial p1, CharmableMaterial p2) {
			return Integer.compare(p1.getMaxLevel(), p2.getMaxLevel());
		}
	};

	// comparator on level
	public static Comparator<CharmableMaterial> levelComparator = new SortByLevel();
	
	// TODO register to event AFTER item are setup.
	// TODO that poses as problem as this charmable material can't be used as the source
	public static void setup() {
		TOPAZ = new CharmableMaterial(1, TreasureItems.TOPAZ.get().getRegistryName(), 2, 1);
		DIAMOND = new CharmableMaterial(2, Items.DIAMOND.getRegistryName(), 4, 3);
		ONYX = new CharmableMaterial(3, TreasureItems.ONYX.get().getRegistryName(), 6, 3);
		EMERALD = new CharmableMaterial(4, Items.EMERALD.getRegistryName(), 8, 3);
		RUBY = new CharmableMaterial(5, TreasureItems.RUBY.get().getRegistryName(), 10, 4);
		SAPPHIRE = new CharmableMaterial(6, TreasureItems.SAPPHIRE.get().getRegistryName() , 12, 6);
		WHITE_PEARL = new CharmableMaterial(7, TreasureItems.WHITE_PEARL.get().getRegistryName() , 10, 8);
		BLACK_PEARL = new CharmableMaterial(8, TreasureItems.BLACK_PEARL.get().getRegistryName() , 12, 10);
		
		// regerister
		GEM_REGISTRY.put(DIAMOND.getName(), DIAMOND);
		GEM_REGISTRY.put(EMERALD.getName(), EMERALD);
		GEM_REGISTRY.put(TOPAZ.getName(), TOPAZ);
		GEM_REGISTRY.put(ONYX.getName(), ONYX);
		GEM_REGISTRY.put(RUBY.getName(), RUBY);
		GEM_REGISTRY.put(SAPPHIRE.getName(), SAPPHIRE);
		GEM_REGISTRY.put(WHITE_PEARL.getName(), WHITE_PEARL);
		GEM_REGISTRY.put(BLACK_PEARL.getName(), BLACK_PEARL);
	}
	
	/**
	 * TODO part of api
	 * @param key
	 * @param material
	 * @return
	 */
	public static boolean registerSourceItem(ResourceLocation key, CharmableMaterial material) {
		try {
			if (key == null || material == null) {
				return false;
			}
			GEM_REGISTRY.put(key, material);
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to register gem ->{} with material -> {}", key, material.getName());
			return false;
		}
		return true;
	}
	
	public static List<CharmableMaterial> getGemValues() {
		return new ArrayList<>(GEM_REGISTRY.values());
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
