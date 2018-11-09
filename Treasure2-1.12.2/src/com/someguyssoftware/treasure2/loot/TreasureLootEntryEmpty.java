/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling on Nov 6, 2018
 *
 */
public class TreasureLootEntryEmpty extends TreasureLootEntry {

	/**
	 * 
	 * @param weightIn
	 * @param qualityIn
	 * @param conditionsIn
	 * @param entryName
	 */
	public TreasureLootEntryEmpty(int weightIn, int qualityIn, TreasureLootCondition[] conditionsIn, String entryName) {
		super(weightIn, qualityIn, conditionsIn, entryName);
	}

	/**
	 * 
	 */
	@Override
	public void addLoot(Collection<ItemStack> stacks, Random rand, TreasureLootContext context) {   }

	/**
	 * 
	 */
	@Override
	protected void serialize(JsonObject json, JsonSerializationContext context) {   }

	/**
	 * 
	 * @param object
	 * @param deserializationContext
	 * @param weightIn
	 * @param qualityIn
	 * @param conditionsIn
	 * @return
	 */
	public static TreasureLootEntryEmpty deserialize(
			JsonObject object, 
			JsonDeserializationContext deserializationContext, 
			int weightIn, 
			int qualityIn, 
			TreasureLootCondition[] conditionsIn) {

		return new TreasureLootEntryEmpty(
				weightIn, 
				qualityIn, 
				conditionsIn, 
				TreasureLootTableManager.readLootEntryName(object, "empty"));
	}
}
