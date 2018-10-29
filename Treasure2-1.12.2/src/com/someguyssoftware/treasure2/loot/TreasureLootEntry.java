/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;

/**
 * @author Mark Gottschling on Oct 28, 2018
 *
 */
public class TreasureLootEntry extends LootEntry {

	protected TreasureLootEntry(int weightIn, int qualityIn, LootCondition[] conditionsIn, String entryName) {
		super(weightIn, qualityIn, conditionsIn, entryName);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.storage.loot.LootEntry#addLoot(java.util.Collection, java.util.Random, net.minecraft.world.storage.loot.LootContext)
	 */
	@Override
	public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.storage.loot.LootEntry#serialize(com.google.gson.JsonObject, com.google.gson.JsonSerializationContext)
	 */
	@Override
	protected void serialize(JsonObject json, JsonSerializationContext context) {
		// TODO Auto-generated method stub

	}

}
