/**
 * 
 */
package com.someguyssoftware.treasure2.loot.functions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;
import com.someguyssoftware.treasure2.loot.conditions.TreasureLootCondition;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public abstract class TreasureLootFunction {
    private final TreasureLootCondition[] conditions;

    protected TreasureLootFunction(TreasureLootCondition[] conditionsIn)
    {
        this.conditions = conditionsIn;
    }

    public abstract ItemStack apply(ItemStack stack, Random rand, TreasureLootContext context);

    public TreasureLootCondition[] getConditions()
    {
        return this.conditions;
    }

    public abstract static class Serializer<T extends TreasureLootFunction>
        {
            private final ResourceLocation lootTableLocation;
            private final Class<T> functionClass;

            protected Serializer(ResourceLocation location, Class<T> clazz)
            {
                this.lootTableLocation = location;
                this.functionClass = clazz;
            }

            public ResourceLocation getFunctionName()
            {
                return this.lootTableLocation;
            }

            public Class<T> getFunctionClass()
            {
                return this.functionClass;
            }

            public abstract void serialize(JsonObject object, T functionClazz, JsonSerializationContext serializationContext);

            public abstract T deserialize(JsonObject object, JsonDeserializationContext deserializationContext, TreasureLootCondition[] conditionsIn);
        }
}
