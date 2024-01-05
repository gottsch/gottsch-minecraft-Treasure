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
package mod.gottsch.forge.treasure2.core.particle.data;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class CollidingParticleType extends ParticleType<CollidingParticleType> implements IParticleData, ICollidingParticleType {
	private ICoords sourceCoords;
	
	/*
	 *  The DESERIALIZER is used to construct MistParticleData from either command line parameters or from a network packet
	 */
	public static final IDeserializer<CollidingParticleType> DESERIALIZER = new IDeserializer<CollidingParticleType>() {

		// parse the parameters for this particle from a /particle command
		@Nonnull
		@Override
		public CollidingParticleType fromCommand(@Nonnull ParticleType<CollidingParticleType> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			int x = reader.readInt();
			reader.expect(' ');
			int y = reader.readInt();
			reader.expect(' ');
			int z = reader.readInt();
			ICoords coords = new Coords(x, y, z);

			return new CollidingParticleType(coords, DESERIALIZER);
		}

		@Override
		public CollidingParticleType fromNetwork(ParticleType<CollidingParticleType> type,	PacketBuffer buf) {
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			ICoords coords = new Coords(x, y, z);
			return new CollidingParticleType(coords, DESERIALIZER);
		}
	};	
	
	/**
	 * 
	 * @param deserializer
	 */
	public CollidingParticleType(ICoords coords, IDeserializer<CollidingParticleType> deserializer) {
		super(false, deserializer);
		this.setSourceCoords(coords);
		Treasure.LOGGER.info("colliding particle data creating from coords, deserializer...");
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public CollidingParticleType(int x, int y, int z) {
		super(false, DESERIALIZER);
		this.sourceCoords = new Coords(x, y, z);
	}

	@Override
	public Codec<CollidingParticleType> codec() {
		return RecordCodecBuilder.create(
		instance -> instance.group(
				Codec.INT.fieldOf("x").forGetter(d ->d.getSourceCoords().getX()),
				Codec.INT.fieldOf("y").forGetter(d -> d.getSourceCoords().getY()),
				Codec.INT.fieldOf("z").forGetter(d -> d.getSourceCoords().getZ())
				).apply(instance, CollidingParticleType::new));
	}
	
	@Override
	public ParticleType<?> getType() {
		return this;
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
	
	@Override
	public ICoords getSourceCoords() {
		return sourceCoords;
	}

	@Override
	public void setSourceCoords(ICoords emitterCoords) {
		this.sourceCoords = emitterCoords;
	}
}
