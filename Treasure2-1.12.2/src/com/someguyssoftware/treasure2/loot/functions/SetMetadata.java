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
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class SetMetadata extends TreasureLootFunction {
	private final RandomValueRange metaRange;

	public SetMetadata(TreasureLootCondition[] conditionsIn, RandomValueRange metaRangeIn) {
		super(conditionsIn);
		this.metaRange = metaRangeIn;
	}

	public ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context) {
		if (stack.isItemStackDamageable()) {
			Treasure.logger.warn("Couldn't set data of loot item {}", (Object) stack);
		} else {
			stack.setItemDamage(this.metaRange.generateInt(rand));
		}

		return stack;
	}

	public static class Serializer extends TreasureLootFunction.Serializer<SetMetadata> {
		protected Serializer() {
			super(new ResourceLocation("set_data"), SetMetadata.class);
		}

		public void serialize(JsonObject object, SetMetadata functionClazz, JsonSerializationContext serializationContext) {
			object.add("data", serializationContext.serialize(functionClazz.metaRange));
		}

		public SetMetadata deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn) {
			return new SetMetadata(conditionsIn, (RandomValueRange) JsonUtils.deserializeClass(object, "data", deserializationContext, RandomValueRange.class));
		}
	}
}