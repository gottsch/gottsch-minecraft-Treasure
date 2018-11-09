/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

/**
 * @author Mark Gottschling on Nov 6, 2018
 *
 */
public abstract class TreasureLootEntry {
	protected final String entryName;
	protected final int weight;
	protected final int quality;
	protected final TreasureLootCondition[] conditions;

	/**
	 * 
	 * @param weightIn
	 * @param qualityIn
	 * @param conditionsIn
	 * @param entryName
	 */
	protected TreasureLootEntry(int weightIn, int qualityIn, TreasureLootCondition[] conditionsIn, String entryName) {
		this.weight = weightIn;
		this.quality = qualityIn;
		this.conditions = conditionsIn;
		this.entryName = entryName;
	}

	/**
	 * Gets the effective weight based on the loot entry's weight and quality multiplied by looter's luck.
	 */
	public int getEffectiveWeight(float luck) {
		return Math.max(MathHelper.floor((float)this.weight + (float)this.quality * luck), 0);
	}

	/**
	 * 
	 * @return
	 */
	public String getEntryName() { 
		return this.entryName; 
	}

	public abstract void addLoot(Collection<ItemStack> stacks, Random rand, TreasureLootContext context);

	protected abstract void serialize(JsonObject json, JsonSerializationContext context);

	/**
	 * 
	 * @author Mark Gottschling on Nov 6, 2018
	 *
	 */
	public static class Serializer implements JsonDeserializer<TreasureLootEntry>, JsonSerializer<TreasureLootEntry> 	{
		public TreasureLootEntry deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = JsonUtils.getJsonObject(element, "loot item");
			String s = JsonUtils.getString(jsonobject, "type");
			int i = JsonUtils.getInt(jsonobject, "weight", 1);
			int j = JsonUtils.getInt(jsonobject, "quality", 0);
			TreasureLootCondition[] alootcondition;

			if (jsonobject.has("conditions")) {
				alootcondition = (TreasureLootCondition[])JsonUtils.deserializeClass(jsonobject, "conditions", context, TreasureLootCondition[].class);
			}
			else {
				alootcondition = new TreasureLootCondition[0];
			}

			// TODO change - this method is unnecessary - the called method does nothing
			TreasureLootEntry ret = TreasureLootTableManager.deserializeJsonLootEntry(s, jsonobject, i, j, alootcondition);
			if (ret != null) return ret;

			if ("item".equals(s)) {
				return TreasureLootEntryItem.deserialize(jsonobject, context, i, j, alootcondition);
			}
			else if ("loot_table".equals(s)) {
				return TreasureLootEntryTable.deserialize(jsonobject, context, i, j, alootcondition);
			}
			else if ("empty".equals(s)) {
				return TreasureLootEntryEmpty.deserialize(jsonobject, context, i, j, alootcondition);
			}
			else {
				throw new JsonSyntaxException("Unknown loot entry type '" + s + "'");
			}
		}

		/**
		 * 
		 */
		public JsonElement serialize(TreasureLootEntry lootEntry, Type type, JsonSerializationContext context) {
			JsonObject jsonobject = new JsonObject();
			if (lootEntry.entryName != null && !lootEntry.entryName.startsWith("custom#"))
				jsonobject.addProperty("entryName", lootEntry.entryName);
			jsonobject.addProperty("weight", Integer.valueOf(lootEntry.weight));
			jsonobject.addProperty("quality", Integer.valueOf(lootEntry.quality));

			if (lootEntry.conditions.length > 0) {
				jsonobject.add("conditions", context.serialize(lootEntry.conditions));
			}

			// TODO change - useless - can be removed
			String lootEntryType = TreasureLootTableManager.getLootEntryType(lootEntry);
			
			if (lootEntryType != null) jsonobject.addProperty("type", lootEntryType);
			else
				if (lootEntry instanceof TreasureLootEntryItem) {
					jsonobject.addProperty("type", "item");
				}
				else if (lootEntry instanceof TreasureLootEntryTable) {
					jsonobject.addProperty("type", "loot_table");
				}
				else {
					if (!(lootEntry instanceof TreasureLootEntryEmpty)) {
						throw new IllegalArgumentException("Don't know how to serialize " + lootEntry);
					}

					jsonobject.addProperty("type", "empty");
				}

			lootEntry.serialize(jsonobject, context);
			return jsonobject;
		}
	}
}
