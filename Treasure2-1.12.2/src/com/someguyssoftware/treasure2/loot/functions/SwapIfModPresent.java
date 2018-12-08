/**
 * 
 */
package com.someguyssoftware.treasure2.loot.functions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.gottschcore.item.util.ItemUtil;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;
import com.someguyssoftware.treasure2.loot.conditions.ModPresent;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootConditionManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Dec 6, 2018
 *
 */
public class SwapIfModPresent extends TreasureLootFunction {
	private final String modID;
	private final String itemID;

	/**
	 * 
	 * @param conditionsIn
	 * @param modID
	 * @param itemID
	 */
	public SwapIfModPresent(TreasureLootCondition[] conditionsIn, String itemID, String modID) {
		super(conditionsIn);
		this.modID = modID;
		this.itemID = itemID;
	}

	/**
	 * 
	 */
	public ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context) {
		ModPresent mp = new ModPresent(this.modID);
		
		// if ModPresent then return the alternate item
		ItemStack newStack = stack;
		if (mp.testCondition(rand, context)) {
			newStack = new ItemStack(ItemUtil.getItemFromName(itemID));
		}
		return newStack;
	}

	/**
	 * 
	 * @author Mark Gottschling on Dec 6, 2018
	 *
	 */
	public static class Serializer extends TreasureLootFunction.Serializer<SwapIfModPresent> {
		protected Serializer() {
			super(new ResourceLocation("treasure2:swao_if_mod_present"), SwapIfModPresent.class);
		}

		public void serialize(JsonObject object, SwapIfModPresent functionClazz, JsonSerializationContext serializationContext) {
			object.add("itemid", serializationContext.serialize(String.valueOf(functionClazz.itemID)));
			object.add("modid", serializationContext.serialize(String.valueOf(functionClazz.modID)));
		}

		/**
		 * 
		 */
		public SwapIfModPresent deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn) {
			return new SwapIfModPresent(conditionsIn, (String) JsonUtils.deserializeClass(object, "itemid", deserializationContext, String.class),
					(String) JsonUtils.deserializeClass(object, "modid", deserializationContext, String.class));
		}
	}
}