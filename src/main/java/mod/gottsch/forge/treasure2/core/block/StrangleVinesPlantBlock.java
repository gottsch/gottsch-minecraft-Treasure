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
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * @author Mark Gottschling on Aug 27, 2024
 *
 */
public class StrangleVinesPlantBlock extends GrowingPlantBodyBlock {
    private static final int MAX_HEIGHT = 10;

    public static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public StrangleVinesPlantBlock(Properties properties) {
        super(properties, Direction.UP, SHAPE, false);
    }

    /**
     * NOTE animateTick is on the client side only. The server is not keeping
     * track of any particles NOTE cannot control the number of ticks per
     * randomDisplayTick() call - it is not controlled by tickRate()
     */
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

        // NOTE can override methods here as it is a factory that creates the particle
        SimpleParticleType particle = ParticleTypes.SPORE_BLOSSOM_AIR;

        try {
            world.addParticle(particle, false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
            world.addParticle(TreasureParticles.BLACK_SPORE_PARTICLE.get(), false, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
        }
        catch(Exception e) {
            Treasure.LOGGER.error("error with particle:", e);
        }
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) TreasureBlocks.STRANGLE_VINES.get();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos p_262033_, BlockState p_261700_, boolean p_261524_) {
        int heightAbove = this.getHeightAboveUpToMax(level, p_262033_);
        int heightBelow = this.getHeightBelowUpToMax(level, p_262033_);

        return heightAbove + heightBelow + 1 < MAX_HEIGHT;
    }

    protected int getHeightAboveUpToMax(BlockGetter p_261541_, BlockPos p_261593_) {
        int i;
        for(i = 0; i < MAX_HEIGHT && p_261541_.getBlockState(p_261593_.above(i + 1)).is(TreasureBlocks.STRANGLE_VINES_PLANT.get()); ++i) {
        }
        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter p_261927_, BlockPos p_261481_) {
        int i;
        for(i = 0; i < MAX_HEIGHT && p_261927_.getBlockState(p_261481_.below(i + 1)).is(TreasureBlocks.STRANGLE_VINES_PLANT.get()); ++i) {
        }
        return i;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // doesn't affect Monster
        if (entity instanceof Monster) return;

        if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
            double d0 = Math.abs(entity.getX() - entity.xOld);
            double d1 = Math.abs(entity.getZ() - entity.zOld);
            if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
            }
        }
    }
}
