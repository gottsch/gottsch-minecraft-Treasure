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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.template.TemplateGenerator;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;


/**
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class WellGenerator implements IWellGenerator<GeneratorResult<GeneratorData>> {

	/**
	 * 
	 */
	@Override
	public Optional<GeneratorResult<GeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords) {
		return generate(context, originalSpawnCoords, null);
	}

	/**
	 * 
	 */
	@Override
	public Optional<GeneratorResult<GeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords, TemplateHolder holder) {
		/*
		 * Setup
		 */
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
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
		Treasure.LOGGER.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());
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
		//		actualSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(actualSpawnCoords.getX(), 255, actualSpawnCoords.getZ()));
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
		// build well
		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(context, template,  placement, originalSpawnCoords);
		//		 , () -> {
		//			 Map<BlockState, BlockState> m = new HashMap<>();
		//		        m.put(Blocks.REDSTONE_BLOCK.defaultBlockState(), TreasureBlocks.WISHING_WELL.get().defaultBlockState());
		//		        return m;
		//		 });

		Treasure.LOGGER.debug("Well gen  structure result -> {}", genResult.isSuccess());
		if (!genResult.isSuccess()) {
			Treasure.LOGGER.debug("failing well gen.");
			return Optional.empty();
		}

		// TODO this isn't good either as we don't know what block to fill in with ex. it could be a desert well.
		// fill any holes left by offset blocks
		List<BlockInfoContext> offsetContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.OFFSET));
		offsetContexts.forEach(ctx -> {
			context.level().setBlock(ctx.getCoords().toPos(), TreasureBlocks.WISHING_WELL.get().defaultBlockState(), 3);
		});

		// get the rotated/transformed size
		//BlockPos transformedSize = holder.getTemplate().transformedSize(rotation);
		ICoords transformedSize = genResult.getData().getSize();
		Treasure.LOGGER.debug("Well transformed size -> {}", transformedSize.toShortString());
		// add flowers around well
		addDecorations(context, genResult.getData().getSpawnCoords(), transformedSize.getX(), transformedSize.getZ());

		// TODO add chest if any

		// add the structure data to the result
		result.setData(genResult.getData());

		Treasure.LOGGER.info("CHEATER! Wishing Well at coords: {}", result.getData().getSpawnCoords().toShortString());

		return Optional.of(result);
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param width
	 * @param depth
	 */
	public void addDecorations(IWorldGenContext context, ICoords coords, int width, int depth) {
		ICoords startCoords = coords.add(-1, 0, -1);

		// TODO change to scan the entire size (x,z) of well footprint and detect the edges ... place flowers adjacent to edge blocks. 

		// north of well
		for (int widthIndex = 0; widthIndex <= width + 1; widthIndex++) {
			if (RandomHelper.randomInt(0, 1) == 0) {
				//				ICoords decoCoords = startCoords.add(widthIndex, 0, 0);
				addDecoration(context, startCoords.add(widthIndex, 0, 0));
			}
		}

		// south of well
		startCoords = coords.add(-1, 0, depth);
		for (int widthIndex = 0; widthIndex <= width + 1; widthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(context, startCoords.add(widthIndex, 0, 0));
			}
		}

		// west of well
		startCoords = coords.add(-1, 0, 0);
		for (int depthIndex = 0; depthIndex < depth-1; depthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(context, startCoords.add(0, 0, depthIndex));
			}
		}

		// east of well
		startCoords = coords.add(width, 0, 0);
		for (int depthIndex = 0; depthIndex < depth-1; depthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(context, startCoords.add(0, 0, depthIndex));
			}
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
		Treasure.LOGGER.debug("Marker on block: {}", markerContext.getState());
		if (markerContext.equalsBlock(Blocks.GRASS_BLOCK) || markerContext.equalsBlock(Blocks.DIRT)) {
			blockState = FLOWERS.get(context.random().nextInt(FLOWERS.size())).defaultBlockState();
		}
		else if (markerContext.equalsBlock(Blocks.COARSE_DIRT)) {
			blockState = Blocks.TALL_GRASS.defaultBlockState();
		}
		else if (markerContext.equalsBlock(Blocks.MYCELIUM) || markerContext.equalsBlock(Blocks.PODZOL)) {
			blockState = MUSHROOMS.get(context.random().nextInt(MUSHROOMS.size())).defaultBlockState();			
		}
		else {
			blockState = TALL_PLANTS.get(context.random().nextInt(TALL_PLANTS.size())).defaultBlockState();
		}				
		// set the block state
		context.level().setBlock(coords.toPos(), blockState, 3);
	}
}