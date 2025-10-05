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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.loot.ILootTableTypes;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author by Mark Gottschling on 8/31/2025
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public enum LootTableRegistry {
    INSTANCE;

//    private static final Map<ILootTableTypes, List<ResourceLocation>> BY_TYPE = Maps.newHashMap();

    /*
     * Guava Table of LootTables by Top-Level(Type) ex chests | wishables | injects, IRarity, List<ResourceLocation>
     */
    private final static Table<ILootTableTypes, IRarity, List<ResourceLocation>> REGISTRY = HashBasedTable.create();

    public static synchronized void register(ILootTableTypes type, IRarity rarity, ResourceLocation lootTable) {
        if(!REGISTRY.contains(type, rarity)) {
            REGISTRY.put(type, rarity, new ArrayList<>());
        }
        REGISTRY.get(type, rarity).add(lootTable);
    }

    public static synchronized List<ResourceLocation> getLootTableIds(ILootTableTypes type, IRarity rarity) {
        return Optional.ofNullable(REGISTRY.get(type, rarity)).orElse(new ArrayList<>());
    }

    public static LootTable getLootTable(ServerLevel level, ResourceLocation lootTableName) {
        return level.getServer().getLootData().getLootTable(lootTableName);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        // The event gives you the MinecraftServer instance directly.
        MinecraftServer server = event.getServer();

        // Get the LootData object, which manages all loot tables.
        // Then, use getKeys() with LootDataType.TABLE to get the list of all loot tables.
        Collection<ResourceLocation> allLootTables = (Collection<ResourceLocation>) server.getLootData().getKeys(LootDataType.TABLE);

        Treasure.LOGGER.debug("found " + allLootTables.size() + " loot tables on server start.");

        // step 1: filter the initial list of loot tables once to create a smaller subset
        List<ResourceLocation> treasureLootTables = allLootTables.stream()
                .filter(location -> location.getNamespace().equals(Treasure.MODID))
                .toList();

        Treasure.LOGGER.debug("found " + treasureLootTables.size() + " treasure loot tables.");

        // step 2: iterate through associations and use the filtered subset
        RarityLootTableAssociationRegistry.getAssociations().forEach(association -> {
            treasureLootTables.stream()
                    .filter(lootTable -> Paths.get(lootTable.getPath()).getParent() != null)
                    .filter(lootTable -> {
                        ResourceLocation parentPath = new ResourceLocation(lootTable.getNamespace(), Paths.get(lootTable.getPath()).getParent().toString().replace("\\", "/"));
                        return association.lootTableId().equals(parentPath);
                    })
                    .forEach(matchingLootTable -> {
                        Optional<ILootTableTypes> type = TreasureLootTableTypes.getLootTableType(association.typeId());
                        Optional<IRarity> rarity = TreasureRarities.getRarityByName(association.rarityId());

                        type.ifPresent(lootTableType -> rarity.ifPresent(rarityEntry -> {
                            register(lootTableType, rarityEntry, matchingLootTable);
                        }));
                    });
        });
    }
}
