/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import net.minecraft.world.level.block.Block;


/**
 * 
 * @author Mark Gottschling on Feb 7, 2020
 *
 */
public class TemplateGeneratorData extends GeneratorData {
	/*
	 * size of the structure represented by ICoords
	 */
	private ICoords size;
	
	/*
	 * map by block. this method assumes that a list of block will be provided to scan for.
	 */
	private Multimap<Block, BlockInfoContext> map = ArrayListMultimap.create();

	public ICoords getSize() {
		return size;
	}

	public void setSize(ICoords size) {
		this.size = size;
	}

	public Multimap<Block, BlockInfoContext> getMap() {
		return map;
	}

	public void setMap(Multimap<Block, BlockInfoContext> map) {
		this.map = map;
	}
}
