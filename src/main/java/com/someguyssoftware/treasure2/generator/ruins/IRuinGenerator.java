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
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 23, 2019
 *
 */
public interface IRuinGenerator<RESULT extends IGeneratorResult<?>> {

	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords spawnCoords);	
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords originalSpawnCoords,
			IDecayRuleSet decayRuleSet);
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder);
	GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random, ICoords originalSpawnCoords,
			TemplateHolder holder, IDecayRuleSet decayRuleSet);

	default public TemplateHolder selectTemplate(IServerWorld world, Random random, ICoords coords, StructureArchetype archetype, StructureType type) {
		// get the biome ID
		Biome biome = world.getBiome(coords.toPos());
		TemplateHolder holder = TreasureTemplateRegistry.getManager().getTemplate(random, archetype, type, biome);
		return holder;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param proximityContexts
	 * @param quantity
	 * @param d
	 */
	default public void buildOneTimeSpawners(IServerWorld world, Random random, List<BlockContext> proximityContexts, Quantity quantity, double d) {
		for (BlockContext c : proximityContexts) {
			Treasure.LOGGER.debug("placing proximity spawner at -> {}", c.getCoords().toShortString());
	    	world.setBlock(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
	    	if (te != null) {
	 	    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
		    	Treasure.LOGGER.debug("using mob -> {} for poximity spawner.", r.getRegistryName().toString());
		    	te.setMobName(r.getRegistryName());
		    	te.setMobNum(new Quantity(1, 2));
		    	te.setProximity(5D);
	    	}
	    	else {
	    		Treasure.LOGGER.debug("unable to generate proximity spawner at -> {}", c.getCoords().toShortString());
	    	}
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnerContexts
	 */
	default public void buildVanillaSpawners(IServerWorld world, Random random, List<BlockContext> spawnerContexts) {
		for (BlockContext c : spawnerContexts) {
			Treasure.LOGGER.debug("placing vanilla spawner at -> {}", c.getCoords().toShortString());
			world.setBlock(c.getCoords().toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			MobSpawnerTileEntity te = (MobSpawnerTileEntity) world.getBlockEntity(c.getCoords().toPos());
			if (te != null) {
				EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
				te.getSpawner().setEntityId(r);
			}
			else {
				Treasure.LOGGER.debug("unable to generate vanilla spawner at -> {}", c.getCoords().toShortString());
			}
		}
	}
}