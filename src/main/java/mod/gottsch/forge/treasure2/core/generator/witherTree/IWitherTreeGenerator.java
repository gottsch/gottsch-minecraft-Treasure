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
import mod.gottsch.forge.gottschcore.size.DoubleRange;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.*;
import mod.gottsch.forge.treasure2.core.block.entity.ProximityMobSetSpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.MobSetConfiguration;
import mod.gottsch.forge.treasure2.core.config.StructureConfiguration;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import mod.gottsch.forge.treasure2.core.size.IntegerRange;
import mod.gottsch.forge.treasure2.core.util.GeometryUtil;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 12/26/2024
 */
public interface IWitherTreeGenerator<RESULT extends IGeneratorResult<?>> {
    /*
     * During generation a 3x3 (x-z axis) chunk area is available to alter ( = 48 blocks).
     * From center, there is a 23/24 block radius (since even number).
     * To be safe, the max gen radius is set to 20.
     */
    public static final int MAX_GEN_RADIUS = 20;
    public static final int CLEARING_RADIUS = 7;
    public static final int MAX_VERTICAL_DIFF = 3;
    public static final int GROUND_REPLACEMENT_PROBABILITY = 90;
    public static final int ROOT_PROBABILITY = 50;
    public static final int BRANCH_PROBABILITY = 30;
    public static final int SPANISH_MOSS_PROBABILITY = 80;
    public static final int TWIG_PROBABILITY = 40;
    public static final int MIN_STRANGLE_VINES = 1;
    public static final int MAX_STRANGLE_VINES = 5;
    public static final int MAX_ROCKS = 5;
    public static final int MIN_ROCKS = 0;
    public static final int MIN_SCRUB = 10;
    public static final int MAX_SCRUB = 20;
    public static final int MIN_SPAWNERS = 0;
    public static final int MAX_SPAWNERS = 2;
    public static final double MOB_SPAWNER_PROXIMITY = 10D;

    public abstract Optional<RESULT> generate(IWorldGenContext context, ICoords coords, ICoords spawnCoords);

    /**
     * @param coords
     * @param centerCoords
     * @return
     */
    // TODO come up with better/faster check ie calculate AABB before hand and test coords against it.
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

    default public int getMinStrangleVines() {
        return MIN_STRANGLE_VINES;
    }

    default public int getMaxStrangleVines() {
        return MAX_STRANGLE_VINES;
    }

