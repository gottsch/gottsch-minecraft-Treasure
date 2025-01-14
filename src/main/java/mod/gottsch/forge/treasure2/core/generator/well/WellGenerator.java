/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
 *
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.generator.well;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.size.DoubleRange;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.StructureConfiguration.StructMeta;
import mod.gottsch.forge.treasure2.core.generator.*;
import mod.gottsch.forge.treasure2.core.generator.template.TemplateGenerator;
import mod.gottsch.forge.treasure2.core.generator.template.TemplatePoiInspector;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;


/**
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class WellGenerator implements IWellGenerator<GeneratorResult<? extends GeneratorData>> {
	private static final ResourceLocation WISHING_WELL_LOOT_TABLE = new ResourceLocation(Treasure.MODID, "wishing_well_free_wishables");
	/**
	 *
	 */
	@Override
	public Optional<GeneratorResult<? extends GeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords) {
		return generate(context, originalSpawnCoords, null);
	}

	/**
	 *
	 */
	@Override
	public Optional<GeneratorResult<? extends GeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords, TemplateHolder holder) {
		/*
		 * Setup
		 */
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();

		// get the template
		if (holder == null) {
			Optional<TemplateHolder> optionalHolder = selectTemplate(context, originalSpawnCoords, StructureCategory.TERRANEAN, StructureType.WELL);
			if (optionalHolder.isPresent()) {
				holder = optionalHolder.get();
			}
		}
		if (holder == null) {
			return Optional.empty();
		}

		GottschTemplate template = (GottschTemplate) holder.getTemplate();
		Treasure.LOGGER.debug("selected template holder -> {}", holder.getLocation());
		if (template == null) {
			Treasure.LOGGER.debug("could not find random template");
			return Optional.empty();
		}

		// select a random rotation
		Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("with rotation -> {}", rotation);
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(context.random());

		ICoords templateSize = new Coords(holder.getTemplate().getSize(rotation));
		ICoords actualSpawnCoords =generator.getTransformedSpawnCoords(originalSpawnCoords, templateSize, placement);

		/*
		 * Environment Checks
		 */
		// 1. determine y-coord of land surface for the actual spawn coords
		actualSpawnCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), actualSpawnCoords);

		if (actualSpawnCoords == null || actualSpawnCoords == Coords.EMPTY) {
			Treasure.LOGGER.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return Optional.empty();
		}
		Treasure.LOGGER.debug("actual spawn coords after dry land surface check -> {}", actualSpawnCoords);

		// 2. check if it has 50% land
		if (!WorldInfo.isSolidBase(context.level(), actualSpawnCoords, 3, 3, 50)) {
			Treasure.LOGGER.debug("Coords [{}] does not meet solid base requires for {} x {}", actualSpawnCoords.toShortString(), 3, 3);
			return Optional.empty();
		}

		/*
		 * Build
		 */
		// update original spawn coords' y-value to be that of actual spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), actualSpawnCoords.getY(), originalSpawnCoords.getZ());
		Treasure.LOGGER.debug("Well original spawn coords -> {}", originalSpawnCoords.toShortString());

