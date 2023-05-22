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

import mod.gottsch.forge.gottschcore.size.Quantity;
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
import mod.gottsch.forge.treasure2.core.block.entity.TreasureProximitySpawnerBlockEntity;
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
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.common.DungeonHooks;

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

		// get the template from the given archetype, type and biome
//		TemplateHolder holder = TreasureTemplateRegistry.getManager().getTemplate(random, StructureArchetype.SURFACE, StructureType.MARKER, biome);
//		if (holder == null) {
//			return result.fail();
//		}
		Optional<GottschTemplate> template = getRandomTemplate(context.random());
		if (!template.isPresent()) {
			Treasure.LOGGER.debug("could not find random template holder.");
			return Optional.empty();
		}	
		

		// TODO could move offset to TemplateGenerator : getOffset() which checks both the offsetblock and the meta
		// get the offset
		int offset = 0;
//		ICoords offsetCoords = ((GottschTemplate2)holder.getTemplate()).findCoords(random, TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.OFFSET));
		ICoords offsetCoords = TreasureTemplateRegistry.getOffsetFrom(context.random(), template.get(), StructureMarkers.OFFSET);
		if (offsetCoords != null) {
			offset = -offsetCoords.getY();
		}
		
		// find entrance
//		ICoords entranceCoords =((GottschTemplate2)holder. getTemplate()).findCoords(random, TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.ENTRANCE));
		ICoords entranceCoords =TreasureTemplateRegistry.getOffsetFrom(context.random(), template.get(), StructureMarkers.ENTRANCE);
		if (entranceCoords == null) {
			Treasure.LOGGER.debug("Unable to locate entrance position.");
			return Optional.empty();
		}

		// select a rotation
		Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("above ground rotation used -> {}", rotation);
				
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(context.random());
		
		// TODO move into TemplateGenerator
		// NOTE these values are still relative to origin (spawnCoords);
		ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3()));
		
		/*
		 *  adjust spawn coords to line up room entrance with pit
		 */
		BlockPos transformedSize = template.get().getSize(rotation);
		ICoords spawnCoords = ITemplateGenerator.alignEntranceToCoords(/*spawnCoords*/coords, newEntrance, transformedSize, placement);
				
		// if offset is 2 or less, then determine if the solid ground percentage is valid
		if (offset >= -2) {
			if (!WorldInfo.isSolidBase(context.level(), spawnCoords, transformedSize.getX(), transformedSize.getZ(), 70)) {
				Treasure.LOGGER.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", 70, spawnCoords.toShortString(), transformedSize.getX(), transformedSize.getY());
				 Optional<GeneratorResult<GeneratorData>> genResult = new GravestoneMarkerGenerator().generate(context, coords);
				 return genResult;
			}
		}

		// generate the structure
		GeneratorResult<TemplateGeneratorData> genResult = new TemplateGenerator().generate(context, template.get(), placement, spawnCoords);
		if (!genResult.isSuccess()) {
			return Optional.empty();
		}

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockInfoContext> spawnerContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockInfoContext> proximityContexts =
				(List<BlockInfoContext>) genResult.getData().getMap().get(GeneratorUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		
		// TODO exact same as SubmergedRuinGenerator... need to put them in an abstract/interface common to all structure generators
		// populate vanilla spawners
		for (BlockInfoContext c : spawnerContexts) {
			ICoords c2 = spawnCoords.add(c.getCoords());
			context.level().setBlock(c2.toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			SpawnerBlockEntity te = (SpawnerBlockEntity) context.level().getBlockEntity(c2.toPos());
			EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
			te.getSpawner().setEntityId(r);
		}
		
		// populate proximity spawners
		for (BlockInfoContext c : proximityContexts) {
			ICoords c2 = spawnCoords.add(c.getCoords());
	    	context.level().setBlock(c2.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.get().defaultBlockState(), 3);
	    	TreasureProximitySpawnerBlockEntity te = (TreasureProximitySpawnerBlockEntity) context.level().getBlockEntity(c2.toPos());
	    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
	    	te.setMobName(r.getRegistryName());
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(10D);
//		    Treasure.LOGGER.debug("Creating proximity spawner @ {} -> [mobName={}, spawnRange={}", c.getCoords().toShortString(), r, te.getProximity());
		}		

		result.setData(genResult.getData());
		return Optional.of(result);
	}
	
	/**
	 * TODO this method is used in other structure generators... add the structurecategory and structureType to the params and abstract it out
	 * @param random
	 * @return
	 */
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