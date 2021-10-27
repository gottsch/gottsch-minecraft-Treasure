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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.item.charm.ICharmable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * 
 * @author Mark Gottschling on Dec 24, 2020
 *
 */
public class SetSlots extends LootFunction {
	private final RandomValueRange slots;

	/**
	 * 
	 * @param conditions
	 * @param slots
	 */
	public SetSlots(LootCondition[] conditions, RandomValueRange countRange) {
		super(conditions);
		this.slots = countRange;
	}

	/**
	 * 
	 */
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		// check for charmable capability
		if (stack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
			ICharmInventoryCapability charmableCap = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			charmableCap.setSlots(Math.min(this.slots.generateInt(rand), ((ICharmable)stack.getItem()).getMaxSlots()));
		}
		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<SetSlots> {
		public Serializer() {
			super(new ResourceLocation("treasure2:set_slots"), SetSlots.class);
		}

		public void serialize(JsonObject object, SetSlots functionClazz,
				JsonSerializationContext serializationContext) {
			object.add("slots", serializationContext.serialize(functionClazz.slots));
		}

		public SetSlots deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditions) {
			return new SetSlots(conditions, (RandomValueRange) JsonUtils.deserializeClass(object, "slots",
					deserializationContext, RandomValueRange.class));
		}
	}
}