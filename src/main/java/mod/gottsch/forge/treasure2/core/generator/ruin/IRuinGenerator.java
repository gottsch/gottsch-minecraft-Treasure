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

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;
import mod.gottsch.forge.treasure2.core.structure.IStructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

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
}