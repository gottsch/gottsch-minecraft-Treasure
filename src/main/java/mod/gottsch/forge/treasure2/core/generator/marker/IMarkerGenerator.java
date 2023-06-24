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

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;
import mod.gottsch.forge.treasure2.core.structure.IStructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

/**
 * @author Mark Gottschling on Jan 27, 2019
 *
 */
public interface IMarkerGenerator<RESULT extends IGeneratorResult<?>> {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param config
	 * @return
	 */
	public abstract Optional<RESULT> generate(IWorldGenContext context, ICoords spawnCoords);

	/**
	 * 
	 * @param context
	 * @param coords
	 * @param category
	 * @param type
	 * @return
	 */
	default public Optional<TemplateHolder> selectTemplate(IWorldGenContext context, ICoords coords, IStructureCategory category, IStructureType type) {
		Optional<TemplateHolder> holder = Optional.empty();
		
		// get the biome ID
		Holder<Biome> biome = context.level().getBiome(coords.toPos());
		
		List<TemplateHolder> holders = TreasureTemplateRegistry.getTemplate(category, type, biome.value().getRegistryName());
		if (!holders.isEmpty()) {
			holder = Optional.ofNullable(holders.get(context.random().nextInt(holders.size())));
		}
		return holder;
	}
}