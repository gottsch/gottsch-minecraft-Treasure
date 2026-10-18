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

import mod.gottsch.forge.treasure2.core.block.ITreasureBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.WitherRootBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Everything one wither tree places, in coordinates relative to the ground block under its trunk
 * (y = 0 is the ground, y = 1 the first log).
 * <p>
 * WitherTreePiece rebuilds this from its saved seed every time postProcess runs, once per chunk the tree
 * touches, and {@link #place} writes only the cells inside that chunk. The only world reads made while
 * placing are in the column being written, so the result does not depend on which chunk generates first.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitherTreeBlueprint {
	/** how far a trunk column is extended down to reach the ground */
	public static final int SINK_DEPTH = 6;
	/** the clearing only changes ground less than this far above or below the tree's ground */
	public static final int CLEARING_RELIEF = 3;
	/** how far a strangle vine's ground may be above or below the tree's ground */
	public static final int VINE_RELIEF = 6;
	/** how far above and below its laid-out ground a tree looks for the real ground (see {@link #seat}) */
	public static final int SEAT_RISE = 6;
	public static final int SEAT_DEPTH = 10;
	/** no neighbour shape updates: a neighbour may be in a chunk not generated yet */
	private static final int FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

	/** trunk, branches and tops; replace anything but block entities and bedrock */
	private final Map<BlockPos, BlockState> structure = new LinkedHashMap<>();
	/** trunk base cells; the column below each is filled with the state until it meets the ground */
	private final Map<BlockPos, BlockState> sinks = new LinkedHashMap<>();
	/** roots, moss, twigs; only placed into air or replaceable plants */
	private final Map<BlockPos, BlockState> decorations = new LinkedHashMap<>();
	/** ground cover; the column's surface is found at placement time */
	private final List<PatchCell> patch = new ArrayList<>();
	/** strangle vines standing on the ground; the column's surface is found at placement time */
	private final List<VineColumn> vines = new ArrayList<>();

	public record PatchCell(int x, int z, BlockState ground, boolean deadBush) {}

	public record VineColumn(int x, int z, int height) {}

	public void structure(BlockPos pos, BlockState state) {
		structure.put(pos, state);
	}

	public void sink(BlockPos pos, BlockState state) {
		sinks.put(pos, state);
	}

	public void decorate(BlockPos pos, BlockState state) {
		decorations.putIfAbsent(pos, state);
	}

	public void patch(int x, int z, BlockState ground, boolean deadBush) {
		patch.add(new PatchCell(x, z, ground, deadBush));
	}

	public void vines(int x, int z, int height) {
		vines.add(new VineColumn(x, z, height));
	}

	/** true if the trunk, a branch or a decoration already uses the cell */
	public boolean isOccupied(BlockPos pos) {
		return structure.containsKey(pos) || decorations.containsKey(pos);
	}

	public boolean isStructure(BlockPos pos) {
		return structure.containsKey(pos);
	}

	public boolean isBlock(BlockPos pos, Block block) {
		BlockState state = structure.get(pos);
		return state != null && state.is(block);
	}

	/**
	 * The world-space box of everything this tree can write when its ground is at origin.
	 */
	public BoundingBox bounds(BlockPos origin) {
		int minX = 0, minY = -SINK_DEPTH - SEAT_DEPTH, minZ = 0, maxX = 0, maxY = VINE_RELIEF + 1, maxZ = 0;
		List<BlockPos> cells = new ArrayList<>(structure.keySet());
		cells.addAll(decorations.keySet());
		for (BlockPos p : cells) {
			minX = Math.min(minX, p.getX()); maxX = Math.max(maxX, p.getX());
			maxY = Math.max(maxY, p.getY());
			minZ = Math.min(minZ, p.getZ()); maxZ = Math.max(maxZ, p.getZ());
		}
		for (PatchCell c : patch) {
			minX = Math.min(minX, c.x()); maxX = Math.max(maxX, c.x());
			minZ = Math.min(minZ, c.z()); maxZ = Math.max(maxZ, c.z());
		}
		for (VineColumn v : vines) {
			minX = Math.min(minX, v.x()); maxX = Math.max(maxX, v.x());
			minZ = Math.min(minZ, v.z()); maxZ = Math.max(maxZ, v.z());
			maxY = Math.max(maxY, VINE_RELIEF + v.height());
		}
		return new BoundingBox(origin.getX() + minX, origin.getY() + minY, origin.getZ() + minZ,
				origin.getX() + maxX, origin.getY() + maxY + SEAT_RISE, origin.getZ() + maxZ);
	}

	/**
	 * The real ground under a tree laid out at origin, or null if there is none near.
	 * <p>
	 * A grove lays its trees out from the generator's noise height, which knows nothing of the main
	 * tree's beard or of carvers; both can move the ground by several blocks. This scans the trunk's own
	 * column, from SEAT_RISE above to SEAT_DEPTH below, for the first terrain block with open space
	 * above it. Logs, leaves, block entities and Treasure blocks are skipped, so another tree (or this
	 * one, placed from a neighbouring chunk) never changes the answer, and every chunk of the tree
	 * seats it in the same place.
	 */
	public static BlockPos seat(WorldGenLevel level, BlockPos origin) {
		for (int dy = SEAT_RISE; dy >= -SEAT_DEPTH; dy--) {
			BlockPos pos = origin.above(dy);
			if (isTerrain(level.getBlockState(pos)) && !isTerrain(level.getBlockState(pos.above()))) {
				return pos;
			}
		}
		return null;
	}

	private static boolean isTerrain(BlockState state) {
		return !isPassable(state) && !state.hasBlockEntity() && !(state.getBlock() instanceof ITreasureBlock)
				&& !state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES);
	}

	/**
	 * Writes the cells of this tree that fall inside chunkBox.
	 */
	public void place(WorldGenLevel level, BlockPos origin, BoundingBox chunkBox) {
		placeClearing(level, origin, chunkBox);

		structure.forEach((rel, state) -> {
			BlockPos pos = origin.offset(rel);
			if (chunkBox.isInside(pos) && canOverwrite(level.getBlockState(pos))) {
				level.setBlock(pos, state, FLAGS);
			}
		});

		sinks.forEach((rel, state) -> {
			BlockPos pos = origin.offset(rel);
			if (!chunkBox.isInside(pos)) {
				return;
			}
			for (int i = 1; i <= SINK_DEPTH; i++) {
				BlockPos below = pos.below(i);
				if (!isPassable(level.getBlockState(below))) {
					break;
				}
				level.setBlock(below, state, FLAGS);
			}
		});

		placeVines(level, origin, chunkBox);

		// bottom up, so whatever a decoration stands on is placed first
		decorations.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().getY()))
				.forEach(e -> {
					BlockPos pos = origin.offset(e.getKey());
					BlockState state = e.getValue();
					if (chunkBox.isInside(pos) && canDecorate(level.getBlockState(pos)) && isSupported(level, pos, state)) {
						level.setBlock(pos, state, FLAGS);
					}
				});
	}

	private void placeClearing(WorldGenLevel level, BlockPos origin, BoundingBox chunkBox) {
		for (PatchCell cell : patch) {
			BlockPos ground = surface(level, origin, cell.x(), cell.z(), chunkBox, CLEARING_RELIEF - 1);
			if (ground == null) {
				continue;
			}
			level.setBlock(ground, cell.ground(), FLAGS);
			BlockPos above = ground.above();
			BlockState aboveState = level.getBlockState(above);
			if (!aboveState.isAir() && aboveState.canBeReplaced() && aboveState.getFluidState().isEmpty()) {
				// the host biome's grass and flowers
				level.setBlock(above, Blocks.AIR.defaultBlockState(), FLAGS);
			}
			if (cell.deadBush()) {
				level.setBlock(above, Blocks.DEAD_BUSH.defaultBlockState(), FLAGS);
			}
		}
	}

	private void placeVines(WorldGenLevel level, BlockPos origin, BoundingBox chunkBox) {
		for (VineColumn vine : vines) {
			BlockPos ground = surface(level, origin, vine.x(), vine.z(), chunkBox, VINE_RELIEF);
			if (ground == null) {
				continue;
			}
			for (int i = 1; i <= vine.height(); i++) {
				BlockPos pos = ground.above(i);
				if (!chunkBox.isInside(pos) || !canDecorate(level.getBlockState(pos))) {
					break;
				}
				BlockState state = i == vine.height()
						? TreasureBlocks.STRANGLE_VINES.get().defaultBlockState()
						: TreasureBlocks.STRANGLE_VINES_PLANT.get().defaultBlockState();
				level.setBlock(pos, state, FLAGS);
			}
		}
	}

	/**
	 * The solid, dry ground block at the top of a column, if the column is in this chunk and its ground
	 * is within relief of the tree's.
	 */
	private static BlockPos surface(WorldGenLevel level, BlockPos origin, int dx, int dz, BoundingBox chunkBox, int relief) {
		int x = origin.getX() + dx;
		int z = origin.getZ() + dz;
		if (x < chunkBox.minX() || x > chunkBox.maxX() || z < chunkBox.minZ() || z > chunkBox.maxZ()) {
			return null;
		}
		int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
		if (Math.abs(top - origin.getY()) > relief) {
			return null;
		}
		BlockPos ground = new BlockPos(x, top, z);
		BlockState state = level.getBlockState(ground);
		if (isPassable(state) || state.hasBlockEntity() || state.getBlock() instanceof ITreasureBlock || state.is(BlockTags.LEAVES)) {
			return null;
		}
		return ground;
	}

	private static boolean canOverwrite(BlockState existing) {
		return !existing.hasBlockEntity() && !existing.is(Blocks.BEDROCK);
	}

	private static boolean isPassable(BlockState state) {
		return state.isAir() || state.canBeReplaced() || !state.getFluidState().isEmpty() || state.is(BlockTags.LEAVES);
	}

	private static boolean canDecorate(BlockState existing) {
		return existing.isAir() || (existing.canBeReplaced() && existing.getFluidState().isEmpty());
	}

	/**
	 * Vertical support only, which is always in the same column. Horizontal support (a twig on a
	 * branch) comes from the blueprint, because the neighbour may be in a chunk not generated yet.
	 */
	private static boolean isSupported(WorldGenLevel level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof WitherRootBlock) {
			BlockPos below = pos.below();
			return !isPassable(level.getBlockState(below));
		}
		if (state.getBlock() == WitherTreeBuilder.spanishMoss()) {
			return !level.getBlockState(pos.above()).isAir();
		}
		return true;
	}
}
