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
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Aug 31, 2021
 *
 */
public class RandomAdornment extends LootFunction {

	// the type of adornment - ring, necklace, bracelet, earrings, pocket watch
	private String adornmentType;
	// the base material of the adornment to be selected
	private String material;

	/**
	 * 
	 * @param conditions
	 */
	protected RandomAdornment(ILootCondition[] conditions) {
		super(conditions);
	}

	@Override
	public LootFunctionType getType() {
		return TreasureLootFunctions.RANDOM_ADORNMENT;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		Random random = new Random();

		List<Item> adornments;
		if (adornmentType != null) {
			adornments = TreasureAdornments.getByType(adornmentType);
			if (material != null) {
				// filter
				adornments = adornments.stream()
						.filter(a -> {
							Set<String> tags = a.getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
							if (tags.contains(material)) {
								return true;
							}
							return false;
						}).collect(Collectors.toList());
			}
		}
		else if (material != null) {
			adornments = TreasureAdornments.getByMaterial(material);
			if (adornmentType != null) {
				// filter
				adornments = adornments.stream()
						.filter(a -> {
							Set<String> tags = a.getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
							if (tags.contains(adornmentType)) {
								return true;
							}
							return false;
						}).collect(Collectors.toList());
			}
		}
		else {
			adornments = TreasureAdornments.getAll();
		}

		// create a new adornment item
		ItemStack adornment;
		if (adornments == null || adornments.isEmpty()) {
			adornment = stack;
		}
		else {
			adornment = new ItemStack(adornments.get(random.nextInt(adornments.size())));
		}

		return adornment;
	}

	public static RandomAdornment.Builder builder() {
		return new RandomAdornment.Builder();
	}

	/**
	 * 
	 *
	 */
	public static class Builder extends LootFunction.Builder<RandomAdornment.Builder> {
		private Optional<String> type = Optional.empty();

		protected RandomAdornment.Builder getThis() {
			return this;
		}

		public RandomAdornment.Builder withType(String type) {
			this.type = Optional.of(type);
			return this;
		}

		@Override
		public ILootFunction build() {
			RandomAdornment ra = new RandomAdornment(this.getConditions());
			if (type.isPresent()) {
				ra.adornmentType = type.get();
			}
			return ra;
		}
	}

	public static class Serializer extends LootFunction.Serializer<RandomAdornment> {

		@Override
		public void serialize(JsonObject json, RandomAdornment value, JsonSerializationContext context) {
			json.add("type", new JsonPrimitive(value.adornmentType));
			json.add("material", new JsonPrimitive(value.material.toString()));
		}

		@Override
		public RandomAdornment deserialize(JsonObject json, JsonDeserializationContext context,
				ILootCondition[] conditions) {

			Optional<String> type;
			if (json.has("type")) {
				type = Optional.of(JSONUtils.getAsString(json, "type").toLowerCase());
			}
			else {
				type =  Optional.empty();
			}

			Optional<String> material;
			if (json.has("material")) {
				material =  Optional.of(JSONUtils.getAsString(json, "material").toLowerCase());
			}
			else {
				material = Optional.empty();
			}

			RandomAdornment ra = new RandomAdornment(conditions);
			if (type.isPresent()) {
				ra.adornmentType = type.get();
			}
			if (material.isPresent()) {
				ra.material = material.get();
			}			
			return ra;
		}
	}
}
