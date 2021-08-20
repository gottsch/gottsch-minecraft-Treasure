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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class TreasureCharms {
	
	// TODO this probably can go away in favor of method in Charm
	private static final Multimap<Class<?>, String> EVENT_CHARM_MAP =  ArrayListMultimap.create();
	
	private static final Map<ResourceLocation, CharmableMaterial> METAL_REGISTRY = new HashMap<>();
	private static final Map<ResourceLocation, CharmableMaterial> GEM_REGISTRY = new HashMap<>();
	
	public static CharmableMaterial COPPER = new CharmableMaterial(1, "copper", 2);
	public static CharmableMaterial SILVER = new CharmableMaterial(2, "silver", 3);
	public static CharmableMaterial GOLD = new CharmableMaterial(3, "gold", 4);

	public static CharmableMaterial DIAMOND;
	public static CharmableMaterial EMERALD;
	public static CharmableMaterial TOPAZ;
	public static CharmableMaterial ONYX;
	public static CharmableMaterial RUBY;
	public static CharmableMaterial SAPPHIRE;
	public static CharmableMaterial WHITE_PEARL;
	public static CharmableMaterial BLACK_PEARL;
	
//	public static final BaseMaterial2 COPPER = new BaseMaterial2("copper", 2, 1D, 2D);
//	public static final BaseMaterial2 SILVER = new BaseMaterial2("silver", 3, 1D, 3D);
//	public static final BaseMaterial2 GOLD = new BaseMaterial2("gold", 4, 2D, 4D);
	
	public static final ICharm HEALING_1 = makeHealing(1);
	public static final ICharm HEALING_2 = makeHealing(2);
	public static final ICharm HEALING_3 = makeHealing(3);
	public static final ICharm HEALING_4 = makeHealing(4);
	public static final ICharm HEALING_5 = makeHealing(5);
	public static final ICharm HEALING_6 = makeHealing(6);
	public static final ICharm HEALING_7 = makeHealing(7);
	public static final ICharm HEALING_8 = makeHealing(8);
	public static final ICharm HEALING_9 = makeHealing(9);
	public static final ICharm HEALING_10 = makeHealing(10);
	public static final ICharm HEALING_11 = makeHealing(11);
	public static final ICharm HEALING_12 = makeHealing(12);
	public static final ICharm HEALING_13 = makeHealing(13);
	public static final ICharm HEALING_14 = makeHealing(14);
	public static final ICharm HEALING_15 = makeHealing(15);
	
	public static final ICharm SHIELDING_1 = makeShielding(1);
	public static final ICharm SHIELDING_2 = makeShielding(2);
	public static final ICharm SHIELDING_3 = makeShielding(3);
	public static final ICharm SHIELDING_4 = makeShielding(4);
	public static final ICharm SHIELDING_5 = makeShielding(5);
	public static final ICharm SHIELDING_6 = makeShielding(6);
	public static final ICharm SHIELDING_7 = makeShielding(7);
	public static final ICharm SHIELDING_8 = makeShielding(8);
	public static final ICharm SHIELDING_9 = makeShielding(9);
	public static final ICharm SHIELDING_10 = makeShielding(10);
	public static final ICharm SHIELDING_11 = makeShielding(11);
	public static final ICharm SHIELDING_12 = makeShielding(12);
	public static final ICharm SHIELDING_13 = makeShielding(13);
	public static final ICharm SHIELDING_14 = makeShielding(14);
	public static final ICharm SHIELDING_15 = makeShielding(15);
	
	static {
		// register
		METAL_REGISTRY.put(COPPER.getName(), COPPER);
		METAL_REGISTRY.put(SILVER.getName(), SILVER);
		METAL_REGISTRY.put(GOLD.getName(), GOLD);
		
		EVENT_CHARM_MAP.put(LivingUpdateEvent.class, HealingCharm.HEALING_TYPE);
		
		TreasureCharmRegistry.register(HEALING_1);
		TreasureCharmRegistry.register(HEALING_2);
		TreasureCharmRegistry.register(HEALING_3);
		TreasureCharmRegistry.register(HEALING_4);
		TreasureCharmRegistry.register(HEALING_5);
		TreasureCharmRegistry.register(HEALING_6);
		TreasureCharmRegistry.register(HEALING_7);
		TreasureCharmRegistry.register(HEALING_8);
		TreasureCharmRegistry.register(HEALING_9);
		TreasureCharmRegistry.register(HEALING_10);
		TreasureCharmRegistry.register(HEALING_11);
		TreasureCharmRegistry.register(HEALING_12);
		TreasureCharmRegistry.register(HEALING_13);
		TreasureCharmRegistry.register(HEALING_14);
		TreasureCharmRegistry.register(HEALING_15);
		
		TreasureCharmRegistry.register(SHIELDING_1);
		TreasureCharmRegistry.register(SHIELDING_2);
		TreasureCharmRegistry.register(SHIELDING_3);
		TreasureCharmRegistry.register(SHIELDING_4);
		TreasureCharmRegistry.register(SHIELDING_5);
		TreasureCharmRegistry.register(SHIELDING_6);
		TreasureCharmRegistry.register(SHIELDING_7);
		TreasureCharmRegistry.register(SHIELDING_8);
		TreasureCharmRegistry.register(SHIELDING_9);
		TreasureCharmRegistry.register(SHIELDING_10);
		TreasureCharmRegistry.register(SHIELDING_11);
		TreasureCharmRegistry.register(SHIELDING_12);
		TreasureCharmRegistry.register(SHIELDING_13);
		TreasureCharmRegistry.register(SHIELDING_14);
		TreasureCharmRegistry.register(SHIELDING_15);
	}
	
	/**
	 * The gem/sourceItem portion of a charm capability takes in a RegistryName of an Item,
	 * there it needs to be setup and registered in a FML event so that the Items being referenced
	 * are already create and registered.
	 * @param event
	 */
	public static void setup(FMLCommonSetupEvent event) {
//		TOPAZ = new CharmableMaterial(1, Items.TOPAZ.getRegistryName(), 4, 1);
		DIAMOND = new CharmableMaterial(2, Items.DIAMOND.getRegistryName(), 5, 3);
//		ONYX = new CharmableMaterial(3, Items.ONYX.getRegistryName(), 6, 3);
		EMERALD = new CharmableMaterial(4, Items.EMERALD.getRegistryName(), 7, 3);
		RUBY = new CharmableMaterial(5, TreasureItems.RUBY.getRegistryName(), 9, 4);
		SAPPHIRE = new CharmableMaterial(6, TreasureItems.SAPPHIRE.getRegistryName() , 11, 6);
		WHITE_PEARL = new CharmableMaterial(7, TreasureItems.WHITE_PEARL.getRegistryName() , 9, 8);
		BLACK_PEARL = new CharmableMaterial(8, TreasureItems.BLACK_PEARL.getRegistryName() , 11, 10);
		
		// regerister
		GEM_REGISTRY.put(DIAMOND.getName(), DIAMOND);
		GEM_REGISTRY.put(EMERALD.getName(), EMERALD);
//		GEM_REGISTRY.put(TOPAZ.getName(), TOPAZ);
//		GEM_REGISTRY.put(ONYX.getName(), ONYX);
		GEM_REGISTRY.put(RUBY.getName(), RUBY);
		GEM_REGISTRY.put(SAPPHIRE.getName(), SAPPHIRE);
		GEM_REGISTRY.put(WHITE_PEARL.getName(), WHITE_PEARL);
		GEM_REGISTRY.put(BLACK_PEARL.getName(), WHITE_PEARL);
	}
	
	/**
	 * Convenience method to build Healing Charm.
	 * @param level
	 * @return
	 */
	public static ICharm makeHealing(int level) {
		return new HealingCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
	}
	
	public static ICharm makeShielding(int level) {
		return new ShieldingCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.percent = level < 4 ? 0.5 : level < 7 ? 0.6 : level < 10 ? 0.7 :  0.8;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
	}
	
	public static Optional<CharmableMaterial> getBaseMaterial(ResourceLocation name) {
		if (name != null && METAL_REGISTRY.containsKey(name)) {
			return Optional.of(METAL_REGISTRY.get(name));
		}
		return Optional.empty();
	}
	
	/**
	 * Accesor wrapp method to return if an charm type is registered to an event.
	 * @param event
	 * @param type
	 * @return
	 */
	public static boolean isCharmEventRegistered(Class<?> event, String type) {
		return EVENT_CHARM_MAP.containsEntry(event, type);
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
}
