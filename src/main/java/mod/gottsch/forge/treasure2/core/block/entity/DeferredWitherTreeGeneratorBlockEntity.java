/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.treasure2.core.world.feature.gen.IWitherFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.WitherFeatureGenerator2;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.NotNull;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureGenContext;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author Mark Gottschling on Oct 12, 2023
 *
 */
public class DeferredWitherTreeGeneratorBlockEntity extends DeferredGeneratorBlockEntity {

//    private static final int X_CHUNKS = 3;
//    private static final int Z_CHUNKS = 3;
//    private static final int CHUNKS_IN_GRID = X_CHUNKS * Z_CHUNKS;

    // a 3x3 chunk (in blocks) array
    private int[][] grid;// = new int[X_CHUNKS * 16][Z_CHUNKS * 16];
    private List<ICoords> queuedChunks;

    public DeferredWitherTreeGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.DEFERRED_WITHER_TREE_GENERATOR_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public IFeatureType getFeatureType() {
        return FeatureType.TERRANEAN;
    }

    @Override
    public IFeatureGenerator getFeatureGenerator() {
        return TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR;
    }

    // NOTE postpone all Grid gen work
//    @Override
//    public void saveAdditional(CompoundTag tag) {
//        // flatten the grid array
//        int[] flattened = Arrays.stream(grid).flatMapToInt(Arrays::stream).toArray();
//        // add to a tag
//        IntArrayTag gridTag = new IntArrayTag(flattened);
//
//        // add array tag to the tag
//        tag.put("grid", gridTag);
//
//        // save the active chunk list
//        ListTag queuedList = new ListTag();
//        queuedChunks.forEach(element -> {
//            CompoundTag coordsTag = element.save(new CompoundTag());
//            queuedList.add(coordsTag);
//        });
//        tag.put("queuedChunks", queuedList);
//
//        super.saveAdditional(tag);
//    }

//    @Override
//    public void load(@NotNull CompoundTag tag) {
//        super.load(tag);
//        try {
//            if (tag.contains("grid")) {
//                int[] intArray = tag.getIntArray("grid");
//                // load intArray into 2d grid
//                int index = 0;
//                for (int x =0; x < grid.length; x++) {
//                    for (int z = 0; z < grid[x].length; z++) {
//                        grid[x][z] = intArray[index];
//                        index++;
//                    }
//                }
//            } else {
//                throw new Exception("unable to locate grid.");
//            }
//            if (tag.contains("queuedChunks")) {
//                queuedChunks.clear();
//                ListTag listTag = tag.getList("queuedChunks", Tag.TAG_COMPOUND);
//                listTag.forEach(activeTag -> {
//                    ICoords coords = Coords.EMPTY.load((CompoundTag)activeTag);
//                    queuedChunks.add(coords);
//                });
//            } else {
//                throw new Exception("unable to locate activeChunks");
//            }
//        } catch (Exception e) {
//            Treasure.LOGGER.error("error reading to tag:", e);
//        }
//    }
//
//    /**
//     * has to override tickServer because this entity is NOT a one-and-done.
//     * it has to manage several chunks of generation asynchronously.
//     */
//    @Override
//    public void tickServer() {
//
//        if (getLevel().isClientSide()) {
//            return;
//        }
//
//        try {
//            if (isEnabled()) {
//                // ensure the right type of chunk
//                if (getLevel().getChunkAt(this.getBlockPos()).getStatus().getChunkType() != ChunkStatus.ChunkType.LEVELCHUNK) {
//                    Treasure.LOGGER.debug("Not a CHUNKLEVEL - don't tick yet.");
//                    return;
//                }
//
//                Treasure.LOGGER.debug("executing tick on server for deferred generator BE");
//                IFeatureType FEATURE_TYPE = getFeatureType();
//                ResourceLocation dimension = WorldInfo.getDimension(getLevel());
//                ICoords spawnCoords = new Coords(getBlockPos());
//
//                IRarity rarity = getRarity();
//
//                // create FeatureGenContext
//                FeatureGenContext featureGenContext = new FeatureGenContext(
//                        (ServerLevel)getLevel(),
//                        ((ServerLevel)getLevel()).getChunkSource().getGenerator(),
//                        getLevel().getRandom(),
//                        FEATURE_TYPE);
//
//                // TODO this could be selected from a feature selector
//                // TODO could have a wrapper method that does the casting
//                IWitherFeatureGenerator featureGenerator = (IWitherFeatureGenerator) getFeatureGenerator();
//                Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
//
//
//                // initialize
//                if (getGeneratedTime() == 0) {
//
//                    // generate the grid
//                    this.grid = featureGenerator.generateGrid(featureGenContext, spawnCoords, rarity);
//
//                    // TEMP dump 2d grid
//                    dumpGrid();
//
//                    // get grid center coords
//                    ICoords gridCenterCoords = new Coords(grid.length / 2 - 1, 0, grid[0].length / 2 - 1);
//
//                    // centering the grid around spawn, calculate the relative offset center for each chunk in grid
//                    // and add to the queue chunks.
//                    for (int x = 0; x < featureGenerator.getXChunks(); x++) {
//                        for (int z = 0; z < featureGenerator.getZChunks(); z++) {
//                            int centerX = x * 16 + 7;
//                            int centerZ = z * 16 + 7;
//                            ICoords offsetChunkCenter = new Coords(centerX - gridCenterCoords.getX(), 0, centerZ - gridCenterCoords.getZ());
//                            this.queuedChunks.add(offsetChunkCenter);
//                            Treasure.LOGGER.debug("queuedChunk center pos -> {}", offsetChunkCenter);
//                        }
//                    }
//                }
//
//                // run
//                if (!queuedChunks.isEmpty()) {
//                    // get the chest registry
//                    GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
//                    if (chestCache == null) {
//                        Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRANEAN. This shouldn't be. Should be initialized.");
//                        return;
//                    }
//
//                    // get the generator config
//                    ChestFeaturesConfiguration config = Config.chestConfig;
//                    if (config == null) {
//                        Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
//                        failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                        return;
//                    }
//
//                    ChestFeaturesConfiguration.Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
//                    if (generatorConfig == null) {
//                        Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
//                        failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                        return;
//                    }
//
//                    Optional<ChestFeaturesConfiguration.ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
//                    if (!rarityConfig.isPresent()) {
//                        Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
//                        failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                        return;
//                    }
//
//                    // generate
//                    Optional<GeneratorResult<ChestGeneratorData>> result =
//                        featureGenerator.generate(featureGenContext, spawnCoords, rarity, rarityConfig.get(), grid, queuedChunks);
//
//                }
//            }
//        } catch(Exception e) {
//
//        } finally {
//            setGeneratedTime(getLevel().getGameTime());
//        }
//
//        // TODO remove self
//
//        // first time through - populate grid and active list
//        if (isEnabled() && getGeneratedTime() == 0) {
//            if (getLevel().isClientSide()) {
//                return;
//            }
//
//            if (getLevel().getChunkAt(this.getBlockPos()).getStatus().getChunkType() != ChunkStatus.ChunkType.LEVELCHUNK) {
//                Treasure.LOGGER.debug("Not a CHUNKLEVEL - don't tick yet.");
//                return;
//            }
//
//            try {
//                Treasure.LOGGER.debug("executing tick on server for deferred generator BE");
//                IFeatureType FEATURE_TYPE = getFeatureType();
//                ResourceLocation dimension = WorldInfo.getDimension(getLevel());
//                ICoords spawnCoords = new Coords(getBlockPos());
//
//                // get the chest registry
//                GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
//                if (chestCache == null) {
//                    Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRANEAN. This shouldn't be. Should be initialized.");
//                    return;
//                }
//
//                IRarity rarity = getRarity();
//
//                // get the generator config
//                ChestFeaturesConfiguration config = Config.chestConfig;
//                if (config == null) {
//                    Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
//                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                    return;
//                }
//
//                ChestFeaturesConfiguration.Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
//                if (generatorConfig == null) {
//                    Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
//                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                    return;
//                }
//
//                // TODO this could be selected from a feature selector
//                // TODO could have a wrapper method that does the casting
//                IWitherFeatureGenerator featureGenerator = (IWitherFeatureGenerator) getFeatureGenerator();
//                Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
//
//                // create FeatureGenContext
//                FeatureGenContext featureGenContext = new FeatureGenContext(
//                        (ServerLevel)getLevel(),
//                        ((ServerLevel)getLevel()).getChunkSource().getGenerator(),
//                        getLevel().getRandom(),
//                        FEATURE_TYPE);
//
//                // generate the grid
//                grid = ((WitherFeatureGenerator2)featureGenerator).generateGrid(featureGenContext, spawnCoords, rarity);
//
//                // TEMP dump 2d grid
//                dumpGrid();
//
//                // get grid center coords
//                ICoords gridCenterCoords = new Coords(grid.length / 2 - 1, 0, grid[0].length / 2 - 1);
//
//                // centering the grid around spawn, calculate the relative offset center for each chunk in grid
//                // and add to the queue chunks.
//                for (int x = 0; x < featureGenerator.getXChunks(); x++) {
//                    for (int z = 0; z < featureGenerator.getZChunks(); z++) {
//                        int centerX = x * 16 + 7;
//                        int centerZ = z * 16 + 7;
//                         ICoords offsetChunkCenter = new Coords(centerX - gridCenterCoords.getX(), 0, centerZ - gridCenterCoords.getZ());
//                        queuedChunks.add(offsetChunkCenter);
//                        Treasure.LOGGER.debug("queuedChunk center pos -> {}", offsetChunkCenter);
//                    }
//                }
//
//                Optional<ChestFeaturesConfiguration.ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
//                if (!rarityConfig.isPresent()) {
//                    Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
//                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
//                    return;
//                }
//
//                // TODO pass grid into feature generator
//                // call generate
////                Optional<GeneratorResult<ChestGeneratorData>> result =
////                        featureGenerator.generate(featureGenContext, spawnCoords, rarity, rarityConfig.get());
////
////                if (result.isPresent()) {
////                    Treasure.LOGGER.debug("generated and caching chest");
////                    cacheGeneratedChest((ServerLevel)getLevel(), rarity, FEATURE_TYPE, chestCache, result.get());
////                    updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
////                } else {
////                    Treasure.LOGGER.debug("failed to generate and placing placeholder");
////                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
////                    return;
////                }
////
////                // save world data
////                TreasureSavedData savedData = TreasureSavedData.get(getLevel());
////                if (savedData != null) {
////                    savedData.setDirty();
////                }
////                // TODO set a status that gen is complete instead of waiting for x time.
//
//            } catch(Exception e) {
//                Treasure.LOGGER.error("unable to generate feature", e);
//            } finally {
//                /*
//                 * can't self-destruct on server without getting a "Can't load DUMMY" spam message.
//                 */
//                setGeneratedTime(getLevel().getGameTime());
//            }
//        }
//
//        // remove self
//        if (!isEnabled() || (getGeneratedTime() > 0 && getLevel().getGameTime() > getGeneratedTime() + 40 && queuedChunks.isEmpty())) {
//            if (getLevel().getBlockState(getBlockPos()).getBlock() == getBlockState().getBlock()) {
//                Treasure.LOGGER.debug("destroying self ie set to air -> {}", this.getBlockPos().toShortString());
//                getLevel().setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
//            }
//            this.setRemoved();
//        }
//    }
//
//    public void dumpGrid() {
//        Treasure.LOGGER.debug("grid ->");
//        for (int[] row : grid) {
//            StringBuilder builder = new StringBuilder();
//            for (int z = 0; z < grid[0].length; z++) {
//                builder.append(row[z]);
//            }
//            Treasure.LOGGER.debug(builder.toString());
//        }
//    }
//
////    public static ICoords generateRandomCoordinate(RandomSource random, int min, int max) {
////        if (random == null) {
////            random = RandomSource.create();
////        }
////
////        // generate a random radius between min and max
////        double radius = random.nextDouble() * (max - min) + min;
////        // generate a random angle in radians
////        double angle = random.nextDouble() * 2 * Math.PI;
////        // calculate x and z coordinates
////        double x = radius * Math.cos(angle);
////        double z = radius * Math.sin(angle);
////
////        return new Coords((int)x, 0, (int)z);
////    }
//
//
////    public void tickServer() {
////        if (Config.SERVER.witherTree.enableWitherTree.get()) {
////            if (getLevel().isClientSide()) {
////                return;
////            }
////
////            try {
////                // TODO this currenlty is hardcoded for the the wither tree generator. this should use the wither tree feature select first
////
////                IFeatureType FEATURE_TYPE = FeatureType.TERRANEAN;
////                ResourceLocation dimension = WorldInfo.getDimension(getLevel());
////                ICoords spawnCoords = new Coords(getBlockPos());
////
////                // get the chest registry
////                GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
////                if (chestCache == null) {
////                    Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRANEAN. This shouldn't be. Should be initialized.");
////                    return;
////                }
////
////                // get the generator config
////                ChestFeaturesConfiguration config = Config.chestConfig;
////                if (config == null) {
////                    Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
////                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
////                    return;
////                }
////
////                ChestFeaturesConfiguration.Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
////                if (generatorConfig == null) {
////                    Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
////                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
////                    return;
////                }
////
////                IFeatureGenerator featureGenerator = TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR;
////                Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
////
////                Optional<ChestFeaturesConfiguration.ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
////                if (!rarityConfig.isPresent()) {
////                    Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
////                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
////                    return;
////                }
////                // call generate
////                Optional<GeneratorResult<ChestGeneratorData>> result =
////                        featureGenerator.generate(
////                                new FeatureGenContext(
////                                    (ServerLevel)getLevel(),
////                                    ((ServerLevel)getLevel()).getChunkSource().getGenerator(),
////                                    getLevel().getRandom(),
////                                    FEATURE_TYPE),
////                                spawnCoords, rarity, rarityConfig.get());
////
////                if (result.isPresent()) {
////                    cacheGeneratedChest((ServerLevel)getLevel(), rarity, FEATURE_TYPE, chestCache, result.get());
////                    updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
////                } else {
////                    failAndPlaceholdChest((ServerLevel)getLevel(), chestCache, rarity, spawnCoords, FEATURE_TYPE);
////                    return;
////                }
////
////                // save world data
////                TreasureSavedData savedData = TreasureSavedData.get(getLevel());
////                if (savedData != null) {
////                    savedData.setDirty();
////                }
////
////            } catch(Exception e) {
////                Treasure.LOGGER.error("unable to generate wither tree", e);
////            } finally {
////                if (getLevel().getBlockState(getBlockPos()).getBlock() == TreasureBlocks.DEFERRED_WITHER_TREE_GENERATOR.get()) {
////                    getLevel().setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
////                }
////            }
////        }
////    }
////
////    @Override
////    public void saveAdditional(CompoundTag tag) {
////        tag.putString("rarity", getRarity().getName());
////        super.saveAdditional(tag);
////    }
////
////    @Override
////    public void load(@NotNull CompoundTag tag) {
////        super.load(tag);
////        try {
////            Optional<IRarity> rarity = Optional.empty();
////            if (tag.contains("rarity")) {
////                rarity = TreasureApi.getRarity(tag.getString("rarity"));
////            }
////
////            setRarity(rarity.orElse(Rarity.NONE));
////        } catch (Exception e) {
////            Treasure.LOGGER.error("error reading to tag:", e);
////        }
////    }
////
////    public IRarity getRarity() {
////        return rarity;
////    }
////
////    public void setRarity(IRarity rarity) {
////        this.rarity = rarity;
////    }
////
////	@Override
////	public boolean meetsProximityCriteria(ServerLevelAccessor world, ResourceLocation dimension, IFeatureType key,
////			ICoords spawnCoords, int minDistance) {
////		// TODO Auto-generated method stub
////		return false;
////	}
}
