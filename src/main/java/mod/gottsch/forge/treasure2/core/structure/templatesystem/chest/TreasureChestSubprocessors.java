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
package mod.gottsch.forge.treasure2.core.structure.templatesystem.chest;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author by Mark Gottschling on 8/27/2025
 */
public class TreasureChestSubprocessors {
    // registry identifier
    public static final ResourceKey<Registry<IChestSubprocessor>> CHEST_SUBPROCESSORS_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Treasure.MODID, "chest_subprocessors"));

    // deferred registry
    public static final DeferredRegister<IChestSubprocessor> CHEST_SUBPROCESSORS_DEFERRED =
            DeferredRegister.create(CHEST_SUBPROCESSORS_REGISTRY_KEY, Treasure.MODID);

    // this is the related Registry for the DeferredRegistry
    public static final Supplier<IForgeRegistry<IChestSubprocessor>> CHEST_SUBPROCESSORS_REGISTRY_SUPPLIER = CHEST_SUBPROCESSORS_DEFERRED.makeRegistry(
            // must include hasTags() to enable tag support
            () -> new RegistryBuilder<IChestSubprocessor>().hasTags()
    );

    // register all the chest subprocessors
    public static RegistryObject<IChestSubprocessor> STANDARD = CHEST_SUBPROCESSORS_DEFERRED.register("standard", StandardChestSubprocessor::new);
    public static RegistryObject<IChestSubprocessor> HIGH_TIER_RARITY = CHEST_SUBPROCESSORS_DEFERRED.register("high_tier_rarity", HighTierRarityChestSubprocessor::new);
    public static RegistryObject<IChestSubprocessor> BONE_CHEST = CHEST_SUBPROCESSORS_DEFERRED.register("bone_chest", BoneChestSubprocessor::new);
    public static RegistryObject<IChestSubprocessor> WITHER_CHEST = CHEST_SUBPROCESSORS_DEFERRED.register("wither_chest", WitherChestSubprocessor::new);
    public static RegistryObject<IChestSubprocessor> LEGENDARY_CHEST = CHEST_SUBPROCESSORS_DEFERRED.register("legendary_chest", LegendaryChestSubprocessor::new);
    public static RegistryObject<IChestSubprocessor> MYTHICAL_CHEST = CHEST_SUBPROCESSORS_DEFERRED.register("mythical_chest", MythicalChestSubprocessor::new);

    /*
     *
     */
    public static void register(IEventBus eventBus) {
        CHEST_SUBPROCESSORS_DEFERRED.register(eventBus);
    }

    public static Optional<IChestSubprocessor> getChestSubprocessor(ResourceLocation name) {
        return Optional.ofNullable(TreasureChestSubprocessors.CHEST_SUBPROCESSORS_REGISTRY_SUPPLIER.get().getValue(name));
    }
}
