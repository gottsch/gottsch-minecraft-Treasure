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
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.spatial.ICoords;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO really this is the base class of Mist.. should deal with collisions at all. that can be a subclass
@OnlyIn(Dist.CLIENT)
public class MistParticle extends AbstractMistParticle {

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param velocityX
	 * @param velocityY
	 * @param velocityZ
	 * @param parentCoords
	 */
	public MistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ICoords parentCoords) {
		super(world, x, y, z);// , velocityX, velocityY, velocityZ);
		this.setParentEmitterCoords(parentCoords);
		init();
	}

	@Override
	public void init() {
		super.init();
	}

	// can be used to change the skylight+blocklight brightness of the rendered Particle.
	@Override
	public int getLightColor(float partialTick) {
		//	    final int BLOCK_LIGHT = 15;  // maximum brightness
		//	    final int SKY_LIGHT = 15;    // maximum brightness
		//	    final int FULL_BRIGHTNESS_VALUE = LightTexture.pack(BLOCK_LIGHT, SKY_LIGHT);
		//	    return FULL_BRIGHTNESS_VALUE;
		return 240 | 240 << 16;

	}

	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void  doPlayerCollisions(IWorld world) {
		return;
	}

	@Override
	public void inflictEffectOnPlayer(PlayerEntity player) {
		return;		
	}
}