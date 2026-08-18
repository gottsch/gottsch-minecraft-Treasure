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

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.cache.TreasureChestCache;
import mod.gottsch.forge.treasure2.core.cache.data.TreasureChestCacheData;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.Rarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeightsManager;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.ChestSubprocessorDataRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;

/**
 * The chest-generation pipeline, extracted so it has exactly one implementation.
 *
 * <p>This is the six-step sequence that used to live inside
 * {@code TreasureChestProcessor.buildTreasureChest}: map the feature type, draw a rarity, resolve
 * the subprocessor data, resolve the subprocessor, run it, then cache the result. That processor
 * still drives it, and {@code TreasureApi.generateChest} exposes the same call to other mods so a
 * foreign structure can place a real Treasure2 chest &mdash; with its rarity, chest block, locks,
 * mimic and cache entry &mdash; instead of a hand-written approximation of one.</p>
 *
 * <h2>Why a caller does not pass a chest {@code BlockState}</h2>
 * <p>The subprocessor reads exactly one thing off the state it is handed: {@code FACING}. A foreign
 * caller has a marker block of its own, not a Treasure2 chest, so this takes the {@link Direction}
 * the finished chest should face and synthesizes the state internally. The chest block itself is
 * selected by rarity inside the subprocessor and never comes from the caller.</p>
 *
 * <h2>Facing is final, not pre-rotation</h2>
 * <p>{@code facing} is the direction the placed chest ends up pointing. A caller inside a structure
 * processor applies its own {@code placeSettings.getRotation()} first. Internally this then runs at
 * {@link Rotation#NONE}, so the result is identical to the old path, which rotated an authored
 * facing on the way in.</p>
 *
 * @author Mark Gottschling on Aug 18, 2026
 */
public class ChestGeneration {

    private ChestGeneration() {}

    /**
     * Generate a chest, drawing the rarity from the feature type's weights.
     */
    public static StructureTemplate.StructureBlockInfo generate(LevelReader level, BlockPos pos,
            RandomSource random, Direction facing, IFeatureType featureType) {

        return generate(level, pos, random, facing, featureType, Optional.empty(), Optional.empty());
    }

    /**
     * Generate a chest.
     *
     * @param level      read-only level; used for the fluid state under the chest and the biome
     *                   recorded in the cache
     * @param pos        where the chest goes
     * @param random     the source every downstream selection draws from
     * @param facing     the direction the finished chest faces (see the class note)
     * @param featureType what generated this chest; anything other than {@link FeatureType#AQUATIC}
     *                   is treated as {@link FeatureType#TERRANEAN}, as it always has been
     * @param rarity     forces the rarity instead of drawing one. <strong>A forced rarity does not
     *                   feed the adaptive weights</strong> &mdash; see {@code adjustWeights} below
     * @param lootTable  overrides the table the subprocessor chose. It must be a table Treasure2 can
     *                   resolve when the chest is opened; this does not register anything
     * @return the block info to place. Never empty: a subprocessor that declines falls back to
     *         {@code defaultChest}, exactly as the processor path always did
     */
    public static StructureTemplate.StructureBlockInfo generate(LevelReader level, BlockPos pos,
            RandomSource random, Direction facing, IFeatureType featureType,
            Optional<IRarity> rarity, Optional<ResourceLocation> lootTable) {

        // 1. map feature type to either TERRANEAN or AQUATIC.
        IFeatureType processedFeatureType = featureType == FeatureType.AQUATIC ? featureType : FeatureType.TERRANEAN;

        // 2. get rarity and ensure it's a valid entry. A caller-supplied rarity skips the draw.
        //
        // The concrete Rarity filter is not defensiveness for its own sake: the subprocessor's
        // addGenerationContext casts IRarity to Rarity, so an interface implementation from a
        // foreign mod would ClassCastException several frames deeper, where the cause would be
        // unrecognisable. Reject it here, where the caller's own argument is still in view.
        Optional<IRarity> requested = rarity.filter(r -> {
            if (r instanceof Rarity) {
                return true;
            }
            Treasure.LOGGER.warn("supplied rarity -> {} is not a Treasure2 Rarity; drawing one instead", r);
            return false;
        });
        boolean drawn = requested.isEmpty();
        IRarity rarityEntry = requested.orElseGet(() -> resolveRarity(processedFeatureType));
        Treasure.LOGGER.debug("rarity -> {}", rarityEntry);

        // 3. get chest subprocessor data.
        ChestSubprocessorData data = ChestSubprocessorDataRegistry
                .getAssociation(processedFeatureType, rarityEntry)
                .orElseGet(() -> {
                    Treasure.LOGGER.warn("unable to locate chest subprocessor data for feature type -> {} and rarity -> {}, reverting to default subprocessor data.", processedFeatureType, rarityEntry.getName());
                    // no data pack entry for this (feature type, rarity) pair - build a safe standard default
                    // so nothing downstream NPEs on a null ChestSubprocessorData.
                    ResourceLocation rarityId = TreasureRarities.getKey(rarityEntry)
                            .orElseGet(() -> new ResourceLocation(Treasure.MODID, rarityEntry.getName()));
                    return new ChestSubprocessorData(TreasureChestSubprocessors.STANDARD.getId(), rarityId, 0.0,
                            List.of(), List.of(), List.of());
                });

        // 4. get a chest subprocessor.
        IChestSubprocessor subprocessor = TreasureChestSubprocessors.getChestSubprocessor(data.getType())
                .orElseGet(() -> {
                    Treasure.LOGGER.warn("unable to locate chest subprocessor for processor type -> {}, reverting to default subprocessor", data.getType());
                    return TreasureChestSubprocessors.STANDARD.get();
                });

        // 5. set properties and process the subprocessor to get the StructureBlockInfo.
        subprocessor.setFeatureType(processedFeatureType);
        subprocessor.setData(data);

        // The subprocessor takes a state only to read FACING off it, and place settings only for
        // rotation and the random source. Both are synthesized here so a caller holding a marker
        // block of its own never has to fabricate a Treasure2 chest state to ask for a chest.
        BlockState carrier = TreasureBlocks.WOOD_CHEST.get().defaultBlockState()
                .setValue(StandardChestBlock.FACING, facing);
        StructurePlaceSettings settings = new StructurePlaceSettings().setRandom(random);

        Optional<StructureTemplate.StructureBlockInfo> infoOptional =
                subprocessor.process(level, random, carrier, pos, Rotation.NONE, rarityEntry, data);

        // 6. if the block info is present, cache the data and return it. Otherwise fall back.
        StructureTemplate.StructureBlockInfo info = infoOptional.orElseGet(() -> {
            Treasure.LOGGER.warn("unable to generate StructureBlockInfo for processor type -> {}, reverting to default chest", data.getType());
            return subprocessor.defaultChest(level, carrier, pos, settings);
        });

        info = applyLootTable(info, lootTable);
        cache(level, info, featureType, rarityEntry);

        // The weights are a self-balancing draw: adjusting them for a rarity nobody drew would skew
        // every later draw toward the one the caller pinned.
        if (drawn) {
            RarityWeightsManager.adjustAllWeightsExcept(featureType, 1, rarityEntry);
        }
        return info;
    }

