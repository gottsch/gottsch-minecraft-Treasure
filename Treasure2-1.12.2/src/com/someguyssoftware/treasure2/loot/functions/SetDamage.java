package com.someguyssoftware.treasure2.loot.functions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class SetDamage extends TreasureLootFunction {
	private final RandomValueRange damageRange;

	public SetDamage(TreasureLootCondition[] conditionsIn, RandomValueRange damageRangeIn) {
		super(conditionsIn);
		this.damageRange = damageRangeIn;
	}

	public ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context) {
		if (stack.isItemStackDamageable()) {
			float f = 1.0F - this.damageRange.generateFloat(rand);
			stack.setItemDamage(MathHelper.floor(f * (float) stack.getMaxDamage()));
		} else {
			Treasure.logger.warn("Couldn't set damage of loot item {}", (Object) stack);
		}

		return stack;
	}

	public static class Serializer extends TreasureLootFunction.Serializer<SetDamage> {
		protected Serializer() {
			super(new ResourceLocation("set_damage"), SetDamage.class);
		}

		public void serialize(JsonObject object, SetDamage functionClazz, JsonSerializationContext serializationContext) {
			object.add("damage", serializationContext.serialize(functionClazz.damageRange));
		}

		public SetDamage deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn) {
			return new SetDamage(conditionsIn, (RandomValueRange) JsonUtils.deserializeClass(object, "damage", deserializationContext, RandomValueRange.class));
		}
	}
}