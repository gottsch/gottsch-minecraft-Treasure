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
package mod.gottsch.forge.treasure2.core.structure.templatesystem.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeight;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * @author by Mark Gottschling on 8/28/2025
 */
public class ChestSubprocessorData {

    // subprocessor type
    private final ResourceLocation type;
    // chest rarity
    private final ResourceLocation rarity;
    // probability that chest is a mimic
    private final Double mimicProbability;
    // optional list of rarity-weights for loot table rarities. mutually-exclusive with lootTables
    private final List<RarityWeight> lootTableRarities;
    // optional list of specific loot tables. mutually-exclusive with lootTableRarities
    private final List<ResourceLocation> lootTables;
    // optional list of lock rarites.
    private final List<ResourceLocation> lockRarities;
    // NOTE deprecated?
    private final List<ResourceLocation> biomesBlacklist;

    // a Codec to parse this data from a JSON file.
    public static final Codec<ChestSubprocessorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(

            ResourceLocation.CODEC.fieldOf("type").forGetter(ChestSubprocessorData::getType),
            ResourceLocation.CODEC.fieldOf("rarity").forGetter(ChestSubprocessorData::getRarity),
            Codec.DOUBLE.optionalFieldOf("mimic", 0.0).forGetter(ChestSubprocessorData::getMimicProbability),
            RarityWeight.CODEC.listOf().optionalFieldOf("loot_table_rarities", List.of()).forGetter(ChestSubprocessorData::getLootTableRarities),
            ResourceLocation.CODEC.listOf().optionalFieldOf("loot_tables", List.of()).forGetter(ChestSubprocessorData::getLootTables),
            ResourceLocation.CODEC.listOf().optionalFieldOf("lock_rarities", List.of()).forGetter(ChestSubprocessorData::getLockRarities),
            ResourceLocation.CODEC.listOf().optionalFieldOf("biomes_blacklist", List.of()).forGetter(ChestSubprocessorData::getBiomesBlacklist)
    ).apply(instance, ChestSubprocessorData::new));

    public ChestSubprocessorData(ResourceLocation type, ResourceLocation rarity, Double mimic,
                                 List<RarityWeight> lootTableRarities, List<ResourceLocation> lootTables,
                                 List<ResourceLocation> lockRarities, List<ResourceLocation> biomesBlacklist) {
        this.type = type;
        this.rarity = rarity;
        this.mimicProbability = mimic;
        this.lootTableRarities = lootTableRarities;
        this.lootTables = lootTables;
        this.lockRarities = lockRarities;
        this.biomesBlacklist = biomesBlacklist;
    }

    public ResourceLocation getRarity() {
        return rarity;
    }

    public ResourceLocation getType() {
        return type;
    }

    public Double getMimicProbability() {
        return mimicProbability;
    }

    public List<RarityWeight> getLootTableRarities() {
        return lootTableRarities;
    }

    public List<ResourceLocation> getLootTables() {
        return lootTables;
    }

    public List<ResourceLocation> getLockRarities() {
        return lockRarities;
    }

    public List<ResourceLocation> getBiomesBlacklist() {
        return biomesBlacklist;
    }
}