    /**
     * The rarity draw, with the same two filters and the same fallback the processor has always used.
     */
    private static IRarity resolveRarity(IFeatureType processedFeatureType) {
        return Optional.ofNullable(RarityWeightsManager.getNextRarity(processedFeatureType))
                .filter(r -> r != Rarity.NONE)
                .filter(r -> r != TreasureRarities.UNKNOWN.get())
                .map(r -> (IRarity) r)
                .orElseGet(() -> {
                    Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}, reverting to default rarity.", processedFeatureType);
                    return TreasureRarities.COMMON.get();
                });
    }

    /**
     * Overwrite the subprocessor's chosen loot table, when the caller pinned one.
     *
     * <p>Everything else the subprocessor decided &mdash; chest block, locks, seal, mimic &mdash;
     * stands. Only the table changes, because that is the one part of a chest a foreign feature has
     * a legitimate opinion about.</p>
     */
    private static StructureTemplate.StructureBlockInfo applyLootTable(
            StructureTemplate.StructureBlockInfo info, Optional<ResourceLocation> lootTable) {

        if (lootTable.isEmpty() || info.nbt() == null) {
            return info;
        }
        info.nbt().putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, lootTable.get().toString());
        return info;
    }

    /**
     * Record the chest so Treasure2's own systems know it exists.
     *
     * <p>Skipping this is what makes a hand-written chest wrong: it opens, but it is absent from the
     * discovery cache and from everything that reads it.</p>
     */
    private static void cache(LevelReader level, StructureTemplate.StructureBlockInfo info,
            IFeatureType featureType, IRarity rarity) {

        TreasureChestCacheData chestSpawn = new TreasureChestCacheData();
        chestSpawn.setChestName(ModUtil.getName(info.state().getBlock()));
        chestSpawn.setCoords(Coords.of(info.pos()));
        chestSpawn.setBiomeName(ModUtil.getName(level.getBiome(info.pos())));
        // NOTE the original feature type to describe what feature spawned the chest
        chestSpawn.setFeatureType(featureType);
        chestSpawn.setRarity(rarity);
        chestSpawn.setDiscovered(false);
        TreasureChestCache.cache(chestSpawn);
        Treasure.LOGGER.debug("caching chest at pos -> {}", info.pos());
    }

    /**
     * Fill in the dimension on a cached chest, once a level that knows its dimension is available.
     *
     * <p>{@code processBlock} runs against a {@link LevelReader}, which cannot name its dimension, so
     * a chest is cached without one and completed here. A foreign structure processor must call this
     * from its own {@code finalizeProcessing} or its chests stay dimensionless in the cache.</p>
     */
    public static void finalizeChest(ServerLevelAccessor levelAccessor, BlockPos pos) {
        TreasureChestCache.getCache().stream()
                .filter(chest -> chest.getCoords().equals(Coords.of(pos)))
                .filter(chest -> chest.getBiomeName().equals(ModUtil.getName(levelAccessor.getBiome(pos))))
                .filter(chest -> chest.getDimensionName() == null)
                .findFirst().ifPresent(chest -> {
                    chest.setDimensionName(levelAccessor.dimensionType().effectsLocation());
                    // mark the persistence data as dirty
                    TreasureSavedData.get(levelAccessor.getLevel()).setDirty();
                });
    }

    /** Whether a block is one of Treasure2's chests, for callers that cannot see the interface. */
    public static boolean isTreasureChest(BlockState state) {
        return state.getBlock() instanceof ITreasureChestBlock;
    }
}
