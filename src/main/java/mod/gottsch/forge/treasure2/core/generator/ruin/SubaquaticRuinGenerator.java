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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;


/**
 * TODO this is exactly the same as SurfaceRuinGenerator except for the 
 * finding of the surface/floor coords and the required base and water size.
 * .. and the offset of the x-z plane when checking the water size.
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubaquaticRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {

	private static final double REQUIRED_BASE_SIZE = 50;
	private static final double REQUIRED_WATER_SIZE = 30;

	/**
	 * 
	 */
	public SubaquaticRuinGenerator() {}

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords) {
		return generate(context, originalSpawnCoords, null);
	}

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, 
			ICoords originalSpawnCoords, TemplateHolder holder) {
		
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator templateGenerator = new TemplateGenerator();
		templateGenerator.setNullBlock(Blocks.AIR);

		// get the template holder from the given archetype, type and biome
		if (holder == null) {
			Optional<TemplateHolder> optionalHolder = selectTemplate(context, originalSpawnCoords, StructureCategory.SUBAQUATIC, StructureType.RUIN);
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

		ICoords templateSize = new Coords(holder.getTemplate().getSize(placement.getRotation()));
		Treasure.LOGGER.debug("template size / footprint -> {}", templateSize.toShortString());

		BlockPos rotatedSize = template.getSize(rotation);
		Treasure.LOGGER.debug("rotated size -> {}", rotatedSize.toShortString());

		ICoords alignedSpawnCoords = GeneratorUtil.align(originalSpawnCoords, rotatedSize, placement);
		Treasure.LOGGER.debug("aligned spawn coords -> {}", alignedSpawnCoords.toShortString());

		ICoords standardizedSpawnCoords = GeneratorUtil.standardizePosition(alignedSpawnCoords, rotatedSize, placement);
		Treasure.LOGGER.debug("new rotated standardized coords -> {}", standardizedSpawnCoords.toShortString());

		/**
		 * Environment Checks
		 */
		alignedSpawnCoords = WorldInfo.getOceanFloorSurfaceCoords(context.level(), context.chunkGenerator(), alignedSpawnCoords);

		Treasure.LOGGER.debug("ocean floor coords -> {}", alignedSpawnCoords.toShortString());
		if (alignedSpawnCoords == Coords.EMPTY) {
			return Optional.empty();
		}
		// update standardized with the correct y value -> same as aligned
		standardizedSpawnCoords.withY(alignedSpawnCoords.getY());

		// if offset coords are set, move to that y for testing solid base
		if (offsetCoords != Coords.EMPTY) {
			standardizedSpawnCoords = standardizedSpawnCoords.add(0, offsetCoords.getY(), 0);
		}

		// check if it has % land
		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(context.level(), standardizedSpawnCoords, templateSize.getX(), templateSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.LOGGER.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", REQUIRED_BASE_SIZE, standardizedSpawnCoords.toShortString(), templateSize.getX(), templateSize.getY());
					return Optional.empty();
				}
				else {
					standardizedSpawnCoords = standardizedSpawnCoords.add(0, -1, 0);
					alignedSpawnCoords = alignedSpawnCoords.add(0, -1, 0);
				}
			}
			else {
				break;
			}
		}

		// get the x-z plane above alignedSpawn Coords
		int offset = 1;
		if (rotatedSize.getY() > 6) {
			offset = 3;
		}
		else if (rotatedSize.getY() >=4) {
			offset =2;
		}

		Treasure.LOGGER.debug("checking for {} % water using offset of -> {} at coords -> {} for dimensions -> {} x {}", REQUIRED_WATER_SIZE, offset, alignedSpawnCoords.add(0, offset, 0), templateSize.getX(), templateSize.getZ());
		if (!WorldInfo.isLiquidBase(context.level(), alignedSpawnCoords.add(0, offset, 0), rotatedSize.getX(), rotatedSize.getZ(), REQUIRED_WATER_SIZE)) {
			Treasure.LOGGER.debug("Coords -> [{}] does not meet {} % water base requirements for size -> {} x {}", alignedSpawnCoords.toShortString(), REQUIRED_WATER_SIZE, templateSize.getX(), templateSize.getZ());
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
//		Optional<StructMeta> meta = Config.getStructMeta(holder.getLocation());
//		ICoords offsetCoords = Coords.EMPTY;
//		if (meta.isPresent()) {
//			offsetCoords = meta.get().getOffset().asCoords();
//		}
//		else {
//			// TEMP dump map
////			Treasure.LOGGER.debug("dump struct meta map -> {}", Config.structConfigMetaMap);
//			Treasure.LOGGER.debug("... was looking for -> {}", holder.getLocation());
//		}

		Treasure.LOGGER.debug("using spawn coords to generate -> {} with rotationg -> {}", alignedSpawnCoords, rotation);

		GeneratorResult<TemplateGeneratorData> genResult = templateGenerator.generate(context, template, placement, alignedSpawnCoords, offsetCoords);
		 if (!genResult.isSuccess()) {
			 return Optional.empty();
		 }

		Treasure.LOGGER.debug("submerged gen result -> {}", genResult);

		 /*
		  * adjust coords with offset and fill below
		  * NOTE the offset value here uses only what is provided by the meta value,
		  * it does not check the structure itself for a marker
		  */
		GeneratorUtil.fillBelow(context, genResult.getData().getSpawnCoords(), rotatedSize, 3, Blocks.GRAVEL.defaultBlockState());

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
		BlockInfoContext chestContext = null;
		if (bossChestContexts != null && !bossChestContexts.isEmpty()) {
			if (bossChestContexts.size() > 1) {
				chestContext = bossChestContexts.get(context.random().nextInt(bossChestContexts.size()));
			}
			else {
				chestContext = bossChestContexts.get(0);
			}			
		}
		
		// TODO turn these checks into methods
		// if a boss chest wasn't found, search for regular chests
		if (chestContext == null) {
			if (chestContexts != null && !chestContexts.isEmpty()) {
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
			chestCoords = chestContext.getCoords();
			if (chestCoords == Coords.EMPTY) {
				chestCoords = null;
			}
			chestContext.setCoords(chestCoords);
		}
		if (chestCoords == null) {
			return Optional.empty();
		}

		GeneratorUtil.buildVanillaSpawners(context, spawnerContexts);

		// populate proximity spawners
		GeneratorUtil.buildOneTimeSpawners(context, proximityContexts, new DoubleRange(1,2), 5D);

		result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
		
		// update with chest context
		result.getData().setCoords(chestContext.getCoords());
		result.getData().setState(chestContext.getState());
		
		return Optional.of(result);
	}	
	
	private ICoords align(ICoords spawnCoords, ICoords newEntrance, BlockPos transformedSize, PlacementSettings placement) {
		ICoords startCoords = null;
		// NOTE work with rotations only for now
		
		// first offset spawnCoords by newEntrance
		startCoords = spawnCoords.add(-newEntrance.getX(), 0, -newEntrance.getZ());
		
		return startCoords;
	}
}