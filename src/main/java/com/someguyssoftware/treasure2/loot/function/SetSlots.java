package com.someguyssoftware.treasure2.loot.function;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;

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
	private final RandomValueRange countRange;

	/**
	 * 
	 * @param conditions
	 * @param countRange
	 */
	public SetSlots(LootCondition[] conditions, RandomValueRange countRange) {
		super(conditions);
		this.countRange = countRange;
	}

	/**
	 * 
	 */
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		// check for charmable capability
		if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmableCapability charmableCap = stack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
			charmableCap.setSlots(this.countRange.generateInt(rand));
		}
		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<SetSlots> {
		protected Serializer() {
			super(new ResourceLocation("set_slots"), SetSlots.class);
		}

		public void serialize(JsonObject object, SetSlots functionClazz,
				JsonSerializationContext serializationContext) {
			object.add("slots", serializationContext.serialize(functionClazz.countRange));
		}

		public SetSlots deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditions) {
			return new SetSlots(conditions, (RandomValueRange) JsonUtils.deserializeClass(object, "slots",
					deserializationContext, RandomValueRange.class));
		}
	}
}