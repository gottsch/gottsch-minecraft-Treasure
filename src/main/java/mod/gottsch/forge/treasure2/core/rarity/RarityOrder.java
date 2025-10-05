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

import java.util.Comparator;

/**
 * @author by Mark Gottschling on 9/4/2025
 */
public record RarityOrder(ResourceLocation rarity, int order) {
    // a Codec to parse this data from a JSON file.
    public static final Codec<RarityOrder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("rarity").forGetter(RarityOrder::rarity),
            Codec.INT.optionalFieldOf("order", 1).forGetter(RarityOrder::order)
    ).apply(instance, RarityOrder::new));

    /**
     * a reusable, efficient comparator that sorts RarityOrder objects
     * by their 'order' field in ascending numerical order.
     */
    public static final Comparator<RarityOrder> BY_ORDER = Comparator.comparingInt(RarityOrder::order);

    @Override
    public String toString() {
        return "RarityOrder{" +
                "order=" + order +
                ", rarity=" + rarity +
                '}';
    }
}
