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
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.*;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 12/26/2024
 */
public interface IWitherTreeGenerator<RESULT extends IGeneratorResult<?>> {
    public static final int MAX_GEN_RADIUS = 20;
    public static final int CLEARING_RADIUS = 7;
    public static final int MAX_VERTICAL_DIFF = 3;
    public static final int GROUND_REPLACEMENT_PROBABILITY = 90;
    public static final int ROOT_PROBABILITY = 50;
    public static final int BRANCH_PROBABILITY = 30;
    public static final int SPANISH_MOSS_PROBABILITY = 80;
    public static final int TWIG_PROBABILITY = 40;

    public abstract Optional<RESULT> generate(IWorldGenContext context, ICoords spawnCoords);

    /**
     * @param coords
     * @param centerCoords
     * @return
     */
    default public boolean isGenerationWithinMaxRadius(ICoords coords, ICoords centerCoords) {
        // check if trunk index is outside generation radius
        if (Math.abs(coords.getX() - centerCoords.getX()) > getMaxGenRadius() ||
                Math.abs(coords.getZ() - centerCoords.getZ()) > getMaxGenRadius()) {
            return false;
        }
        return true;
    }

    default public int getMaxGenRadius() {
        return MAX_GEN_RADIUS;
    }

    default public int getClearingRadius() {
        return CLEARING_RADIUS;
    }

    default public int getMaxVerticalDiff() {
        return MAX_VERTICAL_DIFF;
    }

    default public int getGroundReplacementProbability() {
        return GROUND_REPLACEMENT_PROBABILITY;
    }

    default public int getWitherRootProbability() {
        return ROOT_PROBABILITY;
    }

    default public int getWitherBranchProbability() {
        return BRANCH_PROBABILITY;
    }

    default public int getSpanishMossProbability() {
        return SPANISH_MOSS_PROBABILITY;
    }

    default public int getTwigProbability() {
        return TWIG_PROBABILITY;
    }

