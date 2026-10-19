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
package mod.gottsch.forge.treasure2.core.structure.grove;

import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import mod.gottsch.forge.treasure2.core.block.StrangledWitherwoodLogBlock;
import java.util.HashSet;
import java.util.Set;
import mod.gottsch.forge.treasure2.core.block.BracketFungusBlock;
import net.minecraft.world.level.block.LanternBlock;
import mod.gottsch.forge.gottschcore.block.IFacingBlock;
import mod.gottsch.forge.treasure2.core.block.SpanishMossBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.WitherRootBlock;
import mod.gottsch.forge.treasure2.core.block.WitherwoodRotatedPillarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grows a wither tree into a {@link WitherTreeBlueprint}, built on the 1.20 (pre-4.0)
 * WitherTreeGenerator and GreatWitherTreeGenerator: straight log columns, with branches, twigs, moss,
 * roots, a broken-log top, strangle vines around the tree and a clearing under it. The branches are
 * placed differently from those generators: dead stubs low down, a spiral above, tapering to the top.
 * <p>
 * Everything is drawn from one RandomSource seeded by the piece, so the same seed always gives the
 * same tree; never use the level's random here.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public final class WitherTreeBuilder {
	private static final int MIN_SIZE = 9;
	private static final int MAX_SIZE = 17;
	private static final int GREAT_MIN_SIZE = 11;
	private static final int GREAT_MAX_SIZE = 19;

	private static final float ROOT_PROBABILITY = 0.5F;
	/** moss under a branch; lower branches get the bonus as well */
	private static final float SPANISH_MOSS_PROBABILITY = 0.4F;
	private static final float LOW_MOSS_BONUS = 0.15F;
	private static final float TWIG_PROBABILITY = 0.4F;
	private static final float TRUNK_TWIG_PROBABILITY = 0.15F;
	/** chance per layer of a stub or twig below the living crown */
	private static final float DEAD_LIMB_PROBABILITY = 0.25F;
	/** the shortest a tree is scaled down to toward the grove's edge */
	private static final int MIN_SCALED_SIZE = 7;
	/** layers between living branches: single trunks, and each outer column of a great tree */
	private static final int WITHER_BRANCH_SPACING = 1;
	private static final int GREAT_BRANCH_SPACING = 2;
	private static final float GROUND_REPLACEMENT_PROBABILITY = 0.9F;
	/** fallen leaves on a clearing cell (not on packed mud, which they can't lie on) */
	private static final float LEAF_LITTER_PROBABILITY = 0.25F;
	private static final int CLEARING_RADIUS = 7;
	private static final int STRANGLE_VINE_ATTEMPTS = 4;
	private static final int STRANGLE_VINES_MAX_HEIGHT = 10;
	/**
	 * Moss climbs each log column from the ground to its own moss line, somewhere between a fifth and
	 * a little over half its height; above the line only the odd log is mossy.
	 */
	private static final float MOSS_LINE_MIN = 0.2F;
	private static final float MOSS_LINE_RANGE = 0.35F;
	private static final float MOSS_BELOW_LINE = 0.85F;
	private static final float MOSS_ABOVE_LINE = 0.1F;
	private static final float MOSS_TOP = 0.25F;
	/** salt for the extras' own random, so adding them left every tree's trunk, branches and clearing as they were */
	private static final long EXTRAS_SALT = 0x6A09E667F3BCC909L;
	/** per clearing cell within SUSPICIOUS_GRAVEL_REACH of the trunk */
	private static final float SUSPICIOUS_GRAVEL_PROBABILITY = 0.012F;
	private static final int SUSPICIOUS_GRAVEL_REACH = 3;
	/** grave thorns on the clearing's outer ring, more toward the grove's edge (where trees are scaled down) */
	private static final int THORNS_INNER = 4;
	private static final float THORNS_PROBABILITY = 0.03F;
	private static final float THORNS_EDGE_BONUS = 0.12F;
	private static final float LANTERN_PROBABILITY = 0.2F;
	private static final float WISP_PROBABILITY = 0.35F;
	private static final float SECOND_WISP_PROBABILITY = 0.1F;
	/** a web strung between two branches that leave the trunk at right angles on the same layer */
	private static final float FORK_WEB_PROBABILITY = 0.5F;
	/** a web in the angle above a branch, against the trunk */
	private static final float CORNER_WEB_PROBABILITY = 0.04F;
	private static final int MAX_WEBS = 2;
	/** per open log face in the lower part of the trunk */
	private static final float FUNGUS_PROBABILITY = 0.05F;
	private static final float FUNGUS_REACH = 0.65F;
	private static final int MAX_FUNGI = 4;
	/** salt for the strangle vine's own random, so the wrap changes nothing else */
	private static final long STRANGLE_SALT = 0xBB67AE8584CAA73BL;
	private static final float STRANGLED_PROBABILITY = 0.45F;
	/** of strangled trunks, how many are choked (vine on every face) at the foot */
	private static final float CHOKED_PROBABILITY = 0.25F;

	private static final List<Direction> ALL = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

	private WitherTreeBuilder() {}

	/**
	 * @param scale 1 for a full-size tree; less toward the grove's edge, where trees are shorter
	 */
	public static WitherTreeBlueprint build(long seed, WitherTreeArchetype archetype, float scale) {
		RandomSource random = RandomSource.create(seed);
		WitherTreeBlueprint blueprint = new WitherTreeBlueprint();
		switch (archetype) {
			case WITHER -> wither(blueprint, random, scale);
			case GREAT -> great(blueprint, random, scale);
		}
		moss(blueprint, seed);
		strangleVines(blueprint, random);
		clearing(blueprint, random);
		extras(blueprint, RandomSource.create(seed ^ EXTRAS_SALT), scale);
		strangle(blueprint, new XoroshiroRandomSource(seed ^ STRANGLE_SALT));
		return blueprint;
	}

	/**
	 * A single log column, its living branches spiralling round it.
	 */
	private static void wither(WitherTreeBlueprint blueprint, RandomSource random, float scale) {
		int size = scaled(between(random, MIN_SIZE, MAX_SIZE), scale);
		column(blueprint, new BlockPos(0, 1, 0), size, log(), ALL, WITHER_BRANCH_SPACING, true, ALL.get(random.nextInt(ALL.size())), random);
	}

	/**
	 * A 3x3 trunk with a stripped core. Each outer column has its own height and only branches and
	 * roots toward its own outer sides; every other column gets a broken-log top. Two outer columns
	 * stop well short, snapped, with a broken log of their own.
	 */
	private static void great(WitherTreeBlueprint blueprint, RandomSource random, float scale) {
		// NW, N, NE, W, CORE, E, SW, S, SE
		int[][] offsets = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
		List<List<Direction>> sides = List.of(
				List.of(Direction.NORTH, Direction.WEST), List.of(Direction.NORTH), List.of(Direction.NORTH, Direction.EAST),
				List.of(Direction.WEST), List.of(), List.of(Direction.EAST),
				List.of(Direction.SOUTH, Direction.WEST), List.of(Direction.SOUTH), List.of(Direction.SOUTH, Direction.EAST));
		List<Direction> tops = List.of(Direction.EAST, Direction.SOUTH, Direction.SOUTH, Direction.EAST, Direction.NORTH,
				Direction.WEST, Direction.NORTH, Direction.NORTH, Direction.WEST);
		int core = 4;
		List<Integer> outer = new ArrayList<>(List.of(0, 1, 2, 3, 5, 6, 7, 8));
		int snappedA = outer.remove(random.nextInt(outer.size()));
		int snappedB = outer.remove(random.nextInt(outer.size()));

		int minSize = GREAT_MIN_SIZE;
		int maxSize = between(random, GREAT_MIN_SIZE, GREAT_MAX_SIZE);
		int size = between(random, Math.min(minSize, maxSize), Math.max(minSize, maxSize));
		int tallest = 0;
		for (int i = 0; i < offsets.length; i++) {
			BlockPos base = new BlockPos(offsets[i][0], 1, offsets[i][1]);
			if (i == core) {
				column(blueprint, base, scaled(size, scale), TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState(),
						List.of(), GREAT_BRANCH_SPACING, false, null, random);
			}
			else {
				boolean snapped = i == snappedA || i == snappedB;
				int columnSize = scaled(size, scale);
				if (snapped) {
					columnSize = Math.max(4, Math.round(columnSize * (0.4F + random.nextFloat() * 0.2F)));
				}
				column(blueprint, base, columnSize, log(), sides.get(i), GREAT_BRANCH_SPACING, snapped || i % 2 == 0, tops.get(i), random);
			}
			tallest = Math.max(size, tallest);
			if (i == core) {
				size = between(random, Math.min(GREAT_MIN_SIZE, tallest - 1), Math.max(GREAT_MIN_SIZE, tallest - 1));
			}
			else {
				if (maxSize - minSize <= 2) {
					minSize -= i % 2;
					maxSize -= i % 2;
				}
				size = between(random, Math.min(minSize, maxSize), Math.max(minSize, maxSize));
			}
		}
	}

	/**
	 * One trunk column: roots at its foot, limbs up its sides, and optionally a broken log one above
	 * its top.
	 * @param sides the directions this column may root and branch toward
	 * @param spacing the usual number of layers between living branches
	 */
	private static void column(WitherTreeBlueprint blueprint, BlockPos base, int size, BlockState state, List<Direction> sides,
			int spacing, boolean top, Direction topFacing, RandomSource random) {
		for (int y = 0; y < size; y++) {
			BlockPos pos = base.above(y);
			blueprint.structure(pos, state);
			if (y == 0) {
				// fill any dip under the column
				blueprint.sink(pos, state);
				roots(blueprint, pos, sides, random);
			}
		}
		if (top) {
			blueprint.structure(base.above(size), facing(TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(), topFacing));
		}
		limbs(blueprint, base, size, sides, spacing, random);
	}

	private static void roots(WitherTreeBlueprint blueprint, BlockPos trunk, List<Direction> sides, RandomSource random) {
		for (Direction d : sides) {
			if (random.nextFloat() < ROOT_PROBABILITY) {
				BlockPos pos = trunk.relative(d);
				if (!blueprint.isOccupied(pos)) {
					// placed only where the ground below is solid (see WitherTreeBlueprint#isSupported)
					blueprint.decorate(pos, facing(TreasureBlocks.WITHERWOOD_ROOT.get(), d).setValue(WitherRootBlock.ACTIVATED, true));
				}
			}
		}
	}

	/**
	 * The lowest quarter of the trunk has only dead limbs: the odd snapped stub or bare twig. Above it
	 * the living branches spiral round the trunk, a quarter turn (sometimes a half) each time: two
	 * blocks long in the lower half, one in the upper half, on nearly every layer.
	 */
	private static void limbs(WitherTreeBlueprint blueprint, BlockPos base, int size, List<Direction> sides, int spacing, RandomSource random) {
		if (sides.isEmpty()) {
			return;
		}
		int deadTop = Math.max(3, size / 4);
		for (int y = 2; y < deadTop; y++) {
			if (random.nextFloat() >= DEAD_LIMB_PROBABILITY) {
				continue;
			}
			Direction d = sides.get(random.nextInt(sides.size()));
			if (random.nextFloat() < 0.6F) {
				run(blueprint, base.above(y), d, 1, true, random);
			}
			else {
				twig(blueprint, base.above(y).relative(d), d);
			}
		}

		int side = random.nextInt(sides.size());
		boolean clockwise = random.nextBoolean();
		int y = deadTop + random.nextInt(2);
		while (y <= size - 2) {
			boolean low = y < size / 2;
			boolean upper = y >= size * 2 / 3;
			run(blueprint, base.above(y), sides.get(side), low ? 2 : 1, low, random);
			if (sides.size() == 4 && random.nextFloat() < 0.45F) {
				// a second branch on the same layer: opposite in the crown, at a right angle lower down
				int other = upper ? (side + 2) % 4 : (side + (clockwise ? 3 : 1)) % 4;
				run(blueprint, base.above(y), sides.get(other), 1, low, random);
			}
			int turn = random.nextFloat() < 0.25F ? 2 : 1;
			side = Math.floorMod(side + (clockwise ? turn : -turn), sides.size());
			y += upper ? 1 : spacing + (random.nextFloat() < 0.35F ? 1 : 0);
		}

		// twigs on the bare trunk between branches
		for (int t = deadTop; t <= size - 2; t++) {
			if (random.nextFloat() < TRUNK_TWIG_PROBABILITY) {
				Direction d = sides.get(random.nextInt(sides.size()));
				twig(blueprint, base.above(t).relative(d), d);
			}
		}
	}

	/**
	 * A straight run of branch blocks out of the trunk, moss hanging under it. Never sits directly on
	 * another branch (a twig on the trunk instead, as in the 1.20 generator).
	 */
	private static void run(WitherTreeBlueprint blueprint, BlockPos trunk, Direction d, int length, boolean low, RandomSource random) {
		BlockPos pos = trunk;
		BlockPos last = null;
		for (int segment = 0; segment < length; segment++) {
			pos = pos.relative(d);
			if (blueprint.isBlock(pos.below(), TreasureBlocks.WITHERWOOD_BRANCH.get()) || blueprint.isOccupied(pos)) {
				if (segment == 0 && random.nextFloat() < TWIG_PROBABILITY) {
					twig(blueprint, pos, d);
				}
				break;
			}
			blueprint.structure(pos, facing(TreasureBlocks.WITHERWOOD_BRANCH.get(), d));
			last = pos;
			float moss = SPANISH_MOSS_PROBABILITY + (low ? LOW_MOSS_BONUS : 0F);
			if (random.nextFloat() < moss && !blueprint.isOccupied(pos.below())) {
				blueprint.decorate(pos.below(), TreasureBlocks.SPANISH_MOSS.get().defaultBlockState()
						.setValue(SpanishMossBlock.ACTIVATED, random.nextFloat() < 0.7F));
			}
		}
		// twigs grow only from the trunk (Mark). The tip twig's draw is kept, unused, so every later draw,
		// and so the rest of every tree, comes out as it did; a grove generated half before still joins up
		if (last != null) {
			random.nextFloat();
		}
	}

	private static void twig(WitherTreeBlueprint blueprint, BlockPos pos, Direction d) {
		if (!blueprint.isOccupied(pos)) {
			blueprint.decorate(pos, facing(TreasureBlocks.WITHERWOOD_TWIG.get(), d));
		}
	}

	/**
	 * Suspicious gravel by the trunk, grave thorns round the clearing's edge, now and then a soul lantern
	 * hung on a chain from a branch, and wisps in the air nearby. These draw from their own random, after
	 * everything else, so the rest of the tree is unchanged by them.
	 */
	private static void extras(WitherTreeBlueprint blueprint, RandomSource random, float scale) {
		float edge = Mth.clamp((1F - scale) / 0.35F, 0F, 1F);
		float thorns = THORNS_PROBABILITY + THORNS_EDGE_BONUS * edge;
		Block gravel = Blocks.SUSPICIOUS_GRAVEL;
		blueprint.restatePatch(cell -> {
			int distance = Math.abs(cell.x()) + Math.abs(cell.z());
			boolean packed = cell.ground().is(Blocks.PACKED_MUD);
			if (distance <= SUSPICIOUS_GRAVEL_REACH && random.nextFloat() < SUSPICIOUS_GRAVEL_PROBABILITY) {
				return new WitherTreeBlueprint.PatchCell(cell.x(), cell.z(), gravel.defaultBlockState(), null);
			}
			if (distance >= THORNS_INNER && cell.cover() == null && !packed && random.nextFloat() < thorns) {
				return new WitherTreeBlueprint.PatchCell(cell.x(), cell.z(), cell.ground(), TreasureBlocks.GRAVE_THORNS.get().defaultBlockState());
			}
			return cell;
		});

		if (random.nextFloat() < LANTERN_PROBABILITY) {
			hangLantern(blueprint, random);
		}

		int wisps = random.nextFloat() < WISP_PROBABILITY ? (random.nextFloat() < SECOND_WISP_PROBABILITY ? 2 : 1) : 0;
		for (int i = 0; i < wisps; i++) {
			double angle = random.nextDouble() * Math.PI * 2;
			double distance = 2.5 + random.nextDouble() * 3.5;
			BlockPos pos = new BlockPos((int) Math.round(Math.cos(angle) * distance), 2 + random.nextInt(3), (int) Math.round(Math.sin(angle) * distance));
			if (!blueprint.isOccupied(pos)) {
				blueprint.decorate(pos, TreasureBlocks.WISP.get().defaultBlockState());
			}
		}

		cobwebs(blueprint, random);
		bracketFungi(blueprint, random);
	}

	/**
	 * Cobwebs in the forks: where two branches leave the same trunk block at right angles, a web may fill
	 * the corner cell between them; now and then one sits in the angle above a branch, against the trunk.
	 */
	private static void cobwebs(WitherTreeBlueprint blueprint, RandomSource random) {
		Block branch = TreasureBlocks.WITHERWOOD_BRANCH.get();
		Block log = TreasureBlocks.WITHERWOOD_LOG.get();
		Map<BlockPos, List<Direction>> forks = new java.util.LinkedHashMap<>();
		blueprint.structure().forEach((pos, state) -> {
			if (state.is(branch)) {
				Direction d = state.getValue(IFacingBlock.FACING);
				BlockPos trunk = pos.relative(d.getOpposite());
				if (blueprint.isBlock(trunk, log)) {
					forks.computeIfAbsent(trunk, t -> new ArrayList<>()).add(d);
				}
			}
		});
		int webs = 0;
		for (Map.Entry<BlockPos, List<Direction>> fork : forks.entrySet()) {
			List<Direction> dirs = fork.getValue();
			for (int i = 0; i < dirs.size() && webs < MAX_WEBS; i++) {
				Direction d = dirs.get(i);
				for (int j = i + 1; j < dirs.size() && webs < MAX_WEBS; j++) {
					Direction e = dirs.get(j);
					BlockPos corner = fork.getKey().relative(d).relative(e);
					if (d.getAxis() != e.getAxis() && !blueprint.isOccupied(corner) && random.nextFloat() < FORK_WEB_PROBABILITY) {
						blueprint.decorate(corner, Blocks.COBWEB.defaultBlockState());
						webs++;
					}
				}
				BlockPos above = fork.getKey().relative(d).above();
				if (webs < MAX_WEBS && !blueprint.isOccupied(above) && random.nextFloat() < CORNER_WEB_PROBABILITY) {
					blueprint.decorate(above, Blocks.COBWEB.defaultBlockState());
					webs++;
				}
			}
		}
	}

	/**
	 * Bracket fungus on open faces of the lower trunk: one to three shelves (more often fewer), pointing
	 * away from the log. A face is open when nothing of the tree is in front of it.
	 */
	private static void bracketFungi(WitherTreeBlueprint blueprint, RandomSource random) {
		Block log = TreasureBlocks.WITHERWOOD_LOG.get();
		Map<Long, Integer> heights = new HashMap<>();
		blueprint.structure().forEach((pos, state) -> {
			if (state.is(log)) {
				heights.merge(ChunkPos.asLong(pos.getX(), pos.getZ()), pos.getY(), Math::max);
			}
		});
		List<BlockPos> logs = new ArrayList<>();
		blueprint.structure().forEach((pos, state) -> {
			if (state.is(log) && pos.getY() >= 1
					&& pos.getY() <= Math.max(2, heights.get(ChunkPos.asLong(pos.getX(), pos.getZ())) * FUNGUS_REACH)) {
				logs.add(pos);
			}
		});
		int fungi = 0;
		for (BlockPos pos : logs) {
			for (Direction d : ALL) {
				if (fungi >= MAX_FUNGI) {
					return;
				}
				BlockPos front = pos.relative(d);
				if (!blueprint.isOccupied(front) && random.nextFloat() < FUNGUS_PROBABILITY) {
					float roll = random.nextFloat();
					int count = roll < 0.5F ? 1 : roll < 0.8F ? 2 : 3;
					blueprint.decorate(front, TreasureBlocks.BRACKET_FUNGUS.get().defaultBlockState()
							.setValue(BracketFungusBlock.FACING, d).setValue(BracketFungusBlock.COUNT, count));
					fungi++;
				}
			}
		}
	}

	/**
	 * A soul lantern on one or two links of chain, under a branch with nothing below it.
	 */
	private static void hangLantern(WitherTreeBlueprint blueprint, RandomSource random) {
		Block branch = TreasureBlocks.WITHERWOOD_BRANCH.get();
		List<BlockPos> spots = new ArrayList<>();
		blueprint.structure().forEach((pos, state) -> {
			if (state.is(branch) && pos.getY() >= 5 && !blueprint.isOccupied(pos.below())
					&& !blueprint.isOccupied(pos.below(2)) && !blueprint.isOccupied(pos.below(3))) {
				spots.add(pos);
			}
		});
		if (spots.isEmpty()) {
			return;
		}
		BlockPos pos = spots.get(random.nextInt(spots.size()));
		int links = 1 + random.nextInt(2);
		for (int i = 1; i <= links; i++) {
			blueprint.decorate(pos.below(i), Blocks.CHAIN.defaultBlockState());
		}
		blueprint.decorate(pos.below(links + 1), Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
	}

	/**
	 * A strangle vine wound round some single trunks: one strand climbing from the ground, a face further
	 * round each block, to somewhere between 40 and 90 % of the way up; a quarter of them choked at the
	 * foot, with the vine on every face for the lowest one to three blocks. Runs last, on its own random,
	 * and only re-skins logs, so nothing else about the tree changes. (Great trees' outer columns would
	 * hide the strand on their inner faces; they are left for now.)
	 */
	private static void strangle(WitherTreeBlueprint blueprint, RandomSource random) {
		if (random.nextFloat() >= STRANGLED_PROBABILITY) {
			return;
		}
		Block log = TreasureBlocks.WITHERWOOD_LOG.get();
		int height = 0;
		for (int y = 1; blueprint.isBlock(new BlockPos(0, y, 0), log); y++) {
			height = y;
		}
		if (height == 0) {
			return;
		}
		int start = random.nextInt(4);
		int reach = Math.max(2, Math.round(height * (0.4F + random.nextFloat() * 0.5F)));
		int choked = random.nextFloat() < CHOKED_PROBABILITY ? 1 + random.nextInt(3) : 0;
		StrangledWitherwoodLogBlock strangled = (StrangledWitherwoodLogBlock) TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get();
		blueprint.restateStructure((pos, state) -> {
			if (pos.getX() != 0 || pos.getZ() != 0 || !state.is(log) || pos.getY() > reach) {
				return state;
			}
			return strangled.climbing(start, pos.getY())
					.setValue(StrangledWitherwoodLogBlock.FULL, pos.getY() <= choked)
					.setValue(WitherwoodRotatedPillarBlock.MOSSY, state.getValue(WitherwoodRotatedPillarBlock.MOSSY));
		});
	}

	/**
	 * Grows moss on the logs and broken tops (see MOSS_LINE_MIN). Each choice is a hash of the tree's
	 * seed and the cell, not a draw from the tree's random, so the rest of the tree comes out the same
	 * as it did before moss was chosen here.
	 */
	private static void moss(WitherTreeBlueprint blueprint, long seed) {
		Block log = TreasureBlocks.WITHERWOOD_LOG.get();
		Block top = TreasureBlocks.WITHERWOOD_BROKEN_LOG.get();
		// each column's highest log
		Map<Long, Integer> heights = new HashMap<>();
		blueprint.structure().forEach((pos, state) -> {
			if (state.is(log)) {
				heights.merge(ChunkPos.asLong(pos.getX(), pos.getZ()), pos.getY(), Math::max);
			}
		});
		blueprint.restate((pos, state) -> {
			boolean isTop = state.is(top);
			if (!state.is(log) && !isTop) {
				return state;
			}
			float chance;
			if (isTop) {
				chance = MOSS_TOP;
			}
			else {
				int height = heights.getOrDefault(ChunkPos.asLong(pos.getX(), pos.getZ()), pos.getY());
				int line = Math.round(height * (MOSS_LINE_MIN + MOSS_LINE_RANGE * hash(seed, pos.getX(), 0, pos.getZ())));
				chance = pos.getY() <= line ? MOSS_BELOW_LINE : MOSS_ABOVE_LINE;
			}
			return state.setValue(WitherwoodRotatedPillarBlock.MOSSY, hash(seed, pos.getX(), pos.getY(), pos.getZ()) < chance);
		});
	}

	/** a float in [0, 1) from the tree's seed and a cell */
	private static float hash(long seed, int x, int y, int z) {
		return RandomSource.create(seed ^ Mth.getSeed(x, y, z)).nextFloat();
	}

	/**
	 * Strangle vines standing on the ground 3 to 7 blocks from the trunk, each as tall as a bonemealed
	 * twisting vine grows (capped at the strangle vine's own maximum).
	 */
	private static void strangleVines(WitherTreeBlueprint blueprint, RandomSource random) {
		for (int i = 0; i < STRANGLE_VINE_ATTEMPTS; i++) {
			double angle = random.nextDouble() * Math.PI * 2;
			double distance = 3 + random.nextDouble() * (CLEARING_RADIUS - 3);
			int x = (int) Math.round(Math.cos(angle) * distance);
			int z = (int) Math.round(Math.sin(angle) * distance);
			int height = 1;
			while (height < STRANGLE_VINES_MAX_HEIGHT && random.nextDouble() < 0.826) {
				height++;
			}
			blueprint.vines(x, z, height);
		}
	}

	/**
	 * The ground in a diamond around the tree: dirt and withered grass, with coarse dirt, podzol,
	 * mycelium and mud mixed in, and withered leaves lying on a quarter of it.
	 */
	private static void clearing(WitherTreeBlueprint blueprint, RandomSource random) {
		for (int x = -CLEARING_RADIUS; x <= CLEARING_RADIUS; x++) {
			for (int z = -CLEARING_RADIUS; z <= CLEARING_RADIUS; z++) {
				if (Math.abs(x) + Math.abs(z) > CLEARING_RADIUS || blueprint.isStructure(new BlockPos(x, 1, z))) {
					continue;
				}
				if (random.nextFloat() >= GROUND_REPLACEMENT_PROBABILITY) {
					continue;
				}
				Block ground = switch (random.nextInt(10)) {
					case 0 -> Blocks.MYCELIUM;
					case 1 -> Blocks.PODZOL;
					case 2 -> Blocks.MUD;
					case 3 -> Blocks.PACKED_MUD;
					case 4, 5 -> Blocks.COARSE_DIRT;
					case 6, 7 -> TreasureBlocks.WITHERED_GRASS_BLOCK.get();
					default -> Blocks.DIRT;
				};
				BlockState cover = null;
				if (ground != Blocks.PACKED_MUD && random.nextFloat() < LEAF_LITTER_PROBABILITY) {
					cover = TreasureBlocks.WITHERED_LEAF_LITTER.get().defaultBlockState()
							.setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
							.setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(4));
				}
				blueprint.patch(x, z, ground.defaultBlockState(), cover);
			}
		}
	}

	private static int scaled(int size, float scale) {
		return Math.max(MIN_SCALED_SIZE, Math.round(size * Math.min(1F, scale)));
	}

	/** inclusive of both ends */
	private static int between(RandomSource random, int min, int max) {
		return max <= min ? min : min + random.nextInt(max - min + 1);
	}

	private static BlockState log() {
		return TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState();
	}

	private static BlockState facing(Block block, Direction direction) {
		return block.defaultBlockState().setValue(IFacingBlock.FACING, direction);
	}

	static Block spanishMoss() {
		return TreasureBlocks.SPANISH_MOSS.get();
	}
}
