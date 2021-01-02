package com.someguyssoftware.treasure2.loot.function;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
 * @author Mark Gottschling on Dec 29, 2020
 *
 */
public class SetCustomName extends LootFunction {
	private final String customName;

	/**
	 * 
	 * @param conditions
	 * @param slots
	 */
	public SetCustomName(LootCondition[] conditions, String customName) {
		super(conditions);
		this.customName = customName;
	}

	/**
	 * 
	 */
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		// check for charmable capability
		if (stack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
			ICharmableCapability charmableCap = stack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
			charmableCap.setCustomName(this.customName);
		}
		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<SetCustomName> {
		public Serializer() {
			super(new ResourceLocation("treasure2:set_custom_name"), SetCustomName.class);
		}

		public void serialize(JsonObject json, SetCustomName value, JsonSerializationContext serializationContext) {
			json.add("customName", new JsonPrimitive(value.customName));
		}

		/**
		 * 
		 */
		public SetCustomName deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditions) {
			String name = "";
			if (json.has("customName")) {
				name = JsonUtils.getString(json, "customName");	
			}
			return new SetCustomName(conditions, name);
		}
	}
}