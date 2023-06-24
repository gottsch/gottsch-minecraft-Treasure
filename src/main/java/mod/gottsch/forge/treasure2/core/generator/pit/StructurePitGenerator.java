/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.pit;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import mod.gottsch.forge.gottschcore.size.Quantity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureProximitySpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.DungeonHooks;


/**
 * 
 * @author Mark Gottschling on Dec 9, 2018
 *
 */
public class StructurePitGenerator extends AbstractPitGenerator implements IStructurePitGenerator {
	
	private IPitGenerator<GeneratorResult<ChestGeneratorData>> generator;
	
	/**
	 * 
	 */
	public StructurePitGenerator() {

	}
	
	/**
	 * 
	 * @param generator
	 */
	public StructurePitGenerator(IPitGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		this();
		setGenerator(generator);
		Treasure.LOGGER.debug("using parent generator -> {}", generator.getClass().getSimpleName());
	}
	
	@Override
	public boolean generateEntrance(IWorldGenContext world, ICoords surfaceCoords, ICoords spawnCoords) {
		return getGenerator().generateEntrance(world, surfaceCoords, spawnCoords);
	}
	
	@Override
	public boolean generatePit(IWorldGenContext world, ICoords surfaceCoords, ICoords spawnCoords) {
		getGenerator().setOffsetY(0);
		return getGenerator().generatePit(world, surfaceCoords, spawnCoords);
	}
	
	/**
	 * 
	 * @param context
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		BlockState blockState = context.level().getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.LOGGER.debug("spawn coords is in a cavern.");
			inCavern = true;
		}
		
		if (inCavern) {
			Treasure.LOGGER.debug("shaft is in cavern... finding ceiling.");
			spawnCoords = GeneratorUtil.findSubterraneanCeiling(context.level(), spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.LOGGER.warn("unable to locate cavern ceiling.");
				return Optional.empty();
			}
			result.getData().setSpawnCoords(spawnCoords);
			// update the chest coords in the result
			result.getData().setCoords(spawnCoords);
		}
	
		// get distance to surface
		int verticalDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.LOGGER.debug("y-distance to surface =" + verticalDist);
		
		if (verticalDist > getMinSurfaceToSpawnDistance()) {
			Treasure.LOGGER.debug("generating structure room at -> {}", spawnCoords.toShortString());
			
			// get the biome
//			Biome biome = context.level().getBiome(spawnCoords.toPos());
//			ResourceLocation biomeID = biome.getRegistryName();
//			List<TemplateHolder> templateHolders = TreasureTemplateRegistry.getManager().getTemplatesByArchetypeTypeBiomeTable().get(key, biomeID);
			
			// TODO should the TemplateHolder be returned here instead?
			Optional<GottschTemplate> template = getRandomTemplate(context.random());
			if (!template.isPresent()) {
				Treasure.LOGGER.debug("could not find random template holder.");
				return Optional.empty();
			}			

			// find the (vertical) offset block
			int offset = 0;
			ICoords offsetCoords = TreasureTemplateRegistry.getOffset(context.random(), (GottschTemplate)template.get());
			if (offsetCoords != null) {
				offset = -offsetCoords.getY();
			}
			
			// check if the yDist is big enough to accodate a room
			BlockPos size = template.get().getSize();
			Treasure.LOGGER.debug("template size -> {}, offset -> {}", size, offset);
			
			// if size of room is greater the distance to the surface minus 3, then fail 
			if (size.getY() + offset + 3 >= verticalDist) {
				Treasure.LOGGER.debug("Structure's height is too large for available space.");
				// generate the base pit
				Optional<GeneratorResult<ChestGeneratorData>> pitResult = getGenerator().generate(context, surfaceCoords, spawnCoords);
				if (pitResult.isPresent()) {
					pitResult.get().getData().setCoords(pitResult.get().getData().getSpawnCoords());
					return pitResult;
				}
				else {
					Treasure.LOGGER.debug("Unable to generate base pit.");
					return Optional.empty();
				}
			}
	
			// find the entrance block
//			ICoords entranceCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.ENTRANCE));
			ICoords entranceCoords = TreasureTemplateRegistry.getOffsetFrom(context.random(), template.get(), StructureMarkers.ENTRANCE);
			if (entranceCoords == null) {
				Treasure.LOGGER.debug("Unable to locate entrance position.");
				return Optional.empty();
			}
			Treasure.LOGGER.debug("entrance coords -> {}", entranceCoords.toShortString());

			// TODO determine if the size of the structure on x-z axis will exceed the max generation size (ie 3x3 chunk size).
			
			// select a random rotation
			Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
			Treasure.LOGGER.debug("rotation used -> {}", rotation);
			
			// setup placement
			PlacementSettings placement = new PlacementSettings();
			placement.setRotation(rotation).setRandom(context.random());
			
			// NOTE these values are still relative to origin (spawnCoords);
			ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3()));
			Treasure.LOGGER.debug("new entrance coords -> {}", newEntrance.toShortString());
			
			/*
			 *  adjust spawn coords to line up room entrance and the pit
			 */
			BlockPos transformedSize = template.get().getSize(rotation);
			ICoords roomCoords = alignToPit(spawnCoords, newEntrance, transformedSize, placement);
			Treasure.LOGGER.debug("aligned room coords -> {}", roomCoords.toShortString());
			
