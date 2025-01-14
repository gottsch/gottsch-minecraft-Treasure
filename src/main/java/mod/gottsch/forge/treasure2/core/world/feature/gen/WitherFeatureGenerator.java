/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.WitherChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IStructurePitGenerator;
import mod.gottsch.forge.treasure2.core.generator.template.ITemplateGenerator;
import mod.gottsch.forge.treasure2.core.generator.witherTree.GreatWitherTreeGenerator;
import mod.gottsch.forge.treasure2.core.generator.witherTree.WitherTreeGenerator;
import mod.gottsch.forge.treasure2.core.registry.PitGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.*;
import mod.gottsch.forge.treasure2.core.util.GeometryUtil;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Gottschling on May 12, 2023
 */
public class WitherFeatureGenerator implements IFeatureGenerator {

	protected static int UNDERGROUND_OFFSET = 3;
	
	private ResourceLocation name = new ResourceLocation(Treasure.MODID, "wither");
	
	@Override
	public ResourceLocation getName() {
		return name;
	}

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext context, ICoords spawnCoords,
			IRarity rarity, ChestRarity config) {

		// NOTE spawnCoords will be 1 block in the ground due to the deferred placement. the wither tree should be on top of the ground
		spawnCoords = spawnCoords.up(1);

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}

		// determine spawn coords below ground
		Optional<ICoords> undergroundCoords = getUndergroundSpawnPos(context.level(), context.random(), spawnCoords, config.getMinDepth(), config.getMaxDepth());

		if (undergroundCoords.isEmpty()) {
			Treasure.LOGGER.debug("unable to spawn underground @ {}", spawnCoords);
			return Optional.empty();
		}
		Treasure.LOGGER.debug("below ground -> {}", undergroundCoords.get().toShortString());

		// setup a max AABB centered around the spawn coords
		AABB maxArea = new AABB(spawnCoords.toPos());

		// add pit
		Treasure.LOGGER.debug("generate pit");
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(context.random());
		Treasure.LOGGER.debug("using pit generator -> {}", pitGenerator.getClass().getSimpleName());

		Optional<GeneratorResult<ChestGeneratorData>> pitResult;
		if (pitGenerator instanceof IStructurePitGenerator) {
			Optional<TemplateHolder> optionalHolder = selectTemplate(context, spawnCoords);
			pitResult = ((IStructurePitGenerator)pitGenerator).generate(context, spawnCoords, undergroundCoords.get(), optionalHolder.orElse(null));
		} else {
			pitResult = pitGenerator.generate(context, spawnCoords, undergroundCoords.get());
		}
//		Optional<GeneratorResult<ChestGeneratorData>> pitResult = pitGenerator.generate(context, spawnCoords, undergroundCoords.get(), holder);

		if (pitResult.isEmpty()) {
			return Optional.empty();
		}

		// build great wither tree
		GreatWitherTreeGenerator greatWitherTreeGenerator = new GreatWitherTreeGenerator();
		maxArea = maxArea.inflate(greatWitherTreeGenerator.getMaxGenRadius(), 5, greatWitherTreeGenerator.getMaxGenRadius());
		Optional<GeneratorResult<GeneratorData>> greatTreeResult = greatWitherTreeGenerator.generate(context, spawnCoords, spawnCoords);
		if (greatTreeResult.isEmpty()) {
			return Optional.empty();
		}

		WitherTreeGenerator witherTreeGenerator = new WitherTreeGenerator();
		int numTrees = RandomHelper.randomInt(Config.SERVER.witherTree.minSupportingTrees.get(), Config.SERVER.witherTree.maxSupportingTrees.get());
		for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
			ICoords c = GeometryUtil.generateRandomCoordsByRadius(context.random(), witherTreeGenerator.getMinRadius(), witherTreeGenerator.getMaxRadius());
			c = c.add(spawnCoords);
			c = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), c);
			// add tree if criteria is met
			if (c != Coords.EMPTY) {
				if (c.getDistanceSq(spawnCoords) > 4) {
					if (context.level().getBlockState(c.toPos()).getBlock() != TreasureBlocks.WITHERWOOD_LOG.get()) {
						witherTreeGenerator.generate(context, c, spawnCoords);
					}
				}
			}
		}

		// add decorations
		greatWitherTreeGenerator.addRocks(context, maxArea, spawnCoords);
		greatWitherTreeGenerator.addScrub(context, maxArea, spawnCoords);
		greatWitherTreeGenerator.addSpawners(context, maxArea, spawnCoords);

		// add chest
		ICoords chestCoords = pitResult.get().getData().getCoords();
		if (chestCoords == null) {
			return Optional.empty();
		}
		WitherChestGenerator chestGen = new WitherChestGenerator();
		GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(context, chestCoords, SpecialRarity.WITHER, null);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}
		
		Treasure.LOGGER.info("CHEATER! WITHER chest at coords: {}", spawnCoords.toShortString());
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setCoords(chestCoords);
		result.getData().setRegistryName(chestResult.getData().getRegistryName());
		result.getData().setRarity(SpecialRarity.WITHER);
		
		return Optional.of(result);
	}

	/**
	 * 
	 * @param random
	 * @return
	 */
	public IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(RandomSource random) {
//		PitType pitType = RandomHelper.checkProbability(random, Config.SERVER.pits.structureProbability.get()) ? PitType.STRUCTURE : PitType.STANDARD;
		PitType pitType = PitType.STRUCTURE;
		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = PitGeneratorRegistry.get(pitType);
		IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
		Treasure.LOGGER.debug("using pitType -> {}, gen -> {}", pitType, pitGenerator.getClass().getSimpleName());

		return pitGenerator;
	}

	/**	 *
	 * @param level
	 * @param random
	 * @param startingCoords
	 * @param minDepth
	 * @param maxDepth
	 * @return underground spawn coords
	 */
	public static Optional<ICoords> getUndergroundSpawnPos(ServerLevelAccessor level, RandomSource random, ICoords startingCoords, int minDepth, int maxDepth) {
		// calculate the depth
		int depth = RandomHelper.randomInt(minDepth, maxDepth);
		// use the deepest depth between calculated and default
		int y = Math.min(startingCoords.getY() - UNDERGROUND_OFFSET, startingCoords.getY() - depth);
		Treasure.LOGGER.debug("underground spawn pos.y -> {}", y);
		ICoords coords = new Coords(startingCoords.getX(), y, startingCoords.getZ());
		// get floor pos (if in a cavern or tunnel etc)
		coords = WorldInfo.getSubterraneanSurfaceCoords(level, coords);
		return coords == Coords.EMPTY ? Optional.empty() : Optional.ofNullable(coords);
	}

	public Optional<TemplateHolder> selectTemplate(IWorldGenContext context, ICoords coords) {
		Optional<TemplateHolder> holder = Optional.empty();

		Holder<Biome> biome = context.level().getBiome(coords.toPos());
		List<TemplateHolder> holders = TreasureTemplateRegistry.getTemplate(StructureCategory.SUBTERRANEAN, StructureType.WITHER_TREE_ROOM, ModUtil.getName(biome));
		holders.addAll(TreasureTemplateRegistry.getTemplate(StructureCategory.SUBTERRANEAN, StructureType.ROOM, ModUtil.getName(biome)));
		if (!holders.isEmpty()) {
			holder = Optional.ofNullable(holders.get(context.random().nextInt(holders.size())));
		}
		return holder;
	}
}
