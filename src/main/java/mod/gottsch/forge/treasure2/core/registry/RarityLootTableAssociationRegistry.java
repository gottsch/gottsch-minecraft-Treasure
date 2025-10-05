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
package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Lists;
import mod.gottsch.forge.treasure2.core.loot.ILootTableTypes;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityLootTableAssociation;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

/**
 * @author by Mark Gottschling on 8/31/2025
 */
public enum RarityLootTableAssociationRegistry {
    INSTANCE;

    //    private static final Map<IRarityEntry, RarityLootTableAssociation> MAP = Maps.newHashMap();
    private static final List<RarityLootTableAssociation> LIST = Lists.newArrayList();

    public static synchronized void clear() {
        LIST.clear();
    }

    public static synchronized void register(RarityLootTableAssociation data) {
        LIST.add(data);
    }

    public static synchronized List<RarityLootTableAssociation> getAssociations() {
        return List.copyOf(LIST);
    }

    /**
     * given the type and rarity returns the loot table id/name.
     * @param type
     * @param rarity
     * @return
     */
    public static synchronized Optional<ResourceLocation> getLootTableId(ILootTableTypes type, IRarity rarity) {
        return RarityLootTableAssociationRegistry.getAssociations().stream()
                // transform association's rarityId into a RarityEntry class and compare
                .filter(association -> TreasureRarities.getRarityByName(association.rarityId())
                        .filter(rarity::equals)
                        .isPresent())
                .filter( association -> TreasureLootTableTypes.getTypeByName(association.lootTableId())
                        .filter(type::equals)
                        .isPresent())
                .findFirst()
                .map(RarityLootTableAssociation::lootTableId);
    }
}