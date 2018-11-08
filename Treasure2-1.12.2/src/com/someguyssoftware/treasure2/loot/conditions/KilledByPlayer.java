/**
 * 
 */
package com.someguyssoftware.treasure2.loot.conditions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class KilledByPlayer implements TreasureLootCondition {
	private final boolean inverse;

	public KilledByPlayer(boolean inverseIn) {
		this.inverse = inverseIn;
	}

	public boolean testCondition(Random rand, TreasureLootContext context) {
		boolean flag = context.getKillerPlayer() != null;
		return flag == !this.inverse;
	}

	public static class Serializer extends TreasureLootCondition.Serializer<KilledByPlayer> {
		protected Serializer() {
			super(new ResourceLocation("killed_by_player"), KilledByPlayer.class);
		}

		public void serialize(JsonObject json, KilledByPlayer value, JsonSerializationContext context) {
			json.addProperty("inverse", Boolean.valueOf(value.inverse));
		}

		public KilledByPlayer deserialize(JsonObject json, JsonDeserializationContext context) {
			return new KilledByPlayer(JsonUtils.getBoolean(json, "inverse", false));
		}
	}
}
