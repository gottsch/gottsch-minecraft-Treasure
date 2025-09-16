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
package mod.gottsch.forge.treasure2.core.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

/**
 * @author by Mark Gottschling on 9/1/2025
 */
public class LootTableTypes implements ILootTableTypes {
    // a Codec to parse this data from a JSON file.
    public static final Codec<LootTableTypes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(LootTableTypes::getName)
    ).apply(instance, LootTableTypes::new));

    public static final ILootTableTypes NONE = new LootTableTypes("none");

    // the name of the rarity, used for display.
    private final String name;

    public LootTableTypes(String name) {
        this.name = name.trim().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LootTableTypes that = (LootTableTypes) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "LootTableTypes{" +
                "name='" + name + '\'' +
                '}';
    }
}
