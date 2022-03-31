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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.Adornment;
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
 * 
 * @author Mark Gottschling on Mar 27, 2022
 *
 */
public class RandomAdornment extends LootFunction {
	private static final ResourceLocation LOCATION = new ResourceLocation("treasure2:random_adornment");
	private static final String LEVELS = "levels";
	private static final String MATERIALS = "materials";
	private static final String RARITY = "rarity";
	private static final String HAS_GEM = "hasGem";

	// the type of adornment - ring, necklace, bracelet, earrings, pocket watch
	//private String adornmentType;

	/*
	 * mutually exclusive from rarity
	 */
	private RandomValueRange levels;

	// the base material of the adornment to be selected
	//private String material;
	/*
	 * mutually exclusive from rarity
	 */
	private Optional<List<CharmableMaterial>> materials;

	/*
	 * mutually exclusive from levels and materials
	 */
	private Rarity rarity;
	
	private boolean hasGem;
	
	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public RandomAdornment(LootCondition[] conditions) {
		super(conditions);
	}

	public RandomAdornment(LootCondition[] conditions, RandomValueRange levels, Optional<List<CharmableMaterial>> materials, Rarity rarity, boolean hasGem) {
		super(conditions);
		this.levels = levels;
		this.materials = materials;
		this.rarity = rarity;
		this.hasGem = hasGem;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Random random = new Random();
		Treasure.logger.debug("incoming adornment -> {}", stack.getDisplayName());
		// select material
		CharmableMaterial material = null;
		if (this.materials == null || !this.materials.isPresent()) {
			// use the base material of the default input stack
			material = TreasureCharmableMaterials.getBaseMaterial(stack.getCapability(TreasureCapabilities.CHARMABLE, null).getBaseMaterial()).get();
		}
		else {
			material = this.materials.get().get(random.nextInt(materials.get().size()));
		}
		Treasure.logger.debug("selected material -> {}", material.getName());
		
		// select random level
		int level = this.levels == null ? 1 : this.levels.generateInt(rand);
		// update level if level exceeds max of material
		if (level > material.getMaxLevel()) {
			level = material.getMaxLevel();
		}
		
		Treasure.logger.debug("rarity -> {}, code -> {}", rarity, rarity.getCode());
		Treasure.logger.debug("material -> {}, maxLevel -> {}", material.getName(), material.getMaxLevel());
		Treasure.logger.debug("material rarity -> {}", TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel()));
		
		// TODO this check isn't calculating the max charm level of an adornment with material ie not checking GREAT + GEM
//		if (rarity != null && rarity.getCode() > TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel()).getCode()) {
//			rarity = TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel());
//		}
		Treasure.logger.debug("updated rarity -> {}, code -> {}", rarity, rarity.getCode());
		
		Treasure.logger.debug("hasGem -> {}", hasGem);
		// TODO add "hasGem" property to indicate gem/no-gem adornments
		/*
		 *  TODO adornments skip levels in their definition, so it is possible that lambdaLevel != a.level and not return any adornments.
		 *  ALL filters should be based on rarity. ie if given a level/levels, the choosen level should be matched against the rarity of maxCharmLevel
		 *  and not level == maxCharmLevel
		 */

		final int lambdaLevel = level;
		List<Adornment> adornmentsByMaterial = TreasureAdornments.getByMaterial(material);
		List<Adornment> 	adornments = adornmentsByMaterial.stream()
			// filter for source items (gem/stone)
			.filter(a -> {
				ItemStack itemStack = new ItemStack(a);
				boolean hasSource = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null).getSourceItem() != Items.AIR.getRegistryName();
				if (!hasGem && !hasSource) {
					Treasure.logger.debug("filter: keeping adornment -> {}", itemStack.getDisplayName());
					return true;
				}
				else if (hasGem && hasSource) {
					return true;
				}
				return false;
			})
			.filter(a -> {
				ItemStack itemStack = new ItemStack(a);
				if (rarity != null && rarity == TreasureCharms.LEVEL_RARITY.get(itemStack.getCapability(TreasureCapabilities.CHARMABLE, null).getMaxCharmLevel())) {
					return true;
				}
				else if (itemStack.getCapability(TreasureCapabilities.CHARMABLE, null).getMaxCharmLevel() == lambdaLevel) {
					return true;
				}
				return false;
			})
			.collect(Collectors.toList());

		// create a new adornment item
		ItemStack adornment;
		if (adornments == null || adornments.isEmpty()) {
			Treasure.logger.debug("no adornments match criteria, using incoming adornment -> {}", stack.getDisplayName());
			adornment = stack;
		}
		else {
			adornment = new ItemStack(adornments.get(random.nextInt(adornments.size())));
		}

		Treasure.logger.debug("selected adornment -> {}", adornment.getDisplayName());
		return adornment;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Feb 27, 2022
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<RandomAdornment> {
		public Serializer() {
			super(LOCATION, RandomAdornment.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, RandomAdornment value, JsonSerializationContext context) {
			json.add(LEVELS, context.serialize(value.levels));

			// serialize the materials
			if (value.materials.isPresent()) {
				final JsonArray jsonArray = new JsonArray();
				value.materials.get().forEach(material -> {
					jsonArray.add(new JsonPrimitive(material.getName().toString()));
				});
				json.add(MATERIALS, jsonArray);
				// json.add(MATERIAL, new JsonPrimitive(value.material.getName().toString()));
				if (value.rarity != null) {
					json.add(RARITY, new JsonPrimitive(value.rarity.name()));
				}
				json.add(HAS_GEM, new JsonPrimitive(value.hasGem));
			}
		}

		/**
		 * 
		 */
		public RandomAdornment deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			
			RandomValueRange levels = null;
			if (json.has(LEVELS)) {
				 levels = JsonUtils.deserializeClass(json, LEVELS, deserializationContext, RandomValueRange.class);	
			}
			// TODO potential create new RandomValueRange(1)

			Optional<List<CharmableMaterial>> materials = Optional.empty();
			if (json.has(MATERIALS)) {
				// String materialName = JsonUtils.getString(json, MATERIAL);
				// material = TreasureCharmableMaterials.getBaseMaterial(new ResourceLocation(materialName));
				for (JsonElement element : JsonUtils.getJsonArray(json, MATERIALS)) {
					String materialName = JsonUtils.getString(element, "material");
					Optional<CharmableMaterial> material = TreasureCharmableMaterials.getBaseMaterial(ResourceLocationUtil.create(materialName));
					if (material.isPresent()) {
						if (!materials.isPresent()) {
							materials = Optional.of(new ArrayList<CharmableMaterial>());
						}
						materials.get().add(material.get());
					}
					else {
						Treasure.logger.warn("Unknown material '{}'", materialName);
					}
				}
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
			
			boolean hasGem = false;
			if (json.has(HAS_GEM)) {
				hasGem = JsonUtils.getBoolean(json, HAS_GEM);
			}
			
			// NOTE no default value for material as it is an optional value

			return new RandomAdornment(conditionsIn, levels, materials, rarity, hasGem);
		}
	}
}