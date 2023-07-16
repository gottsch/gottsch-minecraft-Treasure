/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import org.apache.commons.lang3.builder.ToStringBuilder;

import mod.gottsch.forge.gottschcore.spatial.ICoords;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GeneratorData implements IGeneratorData {
	private ICoords spawnCoords;

	public GeneratorData() {}
	
	public GeneratorData(ICoords spawnCoords) {
		super();
		this.spawnCoords = spawnCoords;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public ICoords getSpawnCoords() {
		return spawnCoords;
	}

	public void setSpawnCoords(ICoords spawnCoords) {
		this.spawnCoords = spawnCoords;
	}
		
}
