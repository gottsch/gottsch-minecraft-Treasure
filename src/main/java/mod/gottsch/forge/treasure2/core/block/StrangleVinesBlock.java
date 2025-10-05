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
package mod.gottsch.forge.treasure2.core.block;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * @author Mark Gottschling on Aug 27, 2024
 *
 */
public class StrangleVinesBlock extends GrowingPlantHeadBlock {
    private static final int MAX_HEIGHT = 10;

    public static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 15.0D, 12.0D);

    private static final double GROW_PER_TICK_PROBABILITY = 0.1D;

    /**
     *
     * @param properties
     */
    public StrangleVinesBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, SHAPE, false, GROW_PER_TICK_PROBABILITY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {

        if (RandomHelper.checkProbability(new Random(), 75D)) {
            return;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        // initial positions - has a spread area of up to 1.5 blocks
        double xPos = (x + 0.5D);
        double yPos = y - 0.1D;
        double zPos = (z + 0.5D);
        // initial velocities
        double velocityX = 0;
        double velocityY = -0.1; //0
        double velocityZ = 0;

        SimpleParticleType particle = ParticleTypes.SPORE_BLOSSOM_AIR;

        try {
            world.addParticle(particle, false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
        }
        catch(Exception e) {
            Treasure.LOGGER.error("error with particle:", e);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(AGE) < 25 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos.relative(this.growthDirection), level.getBlockState(pos.relative(this.growthDirection)),randomSource.nextDouble() < GROW_PER_TICK_PROBABILITY)) {
            int height = this.getHeightBelowUpToMax(level, pos) + 1;
            if (height >= MAX_HEIGHT) {
                state.setValue(AGE, MAX_AGE);
                return;
            }
            BlockPos blockpos = pos.relative(this.growthDirection);
            if (this.canGrowInto(level.getBlockState(blockpos))) {
                level.setBlockAndUpdate(blockpos, this.getGrowIntoState(state, level.random));
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, blockpos, level.getBlockState(blockpos));
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos blockPos, BlockState blockState, boolean b) {
        int height = this.getHeightBelowUpToMax(level, blockPos) + 1;
        if (height >= MAX_HEIGHT) {
            blockState.setValue(AGE, MAX_AGE);
        }
        return height < MAX_HEIGHT;
    }

    @Deprecated
    protected int getHeightAboveUpToMax(BlockGetter getter, BlockPos blockPos) {
        int i;
        for(i = 0; i < MAX_HEIGHT && getter.getBlockState(blockPos.above(i + 1)).is(getBodyBlock()); ++i) {
        }
        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter getter, BlockPos blockPos) {
        int i;
        for(i = 0; i < MAX_HEIGHT && getter.getBlockState(blockPos.below(i + 1)).is(getBodyBlock()); ++i) {
        }
        return i;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource randomSource) {
        return NetherVines.getBlocksToGrowWhenBonemealed(randomSource);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return NetherVines.isValidGrowthState(state);
    }

    @Override
    protected Block getBodyBlock() {
        return TreasureBlocks.STRANGLE_VINES_PLANT.get();
    }
}
