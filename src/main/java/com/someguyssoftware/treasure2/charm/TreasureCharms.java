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

/**
 * 
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class TreasureCharms {
	// TODO additional registering (ie tags) needs to take place in a setup event  method
	// TODO probably need a new class that hold the max level and the spawn level range
	private static final Map<ResourceLocation, Integer> ITEM_TO_CHARM_LEVELS = new HashMap<>();
	
	private static final Multimap<Class<?>, String> EVENT_CHARM_MAP =  ArrayListMultimap.create();

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
	
	static {
//		ITEM_TO_CHARM_LEVELS.put(TreasureItems.TOPAZ.getRegistryName(), Integer.valueOf(4));
		ITEM_TO_CHARM_LEVELS.put(Items.DIAMOND.getRegistryName(), Integer.valueOf(5));
//		ITEM_TO_CHARM_LEVELS.put(TreasureItems.ONYX.getRegistryName(), Integer.valueOf(6));
		ITEM_TO_CHARM_LEVELS.put(Items.EMERALD.getRegistryName(), Integer.valueOf(7));
		ITEM_TO_CHARM_LEVELS.put(TreasureItems.RUBY.getRegistryName(), Integer.valueOf(9));
		ITEM_TO_CHARM_LEVELS.put(TreasureItems.SAPPHIRE.getRegistryName(), Integer.valueOf(11));
		ITEM_TO_CHARM_LEVELS.put(TreasureItems.WHITE_PEARL.getRegistryName(), Integer.valueOf(9));
		ITEM_TO_CHARM_LEVELS.put(TreasureItems.BLACK_PEARL.getRegistryName(), Integer.valueOf(11));
		
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
	
	/**
	 * Accessor wrapper method to return Optional<Integer>
	 * @param name
	 * @return
	 */
	public static Optional<Integer> getCharmLevel(ResourceLocation name) {
		if (name != null && ITEM_TO_CHARM_LEVELS.containsKey(name)) {
			return Optional.of(ITEM_TO_CHARM_LEVELS.get(name));
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
}