//		ICoords offsetCoords = Config.structConfigMetaMap.get(holder.getLocation()).getOffset().asCoords();
		Treasure.LOGGER.debug("looking for offset meta for -> {}", holder.getLocation());
		Optional<StructMeta> meta = Config.getStructMeta(holder.getLocation());
		ICoords offsetCoords = Coords.EMPTY;
		if (meta.isPresent()) {
			offsetCoords = meta.get().getOffset().asCoords();
			Treasure.LOGGER.debug("found offset coords -> {}", offsetCoords);
		}
		else {
			// TEMP dump map
			Treasure.LOGGER.debug("dump struct meta map -> {}", Config.structConfigMetaMap);
			Treasure.LOGGER.debug("... was looking for -> {}", holder.getLocation());
		}

		// build well
		GeneratorResult<TemplateGeneratorData> templateGenerationResult = generator.generate(context, template,  placement, originalSpawnCoords, offsetCoords);

		Treasure.LOGGER.debug("Well gen  structure result -> {}", templateGenerationResult.isSuccess());
		if (!templateGenerationResult.isSuccess()) {
			Treasure.LOGGER.debug("failing well gen.");
			return Optional.empty();
		}

		// get the rotated/transformed size
		//BlockPos transformedSize = holder.getTemplate().transformedSize(rotation);
		ICoords transformedSize = templateGenerationResult.getData().getSize();
		Treasure.LOGGER.debug("Well transformed size -> {}", transformedSize.toShortString());
		// add flowers around well
		addDecorations(context, templateGenerationResult.getData().getSpawnCoords(), transformedSize.getX(), transformedSize.getZ());

		// locate structure poi's
		TemplatePoiInspector inspector = new TemplatePoiInspector();
		inspector.inspect(templateGenerationResult.getData().getMap());

		// select a treasure chest (if any)
		Optional<BlockInfoContext> treasureChestContext = GeneratorUtil.selectTreasureChest(context, inspector);

		// build vanilla chests (if any) ie add them back and populate with a loot table
		GeneratorUtil.buildVanillaChests(context, placement, inspector, WISHING_WELL_LOOT_TABLE);

		// build a one-time spawner using a mob set, if any
		Optional.ofNullable(GeneratorUtil.selectMobSet(context, inspector, meta.orElseGet(StructMeta::new)))
				.ifPresent(action -> {
					action.ifPresentOrElse(mobset ->
									// populate proximity spawners with mobSet mobs
									GeneratorUtil.buildOneTimeSpawners(context, inspector.getProximitySpawners(), mobset, null, 5D)
							, () ->
									// populate proximity spawners with generic mobs
									GeneratorUtil.buildOneTimeSpawners(context, inspector.getProximitySpawners(), new DoubleRange(1, 2), 5D)
					);
				});

		// set up the result
		result.getData().setSpawnCoords(actualSpawnCoords);

		if (treasureChestContext.isPresent()) {
			Treasure.LOGGER.debug("treasure chest coords -> {}", treasureChestContext.get().getCoords());
			result.getData().setCoords(treasureChestContext.get().getCoords());
			result.getData().setState(treasureChestContext.get().getState());
		}
		if (templateGenerationResult.getData().getSpawnCoords() == null || templateGenerationResult.getData().getSpawnCoords() == Coords.EMPTY) {
			result.getData().setSpawnCoords(originalSpawnCoords);
		}
		// mark as a success
		result.success();

		Treasure.LOGGER.info("CHEATER! Wishing Well at coords: {}", result.getData().getSpawnCoords().toShortString());

		return Optional.of(result);
	}

	/**
	 *
	 * @param context
	 * @param coords
	 * @param width
	 * @param depth
	 */
	public void addDecorations(IWorldGenContext context, ICoords coords, int width, int depth) {
		ICoords startCoords = coords.add(-1, 0, -1);

		// randomize in a circle around the well
		ICoords midCoords = coords.add(width/2, 0, depth/2);

		for (int i = 0; i < 20; i++) {
			double radius = context.random().nextDouble() * Math.max(width/2 + 2, depth/2 + 2) + Math.min(width + 1, depth + 1);

			// Generate a random angle in radians
			double angle = context.random().nextDouble() * 2 * Math.PI;

			// Calculate x and y coordinates
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);

			addDecoration(context, midCoords.add((int) x, 0, (int) y));
		}
	}

	@Override
	public void addDecoration(IWorldGenContext context, ICoords coords) {
		BlockState blockState = null;
		ICoords markerCoords = WorldInfo.getDryLandSurfaceCoordsWG(context, coords);

		if (markerCoords == null || markerCoords == Coords.EMPTY) {
			Treasure.LOGGER.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return;
		}
		BlockContext markerContext = new BlockContext(context.level(), markerCoords);
		if (!markerContext.isAir() && !markerContext.isReplaceable()) {
			Treasure.LOGGER.debug("Returning due to marker coords is not air nor replaceable.");
			return;
		}

		markerContext = new BlockContext(context.level(), markerCoords.add(0, -1, 0));
		Treasure.LOGGER.debug("marker block -> {} at -> {}", markerContext.getState(), markerContext.getCoords().toShortString());

		if (markerContext.equalsBlock(Blocks.GRASS_BLOCK) || markerContext.equalsBlock(Blocks.DIRT)) {

			if (RandomHelper.checkProbability(context.random(), Config.SERVER.wells.cloverProbability.get())) {
				blockState = TreasureBlocks.CLOVER.get().defaultBlockState();
			} else {
				blockState = context.random().nextInt(4) == 0
						? TALL_PLANTS.get(context.random().nextInt(TALL_PLANTS.size())).defaultBlockState()
						: FLOWERS.get(context.random().nextInt(FLOWERS.size())).defaultBlockState();
			}
		}
		else if (markerContext.equalsBlock(Blocks.COARSE_DIRT)) {
			blockState = Blocks.TALL_GRASS.defaultBlockState();
		}
		else if (markerContext.getState().is(BlockTags.MUSHROOM_GROW_BLOCK)) {
			blockState = MUSHROOMS.get(context.random().nextInt(MUSHROOMS.size())).defaultBlockState();
		}
		else if (markerContext.getState().is(BlockTags.DEAD_BUSH_MAY_PLACE_ON)) {
			blockState = Blocks.CACTUS.defaultBlockState();
		}

		// set the block state
		if (blockState != null) {
			context.level().setBlock(markerCoords.toPos(), blockState, 3);
		}
	}


}