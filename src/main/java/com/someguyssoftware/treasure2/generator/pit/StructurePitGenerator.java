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
package com.someguyssoftware.treasure2.generator.pit;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DungeonHooks;


/**
 * 
 * @author Mark Gottschling on Dec 9, 2018
 *
 */
public class StructurePitGenerator extends AbstractPitGenerator {
	
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
	public boolean generateEntrance(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		return getGenerator().generateEntrance(world, random, surfaceCoords, spawnCoords);
	}
	
	@Override
	public boolean generatePit(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		getGenerator().setOffsetY(0);
		return getGenerator().generatePit(world, random, surfaceCoords, spawnCoords);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		BlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.LOGGER.debug("Spawn coords is in cavern.");
			inCavern = true;
		}
		
		if (inCavern) {
			Treasure.LOGGER.debug("Shaft is in cavern... finding ceiling.");
			spawnCoords = GenUtil.findUndergroundCeiling(world, spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.LOGGER.warn("Exiting: Unable to locate cavern ceiling.");
				return result.fail();
			}
			// update the chest coords in the result
			result.getData().setSpawnCoords(spawnCoords);
		}
	
		// get distance to surface
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.LOGGER.debug("Distance to ySurface =" + yDist);
		
		if (yDist > 6) {
			Treasure.LOGGER.debug("generating structure room at -> {}", spawnCoords.toShortString());
			
			// get structure by archetype (subterranean) and type (room)
			String key = StructureArchetype.SUBTERRANEAN.getName()
					+ ":" + StructureType.ROOM.getName();
			
			// get the biome
			Biome biome = world.getBiome(spawnCoords.toPos());
			ResourceLocation biomeID = biome.getRegistryName();
			TreasureTemplateRegistry.getTemplateManager();
			List<TemplateHolder> templateHolders = TreasureTemplateManager.getTemplatesByArchetypeTypeBiomeTable().get(key, biomeID);
			if (templateHolders == null || templateHolders.isEmpty()) {
				Treasure.LOGGER.debug("could not find template holders for archetype:type, biome -> {} [{}]:[]", key, biomeID, biome.toString());
				return result.fail();
			}
			
			TemplateHolder holder = templateHolders.get(random.nextInt(templateHolders.size()));
			if (holder == null) {
				Treasure.LOGGER.debug("could not find random template holder.");
				return result.fail();
			}
			
			GottschTemplate template = (GottschTemplate) holder.getTemplate();
			Treasure.LOGGER.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());
			if (template == null) {
				Treasure.LOGGER.debug("could not find random template");
				return result.fail();
			}
			
			// find the offset block
			int offset = 0;
//			ICoords offsetCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.OFFSET));
			ICoords offsetCoords = TreasureTemplateRegistry.getTemplateManager().getOffset(random, holder, StructureMarkers.OFFSET);
			if (offsetCoords != null) {
				offset = -offsetCoords.getY();
			}
			
			// check if the yDist is big enough to accodate a room
			BlockPos size = template.getSize();
			Treasure.LOGGER.debug("template size -> {}, offset -> {}", size, offset);
			
			// if size of room is greater the distance to the surface minus 3, then fail 
			if (size.getY() + offset + 3 >= yDist) {
				Treasure.LOGGER.debug("Structure's height is too large for available space.");
				// generate the base pit
				result = getGenerator().generate(world, random, surfaceCoords, spawnCoords);
				if (result.isSuccess()/*isGenerated*/) {
					result.getData().getChestContext().setCoords(result.getData().getSpawnCoords());
					return result;
				}
				else {
					Treasure.LOGGER.debug("Unable to generate base pit.");
					return result.fail();
				}
			}
	
			// find the entrance block
//			ICoords entranceCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.ENTRANCE));
			ICoords entranceCoords = TreasureTemplateRegistry.getTemplateManager().getOffset(random, holder, StructureMarkers.ENTRANCE);
			if (entranceCoords == null) {
				Treasure.LOGGER.debug("Unable to locate entrance position.");
				return result.fail();
			}
			Treasure.LOGGER.debug("entrance coords -> {}", entranceCoords.toShortString());

			// select a random rotation
			Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
			Treasure.LOGGER.debug("rotation used -> {}", rotation);
			
			// setup placement
			PlacementSettings placement = new PlacementSettings();
			placement.setRotation(rotation).setRandom(random);
			
			// NOTE these values are still relative to origin (spawnCoords);
			ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3d()));
			Treasure.LOGGER.debug("new entrance coords -> {}", newEntrance.toShortString());
			
			/*
			 *  adjust spawn coords to line up room entrance with pit
			 */
			BlockPos transformedSize = template.getSize(rotation);
			ICoords roomCoords = alignToPit(spawnCoords, newEntrance, transformedSize, placement);
			Treasure.LOGGER.debug("aligned room coords -> {}", roomCoords.toShortString());
			
			// generate the structure
			GeneratorResult<TemplateGeneratorData> genResult = new TemplateGenerator().generate(world, random, holder, placement, roomCoords);
			if (!genResult.isSuccess()) return result.fail();
			Treasure.LOGGER.debug("template result -> {}", genResult);
			result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
			
			// interrogate info for spawners and any other special block processing (except chests that are handler by caller
			List<BlockContext> spawnerContexts =
					(List<BlockContext>) genResult.getData().getMap().get(TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.SPAWNER));
			List<BlockContext> proximityContexts =
					(List<BlockContext>) genResult.getData().getMap().get(TreasureTemplateRegistry.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
			
			/*
			 *  TODO could lookup to some sort of map of structure -> spawner info
			 *  ex.	uses a Guava Table:
			 *  		map.put(ResourceLocation("treasure2:underground/basic1", SPAWNER, new SpawnerInfo("minecraft:Spider"));
			 *  		map.put(ResourceLocation("treasure2:underground/basic1", PROXIMITY, new SpawnerInfo("minecraft:Spider", new Quantity(1,2), 5D));
			 */
			
			// TODO move to own method
			// populate vanilla spawners
			for (BlockContext c : spawnerContexts) {
				world.setBlock(c.getCoords().toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
				MobSpawnerTileEntity te = (MobSpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
				EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
				te.getSpawner().setEntityId(r);
			}
			
			// TODO move to own method
			// populate proximity spawners
			for (BlockContext c : proximityContexts) {
		    	world.setBlock(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
		    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
		    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
		    	te.setMobName(r.getRegistryName());
		    	te.setMobNum(new Quantity(1, 2));
		    	te.setProximity(5D);
			}

			// shaft enterance
			generateEntrance(world, random, surfaceCoords, spawnCoords.add(0, size.getY()+1, 0));
			
			// build the pit
			generatePit(world, random, surfaceCoords, spawnCoords.add(0, size.getY(), 0));
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			result = new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}		
		Treasure.LOGGER.debug("Generated Structure Pit at " + spawnCoords.toShortString());
		return result.success();
	}

	/**
	 * 
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