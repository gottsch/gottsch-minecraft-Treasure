/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.mobset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * @author by Mark Gottschling on 9/18/2025
 */
public record WeightedMob(ResourceLocation id, int weight) {
    public static final Codec<WeightedMob> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // Use the built-in String codec for the "id" field.
            ResourceLocation.CODEC.fieldOf("id").forGetter(WeightedMob::id),
            Codec.INT.fieldOf("weight").forGetter(WeightedMob::weight)
    ).apply(instance, WeightedMob::new));

    public WeightedMob withWeight(int weight) {
        return new WeightedMob(this.id, weight);
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "WeightedMob{" +
                "id=" + id +
                ", weight=" + weight +
                '}';
    }

}
