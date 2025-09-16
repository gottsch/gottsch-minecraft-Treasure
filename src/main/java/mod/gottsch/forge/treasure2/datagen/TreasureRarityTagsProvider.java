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
package mod.gottsch.forge.treasure2.datagen;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * @author by Mark Gottschling on 8/23/2025
 */
public class TreasureRarityTagsProvider extends TagsProvider<IRarityEntry> {

    // a tag key to represent our wishable common items.
    // This is the tag that the data-driven rarity system looks for.
//    public static final TagKey<IRarity> ALL_RARITIES = createRarityTag("all_rarities");

    public TreasureRarityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
    // pass the custom registry key and the mod ID.
    super(output, TreasureRarities.RARITIES_REGISTRY_KEY, provider, Treasure.MODID, existingFileHelper);
}

    // This method is where you actually define your tags.
    @Override
    protected void addTags(HolderLookup.Provider provider) {

        // core rarities
        tag(TreasureTags.Rarities.ALL_RARITIES)
                .add(TreasureRarities.COMMON.getKey())
                .add(TreasureRarities.UNCOMMON.getKey())
                .add(TreasureRarities.SCARCE.getKey())
                .add(TreasureRarities.RARE.getKey())
                .add(TreasureRarities.EPIC.getKey())
                .add(TreasureRarities.LEGENDARY.getKey())
                .add(TreasureRarities.MYTHICAL.getKey());

        tag(TreasureTags.Rarities.SURFACE_CHEST_RARITIES)
                .add(TreasureRarities.COMMON.getKey())
                .add(TreasureRarities.UNCOMMON.getKey())
                .add(TreasureRarities.SCARCE.getKey());
    }

    /**
     * Creates a TagKey for an item based on its rarity.
     * @param rarityName The name of the rarity (e.g., "common").
     * @return The TagKey for that rarity.
     */
//    private static TagKey<IRarity> createRarityTag(String rarityName) {
//        return TagKey.create(
//                TreasureRarities.RARITIES_REGISTRY_KEY,
//                new ResourceLocation(Treasure.MODID, rarityName)
//        );
//    }
}
