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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 *
 * a data-driven association that links a Rarity to a tag.
 * this class is designed to be flexible and can handle either Item or Block tags.
 *
 * @author by Mark Gottschling on 8/31/2025
 */
public record RarityLootTableAssociation(ResourceLocation rarityId, ResourceLocation lootTableId, ResourceLocation typeId) {

    // a Codec to parse this data from a JSON file.
    public static final Codec<RarityLootTableAssociation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("rarity").forGetter(RarityLootTableAssociation::rarityId),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(RarityLootTableAssociation::lootTableId),
            ResourceLocation.CODEC.fieldOf("type").forGetter(RarityLootTableAssociation::typeId)
    ).apply(instance, RarityLootTableAssociation::new));
}
