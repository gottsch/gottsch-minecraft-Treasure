/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;

/**
 * @author Mark Gottschling on Jan 30, 2021
 *
 */
public class MistParticleData implements IParticleData {
	private ICoords sourceCoords;

	public static final Codec<MistParticleData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.INT.fieldOf("x").forGetter(d -> d.getSourceCoords().getX()),
					Codec.INT.fieldOf("y").forGetter(d -> d.getSourceCoords().getY()),
					Codec.INT.fieldOf("z").forGetter(d -> d.getSourceCoords().getZ())
					).apply(instance, MistParticleData::new)
			);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public MistParticleData(int x, int y, int z) {
		Treasure.LOGGER.info("particle data creating...");
		this.sourceCoords = new Coords(x, y, z);
	}
	
	/**
	 * 
	 * @param parentEmitterCoords
	 */
	public MistParticleData(ICoords coords) {
		Treasure.LOGGER.info("particle data creating...");
		this.sourceCoords = coords;
	}
	
	public ICoords getSourceCoords() {
		return sourceCoords;
	}

	@Override
	public ParticleType<MistParticleData> getType() {
		return TreasureParticles.mistParticleType;
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
	
	// The DESERIALIZER is used to construct FlameParticleData from either command line parameters or from a network packet

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
