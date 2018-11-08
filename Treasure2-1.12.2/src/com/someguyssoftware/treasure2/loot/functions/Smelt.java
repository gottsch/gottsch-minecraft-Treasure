package com.someguyssoftware.treasure2.loot.functions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class Smelt extends TreasureLootFunction {

	public Smelt(TreasureLootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	public ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context) {
		if (stack.isEmpty()) {
			return stack;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stack);

			if (itemstack.isEmpty()) {
				Treasure.logger.warn("Couldn't smelt {} because there is no smelting recipe", (Object) stack);
				return stack;
			} else {
				ItemStack itemstack1 = itemstack.copy();
				itemstack1.setCount(stack.getCount());
				return itemstack1;
			}
		}
	}

	public static class Serializer extends TreasureLootFunction.Serializer<Smelt> {
		protected Serializer() {
			super(new ResourceLocation("furnace_smelt"), Smelt.class);
		}

		public void serialize(JsonObject object, Smelt functionClazz, JsonSerializationContext serializationContext) {
		}

		public Smelt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn) {
			return new Smelt(conditionsIn);
		}
	}
}