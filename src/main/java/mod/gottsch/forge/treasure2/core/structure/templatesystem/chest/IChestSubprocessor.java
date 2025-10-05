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

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.gottschcore.spatial.Rotate;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.lock.ILockSlot;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.Rarity;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeight;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.LootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.MimicRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * a chest subprocessor is similar to the legacy chest generator
 * expect that it deals with jigsaw processing ie blocks are not placed yet
 * and block entities do not exist yet as their owning blocks are not placed.
 * @author by Mark Gottschling on 8/22/2025
 */
public interface IChestSubprocessor {
    // feature type is populated by the calling processor
    public void setFeatureType(IFeatureType featureType);

    public IFeatureType getFeatureType();

    public void setData(ChestSubprocessorData data);
    public ChestSubprocessorData getData();

    default public StructureTemplate.StructureBlockInfo defaultChest(LevelReader levelReader, BlockState state, BlockPos pos, StructurePlaceSettings placeSettings) {
        Treasure.LOGGER.debug("attempting to use default chest.");
        CompoundTag tag = new CompoundTag();

        AbstractTreasureChestBlock chest = (AbstractTreasureChestBlock) TreasureBlocks.WOOD_CHEST.get();
        BlockState newState = addState(levelReader, state, pos, placeSettings, chest);

        tag.putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, BuiltInLootTables.SIMPLE_DUNGEON.toString());
        addSeal(tag, true);

        IRarity rarity = TreasureRarities.COMMON.get();
        Direction direction = placeSettings.getRotation().rotate(state.getValue(StandardChestBlock.FACING));
        // get the current state's facing direction
        Direction facing = state.getValue(StandardChestBlock.FACING);
        Heading originalHeading = Heading.fromDirection(facing);
        // get the direction the block is facing currently
        Heading heading = Heading.fromDirection(direction);
        Rotate rotate = originalHeading.getRotation(heading);
        // add locks
        addLocks(placeSettings.getRandom(pos), tag, chest.getLockLayout(), rarity, rotate);
        // add generation context
        addGenerationContext(tag, getFeatureType(), (Rarity) rarity);

