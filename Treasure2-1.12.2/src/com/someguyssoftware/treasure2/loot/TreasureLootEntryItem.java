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
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootConditionManager;
import com.someguyssoftware.treasure2.loot.functions.TreasureLootFunction;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 6, 2018
 *
 */
public class TreasureLootEntryItem extends TreasureLootEntry {
	protected final Item item;
	protected final TreasureLootFunction[] functions;

	/**
	 * 
	 * @param itemIn
	 * @param weightIn
	 * @param qualityIn
	 * @param functionsIn
	 * @param conditionsIn
	 * @param entryName
	 */
    public TreasureLootEntryItem(Item itemIn, int weightIn, int qualityIn, TreasureLootFunction[] functionsIn, TreasureLootCondition[] conditionsIn, String entryName) {
        super(weightIn, qualityIn, conditionsIn, entryName);
        this.item = itemIn;
        this.functions = functionsIn;
    }
    
	@Override
	public void addLoot(Collection<ItemStack> stacks, Random rand, TreasureLootContext context) {
		ItemStack itemstack = new ItemStack(this.item);

		for (TreasureLootFunction lootfunction : this.functions) {
			if (TreasureLootConditionManager.testAllConditions(lootfunction.getConditions(), rand, context)) {
				itemstack = lootfunction.apply(itemstack, rand, context);
			}
		}

		if (!itemstack.isEmpty()) {
			if (itemstack.getCount() < this.item.getItemStackLimit(itemstack)) {
				stacks.add(itemstack);
			}
			else {
				int i = itemstack.getCount();

				while (i > 0)	{
					ItemStack itemstack1 = itemstack.copy();
					itemstack1.setCount(Math.min(itemstack.getMaxStackSize(), i));
					i -= itemstack1.getCount();
					stacks.add(itemstack1);
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void serialize(JsonObject json, JsonSerializationContext context) {
		if (this.functions != null && this.functions.length > 0) {
			json.add("functions", context.serialize(this.functions));
		}

		ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);

		if (resourcelocation == null) {
			throw new IllegalArgumentException("Can't serialize unknown item " + this.item);
		}
		else {
			json.addProperty("name", resourcelocation.toString());
		}
	}

	/**
	 * 
	 * @param object
	 * @param deserializationContext
	 * @param weightIn
	 * @param qualityIn
	 * @param conditionsIn
	 * @return
	 */
	public static TreasureLootEntryItem deserialize(JsonObject object, JsonDeserializationContext deserializationContext, int weightIn, int qualityIn, TreasureLootCondition[] conditionsIn) {
		String name = TreasureLootTableManager.readLootEntryName(object, "item");
		Item item = JsonUtils.getItem(object, "name");
		TreasureLootFunction[] alootfunction;

		if (object.has("functions")) {
			alootfunction = (TreasureLootFunction[])JsonUtils.deserializeClass(object, "functions", deserializationContext, TreasureLootFunction[].class);
		}
		else {
			alootfunction = new TreasureLootFunction[0];
		}

		return new TreasureLootEntryItem(item, weightIn, qualityIn, alootfunction, conditionsIn, name);
	}
}
