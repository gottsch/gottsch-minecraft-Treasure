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
package mod.gottsch.forge.treasure2.core.generator;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.StandardChestGenerator;
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
 * @author by Mark Gottschling on 9/3/2025
 */
@Deprecated
public class TreasureChestGenerators {
    // registry identifier
    public static final ResourceKey<Registry<IChestGenerator>> CHEST_GENERATORS_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Treasure.MODID, "chest_generators"));

    // deferred registry
    public static final DeferredRegister<IChestGenerator> CHEST_GENERATORS_DEFERRED =
            DeferredRegister.create(CHEST_GENERATORS_REGISTRY_KEY, Treasure.MODID);

    // this is the related Registry for the DeferredRegistry
    public static final Supplier<IForgeRegistry<IChestGenerator>> CHEST_GENERATORS_REGISTRY_SUPPLIER = CHEST_GENERATORS_DEFERRED.makeRegistry(
            // must include hasTags() to enable tag support
            () -> new RegistryBuilder<IChestGenerator>().hasTags()
    );

    // register all the chest subprocessors
    public static RegistryObject<IChestGenerator> STANDARD = CHEST_GENERATORS_DEFERRED.register("standard", () -> new StandardChestGenerator("standard"));

    /*
     *
     */
    public static void register(IEventBus eventBus) {
        CHEST_GENERATORS_DEFERRED.register(eventBus);
    }

    public static Optional<IChestGenerator> get(ResourceLocation name) {
        return Optional.ofNullable(TreasureChestGenerators.CHEST_GENERATORS_REGISTRY_SUPPLIER.get().getValue(name));
    }
}
