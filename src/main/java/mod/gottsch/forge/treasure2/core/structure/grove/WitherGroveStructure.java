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

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.gottsch.forge.treasure2.core.structure.ModJigsawStructure;
import mod.gottsch.forge.treasure2.core.structure.TreasureStructures;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A Wither Grove: the jigsaw-built main wither tree (with its pit and golem pad) as the anchor, and a
 * ring of procedural {@link WitherTreePiece}s scattered around it.
 * <p>
 * The jigsaw pieces go into the builder first, so piece 0 is still the main tree (vanilla takes the
 * pivot it hands to every piece's postProcess from piece 0).
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class WitherGroveStructure extends ModJigsawStructure {

	/**
	 * Grove layout, from the structure JSON's "grove" object. Every field is required, so a misspelled
	 * key fails to load instead of falling back to a default.
	 * @param minTrees the fewest supporting trees to try for
	 * @param maxTrees the most supporting trees
	 * @param radius how far trees reach from the main tree
	 * @param spacing the least distance between two trees
	 * @param clearance the radius around the main tree kept free of grove trees
	 * @param maxRelief how far a tree's ground may be above or below the main tree's and still grow there
	 */
	public record GroveSettings(int minTrees, int maxTrees, int radius, int spacing, int clearance, int maxRelief) {
		public static final Codec<GroveSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.intRange(0, 40).fieldOf("min_trees").forGetter(GroveSettings::minTrees),
				Codec.intRange(0, 40).fieldOf("max_trees").forGetter(GroveSettings::maxTrees),
				Codec.intRange(8, 48).fieldOf("radius").forGetter(GroveSettings::radius),
				Codec.intRange(2, 16).fieldOf("spacing").forGetter(GroveSettings::spacing),
				Codec.intRange(0, 24).fieldOf("clearance").forGetter(GroveSettings::clearance),
				Codec.intRange(1, 32).fieldOf("max_relief").forGetter(GroveSettings::maxRelief)
		).apply(instance, GroveSettings::new));
	}

	public static final Codec<WitherGroveStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.<WitherGroveStructure>mapCodec((instance) -> {
		return instance.group(settingsCodec(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((structure) -> {
			return structure.startPool;
		}), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> {
			return structure.startJigsawName;
		}), Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> {
			return structure.maxDepth;
		}), HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> {
			return structure.startHeight;
		}), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((structure) -> {
			return structure.useExpansionHack;
		}), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((structure) -> {
			return structure.projectStartToHeightmap;
		}), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> {
			return structure.maxDistanceFromCenter;
		}), FeatureType.CODEC.fieldOf("feature_type").forGetter(structure -> {
			return (FeatureType) structure.featureType;
		}), Codec.DOUBLE.optionalFieldOf("probability", 100.0).forGetter((structure) -> {
			return structure.probability;
		}), GroveSettings.CODEC.fieldOf("grove").forGetter((structure) -> {
			return structure.grove;
		}))
				.apply(instance, WitherGroveStructure::new);
	}), WitherGroveStructure::verifyGroveRange).codec();

	private final GroveSettings grove;

	private static DataResult<WitherGroveStructure> verifyGroveRange(WitherGroveStructure structure) {
		return verifyRange(structure).map(s -> structure);
	}

	public WitherGroveStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName,
			int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap,
			int maxDistanceFromCenter, IFeatureType featureType, Double probability, GroveSettings grove) {
		super(structureSettings, startPool, startJigsawName, maxDepth, startHeight, useExpansionHack, projectStartToHeightmap,
				maxDistanceFromCenter, featureType, probability);
		this.grove = grove;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		Optional<GenerationStub> jigsaw = super.findGenerationPoint(context);
		if (jigsaw.isEmpty()) {
			return jigsaw;
		}
		GenerationStub stub = jigsaw.get();
		// the jigsaw stub's position is the centre of the main tree, one above its ground
		BlockPos centre = stub.position();
		long groveSeed = context.random().nextLong();
		return Optional.of(new GenerationStub(centre, builder -> {
			List<StructurePiece> anchor = stub.getPiecesBuilder().build().pieces();
			anchor.forEach(builder::addPiece);
			layoutGrove(context, centre, groveSeed, anchor, builder);
		}));
	}

	/**
	 * Scatters trees by dart throwing (a cheap Poisson-disc): a random point in the disc is kept if it
	 * is inside the grove's wavy edge, clear of the main tree and its pieces, far enough from every tree
	 * so far, and on dry ground near the main tree's height.
	 */
	private void layoutGrove(GenerationContext context, BlockPos centre, long groveSeed, List<StructurePiece> anchor, StructurePiecesBuilder builder) {
		RandomSource random = RandomSource.create(groveSeed);
		int radius = grove.radius();
		int ground = centre.getY() - 1;
		// a wobble on the edge, so the grove is not a perfect circle
		double phase3 = random.nextDouble() * Math.PI * 2;
		double phase5 = random.nextDouble() * Math.PI * 2;

		List<BoundingBox> keepOut = new ArrayList<>();
		for (StructurePiece piece : anchor) {
			keepOut.add(piece.getBoundingBox().inflatedBy(2));
		}
		List<BlockPos> trees = new ArrayList<>();
		int target = grove.minTrees() + random.nextInt(Math.max(1, grove.maxTrees() - grove.minTrees() + 1));
		int minDistanceSq = grove.spacing() * grove.spacing();
		int attempts = 3 * radius * radius / grove.spacing();

		for (int attempt = 0; attempt < attempts && trees.size() < target; attempt++) {
			double angle = random.nextDouble() * Math.PI * 2;
			double distance = Math.sqrt(random.nextDouble()) * radius;
			double edge = radius * (0.82 + 0.10 * Math.sin(3 * angle + phase3) + 0.08 * Math.sin(5 * angle + phase5));
			if (distance > edge || distance < grove.clearance()) {
				continue;
			}
			// thinner toward the edge
			double fraction = distance / edge;
			if (random.nextDouble() < 0.25 * fraction * fraction) {
				continue;
			}
			int x = centre.getX() + (int) Math.round(Math.cos(angle) * distance);
			int z = centre.getZ() + (int) Math.round(Math.sin(angle) * distance);
			if (trees.stream().anyMatch(t -> (t.getX() - x) * (t.getX() - x) + (t.getZ() - z) * (t.getZ() - z) < minDistanceSq)) {
				continue;
			}
			if (keepOut.stream().anyMatch(box -> x >= box.minX() && x <= box.maxX() && z >= box.minZ() && z <= box.maxZ())) {
				continue;
			}
			int surface = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
			int floor = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
			// water above the floor
			if (surface != floor || Math.abs(surface - ground) > grove.maxRelief()) {
				continue;
			}
			BlockPos origin = new BlockPos(x, surface, z);
			// tallest beside the main tree, shorter toward the edge
			float scale = (float) (1.0 - 0.35 * fraction);
			builder.addPiece(new WitherTreePiece(origin, random.nextLong(), WitherTreeArchetype.choose(fraction, random), scale));
			trees.add(origin);
		}
	}

	@Override
	public StructureType<?> type() {
		return TreasureStructures.WITHER_GROVE.get();
	}
}
