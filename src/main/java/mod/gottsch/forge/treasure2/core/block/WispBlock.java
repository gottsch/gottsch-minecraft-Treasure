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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A will-o'-the-wisp hanging in the air between the Wither Grove's trees: invisible and untouchable,
 * like vanilla's light block, but only at night does it give a faint light and drift pale motes.
 * Random ticks switch the light on at dusk and off at dawn; the motes check the time themselves, so
 * they follow the night exactly.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WispBlock extends Block implements ITreasureBlock {
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	public static final int LIGHT = 6;

	public WispBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		boolean night = level.isNight();
		if (state.getValue(LIT) != night) {
			level.setBlock(pos, state.setValue(LIT, night), Block.UPDATE_CLIENTS);
		}
	}

	/** dusk to dawn by the clock, as vanilla's isNight() reads in clear weather */
	private static boolean isNightTime(Level level) {
		long time = level.getDayTime() % 24000L;
		return time >= NIGHT_START && time < NIGHT_END;
	}

	private static final long NIGHT_START = 13000L;
	private static final long NIGHT_END = 23000L;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		// the clock, not level.isNight(): a client level sets its sky darkness once, when it loads, and
		// never again, so isNight() there is stuck at whatever it was on joining. Nor LIT, which waits for a
		// random tick (a minute or so) to catch up. The day time is synced, and /time set sends it at once.
		if (!isNightTime(level) || random.nextInt(3) != 0) {
			return;
		}
		// anywhere within a block or so of the wisp, pushed off in any direction, some sinking
		level.addParticle(TreasureParticles.WISP_PARTICLE.get(),
				pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0, pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.5,
				pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0,
				(random.nextDouble() - 0.5) * 0.04, (random.nextDouble() - 0.35) * 0.02, (random.nextDouble() - 0.5) * 0.04);
	}
}
