/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;


/**
 * @author Mark Gottschling on Feb 16, 2020
 *
 */
public class MistEmitterBlockEntity extends BlockEntity {
	public static final float DEFAULT_PROXIMITY = 5.0F;
	private float proximity = DEFAULT_PROXIMITY;
	private boolean active = false;
	private List<Player> playersWithinProximity;

	/**
	 * 
	 */
	public MistEmitterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		playersWithinProximity = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void tickClient() {
        if (WorldInfo.isServerSide(level)) {
        	return;
        }

    	// get all players within range
		int x = getBlockPos().getX();
		int y = getBlockPos().getY();
		int z = getBlockPos().getZ();
		float radius = 5.0F;
        double proximitySq = getProximity() * getProximity();

        // clear list
        getPlayersWithinProximity().clear();
        setActive(false);
        
        // for each player
		for(Player player : getLevel().getEntitiesOfClass(Player.class, new AABB((double)((float)x - radius), (double)((float)y - radius), (double)((float)z - radius), 
		(double)((float)(x + 1) + radius), (double)((float)(y + 1) + radius), (double)((float)(z + 1) + radius)))) {
            // get the distance
            double distanceSq = player.distanceToSqr(x + 0.5D, y + 0.5D, z + 0.5D);
           
            if (distanceSq < proximitySq) {
            	// add player to list
            	getPlayersWithinProximity().add(player);
            	setActive(true);
            }
        }
	}
	
	/**
	 * 
	 */
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
	}
	
	/**
	 * 
	 */
	@Override
	public void saveAdditional(CompoundTag tag) {
	    super.saveAdditional(tag);
	}

	public synchronized List<Player> getPlayersWithinProximity() {
		return playersWithinProximity;
	}

	private synchronized void setPlayersWithinProximity(List<Player> playersWithinProximity) {
		this.playersWithinProximity = playersWithinProximity;
	}

	public synchronized boolean isActive() {
		return active;
	}

	public synchronized void setActive(boolean active) {
		this.active = active;
	}

	public float getProximity() {
		return proximity;
	}

	public void setProximity(float proximity) {
		this.proximity = proximity;
	}
}