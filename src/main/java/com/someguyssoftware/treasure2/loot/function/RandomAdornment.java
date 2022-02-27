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

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * 
 * @author Mark Gottschling on Aug 31, 2021
 *
 */
public class RandomAdornment extends LootFunction {
	private static final String LOCATION = new ResourceLocation("treasure2:random_adornment");
	private static final String LEVELS = "levels";
	private static final String MATERIALS = "materials";

	// the type of adornment - ring, necklace, bracelet, earrings, pocket watch
	//private String adornmentType;

	private RandomValueRange levels;

	// the base material of the adornment to be selected
	//private String material;
	private Optional<List<CharmableMaterial>> materials;


	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public RandomAdornment(LootCondition[] conditions) {
		super(conditions);
	}

	public RandomAdornment(LootCondition[] conditions, RandomValueRange levels, Optional<List<CharmableMaterial>> materials) {
		super(conditions);
		this.levels = levels;
		this.materials = materials;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Random random = new Random();

		// select random level
		int level = this.levels == null ? 1 : this.levels.generateInt(rand);

		// select material
		CharmableMaterial material = null;
		if (this.materials == null || !this.materials.isPresent()) {
			material = TreasureCharmableMaterials.getBaseMaterial(stack.getCapability(TreasureCapabilities.CHARMABLE, null).getMaterial());
		}
		else {
			material = this.materials.get().get(random.nextInt(materials.get().size()));
		}

		// update level if level exceeds max of material
		if (level > material.getMaxLevel()) {
			level = material.getMaxLevel();
		}

		// TODO select all adornments that meet the level and material criteria - this includes all material + gem combos.
		// ex at this point: level = 3, material = silver, so all silver rings, necklaces, bracelets where level <= 3 (or should be == 3 ?)
		List<Adornment> adornmentsByMaterial = TreasureAdornments.getByMaterial(material.get());
		List<Adornment> adornments = 
			adornments = adornmentsByMaterial.stream()
			.filter(a -> {
				ItemStack itemStack = new ItemStack(a);
				if (a.getCapability(TreasureCapabilities.CHARMABLE, null).getMaxCharmLevel() == level) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());

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
			}
		}

		/**
		 * 
		 */
		public CharmRandomly deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
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
					Optional<CharmableMaterial> material = TreasureCharmableMaterials.getBaseMaterial(new ResourceLocation(materialName));
					if (material.isPresent()) {
						if (!materials.isPresent()) {
							materials = Optional.of(new ArrayList<CharmableMaterial>());
						}
						materials.get().add(material);
					}
					else {
						Treasure.logger.warn("Unknown material '{}'", materialName);
					}
				}
			}

			// NOTE no default value for material as it is an optional value

			return new RandomCharm(conditionsIn, levels, materials);
		}
	}

//	/**
//	 * 
//	 * @param conditions
//	 */
//	protected RandomAdornment(ILootCondition[] conditions) {
//		super(conditions);
//	}
//
//	@Override
//	public LootFunctionType getType() {
//		return TreasureLootFunctions.RANDOM_ADORNMENT;
//	}
//
//	@Override
//	protected ItemStack run(ItemStack stack, LootContext context) {
//		Random random = new Random();
//
//		List<Item> adornments;
//		if (adornmentType != null) {
//			adornments = TreasureAdornments.getByType(adornmentType);
//			if (material != null) {
//				// filter
//				adornments = adornments.stream()
//						.filter(a -> {
//							Set<String> tags = a.getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
//							if (tags.contains(material)) {
//								return true;
//							}
//							return false;
//						}).collect(Collectors.toList());
//			}
//		}
//		else if (material != null) {
//			adornments = TreasureAdornments.getByMaterial(material);
//			if (adornmentType != null) {
//				// filter
//				adornments = adornments.stream()
//						.filter(a -> {
//							Set<String> tags = a.getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
//							if (tags.contains(adornmentType)) {
//								return true;
//							}
//							return false;
//						}).collect(Collectors.toList());
//			}
//		}
//		else {
//			adornments = TreasureAdornments.getAll();
//		}
//
//		// create a new adornment item
//		ItemStack adornment;
//		if (adornments == null || adornments.isEmpty()) {
//			adornment = stack;
//		}
//		else {
//			adornment = new ItemStack(adornments.get(random.nextInt(adornments.size())));
//		}
//
//		return adornment;
//	}
//
//	public static RandomAdornment.Builder builder() {
//		return new RandomAdornment.Builder();
//	}
//
//	/**
//	 * 
//	 *
//	 */
//	public static class Builder extends LootFunction.Builder<RandomAdornment.Builder> {
//		private Optional<String> type = Optional.empty();
//
//		protected RandomAdornment.Builder getThis() {
//			return this;
//		}
//
//		public RandomAdornment.Builder withType(String type) {
//			this.type = Optional.of(type);
//			return this;
//		}
//
//		@Override
//		public ILootFunction build() {
//			RandomAdornment ra = new RandomAdornment(this.getConditions());
//			if (type.isPresent()) {
//				ra.adornmentType = type.get();
//			}
//			return ra;
//		}
//	}
//
//	public static class Serializer extends LootFunction.Serializer<RandomAdornment> {
//
//		@Override
//		public void serialize(JsonObject json, RandomAdornment value, JsonSerializationContext context) {
//			json.add("type", new JsonPrimitive(value.adornmentType));
//			json.add("material", new JsonPrimitive(value.material.toString()));
//		}
//
//		@Override
//		public RandomAdornment deserialize(JsonObject json, JsonDeserializationContext context,
//				ILootCondition[] conditions) {
//
//			Optional<String> type;
//			if (json.has("type")) {
//				type = Optional.of(JSONUtils.getAsString(json, "type").toLowerCase());
//			}
//			else {
//				type =  Optional.empty();
//			}
//
//			Optional<String> material;
//			if (json.has("material")) {
//				material =  Optional.of(JSONUtils.getAsString(json, "material").toLowerCase());
//			}
//			else {
//				material = Optional.empty();
//			}
//
//			RandomAdornment ra = new RandomAdornment(conditions);
//			if (type.isPresent()) {
//				ra.adornmentType = type.get();
//			}
//			if (material.isPresent()) {
//				ra.material = material.get();
//			}			
//			return ra;
//		}
//	}
}