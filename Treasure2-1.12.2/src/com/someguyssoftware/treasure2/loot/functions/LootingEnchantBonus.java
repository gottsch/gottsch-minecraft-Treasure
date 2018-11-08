package com.someguyssoftware.treasure2.loot.functions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class LootingEnchantBonus extends TreasureLootFunction {
	private final RandomValueRange count;
	private final int limit;

	/**
	 * 
	 * @param conditions
	 * @param countIn
	 * @param limitIn
	 */
	public LootingEnchantBonus(TreasureLootCondition[] conditions, RandomValueRange countIn, int limitIn) {
		super(conditions);
		this.count = countIn;
		this.limit = limitIn;
	}

	public ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context) {
		Entity entity = context.getKiller();

		if (entity instanceof EntityLivingBase) {
			int i = context.getLootingModifier();

			if (i == 0) {
				return stack;
			}

			float f = (float) i * this.count.generateFloat(rand);
			stack.grow(Math.round(f));

			if (this.limit != 0 && stack.getCount() > this.limit) {
				stack.setCount(this.limit);
			}
		}

		return stack;
	}

	public static class Serializer extends TreasureLootFunction.Serializer<LootingEnchantBonus> {
		protected Serializer() {
			super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
		}

		public void serialize(JsonObject object, LootingEnchantBonus functionClazz, JsonSerializationContext serializationContext) {
			object.add("count", serializationContext.serialize(functionClazz.count));

			if (functionClazz.limit > 0) {
				object.add("limit", serializationContext.serialize(Integer.valueOf(functionClazz.limit)));
			}
		}

		public LootingEnchantBonus deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn) {
			int i = JsonUtils.getInt(object, "limit", 0);
			return new LootingEnchantBonus(conditionsIn, (RandomValueRange) JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class), i);
		}
	}
}