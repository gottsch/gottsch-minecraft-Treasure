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
import mod.gottsch.forge.treasure2.core.block.WitherwoodRotatedPillarBlock;
import mod.gottsch.forge.treasure2.core.structure.TreasureStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraftforge.common.world.PieceBeardifierModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A fallen, hollow witherwood log lying on a Wither Grove's floor, sometimes with the hollow stump it
 * snapped from at one end.
 * <p>
 * Saved: the highest ground under the log, the direction it lies, its length and a seed. The grove only
 * lays a log out where the generator's ground is level to within one block along its whole length. At
 * placement each column still seats itself: the log lies level one above the saved ground, bridging a
 * dip of one, and a column whose real ground is higher, or more than one lower, is left out. Every
 * column reads only itself. No beard.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class WitherFallenLogPiece extends StructurePiece implements PieceBeardifierModifier {
	private static final String ORIGIN = "Origin";
	private static final String DIRECTION = "Direction";
	private static final String LENGTH = "Length";
	private static final String SEED = "Seed";

	public static final int MIN_LENGTH = 3;
	public static final int MAX_LENGTH = 6;
	private static final float MOSSY_PROBABILITY = 0.4F;
	private static final float LEAF_LITTER_PROBABILITY = 0.2F;
	private static final float STUMP_PROBABILITY = 0.5F;
	private static final int FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

	/** the ground block under the first log cell */
	private final BlockPos origin;
	/** the way the log lies, from its first cell */
	private final Direction direction;
	private final int length;
	private final long seed;

	/** one cell: offset along the log (-1 is the stump, a gap before the log), the block and what lies on it */
	private record Cell(int step, BlockState[] stack, BlockState cover) {}

	public WitherFallenLogPiece(BlockPos origin, Direction direction, int length, long seed) {
		super(TreasureStructures.WITHER_FALLEN_LOG_PIECE.get(), 0, bounds(origin, direction, length));
		this.origin = origin;
		this.direction = direction;
		this.length = length;
		this.seed = seed;
	}

	public WitherFallenLogPiece(CompoundTag tag) {
		super(TreasureStructures.WITHER_FALLEN_LOG_PIECE.get(), tag);
		this.origin = NbtUtils.readBlockPos(tag.getCompound(ORIGIN));
		this.direction = Direction.from3DDataValue(tag.getInt(DIRECTION));
		this.length = tag.getInt(LENGTH);
		this.seed = tag.getLong(SEED);
	}

	private static BoundingBox bounds(BlockPos origin, Direction direction, int length) {
		// from the stump (two back) to the last cell, plus room for the seat scan and the stump's height
		BlockPos stump = origin.relative(direction, -2);
		BlockPos end = origin.relative(direction, length - 1);
		return new BoundingBox(Math.min(stump.getX(), end.getX()), origin.getY() - WitherTreeBlueprint.SEAT_DEPTH - 1, Math.min(stump.getZ(), end.getZ()),
				Math.max(stump.getX(), end.getX()), origin.getY() + WitherTreeBlueprint.SEAT_RISE + 4, Math.max(stump.getZ(), end.getZ()));
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.put(ORIGIN, NbtUtils.writeBlockPos(origin));
		tag.putInt(DIRECTION, direction.get3DDataValue());
		tag.putInt(LENGTH, length);
		tag.putLong(SEED, seed);
	}

	private List<Cell> cells() {
		RandomSource random = new XoroshiroRandomSource(seed);
		List<Cell> cells = new ArrayList<>();
		BlockState hollow = TreasureBlocks.HOLLOW_WITHERWOOD_LOG.get().defaultBlockState();
		for (int i = 0; i < length; i++) {
			BlockState log = hollow.setValue(RotatedPillarBlock.AXIS, direction.getAxis())
					.setValue(WitherwoodRotatedPillarBlock.MOSSY, random.nextFloat() < MOSSY_PROBABILITY);
			BlockState cover = random.nextFloat() < LEAF_LITTER_PROBABILITY
					? TreasureBlocks.WITHERED_LEAF_LITTER.get().defaultBlockState()
							.setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
							.setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(3))
					: null;
			cells.add(new Cell(i, new BlockState[] {log}, cover));
		}
		if (random.nextFloat() < STUMP_PROBABILITY) {
			int height = 1 + random.nextInt(2);
			BlockState[] stump = new BlockState[height];
			for (int h = 0; h < height; h++) {
				stump[h] = hollow.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
						.setValue(WitherwoodRotatedPillarBlock.MOSSY, h == 0 || random.nextBoolean());
			}
			// one gap between the stump and where the log broke away
			cells.add(new Cell(-2, stump, null));
		}
		return cells;
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
		for (Cell cell : cells()) {
			BlockPos column = origin.relative(direction, cell.step());
			if (!chunkBox.isInside(new BlockPos(column.getX(), chunkBox.minY(), column.getZ()))) {
				continue;
			}
			BlockPos ground = WitherTreeBlueprint.seat(level, column);
			// the log lies level, one above the origin's ground, bridging a dip of one; the stump stands on its own ground
			if (ground == null || ground.getY() > origin.getY() || ground.getY() < origin.getY() - 1
					|| !level.getBlockState(ground.above()).getFluidState().isEmpty()) {
				continue;
			}
			if (cell.step() >= 0) {
				ground = new BlockPos(ground.getX(), origin.getY(), ground.getZ());
			}
			BlockState[] stack = cell.stack();
			int placed = 0;
			for (int h = 0; h < stack.length; h++) {
				BlockPos pos = ground.above(1 + h);
				BlockState existing = level.getBlockState(pos);
				// small plants give way, the trees' leaf litter and thorns among them
				if (!existing.isAir() && !existing.canBeReplaced() && !(existing.getBlock() instanceof BushBlock)) {
					break;
				}
				level.setBlock(pos, stack[h], FLAGS);
				placed++;
			}
			if (cell.cover() != null && placed == stack.length) {
				BlockPos above = ground.above(1 + stack.length);
				if (level.getBlockState(above).isAir()) {
					level.setBlock(above, cell.cover(), FLAGS);
				}
			}
		}
	}

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
