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
package com.someguyssoftware.treasure2.particle.data;

import java.util.Locale;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;

public abstract class AbstractMistParticleData  implements IParticleData {
	private ICoords sourceCoords;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public AbstractMistParticleData(int x, int y, int z) {
		Treasure.LOGGER.info("abstract particle data creating...");
		this.sourceCoords = new Coords(x, y, z);
	}
	
	/**
	 * 
	 * @param parentEmitterCoords
	 */
	public AbstractMistParticleData(ICoords coords) {
		this.sourceCoords = coords;
	}

	@Override
	public void writeToNetwork(PacketBuffer buffer) {
		Treasure.LOGGER.info("particle writing to buffer...{}", sourceCoords.toShortString());
		buffer.writeInt(sourceCoords.getX());
		buffer.writeInt(sourceCoords.getY());
		buffer.writeInt(sourceCoords.getZ());
	}
	
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %s",
				this.getType().getRegistryName(), sourceCoords.toShortString());
	}
	
	public ICoords getSourceCoords() {
		return sourceCoords;
	}

	public void setSourceCoords(ICoords sourceCoords) {
		this.sourceCoords = sourceCoords;
	}	
}
