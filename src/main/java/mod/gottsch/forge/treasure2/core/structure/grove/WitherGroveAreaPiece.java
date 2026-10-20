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
 * An empty piece whose box covers the whole area the grove paints with its biome.
 * <p>
 * A structure's afterPlace (where the biome is painted) only runs for chunks its bounding box reaches,
 * and that box is the union of its pieces. Where no grove trees grow (on a side rejected for water,
 * say), the box would stop short of the painted circle, those chunks would never be painted, and the
 * host biome's trees would grow there. This piece writes nothing and is never bearded; its box is its
 * whole job (see the worldgen patterns, section 3).
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitherGroveAreaPiece extends StructurePiece implements PieceBeardifierModifier {

	public WitherGroveAreaPiece(BoundingBox area) {
		super(TreasureStructures.WITHER_GROVE_AREA_PIECE.get(), 0, area);
	}

	public WitherGroveAreaPiece(CompoundTag tag) {
		super(TreasureStructures.WITHER_GROVE_AREA_PIECE.get(), tag);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		// the box, saved by StructurePiece, is all there is
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
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
