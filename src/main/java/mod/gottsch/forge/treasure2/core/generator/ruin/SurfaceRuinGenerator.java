/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.ruin;

import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.size.DoubleRange;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.StructureConfiguration.StructMeta;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.template.TemplateGenerator;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.Material;

/**
 * 
 * @author Mark Gottschling on Dec 13, 2019
 *
 */
public class SurfaceRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {
	
	private static final double REQUIRED_BASE_SIZE = 75;
	private static final double REQUIRED_AIR_SIZE = 50;

	/**
	 * 
	 */
	public SurfaceRuinGenerator() {}
	
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords) {
		return generate(context, originalSpawnCoords, null);
	}
	
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords, TemplateHolder holder) {
	
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		
		// get the template
		if (holder == null) {
			Optional<TemplateHolder> optionalHolder = selectTemplate(context, originalSpawnCoords, StructureCategory.TERRANEAN, StructureType.RUIN);
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
		Treasure.LOGGER.debug("original spawn coords -> {}", originalSpawnCoords.toShortString());

		// get the offset
		Optional<StructMeta> meta = Config.getStructMeta(holder.getLocation());
		ICoords offsetCoords = Coords.EMPTY;
		if (meta.isPresent()) {
			offsetCoords = meta.get().getOffset().asCoords();
		}
		else {
			// TEMP dump map
//			Treasure.LOGGER.debug("dump struct meta map -> {}", Config.structConfigMetaMap);
			Treasure.LOGGER.debug("... was looking for -> {}", holder.getLocation());
		}
		Treasure.LOGGER.debug("using offset coords -> {}", offsetCoords.toShortString());

		// select a random rotation
		Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("rotation used -> {}", rotation);
		
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(context.random());

		ICoords templateSize = new Coords(holder.getTemplate().getSize());
		Treasure.LOGGER.debug("template size -> {}", templateSize.toShortString());

		// NOTE TO SELF only need to find the 'entrance' block when building a pit below
		// the structure, or aligning the subterranean room with the pit above.

		/*
		 * we want to align the structure center to the center of the chunk ie. original spawn
		 * this involves calculating the rotated size and moving the original spawn coords
		 * to a new position, so that the rotated structure center point is aligned to the
		 *  chunk center point.
		 */
		BlockPos rotatedSize = template.getSize(rotation);
		Treasure.LOGGER.debug("rotated size -> {}", rotatedSize.toShortString());

		ICoords alignedSpawnCoords = GeneratorUtil.align(originalSpawnCoords, rotatedSize, placement);
		Treasure.LOGGER.debug("aligned spawn coords -> {}", alignedSpawnCoords.toShortString());

		// calculate the footprint coords block of the template ie adjust the aligned spawn to take into account the rotation
		// and assume building only in +x, +z direction.
		// NOTE this is only used for checks and building underneath. will need to alter the original in the opposite
		// direction because it will rotate around the coords during gen. (not post gen)
		ICoords standardizedSpawnCoords = GeneratorUtil.standardizePosition(alignedSpawnCoords, rotatedSize, placement);
		Treasure.LOGGER.debug("new rotated standardized coords -> {}", standardizedSpawnCoords.toShortString());

		// NOTE these checks don't really belong in a generator as their task is to just generate.
		// however, the template is unknown outside this call and thus the rotate, placement, size and actual coords would be unknown.
		/**
		 * Environment Checks
		 */
		alignedSpawnCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), alignedSpawnCoords);

		Treasure.LOGGER.debug("surface coords -> {}", alignedSpawnCoords.toShortString());
		if (alignedSpawnCoords == Coords.EMPTY) {
			return Optional.empty();
		}
		// update standardized with the correct y value -> same as aligned
		standardizedSpawnCoords = standardizedSpawnCoords.withY(alignedSpawnCoords.getY());
		Treasure.LOGGER.debug("surface (standardized) coords -> {}", standardizedSpawnCoords.toShortString());

		// TODO all this % base checking could be moved to method
		// if offset coords are set, move to that y for testing solid base
		if (offsetCoords != Coords.EMPTY) {
			standardizedSpawnCoords = standardizedSpawnCoords.add(0, offsetCoords.getY(), 0);
		}

		// check if it has % land base using the standardizedSpawnCoords
		for (int i = 0; i < 3; i++) {
			Treasure.LOGGER.debug("finding solid base index -> {} at coords -> {}", i, standardizedSpawnCoords.toShortString());
			if (!WorldInfo.isSolidBase(context.level(), standardizedSpawnCoords, rotatedSize.getX(), rotatedSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.LOGGER.debug("coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", standardizedSpawnCoords.toShortString(), REQUIRED_BASE_SIZE, rotatedSize.getX(), rotatedSize.getZ());
					return Optional.empty();
				}
				else {
					standardizedSpawnCoords = standardizedSpawnCoords.add(0, -1, 0);
					alignedSpawnCoords = alignedSpawnCoords.add(0, -1, 0);
					Treasure.LOGGER.debug("move standardized spawn coords down for solid base check -> {}", standardizedSpawnCoords.toShortString());
				}
			}
			else {
				break;
			}
		}

		Treasure.LOGGER.debug("using solid base coords -> {}", standardizedSpawnCoords.toShortString());
		
		// check if the plane above the actual spawn coords is % air
		Treasure.LOGGER.debug("checking for {} % air at coords -> {} for dimensions -> {} x {}", REQUIRED_AIR_SIZE, standardizedSpawnCoords.add(0, 1, 0), rotatedSize.getX(), rotatedSize.getZ());
		if (!WorldInfo.isAirBase(context.level(), standardizedSpawnCoords.add(0, 1, 0), rotatedSize.getX(), rotatedSize.getZ(), REQUIRED_AIR_SIZE)) {
			Treasure.LOGGER.debug("coords -> [{}] does not meet {} % air base requirements for size -> {} x {}", REQUIRED_AIR_SIZE, standardizedSpawnCoords.toShortString(),rotatedSize.getX(), rotatedSize.getZ());
			return Optional.empty();
		}

		// move back by the amount of offsetCoords to get in the right position
		// (because the generate will apply the offset again)
		if (offsetCoords != Coords.EMPTY) {
			standardizedSpawnCoords = standardizedSpawnCoords.add(0, -offsetCoords.getY(), 0);
		}

		/**
		 * Build
		 */
		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(context, template, placement, alignedSpawnCoords, offsetCoords);
		 if (!genResult.isSuccess()) {
			 return Optional.empty();
		 }
		Treasure.LOGGER.debug("surface gen result -> {}", genResult);

		/*
		 * adjust coords with offset and fill below
		 * NOTE the offset value here uses only what is provided by the meta value,
		 * it does not check the structure itself for a marker
		 */
		GeneratorUtil.fillBelow(context, genResult.getData().getSpawnCoords(), rotatedSize, 5, Blocks.DIRT.defaultBlockState());

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockInfoContext> bossChestContexts =
					(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.BOSS_CHEST));
		List<BlockInfoContext> chestContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.CHEST));
		List<BlockInfoContext> spawnerContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockInfoContext> proximityContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));

		/*
		 *  NOTE currently only 1 chest is allowed per structure - the rest are ignored.
		 */
		// check if there is a boss chest(s)
		// TODO turn these checks into methods --> getChestContext();
		BlockInfoContext chestContext = null;
		if (bossChestContexts != null && bossChestContexts.size() > 0) {
			if (bossChestContexts.size() > 1) {
				chestContext = bossChestContexts.get(context.random().nextInt(bossChestContexts.size()));
			}
			else {
				chestContext = bossChestContexts.get(0);
			}			
		}		

		// if a boss chest wasn't found, search for regular chests
		if (chestContext == null) {
			if (chestContexts != null && chestContexts.size() > 0) {
				if (chestContexts.size() > 1) {
					chestContext = chestContexts.get(context.random().nextInt(chestContexts.size()));
				}
				else {
					chestContext = chestContexts.get(0);
				}			
			}			
		}

		ICoords chestCoords = null;
		if (chestContext != null) {
			Treasure.LOGGER.debug("chest context coords -> {}", chestContext.getCoords());
			// move the chest coords to the first solid block beneath it.
			// NOTE can't use this method as it will disregard the entire structure as the chunkGenerator only looks at the land mass.
			// TODO need to use the old non-chunk generator version of doing this. ie stepping down a block and check.
			// NOTE however, since there isn't a decay processor, there is no need to perform this operation.
//			chestCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), chestContext.getCoords());
			chestCoords = chestContext.getCoords();
			if (chestCoords == Coords.EMPTY) {
				chestCoords = null;
			}
			chestContext.setCoords(chestCoords);
		}
		if (chestCoords == null) {
			return Optional.empty();
		}
		
		if (proximityContexts != null)
			Treasure.LOGGER.debug("Proximity spawners size -> {}", proximityContexts.size());
		else
			Treasure.LOGGER.debug("No proximity spawners found.");
		
		// populate vanilla spawners
		GeneratorUtil.buildVanillaSpawners(context, spawnerContexts);
		
		// populate proximity spawners
		GeneratorUtil.buildOneTimeSpawners(context, proximityContexts, new DoubleRange(1,2), 5D);
		
		// copy all data from genResult
		result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
		
		// update with chest context
		result.getData().setCoords(chestContext.getCoords());
		result.getData().setState(chestContext.getState());
		
		return Optional.of(result);
	}

	/**
	 * TODO replace call to this with one in GottschCore. it not exist then use the chunkGenerator
	 * NOTE candidate for GottschCore
	 * @param serverLevelAccessor
	 * @param coords
	 * @return
	 */
	public static ICoords getSolidSurfaceCoords(final ServerLevelAccessor serverLevelAccessor, final ICoords coords) {
		boolean isSurfaceBlock = false;
		ICoords newCoords = coords;
		
		while (!isSurfaceBlock) {
			// get the cube that is 1 below current position
			BlockContext blockContext = new BlockContext(serverLevelAccessor.getLevel(), newCoords.down(1));
//			Treasure.LOGGER.debug("below block -> {} @ {}", cube.getState().getBlock().getRegistryName(), cube.getCoords().toShortString());
			// exit if not valid Y coordinate
			if (!WorldInfo.isHeightValid(blockContext.getCoords())) {
				return Coords.EMPTY;
			}	

			if (blockContext.isAir() || blockContext.isReplaceable()
					|| blockContext.equalsMaterial(Material.LEAVES) || blockContext.isFluid()
					|| blockContext.isBurning()) {
//				Treasure.LOGGER.debug("block is air, leaves, replacable, liquid or burning");
				newCoords = newCoords.down(1);
			}
			else {
				isSurfaceBlock = true;
			}		
		}
		return newCoords;
	}

}