    default public void generateClearing(IWorldGenContext context, ICoords coords) {
        ICoords buildCoords = null;
        Treasure.LOGGER.debug("build clearing at -> {}", coords.toShortString());

        // build clearing
        for (int xOffset = -(getClearingRadius()); xOffset <= getClearingRadius(); xOffset++) {
            for (int zOffset = -(getClearingRadius()); zOffset <= getClearingRadius(); zOffset++) {
                if (Math.abs(xOffset) + Math.abs(zOffset) <= getClearingRadius()) {
                    buildCoords = coords.add(xOffset, 0, zOffset);

                    // check if trunk index is outside generation radius
                    if (!isGenerationWithinMaxRadius(buildCoords, coords)) {
                        Treasure.LOGGER.debug("outside max radius -> skip");
                        continue;
                    }

                    // find the first surface
                    buildCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, buildCoords);

                    if (buildCoords == Coords.EMPTY) {
                        continue;
                    }

                    // additional check that it's not a tree and within 2 y-blocks of original
                    if (Math.abs(buildCoords.getY() - coords.getY()) < getMaxVerticalDiff()) {
                        BlockContext cube = new BlockContext(context.level(), buildCoords.down(1));
                        if (cube.isFluid()) {
                            continue;
                        }
                        if (RandomHelper.checkProbability(context.random(), getGroundReplacementProbability())) {
                            switch (context.random().nextInt(10)) {
                                case 0 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.MYCELIUM.defaultBlockState(), 3);
                                case 1 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.PODZOL.defaultBlockState(), 3);
                                case 2, 3 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.MUD.defaultBlockState(), 3);
                                case 4, 5 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.COARSE_DIRT.defaultBlockState(), 3);
                                default -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.DIRT.defaultBlockState(), 3);
                            }
                        }
                    }

                    // remove existing tree
                    BlockContext blockContext = new BlockContext(context.level(), buildCoords);
                    ICoords climbCoords = new Coords(buildCoords);
                    while (blockContext.getState().getBlock().defaultMapColor() == MapColor.WOOD
                            && !(blockContext.getState().getBlock() instanceof ITreasureBlock)) {

                        // remove log
                        context.level().setBlock(climbCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
                        // climb upwards
                        climbCoords = climbCoords.up(1);
                        blockContext = new BlockContext(context.level(), climbCoords);
                    }
                }
            }
        }
    }

    default public void addRoot(IWorldGenContext context, ICoords coords, ICoords spawnCoords, List<Direction> directions) {
        // for each direction
        for (Direction direction : directions) {
            if (RandomHelper.checkProbability(context.random(), getWitherRootProbability())) {
                // update the coords to the correct position
                ICoords newCoords = coords.add(direction, 1);

                // check if trunk index is outside generation radius
                if (!isGenerationWithinMaxRadius(newCoords, spawnCoords)) {
                    continue;
                }

                BlockContext groundBlockContext = new BlockContext(context.level(), newCoords.down(1));
                BlockContext replaceBlockContext = new BlockContext(context.level(), newCoords);
                if (!groundBlockContext.isReplaceable()
                        && (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable())) {

                    WorldInfo.setBlock(context.level(), newCoords, TreasureBlocks.WITHERWOOD_ROOT.get().defaultBlockState()
                            .setValue(WitherRootBlock.FACING, direction)
                            .setValue(WitherRootBlock.ACTIVATED, true));
                }
            }
        }
    }

    default public void addTop(IWorldGenContext context, ICoords coords, ICoords spawnCoords, int y, Direction direction) {
        if (direction != null) {
            BlockState state = TreasureBlocks.WITHERWOOD_BROKEN_LOG.get().defaultBlockState().setValue(WitherRootBlock.FACING, direction);
            ICoords topCoords = coords.up(y);
            if (isGenerationWithinMaxRadius(topCoords, spawnCoords)) {
                WorldInfo.setBlock(context.level(), topCoords, state);
            }
        }
    }

    default public void addBranch(IWorldGenContext context, ICoords trunkCoords, ICoords spawnCoords, int y, int maxSize, List<Direction> directions) {

        int branchSize = 0;
        if (y < maxSize / 2 || y > maxSize / 4)
            branchSize = 2;
        else
            branchSize = 1;

        // for each direction
        for (Direction direction : directions) {
            // TODO why do this - just pass in the correct y value when method is called
            ICoords c = trunkCoords.up(y);  // 7/2/2021 changed: added .add(0, y, 0)
            // randomize if a branch is generated
            if (RandomHelper.checkProbability(context.random(), getWitherBranchProbability())) {

                // check if trunk index is outside generation radius
                if (!isGenerationWithinMaxRadius(c, spawnCoords)) {
                    continue;
                }
                // for the num of branch segments
                for (int segment = 0; segment < branchSize; segment++) {
                    c = c.add(direction, 1);
                    BlockContext replaceBlockContext = new BlockContext(context.level(), c);

                    // if there is a branch directly below, don't add another branch but potentially add a twig
                    if (context.level().getBlockState(c.down(1).toPos()).getBlock() instanceof WitherBranchBlock) {
                        if (segment == 0 && RandomHelper.checkProbability(context.random(), getTwigProbability())) {
                            addTwig(context, c, direction);
                        }
                        break;
                    }

                    // if able to place branch here
                    if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
                        // rotate the branch in the right direction
                        BlockState state = TreasureBlocks.WITHERWOOD_BRANCH.get().defaultBlockState()
                                .setValue(WitherBranchBlock.FACING, direction);

                        // add the branch to the world
                        WorldInfo.setBlock(context.level(), c, state);

                        // add spanish moss
                        if (RandomHelper.checkProbability(context.random(), getSpanishMossProbability())) {
                            replaceBlockContext = new BlockContext(context.level(), c.add(0, /*y*/ - 1, 0));
                            if (replaceBlockContext.isAir() || replaceBlockContext.isReplaceable()) {
                                context.level().setBlock(replaceBlockContext.getCoords().toPos(), TreasureBlocks.SPANISH_MOSS
                                        .get().defaultBlockState().setValue(SpanishMossBlock.ACTIVATED, true), 3);
                            }
                        }
                    } else {
                        break;
                    }
                }
            } else if (RandomHelper.checkProbability(context.random(), getTwigProbability())) {
                addTwig(context, c.add(direction, 1), direction);
            }
        }
    }

    default public void addTwig(IWorldGenContext context, ICoords coords, Direction direction) {
        WorldInfo.setBlock(context.level(), coords, TreasureBlocks.WITHERWOOD_TWIG.get().defaultBlockState()
                .setValue(WitherTwigBlock.FACING, direction));
    }
}
