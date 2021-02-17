/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
	private ICoords parentEmitterCoords;

	/**
	 * 
	 * @param parentEmitterCoords
	 */
	public MistParticleData(ICoords parentEmitterCoords) {
		Treasure.LOGGER.info("particle data creating...");
		this.parentEmitterCoords = parentEmitterCoords;
	}
	
	public ICoords getParentEmitterCoords() {
		return parentEmitterCoords;
	}

	@Override
	public ParticleType<MistParticleData> getType() {
		return TreasureParticles.mistParticleType;
	}

	@Override
	public void write(PacketBuffer buffer) {
		Treasure.LOGGER.info("particle writing to buffer...{}", parentEmitterCoords.toShortString());
		buffer.writeInt(parentEmitterCoords.getX());
		buffer.writeInt(parentEmitterCoords.getY());
		buffer.writeInt(parentEmitterCoords.getZ());
	}

	@Override
	public String getParameters() {
	    return String.format(Locale.ROOT, "%s %s",
	            this.getType().getRegistryName(), parentEmitterCoords.toShortString());
	}
	
	// The DESERIALIZER is used to construct FlameParticleData from either command line parameters or from a network packet

	  public static final IDeserializer<MistParticleData> DESERIALIZER = new IDeserializer<MistParticleData>() {

	    // parse the parameters for this particle from a /particle command
	    @Nonnull
	    @Override
	    public MistParticleData deserialize(@Nonnull ParticleType<MistParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
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
	    public MistParticleData read(@Nonnull ParticleType<MistParticleData> type, PacketBuffer buf) {
	    	Treasure.LOGGER.info("particle reading from buffer ...");
	      int x = buf.readInt();
	      int y = buf.readInt();
	      int z = buf.readInt();
	      ICoords coords = new Coords(x, y, z);
	      return new MistParticleData(coords);
	    }
	  };
}
