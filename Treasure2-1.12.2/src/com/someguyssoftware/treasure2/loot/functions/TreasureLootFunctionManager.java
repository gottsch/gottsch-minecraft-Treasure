/**
 * 
 */
package com.someguyssoftware.treasure2.loot.functions;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class TreasureLootFunctionManager {
	private static final Map<ResourceLocation, TreasureLootFunction.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.<ResourceLocation, TreasureLootFunction.Serializer<?>>newHashMap();
	private static final Map<Class<? extends TreasureLootFunction>, TreasureLootFunction.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.<Class<? extends TreasureLootFunction>, TreasureLootFunction.Serializer<?>>newHashMap();

	public static <T extends TreasureLootFunction> void registerFunction(TreasureLootFunction.Serializer<? extends T> serializer) {
		ResourceLocation resourcelocation = serializer.getFunctionName();
		Class<T> oclass = (Class<T>) serializer.getFunctionClass();

		if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
			throw new IllegalArgumentException("Can't re-register item function name " + resourcelocation);
		} else if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
			throw new IllegalArgumentException("Can't re-register item function class " + oclass.getName());
		} else {
			NAME_TO_SERIALIZER_MAP.put(resourcelocation, serializer);
			CLASS_TO_SERIALIZER_MAP.put(oclass, serializer);
		}
	}

	public static TreasureLootFunction.Serializer<?> getSerializerForName(ResourceLocation location) {
		TreasureLootFunction.Serializer<?> serializer = (TreasureLootFunction.Serializer) NAME_TO_SERIALIZER_MAP.get(location);

		if (serializer == null) {
			throw new IllegalArgumentException("Unknown TreasureLoot item function '" + location + "'");
		} else {
			return serializer;
		}
	}

	public static <T extends TreasureLootFunction> TreasureLootFunction.Serializer<T> getSerializerFor(T functionClass) {
		TreasureLootFunction.Serializer<T> serializer = (TreasureLootFunction.Serializer) CLASS_TO_SERIALIZER_MAP.get(functionClass.getClass());

		if (serializer == null) {
			throw new IllegalArgumentException("Unknown TreasureLoot item function " + functionClass);
		} else {
			return serializer;
		}
	}

	static {
		registerFunction(new SetCount.Serializer());
		registerFunction(new SetMetadata.Serializer());
		registerFunction(new EnchantWithLevels.Serializer());
		registerFunction(new EnchantRandomly.Serializer());
		registerFunction(new SetNBT.Serializer());
		registerFunction(new Smelt.Serializer());
		registerFunction(new LootingEnchantBonus.Serializer());
		registerFunction(new SetDamage.Serializer());
		registerFunction(new SetAttributes.Serializer());
	}

	public static class Serializer implements JsonDeserializer<TreasureLootFunction>, JsonSerializer<TreasureLootFunction> {
		public TreasureLootFunction deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
			JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "function");
			ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "function"));
			TreasureLootFunction.Serializer<?> serializer;

			try {
				serializer = TreasureLootFunctionManager.getSerializerForName(resourcelocation);
			} catch (IllegalArgumentException var8) {
				throw new JsonSyntaxException("Unknown function '" + resourcelocation + "'");
			}

			return serializer.deserialize(jsonobject, p_deserialize_3_,
					(TreasureLootCondition[]) JsonUtils.deserializeClass(jsonobject, "conditions", new TreasureLootCondition[0], p_deserialize_3_, TreasureLootCondition[].class));
		}

		public JsonElement serialize(TreasureLootFunction p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			TreasureLootFunction.Serializer<TreasureLootFunction> serializer = TreasureLootFunctionManager.<TreasureLootFunction>getSerializerFor(p_serialize_1_);
			JsonObject jsonobject = new JsonObject();
			serializer.serialize(jsonobject, p_serialize_1_, p_serialize_3_);
			jsonobject.addProperty("function", serializer.getFunctionName().toString());

			if (p_serialize_1_.getConditions() != null && p_serialize_1_.getConditions().length > 0) {
				jsonobject.add("conditions", p_serialize_3_.serialize(p_serialize_1_.getConditions()));
			}

			return jsonobject;
		}
	}
}
