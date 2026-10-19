/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Strangle Vines: a tangle of strangle vine clinging flat to the faces of the blocks around it, any of
 * the six, like glow lichen. It catches at anything that pushes through it (slows it and scratches it
 * while it moves), but not the monsters of the Wither Grove. Only shears take it.
 * <p>
 * The upright strangle vine plant keeps the strangle_vines id but is shown as Blightstalk; this block is
 * the one now named Strangle Vines.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class ClingingStrangleVinesBlock extends MultifaceBlock implements ITreasureBlock {
	private final MultifaceSpreader spreader = new MultifaceSpreader(this);

	public ClingingStrangleVinesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public MultifaceSpreader getSpreader() {
		return spreader;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!(entity instanceof LivingEntity) || entity instanceof Monster) {
			return;
		}
		entity.makeStuckInBlock(state, new Vec3(0.85F, 0.9D, 0.85F));
		if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
			double dx = Math.abs(entity.getX() - entity.xOld);
			double dz = Math.abs(entity.getZ() - entity.zOld);
			if (dx >= 0.003F || dz >= 0.003F) {
				entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
			}
		}
	}

	/** now and then a black spore drifts off it */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(40) != 0) {
			return;
		}
		level.addParticle(TreasureParticles.BLACK_SPORE_PARTICLE.get(),
				pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0, 0);
	}

	/** this block with just the given face covered */
	public BlockState on(Direction face) {
		return defaultBlockState().setValue(getFaceProperty(face), true);
	}
}
