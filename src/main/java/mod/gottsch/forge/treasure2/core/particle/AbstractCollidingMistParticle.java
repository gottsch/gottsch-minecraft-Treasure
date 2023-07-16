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
import mod.gottsch.forge.treasure2.core.block.entity.MistEmitterBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

/**
 * 
 * @author Mark Gottschling on Aug 8, 2021
 *
 */
public abstract class AbstractCollidingMistParticle extends AbstractMistParticle implements ICollidingParticle {
	private ICoords sourceCoords;
	
	/**
	 * 
	 * @param level
	 * @param x
	 * @param y
	 * @param z
	 * @param coords
	 */
	public AbstractCollidingMistParticle(ClientLevel level, double x, double y, double z, ICoords coords) {
		super(level, x, y, z);
		setSourceCoords(coords);
	}

	@Override
	public void tick() {
		doPlayerCollisions(level);
		super.tick();
	}
	
	/**
	 * 
	 * @param world
	 */
	@Override
	public void doPlayerCollisions(Level world) {

		if (getSourceCoords() == null) {
			return;
		}

		// get the emitter tile entity
		BlockEntity emitterTileEntity = world.getBlockEntity(getSourceCoords().toPos());
		if (emitterTileEntity == null || !(emitterTileEntity instanceof MistEmitterBlockEntity)) {
			return;
		}

		// create an AxisAlignedBB for the particle
		AABB aabb = new AABB(x - 0.125D, y, z - 0.125D, x + 0.125D, y + 0.25D,
				z + 0.125D);

		// for all the players in the mist emitter tile entity list
		for (Player player : ((MistEmitterBlockEntity) emitterTileEntity).getPlayersWithinProximity()) {
			if (player.getBoundingBox().intersects(aabb)) {
				inflictEffectOnPlayer(player);
			}
		}
	}

	/**
	 * 
	 * @param player
	 */
	public abstract void inflictEffectOnPlayer(Player player);
	
	@Override
	public ICoords getSourceCoords() {
		return sourceCoords;
	}

	@Override
	public void setSourceCoords(ICoords parentEmitterCoords) {
		this.sourceCoords = parentEmitterCoords;
	}
}
