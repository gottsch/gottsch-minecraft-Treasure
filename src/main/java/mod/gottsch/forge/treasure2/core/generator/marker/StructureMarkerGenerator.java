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
package mod.gottsch.forge.treasure2.core.generator.marker;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.template.ITemplateGenerator;
import mod.gottsch.forge.treasure2.core.generator.template.TemplateGenerator;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import mod.gottsch.forge.treasure2.core.util.GeometryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

/**
 * @author Mark Gottschling on Jan 28, 2019
 *
 */
public class StructureMarkerGenerator implements IMarkerGenerator<GeneratorResult<GeneratorData>> {

	/**
	 * 
	 */
	public StructureMarkerGenerator() {
	}

	@Override
	public Optional<GeneratorResult<GeneratorData>> generate(IWorldGenContext context, ICoords coords) {
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);

		// get the template
		Optional<TemplateHolder> optionalHolder = selectTemplate(context, coords, StructureCategory.TERRANEAN, StructureType.MARKER);
		if (optionalHolder.isEmpty()) {
			return Optional.empty();	
		}
		TemplateHolder holder = optionalHolder.get();

		GottschTemplate template = (GottschTemplate) holder.getTemplate();
		Treasure.LOGGER.debug("selected template holder -> {}", holder.getLocation());
		if (template == null) {
			Treasure.LOGGER.debug("could not find random template");
			return Optional.empty();
		}
		Treasure.LOGGER.debug("original spawn coords -> {}", coords.toShortString());

		// get the offset
		Optional<StructMeta> meta = Config.getStructMeta(holder.getLocation());
		ICoords offsetCoords = Coords.EMPTY;
		if (meta.isPresent()) {
			offsetCoords = meta.get().getOffset().asCoords();
		}
		else {
			// TEMP dump map
//			Treasure.LOGGER.debug("dump struct meta map -> {}", Config.structConfigMetaMap);
			Treasure.LOGGER.debug("... was looking for template meta -> {}", holder.getLocation());
		}
		Treasure.LOGGER.debug("using offset coords -> {}", offsetCoords.toShortString());

		// find entrance
		ICoords entranceCoords =TreasureTemplateRegistry.getOffsetFrom(context.random(), template, StructureMarkers.ENTRANCE);
		if (entranceCoords == null) {
			Treasure.LOGGER.debug("Unable to locate entrance position.");
			return Optional.empty();
		}
		Treasure.LOGGER.debug("entrance coords -> {}", entranceCoords);

		// select a rotation
		Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("rotation used -> {}", rotation);

		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(context.random());

		ICoords templateSize = new Coords(holder.getTemplate().getSize());
		Treasure.LOGGER.debug("template size -> {}", templateSize.toShortString());

		// TODO move into TemplateGenerator
		// NOTE these values are still relative to origin (spawnCoords), so they are like a size
		// NOTE look at StructureTemplate.transform(). minecraft essentially has a 0,0 point and a -0, -0 point
		// so when rotating a point around a coords, the regular formula (180) of (x,y) -> (x, -y)
//		ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3()));
		ICoords newEntrance = GeometryUtil.rotate(entranceCoords, rotation);
		// TODO wrap entrance calcu in method as it needs massaging
		if (entranceCoords.equals(new Coords(0, 0, 0))) {
			newEntrance = entranceCoords;
		}
		Treasure.LOGGER.debug("new entrance coords -> {}", newEntrance);

		/*
		 *  adjust spawn coords to line up room entrance with pit (pit is at spawn coords)
		 */
		BlockPos rotatedSize = template.getSize(rotation);
		Treasure.LOGGER.debug("rotated size -> {}", rotatedSize.toShortString());

		ICoords spawnCoords = ITemplateGenerator.alignEntranceToCoords(coords, newEntrance);
		Treasure.LOGGER.debug("aligned spawn coords -> {}", spawnCoords.toShortString());

		ICoords standardizedSpawnCoords = GeneratorUtil.standardizePosition(spawnCoords, rotatedSize, placement);
		Treasure.LOGGER.debug("new rotated standardized coords -> {}", standardizedSpawnCoords.toShortString());

