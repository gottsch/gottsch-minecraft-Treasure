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


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.registry.MimicRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Optional;

/**
 *
 * @author by Mark Gottschling on 8/27/2025
 */
public class VanillaChestProcessor extends ModProcessor {

    protected Optional<ResourceLocation> lootTable;

    public static final Codec<VanillaChestProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(VanillaChestProcessor::getLootTable)
    ).apply(instance, VanillaChestProcessor::new));

    public VanillaChestProcessor(Optional<ResourceLocation> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.VANILLA_CHEST_PROCESSOR.get();
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {

        // if the block is not a structure block, process it normally.
        if (current.state().is(Blocks.CHEST)) {

            ResourceLocation lootTable = getLootTable().orElse(BuiltInLootTables.SIMPLE_DUNGEON);
            return buildVanillaChest(placementSettings.getRandom(current.pos()), current.state(), current.pos(), lootTable);

        } else if (current.state().is(Blocks.ENDER_CHEST)) {
            // TODO what about them?
        }

        return current;
    }

    public StructureTemplate.StructureBlockInfo buildVanillaChest(RandomSource random, BlockState state, BlockPos pos, ResourceLocation lootTable) {

        BlockState newState;
        CompoundTag tag = new CompoundTag();

        // TODO change to use association data
        // add mimic if any
        if (Config.SERVER.mobs.enableMimics.get() && RandomHelper.checkProbability(random, Config.SERVER.mobs.mimicProbability.get())) {
            Direction direction = state.getValue(ChestBlock.FACING).getOpposite();

            // switch to a Treasure2 Vanilla Chest
            newState = TreasureBlocks.VANILLA_CHEST.get().defaultBlockState().setValue(StandardChestBlock.FACING, direction);

            // update nbt to indicate a mimic
            MimicRegistry.getMimic(ModUtil.getName(TreasureBlocks.VANILLA_CHEST.get()))
                    .ifPresent(mimicName -> tag.putString(AbstractTreasureChestBlockEntity.MIMIC_TAG, mimicName.toString()));

            // update mimic's loot table
            tag.putString(AbstractTreasureChestBlockEntity.LOOT_TABLE_TAG, lootTable.toString());

        } else {
            // place vanilla chest
            newState = state;

            // update vanilla chest's loot table.
            // NOTE the tag is different than a Treasure/Mimic loot table.
            tag.putString(RandomizableContainerBlockEntity.LOOT_TABLE_TAG, lootTable.toString());
        }

        return new StructureTemplate.StructureBlockInfo(pos, newState, tag);
    }

    public Optional<ResourceLocation> getLootTable() {
        return lootTable;
    }
}
