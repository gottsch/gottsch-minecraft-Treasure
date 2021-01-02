package com.someguyssoftware.treasure2.loot.function;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;
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
		if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmableCapability charmableCap = stack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
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