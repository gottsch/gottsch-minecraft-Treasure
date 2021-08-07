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

import javax.annotation.Nonnull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.TreasureParticles;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleType;

/**
 * @author Mark Gottschling on Jan 30, 2021
 *
 */
public class MistParticleData extends AbstractMistParticleData {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public MistParticleData(int x, int y, int z) {
		super(x, y, z);
		Treasure.LOGGER.info("particle data creating...");
	}
	
	/**
	 * 
	 * @param parentEmitterCoords
	 */
	public MistParticleData(ICoords coords) {
		super(coords);
	}

	@Nonnull
	@Override
	public ParticleType<MistParticleData> getType() {
		return TreasureParticles.MIST_PARTICLE_TYPE.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Codec<MistParticleData> codecMist() {
		return RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.INT.fieldOf("x").forGetter(d -> d.getSourceCoords().getX()),
						Codec.INT.fieldOf("y").forGetter(d -> d.getSourceCoords().getY()),
						Codec.INT.fieldOf("z").forGetter(d -> d.getSourceCoords().getZ())
						).apply(instance, MistParticleData::new));
	}

	// The DESERIALIZER is used to construct MistParticleData from either command line parameters or from a network packet

	public static final IDeserializer<MistParticleData> DESERIALIZER = new IDeserializer<MistParticleData>() {

		// parse the parameters for this particle from a /particle command
		@Nonnull
		@Override
		public MistParticleData fromCommand(@Nonnull ParticleType<MistParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			Treasure.LOGGER.info("particle data deserializing...");
			reader.expect(' ');
			int x = reader.readInt();
			reader.expect(' ');
			int y = reader.readInt();
			reader.expect(' ');
			int z = reader.readInt();
			ICoords coords = new Coords(x, y, z);

			return new MistParticleData(coords);
		}

		// read the particle information from a PacketBuffer after the client has received it from the server
		@Override
		public MistParticleData fromNetwork(@Nonnull ParticleType<MistParticleData> type, PacketBuffer buf) {
			Treasure.LOGGER.info("particle reading from buffer ...");
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			ICoords coords = new Coords(x, y, z);
			return new MistParticleData(coords);
		}
	};
}
