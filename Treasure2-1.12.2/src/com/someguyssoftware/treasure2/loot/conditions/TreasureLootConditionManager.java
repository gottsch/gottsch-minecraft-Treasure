/**
 * 
 */
package com.someguyssoftware.treasure2.loot.conditions;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class TreasureLootConditionManager {
	private static final Map<ResourceLocation, TreasureLootCondition.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.<ResourceLocation, TreasureLootCondition.Serializer<?>>newHashMap();
	private static final Map<Class<? extends TreasureLootCondition>, TreasureLootCondition.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.<Class<? extends TreasureLootCondition>, TreasureLootCondition.Serializer<?>>newHashMap();

	public static <T extends TreasureLootCondition> void registerCondition(TreasureLootCondition.Serializer<? extends T> condition) {
		ResourceLocation resourcelocation = condition.getLootTableLocation();
		Class<T> oclass = (Class<T>) condition.getConditionClass();

		if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
			throw new IllegalArgumentException("Can't re-register item condition name " + resourcelocation);
		} else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
			throw new IllegalArgumentException("Can't re-register item condition class " + oclass.getName());
		} else {
			NAME_TO_SERIALIZER_MAP.put(resourcelocation, condition);
			CLASS_TO_SERIALIZER_MAP.put(oclass, condition);
		}
	}

	public static boolean testAllConditions(Iterable<TreasureLootCondition> conditions, Random rand, TreasureLootContext context) {
		if (conditions == null)
			return true;
		for (TreasureLootCondition cond : conditions)
			if (!cond.testCondition(rand, context))
				return false;
		return true;
	}

	public static boolean testAllConditions(@Nullable TreasureLootCondition[] conditions, Random rand, TreasureLootContext context) {
		if (conditions == null) {
			return true;
		} else {
			for (TreasureLootCondition lootcondition : conditions) {
				if (!lootcondition.testCondition(rand, context)) {
					return false;
				}
			}

			return true;
		}
	}

	public static TreasureLootCondition.Serializer<?> getSerializerForName(ResourceLocation location) {
		TreasureLootCondition.Serializer<?> serializer = (TreasureLootCondition.Serializer) NAME_TO_SERIALIZER_MAP.get(location);

		if (serializer == null) {
			throw new IllegalArgumentException("Unknown loot item condition '" + location + "'");
		} else {
			return serializer;
		}
	}

	public static <T extends TreasureLootCondition> TreasureLootCondition.Serializer<T> getSerializerFor(T conditionClass) {
		TreasureLootCondition.Serializer<T> serializer = (TreasureLootCondition.Serializer) CLASS_TO_SERIALIZER_MAP.get(conditionClass.getClass());

		if (serializer == null) {
			throw new IllegalArgumentException("Unknown loot item condition " + conditionClass);
		} else {
			return serializer;
		}
	}

	static {
		registerCondition(new RandomChance.Serializer());
		registerCondition(new RandomChanceWithLooting.Serializer());
		registerCondition(new EntityHasProperty.Serializer());
		registerCondition(new KilledByPlayer.Serializer());
		registerCondition(new EntityHasScore.Serializer());
	}

	public static class Serializer implements JsonDeserializer<TreasureLootCondition>, JsonSerializer<TreasureLootCondition> {
		public TreasureLootCondition deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
			JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "condition");
			ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "condition"));
			TreasureLootCondition.Serializer<?> serializer;

			try {
				serializer = TreasureLootConditionManager.getSerializerForName(resourcelocation);
			} catch (IllegalArgumentException var8) {
				throw new JsonSyntaxException("Unknown condition '" + resourcelocation + "'");
			}

			return serializer.deserialize(jsonobject, p_deserialize_3_);
		}

		public JsonElement serialize(TreasureLootCondition p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			TreasureLootCondition.Serializer<TreasureLootCondition> serializer = TreasureLootConditionManager.<TreasureLootCondition>getSerializerFor(p_serialize_1_);
			JsonObject jsonobject = new JsonObject();
			serializer.serialize(jsonobject, p_serialize_1_, p_serialize_3_);
			jsonobject.addProperty("condition", serializer.getLootTableLocation().toString());
			return jsonobject;
		}
	}
}