        return new StructureTemplate.StructureBlockInfo(pos, newState, tag);
    }

    default public Optional<StructureTemplate.StructureBlockInfo> process(LevelReader levelReader, RandomSource random, BlockState state, BlockPos pos, Rotation rotation, IRarity rarity, ChestSubprocessorData data) {
        CompoundTag tag = new CompoundTag();

        // select a chest by the rarity
        AbstractTreasureChestBlock chest = selectChest(random, rarity);

        Treasure.LOGGER.debug("using chest -> {}", ModUtil.getName(chest));
            // move to own method
            FluidState fluidState = levelReader.getBlockState(pos).getFluidState() ;
            // have to manually rotate chests as they do not extend vanilla chests and aren't recognized for rotation.
            Direction newDirection = rotation.rotate(state.getValue(StandardChestBlock.FACING));
            BlockState newState = chest
                    .defaultBlockState()
                    .setValue(StandardChestBlock.FACING, newDirection)
                    .setValue(ITreasureChestBlock.DISCOVERED, false)
                    .setValue(AbstractTreasureChestBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);

        // rotate locks from NORTH to current facing
        Heading heading = Heading.fromDirection(newDirection);
        Rotate rotate = Heading.NORTH.getRotation(heading);
            // add the loot table
            addLootTable(random, tag, rarity);

            // seal the chest
            addSeal(tag, true);

             // add locks
            addLocks(random, tag, chest.getLockLayout(), rarity, rotate);

            // TODO for a lot of code, the concrete class Rarity is required, not the interface. possible to use interface throughout?
            // add generation context
            addGenerationContext(tag, getFeatureType(), (Rarity) rarity);

            addMimic(random, tag, chest, (Rarity)rarity);

            // NOTE no longer executing addGenEffects(). this would have to take place in the StructureProcessor.finalizeProcessing().
            // NOTE vanilla will not be able to rotate Treasure2 blocks with the structure since they do not share the same FACING property, so must manually rotate.

            return Optional.of(new StructureTemplate.StructureBlockInfo(pos, newState, tag));

    }

    /*
     *
     */
    default public AbstractTreasureChestBlock selectChest(final RandomSource random, final IRarity rarity) {
        List<AbstractTreasureChestBlock> validChests = getValidChests(rarity);

        if (validChests.isEmpty()) {
            Treasure.LOGGER.warn("unable to get treasure chest by rarity {}, no valid AbstractTreasureChestBlocks found", rarity);
            return (AbstractTreasureChestBlock) TreasureBlocks.WOOD_CHEST.get();
        }

        // select a random chest from the filtered list
        return validChests.get(random.nextInt(validChests.size()));
    }

    /*
     *
     */
    default public List<AbstractTreasureChestBlock> getValidChests(final IRarity rarity) {
        List<Block> chestBlocks = RarityTagAssociationRegistry.getChestBlocks(rarity);

        // filter and cast the list to ensure only valid chest blocks are considered
        return chestBlocks.stream()
                .filter(block -> block instanceof AbstractTreasureChestBlock)
                .map(block -> (AbstractTreasureChestBlock) block)
                .collect(Collectors.toList());
    }

    default public BlockState addState(LevelReader level, BlockState state, BlockPos pos, StructurePlaceSettings placeSettings, AbstractTreasureChestBlock chest) {
        FluidState fluidState = level.getBlockState(pos).getFluidState() ;
        Direction direction = placeSettings.getRotation().rotate(state.getValue(StandardChestBlock.FACING));
        return chest
                .defaultBlockState()
                .setValue(StandardChestBlock.FACING, direction)
                .setValue(ITreasureChestBlock.DISCOVERED, false)
                .setValue(AbstractTreasureChestBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    /*
     *
     */
    default public void addLootTable(RandomSource random, CompoundTag tag, IRarity defaultRarity) {
        // TODO move most of this to buildLootTableList
        // TODO this method should only select a table and update the tag

        IRarity rarity;

        // safely select a loot table rarities from the data association list, with a fallback
        List<RarityWeight> rarityWeights = getData().getLootTableRarities();

        // select a rarity depending on which rarity selection strategy is provided in the data
        if (!rarityWeights.isEmpty()) {
            // load rarity weights into a RarityWeightedCollection
            WeightedCollection<Integer, ResourceLocation> weightedCollection = new WeightedCollection<>();
            rarityWeights.forEach(rarityWeight -> {
                weightedCollection.add(rarityWeight.getWeight(), rarityWeight.getRarity());
            });

            // randomly select one of the rarities and solve to RarityEntry object
            rarity = TreasureRarities.getRarityByName(weightedCollection.next()).orElse(TreasureRarities.COMMON.get());
        } else {
            rarity = defaultRarity;
        }

        List<ResourceLocation> lootTables;
        if (!getData().getLootTables().isEmpty()) {
            lootTables = getData().getLootTables();
        }
        else {
            // get the loot table list from the registry based on type and rarity, with a fallback if the list is empty
            lootTables = LootTableRegistry.getLootTableIds(TreasureLootTableTypes.CHESTS.get(), rarity);
        }

        if (lootTables.isEmpty()) {
            Treasure.LOGGER.warn("unable to locate loot tables by rarity {}", rarity);
            tag.putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, BuiltInLootTables.SIMPLE_DUNGEON.toString());
            return;
        }

        // select a loot table from the non-empty list
        ResourceLocation lootTable = lootTables.get(random.nextInt(lootTables.size()));

        Treasure.LOGGER.debug("using loot table -> {}", lootTable);
        // update the tag with the selected loot table
        tag.putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, lootTable.toString());
    }

    /*
     *
     */
    default public void addSeal(CompoundTag tag, boolean isSealed) {
        Optional.ofNullable(tag).ifPresent(t -> {
            t.putBoolean(AbstractTreasureChestBlockEntity.SEALED_TAG, isSealed);
        });
    }

    default public void addGenerationContext(CompoundTag tag, IFeatureType featureType, Rarity rarity) {
        CompoundTag contextTag = new CompoundTag();
        contextTag.putString(AbstractTreasureChestBlockEntity.LOOT_RARITY_TAG, TreasureRarities.getKey(rarity).orElse(TreasureRarities.COMMON.getId()).toString());
        contextTag.putString(AbstractTreasureChestBlockEntity.FEATURE_TYPE_TAG, featureType.getValue().toLowerCase());
        tag.put(AbstractTreasureChestBlockEntity.GENERATION_CONTEXT_TAG, contextTag);
    }

    default public void addMimic(RandomSource random, CompoundTag tag, AbstractTreasureChestBlock chest, IRarity rarity) {
        // check against config if mimic should be used
        if (Config.SERVER.mobs.enableMimics.get() && random.nextDouble() < getData().getMimicProbability()) {
            MimicRegistry.getMimic(ModUtil.getName(chest))
                    .ifPresent(mimicName -> tag.putString(AbstractTreasureChestBlockEntity.MIMIC_TAG, mimicName.toString()));
        }
    }

    default public void addLocks(RandomSource random, CompoundTag tag, LockLayout layout, IRarity defaultRarity, Rotate rotate) {
        Treasure.LOGGER.debug("original lock layout -> {}", layout);
        Treasure.LOGGER.debug("default rarity {} for locks", defaultRarity);
        List<LockItem> locks = new ArrayList<>();

        // create a list of locks to select from
        if (!getData().getLockRarities().isEmpty()) {
            List<IRarity> rarities = getData().getLockRarities().stream()
                    .map(TreasureRarities::getRarityByName)
                    .flatMap(Optional::stream)
                    .toList();

            rarities.forEach(rarity -> {
                List<LockItem> lockItems = RarityTagAssociationRegistry.getLockItems(rarity).stream()
                        .filter(LockItem.class::isInstance)
                        .map(LockItem.class::cast)
                        .toList();
                locks.addAll(lockItems);
            });
        } else {
            locks.addAll(RarityTagAssociationRegistry.getLockItems(defaultRarity).stream()
                    .filter(LockItem.class::isInstance)
                    .map(LockItem.class::cast)
                    .toList());
        }

        // determine the number of locks to use
        int numLocks = randomizedNumberOfLocks(random, layout);

        if (numLocks > 0 && !locks.isEmpty()) {
            addLocks(tag, random, layout, locks, numLocks, rotate);
        }
    }

    default public void addLocks(CompoundTag tag, RandomSource random, LockLayout lockLayout, List<LockItem> locks, int numLocks, Rotate rotate) {
        if (locks == null || locks.isEmpty() || numLocks <= 0) {
            return; // no locks to add, so we can exit early.
        }

        ListTag locksTag = new ListTag();
        int availableLocksSize = locks.size();

        for (int i = 0; i < numLocks; i++) {
            LockItem lock = locks.get(random.nextInt(locks.size()));

            LockState lockState = new LockState();
            ILockSlot lockSlot = lockLayout.getSlots()[i];
            // skip rotate if NO_ROTATE as there is still processing withing the rotate() method
            if (rotate != Rotate.NO_ROTATE) {
                lockSlot = lockSlot.rotate(rotate);
            }

            lockState.setSlot(lockSlot);
            lockState.setLock(lock);

            locksTag.add(lockState.save(new CompoundTag()));
        }

        tag.put(AbstractTreasureChestBlockEntity.LOCK_STATES_TAG, locksTag);
    }

    default public List<LockItem> selectLocks(LockLayout layout, IRarity defaultRarity) {
        List<IRarity> rarities = getData().getLockRarities().stream()
                .map(TreasureRarities::getRarityByName)
                .flatMap(Optional::stream)
                .toList();

        IRarity selectedRarity = rarities.isEmpty() ? defaultRarity : rarities.get(0);

        return RarityTagAssociationRegistry.getLockItems(selectedRarity).stream()
                .filter(LockItem.class::isInstance)
                .map(LockItem.class::cast)
                .collect(Collectors.toList());
    }

    default public int randomizedNumberOfLocks(RandomSource random, LockLayout lockLayout) {
        // determine the number of locks to add

        // NOTE use RandomHelper instead of random.nextInt() since RandomHelper is inclusive and nextInt() is not.
        int numLocks = RandomHelper.randomInt(random, random.nextInt(100) < 20 ? 0 : 1, lockLayout.getMaxLocks());
        Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
        return numLocks;
    }
}
