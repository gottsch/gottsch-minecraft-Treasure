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

import mod.gottsch.forge.treasure2.core.structure.TreasureStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraftforge.common.world.PieceBeardifierModifier;

/**
 * One procedural tree in a Wither Grove.
 * <p>
 * Only the seed, archetype, scale and ground position are saved. The tree is rebuilt from them each
 * time postProcess runs (once per chunk it touches), so every chunk sees the same tree, and is seated
 * on the real ground found in its trunk column (see WitherTreeBlueprint#seat). Opts out of
 * the structure's beard: the main tree keeps beard_thin, the grove trees sit on the ground as it is.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitherTreePiece extends StructurePiece implements PieceBeardifierModifier {
	private static final String ORIGIN = "Origin";
	private static final String SEED = "Seed";
	private static final String ARCHETYPE = "Archetype";
	private static final String SCALE = "Scale";

	/** the ground block under the trunk */
	private final BlockPos origin;
	private final long seed;
	private final WitherTreeArchetype archetype;
	/** 1 for full size; smaller toward the grove's edge */
	private final float scale;

	public WitherTreePiece(BlockPos origin, long seed, WitherTreeArchetype archetype, float scale) {
		super(TreasureStructures.WITHER_TREE_PIECE.get(), 0, WitherTreeBuilder.build(seed, archetype, scale).bounds(origin));
		this.origin = origin;
		this.seed = seed;
		this.archetype = archetype;
		this.scale = scale;
	}

	public WitherTreePiece(CompoundTag tag) {
		super(TreasureStructures.WITHER_TREE_PIECE.get(), tag);
		this.origin = NbtUtils.readBlockPos(tag.getCompound(ORIGIN));
		this.seed = tag.getLong(SEED);
		this.archetype = WitherTreeArchetype.byName(tag.getString(ARCHETYPE));
		this.scale = tag.getFloat(SCALE);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.put(ORIGIN, NbtUtils.writeBlockPos(origin));
		tag.putLong(SEED, seed);
		tag.putString(ARCHETYPE, archetype.name());
		tag.putFloat(SCALE, scale);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
		BlockPos ground = WitherTreeBlueprint.seat(level, origin);
		if (ground != null) {
			WitherTreeBuilder.build(seed, archetype, scale).place(level, ground, chunkBox);
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
