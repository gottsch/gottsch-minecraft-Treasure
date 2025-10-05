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
package mod.gottsch.forge.treasure2.core.structure.templatesystem.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * a thread-safe cache for storing block data during structure generation.
 * the key is the absolute BlockPos of the entire structure's origin, which
 * is available to both the processors and the afterPlaced() method.
 * @author by Mark Gottschling on 9/9/2025
 */
public class BlockDataCache {
    private static final ConcurrentMap<BlockPos, Set<StructureTemplate.StructureBlockInfo>> originalBlocks = new ConcurrentHashMap<>();
    private static final ConcurrentMap<BlockPos, Set<StructureTemplate.StructureBlockInfo>> processedBlocks = new ConcurrentHashMap<>();

    public static void putOriginalBlocks(BlockPos pos, Set<StructureTemplate.StructureBlockInfo> blocks) {
        originalBlocks.put(pos, blocks);
    }

    public static Set<StructureTemplate.StructureBlockInfo> getOriginalBlocks(BlockPos pos) {
        return Optional.ofNullable(originalBlocks.get(pos)).orElseGet(HashSet::new);
    }

    public static boolean hasOriginalBlocks(BlockPos pos) {
        return originalBlocks.containsKey(pos);
    }

    public static void putProcessedBlocks(BlockPos pos, Set<StructureTemplate.StructureBlockInfo> blocks) {
        processedBlocks.put(pos, blocks);
    }

    public static Set<StructureTemplate.StructureBlockInfo> getProcessedBlocks(BlockPos pos) {
        return Optional.ofNullable(processedBlocks.get(pos)).orElseGet(HashSet::new);
    }

    public static boolean hasProcessedBlocks(BlockPos pos) {
        return processedBlocks.containsKey(pos);
    }

    public static void clear(BlockPos pos) {
        originalBlocks.remove(pos);
        processedBlocks.remove(pos);
    }
}
