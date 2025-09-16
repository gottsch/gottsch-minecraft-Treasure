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
package mod.gottsch.forge.treasure2.core.rarity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * @author by Mark Gottschling on 8/26/2025
 */
public class RarityWeight {
    // a Codec to parse this data from a JSON file.
    public static final Codec<RarityWeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("rarity").forGetter(RarityWeight::getRarity),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(RarityWeight::getWeight)
    ).apply(instance, RarityWeight::new));

    private final ResourceLocation rarity;
    private final int weight;

    public RarityWeight(ResourceLocation rarity, int weight) {
        this.rarity = rarity;
        this.weight = weight;
    }
    public ResourceLocation getRarity() {
        return rarity;
    }

    public int getWeight() {
        return weight;
    }
}
