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
package mod.gottsch.forge.treasure2.core.structure;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author by Mark Gottschling on 9/9/2025
 */
public class StructureBlockSorter {
    /**
     * sorts a list of StructureBlockInfo objects in a natural construction order.
     * the order is: bottom to top (Y), north to south (Z), and west to east (X).
     * this is useful for ensuring that block placements occur in a predictable
     * order, which can be important for blocks that depend on block updates
     * from their neighbors.
     *
     * @param blocks the list of StructureBlockInfo objects to be sorted.
     * @return a new list containing the sorted blocks.
     */
    public static List<StructureTemplate.StructureBlockInfo> sortByNaturalOrder(Set<StructureTemplate.StructureBlockInfo> blocks) {
        // create a new list to avoid modifying the original list passed to the method.
        List<StructureTemplate.StructureBlockInfo> sortedBlocks = new ArrayList<>(blocks);

        // use a custom Comparator to define the sorting logic.
        sortedBlocks.sort(Comparator
                // First, sort by the Y-coordinate (bottom to top).
                .comparingInt((StructureTemplate.StructureBlockInfo block) -> block.pos().getY())
                // Second, sort by the Z-coordinate (north to south).
                // A higher Z-value is further south, so we use the Z value directly.
                .thenComparingInt(block -> block.pos().getZ())
                // Third, sort by the X-coordinate (west to east).
                .thenComparingInt(block -> block.pos().getX())
        );

        return sortedBlocks;
    }
}
