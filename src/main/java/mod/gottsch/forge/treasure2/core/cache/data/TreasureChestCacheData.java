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
package mod.gottsch.forge.treasure2.core.cache.data;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * @author by Mark Gottschling on 9/2/2025
 */
public class TreasureChestCacheData {
    private static final String CHEST_NAME = "chestName";
    private static final String DIMENSION_NAME = "dimensionName";
    private static final String BIOME_NAME = "biomeName";
    private static final String DISCOVERED = "discovered";
    private static final String FEATURE_TYPE = "featureType";
    private static final String RARITY_NAME = "rarityName";
    private static final String COORDS = "coords";
    private static final String CHARTED_FROM = "chartedFrom";
    private static final String LOOT_TABLE_NAME = "lootTableName";

    // resource name of the chest
    private ResourceLocation chestName;
    // the dimension
    private ResourceLocation dimensionName;
    // the biome
    private ResourceLocation biomeName;
    // the loot table
    private ResourceLocation lootTableName;
    // the feature type that generated this chest
    private IFeatureType featureType;
    // the rarity of this chest
    private IRarity rarity;

    // has the chest been discovered
    private boolean discovered;
    // coords of chest that contains a treasure map to this chest
    private ICoords chartedFrom;
    // exact coords of the chest
    private ICoords coords;

    public CompoundTag save(CompoundTag tag) {
        if (getChestName() != null) {
            tag.putString(CHEST_NAME, getChestName().toString());
        }

        if (getChestName() != null) {
            tag.putString(DIMENSION_NAME, getDimensionName().toString());
        }

        if (getBiomeName() != null) {
            tag.putString(BIOME_NAME, getBiomeName().toString());
        }

        tag.putString(FEATURE_TYPE, getFeatureType().getName());
        tag.putString(RARITY_NAME, TreasureRarities.getKey(getRarity()).map(ResourceLocation::toString).orElse(TreasureRarities.COMMON.getId().toString()));

        if (getLootTableName() != null) {
            tag.putString(LOOT_TABLE_NAME, getLootTableName().toString());
        }

        tag.putBoolean(DISCOVERED, isDiscovered());

        if (isCharted()) {
            CompoundTag chartedFromTag = getChartedFrom().save(new CompoundTag());
            tag.put(CHARTED_FROM, chartedFromTag);
        }

        if (getCoords() != null) {
            tag.put(COORDS, getCoords().save(new CompoundTag()));
        }

        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.contains(CHEST_NAME)) {
            this.chestName = ModUtil.asLocation(tag.getString(CHEST_NAME));
        }

        if (tag.contains(DIMENSION_NAME)) {
            this.dimensionName = ModUtil.asLocation(tag.getString(DIMENSION_NAME));
        }

        if (tag.contains(BIOME_NAME)) {
            this.biomeName = ModUtil.asLocation(tag.getString(BIOME_NAME));
        }

        if (tag.contains(FEATURE_TYPE)) {
            this.featureType = TreasureApi.getFeatureType(tag.getString(FEATURE_TYPE).toUpperCase()).orElse(FeatureType.UNKNOWN);
        }
        if (tag.contains(RARITY_NAME)) {
            this.rarity = TreasureRarities.getRarityByName(ModUtil.asLocation(tag.getString(RARITY_NAME))).orElse(TreasureRarities.UNKNOWN.get());
        }

        if (tag.contains(LOOT_TABLE_NAME)) {
            this.lootTableName = ModUtil.asLocation(tag.getString(LOOT_TABLE_NAME));
        }

        if (tag.contains(DISCOVERED)) {
            this.discovered = tag.getBoolean(DISCOVERED);
        }

        if (tag.contains(CHARTED_FROM)) {
            this.chartedFrom = Coords.EMPTY.load(tag.getCompound(CHARTED_FROM));
        }

        if (tag.contains(COORDS)) {
            this.coords = Coords.EMPTY.load(tag.getCompound(COORDS));
        }
    }

    @Override
    public String toString() {
        return "TreasureChestCacheData{" +
                "biomeName=" + biomeName +
                ", chestName=" + chestName +
                ", dimensionName=" + dimensionName +
                ", lootTableName=" + lootTableName +
                ", featureType=" + featureType +
                ", rarity=" + rarity +
                ", discovered=" + discovered +
                ", chartedFrom=" + chartedFrom +
                ", coords=" + coords +
                '}';
    }

    public ResourceLocation getBiomeName() {
        return biomeName;
    }

    public void setBiomeName(ResourceLocation biomeName) {
        this.biomeName = biomeName;
    }

    public ResourceLocation getChestName() {
        return chestName;
    }

    public void setChestName(ResourceLocation chestName) {
        this.chestName = chestName;
    }

    public ICoords getCoords() {
        return coords;
    }

    public void setCoords(ICoords coords) {
        this.coords = coords;
    }

    public ResourceLocation getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(ResourceLocation dimensionName) {
        this.dimensionName = dimensionName;
    }

    public boolean isCharted() {
        return chartedFrom != null && chartedFrom != Coords.EMPTY;
    }

    public ICoords getChartedFrom() {
        return chartedFrom;
    }

    public void setChartedFrom(ICoords chartedFrom) {
        this.chartedFrom = chartedFrom;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public IFeatureType getFeatureType() {
        return featureType;
    }

    public void setFeatureType(IFeatureType featureType) {
        this.featureType = featureType;
    }

    public IRarity getRarity() {
        return rarity;
    }

    public void setRarity(IRarity rarity) {
        this.rarity = rarity;
    }

    public ResourceLocation getLootTableName() {
        return lootTableName;
    }

    public void setLootTableName(ResourceLocation lootTableName) {
        this.lootTableName = lootTableName;
    }
}
