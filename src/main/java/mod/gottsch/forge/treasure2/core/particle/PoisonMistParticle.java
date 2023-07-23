/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.particle;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.core.network.PoisonMistMessageToServer;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling2021
 *
 */
public class PoisonMistParticle extends AbstractCollidingMistParticle {

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
	public PoisonMistParticle(ClientLevel world, double x, double y, double z, ICoords coords) {
		super(world, x, y, z, coords);
		init();
	}

	@Override
	public float getMistAlpha() {
		return 0.4F;
	}
	
	/**
	 * 
	 */
	@Override
	public void inflictEffectOnPlayer(Player player) {
		if (WorldInfo.isServerSide(player.level())) {
			return;
		}

		// check all player effects for poison
		boolean isAffected = false;
		for (MobEffectInstance effectInstance : player.getActiveEffects()) {
			if (effectInstance.getEffect() == MobEffects.POISON) {
				isAffected = true;
			}
		}

		// if player does not have poison effect, add it
		if (!isAffected) {
			PoisonMistMessageToServer messageToServer = new PoisonMistMessageToServer(player.getStringUUID());
			TreasureNetworking.channel.sendToServer(messageToServer);

		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<CollidingParticleType> {
		/*
		 * contains a list of textures; choose one using either
		 * newParticle.selectSpriteRandomly(spriteSet); or newParticle.selectSpriteWithAge(spriteSet);
		 */
		private final SpriteSet spriteSet;
		
		/**
		 * 
		 * @param sprite
		 */
		public Provider(SpriteSet sprite) {
			this.spriteSet = sprite;
		}
		
		/**
		 * 
		 */
		@Override
		public Particle createParticle(CollidingParticleType data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			PoisonMistParticle particle = new PoisonMistParticle(world, x, y, z, data.getSourceCoords());
			particle.pickSprite(spriteSet);
			return particle;
		}
	}
}
