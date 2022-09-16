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
import com.someguyssoftware.treasure2.adornment.TreasureAdornmentRegistry;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

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
	private IRandomRange levels;

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
	public RandomAdornment(ILootCondition[] conditions) {
		super(conditions);
	}

	public RandomAdornment(ILootCondition[] conditions, IRandomRange levels, Optional<List<CharmableMaterial>> materials, Rarity rarity, boolean hasGem) {
		super(conditions);
		this.levels = levels;
		this.materials = materials;
		this.rarity = rarity;
		this.hasGem = hasGem;
	}

	@Override
	public LootFunctionType getType() {
		return TreasureLootFunctions.RANDOM_ADORNMENT;
	}
	
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		Random random = new Random();
		Treasure.LOGGER.debug("incoming adornment -> {}", stack.getDisplayName());
		// select material
		CharmableMaterial material = null;
		if (this.materials == null || !this.materials.isPresent()) {
			// use the base material of the default input stack
			material = TreasureCharmableMaterials
					.getBaseMaterial(stack.getCapability(TreasureCapabilities.CHARMABLE)
							.map(cap -> cap.getBaseMaterial()).orElseThrow(() -> new IllegalStateException())).get();
		}
		else {
			material = this.materials.get().get(random.nextInt(materials.get().size()));
		}
		Treasure.LOGGER.debug("selected material -> {}", material.getName());
		
		// select random level
		int level = this.levels == null ? 1 : this.levels.getInt(random);
		// update level if level exceeds max of material
		if (level > material.getMaxLevel()) {
			level = material.getMaxLevel();
		}
		
		Treasure.LOGGER.debug("rarity -> {}, code -> {}", rarity, rarity.getCode());
		Treasure.LOGGER.debug("material -> {}, maxLevel -> {}", material.getName(), material.getMaxLevel());
		Treasure.LOGGER.debug("material rarity -> {}", TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel()));
		
		// TODO this check isn't calculating the max charm level of an adornment with material ie not checking GREAT + GEM
//		if (rarity != null && rarity.getCode() > TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel()).getCode()) {
//			rarity = TreasureCharms.LEVEL_RARITY.get(material.getMaxLevel());
//		}
		Treasure.LOGGER.debug("updated rarity -> {}, code -> {}", rarity, rarity.getCode());
		
		Treasure.LOGGER.debug("hasGem -> {}", hasGem);
		// TODO add "hasGem" property to indicate gem/no-gem adornments
		/*
		 *  TODO adornments skip levels in their definition, so it is possible that lambdaLevel != a.level and not return any adornments.
		 *  ALL filters should be based on rarity. ie if given a level/levels, the choosen level should be matched against the rarity of maxCharmLevel
		 *  and not level == maxCharmLevel
		 */

		final int lambdaLevel = level;
		List<Adornment> adornmentsByMaterial = TreasureAdornmentRegistry.getByMaterial(material);
		List<Adornment> 	adornments = adornmentsByMaterial.stream()
			// filter for source items (gem/stone)
			.filter(a -> {
				ItemStack itemStack = new ItemStack(a);
				boolean hasSource = itemStack.getCapability(TreasureCapabilities.CHARMABLE)
						.map(cap -> cap.getSourceItem()).orElse(Items.AIR.getRegistryName()) != Items.AIR.getRegistryName();
				if (!hasGem && !hasSource) {
					Treasure.LOGGER.debug("filter: keeping adornment -> {}", itemStack.getDisplayName());
					return true;
				}
				else if (hasGem && hasSource) {
					return true;
				}
				return false;
			})
			.filter(a -> {
				ItemStack itemStack = new ItemStack(a);
				if (rarity != null && rarity == TreasureCharms.LEVEL_RARITY.get(itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap.getMaxCharmLevel()).orElse(0))) {
					return true;
				}
				else if (itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap.getMaxCharmLevel()).orElse(0) == lambdaLevel) {
					return true;
				}
				return false;
			})
			.collect(Collectors.toList());

		// create a new adornment item
		ItemStack adornment;
		if (adornments == null || adornments.isEmpty()) {
			Treasure.LOGGER.debug("no adornments match criteria, using incoming adornment -> {}", stack.getDisplayName());
			adornment = stack;
		}
		else {
			adornment = new ItemStack(adornments.get(random.nextInt(adornments.size())));
		}

		Treasure.LOGGER.debug("selected adornment -> {}", adornment.getDisplayName());
		return adornment;
	}
	
	/*
	 * 
	 */
	public static RandomAdornment.Builder builder() {
		return new RandomAdornment.Builder();
	}
	
	public static class Builder extends LootFunction.Builder<RandomAdornment.Builder> {
		List<ICharm> charms;
		IRandomRange range;
		protected RandomAdornment.Builder getThis() {
			return this;
		}

		@Override
		public ILootFunction build() {
			return new RandomAdornment(this.getConditions());
		}
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Feb 27, 2022
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<RandomAdornment> {

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
				ILootCondition[] conditionsIn) {
			
			IRandomRange levels = null;
			if (json.has(LEVELS)) {
				levels = RandomRanges.deserialize(json.get(LEVELS), deserializationContext);
			}
			// TODO potential create new RandomValueRange(1)

			Optional<List<CharmableMaterial>> materials = Optional.empty();
			if (json.has(MATERIALS)) {
				// String materialName = JsonUtils.getString(json, MATERIAL);
				// material = TreasureCharmableMaterials.getBaseMaterial(new ResourceLocation(materialName));
				for (JsonElement element : JSONUtils.getAsJsonArray(json, MATERIALS)) {
					String materialName = JSONUtils.convertToString(element, "material");
					Optional<CharmableMaterial> material = TreasureCharmableMaterials.getBaseMaterial(ModUtils.asLocation(materialName));
					if (material.isPresent()) {
						if (!materials.isPresent()) {
							materials = Optional.of(new ArrayList<CharmableMaterial>());
						}
						materials.get().add(material.get());
					}
					else {
						Treasure.LOGGER.warn("Unknown material '{}'", materialName);
					}
				}
			}

			Rarity rarity = null;
			if (json.has(RARITY)) {
				String rarityString = JSONUtils.getAsString(json, RARITY);
				try {
					rarity = Rarity.valueOf(rarityString.toUpperCase());
				}
				catch(Exception e) {
					Treasure.LOGGER.error("Unable to convert rarity {} to Rarity", rarityString);
				}
			}
			
			boolean hasGem = false;
			if (json.has(HAS_GEM)) {
				hasGem = JSONUtils.getAsBoolean(json, HAS_GEM);
			}
			
			// NOTE no default value for material as it is an optional value

			return new RandomAdornment(conditionsIn, levels, materials, rarity, hasGem);
		}
	}
}