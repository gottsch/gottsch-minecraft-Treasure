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
package mod.gottsch.forge.treasure2.core.entity.monster;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.GravestoneProximitySpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
public class BoundSoul extends Monster {
	private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(BoundSoul.class, EntityDataSerializers.BLOCK_POS);
	private static final String HOME_POS_KEY = "HomePos";


	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public BoundSoul(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.restrictTo(BlockPos.ZERO, 12);
	}

	/**
	 * 
	 * @param worldIn
	 */
//	public BoundSoul(Level level) {
//		this(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), level);
//	}
//	
//	/**
//	 * 
//	 * @param world
//	 * @param homePos
//	 */
//	public BoundSoul(Level level, BlockPos homePos) {
//		this(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), level);
//		this.setHomePos(homePos);
//	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt(HOME_POS_KEY + "X", this.getHomePos().getX());
		compound.putInt(HOME_POS_KEY + "Y", this.getHomePos().getY());
		compound.putInt(HOME_POS_KEY + "Z", this.getHomePos().getZ());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
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
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2,  new MoveTowardsRestrictionGoal(this, 1.2D));
		
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()				
				.add(Attributes.MAX_HEALTH, 22D)
				.add(Attributes.FOLLOW_RANGE, 20D)
				.add(Attributes.MOVEMENT_SPEED, 0.24F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 3.0D);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HOME_POS, BlockPos.ZERO);
	}
	
	@Override
	public void aiStep() {
		// set on fire if in sun
		boolean flag = this.isSunBurnTick();
		if (flag) {
			this.setSecondsOnFire(8);
		}
		
		// regeneration
		if (hasHome()) {
			BlockEntity homeBlockEntity = this.level().getBlockEntity(getHomePos());
			if (homeBlockEntity == null || !(homeBlockEntity instanceof GravestoneProximitySpawnerBlockEntity)) {
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
		Treasure.LOGGER.debug("setting bound soul home pos -> {}", pos.toString());
		this.entityData.set(HOME_POS, pos);
	}

	private BlockPos getHomePos() {
		return this.entityData.get(HOME_POS);
	}
}
