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
package com.someguyssoftware.treasure2.util;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ILevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.server.ServerLevel;

/**
 * @author Mark Gottschling on Jul 22, 2021
 *
 */
public class ModUtils {
	public static ResourceLocation asLocation(String name) {
		return hasDomain(name) ? new ResourceLocation(name) : new ResourceLocation(Treasure.MODID, name);
	}

	public static boolean hasDomain(String name) {
		return name.indexOf(":") >= 0;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Jul 25, 2021
	 *
	 */
	public static class SpawnHelper {
		/**
		 * 
		 * @param world
		 * @param entity
		 * @param itemStack
		 * @param player
		 * @param pos
		 * @param spawnReason
		 * @param flag
		 * @param offsetFlag
		 * @return
		 */
		@Nullable
		public static Entity spawn(ServerLevel world, Entity entity, @Nullable ItemStack itemStack, @Nullable Player player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offsetFlag) {
			return spawn(world, entity, itemStack == null ? null : itemStack.getTag(), itemStack != null && itemStack.hasCustomHoverName() ? itemStack.getHoverName() : null, player, pos, spawnReason, flag, offsetFlag);
		}

		/**
		 * 
		 * @param world
		 * @param entity
		 * @param compoundNbt
		 * @param textComponent
		 * @param player
		 * @param pos
		 * @param spawnReason
		 * @param flag
		 * @param offsetFlag
		 * @return
		 */
		@Nullable
		public static Entity spawn(ServerLevel world, Entity entity,@Nullable CompoundTag compoundNbt, @Nullable TextComponent textComponent, @Nullable Player player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offsetFlag) {
			Entity t = create(world, entity, compoundNbt, textComponent, player, pos, spawnReason, flag, offsetFlag);
			if (t != null) {
				if (t instanceof net.minecraft.entity.Mob && net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn((net.minecraft.entity.Mob) t, world, pos.getX(), pos.getY(), pos.getZ(), null, spawnReason)) return null;
				world.addFreshEntityWithPassengers(t);
			}
			return t;
		}

		/**
		 * Modified from vanilla EntityType
		 * @param world
		 * @param entity
		 * @param compound
		 * @param textComponent
		 * @param player
		 * @param pos
		 * @param spawnReason
		 * @param flag
		 * @param offfsetFlag
		 * @return
		 */
		@Nullable
		public static Entity create(ServerLevel world, Entity entity, @Nullable CompoundTag compound, @Nullable TextComponent textComponent, 
				@Nullable Player player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offfsetFlag) {

			if (entity == null) {
				return null;
			} else {
				double d0;
				if (flag) {
					entity.setPos((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
					d0 = getYOffset(world, pos, offfsetFlag, entity.getBoundingBox());
				} else {
					d0 = 0.0D;
				}

				entity.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
				if (entity instanceof Mob) {
					Mob mobentity = (Mob)entity;
					mobentity.yHeadRot = mobentity.yRot;
					mobentity.yBodyRot = mobentity.yRot;
					mobentity.finalizeSpawn(world, world.getCurrentDifficultyAt(mobentity.blockPosition()), spawnReason, (ILivingEntityData)null, compound);
					mobentity.playAmbientSound();
				}

				if (textComponent != null && entity instanceof LivingEntity) {
					entity.setCustomName(textComponent);
				}

				updateCustomEntityTag(world, player, entity, compound);
				return entity;
			}
		}

		/**
		 * Originally from vanilla EntityType
		 * @param world
		 * @param pos
		 * @param flag
		 * @param aabb
		 * @return
		 */
		protected static double getYOffset(ILevelReader world, BlockPos pos, boolean flag, AABB aabb) {
			AABB axisalignedbb = new AABB(pos);
			if (flag) {
				axisalignedbb = axisalignedbb.expandTowards(0.0D, -1.0D, 0.0D);
			}

			Stream<VoxelShape> stream = world.getCollisions((Entity)null, axisalignedbb, (p_233596_0_) -> {
				return true;
			});
			return 1.0D + VoxelShapes.collide(Direction.Axis.Y, aabb, stream, flag ? -2.0D : -1.0D);
		}

		/**
		 * Originally from vanilla EntityType
		 * @param world
		 * @param player
		 * @param entity
		 * @param compoundNbt
		 */
		protected static void updateCustomEntityTag(Level world, @Nullable Player player, @Nullable Entity entity, @Nullable CompoundTag compoundNbt) {
			if (compoundNbt != null && compoundNbt.contains("EntityTag", 10)) {
				MinecraftServer minecraftserver = world.getServer();
				if (minecraftserver != null && entity != null) {
					if (world.isClientSide || !entity.onlyOpCanSetNbt() || player != null && minecraftserver.getPlayerList().isOp(player.getGameProfile())) {
						CompoundTag compoundnbt = entity.saveWithoutId(new CompoundTag());
						UUID uuid = entity.getUUID();
						compoundnbt.merge(compoundNbt.getCompound("EntityTag"));
						entity.setUUID(uuid);
						entity.load(compoundnbt);
					}
				}
			}
		}
	}
}
