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

import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;

import java.util.Map;

/**
 * NOTE this class will be removed after all ILootTableType are replaced with ILootTableTypes
 * @author by Mark Gottschling on 9/1/2025
 */
public enum LootTableTypeAdapter {
    INSTANCE;

    // TEMP
    /// ///////////////
    private final static Map<ILootTableTypes, ILootTableType> ADAPTER = Maps.newHashMap();

    static {
        ADAPTER.put(TreasureLootTableTypes.CHESTS.get(), LootTableType.CHESTS);
        ADAPTER.put(TreasureLootTableTypes.INJECTS.get(), LootTableType.INJECTS);
        ADAPTER.put(TreasureLootTableTypes.WISHABLES.get(), LootTableType.WISHABLES);
    }
    /// /////////////////

    public static ILootTableType get(ILootTableTypes lootTableTypes) {
        return ADAPTER.get(lootTableTypes);
    }
}
