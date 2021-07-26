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
package com.someguyssoftware.treasure2.entity.monster;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.GravestoneBlock;
import com.someguyssoftware.treasure2.entity.TreasureEntities;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerTileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RestrictSunGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
public class BoundSoulEntity extends MonsterEntity {
	private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.defineId(BoundSoulEntity.class, DataSerializers.BLOCK_POS);
	private static final String HOME_POS_KEY = "HomePos";

	//	public static final int MOB_ID = 3;

	/**
	 * 
	 * @param type
	 * @param world
	 */
	public BoundSoulEntity (EntityType<BoundSoulEntity> type, World world) {
		super(type, world);
		this.setHomePos(BlockPos.ZERO);
	}

	/**
	 * 
	 * @param worldIn
	 */
	public BoundSoulEntity(World world) {
		this(TreasureEntities.BOUND_SOUL_ENTITY_TYPE, world);
	}

	/**
	 * 
	 * @param world
	 * @param homePos
	 */
	public BoundSoulEntity(World world, BlockPos homePos) {
		this(TreasureEntities.BOUND_SOUL_ENTITY_TYPE, world);
		this.setHomePos(homePos);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt(HOME_POS_KEY + "X", this.getHomePos().getX());
		compound.putInt(HOME_POS_KEY + "Y", this.getHomePos().getY());
		compound.putInt(HOME_POS_KEY + "Z", this.getHomePos().getZ());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		int i = compound.getInt(HOME_POS_KEY + "X");
		int j = compound.getInt(HOME_POS_KEY + "Y");
		int k = compound.getInt(HOME_POS_KEY + "Z");
		this.setHomePos(new BlockPos(i, j, k));
		super.readAdditionalSaveData(compound); 
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.2D));

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HOME_POS, BlockPos.ZERO);
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		MutableAttribute attr =  MobEntity.createMobAttributes(); // first get the mob attribs and then add this mob attribs

		return attr
				.add(Attributes.MAX_HEALTH, 22.0D)
				.add(Attributes.FOLLOW_RANGE, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.24D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 3.0D);
	}

	// determines if despawn-able
	//	public CreatureAttribute getMobType() {
	//		return CreatureAttribute.UNDEAD;
	//	}


	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void aiStep() {
		// burn in the sun
		boolean flag = this.isSunBurnTick();
		if (flag) {
			ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
			if (!itemstack.isEmpty()) {
				if (itemstack.isDamageableItem()) {
					itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
					if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
						this.broadcastBreakEvent(EquipmentSlotType.HEAD);
						this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
					}
				}
				flag = false;
			}

			if (flag) {
				this.setSecondsOnFire(8);
			}
		}

		// regeneration
		if (hasHome()) {
			TileEntity homeTileEntity = this.level.getBlockEntity(getHomePos());
			if (homeTileEntity == null || !(homeTileEntity instanceof GravestoneProximitySpawnerTileEntity)) {
				Treasure.LOGGER.debug("not a valid home"); // TEMP remove or spammed
				setHomePos(BlockPos.ZERO);
			} else {
				if (!this.dead && this.tickCount % 20 == 0 && this.getHealth() > 0
						&& this.getHealth() < this.getMaxHealth()) {
					this.setHealth(this.getHealth() + 1.0F);
				}
			}
		}
		super.aiStep();
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void dropFromLootTable(DamageSource source, boolean p_213354_2_) {
		super.dropFromLootTable(source, p_213354_2_);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasHome() {
		if (getHomePos() != null && !getHomePos().equals(BlockPos.ZERO)) {
			return true;
		}
		return false;
	}

	public void setHomePos(BlockPos pos) {
		Treasure.LOGGER.debug("setting bound soul home pos -> {}", pos.toShortString());
		this.entityData.set(HOME_POS, pos);
	}

	private BlockPos getHomePos() {
		return this.entityData.get(HOME_POS);
	}

	/**
	 * 
	 * @author Mark Gottschling on Jul 23, 2021
	 *
	 */
	static class GoHomeGoal extends MoveToBlockGoal {
		private final BoundSoulEntity soul;
		private final BlockPos homePos;

		private GoHomeGoal(BoundSoulEntity entity, double speed, BlockPos homePos) {
			super(entity, speed, 12);
			this.soul = entity;
			this.homePos = homePos;
			this.verticalSearchStart = -1;
		}

		@Override
		public boolean shouldRecalculatePath() {
			return this.tryTicks % 160 == 0;
		}

		@Override
		protected boolean isValidTarget(IWorldReader worldReader, BlockPos pos) {
			return pos == homePos;
		}
	}

	//	@Override
	//	public void onLivingUpdate() {
	//		super.onLivingUpdate();
	//
	//		/*
	//		 *  create a mist particle if on client.
	//		 *  this is slight change to the vanilla call EntityLiving#isServerWorld()
	//		 */
	//		if (WorldInfo.isClientSide(this.world)) {
	//			if (!this.isAIDisabled()) {
	//				spawnMist();
	//			}
	//		}

	//	}
	//
	//	@SideOnly(Side.CLIENT)
	//	private void spawnMist() {
	//		if (this.ticksExisted % 4 == 0) {
	//			Random random = new Random();
	//			AbstractMistParticle mistParticle = new MistParticle(world,
	//					this.getPosition().getX() + (random.nextFloat() * 0.5 - 0.25),
	//					this.getPosition().getY() + (random.nextFloat() * 0.25),
	//					this.getPosition().getZ() + (random.nextFloat() * 0.5 - 0.25), 0, 0, 0, null);
	//			// reduce the max age - don't want it too misty around entity
	//			mistParticle.setMaxAge(160);
	//			mistParticle.init();
	//			Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
	//		}
	//	}

}