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
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TEST class
@Deprecated
public class Dirt extends StructureProcessor {
    // a thread-safe set to track process runs
    private static final Set<BlockPos> singleProcessGuard = Collections.synchronizedSet(new HashSet<>());
    // a thread-safe set to track finalized runs.
    private static final Set<BlockPos> singleFinalizeGuard = Collections.synchronizedSet(new HashSet<>());


    public static final Codec<Dirt> CODEC = Codec.unit(Dirt::new);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {

        // check if the current block is an end rod (which turns into a flower)
        if (!current.state().is(Blocks.GRASS_BLOCK)) {
            return current;
        }
        if (singleProcessGuard.add(pos)) {
            Treasure.LOGGER.debug("dirt is replacing...");
            BlockState newState = Blocks.STONE.defaultBlockState();
            return new StructureTemplate.StructureBlockInfo(pos, newState, current.nbt());
        }
        return current;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor p_278247_, BlockPos pos, BlockPos p_277935_, List<StructureTemplate.StructureBlockInfo> p_278070_, List<StructureTemplate.StructureBlockInfo> p_278053_, StructurePlaceSettings p_277497_) {
        // Use the structure's origin as a unique key for this run.
        if (singleFinalizeGuard.add(pos)) {
            Treasure.LOGGER.debug("dirt is finalizing...");
        }
        return super.finalizeProcessing(p_278247_, pos, p_277935_, p_278070_, p_278053_, p_277497_);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.DIRT.get();
    }

}
