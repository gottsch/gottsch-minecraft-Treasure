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
package mod.gottsch.forge.treasure2.core.structure.templatesystem;


import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import mod.gottsch.forge.gottschcore.size.DoubleRange;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.ProximityMobSetSpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.StructureMobSetBlockEntity;
import mod.gottsch.forge.treasure2.core.config.MobSetConfiguration;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.DungeonHooks;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author by Mark Gottschling on 8/13/2025
 */
public class ProximityMobSetSpawnerProcessor extends StructureProcessor {

    public static final Codec<ProximityMobSetSpawnerProcessor> CODEC = Codec.unit(ProximityMobSetSpawnerProcessor::new);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {
try {
        // if the block is not a structure block, process it normally.
        if (!current.state().is(TreasureBlocks.STRUCTURE_MOB_SET.get())) {

            // handle LEGACY iron block replacement
            if (current.state().is(Blocks.IRON_BLOCK)) {
//                return new StructureTemplate.StructureBlockInfo(current.pos(), Blocks.GOLD_BLOCK.defaultBlockState(), null);
                return buildDefaultOneTimeSpawner(placementSettings.getRandom(current.pos()), current.pos(), new DoubleRange(1, 2), 5D);
            }
            // return the original block
            return current;
        }

        // process STRUCTURE_MOB_SET blocks.
        return Optional.ofNullable(current.nbt())
                .map(tag -> tag.getString(StructureMobSetBlockEntity.MOBSET))
                .filter(mobSet -> !mobSet.isEmpty())
                .map(mobSet -> {
                    Treasure.LOGGER.info("found mobSet -> {}", mobSet);
                    return GeneratorUtil.selectMobSet(new Random(), Lists.newArrayList(mobSet))
                            .map(selectedMobSet -> buildOneTimeMobSetSpawner(placementSettings.getRandom(current.pos()), current.pos(), selectedMobSet, current.nbt().getInt(StructureMobSetBlockEntity.PROXIMITY)))
                            .orElse(null); // return null if mob set isn't found TODO this could use the default OTS
                })
                .orElseGet(() -> {
                    // Fallback to mobSets if the single mobSet value is empty
                    return Optional.ofNullable(current.nbt())
                            .map(tag -> tag.getList("mobSets", Tag.TAG_STRING))
                            .map(listTag -> {
                                List<String> mobSets = listTag.stream().map(tag -> ((StringTag) tag).getAsString()).collect(Collectors.toList());
                                return GeneratorUtil.selectMobSet(new Random(), mobSets)
                                        .map(selectedMobSet -> buildOneTimeMobSetSpawner(placementSettings.getRandom(current.pos()), current.pos(), selectedMobSet, current.nbt().getInt(StructureMobSetBlockEntity.PROXIMITY)))
                                        .orElse(null); // return null if mob set isn't found
                            })
                            .orElseGet(() -> buildDefaultOneTimeSpawner(placementSettings.getRandom(current.pos()), current.pos(), new DoubleRange(1, 2), 10D));
                });
} catch(Exception e) {
    Treasure.LOGGER.error(e);
    throw e;
}
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.MOB_SET_PROCESSOR.get();
    }

    private StructureTemplate.StructureBlockInfo buildOneTimeMobSetSpawner(RandomSource random, BlockPos pos, MobSetConfiguration.MobSet mobSet, double proximity) {

        // create and populate nbt data of a ProximityMobSetSpawner
        CompoundTag tag = new CompoundTag();
        tag.putString(ProximityMobSetSpawnerBlockEntity.MOBSET_NAME, mobSet.getName());
        tag.putInt(ProximityMobSetSpawnerBlockEntity.MIN_MOBS, mobSet.getCount().getMin());
        tag.putInt(ProximityMobSetSpawnerBlockEntity.MAX_MOBS, mobSet.getCount().getMax());
        tag.putDouble(ProximityMobSetSpawnerBlockEntity.PROXIMITY_TAG, proximity);
        tag.putBoolean(ProximityMobSetSpawnerBlockEntity.IS_DEAD, false);

        return new StructureTemplate.StructureBlockInfo(pos, TreasureBlocks.PROXIMITY_MOBSET_SPAWNER.get().defaultBlockState(), tag);
    }

    private StructureTemplate.StructureBlockInfo buildDefaultOneTimeSpawner(RandomSource random, BlockPos pos, DoubleRange range, double proximity) {

        // select a vanilla mob, with a 20% chance to be a VINDICATOR.
        EntityType<?> entityType = random.nextInt(100) < 20
                ? EntityType.VINDICATOR
                : DungeonHooks.getRandomDungeonMob(random);

        Treasure.LOGGER.debug("Using mob -> {} for proximity spawner.", EntityType.getKey(entityType));

        // create and populate nbt data of a ProximitySpawner
        CompoundTag tag = new CompoundTag();
        tag.putString("mobName", EntityType.getKey(entityType).toString());
        tag.putInt("mobNumMin", range.getMinInt());
        tag.putInt("mobNumMax", range.getMaxInt());
        tag.putDouble(ProximityMobSetSpawnerBlockEntity.PROXIMITY_TAG, proximity);
        tag.putBoolean(ProximityMobSetSpawnerBlockEntity.IS_DEAD, false);

        return new StructureTemplate.StructureBlockInfo(pos, TreasureBlocks.PROXIMITY_SPAWNER.get().defaultBlockState(), tag);
    }
}
