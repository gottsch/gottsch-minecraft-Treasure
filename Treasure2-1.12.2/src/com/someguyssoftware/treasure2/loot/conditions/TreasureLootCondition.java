/**
 * 
 */
package com.someguyssoftware.treasure2.loot.conditions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public interface TreasureLootCondition {
	boolean testCondition(Random rand, TreasureLootContext context);

	/**
	 * 
	 * @author Mark Gottschling on Nov 7, 2018
	 *
	 * @param <T>
	 */
	public abstract static class Serializer<T extends TreasureLootCondition> {
		private final ResourceLocation lootTableLocation;
		private final Class<T> conditionClass;

		protected Serializer(ResourceLocation location, Class<T> clazz) {
			this.lootTableLocation = location;
			this.conditionClass = clazz;
		}

		public ResourceLocation getLootTableLocation() {
			return this.lootTableLocation;
		}

		public Class<T> getConditionClass() {
			return this.conditionClass;
		}

		public abstract void serialize(JsonObject json, T value, JsonSerializationContext context);

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
	}

}
