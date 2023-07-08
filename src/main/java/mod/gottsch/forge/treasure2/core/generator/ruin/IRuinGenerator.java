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
package mod.gottsch.forge.treasure2.core.generator.ruin;

import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.size.DoubleRange;
import mod.gottsch.forge.gottschcore.size.Quantity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureProximitySpawnerBlockEntity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;
import mod.gottsch.forge.treasure2.core.structure.IStructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 23, 2019
 *
 */
public interface IRuinGenerator<RESULT extends IGeneratorResult<?>> {

	Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords spawnCoords);	
	Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords originalSpawnCoords, TemplateHolder holder);

	/**
	 * 
	 * @param context
	 * @param coords
	 * @param category
	 * @param type
	 * @return
	 */
	// TODO should be common to all ITemplateGenerators
	default public Optional<TemplateHolder> selectTemplate(IWorldGenContext context, ICoords coords, IStructureCategory category, IStructureType type) {
		Optional<TemplateHolder> holder = Optional.empty();
		
		// get the biome ID
		Holder<Biome> biome = context.level().getBiome(coords.toPos());
		
		List<TemplateHolder> holders = TreasureTemplateRegistry.getTemplate(category, type, ModUtil.getName(biome));
		if (!holders.isEmpty()) {
			holder = Optional.ofNullable(holders.get(context.random().nextInt(holders.size())));
		}
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
	default public void buildOneTimeSpawners(IWorldGenContext context, List<BlockInfoContext> proximityContexts, Quantity quantity, double proximity) {
		for (BlockInfoContext c : proximityContexts) {
			Treasure.LOGGER.debug("placing proximity spawner at -> {}", c.getCoords().toShortString());
	    	context.level().setBlock(c.getCoords().toPos(), TreasureBlocks.PROXIMITY_SPAWNER.get().defaultBlockState(), 3);
	    	TreasureProximitySpawnerBlockEntity te = (TreasureProximitySpawnerBlockEntity) context.level().getBlockEntity(c.getCoords().toPos());
	    	if (te != null) {	    		
	 	    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
	 	    	if (RandomHelper.checkProbability(context.random(), 20)) {
	 	    		r = EntityType.VINDICATOR;
	 	    	}
		    	Treasure.LOGGER.debug("using mob -> {} for poximity spawner.", EntityType.getKey(r).toString());
		    	te.setMobName(EntityType.getKey(r));
		    	te.setMobNum(new DoubleRange(1, 2));
		    	te.setProximity(proximity);
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
	default public void buildVanillaSpawners(IWorldGenContext context, List<BlockInfoContext> spawnerContexts) {
		for (BlockInfoContext c : spawnerContexts) {
			Treasure.LOGGER.debug("placing vanilla spawner at -> {}", c.getCoords().toShortString());
			context.level().setBlock(c.getCoords().toPos(), Blocks.SPAWNER.defaultBlockState(), 3);
			SpawnerBlockEntity te = (SpawnerBlockEntity) context.level().getBlockEntity(c.getCoords().toPos());
			if (te != null) {
				EntityType<?> r = DungeonHooks.getRandomDungeonMob(context.random());
				te.getSpawner().setEntityId(r, context.level().getLevel(), context.random(), c.getCoords().toPos());
			}
			else {
				Treasure.LOGGER.debug("unable to generate vanilla spawner at -> {}", c.getCoords().toShortString());
			}
		}
	}
}