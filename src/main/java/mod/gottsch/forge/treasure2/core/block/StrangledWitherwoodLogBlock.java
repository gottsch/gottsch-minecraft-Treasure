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
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

/**
 * A standing witherwood log with a strangle vine wound round it.
 * <p>
 * The vine is drawn on one face, corner to corner, from bottom left to top right; that corner is where
 * the next face round begins, one block up. So a trunk whose blocks turn one face on per block
 * (FACE 0, 1, 2, 3 = north, west, south, east) carries one continuous strand spiralling up it. FULL puts
 * the strand on every face: four strands, a trunk choked by vines. Shares the witherwood log's mossy
 * property. An axe strips it to a stripped log, vine and all.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class StrangledWitherwoodLogBlock extends Block implements ITreasureBlock {
	/** which face carries the strand, in the order the strand climbs: north, west, south, east */
	public static final IntegerProperty FACE = IntegerProperty.create("face", 0, 3);
	public static final BooleanProperty FULL = BooleanProperty.create("full");
	private static final Direction[] CLIMB = {Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};

	public StrangledWitherwoodLogBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACE, 0).setValue(FULL, false)
				.setValue(WitherwoodRotatedPillarBlock.MOSSY, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACE, FULL, WitherwoodRotatedPillarBlock.MOSSY);
	}

	/** the state for the block at height y of a strand that starts on face start */
	public BlockState climbing(int start, int y) {
		return defaultBlockState().setValue(FACE, Math.floorMod(start + y, 4));
	}

	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
		if (context.getItemInHand().getItem() instanceof AxeItem) {
			return TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		}
		return super.getToolModifiedState(state, context, toolAction, simulate);
	}

	/** now and then a black spore drifts off the vine */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(state.getValue(FULL) ? 12 : 30) != 0) {
			return;
		}
		Direction face = state.getValue(FULL) ? CLIMB[random.nextInt(4)] : CLIMB[state.getValue(FACE)];
		if (!level.getBlockState(pos.relative(face)).isAir()) {
			return;
		}
		double x = pos.getX() + 0.5 + face.getStepX() * 0.55 + (face.getStepX() == 0 ? random.nextDouble() - 0.5 : 0);
		double z = pos.getZ() + 0.5 + face.getStepZ() * 0.55 + (face.getStepZ() == 0 ? random.nextDouble() - 0.5 : 0);
		level.addParticle(TreasureParticles.BLACK_SPORE_PARTICLE.get(), x, pos.getY() + random.nextDouble(), z, 0, 0, 0);
	}
}
