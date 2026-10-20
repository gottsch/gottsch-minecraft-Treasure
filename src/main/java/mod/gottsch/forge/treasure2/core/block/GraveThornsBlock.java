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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A low, dead bramble at the Wither Grove's edge. Like a sweet berry bush it slows anything walking
 * through and stings it while it moves, but it never grows or fruits, and (like the strangle vines)
 * it leaves monsters alone, so the grove's own mobs pass through freely.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class GraveThornsBlock extends BushBlock implements ITreasureBlock {
	private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);

	public GraveThornsBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!(entity instanceof LivingEntity) || entity instanceof Monster) {
			return;
		}
		entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
		if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
			double dx = Math.abs(entity.getX() - entity.xOld);
			double dz = Math.abs(entity.getZ() - entity.zOld);
			if (dx >= 0.003F || dz >= 0.003F) {
				entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
			}
		}
	}
}
