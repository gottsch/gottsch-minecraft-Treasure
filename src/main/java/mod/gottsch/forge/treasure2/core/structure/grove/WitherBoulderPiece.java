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

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.structure.TreasureStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraftforge.common.world.PieceBeardifierModifier;

import java.util.HashMap;
import java.util.Map;

/**
 * A small boulder half sunk in a Wither Grove's floor: mossy cobblestone, or cobbled deepslate with
 * tuff, its rim softened with stairs and slabs.
 * <p>
 * Like a grove tree, only the ground position and a seed are saved, and the shape is rebuilt from the
 * seed each time postProcess runs. Each column of the boulder finds its own ground, in its own column,
 * so the boulder settles over a slope and every chunk places its share the same way. No beard.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitherBoulderPiece extends StructurePiece implements PieceBeardifierModifier {
	private static final String ORIGIN = "Origin";
	private static final String SEED = "Seed";
	/** the furthest a column reaches from the boulder's centre */
	private static final int REACH = 3;
	private static final int MAX_HEIGHT = 3;
	/** a column's ground may be this far above or below the boulder's layout height */
	private static final int RELIEF = 4;
	private static final float SLAB_PROBABILITY = 0.6F;
	private static final float LEAF_LITTER_PROBABILITY = 0.15F;

	private final BlockPos origin;
	private final long seed;

	private enum Stone {
		MOSSY(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE, 0.25F, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_SLAB),
		DEEPSLATE(Blocks.COBBLED_DEEPSLATE, Blocks.TUFF, 0.35F, Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE_SLAB);

		final Block main;
		final Block mix;
		final float mixProbability;
		final Block stairs;
		final Block slab;

		Stone(Block main, Block mix, float mixProbability, Block stairs, Block slab) {
			this.main = main;
			this.mix = mix;
			this.mixProbability = mixProbability;
			this.stairs = stairs;
			this.slab = slab;
		}
	}

	/**
	 * One column of the boulder.
	 * @param height blocks above the ground; 0 is a stone flush with the ground
	 * @param cells from the ground block (buried) up; cells[0] replaces the ground itself
	 * @param cover what lies on top, or null
	 */
	private record Column(int height, BlockState[] cells, BlockState cover) {}

	public WitherBoulderPiece(BlockPos origin, long seed) {
		super(TreasureStructures.WITHER_BOULDER_PIECE.get(), 0, bounds(origin));
		this.origin = origin;
		this.seed = seed;
	}

	public WitherBoulderPiece(CompoundTag tag) {
		super(TreasureStructures.WITHER_BOULDER_PIECE.get(), tag);
		this.origin = NbtUtils.readBlockPos(tag.getCompound(ORIGIN));
		this.seed = tag.getLong(SEED);
	}

	private static BoundingBox bounds(BlockPos origin) {
		return new BoundingBox(origin.getX() - REACH, origin.getY() - WitherTreeBlueprint.SEAT_DEPTH - RELIEF - 1, origin.getZ() - REACH,
				origin.getX() + REACH, origin.getY() + WitherTreeBlueprint.SEAT_RISE + RELIEF + MAX_HEIGHT + 1, origin.getZ() + REACH);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.put(ORIGIN, NbtUtils.writeBlockPos(origin));
		tag.putLong(SEED, seed);
	}

	/**
	 * The boulder's columns, keyed by offset from its centre: a squashed, wobbly round dome. A top that stands above a lower neighbour on one side only becomes a stair rising toward
	 * the higher side; a lone 1-high top often becomes a slab.
	 */
	static Map<Long, Column> shape(long seed) {
		// Xoroshiro mixes its seed, so neighbouring seeds don't give near-identical first draws as the legacy random does
		RandomSource random = new XoroshiroRandomSource(seed);
		Stone stone = random.nextFloat() < 0.55F ? Stone.MOSSY : Stone.DEEPSLATE;
		double rx = 1.4 + random.nextDouble() * 1.4;
		double rz = 1.4 + random.nextDouble() * 1.4;
		int peak = 1 + random.nextInt(MAX_HEIGHT);

		Map<Long, Integer> heights = new HashMap<>();
		for (int dx = -REACH; dx <= REACH; dx++) {
			for (int dz = -REACH; dz <= REACH; dz++) {
				double d = Math.sqrt((dx / rx) * (dx / rx) + (dz / rz) * (dz / rz)) + (random.nextDouble() - 0.5) * 0.5;
				if (d <= 1.0) {
					// a round dome, not a cone: full height well out from the centre
					heights.put(ChunkPos.asLong(dx, dz), (int) Math.round(peak * Math.sqrt(Math.max(0, 1 - d * d)) - 0.2));
				}
			}
		}

		Map<Long, Column> columns = new HashMap<>();
		for (int dx = -REACH; dx <= REACH; dx++) {
			for (int dz = -REACH; dz <= REACH; dz++) {
				Integer height = heights.get(ChunkPos.asLong(dx, dz));
				if (height == null) {
					continue;
				}
				// buried: the ground block, and one below it under anything that stands up
				BlockState[] cells = new BlockState[height + 2];
				for (int i = 0; i < cells.length; i++) {
					cells[i] = (random.nextFloat() < stone.mixProbability ? stone.mix : stone.main).defaultBlockState();
				}
				BlockState cover = null;
				if (height > 0) {
					int top = height + 1;
					Direction rise = null;
					int highest = height;
					for (Direction d : Direction.Plane.HORIZONTAL) {
						int neighbour = heights.getOrDefault(ChunkPos.asLong(dx + d.getStepX(), dz + d.getStepZ()), -1);
						if (neighbour > highest) {
							highest = neighbour;
							rise = d;
						}
					}
					if (rise != null) {
						cells[top] = stone.stairs.defaultBlockState().setValue(StairBlock.FACING, rise).setValue(StairBlock.HALF, Half.BOTTOM);
					}
					else if (height == 1 && random.nextFloat() < SLAB_PROBABILITY) {
						cells[top] = stone.slab.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM);
					}
					else if (random.nextFloat() < LEAF_LITTER_PROBABILITY) {
						cover = TreasureBlocks.WITHERED_LEAF_LITTER.get().defaultBlockState()
								.setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
								.setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(3));
					}
				}
				columns.put(ChunkPos.asLong(dx, dz), new Column(height, height > 0 ? cells : new BlockState[] {cells[0]}, cover));
			}
		}
		return columns;
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
		shape(seed).forEach((key, column) -> {
			int x = origin.getX() + ChunkPos.getX(key);
			int z = origin.getZ() + ChunkPos.getZ(key);
			if (x < chunkBox.minX() || x > chunkBox.maxX() || z < chunkBox.minZ() || z > chunkBox.maxZ()) {
				return;
			}
			// each column seats itself from the layout height, so nothing outside its own column is read
			BlockPos ground = WitherTreeBlueprint.seat(level, new BlockPos(x, origin.getY(), z));
			if (ground == null || Math.abs(ground.getY() - origin.getY()) > RELIEF
					|| !level.getBlockState(ground.above()).getFluidState().isEmpty()) {
				return;
			}
			// cells[0] is the ground block; under a standing column, cells[1] goes one below it
			BlockState[] cells = column.cells();
			if (column.height() > 0) {
				level.setBlock(ground.below(), cells[1], FLAGS);
			}
			level.setBlock(ground, cells[0], FLAGS);
			for (int i = 1; i <= column.height(); i++) {
				BlockPos pos = ground.above(i);
				BlockState existing = level.getBlockState(pos);
				// small plants give way, the trees' leaf litter and thorns among them
				if (!existing.isAir() && !existing.canBeReplaced() && !(existing.getBlock() instanceof BushBlock)) {
					return;
				}
				level.setBlock(pos, cells[i + 1], FLAGS);
			}
			// shape() only gives a cover to a standing column whose top is a full block
			if (column.cover() != null) {
				BlockPos above = ground.above(column.height() + 1);
				if (level.getBlockState(above).isAir()) {
					level.setBlock(above, column.cover(), FLAGS);
				}
			}
		});
	}

	/** as the grove trees: no neighbour shape updates, since a neighbour may be in a chunk not generated yet */
	private static final int FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

	@Override
	public BoundingBox getBeardifierBox() {
		return boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}
}
