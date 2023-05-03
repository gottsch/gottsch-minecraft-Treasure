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

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateHolder;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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

	// TODO move to GottschCore
	/**
	 * Vanilla NBTUtil method with some extra checks.
	 * @param nbt
	 * @return
	 */
	public static BlockState readBlockState(CompoundNBT nbt) {
		if (!nbt.contains("Name", 8)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			String name = nbt.getString("Name");
			if (name.equalsIgnoreCase("minecraft:%%FILTER_ME%%")) {
				return Blocks.AIR.defaultBlockState();
			}
			Block block = Registry.BLOCK.get(new ResourceLocation(name));
			BlockState blockstate = block.defaultBlockState();
			if (nbt.contains("Properties", 10)) {
				CompoundNBT compoundnbt = nbt.getCompound("Properties");
				StateContainer<Block, BlockState> statecontainer = block.getStateDefinition();

				for(String s : compoundnbt.getAllKeys()) {
					Property<?> property = statecontainer.getProperty(s);
					if (property != null) {
						blockstate = setValueHelper(blockstate, property, s, compoundnbt, nbt);
					}
				}
			}
			return blockstate;
		}
	}

	// TODO move to GottschCore
	/**
	 * Vanilla method from NBTUtil.
	 * @param <S>
	 * @param <T>
	 * @param p_193590_0_
	 * @param p_193590_1_
	 * @param p_193590_2_
	 * @param p_193590_3_
	 * @param p_193590_4_
	 * @return
	 */
	private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S p_193590_0_, Property<T> p_193590_1_, String p_193590_2_, CompoundNBT p_193590_3_, CompoundNBT p_193590_4_) {
		Optional<T> optional = p_193590_1_.getValue(p_193590_3_.getString(p_193590_2_));
		if (optional.isPresent()) {
			return p_193590_0_.setValue(p_193590_1_, optional.get());
		} else {
			Treasure.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", p_193590_2_, p_193590_3_.getString(p_193590_2_), p_193590_4_.toString());
			return p_193590_0_;
		}
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
		public static Entity spawn(ServerWorld world, Entity entity, @Nullable ItemStack itemStack, @Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offsetFlag) {
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
		public static Entity spawn(ServerWorld world, Entity entity,@Nullable CompoundNBT compoundNbt, @Nullable ITextComponent textComponent, @Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offsetFlag) {
			Entity t = create(world, entity, compoundNbt, textComponent, player, pos, spawnReason, flag, offsetFlag);
			if (t != null) {
				if (t instanceof net.minecraft.entity.MobEntity && net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn((net.minecraft.entity.MobEntity) t, world, pos.getX(), pos.getY(), pos.getZ(), null, spawnReason)) return null;
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
		public static Entity create(ServerWorld world, Entity entity, @Nullable CompoundNBT compound, @Nullable ITextComponent textComponent, 
				@Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean flag, boolean offfsetFlag) {

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

				entity.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
				if (entity instanceof MobEntity) {
					MobEntity mobentity = (MobEntity)entity;
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
		protected static double getYOffset(IWorldReader world, BlockPos pos, boolean flag, AxisAlignedBB aabb) {
			AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos);
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
		protected static void updateCustomEntityTag(World world, @Nullable PlayerEntity player, @Nullable Entity entity, @Nullable CompoundNBT compoundNbt) {
			if (compoundNbt != null && compoundNbt.contains("EntityTag", 10)) {
				MinecraftServer minecraftserver = world.getServer();
				if (minecraftserver != null && entity != null) {
					if (world.isClientSide || !entity.onlyOpCanSetNbt() || player != null && minecraftserver.getPlayerList().isOp(player.getGameProfile())) {
						CompoundNBT compoundnbt = entity.saveWithoutId(new CompoundNBT());
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
