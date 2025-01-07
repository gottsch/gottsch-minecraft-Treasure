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

import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.ITreasureBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.WitherwoodGolem;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.util.GeometryUtil;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 12/26/2024
 */
public class GreatWitherTreeGenerator implements IWitherTreeGenerator<GeneratorResult<GeneratorData>> {

    private static final int NW = 0;
    private static final int N = 1;
    private static final int NE = 2;
    private static final int W = 3;
    private static final int CORE = 4;
    private static final int E = 5;
    private static final int SW = 6;
    private static final int S = 7;
    private static final int SE = 8;

    private static final int MIN_SIZE = 11;

    @Override
    public Optional<GeneratorResult<GeneratorData>> generate(IWorldGenContext context, ICoords coords, ICoords spawnCoords) {
        // NOTE for the Great Wither Tree, coords == spawnCoords

        /*
         * Setup
         */
        GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

        // setup a max AABB centered around the spawn coords
        AABB maxArea = new AABB(spawnCoords.toPos());
        maxArea = maxArea.inflate(getMaxGenRadius(), 5, getMaxGenRadius());

        // clear the area
        generateClearing(context, coords, maxArea);

        Map<Integer, List<Direction>> trunkMatrix = buildTrunkMap();
        List<Direction> trunkTopMatrix = buildTrunkTopMap();
        ICoords[] trunkCoords = buildTrunkCoords(coords);

        // determine the max size of any trunk piece
        int minSize = getMinSize();
        int maxSize = RandomHelper.randomInt(context.random(),
                Math.min(minSize, Config.SERVER.witherTree.maxTrunkSize.get() + 2),
                Math.max(minSize, Config.SERVER.witherTree.maxTrunkSize.get() + 2));

        // randomize the size of the working trunk piece
        int size = RandomHelper.randomInt(context.random(), Math.min(minSize, maxSize), Math.max(minSize, maxSize));
        int tallestSize = 0;

        for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {
            // check under the trunk
            if (context.level().getBlockState(trunkCoords[trunkIndex].down(1).toPos()).canBeReplaced()) {
                context.level().setBlock(trunkCoords[trunkIndex].down(1).toPos(), Blocks.DIRT.defaultBlockState(), 3);
            }

            // select the log
            BlockState trunkBlockState;
            trunkBlockState = (trunkIndex == CORE)
                    ? trunkBlockState = TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState()
                    : TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState();

            for (int y = 0; y < size; y++) {
                // add the trunk
                context.level().setBlock(trunkCoords[trunkIndex].up(y).toPos(),	trunkBlockState, 3);

                // add the decorations (branches, roots, top)
                if (trunkIndex != CORE) {
                    if (y == 0) {
                        addRoot(context, trunkCoords[trunkIndex], maxArea, trunkMatrix.get(trunkIndex));
                    } else if (y == size - 1 && trunkIndex % 2 == 0) {
                        addTop(context, trunkCoords[trunkIndex], maxArea, y + 1, trunkTopMatrix.get(trunkIndex));
                    } else if (y >= 3) {
                        addBranch(context, trunkCoords[trunkIndex], maxArea, y, size, trunkMatrix.get(trunkIndex));
                    }
                }
            }

            // store the tallest size.
            tallestSize = Math.max(size, tallestSize);

            // set the new size
            if (trunkIndex == CORE) {
                size = RandomHelper.randomInt(context.random(), Math.min(getMinSize(), tallestSize - 1), Math.max(getMinSize(), tallestSize - 1));
            } else {
                if (maxSize - minSize <= 2) {
                    minSize -= trunkIndex % 2;
                    maxSize -= trunkIndex % 2;
                }
                size = RandomHelper.randomInt(context.random(), Math.min(minSize, maxSize), Math.max(minSize,maxSize));
            }
        }

        // generate strangle vine around the tree
        for (int i = getMinStrangleVines(); i < getMaxStrangleVines(); i++) {
            ICoords strangleVineCoords = GeometryUtil.generateRandomCoordsByRadius(context.random(), 3, getClearingRadius());
            strangleVineCoords = strangleVineCoords.add(coords);
            addStrangleVines(context, strangleVineCoords, maxArea);
        }

        Treasure.LOGGER.debug("attempting to spawn golem -> {}", spawnCoords.south(2));
        WitherwoodGolem mob = (TreasureEntities.WITHERWOOD_GOLEM_ENTITY_TYPE.get()).create((Level) context.level());
        mob.restrictTo(spawnCoords.toPos(), 24);
        mob.setHomePos(spawnCoords.toPos());
        WitherwoodGolem golem = (WitherwoodGolem) ModUtil.SpawnEntityHelper.spawn((ServerLevel) context.level(), context.random(), TreasureEntities.WITHERWOOD_GOLEM_ENTITY_TYPE.get(), mob, spawnCoords.south(2));
        if (golem != null) {
            Treasure.LOGGER.debug("golem spawn successful -> {}", golem.blockPosition().toShortString());
        }

        // update result
        result.getData().setSpawnCoords(coords);
        result.success();
        return Optional.of(result);
    }

    private Map<Integer, List<Direction>> buildTrunkMap() {
        Map<Integer, List<Direction>> map = Maps.newHashMap();

        map.put(NW, List.of(Direction.NORTH, Direction.WEST));
        map.put(N, List.of(Direction.NORTH));
        map.put(NE, List.of(Direction.NORTH, Direction.EAST));

        map.put(W, List.of(Direction.WEST));
        map.put(CORE, new ArrayList<>());
        map.put(E, List.of(Direction.EAST));

        map.put(SW, List.of(Direction.SOUTH, Direction.WEST));
        map.put(S, List.of(Direction.SOUTH));
        map.put(SE, List.of(Direction.SOUTH, Direction.EAST));

        return map;
    }

    private List<Direction> buildTrunkTopMap() {
        List<Direction> list = Lists.newArrayList();
        list.add(NW, Direction.EAST);
        list.add(N, Direction.SOUTH);
        list.add(NE, Direction.SOUTH);
        list.add(W, Direction.EAST);
        list.add(CORE, Direction.NORTH);
        list.add(E, Direction.WEST);
        list.add(SW, Direction.NORTH);
        list.add(S, Direction.NORTH);
        list.add(SE, Direction.WEST);
        return list;
    }

    private ICoords[] buildTrunkCoords(ICoords coords) {
        ICoords[] trunkCoords = new Coords[9];
//        trunkCoords[NW] = coords;
//        trunkCoords[N] = coords.east(1);
//        trunkCoords[NE] = coords.east(2);
//        trunkCoords[W] = coords.south(1);
//        trunkCoords[CORE] = coords.add(1, 0, 1);
//        trunkCoords[E] = coords.add(2, 0, 1);
//        trunkCoords[SW] = coords.south(2);
//        trunkCoords[S] = coords.add(1, 0, 2);
//        trunkCoords[SE] = coords.add(2, 0, 2);
        trunkCoords[NW] = coords.north(1).west(1);
        trunkCoords[N] = coords.north(1);
        trunkCoords[NE] = coords.north(1).east(1);
        trunkCoords[W] = coords.west(1);
        trunkCoords[CORE] = coords;
        trunkCoords[E] = coords.east(1);
        trunkCoords[SW] = coords.south(1).west(1);
        trunkCoords[S] = coords.south(1);
        trunkCoords[SE] = coords.south(1).east(1);

        return trunkCoords;
    }

    public int getMinSize() {
        return MIN_SIZE;
    }
}
