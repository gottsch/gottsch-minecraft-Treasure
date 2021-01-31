/**
 * 
 */
package com.someguyssoftware.treasure2.particle;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
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
