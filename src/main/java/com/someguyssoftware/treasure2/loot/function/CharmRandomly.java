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
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmableMaterial;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * This function will add charms to the INNATE inventory of an item with CharmableCapability by default.
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class CharmRandomly extends LootFunction {
	private static final String CHARM = "charm";
	private static final String LEVELS = "levels";
	private static final String CURSE_CHANCE = "curseChance";
	
	
	private List<ICharm> charms;
	private IRandomRange levels;
	private List<String> gems;
	private InventoryType type;
	private IRandomRange curseChance;

	/**
	 * 
	 * @param conditions
	 */
	protected CharmRandomly(ILootCondition[] conditions, @Nullable List<ICharm> charms, IRandomRange range, InventoryType type) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = range;
		this.type = type;
	}
	
	protected CharmRandomly(ILootCondition[] conditions, @Nullable List<ICharm> charms, IRandomRange levels, InventoryType type, IRandomRange curseChance) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = levels;
		this.type = type;
		this.curseChance = curseChance;
	}

	@Override
	public LootFunctionType getType() {
		return TreasureLootFunctions.CHARM_RANDOMLY;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		Random rand = context.getRandom();
		Treasure.LOGGER.debug("selected item from charm pool -> {}", stack.getDisplayName());
		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			Treasure.LOGGER.debug("has charm cap");
			ICharm charm = null;

			if (this.charms.isEmpty()) {
				List<ICharm> tempCharms = new ArrayList<>();			
				
				// check the levels property
				if(levels != null) {
					int level = this.levels.getInt(rand);
					// TODO if level > cap's max level, then use the max level
					
					double curseProb = this.curseChance != null ? this.levels.getInt(rand) : 0.0;
					Treasure.LOGGER.debug("curse chance -> {}", curseProb);
					Optional<List<ICharm>> levelCharms;
					if (curseProb > 0.0) {
						levelCharms = TreasureCharmRegistry.getBy(c -> {
							if (c.isCurse()) {
								return c.getLevel() == level && RandomHelper.checkProbability(rand, curseProb);
							}
							else {
								return c.getLevel() == level;
							}
						});
					}
					else {
						// get all the charms from level
//						levelCharms = TreasureCharmRegistry.get(level);
						levelCharms = TreasureCharmRegistry.getBy(c -> {
							return  c.getLevel() == level && !c.isCurse();
						});
					}
					
					if (levelCharms.isPresent()) {
						tempCharms.addAll(levelCharms.get());
					}
				}
				else {
					// if charms list is empty and levels are null, create a default list of minor charms
					Optional<List<ICharm>> defaultCharms = TreasureCharmRegistry.getBy(c -> {
						return c.getLevel() <= cap.getMaxCharmLevel() && !c.isCurse();
					});
					if (defaultCharms.isPresent()) {
						tempCharms.addAll(defaultCharms.get());
					}
				}
				Treasure.LOGGER.debug("temp charms size -> {}", tempCharms.size());
				if (!tempCharms.isEmpty()) {
					// TEMP dump all charms
					for (ICharm c : tempCharms) {
						Treasure.LOGGER.debug("temp charm -> {}", c.getName().toString());
					}
					// select a charm randomly
					charm = tempCharms.get(rand.nextInt(tempCharms.size()));
					Treasure.LOGGER.debug("selected charm for item -> {}", charm.getName().toString());
				}
			}
			// explicitly listed charms to use. levels, curseChance are ignored
			else {
				charm = charms.get(rand.nextInt(charms.size()));
				Treasure.LOGGER.debug("selected charm for item -> {}", charm.getName().toString());
			}

			if (charm != null) {				
				Treasure.LOGGER.debug("charm is not null -> {}", charm.getName());
				if (!cap.contains(charm)) {
					Treasure.LOGGER.debug("adding charm to charm instances - > {}", charm.getName().toString());
					cap.add(type, charm.createEntity());
					// TODO have to enable the property corresponding to the type. ex. cap.setInnate = true if type == INNATE
				}
			}
			
			// cycle thru all charms in stack inventory to determine highest level charm
			int highestLevel = cap.getHighestLevel().getCharm().getLevel();

			/*
			 *  select the correct gem/sourceItem
			 */
			CharmableMaterial baseMaterial = TreasureCharms.getBaseMaterial(cap.getBaseMaterial()).get();
			int baseMaterialLevel = baseMaterial.getMaxLevel();
			// if the highest charm level is > the base's max level, then select a gem to increase the items total max level
			if (highestLevel > baseMaterialLevel) {
				List<CharmableMaterial> gems = TreasureCharms.getGemValues();
				Collections.sort(gems, TreasureCharms.levelComparator);
				for (CharmableMaterial gem : gems) {
					if (baseMaterialLevel + Math.floor(baseMaterial.getLevelMultiplier() * gem.getMaxLevel()) >= highestLevel) {
						cap.setSourceItem(gem.getName());
						break;
					}
				}
			}
			});
		Treasure.LOGGER.debug("returning charmed item -> {}", stack.getDisplayName().getString());
		return stack;
	}

	public static CharmRandomly.Builder builder() {
		return new CharmRandomly.Builder();
	}

	public static class Builder extends LootFunction.Builder<CharmRandomly.Builder> {
		List<ICharm> charms;
		IRandomRange range;
		protected CharmRandomly.Builder getThis() {
			return this;
		}

		public CharmRandomly.Builder withCharm(ICharm charm) {
			// TODO complete this is to build from within mod (ie not loot_table)
			return this;
		}

		public CharmRandomly.Builder withLevel(int level) {
			return this;
		}

		@Override
		public ILootFunction build() {
			return new CharmRandomly(this.getConditions(), charms, range, InventoryType.INNATE);
		}
	}

	public static class Serializer extends LootFunction.Serializer<CharmRandomly> {
		
		@Override
		public void serialize(JsonObject json, CharmRandomly value, JsonSerializationContext context) {
			if (!value.charms.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (ICharm charm : value.charms) {
					jsonArray.add(new JsonPrimitive(charm.getName().toString()));
				}
				json.add("charms", jsonArray);
			}
			if (!value.gems.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (String gem : value.gems) {
					jsonArray.add(new JsonPrimitive(gem));
				}
				json.add("gems", jsonArray);
			}
			json.add("levels", RandomRanges.serialize(value.levels, context));
			json.add(CURSE_CHANCE, RandomRanges.serialize(value.curseChance, context));
		}
		
		@Override
		public CharmRandomly deserialize(JsonObject json, JsonDeserializationContext context,
				ILootCondition[] conditions) {

			Map<String, ICharm> charmsByType = new HashMap<>(10);
			List<ICharm> list = Lists.<ICharm>newArrayList();

			IRandomRange range = null;
			if (json.has("levels")) {
				range = RandomRanges.deserialize(json.get("levels"), context);	
			}
			
			IRandomRange curseChance = null;
			if (json.has(CURSE_CHANCE)) {
				range = RandomRanges.deserialize(json.get(CURSE_CHANCE), context);	
			}

			if (json.has("charms")) {
				for (JsonElement element : JSONUtils.getAsJsonArray(json, "charms")) {
					String charmName = JSONUtils.convertToString(element, "charm");

					Optional<ICharm> charm = TreasureCharmRegistry.get(ModUtils.asLocation(charmName));
					if (!charm.isPresent()) {
						Treasure.LOGGER.warn("Unknown charm '{}'", charmName);
					}

					// add to the map to prevent duplicates of same charm
					if (!charmsByType.containsKey(charm.get().getType())) {
						charmsByType.put(charm.get().getType(), charm.get());
						// add to the list of charms
						list.add(charm.get());
					}
				}
			}
			// TODO get gems

			InventoryType type = InventoryType.INNATE;
			if (json.has("type")) {
				String typeString = JSONUtils.getAsString(json, "type");
				try {
					type = InventoryType.valueOf(typeString.toUpperCase());
				}
				catch(Exception e) {}
			}
			return new CharmRandomly(conditions, list, range, type, curseChance);
		}

	}
}
