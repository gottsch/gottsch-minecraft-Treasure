/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
 *
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.generator.witherTree;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.util.GeometryUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 12/26/2024
 */
public class WitherTreeGenerator  implements IWitherTreeGenerator<GeneratorResult<GeneratorData>> {

    public static final int MIN_RADIUS = 5;
    public static final int MAX_RADIUS = 15;
    private static final int MIN_SIZE = 9;

    @Override
    public Optional<GeneratorResult<GeneratorData>> generate(IWorldGenContext context, ICoords coords, ICoords spawnCoords) {
        /*
         * Setup
         */
        GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

        AABB maxArea = new AABB(spawnCoords.toPos());
        maxArea = maxArea.inflate(getMaxGenRadius(), 5, getMaxGenRadius());

        // validate coords
        BlockContext groundBlockContext = new BlockContext(context.level(), coords.down(1));
        BlockContext replaceBlockContext = new BlockContext(context.level(), coords);
        if (groundBlockContext.isReplaceable()
                || (!replaceBlockContext.isAir() && !replaceBlockContext.isReplaceable())) {
            return Optional.of(result.fail());
        }

        // TODO determine if entire clearing is within bounds, if so, no need to perform checks
        // TODO or determine how large clearing is so when building trunk, roots, branches the test is easier?
        // clear the area
        generateClearing(context, coords, maxArea);

        List<Direction> supportTrunkMatrix = buildSupportTrunkMap();

        // determine the size of the main trunk
        int  maxSize = RandomHelper.randomInt(context.random(), getMinSize(), Config.SERVER.witherTree.maxTrunkSize.get());

        for (int y = 0; y < maxSize; y++) {
            // add the trunk
            context.level().setBlock(coords.add(0, y, 0).toPos(), TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState(), 3);

            // add the branches/roots
            if (y == 0) {
                addRoot(context, coords, maxArea, supportTrunkMatrix);
            } else if (y == maxSize - 1) {
                addTop(context, coords, maxArea, y + 1, supportTrunkMatrix.get(context.random().nextInt(supportTrunkMatrix.size())));
            } else if (y > 3) {
                addBranch(context, coords, maxArea, y, maxSize, supportTrunkMatrix);
            }
        }

        // generate strangle vine around the tree
        for (int i = getMinStrangleVines(); i < getMaxStrangleVines(); i++) {
            ICoords strangleVineCoords = GeometryUtil.generateRandomCoordsByRadius(context.random(), 3, getClearingRadius());
            strangleVineCoords = strangleVineCoords.add(coords);
            addStrangleVines(context, strangleVineCoords, maxArea);
        }

        // update result
        result.getData().setSpawnCoords(coords);
        result.success();
        return Optional.of(result);
    }

    private List<Direction> buildSupportTrunkMap() {
        List<Direction> list = Lists.newArrayList();
        list.add(Direction.NORTH);
        list.add(Direction.EAST);
        list.add(Direction.SOUTH);
        list.add(Direction.WEST);
        return list;
    }

    public int getMinRadius() {
        return MIN_RADIUS;
    }

    public int getMaxRadius() {
        return MAX_RADIUS;
    }

    public int getMinSize() {
        return MIN_SIZE;
    }
}
