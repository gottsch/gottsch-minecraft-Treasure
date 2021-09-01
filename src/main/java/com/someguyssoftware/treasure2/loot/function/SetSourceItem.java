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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;
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
public class SetSourceItem extends LootFunction {
	private ResourceLocation sourceItem;
	private List<ResourceLocation> sourceItems;
	private IRandomRange levels;
	private Boolean strict;

	/**
	 * 
	 * @param conditions
	 */
	protected SetSourceItem(ILootCondition[] conditions, ResourceLocation sourceItem) {
		super(conditions);
		this.sourceItem = sourceItem;
	}
	
	protected SetSourceItem(ILootCondition[] conditions, IRandomRange levels, Boolean strict) {
		super(conditions);
		this.levels = levels;
		this.strict = strict;
	}


	@Override
	public LootFunctionType getType() {
		return TreasureLootFunctions.SET_SOURCE_ITEM;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		Treasure.LOGGER.debug("selected item from charm pool -> {}", stack.getDisplayName());
		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			Treasure.LOGGER.debug("has charm cap");
			cap.setSourceItem(this.sourceItem);
		});
		return stack;
	}

	public static SetSourceItem.Builder builder() {
		return new SetSourceItem.Builder();
	}

	/**
	 * 
	 *
	 */
	public static class Builder extends LootFunction.Builder<SetSourceItem.Builder> {
		private ResourceLocation sourceItem;

		protected SetSourceItem.Builder getThis() {
			return this;
		}

		public SetSourceItem.Builder withSourceItem(ResourceLocation sourceItem) {
			this.sourceItem = sourceItem;
			return this;
		}

		@Override
		public ILootFunction build() {
			return new SetSourceItem(this.getConditions(), sourceItem);
		}
	}

	public static class Serializer extends LootFunction.Serializer<SetSourceItem> {

		@Override
		public void serialize(JsonObject json, SetSourceItem value, JsonSerializationContext context) {
			json.add("sourceItem", new JsonPrimitive(value.sourceItem.toString()));
		}

		@Override
		public SetSourceItem deserialize(JsonObject json, JsonDeserializationContext context,
				ILootCondition[] conditions) {

			ResourceLocation sourceItem = null;
			if (json.has("sourceItem")) {
				String typeString = JSONUtils.getAsString(json, "sourceItem");
				try {
					sourceItem = ModUtils.asLocation(typeString);
				}
				catch(Exception e) {}
			}
			return new SetSourceItem(conditions, sourceItem);
		}

	}
}
