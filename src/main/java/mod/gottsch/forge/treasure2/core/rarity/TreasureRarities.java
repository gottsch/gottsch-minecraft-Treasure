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

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author by Mark Gottschling on 8/25/2025
 */
public class TreasureRarities {
    // a unique identifier for your new registry.
    // this is what you will reference in data packs.
    public static final ResourceKey<Registry<IRarityEntry>> RARITIES_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Treasure.MODID, "rarities"));

    // this is the DeferredRegister instance for your custom registry.
    public static final DeferredRegister<IRarityEntry> RARITIES_DEFERRED =
            DeferredRegister.create(RARITIES_REGISTRY_KEY, Treasure.MODID);

    // this is the related Registry for the DeferredRegistry
    public static final Supplier<IForgeRegistry<IRarityEntry>> RARITIES_REGISTRY_SUPPLIER = RARITIES_DEFERRED.makeRegistry(
            // must include hasTags() to enable tag support
            () -> new RegistryBuilder<IRarityEntry>().hasTags()
    );

    // register all the rarities
    public static RegistryObject<IRarityEntry> UNKNOWN = RARITIES_DEFERRED.register("unknown", () -> new RarityEntry("unknown", -1));
    public static RegistryObject<IRarityEntry> COMMON = RARITIES_DEFERRED.register("common", () -> new RarityEntry("common", 0));
    public static RegistryObject<IRarityEntry> UNCOMMON = RARITIES_DEFERRED.register("uncommon", () -> new RarityEntry("uncommon", 1));
    public static RegistryObject<IRarityEntry> SCARCE = RARITIES_DEFERRED.register("scarce", () -> new RarityEntry("scarce", 2));
    public static RegistryObject<IRarityEntry> RARE = RARITIES_DEFERRED.register("rare", () -> new RarityEntry("rare", 3));
    public static RegistryObject<IRarityEntry> EPIC = RARITIES_DEFERRED.register("epic", () -> new RarityEntry("epic", 4));
    public static RegistryObject<IRarityEntry> LEGENDARY = RARITIES_DEFERRED.register("legendary", () -> new RarityEntry("legendary", 5));
    public static RegistryObject<IRarityEntry> MYTHICAL = RARITIES_DEFERRED.register("mythical", () -> new RarityEntry("mythical", 6));

    // special rarities
    public static RegistryObject<IRarityEntry> SKULL = RARITIES_DEFERRED.register("skull", () -> new RarityEntry("skull", 2));
    public static RegistryObject<IRarityEntry> GOLD_SKULL = RARITIES_DEFERRED.register("gold_skull", () -> new RarityEntry("gold_skull", 3));
    public static RegistryObject<IRarityEntry> CRYSTAL_SKULL = RARITIES_DEFERRED.register("crystal_skull", () -> new RarityEntry("crystal_skull", 4));
    public static RegistryObject<IRarityEntry> CAULDRON = RARITIES_DEFERRED.register("cauldron", () -> new RarityEntry("cauldron", 4));
    public static RegistryObject<IRarityEntry> WITHER = RARITIES_DEFERRED.register("wither", () -> new RarityEntry("wither", 2));

    static {
        // order rarities ???
    }
    public static void register(IEventBus eventBus) {
        RARITIES_DEFERRED.register(eventBus);
    }

    public static Optional<IRarityEntry> getRarityByName(ResourceLocation name) {
        return Optional.ofNullable(TreasureRarities.RARITIES_REGISTRY_SUPPLIER.get().getValue(name));
    }

    public static Optional<ResourceLocation> getKey(IRarityEntry rarity) {
        return Optional.ofNullable(TreasureRarities.RARITIES_REGISTRY_SUPPLIER.get().getKey(rarity));
    }

    /**
     * a helper method to check if a Rarity object is in a given tag.
     * this is similar to how you would check if an Item is in an ItemTag.
     * @param object The Rarity RegistryObject to check.
     * @param tagKey The TagKey to check against.
     * @return True if the Rarity is in the tag, false otherwise.
     */
    public static boolean isInTag(RegistryObject<IRarityEntry> object, TagKey<IRarityEntry> tagKey) {
        // we get the registry from the supplier, then check the object's Holder.
        return RARITIES_REGISTRY_SUPPLIER.get().getHolder(object.getKey()).filter(holder -> holder.is(tagKey)).isPresent();
    }
}
