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
package mod.gottsch.forge.treasure2.core.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hangs roots from a piece's ceilings as it is placed: an air cell of the template directly under a
 * ceiling block becomes hanging roots, at the given probability. A ceiling is a block in the ceilings
 * tag (earth, wood, mud brick by default) with a solid underside. Under the template's own blocks the
 * test reads the template; under its top layer, where the piece is open above, it reads the ground
 * above, which is in the same column. The choice is seeded by position, so a piece comes out the same
 * from whichever chunk places it.
 * <p>
 * JSON: {"processor_type": "treasure2:hanging_roots_processor", "probability": 0.2, "ceilings": "#treasure2:hanging_roots_ceilings"};
 * both fields are optional.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class HangingRootsProcessor extends StructureProcessor {
	public static final Codec<HangingRootsProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.floatRange(0F, 1F).optionalFieldOf("probability", 0.2F).forGetter(p -> p.probability),
			TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("ceilings", TreasureTags.Blocks.HANGING_ROOTS_CEILINGS).forGetter(p -> p.ceilings)
	).apply(instance, HangingRootsProcessor::new));

	private final float probability;
	private final TagKey<Block> ceilings;

	public HangingRootsProcessor(float probability, TagKey<Block> ceilings) {
		this.probability = probability;
		this.ceilings = ceilings;
	}

	@Override
	public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor level, BlockPos offset, BlockPos pos,
			List<StructureTemplate.StructureBlockInfo> originalBlocks, List<StructureTemplate.StructureBlockInfo> processedBlocks,
			StructurePlaceSettings settings) {
		Map<BlockPos, BlockState> template = new HashMap<>();
		for (StructureTemplate.StructureBlockInfo info : processedBlocks) {
			template.put(info.pos(), info.state());
		}
		List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>(processedBlocks.size());
		for (StructureTemplate.StructureBlockInfo info : processedBlocks) {
			if (info.state().isAir()) {
				BlockPos above = info.pos().above();
				BlockState ceiling = template.containsKey(above) ? template.get(above) : level.getBlockState(above);
				if (isCeiling(level, above, ceiling) && chance(level, info.pos()) < probability) {
					result.add(new StructureTemplate.StructureBlockInfo(info.pos(), Blocks.HANGING_ROOTS.defaultBlockState(), null));
					continue;
				}
			}
			result.add(info);
		}
		return result;
	}

	/** the world seed mixed with the cell, so the draw depends on nothing but where the cell is */
	private static float chance(ServerLevelAccessor level, BlockPos pos) {
		return RandomSource.create(Mth.getSeed(pos) ^ level.getLevel().getSeed()).nextFloat();
	}

	private boolean isCeiling(ServerLevelAccessor level, BlockPos pos, BlockState state) {
		return state.is(ceilings) && state.isFaceSturdy(level, pos, Direction.DOWN);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return ModProcessors.HANGING_ROOTS_PROCESSOR.get();
	}
}
