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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.util.ModUtils;

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
	private static final Map<ResourceLocation, CharmableMaterial> METAL_REGISTRY = new HashMap<>();
	private static final Map<ResourceLocation, CharmableMaterial> GEM_REGISTRY = new HashMap<>();
	
	public static CharmableMaterial COPPER = new CharmableMaterial(1, ModUtils.asLocation("copper"), 2, 1, 0.5D);
	public static CharmableMaterial SILVER = new CharmableMaterial(2, ModUtils.asLocation("silver"), 3, 1, 0.75D);
	public static CharmableMaterial GOLD = new CharmableMaterial(3, ModUtils.asLocation("gold"), 4);

	public static CharmableMaterial DIAMOND;
	public static CharmableMaterial EMERALD;
	public static CharmableMaterial TOPAZ;
	public static CharmableMaterial ONYX;
	public static CharmableMaterial RUBY;
	public static CharmableMaterial SAPPHIRE;
	public static CharmableMaterial WHITE_PEARL;
	public static CharmableMaterial BLACK_PEARL;
	
	// charms	
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
	
	public static final ICharm GREATER_HEALING_8 = makeGreaterHealing(8);
	public static final ICharm GREATER_HEALING_9 = makeGreaterHealing(9);
	public static final ICharm GREATER_HEALING_10 = makeGreaterHealing(10);
	public static final ICharm GREATER_HEALING_11 = makeGreaterHealing(11);
	public static final ICharm GREATER_HEALING_12 = makeGreaterHealing(12);
	public static final ICharm GREATER_HEALING_13 = makeGreaterHealing(13);
	public static final ICharm GREATER_HEALING_14 = makeGreaterHealing(14);
	public static final ICharm GREATER_HEALING_15 = makeGreaterHealing(15);
	
	// special levels
	public static final ICharm GREATER_HEALING_20 = makeGreaterHealing(20);
		
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
	
	public static final ICharm AEGIS_1 = makeAegis(1);
	public static final ICharm AEGIS_2 = makeAegis(2);
	public static final ICharm AEGIS_3 = makeAegis(3);
	public static final ICharm AEGIS_4 = makeAegis(4);
	public static final ICharm AEGIS_5 = makeAegis(5);
	public static final ICharm AEGIS_6 = makeAegis(6);
	public static final ICharm AEGIS_7 = makeAegis(7);
	public static final ICharm AEGIS_8 = makeAegis(8);
	public static final ICharm AEGIS_9 = makeAegis(9);
	public static final ICharm AEGIS_10 = makeAegis(10);
	public static final ICharm AEGIS_11 = makeAegis(11);
	public static final ICharm AEGIS_12 = makeAegis(12);
	public static final ICharm AEGIS_13 = makeAegis(13);
	public static final ICharm AEGIS_14 = makeAegis(14);
	public static final ICharm AEGIS_15 = makeAegis(15);
	
	public static final ICharm FIRE_IMMUNITY_1 = makeFireImmunity(1);
	public static final ICharm FIRE_IMMUNITY_2 = makeFireImmunity(2);
	public static final ICharm FIRE_IMMUNITY_3 = makeFireImmunity(3);
	public static final ICharm FIRE_IMMUNITY_4 = makeFireImmunity(4);
	public static final ICharm FIRE_IMMUNITY_5 = makeFireImmunity(5);
	public static final ICharm FIRE_IMMUNITY_6 = makeFireImmunity(6);
	public static final ICharm FIRE_IMMUNITY_7 = makeFireImmunity(7);
	public static final ICharm FIRE_IMMUNITY_8 = makeFireImmunity(8);
	public static final ICharm FIRE_IMMUNITY_9 = makeFireImmunity(9);
	public static final ICharm FIRE_IMMUNITY_10 = makeFireImmunity(10);
	public static final ICharm FIRE_IMMUNITY_11 = makeFireImmunity(11);
	public static final ICharm FIRE_IMMUNITY_12 = makeFireImmunity(12);
	public static final ICharm FIRE_IMMUNITY_13 = makeFireImmunity(13);
	public static final ICharm FIRE_IMMUNITY_14 = makeFireImmunity(14);
	public static final ICharm FIRE_IMMUNITY_15 = makeFireImmunity(15);
	
	public static final ICharm FIRE_RESISTENCE_1 = makeFireResistence(1);
	public static final ICharm FIRE_RESISTENCE_2 = makeFireResistence(2);
	public static final ICharm FIRE_RESISTENCE_3 = makeFireResistence(3);
	public static final ICharm FIRE_RESISTENCE_4 = makeFireResistence(4);
	public static final ICharm FIRE_RESISTENCE_5 = makeFireResistence(5);
	public static final ICharm FIRE_RESISTENCE_6 = makeFireResistence(6);
	public static final ICharm FIRE_RESISTENCE_7 = makeFireResistence(7);
	public static final ICharm FIRE_RESISTENCE_8 = makeFireResistence(8);
	public static final ICharm FIRE_RESISTENCE_9 = makeFireResistence(9);
	public static final ICharm FIRE_RESISTENCE_10 = makeFireResistence(10);
	public static final ICharm FIRE_RESISTENCE_11 = makeFireResistence(11);
	public static final ICharm FIRE_RESISTENCE_12 = makeFireResistence(12);
	public static final ICharm FIRE_RESISTENCE_13 = makeFireResistence(13);
	public static final ICharm FIRE_RESISTENCE_14 = makeFireResistence(14);
	public static final ICharm FIRE_RESISTENCE_15 = makeFireResistence(15);
	
	public static final ICharm SATIETY_1 = makeSatiety(1);
	public static final ICharm SATIETY_2 = makeSatiety(2);
	public static final ICharm SATIETY_3 = makeSatiety(3);
	public static final ICharm SATIETY_4 = makeSatiety(4);
	public static final ICharm SATIETY_5 = makeSatiety(5);
	public static final ICharm SATIETY_6 = makeSatiety(6);
	public static final ICharm SATIETY_7 = makeSatiety(7);
	public static final ICharm SATIETY_8 = makeSatiety(8);
	public static final ICharm SATIETY_9 = makeSatiety(9);
	public static final ICharm SATIETY_10 = makeSatiety(10);
	public static final ICharm SATIETY_11 = makeSatiety(11);
	public static final ICharm SATIETY_12 = makeSatiety(12);
	public static final ICharm SATIETY_13 = makeSatiety(13);
	public static final ICharm SATIETY_14 = makeSatiety(14);
	public static final ICharm SATIETY_15 = makeSatiety(15);
	
	public static final ICharm LIFE_STRIKE_1 = makeLifeStrike(1);
	public static final ICharm LIFE_STRIKE_2 = makeLifeStrike(2);
	public static final ICharm LIFE_STRIKE_3 = makeLifeStrike(3);
	public static final ICharm LIFE_STRIKE_4 = makeLifeStrike(4);
	public static final ICharm LIFE_STRIKE_5 = makeLifeStrike(5);
	public static final ICharm LIFE_STRIKE_6 = makeLifeStrike(6);
	public static final ICharm LIFE_STRIKE_7 = makeLifeStrike(7);
	public static final ICharm LIFE_STRIKE_8 = makeLifeStrike(8);
	public static final ICharm LIFE_STRIKE_9 = makeLifeStrike(9);
	public static final ICharm LIFE_STRIKE_10 = makeLifeStrike(10);
	public static final ICharm LIFE_STRIKE_11 = makeLifeStrike(11);
	public static final ICharm LIFE_STRIKE_12 = makeLifeStrike(12);
	public static final ICharm LIFE_STRIKE_13 = makeLifeStrike(13);
	public static final ICharm LIFE_STRIKE_14 = makeLifeStrike(14);
	public static final ICharm LIFE_STRIKE_15 = makeLifeStrike(15);
	
	public static final ICharm REFLECTION_1 = makeReflection(1);
	public static final ICharm REFLECTION_2 = makeReflection(2);
	public static final ICharm REFLECTION_3 = makeReflection(3);
	public static final ICharm REFLECTION_4 = makeReflection(4);
	public static final ICharm REFLECTION_5 = makeReflection(5);
	public static final ICharm REFLECTION_6 = makeReflection(6);
	public static final ICharm REFLECTION_7 = makeReflection(7);
	public static final ICharm REFLECTION_8 = makeReflection(8);
	public static final ICharm REFLECTION_9 = makeReflection(9);
	public static final ICharm REFLECTION_10 = makeReflection(10);
	public static final ICharm REFLECTION_11 = makeReflection(11);
	public static final ICharm REFLECTION_12 = makeReflection(12);
	public static final ICharm REFLECTION_13 = makeReflection(13);
	public static final ICharm REFLECTION_14 = makeReflection(14);
	public static final ICharm REFLECTION_15 = makeReflection(15);

	public static final ICharm DRAIN_1 = makeDrain(1);
	public static final ICharm DRAIN_2 = makeDrain(2);
	public static final ICharm DRAIN_3 = makeDrain(3);
	public static final ICharm DRAIN_4 = makeDrain(4);
	public static final ICharm DRAIN_5 = makeDrain(5);
	public static final ICharm DRAIN_6 = makeDrain(6);
	public static final ICharm DRAIN_7 = makeDrain(7);
	public static final ICharm DRAIN_8 = makeDrain(8);
	public static final ICharm DRAIN_9 = makeDrain(9);
	public static final ICharm DRAIN_10 = makeDrain(10);
	public static final ICharm DRAIN_11 = makeDrain(11);
	public static final ICharm DRAIN_12 = makeDrain(12);
	public static final ICharm DRAIN_13 = makeDrain(13);
	public static final ICharm DRAIN_14 = makeDrain(14);
	public static final ICharm DRAIN_15 = makeDrain(15);
	
	public static final ICharm ILLUMINATION_3 = makeIllumination(3);
	public static final ICharm ILLUMINATION_6 = makeIllumination(6);
	public static final ICharm ILLUMINATION_9 = makeIllumination(9);
	public static final ICharm ILLUMINATION_12 = makeIllumination(12);
	public static final ICharm ILLUMINATION_15 = makeIllumination(15);
	
	// HARVESTING
	
	// curses
	public static final ICharm DECAY_1 = makeDecay(1);
	public static final ICharm DECAY_3 = makeDecay(3);
	public static final ICharm DECAY_5 = makeDecay(5);
	public static final ICharm DECAY_7 = makeDecay(7);
	
	public static final ICharm DECREPIT_1 = makeDecrepit(1);
	public static final ICharm DECREPIT_2 = makeDecrepit(2);
	public static final ICharm DECREPIT_3 = makeDecrepit(3);
	public static final ICharm DECREPIT_4 = makeDecrepit(4);
	public static final ICharm DECREPIT_5 = makeDecrepit(5);
	public static final ICharm DECREPIT_6 = makeDecrepit(6);
	public static final ICharm DECREPIT_7 = makeDecrepit(7);
	public static final ICharm DECREPIT_8 = makeDecrepit(8);
	
	public static final ICharm DIRT_FILL_2 = makeDirtFill(2);
	public static final ICharm DIRT_FILL_4 = makeDirtFill(4);
	public static final ICharm DIRT_FILL_6 = makeDirtFill(6);
	
	public static final ICharm DIRT_WALK_2 = makeDirtWalk(2);
	public static final ICharm DIRT_WALK_4 = makeDirtWalk(4);
	public static final ICharm DIRT_WALK_6 = makeDirtWalk(6);
	
	public static final ICharm RUIN_1 = makeRuin(1);
	public static final ICharm RUIN_3 = makeRuin(3);
	public static final ICharm RUIN_5 = makeRuin(5);
	public static final ICharm RUIN_7 = makeRuin(7);
	public static final ICharm RUIN_9 = makeRuin(9);
	public static final ICharm RUIN_11 = makeRuin(11);
	
	static {
		// register
		METAL_REGISTRY.put(COPPER.getName(), COPPER);
		METAL_REGISTRY.put(SILVER.getName(), SILVER);
		METAL_REGISTRY.put(GOLD.getName(), GOLD);
	}
	
	
	public static class SortByLevel implements Comparator<CharmableMaterial> {
		@Override
		public int compare(CharmableMaterial p1, CharmableMaterial p2) {
			return Integer.compare(p1.getMaxLevel(), p2.getMaxLevel());
		}
	};
	
	// comparator on level
	public static Comparator<CharmableMaterial> levelComparator = new SortByLevel();
	
	/**
	 * The gem/sourceItem portion of a charm capability takes in a RegistryName of an Item,
	 * there it needs to be setup and registered in a FML event so that the Items being referenced
	 * are already create and registered.
	 * @param event
	 */
	public static void setup(FMLCommonSetupEvent event) {
		TOPAZ = new CharmableMaterial(1, TreasureItems.TOPAZ.getRegistryName(), 4, 1);
		DIAMOND = new CharmableMaterial(2, Items.DIAMOND.getRegistryName(), 5, 3);
		ONYX = new CharmableMaterial(3, TreasureItems.ONYX.getRegistryName(), 6, 3);
		EMERALD = new CharmableMaterial(4, Items.EMERALD.getRegistryName(), 7, 3);
		RUBY = new CharmableMaterial(5, TreasureItems.RUBY.getRegistryName(), 9, 4);
		SAPPHIRE = new CharmableMaterial(6, TreasureItems.SAPPHIRE.getRegistryName() , 11, 6);
		WHITE_PEARL = new CharmableMaterial(7, TreasureItems.WHITE_PEARL.getRegistryName() , 9, 8);
		BLACK_PEARL = new CharmableMaterial(8, TreasureItems.BLACK_PEARL.getRegistryName() , 11, 10);
		
		// regerister
		GEM_REGISTRY.put(DIAMOND.getName(), DIAMOND);
		GEM_REGISTRY.put(EMERALD.getName(), EMERALD);
		GEM_REGISTRY.put(TOPAZ.getName(), TOPAZ);
		GEM_REGISTRY.put(ONYX.getName(), ONYX);
		GEM_REGISTRY.put(RUBY.getName(), RUBY);
		GEM_REGISTRY.put(SAPPHIRE.getName(), SAPPHIRE);
		GEM_REGISTRY.put(WHITE_PEARL.getName(), WHITE_PEARL);
		GEM_REGISTRY.put(BLACK_PEARL.getName(), WHITE_PEARL);
	}
	
