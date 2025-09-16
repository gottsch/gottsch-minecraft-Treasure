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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.random.RarityAdjustingWeightedCollection;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.ChestSubprocessorRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.IChestSubprocessor;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * NOTE this is loaded from RarityWeightDataHandler
 * @author by Mark Gottschling on 8/22/2025
 */
// TODO should merge this into TreasureRarities  ??? anything to do with rarities goes there?
public enum RarityWeightsManager {
    INSTANCE;

    public static final String RARITY_SELECTOR_TAG = "raritySelector";
    private static final String RARITY_COLLECTION_TAG = "rarityCollection";

    // collection of chest generators by rarity and type
    public static final Map<IFeatureType, RarityAdjustingWeightedCollection> RARITY_SELECTOR = new HashMap<>();

    /**
     * registered from RarityWeightDataHandler during the AddReloadListenerEvent. ie datapack loading
     * @param type
     * @param rarityWeight
     */
    public static void register(IFeatureType type, RarityWeight rarityWeight) {
        // convert resourceLocation to rarity
        TreasureRarities.getRarityByName(rarityWeight.getRarity())
                .ifPresent(rarity ->
                        RARITY_SELECTOR.computeIfAbsent(type, featureType -> new RarityAdjustingWeightedCollection())
                                .add(rarityWeight.getWeight(), rarity));
    }

    public static void clear() {
        RARITY_SELECTOR.clear();
    }

    /**
     * Saves the RARITY_SELECTOR as it has state stored in the LevelWeightedCollection.
     * @return
     */
    public static CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        RARITY_SELECTOR.forEach((featureType, collection) -> {
            CompoundTag featureTag = new CompoundTag();
            featureTag.putString("featureType", featureType.getValue());
            featureTag.put(RARITY_COLLECTION_TAG, collection.save());
            listTag.add(featureTag);
        });
        tag.put(RARITY_SELECTOR_TAG, listTag);
        return tag;
    }

    public static void load(CompoundTag tag) {
        if (tag.contains(RARITY_SELECTOR_TAG, Tag.TAG_LIST)) {
            tag.getList(RARITY_SELECTOR_TAG, Tag.TAG_COMPOUND).stream()
                    .map(selectorTag -> (CompoundTag) selectorTag)
                    .forEach(selector -> {
                        String featureTypeName = selector.getString("featureType").trim().toLowerCase();
                        IFeatureType featureType = FeatureType.getByValue(featureTypeName);

                        if (featureType != null && RARITY_SELECTOR.containsKey(featureType)) {
                            RARITY_SELECTOR.get(featureType).load(selector.getCompound(RARITY_COLLECTION_TAG));
                        }
                    });
         }
    }

    /**
     *
     * @param type
     * @return
     */
    public static RarityEntry getNextRarity(IFeatureType type) {
        return (RarityEntry) Optional.ofNullable(RARITY_SELECTOR.get(type))
                .map(RarityAdjustingWeightedCollection::next)
                .orElse(RarityEntry.NONE);
    }

    public static void adjustAllWeightsExcept(IFeatureType featureType, int increment, IRarityEntry rarity) {
        if (RARITY_SELECTOR.containsKey(featureType)) {
            RARITY_SELECTOR.put(featureType, new RarityAdjustingWeightedCollection(RARITY_SELECTOR.get(featureType).adjustExcept(increment, rarity)));
        }
    }
}
