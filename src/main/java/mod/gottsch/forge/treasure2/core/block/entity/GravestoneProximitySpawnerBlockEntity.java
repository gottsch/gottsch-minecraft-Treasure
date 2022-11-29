/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block.entity;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.block.entity.ProximitySpawnerBlockEntity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.size.Quantity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class GravestoneProximitySpawnerBlockEntity extends ProximitySpawnerBlockEntity {
	private boolean hasEntity;
	
	/**
	 * 
	 * @param type
	 */
	public GravestoneProximitySpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.GRAVESTONE_PROXIMITY_SPAWNER_ENTITY_TYPE.get(), pos, state);
		setProximity(20D);
		setMobName(new ResourceLocation(Treasure.MODID, "bound_soul"));
		setMobNum(new Quantity(1, 1));
		setProximity(3D);
		setHasEntity(true); // TEMP true
		Treasure.LOGGER.debug("Created Gravestone tile entity");
	}

	/**
	 * NOTE this was not working when calling the super.update()
	 */
	public void tickServer() {
		boolean hasEntity = hasEntity();
		if (hasEntity && Config.SERVER.markers.enableGravestoneSpawner.get()) {

			// this is copied fromt he abstract
			if (this.level.isClientSide()) {
				return;
			}

			boolean isTriggered = false;
			double proximitySq = getProximity() * getProximity();
			if (proximitySq < 1) {
				proximitySq = 1;
			}

			// for each player
			for (Player player : getLevel().players()) {
				// get the distance
				double distanceSq = player.distanceToSqr(this.getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
				if (!isTriggered && !this.isDead() && (distanceSq < proximitySq)) {
					Treasure.LOGGER.debug("proximity @ -> {} was met.", new Coords(getBlockPos()).toShortString());
					isTriggered = true;
					// exectute action
					Treasure.LOGGER.debug("proximity pos -> {}", this.getBlockPos());
					execute(level, new Random(), new Coords(this.getBlockPos()), new Coords(player.blockPosition()));
					// NOTE: does not self-destruct that is up to the execute action to perform
				}
				if (this.isDead()) {
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void execute(Level world, Random random, ICoords blockCoords, ICoords playerCoords) {
		Treasure.LOGGER.debug("executing...");
		Treasure.LOGGER.debug("blockCoords -> {}, playerCoords -> {}", blockCoords.toShortString(), playerCoords.toShortString());
		int mobCount = RandomHelper.randomInt(random, getMobNum().getMinInt(), getMobNum().getMaxInt());

		for (int i = 0; i < mobCount; i++) {
			// create the mob entity
			BoundSoul mob = (BoundSoul)TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get().create(level);

			if (mob != null) {
				// set specific properties of the mob entity
				mob.restrictTo(blockCoords.toPos(), 16);
				Treasure.LOGGER.debug("attempted location for bound soul spawn -> {}", blockCoords.toShortString());
		        ModUtil.SpawnEntityHelper.spawn((ServerLevel)world, random, TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), mob, blockCoords);
			}
		}
		// self destruct
		selfDestruct();
	}

	/**
	 * 
	 */
	private void selfDestruct() {
		Treasure.LOGGER.debug("self destructing.");
		this.setDead(true);
		this.setHasEntity(false);
	}
	
	@Override
	public boolean isDead() {
		return super.isDead() || !hasEntity();
	}

	/**
	 * 
	 */
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		try {
			// read the custom name
			if (tag.contains("hasEntity", 8)) {
				this.hasEntity = tag.getBoolean("hasEntity");
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading GravestoneProximityBlockEntity properties from tag:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putBoolean("hasEntity", hasEntity());
	}

	/**
	 * Sync client and server states
	 */
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		if (tag != null) {
			load(tag);
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		handleUpdateTag(tag);
	}
	
	public boolean hasEntity() {
		return this.hasEntity;
	}

	public void setHasEntity(boolean hasEntity) {
		this.hasEntity = hasEntity;
	}
}