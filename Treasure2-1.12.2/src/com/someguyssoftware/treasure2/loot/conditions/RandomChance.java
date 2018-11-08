package com.someguyssoftware.treasure2.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import java.util.Random;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class RandomChance implements TreasureLootCondition {
    private final float chance;

    public RandomChance(float chanceIn) {
        this.chance = chanceIn;
    }

    public boolean testCondition(Random rand, TreasureLootContext context) {
        return rand.nextFloat() < this.chance;
    }

    /**
     * 
     * @author Mark Gottschling on Nov 7, 2018
     *
     */
    public static class Serializer extends TreasureLootCondition.Serializer<RandomChance> {
            protected Serializer() {
                super(new ResourceLocation("random_chance"), RandomChance.class);
            }

            public void serialize(JsonObject json, RandomChance value, JsonSerializationContext context) {
                json.addProperty("chance", Float.valueOf(value.chance));
            }

            public RandomChance deserialize(JsonObject json, JsonDeserializationContext context) {
                return new RandomChance(JsonUtils.getFloat(json, "chance"));
            }
        }
}