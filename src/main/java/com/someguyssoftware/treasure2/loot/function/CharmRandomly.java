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
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * @author Mark Gottschling on May 1, 2020
 * @param <ICharmType>
 *
 */
public class CharmRandomly extends LootFunction {
	private static final String CHARM = "charm";
	private static final String LEVELS = "levels";
	private static final String CURSE_CHANCE = "curseChance";
	
	private List<ICharm> charms;
	private RandomValueRange levels;
	private List<String> gems;
	private InventoryType type;
	private RandomValueRange curseChance;
	
	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms, RandomValueRange range, InventoryType type) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = range;
		this.type = type;
	}
	
	/**
	 * 
	 * @param conditions
	 * @param charms
	 * @param range
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms, RandomValueRange levels, InventoryType type, RandomValueRange curseChance) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = levels;
		this.type = type;
		this.curseChance = curseChance;
	}
	

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		ICharm charm = null;
		Treasure.logger.debug("selected item from charm pool -> {}", stack.getDisplayName());
		// ensure that the stack has charm capabilities
		ICharmableCapability charmCap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);

		if (charmCap != null) {
			Treasure.logger.debug("has charm cap");
			List<ICharmEntity> charmEntities = (List<ICharmEntity>) charmCap.getCharmEntities().values();
			List<ICharm> tempCharms = new ArrayList<>();
			
			if (this.charms.isEmpty()) {	
				// check the levels property
				if(levels != null) {
					int level = this.levels.generateInt(rand);
					// TODO if level > cap's max level, then use the max level
					double curseProb = this.curseChance != null ? this.levels.generateInt(rand) : 0D;
					Treasure.logger.debug("curse chance -> {}", curseProb);
					
					// get all the charms from level
					Optional<List<ICharm>> levelCharms;// = TreasureCharmRegistry.get(level);
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
						levelCharms = TreasureCharmRegistry.getBy(c -> {
							return  c.getLevel() == level && !c.isCurse();
						});
					}
					
					if (levelCharms.isPresent()) {
						tempCharms.addAll(levelCharms.get());
					}
				}
				else {
					// TODO determine if there is a source item, get the min spawn level and add that to the Predicate
					// if charms list is empty and levels are null, create a default list of minor charms
					Optional<List<ICharm>> defaultCharms = TreasureCharmRegistry.getBy(c -> {
						return c.getLevel() <= charmCap.getMaxCharmLevel() && !c.isCurse();
					});
					if (defaultCharms.isPresent()) {
						tempCharms.addAll(defaultCharms.get());
					}
				}
//				else {
//					// if charms list is empty, create a default list of minor charms
//					for (ICharm c : TreasureCharmRegistry.values()) {
//						if (c.getLevel() == 1 || c.getLevel() == 2) {
//							tempCharms.add(c);
//						}
//					}
//				}
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
//			else {
//				// check the levels property
//				if(levels != null) {
//					int level = this.levels.generateInt(rand);
//					// get all the charms from level
//					Optional<List<ICharm>> levelCharms = TreasureCharmRegistry.get(level);
//					if (levelCharms.isPresent()) {
//						tempCharms.addAll(levelCharms.get());
//					}
//				}
//				// add the listed charms to the temp list
//				tempCharms.addAll(charms);
//				
//				// select a charm randomly
//				charm =tempCharms.get(rand.nextInt(tempCharms.size()));
//				Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
//			}
//			if (charm != null) {
				Treasure.logger.debug("charm is not null -> {}", charm.getName());
				if (!charmCap.contains(charm)) {
					Treasure.logger.debug("adding charm to charm instances - > {}", charm.getName().toString());
					charmCap.add(type, charm.createEntity());
				}
				// cycle thru all charms in cap inventory to determine highest level charm
				int highestLevel = charmCap.getHighestLevel().getCharm().getLevel();
				
				/*
				 *  select the correct gem/sourceItem
				 */
				if (charmCap.getSourceItem() == null || charmCap.getSourceItem().equals(Items.AIR.getRegistryName())) {
					Optional<CharmableMaterial> baseMaterial = TreasureCharmableMaterials.getBaseMaterial(charmCap.getBaseMaterial());
					if (baseMaterial.isPresent()) { // <-- TOOD need a better check for items that take source items. ie charm books don't take different types of source items
						int baseMaterialLevel = baseMaterial.get().getMaxLevel();
						// if the highest charm level is > the base's max level, then select a gem to increase the items total max level
						if (highestLevel > charmCap.getLevelModifier().modifyMaxLevel(baseMaterialLevel)) {
							List<CharmableMaterial> gems = TreasureCharmableMaterials.getGemValues();
							Collections.sort(gems, TreasureCharmableMaterials.levelComparator);
							for (CharmableMaterial gem : gems) {
								if (charmCap.getLevelModifier().modifyMaxLevel(baseMaterialLevel)
										+ Math.floor(charmCap.getLevelModifier().modifyLevelMultiplier(baseMaterial.get().getLevelMultiplier()) * gem.getMaxLevel()) >= highestLevel) {
									/*
									 * 		return levelModifier.modifyMaxLevel(effectiveBase.getMaxLevel())
				+ (int) Math.floor(levelModifier.modifyLevelMultiplier(effectiveBase.getLevelMultiplier()) * (source.isPresent() ? source.get().getMaxLevel() : 0));
									 */
									charmCap.setSourceItem(gem.getName());
									break;
								}
							}
						}
						// TODO only build a custom name if innate inventory has charms
						// set the hover name
						if (charmCap.isNamedByMaterial() || charmCap.isNamedByCharm()) {
							TreasureCharmables.setHoverName(stack);
						}
					}		
				}
				// ensure that the item doesn't already have the same charm or same type or exceeded the maximum charms.
//				boolean hasCharm = false;
//				for (ICharmEntity entity : charmEntities) {
//					if (entity.getCharm().getType().equalsIgnoreCase(charm.getType()) ||
//							entity.getCharm().getName().equals(charm.getName())) {
//								hasCharm = true;
//								break;
//							}
				
//				}
//				if (!hasCharm) {
//					Treasure.logger.debug("adding charm to charm instances.");
//					charmEntities.add(charm.createEntity());
//				}
//			}
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
			if (!value.gems.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (String gem : value.gems) {
					jsonArray.add(new JsonPrimitive(gem));
				}
				json.add("gems", jsonArray);
				json.add(LEVELS, context.serialize(value.levels));
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
			
			RandomValueRange range = null;
			if (json.has(LEVELS)) {
				 range = JsonUtils.deserializeClass(json, LEVELS, deserializationContext, RandomValueRange.class);	
			}
			
			RandomValueRange curseChance = null;
			if (json.has(CURSE_CHANCE)) {
				range = JsonUtils.deserializeClass(json, CURSE_CHANCE, deserializationContext, RandomValueRange.class);	
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
			
			// TODO get gems

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
						
			return new CharmRandomly(conditionsIn, list, range, type, curseChance);
		}
	}
}