//	public static Comparator<CharmableMaterial> levelComparator = new Comparator<CharmableMaterial>() {
//		@Override
//		public int compare(CharmableMaterial p1, CharmableMaterial p2) {
//			return Integer.compare(p1.getMaxLevel(), p2.getMaxLevel());
//		}
//	};
	
	public static List<CharmableMaterial> getGemValues() {
		return new ArrayList<>(GEM_REGISTRY.values());
	}
	
	/**
	 * Convenience method to build Healing Charm.
	 * @param level
	 * @return
	 */
	public static ICharm makeHealing(int level) {
		ICharm charm =  new HealingCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeGreaterHealing(int level) {
		ICharm charm = new GreaterHealingCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeShielding(int level) {
		ICharm charm =  new ShieldingCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.percent = level < 4 ? 0.5 : level < 7 ? 0.6 : level < 10 ? 0.7 :  0.8;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeAegis(int level) {
		ICharm charm = new AegisCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = false;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireImmunity(int level) {
		ICharm charm =  new FireImmunityCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = false;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireResistence(int level) {
		ICharm charm =  new FireResistenceCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.percent = level < 6 ? 0.3 : level < 11 ? 0.5 : 0.8;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeSatiety(int level) {
		ICharm charm =  new SatietyCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeLifeStrike(int level) {
		ICharm charm =  new LifeStrikeCharm.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.percent = level < 4 ? 1.2 : level < 7 ? 1.4 : level < 10 ? 1.6 :  level < 13 ? 1.8 : 2.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeReflection(int level) {
		ICharm charm =  new ReflectionCharm.Builder(level).with($ -> {
			$.value = level < 8 ? (level * 10.0 + 10.0) : ((level -7) * 10.0 + 10.0);
			$.percent = level < 3 ? 0.2 : level < 5 ? 0.35 : level < 7 ? 0.50 :  level <9 ? 0.65 : level < 11 ? 0.8 : level < 13 ? 0.95 : 1.1;
			$.duration = level < 4 ? 2D : level < 7 ? 3D : level < 10 ? 4D : level < 13 ? 5D : 6D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeDrain(int level) {
		ICharm charm =  new DrainCharm.Builder(level).with($ -> {
			$.value =  level < 8 ? (level * 10.0 + 10.0) : ((level -7) * 10.0 + 10.0);
			$.duration = level < 4 ? 2D : level < 7 ? 3D : level < 10 ? 4D : level < 13 ? 5D : 6D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeIllumination(int level) {
		ICharm charm =  new IlluminationCharm.Builder(level).with($ -> {
			$.value =  level < 4 ? 3D : level < 7 ? 6D : 30D;
			$.effectStackable = false;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	// curses
	public static ICharm makeDecay(int level) {
		ICharm curse =  new DecayCurse.Builder(level).with($ -> {
			$.value = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}
	
	public static ICharm makeDecrepit(int level) {
		ICharm curse =  new DecrepitCurse.Builder(level).with($ -> {
			$.value = level * 10.0 + 10.0;
			$.percent = 1D + ((level + (level % 2))/20);
//			$.duration = 20.0; set in 1.12.2 but not used.
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDirtFill(int level) {
		ICharm curse =  new DirtFillCurse.Builder(level).with($ -> {
			$.value = level * 25D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}
	
	public static ICharm makeDirtWalk(int level) {
		ICharm curse =  new DirtWalkCurse.Builder(level).with($ -> {
			$.value = level * 25D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}
	
	public static ICharm makeRuin(int level) {
		ICharm charm =  new RuinCurse.Builder(level).with($ -> {
			$.value =  level *20D;
			$.duration = level < 4 ? 20D : level < 7 ? 17D : level < 10 ? 15D : level < 13 ? 13D : 10D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
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
}
