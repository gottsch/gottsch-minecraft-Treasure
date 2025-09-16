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
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author by Mark Gottschling on 8/18/2025
 */
public class WellFlowerProcessor extends StructureProcessor {
    public static final List<Block> FLOWERS = List.of(
            Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
            Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY);
    public static final List<Block> MUSHROOMS = List.of(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);
    public static final List<Block> TALL_PLANTS = List.of(Blocks.TALL_GRASS, Blocks.LARGE_FERN);


    public static final Codec<WellFlowerProcessor> CODEC = Codec.unit(WellFlowerProcessor::new);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos relativePos, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings) {

        // check if the current block is an end rod (which turns into a flower)
        if (!current.state().is(Blocks.END_ROD)) {
            return current;
        }

        /*
         * NOTE the heightMap seems to be localized to the template, meaning groundY always equals current.pos,
         * ie it does not return the real world height.
         */
        // find the ground level at the block's current x, z position.
        // int groundY = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, current.pos().getX(), current.pos().getZ());


        // calculate the block's new y position relative to the ground.
        // we use relativePos.getY() to get the original height in the template. (kinda moot as all flowers are at relative = 0)
        BlockPos newPos =  current.pos(); //new BlockPos(current.pos().getX(), groundY + relativePos.getY(), current.pos().getZ());
        BlockState groundState = levelReader.getBlockState(newPos);

        // 1. check the current pos if viable location
        if (groundState.isSolidRender(levelReader, newPos) && groundState.getFluidState().isEmpty()) {
            // check above, and if clear, move the pos up
            if (levelReader.getBlockState(newPos.above()).canBeReplaced()) {
                newPos = newPos.above();
            } else {
                return null;
            }
        }
        else if (!groundState.is(Blocks.AIR)) {
            return null;
        }

        BlockPos posBelow = newPos.below();
        BlockState stateBelow = levelReader.getBlockState(posBelow);

        // 2. if the block is not a grass block, it needs to be a valid, empty spot to move down.
        if (!stateBelow.is(Blocks.GRASS_BLOCK)) {
            // if the block is not replaceable or is a fluid, it's an invalid location.
            if (!stateBelow.canBeReplaced() || !stateBelow.getFluidState().isEmpty()) {
                return null;
            }

            // check if the block two positions below is a grass block.
            if (levelReader.getBlockState(posBelow.below()).is(Blocks.GRASS_BLOCK)) {
                newPos = posBelow;
            } else {
                return null;
            }
        }

        BlockState newState = null;
        // get a random probability from the settings and check conditions
        RandomSource random = placementSettings.getRandom(current.pos());

        if (random.nextDouble() < 1) { // TODO check against config values
            newState = random.nextInt(4) == 0
                    ? TALL_PLANTS.get(random.nextInt(TALL_PLANTS.size())).defaultBlockState()
                    : FLOWERS.get(random.nextInt(FLOWERS.size())).defaultBlockState();
        } else if (RandomHelper.checkProbability(placementSettings.getRandom(current.pos()), Config.SERVER.wells.cloverProbability.get())) {
            newState = TreasureBlocks.CLOVER.get().defaultBlockState();
        }

        return newState != null ? new StructureTemplate.StructureBlockInfo(newPos, newState, current.nbt()) : null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.WELL_FLOWER_PROCESSOR.get();
    }

}
