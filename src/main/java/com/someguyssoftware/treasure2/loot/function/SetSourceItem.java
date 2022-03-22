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
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.TreasureCharmables;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * 
 * @author Mark Gottschling on Aug 31, 2021
 *
 */
@Deprecated
// TODO merge into RandomAdornment
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
	private RandomValueRange levels;

	/**
	 * 
	 * @param conditions
	 */
	protected SetSourceItem(LootCondition[] conditions, ResourceLocation sourceItem) {
		super(conditions);
		this.sourceItem = sourceItem;
	}

	protected SetSourceItem(LootCondition[] conditions, RandomValueRange levels) {
		super(conditions);
		this.levels = levels;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability cap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);
			
			if (this.sourceItem != null) {
				cap.setSourceItem(this.sourceItem);
			}
			else {
				// pick a sourceitem by level
				List<ResourceLocation> items = new ArrayList<>(3);
				Random random = rand;
				AtomicInteger level = new AtomicInteger(this.levels.generateInt(random));
				AtomicBoolean isFound = new AtomicBoolean(false);
				AtomicInteger counter = new AtomicInteger(1);
				
				// loop through multiple times, decrementing level until a source item is found (max 3x)
				while (!isFound.get() && counter.getAndIncrement() < 3) {
					// get the sourceItem from registry
					TreasureCharmableMaterials.getGemValues().forEach(gem -> {
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
					Treasure.logger.debug("adding source item -> {}", source.toString());
					cap.setSourceItem(source);
				}
			}
			// update the hover name
			if (cap.isNamedByMaterial() || cap.isNamedByCharm()) {
				TreasureCharmables.setHoverName(stack);
			}
			Treasure.logger.debug("hover name -> {}", stack.getDisplayName());

		}
		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<SetSourceItem> {

		public Serializer() {
			super(new ResourceLocation("treasure2:charm_randomly"), SetSourceItem.class);
		}

		@Override
		public void serialize(JsonObject json, SetSourceItem value, JsonSerializationContext context) {
			json.add(SOURCE_ITEM, new JsonPrimitive(value.sourceItem.toString()));
		}

		@Override
		public SetSourceItem deserialize(JsonObject json, JsonDeserializationContext context,
				LootCondition[] conditions) {


			ResourceLocation sourceItem = null;
			if (json.has(SOURCE_ITEM)) {
				String typeString = JsonUtils.getString(json, SOURCE_ITEM);
				try {
					sourceItem = ResourceLocationUtil.create(typeString);
				}
				catch(Exception e) {}
			}
			if (sourceItem != null) {
				return  new SetSourceItem(conditions, sourceItem);
			}

			RandomValueRange levels = new RandomValueRange(MIN_GEM_LEVEL, MAX_GEM_LEVEL);
			if (json.has(LEVELS)) {
				levels = JsonUtils.deserializeClass(json, LEVELS, context, RandomValueRange.class);	
			}

			return new SetSourceItem(conditions, levels);
		}
	}
}