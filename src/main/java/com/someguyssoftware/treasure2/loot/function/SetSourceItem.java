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
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.TreasureAdornments;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Mark Gottschling on Aug 31, 2021
 *
 */
public class SetSourceItem extends LootFunction {
	private static String SOURCE_ITEM = "sourceItem";
	private static String LEVELS = "levels";
	private static float MIN_GEM_LEVEL = 4F;
	private static float MAX_GEM_LEVEL = 11F;

	// the source item to use
	private ResourceLocation sourceItem;
	// a list of source item to randomly select from
	private List<ResourceLocation> sourceItems;
	// a range of source item levels to select from
	private IRandomRange levels;

	/**
	 * 
	 * @param conditions
	 */
	protected SetSourceItem(ILootCondition[] conditions, ResourceLocation sourceItem) {
		super(conditions);
		this.sourceItem = sourceItem;
	}

	protected SetSourceItem(ILootCondition[] conditions, IRandomRange levels) {
		super(conditions);
		this.levels = levels;
	}


	@Override
	public LootFunctionType getType() {
		return TreasureLootFunctions.SET_SOURCE_ITEM;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			if (this.sourceItem != null) {
				cap.setSourceItem(this.sourceItem);
			}
			else {
				// pick a sourceitem by level
				List<ResourceLocation> items = new ArrayList<>(3);
				Random random = context.getRandom();
				AtomicInteger level = new AtomicInteger(this.levels.getInt(random));
				AtomicBoolean isFound = new AtomicBoolean(false);
				AtomicInteger counter = new AtomicInteger(1);
				
				// loop through multiple times, decrementing level until a source item is found (max 3x)
				while (!isFound.get() && counter.getAndIncrement() < 3) {
					// get the sourceItem from registry
					TreasureCharms.getGemValues().forEach(gem -> {
						if (gem.getMaxLevel() == level.get()) {
							items.add(gem.getName());
							isFound.set(true);
						}
					});
					if (isFound.get()) {
						break;
					}
					level.decrementAndGet();
				}

				if (items.size() > 0) {
					ResourceLocation source = items.get(random.nextInt(items.size()));
					Treasure.LOGGER.debug("adding source item -> {}", source.toString());
					cap.setSourceItem(source);
//					TreasureAdornments.setHoverName(stack);
					
//					Item sourceItem = ForgeRegistries.ITEMS.getValue(source);
//					if (!cap.isCharmed()) {
//						Treasure.LOGGER.debug("not charmed, setting hover name");
//						stack.setHoverName(
//								((TranslatableComponent)sourceItem.getName(new ItemStack(sourceItem)))
//								.append(new StringTextComponent(" "))
//								.append(stack.getItem().getName(stack)));
//					}
//					else {
//						Treasure.LOGGER.debug("charmed, setting hover name");
//						stack.setHoverName(
//								((TranslatableComponent)sourceItem.getName(new ItemStack(sourceItem)))
//								.append(new StringTextComponent(" "))
//								.append(stack.getHoverName()));
//					}

				}
			}
			// update the hover name
			if (cap.isNamedByMaterial() || cap.isNamedByCharm()) {
				TreasureCharmables.setHoverName(stack);
			}
			Treasure.LOGGER.debug("hover name -> {}", stack.getHoverName().getString());
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
		private IRandomRange levels;

		protected SetSourceItem.Builder getThis() {
			return this;
		}

		public SetSourceItem.Builder withSourceItem(ResourceLocation sourceItem) {
			this.sourceItem = sourceItem;
			return this;
		}
		
		public SetSourceItem.Builder withLevels(IRandomRange levels) {
			this.levels = levels;
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
			json.add(SOURCE_ITEM, new JsonPrimitive(value.sourceItem.toString()));
		}

		@Override
		public SetSourceItem deserialize(JsonObject json, JsonDeserializationContext context,
				ILootCondition[] conditions) {


			ResourceLocation sourceItem = null;
			if (json.has(SOURCE_ITEM)) {
				String typeString = JSONUtils.getAsString(json, SOURCE_ITEM);
				try {
					sourceItem = ModUtils.asLocation(typeString);
				}
				catch(Exception e) {}
			}
			if (sourceItem != null) {
				return  new SetSourceItem(conditions, sourceItem);
			}

			IRandomRange levels = new RandomValueRange(MIN_GEM_LEVEL, MAX_GEM_LEVEL);
			if (json.has(LEVELS)) {
				levels = RandomRanges.deserialize(json.get(LEVELS), context);	
			}

			return new SetSourceItem(conditions, levels);
		}
	}
}
