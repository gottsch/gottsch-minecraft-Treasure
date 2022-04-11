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
package com.someguyssoftware.treasure2.loot.function;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.HealingCharm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * Not to be confused with CharmRandomly. This
 * @author Mark Gottschling on Feb 21, 2022
 *
 */
public class RandomCharm extends LootFunction {
	private static final ResourceLocation LOCATION = new ResourceLocation("treasure2:random_charm");
	private static final String LEVELS = "levels";
	private static final String RARITY = "rarity";
	private static final String RARITIES = "rarities";
	private static final String DEFAULT_CHARM = "defaultCharm";
	private static final String IS_BOOK = "isBook";

	private RandomValueRange levels;
	private Rarity rarity;
	// private List<Rarity> rarities;
	private ICharm defaultCharm;
	private boolean isBook;

	/**
	 * 
	 * @param conditions
	 * @param levels
	 * @param defaultCharm
	 */
	public RandomCharm(LootCondition[] conditions, RandomValueRange levels, Rarity rarity, ICharm defaultCharm, boolean isBook) {
		super(conditions);
		this.levels = levels;
		this.rarity = rarity;
		this.defaultCharm = defaultCharm;
		this.isBook = isBook;
		Treasure.LOGGER.debug("rarity in constructor -> {}", rarity);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		
		Treasure.LOGGER.debug("incoming stack -> {}", stack.getDisplayName());
		// default charm
		ICharm defaultCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get();
		ICharm charm = this.defaultCharm == null ? defaultCharm : this.defaultCharm;
		Optional<List<ICharm>> charms = Optional.empty();

		// select random level
		int level = this.levels == null ? 1 : this.levels.generateInt(rand);
		Treasure.LOGGER.debug("selected level -> {}", level);
		// TODO if rarities property, select a random rarity

		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity != null) {
			// get all the charms by rarity exluding curses
			charms = TreasureCharmRegistry.getBy(c -> {
				return  c.getRarity() == rarity && !c.isCurse();
			});
//			charms = TreasureCharmRegistry.get(rarity);
			if (charms.isPresent()) {
				Random random = new Random();
				charm = charms.get().get(random.nextInt(charms.get().size()));
				// override level with that of the selected rarity charm
				level = charm.getLevel();
				Treasure.LOGGER.debug("selected charm -> {}, level -> {}", charm.getName(), charm.getLevel());
			}
			else {
				charm = defaultCharm;
			}
		}
		else {
			// select a charm by level
			charms = TreasureCharmRegistry.get(level);
			if (charms.isPresent()) {
				Random random = new Random();
				charm = charms.get().get(random.nextInt(charms.get().size()));
			}
			else {
				charm = defaultCharm;
			}
		}

		Treasure.LOGGER.debug("selected charm -> {}", charm.getName());

		// select first charm item that meets the minimum level == level
		ItemStack charmStack = null;
		if (this.isBook) {
			charmStack = new ItemStack(TreasureItems.CHARM_BOOK);
		}
		else {
			Item charmItem = TreasureItems.getCharmItemByLevel(level);
			charmStack = new ItemStack(charmItem);
		}
		
		Treasure.LOGGER.debug("selected stack -> {}", charmStack.getDisplayName());
		// TODO need to order charm items by max level
//		for (Item item : TreasureItems.ITEMS.values()) {
//			if (item instanceof CharmItem) {
//				ItemStack itemStack = new ItemStack(item);
//				// get the capability
//				ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
//				if (cap != null) {
//					if (cap.getMaxCharmLevel() == level) {
//						charmStack = itemStack;
//						break;
//					}
//				}
//			}
//		}

//		if (charmStack == null) {
//			// TODO default to level 1 item ie silver_charm
//			// charmItem = TreasureItems.ITEMS.get(new ResourceLocation("treasure2:silver_charm"));
//			charmStack = stack;
//			charm = defaultCharm;
//		}

		// add charm to charmStack
		ICharmableCapability cap = charmStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		cap.clearCharms();
		cap.getCharmEntities().get(InventoryType.INNATE).add(charm.createEntity());

		return charmStack;
	}


	public static class Serializer extends LootFunction.Serializer<RandomCharm> {
		public Serializer() {
			super(LOCATION, RandomCharm.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, RandomCharm value, JsonSerializationContext context) {
			if (value.levels != null) {
				json.add(LEVELS, context.serialize(value.levels));
			}
			if (value.rarity != null) {
				json.add(RARITY, new JsonPrimitive(value.rarity.name()));
			}
			if (value.defaultCharm != null) {
				json.add(DEFAULT_CHARM, new JsonPrimitive(value.defaultCharm.getName().toString()));
			}
			json.add(IS_BOOK, new JsonPrimitive(value.isBook));
		}

		/**
		 * 
		 */
		public RandomCharm deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			
			RandomValueRange levels = null;
			if (json.has(LEVELS)) {
				 levels = JsonUtils.deserializeClass(json, LEVELS, deserializationContext, RandomValueRange.class);	
			}
			// TODO potential create new RandomValueRange(1)

			Rarity rarity = null;
			if (json.has(RARITY)) {
				String rarityString = JsonUtils.getString(json, RARITY);
				try {
					rarity = Rarity.valueOf(rarityString.toUpperCase());
				}
				catch(Exception e) {
					Treasure.LOGGER.error("Unable to convert rarity {} to Rarity", rarityString);
				}
			}

			Optional<ICharm> charm = Optional.empty();
			if (json.has(DEFAULT_CHARM)) {
				String charmName = JsonUtils.getString(json, DEFAULT_CHARM);
				charm = TreasureCharmRegistry.get(new ResourceLocation(charmName));
			}

			if (!charm.isPresent()) {
				// set a default
				charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1)));
			}
			
			boolean isBook = false;
			if (json.has(IS_BOOK)) {
				isBook = JsonUtils.getBoolean(json, IS_BOOK);
			}
			return new RandomCharm(conditionsIn, levels, rarity, charm.get(), isBook);
		}
	}
}