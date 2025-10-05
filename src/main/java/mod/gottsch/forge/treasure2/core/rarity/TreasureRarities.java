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

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author by Mark Gottschling on 8/25/2025
 */
public class TreasureRarities {
    // a unique identifier for your new registry.
    // this is what you will reference in data packs.
    public static final ResourceKey<Registry<IRarity>> RARITIES_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Treasure.MODID, "rarities"));

    // this is the DeferredRegister instance for your custom registry.
    public static final DeferredRegister<IRarity> RARITIES_DEFERRED =
            DeferredRegister.create(RARITIES_REGISTRY_KEY, Treasure.MODID);

    // this is the related Registry for the DeferredRegistry
    public static final Supplier<IForgeRegistry<IRarity>> RARITIES_REGISTRY_SUPPLIER = RARITIES_DEFERRED.makeRegistry(
            // must include hasTags() to enable tag support
            () -> new RegistryBuilder<IRarity>().hasTags()
    );

    // register all the rarities
    public static RegistryObject<IRarity> UNKNOWN = RARITIES_DEFERRED.register("unknown", () -> new Rarity("unknown", -1));
    public static RegistryObject<IRarity> COMMON = RARITIES_DEFERRED.register("common", () -> new Rarity("common", 0));
    public static RegistryObject<IRarity> UNCOMMON = RARITIES_DEFERRED.register("uncommon", () -> new Rarity("uncommon", 1));
    public static RegistryObject<IRarity> SCARCE = RARITIES_DEFERRED.register("scarce", () -> new Rarity("scarce", 2));
    public static RegistryObject<IRarity> RARE = RARITIES_DEFERRED.register("rare", () -> new Rarity("rare", 3));
    public static RegistryObject<IRarity> EPIC = RARITIES_DEFERRED.register("epic", () -> new Rarity("epic", 4));
    public static RegistryObject<IRarity> LEGENDARY = RARITIES_DEFERRED.register("legendary", () -> new Rarity("legendary", 5));
    public static RegistryObject<IRarity> MYTHICAL = RARITIES_DEFERRED.register("mythical", () -> new Rarity("mythical", 6));

    // special rarities
    public static RegistryObject<IRarity> SKULL = RARITIES_DEFERRED.register("skull", () -> new Rarity("skull", 2) {
        @Override
        public String getDisplayName() {
            return SCARCE.get().getDisplayName();
        }
    });
    public static RegistryObject<IRarity> GOLD_SKULL = RARITIES_DEFERRED.register("gold_skull", () -> new Rarity("gold_skull", 3) {
        @Override
        public String getDisplayName() {
            return RARE.get().getDisplayName();
        }
    });
    public static RegistryObject<IRarity> CRYSTAL_SKULL = RARITIES_DEFERRED.register("crystal_skull", () -> new Rarity("crystal_skull", 4) {
        @Override
        public String getDisplayName() {
            return EPIC.get().getDisplayName();
        }
    });
    public static RegistryObject<IRarity> CAULDRON = RARITIES_DEFERRED.register("cauldron", () -> new Rarity("cauldron", 4) {
        @Override
        public String getDisplayName() {
            return EPIC.get().getDisplayName();
        }
    });
    public static RegistryObject<IRarity> WITHER = RARITIES_DEFERRED.register("wither", () -> new Rarity("wither", 2) {
        @Override
        public String getDisplayName() {
            return SCARCE.get().getDisplayName();
        }
    });
    public static RegistryObject<IRarity> BONE = RARITIES_DEFERRED.register("bone", () -> new Rarity("bone", 2) {
        @Override
        public String getDisplayName() {
            return SCARCE.get().getDisplayName();
        }
    });

    static {
        // order rarities ???
    }
    public static void register(IEventBus eventBus) {
        RARITIES_DEFERRED.register(eventBus);
    }

    public static List<IRarity> getRarities() {
        return TreasureRarities.RARITIES_DEFERRED.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }

    public static Optional<IRarity> getRarityByName(ResourceLocation name) {
        return Optional.ofNullable(TreasureRarities.RARITIES_REGISTRY_SUPPLIER.get().getValue(name));
    }

    public static Optional<ResourceLocation> getKey(IRarity rarity) {
        return Optional.ofNullable(TreasureRarities.RARITIES_REGISTRY_SUPPLIER.get().getKey(rarity));
    }

    /**
     * a helper method to check if a Rarity object is in a given tag.
     * this is similar to how you would check if an Item is in an ItemTag.
     * @param object The Rarity RegistryObject to check.
     * @param tagKey The TagKey to check against.
     * @return True if the Rarity is in the tag, false otherwise.
     */
    public static boolean isInTag(RegistryObject<IRarity> object, TagKey<IRarity> tagKey) {
        // we get the registry from the supplier, then check the object's Holder.
        return RARITIES_REGISTRY_SUPPLIER.get().getHolder(object.getKey()).filter(holder -> holder.is(tagKey)).isPresent();
    }
}