    default public void generateClearing(IWorldGenContext context, ICoords coords, AABB area) {
        ICoords buildCoords = null;
        Treasure.LOGGER.debug("build clearing at -> {}", coords.toShortString());

        // build clearing
        for (int xOffset = -(getClearingRadius()); xOffset <= getClearingRadius(); xOffset++) {
            for (int zOffset = -(getClearingRadius()); zOffset <= getClearingRadius(); zOffset++) {
                if (Math.abs(xOffset) + Math.abs(zOffset) <= getClearingRadius()) {
                    buildCoords = coords.add(xOffset, 0, zOffset);

                    // check if buildCoords is outside generation radius
                    if (!area.contains(buildCoords.toVec3())) {
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
                                case 2 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.MUD.defaultBlockState(), 3);
                                case 3 -> context.level().setBlock(buildCoords.down(1).toPos(), Blocks.PACKED_MUD.defaultBlockState(), 3);
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

    default public void addRoot(IWorldGenContext context, ICoords coords, AABB area, List<Direction> directions) {
        // for each direction
        for (Direction direction : directions) {
            if (RandomHelper.checkProbability(context.random(), getWitherRootProbability())) {
                // update the coords to the correct position
                ICoords newCoords = coords.add(direction, 1);

                // check if trunk index is outside generation radius
                if (!area.contains(newCoords.toVec3())) {
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

    default public void addTop(IWorldGenContext context, ICoords coords, AABB area, int y, Direction direction) {
        if (direction != null) {
            BlockState state = TreasureBlocks.WITHERWOOD_BROKEN_LOG.get().defaultBlockState().setValue(WitherRootBlock.FACING, direction);
            ICoords topCoords = coords.up(y);
            if (area.contains(topCoords.toVec3())) {
                WorldInfo.setBlock(context.level(), topCoords, state);
            }
        }
    }

    default public void addBranch(IWorldGenContext context, ICoords trunkCoords, AABB area, int y, int maxSize, List<Direction> directions) {

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

                // for the num of branch segments
                for (int segment = 0; segment < branchSize; segment++) {
                    c = c.add(direction, 1);
                    // check if trunk index is outside generation radius
                    if (!area.contains(c.toVec3())) {
                        break;
                    }

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

    /**
     * NOTE this method is different than addScrub() and addRocks() as it
     * takes in the exact coords to place the strangle vines.
     * @param context
     * @param coords
     * @param area
     */
    default public void addStrangleVines(IWorldGenContext context, ICoords coords, AABB area) {
        coords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), coords);

        // check if trunk index is outside generation radius
        if (coords == Coords.EMPTY || !area.contains(coords.toVec3())) {
            Treasure.LOGGER.debug("unable to place strangle vines at {} -> outside; area -> {}", coords.toShortString(), area);
            return;
        }

        // validate coords
        BlockContext groundBlockContext = new BlockContext(context.level(), coords.down(1));
        BlockContext replaceBlockContext = new BlockContext(context.level(), coords);
        if (groundBlockContext.isReplaceable()
                || (!replaceBlockContext.isAir() && !replaceBlockContext.isReplaceable())
                || (groundBlockContext.getState().getBlock() instanceof ITreasureBlock)) {
            Treasure.LOGGER.debug("unable to place strangle vines -> {}; area -> {}", coords.toShortString(), area);
            return;
        }

        context.level().setBlock(coords.toPos(), TreasureBlocks.STRANGLE_VINES.get().defaultBlockState(), 3);
        BlockState bonemealState = context.level().getBlockState(coords.toPos());
        ((BonemealableBlock) bonemealState.getBlock())
                .performBonemeal((ServerLevel) context.level(), context.random(), coords.toPos(), bonemealState);

    }

    // NOTE this should really belong in a WitherGroveGenerator class
    default public void addRocks(IWorldGenContext context, AABB area, ICoords spawnCoords) {
        int width = Math.abs((int) (area.maxX - area.minX));
        int depth = Math.abs((int) (area.maxZ - area.minZ));

        int radius = Math.max(width/2, depth/2);
        for (int rockIndex = 0; rockIndex < RandomHelper.randomInt(getMinRocks(), getMaxRocks()); rockIndex++) {
            ICoords rocksCoords = GeometryUtil.generateRandomCoordsByRadius(context.random(), 4, radius);
            rocksCoords = rocksCoords.add(spawnCoords);

            rocksCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), rocksCoords);

            // check if coords is outside generation radius
            if (rocksCoords == Coords.EMPTY || !area.contains(rocksCoords.toVec3())) {
                continue;
            }

            // check if current block is a tree or any treasure block
            if (context.level().getBlockState(rocksCoords.toPos()).getBlock() instanceof ITreasureBlock) {
                continue;
            }

            // build rock
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    for (int x = 0; x < 2; x++) {
                        if (RandomHelper.checkProbability(context.random(), 70)) {
                            ICoords c = new Coords(rocksCoords).add(x, y, z);
                            context.level().setBlock(c.toPos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    // TODO register wither tree mobsets, then pull a random mobset from registry
    default public void addSpawners(IWorldGenContext context, AABB area, ICoords spawnCoords) {
        int radius = (int) Math.max(area.getXsize() / 2, area.getZsize() / 2);
        for (int i = 0; i < RandomHelper.randomInt(getMinSpawners(), getMaxSpawners()); i++) {
            ICoords spawnerCoords = GeometryUtil.generateRandomCoordsByRadius(context.random(), 4, radius);
            spawnerCoords = spawnerCoords.add(spawnCoords);

            spawnerCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), spawnerCoords);

            // check if coords is outside generation radius
            if (spawnerCoords == Coords.EMPTY || !area.contains(spawnerCoords.toVec3())) {
                Treasure.LOGGER.debug("unable to place spawner at {} -> outside; area -> {}", spawnerCoords.toShortString(), area);
                continue;
            }

            // check if current block is a tree or any treasure block
            if (context.level().getBlockState(spawnerCoords.toPos()).getBlock() instanceof ITreasureBlock) {
                Treasure.LOGGER.debug("unable to place spawner at {} -> outside; area -> {}", spawnerCoords.toShortString(), area);
                continue;
            }

            ResourceLocation mobSetName = ModUtil.asLocation("wither_tree_group");

            if (Config.mobSetMap.containsKey(mobSetName)) {
                MobSetConfiguration.MobSet mobSet = Config.mobSetMap.get(mobSetName);
                context.level().setBlock(spawnerCoords.toPos(), TreasureBlocks.PROXIMITY_MOBSET_SPAWNER.get().defaultBlockState(), 3);
                ProximityMobSetSpawnerBlockEntity blockEntity = (ProximityMobSetSpawnerBlockEntity) context.level().getBlockEntity(spawnerCoords.toPos());
                if (blockEntity != null) {
                    blockEntity.setMobSetName(mobSetName);
                    blockEntity.setMobSizeRange(new IntegerRange(mobSet.getCount().getMin(), mobSet.getCount().getMax()));
                    blockEntity.setProximity(getMobSpawnerProximity());
                    Treasure.LOGGER.debug("placed spawner at {} -> outside; area -> {}", spawnerCoords.toShortString(), area);
                }
             }
        }
    }

    default public int getMinSpawners() {
        return MIN_SPAWNERS;
    }

    default public int getMaxSpawners() {
        return MAX_SPAWNERS;
    }

    default public double getMobSpawnerProximity() {
        return MOB_SPAWNER_PROXIMITY;
    }

    default public void addScrub(IWorldGenContext context, AABB area, ICoords spawnCoords) {
        int radius = (int) Math.max(area.getXsize() / 2, area.getZsize() / 2);

        for (int scrubIndex = 0; scrubIndex < RandomHelper.randomInt(getMinScrub(), getMaxScrub()); scrubIndex++) {
            ICoords coords = GeometryUtil.generateRandomCoordsByRadius(context.random(), 4, radius);
            coords = coords.add(spawnCoords);

            coords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), coords);
            // check if coords is outside generation radius
            if (coords == Coords.EMPTY || !area.contains(coords.toVec3())) {
                continue;
            }

            // validate coords
            BlockContext groundBlockContext = new BlockContext(context.level(), coords.down(1));
            BlockContext replaceBlockContext = new BlockContext(context.level(), coords);
            if (groundBlockContext.isReplaceable()
                    || (!replaceBlockContext.isAir() && !replaceBlockContext.isReplaceable())
                    || (groundBlockContext.getState().getBlock() instanceof ITreasureBlock)) {
                Treasure.LOGGER.debug("unable to place scrub -> {}; area -> {}", coords.toShortString(), area);
                continue;
            }

            // check if current block is a dirt, podzol, coarse dirt or sand
//            Block supportBlock = context.level().getBlockState(coords.down(1).toPos()).getBlock();
//            if (supportBlock == Blocks.DIRT || supportBlock == Blocks.SAND) {
                // randomize between bush and stump
                if (RandomHelper.checkProbability(context.random(), 25)) {
                    context.level().setBlock(coords.toPos(), Blocks.OAK_LOG.defaultBlockState(), 3);
                }
                else {
                    context.level().setBlock(coords.toPos(), Blocks.DEAD_BUSH.defaultBlockState(), 3);
                }
//            }
        }
    }

    default public int getMinRocks() {
        return MIN_ROCKS;
    }

    default public int getMaxRocks() {
        return MAX_ROCKS;
    }

    default public int getMinScrub() {
        return MIN_SCRUB;
    }

    default public int getMaxScrub() {
        return MAX_SCRUB;
    }
}
