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
package com.someguyssoftware.treasure2.tileentity;

import java.util.Objects;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerBlockEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.TreasureEntities;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.server.ServerLevel;

/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class GravestoneProximitySpawnerTileEntity extends ProximitySpawnerTileEntity {
	private boolean hasEntity;

	public GravestoneProximitySpawnerTileEntity() {
		this(TreasureTileEntities.GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE);
	}
	
	/**
	 * 
	 * @param type
	 */
	public GravestoneProximitySpawnerTileEntity(TileEntityType<?> type) {
		super(type);
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
	@Override
	public void tick() {
		boolean hasEntity = hasEntity();
		if (hasEntity && TreasureConfig.MARKERS.gravestoneSpawnMobAllowed.get()) {

			// this is copied fromt he abstract
			if (this.level.isClientSide()) {
				return;
			}

			boolean isTriggered = false;
			double proximitySq = getProximity() * getProximity();
			if (proximitySq < 1)
				proximitySq = 1;

			// for each player
			for (Player player : getLevel().players()) {
				// get the distance
				double distanceSq = player.distanceToSqr(this.getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
				if (!isTriggered && !this.isDead() && (distanceSq < proximitySq)) {
					Treasure.LOGGER.debug("PSTE proximity @ -> {} was met.", new Coords(getBlockPos()).toShortString());
					isTriggered = true;
					// exectute action
					Treasure.LOGGER.debug("Proximity.getBlockPos() -> {}", this.getBlockPos());
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
			BoundSoulEntity entity = new BoundSoulEntity(world, blockCoords.toPos());
			entity.restrictTo(blockCoords.toPos(), 16);
//			if (entity == null) {
//				Treasure.LOGGER.debug("unable to create entity -> {}", getMobName());
//				selfDestruct();
//				return;
//			}

			if (entity instanceof LivingEntity) {
				double x = (double) blockCoords.getX() + (world.random.nextDouble() * getProximity())
						- getProximity() / 2 + 0.5D;
				double y = (double) (blockCoords.getY());// + world.rand.nextInt(3) - 1);
				double z = (double) blockCoords.getZ() + (world.random.nextDouble() * getProximity())
						- getProximity() / 2 + 0.5D;
				BlockPos newPos = new BlockPos(x, y, z);
				Treasure.LOGGER.debug("attempted location for bound soul spawn -> {} {} {}", x, y, z);

		        ModUtils.SpawnHelper.spawn((ServerLevel)world, entity, null, null, newPos, SpawnReason.SPAWNER, true, false);
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
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		try {
			// read the custom name
			if (nbt.contains("hasEntity", 8)) {
				this.hasEntity = nbt.getBoolean("hasEntity");
//				Treasure.LOGGER.debug("value of nbt entity -> {}", nbt.getBoolean("hasEntity"));
			}
//		    Treasure.LOGGER.debug("reading proximity spawner @ {} -> [hasEntity={}, mobName={}, mobName={}", this.pos, nbt.getBoolean("hasEntity"), nbt.getString("mobName"), this.getMobName());
		} catch (Exception e) {
			Treasure.LOGGER.error("Error reading AbstractProximity properties from NBT:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putBoolean("hasEntity", hasEntity());
//	    Treasure.LOGGER.debug("writing proximity spawner @ {} -> [hasEntity={}, mobName={}, spawnRange={}", this.pos, tag.getBoolean("hasEntity"), tag.getString("mobName"), tag.getDouble("spawnRange"));

		return nbt;
	}

	public boolean hasEntity() {
		return this.hasEntity;
	}

	public void setHasEntity(boolean hasEntity) {
		this.hasEntity = hasEntity;
	}
}