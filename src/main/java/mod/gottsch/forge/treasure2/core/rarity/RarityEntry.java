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

import java.util.Objects;

/**
 * TODO Rename this class to Rarity is so.
 *
 * represents a single rarity entry as defined in a data pack.
 * @author by Mark Gottschling on 8/25/2025
 */
public class RarityEntry implements IRarityEntry {
    // a Codec to parse this data from a JSON file.
    public static final Codec<RarityEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(RarityEntry::getName),
            Codec.INT.fieldOf("order").forGetter(RarityEntry::getOrder)
    ).apply(instance, RarityEntry::new));

    public static final IRarityEntry NONE = new RarityEntry("none", 0);

    // the name of the rarity, used for display.
    private final String name;
    private final int order;

    /**
     *
     * @param name the name of the Rarity ex common
     */
    public RarityEntry(String name, int order) {
        this.name = name.trim().toLowerCase();
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RarityEntry that = (RarityEntry) o;
        return order == that.order && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, order);
    }

    @Override
    public String toString() {
        return "RarityEntry {" +
                "name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
