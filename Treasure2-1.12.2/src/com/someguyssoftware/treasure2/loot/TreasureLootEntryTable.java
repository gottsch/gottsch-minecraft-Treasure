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
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 6, 2018
 *
 */
public class TreasureLootEntryTable extends TreasureLootEntry {

    protected final ResourceLocation table;

    /**
     * 
     * @param tableIn
     * @param weightIn
     * @param qualityIn
     * @param conditionsIn
     * @param entryName
     */
    public TreasureLootEntryTable(ResourceLocation tableIn, int weightIn, int qualityIn, TreasureLootCondition[] conditionsIn, String entryName) {
        super(weightIn, qualityIn, conditionsIn, entryName);
        this.table = tableIn;
    }
    
    /**
     * TODO have to replace LootContext
     */
    @Override
    public void addLoot(Collection<ItemStack> stacks, Random rand, TreasureLootContext context) {
    	TreasureLootTable loottable = context.getLootTableManager().getLootTableFromLocation(this.table);
        Collection<ItemStack> collection = loottable.generateLootFromPools(rand, context);
        stacks.addAll(collection);
    }

    /**
     * 
     */
    @Override
    protected void serialize(JsonObject json, JsonSerializationContext context) {
        json.addProperty("name", this.table.toString());
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
    public static TreasureLootEntryTable deserialize(JsonObject object, JsonDeserializationContext deserializationContext, int weightIn, int qualityIn, TreasureLootCondition[] conditionsIn) {
    	String name = TreasureLootTableManager.readLootEntryName(object, "loot_table");
        ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(object, "name"));
        return new TreasureLootEntryTable(resourcelocation, weightIn, qualityIn, conditionsIn, name);
    }
}
