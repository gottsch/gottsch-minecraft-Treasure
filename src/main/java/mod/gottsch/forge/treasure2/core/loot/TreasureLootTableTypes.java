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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.registry.LootTableRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author by Mark Gottschling on 8/27/2025
 */
public class TreasureLootTableTypes {
    // registry identifier
    public static final ResourceKey<Registry<ILootTableTypes>> LOOT_TABLE_TYPES_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Treasure.MODID, "loot_table_types"));

    // deferred registry
    public static final DeferredRegister<ILootTableTypes> LOOT_TABLE_TYPES_DEFERRED =
            DeferredRegister.create(LOOT_TABLE_TYPES_REGISTRY_KEY, Treasure.MODID);

    // this is the related Registry for the DeferredRegistry
    public static final Supplier<IForgeRegistry<ILootTableTypes>> LOOT_TABLE_TYPES_REGISTRY_SUPPLIER = LOOT_TABLE_TYPES_DEFERRED.makeRegistry(
            // must include hasTags() to enable tag support
            () -> new RegistryBuilder<ILootTableTypes>().hasTags()
    );

    public static RegistryObject<ILootTableTypes> UNKNOWN = LOOT_TABLE_TYPES_DEFERRED.register("unknown", () -> new LootTableTypes("unknown"));
    public static RegistryObject<ILootTableTypes> CHESTS = LOOT_TABLE_TYPES_DEFERRED.register("chests", () -> new LootTableTypes("chests"));
    public static RegistryObject<ILootTableTypes> WISHABLES = LOOT_TABLE_TYPES_DEFERRED.register("wishables", () -> new LootTableTypes("wishables"));
    public static RegistryObject<ILootTableTypes> INJECTS = LOOT_TABLE_TYPES_DEFERRED.register("injects", () -> new LootTableTypes("injects"));

    /*
     *
     */
    public static void register(IEventBus eventBus) {
        LOOT_TABLE_TYPES_DEFERRED.register(eventBus);
    }

    public static Optional<ILootTableTypes> getLootTableType(ResourceLocation name) {
        return Optional.ofNullable(TreasureLootTableTypes.LOOT_TABLE_TYPES_REGISTRY_SUPPLIER.get().getValue(name));
    }

    // NOTE use this only for interim, so the mod compiles until complete
    @Deprecated
    public static List<ResourceLocation> getLootTables(ILootTableTypes type, IRarityEntry rarity) {
        List<ResourceLocation> lootTables = LootTableRegistry.getLootTableIds(type, rarity);
        return lootTables;

//        if (lootTables.isEmpty()) {
//            // TODO add warning
//            tag.putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, BuiltInLootTables.SIMPLE_DUNGEON.toString());
//            return;
//        }

        // TODO this will have to be replaced when Rarity is removed
        // NOTE ADAPTER
//        return TreasureLootTableRegistry.getLootTableByRarity(LootTableTypeAdapter.get(type), RarityAdapter.get(rarity));


    }

    public static Optional<ILootTableTypes> getTypeByName(ResourceLocation name) {
        return Optional.ofNullable(TreasureLootTableTypes.LOOT_TABLE_TYPES_REGISTRY_SUPPLIER.get().getValue(name));
    }

    public static Optional<ResourceLocation> getKey(LootTableTypes type) {
        return Optional.ofNullable(TreasureLootTableTypes.LOOT_TABLE_TYPES_REGISTRY_SUPPLIER.get().getKey(type));
    }
}
