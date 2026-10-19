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
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
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
	/** never in any biome source; only ever painted onto a grove */
	public static final ResourceKey<Biome> GROVE_BIOME = ResourceKey.create(Registries.BIOME, new ResourceLocation(Treasure.MODID, "wither_grove"));
	/** how far the painted biome reaches past the furthest trees */
	private static final int PAINT_MARGIN = 3;
	/** how far below the ground the biome is painted; the pit room, deeper under the main tree, is left alone */
	private static final int PAINT_DEPTH = 8;
	/** how far a village spreads from its start: the jigsaw limit of 80, plus its outermost house and a margin (one measured 95) */
	private static final int VILLAGE_REACH = 104;
	/** boulders per grove, and how far they keep from trees and from each other */
	private static final int MIN_BOULDERS = 2;
	private static final int MAX_BOULDERS = 5;
	private static final int BOULDER_TREE_SPACING = 3;
	private static final int BOULDER_SPACING = 7;

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
		// vanilla tests the biome only after this method returns, so bail out of oceans before any jigsaw work
		ChunkPos chunk = context.chunkPos();
		Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromBlock(chunk.getMiddleBlockX()),
				QuartPos.fromBlock(context.chunkGenerator().getSeaLevel()), QuartPos.fromBlock(chunk.getMiddleBlockZ()), context.randomState().sampler());
		if (!context.validBiome().test(biome)) {
			return Optional.empty();
		}
		// and out of rivers and lakes, which the jigsaw would otherwise be assembled for
		if (isWet(context, chunk.getMiddleBlockX(), chunk.getMiddleBlockZ())) {
			return Optional.empty();
		}
		// and away from villages, whose own trees and houses the grove's biome cannot keep out
		if (isNearVillage(context, chunk)) {
			return Optional.empty();
		}

		Optional<GenerationStub> jigsaw = super.findGenerationPoint(context);
		if (jigsaw.isEmpty()) {
			return jigsaw;
		}
		GenerationStub stub = jigsaw.get();
		// the jigsaw stub's position is the centre of the main tree, one above its ground
		BlockPos centre = stub.position();
		if (isFlooded(context, centre)) {
			return Optional.empty();
		}
		long groveSeed = context.random().nextLong();
		return Optional.of(new GenerationStub(centre, builder -> {
			List<StructurePiece> anchor = stub.getPiecesBuilder().build().pieces();
			anchor.forEach(builder::addPiece);
			layoutGrove(context, centre, groveSeed, anchor, builder);
		}));
	}

	/**
	 * Keeps the grove off water. The biome check misses rivers beside the centre and lakes (which have no
	 * biome), so look at the surface itself: the main tree's own spot must be dry, at most one of eight
	 * points round it (6 out) may be wet, and at most three of eight across the grove (12 out).
	 */
	private static boolean isFlooded(GenerationContext context, BlockPos centre) {
		if (isWet(context, centre.getX(), centre.getZ())) {
			return true;
		}
		return countWet(context, centre, 6, 2) >= 2 || countWet(context, centre, 12, 4) >= 4;
	}

	/** wet points among eight round the centre, stopping as soon as the limit is reached */
	private static int countWet(GenerationContext context, BlockPos centre, int radius, int limit) {
		int wet = 0;
		for (int i = 0; i < 8 && wet < limit; i++) {
			double angle = i * Math.PI / 4;
			if (isWet(context, centre.getX() + (int) Math.round(Math.cos(angle) * radius), centre.getZ() + (int) Math.round(Math.sin(angle) * radius))) {
				wet++;
			}
		}
		return wet;
	}

	/**
	 * Whether a village could start near enough to run into the grove. The villages' own placement
	 * picks one candidate chunk per spacing-sized region, exactly as the game does, and a candidate
	 * only counts if one of the village structures accepts the biome there, which is the same test
	 * vanilla makes before building it. (An exclusion zone would block every candidate, biome or not.)
	 * Frequency reduction is ignored; vanilla villages have none.
	 */
	private boolean isNearVillage(GenerationContext context, ChunkPos chunk) {
		Optional<Holder.Reference<StructureSet>> villages = context.registryAccess().registryOrThrow(Registries.STRUCTURE_SET)
				.getHolder(BuiltinStructureSets.VILLAGES);
		if (villages.isEmpty() || !(villages.get().value().placement() instanceof RandomSpreadStructurePlacement placement)) {
			return false;
		}
		int centreX = chunk.getMiddleBlockX();
		int centreZ = chunk.getMiddleBlockZ();
		int reach = grove.radius() + PAINT_MARGIN + VILLAGE_REACH;
		int spacing = placement.spacing();
		int regionX = Math.floorDiv(chunk.x, spacing);
		int regionZ = Math.floorDiv(chunk.z, spacing);
		for (int rx = regionX - 1; rx <= regionX + 1; rx++) {
			for (int rz = regionZ - 1; rz <= regionZ + 1; rz++) {
				ChunkPos candidate = placement.getPotentialStructureChunk(context.seed(), rx * spacing, rz * spacing);
				int dx = candidate.getMiddleBlockX() - centreX;
				int dz = candidate.getMiddleBlockZ() - centreZ;
				if (dx * dx + dz * dz > reach * reach) {
					continue;
				}
				int y = context.chunkGenerator().getFirstOccupiedHeight(candidate.getMiddleBlockX(), candidate.getMiddleBlockZ(),
						Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
				Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromBlock(candidate.getMiddleBlockX()), QuartPos.fromBlock(y),
						QuartPos.fromBlock(candidate.getMiddleBlockZ()), context.randomState().sampler());
				for (StructureSet.StructureSelectionEntry entry : villages.get().value().structures()) {
					if (entry.structure().value().biomes().contains(biome)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** fluid on top: the surface heightmap counts it, the ocean-floor one does not */
	private static boolean isWet(GenerationContext context, int x, int z) {
		int surface = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int floor = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
		return surface > floor;
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

		boulders(context, centre, ground, random, keepOut, trees, builder);

		// reach every chunk the biome is painted on, even on sides where no tree grew (see paintBiome)
		int reach = radius + PAINT_MARGIN + 3;
		builder.addPiece(new WitherGroveAreaPiece(new BoundingBox(centre.getX() - reach, ground - PAINT_DEPTH, centre.getZ() - reach,
				centre.getX() + reach, ground + 1, centre.getZ() + reach)));
	}

	/**
	 * A few boulders between the trees, by dart throwing as for the trees. Drawn after every tree, so
	 * the trees come out as they did before boulders existed.
	 */
	private void boulders(GenerationContext context, BlockPos centre, int ground, RandomSource random,
			List<BoundingBox> keepOut, List<BlockPos> trees, StructurePiecesBuilder builder) {
		int target = MIN_BOULDERS + random.nextInt(MAX_BOULDERS - MIN_BOULDERS + 1);
		double reach = grove.radius() * 0.95;
		int treeSq = BOULDER_TREE_SPACING * BOULDER_TREE_SPACING;
		int boulderSq = BOULDER_SPACING * BOULDER_SPACING;
		List<BlockPos> placed = new ArrayList<>();
		for (int attempt = 0; attempt < 80 && placed.size() < target; attempt++) {
			double angle = random.nextDouble() * Math.PI * 2;
			double distance = grove.clearance() + Math.sqrt(random.nextDouble()) * (reach - grove.clearance());
			int x = centre.getX() + (int) Math.round(Math.cos(angle) * distance);
			int z = centre.getZ() + (int) Math.round(Math.sin(angle) * distance);
			if (trees.stream().anyMatch(t -> (t.getX() - x) * (t.getX() - x) + (t.getZ() - z) * (t.getZ() - z) < treeSq)
					|| placed.stream().anyMatch(b -> (b.getX() - x) * (b.getX() - x) + (b.getZ() - z) * (b.getZ() - z) < boulderSq)
					|| keepOut.stream().anyMatch(box -> x >= box.minX() - 3 && x <= box.maxX() + 3 && z >= box.minZ() - 3 && z <= box.maxZ() + 3)) {
				continue;
			}
			int surface = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
			int floor = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
			if (surface != floor || Math.abs(surface - ground) > grove.maxRelief()) {
				continue;
			}
			BlockPos origin = new BlockPos(x, surface, z);
			builder.addPiece(new WitherBoulderPiece(origin, random.nextLong()));
			placed.add(origin);
		}
	}

	/**
	 * Paints the Wither Grove biome over this chunk's share of the grove, the way /fillbiome does.
	 * <p>
	 * This runs during the chunk's surface_structures step, before its vegetal_decoration step, and
	 * vanilla's BiomeFilter checks the biome at each feature's own position. The grove biome has no
	 * features, so the host biome's trees, flowers and grass rooted inside the painted area never
	 * generate. Only this chunk is written, so it is order-independent.
	 */
	@Override
	public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random,
			BoundingBox box, ChunkPos chunkPos, PiecesContainer piecesContainer) {
		paintBiome(level, chunkPos, piecesContainer);
		super.afterPlace(level, structureManager, chunkGenerator, random, box, chunkPos, piecesContainer);
	}

	private void paintBiome(WorldGenLevel level, ChunkPos chunkPos, PiecesContainer piecesContainer) {
		if (piecesContainer.pieces().isEmpty()) {
			return;
		}
		Optional<Holder.Reference<Biome>> biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolder(GROVE_BIOME);
		if (biome.isEmpty()) {
			return;
		}
		// piece 0 is the main tree; its box starts at the ground
		BoundingBox main = piecesContainer.pieces().get(0).getBoundingBox();
		int centreX = main.getCenter().getX();
		int centreZ = main.getCenter().getZ();
		int minY = main.minY() - PAINT_DEPTH;
		double radius = grove.radius() + PAINT_MARGIN;

		// skip chunks entirely outside the painted circle
		int nearestX = Mth.clamp(centreX, chunkPos.getMinBlockX(), chunkPos.getMaxBlockX());
		int nearestZ = Mth.clamp(centreZ, chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ());
		double reach = radius + 2;
		if ((nearestX - centreX) * (nearestX - centreX) + (nearestZ - centreZ) * (nearestZ - centreZ) > reach * reach) {
			return;
		}

		ChunkAccess chunk = level.getChunk(chunkPos.x, chunkPos.z);
		// each 4x4 biome column is painted from PAINT_DEPTH below its own lowest surface, so ground on a
		// slope well below the main tree is still covered; never shallower than below the main tree
		int[][] paintFrom = new int[4][4];
		for (int[] row : paintFrom) {
			java.util.Arrays.fill(row, minY);
		}
		for (int lx = 0; lx < 16; lx++) {
			for (int lz = 0; lz < 16; lz++) {
				int surface = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, lx, lz);
				paintFrom[lx >> 2][lz >> 2] = Math.min(paintFrom[lx >> 2][lz >> 2], surface - PAINT_DEPTH);
			}
		}
		Holder<Biome> groveBiome = biome.get();
		chunk.fillBiomesFromNoise((quartX, quartY, quartZ, sampler) -> {
			// the centre of the 4x4x4 biome cell
			int dx = QuartPos.toBlock(quartX) + 2 - centreX;
			int dz = QuartPos.toBlock(quartZ) + 2 - centreZ;
			// a ragged edge: each cell's own reach varies by a couple of blocks
			double edge = radius + ((Mth.getSeed(quartX, 0, quartZ) >> 16 & 3) - 1.5) * 1.5;
			int from = paintFrom[quartX & 3][quartZ & 3];
			if (QuartPos.toBlock(quartY) + 3 >= from && dx * dx + dz * dz <= edge * edge) {
				return groveBiome;
			}
			return chunk.getNoiseBiome(quartX, quartY, quartZ);
		}, level.getLevel().getChunkSource().randomState().sampler());
	}

	@Override
	public StructureType<?> type() {
		return TreasureStructures.WITHER_GROVE.get();
	}
}
