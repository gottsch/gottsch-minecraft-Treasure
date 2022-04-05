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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.HealingCharm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * Levels and Rarity/Rarites are mutally exclusive, with Rarity/Rarities having override priority.
 * @author Mark Gottschling on May 1, 2020
 * @param <ICharmType>
 *
 */
public class CharmRandomly extends LootFunction {
	private static final String CHARM = "charm";
	private static final String LEVELS = "levels";
	private static final String RARITY = "rarity";
	private static final String RARITIES = "rarities";
	private static final String CURSE_CHANCE = "curseChance";

	private List<ICharm> charms;
	private RandomValueRange levels;
	private Rarity rarity;
	private InventoryType type;
	private RandomValueRange curseChance;

	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms, RandomValueRange levels, InventoryType type) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = levels;
		this.type = type;
	}

	/**
	 * 
	 * @param conditions
	 * @param charms
	 * @param range
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms, RandomValueRange levels, Rarity rarity, InventoryType type, RandomValueRange curseChance) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = levels;
		this.rarity = rarity;
		this.type = type;
		this.curseChance = curseChance;
	}


	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Treasure.logger.debug("incoming stack -> {}", stack.getDisplayName());
		// ensure that the stack has charm capabilities
		if (!stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			return stack;
		}
		Treasure.logger.debug("has charm cap");

		// default charm
		ICharm defaultCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get();
		ICharm charm = defaultCharm;

		ICharmableCapability charmCap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);
		List<ICharm> tempCharms = new ArrayList<>();
		int defaultLevel = charmCap.getMaxCharmLevel();

		Optional<List<ICharm>> charmList = Optional.empty();
		// select random level
		int level = this.levels == null ? defaultLevel : this.levels.generateInt(rand);

		// explicity charms list is empty
		if (this.charms.isEmpty()) {
			// check the rarity property
			if (rarity != null) {
				// ensure that rarity isn't higher than rarity of adornment
				if (TreasureCharms.LEVEL_RARITY.get(charmCap.getMaxCharmLevel()).getCode() > rarity.getCode()) {
					rarity = TreasureCharms.LEVEL_RARITY.get(charmCap.getMaxCharmLevel());
				}				
				charmList = TreasureCharmRegistry.get(rarity);
				if (charmList.isPresent()) {
					tempCharms.addAll(charmList.get());
				}
				else {
					charm = defaultCharm;
				}
			}
			// check the levels property
			else if(levels != null) {
				int lambdaLevel = level;
				// TODO if level > cap's max level, then use the max level
				double curseProb = this.curseChance != null ? this.levels.generateInt(rand) : 0D;
				Treasure.logger.debug("curse chance -> {}", curseProb);

				// get all the charms from level
				Optional<List<ICharm>> levelCharms;
				if (curseProb > 0.0) {
					levelCharms = TreasureCharmRegistry.getBy(c -> {
						if (c.isCurse()) {
							return c.getLevel() == lambdaLevel && RandomHelper.checkProbability(rand, curseProb);
						}
						else {
							return c.getLevel() == lambdaLevel;
						}
					});
				}
				else {
					// get all the charms from level
					levelCharms = TreasureCharmRegistry.getBy(c -> {
						return  c.getLevel() == lambdaLevel && !c.isCurse();
					});
				}

				if (levelCharms.isPresent()) {
					tempCharms.addAll(levelCharms.get());
				}
			}
			else {
				// if charms list is empty and levels are null, create a default list of minor charms
				Optional<List<ICharm>> defaultCharms = TreasureCharmRegistry.getBy(c -> {
					return c.getLevel() <= charmCap.getMaxCharmLevel() && !c.isCurse();
				});
				if (defaultCharms.isPresent()) {
					tempCharms.addAll(defaultCharms.get());
				}
			}
			Treasure.logger.debug("temp charms size -> {}", tempCharms.size());
			if (!tempCharms.isEmpty()) {
				// select a charm randomly
				charm = tempCharms.get(rand.nextInt(tempCharms.size()));
				Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
			}
		}
		// explicitly listed charms to use. levels, curseChance are ignored
		else {
			charm = charms.get(rand.nextInt(charms.size()));
			Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
		}

		// short-circuit if a charm is not selected
		if (charm == null) {
			return stack;
		}

		Treasure.logger.debug("charm is not null -> {}", charm.getName());
		if (!charmCap.contains(charm)) {
			Treasure.logger.debug("adding charm to charm instances - > {}", charm.getName().toString());
			charmCap.add(type, charm.createEntity());
		}

		Treasure.logger.debug("returning charmed item -> {}", stack.getDisplayName());
		return stack;
	}

	/**
	 * 
	 * @author Mark Gottschling on May 1, 2020
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<CharmRandomly> {
		public Serializer() {
			super(new ResourceLocation("treasure2:charm_randomly"), CharmRandomly.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, CharmRandomly value, JsonSerializationContext context) {
			if (!value.charms.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (ICharm charm : value.charms) {
					jsonArray.add(new JsonPrimitive(charm.getName().toString()));
				}
				json.add("charms", jsonArray);
			}

			if (value.levels != null) {
				json.add(LEVELS, context.serialize(value.levels));
			}
			if (value.rarity != null) {
				json.add(RARITY, new JsonPrimitive(value.rarity.name())); // TODO toString() ?
			}
			if (value.curseChance != null) {
				json.add(CURSE_CHANCE, context.serialize(value.curseChance));
			}
		}

		/**
		 * 
		 */
		public CharmRandomly deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			Map<String, ICharm> charmsByType = new HashMap<>(10);
			List<ICharm> list = Lists.<ICharm>newArrayList();

			RandomValueRange levels = null;
			if (json.has(LEVELS)) {
				levels = JsonUtils.deserializeClass(json, LEVELS, deserializationContext, RandomValueRange.class);	
			}

			Rarity rarity = null;
			if (json.has(RARITY)) {
				String rarityString = JsonUtils.getString(json, RARITY);
				try {
					rarity = Rarity.valueOf(rarityString.toUpperCase());
				}
				catch(Exception e) {
					Treasure.logger.error("Unable to convert rarity {} to Rarity", rarityString);
				}
			}

			RandomValueRange curseChance = null;
			if (json.has(CURSE_CHANCE)) {
				curseChance = JsonUtils.deserializeClass(json, CURSE_CHANCE, deserializationContext, RandomValueRange.class);	
			}

			if (json.has("charms")) {
				for (JsonElement element : JsonUtils.getJsonArray(json, "charms")) {
					String charmName = JsonUtils.getString(element, "charm");
					Optional<ICharm> charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));

					if (!charm.isPresent()) {
						Treasure.logger.warn("Unknown charm '{}'", charmName);
					}

					// add to the map to prevent duplicates of same charm
					if (!charmsByType.containsKey(charm.get().getType())) {
						charmsByType.put(charm.get().getType(), charm.get());
						// add to the list of charms
						list.add(charm.get());
					}
				}
			}

			InventoryType type = InventoryType.INNATE;
			if (json.has("type")) {
				String typeString = JsonUtils.getString(json, "type");
				try {
					type = InventoryType.valueOf(typeString.toUpperCase());
				}
				catch(Exception e) {
					Treasure.logger.error("Unable to convert type {} to InventoryType", typeString);
				}
			}

			return new CharmRandomly(conditionsIn, list, levels, rarity, type, curseChance);
		}
	}
}