			// generate the structure
			GeneratorResult<TemplateGeneratorData> genResult = new TemplateGenerator().generate(context, template.get(), placement, roomCoords);
			if (!genResult.isSuccess()) {
				return Optional.empty();
			}
			Treasure.LOGGER.debug("template result -> {}", genResult);
			result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
			
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
			if (bossChestContexts != null && bossChestContexts.size() > 0) {
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
				if (chestContexts != null && chestContexts.size() > 0) {
					if (chestContexts.size() > 1) {
						chestContext = chestContexts.get(context.random().nextInt(chestContexts.size()));
					}
					else {
						chestContext = chestContexts.get(0);
					}			
				}			
			}
			
			// update with chest context
			result.getData().setCoords(chestContext.getCoords());
			result.getData().setState(chestContext.getState());
			
			/*
			 *  TODO could lookup to some sort of map of structure -> spawner info
			 *  ex.	uses a Guava Table:
			 *  		map.put(ResourceLocation("treasure2:underground/basic1", SPAWNER, new SpawnerInfo("minecraft:Spider"));
			 *  		map.put(ResourceLocation("treasure2:underground/basic1", PROXIMITY, new SpawnerInfo("minecraft:Spider", new Quantity(1,2), 5D));
			 */
			
			// TODO move to own method
			// populate vanilla spawners
			for (BlockInfoContext c : spawnerContexts) {
				context.level().setBlock(c.getCoords().toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
				SpawnerBlockEntity blockEntity = (SpawnerBlockEntity) context.level().getBlockEntity(c.getCoords().toPos());
				EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
				blockEntity.getSpawner().setEntityId(r);
			}
			
			// TODO move to own method
			// populate proximity spawners
			for (BlockInfoContext c : proximityContexts) {
		    	context.level().setBlock(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.get().defaultBlockState(), 3);
		    	TreasureProximitySpawnerBlockEntity blockEntity = (TreasureProximitySpawnerBlockEntity) context.level().getBlockEntity(c.getCoords().toPos());
		    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
		    	blockEntity.setMobName(r.getRegistryName());
		    	blockEntity.setMobNum(new Quantity(1, 2));
		    	blockEntity.setProximity(5D);
			}

			// shaft enterance
			generateEntrance(context, surfaceCoords, spawnCoords.add(0, size.getY()+1, 0));
			
			// build the pit
			generatePit(context, surfaceCoords, spawnCoords.add(0, size.getY(), 0));
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (verticalDist >= 2) {
			// simple short pit
			return new SimpleShortPitGenerator().generate(context, surfaceCoords, spawnCoords);
		}		
		Treasure.LOGGER.debug("Generated Structure Pit at " + spawnCoords.toShortString());
		return Optional.ofNullable(result);
	}

	/**
	 * 
	 * @param random
	 * @return
	 */
	private Optional<GottschTemplate> getRandomTemplate(Random random) {
		Optional<GottschTemplate> result = Optional.empty();
		
		List<TemplateHolder> holders = TreasureTemplateRegistry.getTemplate(StructureCategory.SUBTERRANEAN, StructureType.ROOM);
	
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

	/**
	 * Aligns or "centers" the structure to the pit coords based on the structure's entrance coords.
	 * @param spawnCoords
	 * @param newEntrance
	 * @param transformedSize
	 * @param placement
	 * @return
	 */
	private ICoords alignToPit(ICoords spawnCoords, ICoords newEntrance, BlockPos transformedSize, PlacementSettings placement) {
		ICoords startCoords = null;
		// NOTE work with rotations only for now
		
		// first offset spawnCoords by newEntrance
		startCoords = spawnCoords.add(-newEntrance.getX(), 0, -newEntrance.getZ());
		
		// make adjustments for the rotation. REMEMBER that pits are 2x2
		switch (placement.getRotation()) {
		case CLOCKWISE_90:
			startCoords = startCoords.add(1, 0, 0);
			break;
		case CLOCKWISE_180:
			startCoords = startCoords.add(1, 0, 1);
			break;
		case COUNTERCLOCKWISE_90:
			startCoords = startCoords.add(0, 0, 1);
			break;
		default:
			break;
		}
		return startCoords;
	}
	
	/**
	 * @return the generator
	 */
	@Override
	public IPitGenerator<GeneratorResult<ChestGeneratorData>> getGenerator() {
		return generator;
	}

	/**
	 * @param generator the generator to set
	 */
	public void setGenerator(IPitGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		this.generator = generator;
	}
}