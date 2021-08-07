/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import com.mojang.serialization.Codec;
import com.someguyssoftware.treasure2.particle.data.MistParticleData;

import net.minecraft.particles.ParticleType;

/**
 * @author Mark Gottschling on Jan 29, 2021
 *
 */
public class MistParticleType extends ParticleType<MistParticleData> {
	private static final boolean ALWAYS_SHOW = false;
	public MistParticleType() {
		super(ALWAYS_SHOW, MistParticleData.DESERIALIZER);
	}

	// get the Codec used to
	// a) convert a FlameParticleData to a serialised format
	// b) construct a FlameParticleData object from the serialised format
	@Override
	public Codec<MistParticleData> codec() {
		// TODO Auto-generated method stub
		return MistParticleData.codecMist();//CODEC;
	}
	
//ParticleType<MistParticleType> implements IParticleData {
//	// good call TGG - this will help to remind what the first param is for.
//	private static boolean ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER = false;
//	public MistParticleType() {		
//		super(ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER, );
//	}
//	public MistParticleType(boolean p_i50792_1_, IDeserializer<MistParticleType> p_i50792_2_) {
//		super(p_i50792_1_, p_i50792_2_);
//		// TODO Auto-generated constructor stub
//	}

}