		/**
		 * Environment Checks
		 */
		spawnCoords = WorldInfo.getDryLandSurfaceCoords(context.level(), context.chunkGenerator(), spawnCoords);

		Treasure.LOGGER.debug("surface (aligned) coords -> {}", spawnCoords.toShortString());
		if (spawnCoords == Coords.EMPTY) {
			return Optional.empty();
		}
		// update standardized with the correct y value -> same as aligned
		standardizedSpawnCoords = standardizedSpawnCoords.withY(spawnCoords.getY());
		Treasure.LOGGER.debug("surface (standardized) coords -> {}", standardizedSpawnCoords.toShortString());

		// TODO all this % base checking could be moved to method
		// if offset coords are set, move to that y for testing solid base
		if (offsetCoords != Coords.EMPTY) {
			standardizedSpawnCoords = standardizedSpawnCoords.add(0, offsetCoords.getY(), 0);
		}

		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(context.level(), standardizedSpawnCoords, rotatedSize.getX(), rotatedSize.getZ(), 70)) {
				if (i == 1) {
					Treasure.LOGGER.debug("coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", standardizedSpawnCoords.toShortString(), 70, rotatedSize.getX(), rotatedSize.getY());
					Optional<GeneratorResult<GeneratorData>> genResult = new GravestoneMarkerGenerator().generate(context, coords);
					return genResult;
				} else {
					standardizedSpawnCoords = standardizedSpawnCoords.add(0, -1, 0);
					spawnCoords = spawnCoords.add(0, -1, 0);
					Treasure.LOGGER.debug("move standardized spawn coords down for solid base check -> {}", standardizedSpawnCoords.toShortString());
				}
			} else {
				break;
			}
		}

		// move back by the amount of offsetCoords to get in the right position
		// (because the generate will apply the offset again)
		if (offsetCoords != Coords.EMPTY) {
			standardizedSpawnCoords = standardizedSpawnCoords.add(0, -offsetCoords.getY(), 0);
		}

		Treasure.LOGGER.debug("using solid base coords -> {}", standardizedSpawnCoords.toShortString());

		/**
		 * Build
		 */
		GeneratorResult<TemplateGeneratorData> genResult = new TemplateGenerator().generate(context, template, placement, spawnCoords, offsetCoords);
		if (!genResult.isSuccess()) {
			return Optional.empty();
		}

		/*
		 * adjust coords with offset and fill below
		 * NOTE the offset value here uses only what is provided by the meta value,
		 * it does not check the structure itself for a marker
		 */
		GeneratorUtil.fillBelow(context, genResult.getData().getSpawnCoords(), rotatedSize, 5, Blocks.DIRT.defaultBlockState());

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockInfoContext> spawnerContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockInfoContext> proximityContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		
		GeneratorUtil.buildVanillaSpawners(context, spawnerContexts);
		
		// populate proximity spawners
		GeneratorUtil.buildOneTimeSpawners(context, proximityContexts, new DoubleRange(1, 2), 10D);

		result.setData(genResult.getData());
		return Optional.of(result);
	}
	
	/**
	 * TODO this method is used in other structure generators... add the structurecategory and structureType to the params and abstract it out
	 * @param random
	 * @return
	 */
	@Deprecated
	private Optional<GottschTemplate> getRandomTemplate(Random random) {
		Optional<GottschTemplate> result = Optional.empty();
		
		List<TemplateHolder> holders = TreasureTemplateRegistry.getTemplate(StructureCategory.TERRANEAN, StructureType.MARKER);
	
		if (holders != null && !holders.isEmpty()) {
			TemplateHolder holder = holders.get(random.nextInt(holders.size()));
			GottschTemplate template = (GottschTemplate) holder.getTemplate();
			if (template == null) {
				Treasure.LOGGER.debug("could not find template");
				return Optional.empty();
			}
			Treasure.LOGGER.debug("selected template holder.location -> {}, tags -> {}", holder.getLocation(), holder.getTags());
			result = Optional.of(template);
		}
		return result;
	}
}